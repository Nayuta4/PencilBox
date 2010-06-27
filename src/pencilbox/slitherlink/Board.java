package pencilbox.slitherlink;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.resource.Messages;
import pencilbox.slalom.Link;
import pencilbox.util.ArrayUtil;

/**
 * �u�X���U�[�����N�v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int HORIZ = Direction.HORIZ; 
	static final int VERT = Direction.VERT;
	
	static final int UNKNOWN = 0;
	static final int LINE = 1;
	static final int NOLINE = -1;
	static final int NONUMBER = -1;
	static final int OUTER = -9;
	static final int UNDECIDED_NUMBER = 5;

	private int[][] number;
	private int[][][] state;

	private List<Link> linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		number = new int[rows()-1][cols()-1];
		ArrayUtil.initArrayInt2(number, NONUMBER);
		state = new int[2][][];
		state[0] = new int[rows()][cols() - 1];
		state[1] = new int[rows() - 1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[VERT] = new Link[rows()][cols() - 1];
		link[HORIZ] = new Link[rows() - 1][cols()];
	}
	
	/**
	 * �w�肵�����W����萔�����W�ŔՖʏ�ɂ��邩
	 * @param r �s���W
	 * @param c ����W
	 * @return �Ֆʏ�Ȃ� true
	 */
	public boolean isNumberOn(int r, int c) {
		return (r >= 0 && r < rows()-1 && c >= 0 && c < cols()-1);
	}

	public boolean isNumberOn(Address pos) {
		return isNumberOn(pos.r(), pos.c());
	}
	/**
	 * �w�肵�����W�̐������擾����
	 * @param r �����ʒu���W�ł̍s���W
	 * @param c �����ʒu���W�ł̗���W
	 * @return ���̍��W�̐���
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}

	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}

	/**
	 * �w�肵�����W�ɐ����Ȃ������萔���͂��邩
	 * @param r �����ʒu���W�ł̍s���W
	 * @param c �����ʒu���W�ł̗���W
	 * @return �����������true0
	 */
	public boolean isNumber(int r, int c) {
		return number[r][c] >=0 && number[r][c] <= 5;
	}
	
	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
	}
	/**
	 * �w�肵�����W�ɐ�����ݒ肷��
	 * @param r �����ʒu���W�ł̍s���W
	 * @param c �����ʒu���W�ł̗���W
	 * @param n �ݒ肷�鐔��
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}

	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * @return Returns the state.
	 */
	int[][][] getState() {
		return state;
	}
	/**
	 * @return Returns the  number.
	 */
	int[][] getNumber() {
		return number;
	}
	/**
	 * �ӏ�Ԃ̎擾
	 * @param d
	 * @param r
	 * @param c
	 * @return �ӂ̏�Ԃ�Ԃ�
	 */
	public int getState(int d, int r, int c) {
		if (isSideOn(d,r,c))
			return state[d][r][c];
		else
			return OUTER;
	}

	public int getState(SideAddress pos) {
		return getState(pos.d(), pos.r(), pos.c());
	}
	/**
	 * �ӏ�Ԃ̐ݒ�
	 * @param d
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setState(int d, int r, int c, int st) {
		state[d][r][c] = st;
	}

	public void setState(SideAddress pos, int st) {
		setState(pos.d(), pos.r(), pos.c(), st);
	}

	public boolean isLine(int d, int r, int c) {
		if (isSideOn(d, r, c))
			return state[d][r][c] == LINE;
		else 
			return false;
	}

	public Link getLink(SideAddress pos) {
		if (isSideOn(pos))
			return link[pos.d()][pos.r()][pos.c()];
		else
			return null;
	}
	/**
	 * ���̃}�X���܂� Link ��Ԃ�
	 */
	public Link getLink(Address p) {
		for (int d = 0; d < 4; d++) {
			Link link = getLink(SideAddress.get(p, d));
			if (link != null)
				return link;
		}
		return null;
	}

	public void setLink(SideAddress pos, Link l) {
		link[pos.d()][pos.r()][pos.c()] = l;
	}
	/**
	 * �Ֆʂ� Link ���������邩
	 * @return�@Link ����������Ȃ� true
	 */
	public boolean hasMultipleLinks() {
		return linkList.size() > 1;
	}

	/**
	 * �ӂ̏�Ԃ��w�肵����ԂɕύX����
	 * @param p �Ӎ��W
	 * @param st �ύX��̏��
	 */
	public void changeState(SideAddress p, int st) {
		if (isRecordUndo())
			fireUndoableEditUpdate(new BorderEditStep(p, getState(p), st));
		int previousState = getState(p);
		setState(p,st);
		if (previousState == LINE) {
			cutLink(p);
		}
		if (st == LINE) {
			connectLink(p);
		}
	}

	/**
	 * �ӂ̏�Ԃ��w�肵����ԂɕύX����
	 * �A���h�D���X�i�[�ɕύX��ʒm����
	 * @param pos �Ӎ��W
	 * @param st �ύX��̏��
	 */

	public void undo(AbstractStep step) {
		BorderEditStep s = (BorderEditStep) step;
		changeState(s.getPos(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		BorderEditStep s = (BorderEditStep) step;
		changeState(s.getPos(), s.getAfter());
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		initBoard();
	}

	public void trimAnswer() {
		for (SideAddress p : borderAddrs()) {
			if (getState(p) == NOLINE) 
				setState(p, UNKNOWN);
		}
	}

	public void initBoard() {
		initLinks();
	}

	void initLinks() {
		Link.resetId();
		linkList.clear();
		ArrayUtil.initArrayObject2(link[0],null);
		ArrayUtil.initArrayObject2(link[1],null);
		for (Address p : cellAddrs()) {
			initLink(p);
		}
	}

	/**
	 * ����}�X���܂� Link �̏�����
	 * link[][][] �͏�������Ă�����̂Ƃ���
	 * @param p Link�������̋N�_�}�X�̍��W
	 */
	void initLink(Address p) {
		initializingLink = new Link();
		for (int d = 0; d < 4; d++) {
			initLink1(SideAddress.get(p, d));
		}
		if (!initializingLink.isEmpty())
			linkList.add(initializingLink);
	}

	private void initLink1(SideAddress p) {
		if (!isSideOn(p))
			return;
		if (getState(p) != LINE)
			return;
		if (getLink(p) != null)
			return;
		initializingLink.add(p);
		setLink(p, initializingLink);
		for (int d = 0; d < 6; d++) {
			initLink1(SideAddress.nextBorder(p, d));
		}
	}
	/**
	 * Link ����
	 */	
	void connectLink(SideAddress p) {
		Link newLink = new Link();
		for (int d = 0; d < 2; d++) {
			Link link = getLink(SideAddress.nextCellFromBorder(p, d));
			if (link != null && (link.size() > newLink.size()))
				newLink = link;
		}
		if (newLink.isEmpty()) {
			linkList.add(newLink);
		}
		for (int d = 0; d < 2; d++) {
			Link link = getLink(SideAddress.nextCellFromBorder(p, d));
			if (link != null && link != newLink) {
				for(SideAddress b : link) {
					setLink(b, newLink);
					newLink.add(b);
				}
				linkList.remove(link);
			}
		}
		newLink.add(p);
		setLink(p, newLink);
	}
	/**
	 * Link �ؒf
	 */
	void cutLink(SideAddress p) {
		Link oldLink = getLink(p);
		Link longerLink = new Link();
		for (SideAddress b : oldLink) {
			setLink(b, null);
		}
		linkList.remove(oldLink);
		for (int d = 0; d < 2; d++) {
			Address p1 = SideAddress.nextCellFromBorder(p, d);
			initLink(p1);
			if (initializingLink.size() > longerLink.size())
				longerLink = initializingLink;
		}
		longerLink.setId(oldLink.getId());
	}

	/**
	 * �}�X�̏㉺���E4�����̂����C���ݐ���������Ă��鐔��Ԃ�
	 * @param p �}�X�̍��W
	 * @return �}�X�̏㉺���E�Ɉ�����Ă�����̐�
	 */
	public int countLine(Address p) {
		int no = 0;
		for (int d = 0; d < 4; d++) {
			SideAddress b = SideAddress.get(p, d);
			if (isSideOn(b) && getState(b) == LINE)
				no ++;
		}
		return no;
	}

	/**
	 * �����̂S�ӂ̐��̐��𐔂���
	 * @param r �s���W
	 * @param c ����W
	 * @return 	�����̂S�ӂ̐��̐��𐔂���
	 */
	public int lineAround(int r, int c){
		int nl = 0;
		if (isLine(VERT,r,c))
			nl++;
		if (isLine(HORIZ,r,c))
			nl++;
		if (isLine(VERT,r + 1,c))
			nl++;
		if (isLine(HORIZ,r,c + 1))
			nl++;
		return nl;		
	}

	public int checkAnswerCode() {
		int result = 0;
		int nline = 0;
		int number = 0;
		for (Address p : cellAddrs()) {
			int l = countLine(p);
			if (l > 2) {
				result |= 1;
			} else if ( l == 1 ) {
				result |= 2;
			}
		}
		for (int r=0; r<rows()-1; r++) {
			for (int c=0; c<cols()-1; c++) {
				number = getNumber(r,c);
				if (number>=0 && number<=4) {
					nline = lineAround(r,c);
					if (nline > number)
						result |= 4;
					else if (nline < number)
						result |= 32;   
				}
			}
		}
		if (linkList.size() > 1)
			result |= 16;
		else if (linkList.size() == 0)
			result |= 128;
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE; 
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(Messages.getString("slitherlink.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("slitherlink.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("slitherlink.AnswerCheckMessage5")); //$NON-NLS-1$
		if ((result & 128) == 128)
			message.append(Messages.getString("slitherlink.AnswerCheckMessage8")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("slitherlink.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 32) == 32)
			message.append(Messages.getString("slitherlink.AnswerCheckMessage6")); //$NON-NLS-1$
		return message.toString();
	}
}
