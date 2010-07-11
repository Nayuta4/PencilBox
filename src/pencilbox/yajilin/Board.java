package pencilbox.yajilin;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;

/**
 * �u���W�����v�ՖʃN���X
 */
public class Board extends BoardBase {
	
	public static final int UNKNOWN = 0;
	public static final int LINE = 1;
	public static final int NOLINE = -1; // GUI�ł͕s�g�p
	public static final int BLANK = -3;
	public static final int WHITE = -1;
	public static final int BLACK = -2;
	public static final int OUTER = -9;
	public static final int UNDECIDED_NUMBER = -4;

	private int[][] number;  // �}�X�̏��
	private int[][][] state; // �ӂ̏��

	private List<Link> linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		ArrayUtil.initArrayInt2(number, BLANK);
		state = new int[2][][];
		state[0] = new int[rows()][cols() - 1];
		state[1] = new int[rows() - 1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[0] = new Link[rows()][cols() - 1];
		link[1] = new Link[rows() - 1][cols()];
	}

	/**
	 * �����̍��W�����}�X���ǂ����B
	 * @param p ���W
	 * @return ���}�X�Ȃ� true ��Ԃ��B
	 */
	public boolean isBlack(Address p) {
		return isOn(p) && getNumber(p) == BLACK;
	}
	/**
	 * �w�肵���}�X�ɐ����}�X�C���}�X�C���}�X�C�󔒃}�X�̏�Ԃ�ݒ肷��
	 * @param r �s���W
	 * @param c ����W
	 * @param n �ݒ肷����
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}

	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * �w�肵���}�X�̏�Ԃ��擾����
	 * @param r �s���W
	 * @param c ����W
	 */
	public int getNumber(int r, int c) {
		return number[r][c]; 
	}

	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	/**
	 * �w�肵���}�X�̐��������݂̂�Ԃ��B�����}�X�ɑ΂��Ďg�p����B
	 * @param p ���W
	 * @return �}�X�̐�����Ԃ��B�����}�X�łȂ���� -1
	 */
	public int getArrowNumber(Address p) {
		int n = getNumber(p);
		return n >=0 ? n&15 : -1; 
	}
	
	/**
	 * �w�肵���}�X�ɏ�������̐�����ݒ肷��B
	 * @param p ���W
	 */
	public void setArrowNumber(Address p, int n) {
		setNumber(p, n);
	}
	/**
	 * �w�肵���}�X�̖��̕��p��Ԃ��B�����}�X�ɑ΂��Ďg�p����B
	 * @param p ���W
	 * @return ���̕��p�萔�Ԃ��B�����}�X�łȂ���� -1
	 */
	public int getArrowDirection(Address p) {
		int n = getNumber(p);
		return n >= 0 ? (n>>4) & 3 : -1; 
	}
	/**
	 * �w�肵���}�X�̖��̕��p��ݒ肷��B�����}�X�ɑ΂��Ďg�p����B
	 * @param p ���W
	 */
	public void setArrowDirection(Address p, int dir) {
		if (dir < 0 || dir > 3) return;
		int n = (getNumber(p) & ~(3 << 4)) | (dir << 4); 
		setNumber(p, n);
	}
	/**
	 * �����}�X��
	 * @param p ���W
	 * @return �����}�X���C���萔���}�X�ł���� true
	 */
	public boolean isNumber(Address p) {
		int n = getNumber(p);
		return n >= 0 || n == UNDECIDED_NUMBER;
	}

	/**
	 * �����W�̗�����2�}�X�����ꂩ�������}�X�Ȃ������}�X���ǂ���
	 * @param b
	 * @return �����W�̗�����2�}�X�����ꂩ�������}�X�Ȃ������}�X�ł���� true
	 */
	public boolean hasNumberOrBlack(SideAddress b) {
		for (int d = 0; d < 2; d++) {
			Address p = SideAddress.nextCellFromBorder(b, d);
			if (isNumber(p) || isBlack(p))
				return true;
		}
		return false;
	}
	
	/**
	 * �󔒃}�X�ɂ���
	 * @param p ���W
	 */
	public void eraseNumber(Address p) {
		setNumber(p, BLANK);
	}
	/**
	 * �����̓��͂��󂯕t����
	 * ���̐����Ɠ��������ł���Ζ��̌�����ς���B
	 * �����łȂ���ΐV������������t���̐�����ݒ肷��B
	 * 2����������͂���ɂ́H
	 * ��������������
	 * @param p �}�X���W
	 * @param n ���͂��ꂽ����
	 */
	public void enterNumber(Address p, int n) {
		int v = -99;
		if (n >= 0) {
			int t = getArrowDirection(p);
			if (getArrowNumber(p) == n) {
				t = (t+1)%4;
			} else {
			}
			v = getNumberValue(n, t);
		} else if (n == Board.UNDECIDED_NUMBER) {
			v = n;
		} else if (n == Board.BLANK) {
			v = n;
		}
		if (v != Board.BLANK) {
			eraseLinesAround(p);
		}
		changeNumber(p, v);
	}

	/**
	 * �����̐����ƌ����̃f�[�^������}�X�̓����f�[�^��̒l���擾����
	 * 0�ȏ�̐����ȊO���󂯂��Ƃ��͂��̂܂܂̒l��Ԃ�
	 * @param n  �����@
	 * @param d ����
	 * @return �l
	 */
	static int getNumberValue(int n, int d) {
		if (d >= 0 && d <= 3) {
			n &= ~(3 << 4);
			n |= (d << 4); 
		}
		return n;
	}
	/**
	 * @return Returns the state.
	 */
	int[][][] getState() {
		return state;
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
		if (isSideOn(d, r, c))
			state[d][r][c] = st;
	}

	public void setState(SideAddress pos, int st) {
		setState(pos.d(), pos.r(), pos.c(), st);
	}

	public Link getLink(SideAddress p) {
		if (isSideOn(p))
			return link[p.d()][p.r()][p.c()];
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

	public void setLink(SideAddress p, Link l) {
		link[p.d()][p.r()][p.c()] =  l;
	}
	/**
	 * �}�X����㉺���E4�����Ɉ�����Ă��������������
	 * �}�X�����}�X�␔���}�X�ɕύX���ꂽ�ꍇ�ɐ����������邽�߂Ɏg�p����
	 * @param p �}�X�̍��W
	 */
	void eraseLinesAround(Address p) {
		for (int d = 0; d < 4; d++) {
			SideAddress side = SideAddress.get(p, d);
			if (getState(side) == LINE) {
				changeState(side, UNKNOWN);
			}
		}
	}
	/**
	 * �Ֆʏ�Ԃ�ύX���C�A���h�D���X�i�[�ɕύX��ʒm����D
	 * @param p
	 * @param st
	 */
	public void changeNumber(Address p, int st) {
		int prev = getNumber(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		setNumber(p, st);
	}

	/**
	 * �ӂ̏�Ԃ��w�肵����ԂɕύX����
	 * �A���h�D���X�i�[�ɕύX��ʒm����
	 * @param p �Ӎ��W
	 * @param st �ύX��̏��
	 */
	public void changeState(SideAddress p, int st) {
		int prev = getState(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new BorderEditStep(p, prev, st));
		setState(p, st);
		if (prev == LINE) {
			cutLink(p);
		}
		if (st == LINE) {
			connectLink(p);
		}
	}

	public void undo(AbstractStep step) {
		if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeState(s.getPos(), s.getBefore());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeState(s.getPos(), s.getAfter());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getAfter());
		}
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		for (Address p : cellAddrs()) {
			if (!isNumber(p)) {
				setNumber(p, BLANK);
			}
		}
		initBoard();
	}
	
	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getNumber(p) == WHITE) {
				setNumber(p, BLANK);
			}
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
	 * ���̃}�X�̏㉺���E�̗אڂS�}�X�ɍ��}�X�����邩�ǂ����𒲂ׂ�
	 * @param p
	 * @return �㉺���E�ɍ��}�X���ЂƂł������ true
	 */
	boolean isBlock(Address p) {
		for (int d=0; d<4; d++) {
			Address p1 = p.nextCell(d);
			if (isBlack(p1))
				return true;
		}
		return false;
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

	private int checkLinks() {
		int result = 0;
		for (Address p : cellAddrs()) {
			int l = countLine(p);
			if (l > 2) {
				result |= 1;
			} else if ( l == 1 ) {
				result |= 2; 
			}
			if (isBlack(p) && (l > 0))
				result |= 64;
			if (!isNumber(p) && !isBlack(p) && (l == 0))
				result |= 8;
		}
		if (linkList.size() > 1)
			result |= 4;
		return result;
	}
	
	private int checkArrows() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getNumber(p) >= 0) {
				result |= checkArrow(p);
			}
			if (isBlack(p)) {
				if (isBlock(p)) {
					result |= 32;
				}
			}
		}
		return result;
	}
	
	int checkArrow(Address p0) {
		int result = 0;
		int blackCount = 0;
		int dir = getArrowDirection(p0);
		int number = getArrowNumber(p0);
		Address p = Address.nextCell(p0, dir);
		while (isOn(p)) {
			if (isBlack(p))
				blackCount++;
			p = Address.nextCell(p, dir);
		}
		if (number == blackCount)
			result = 0; // �����ƍ��}�X������v����
		else
			result = 16; // �����ƍ��}�X������v���Ȃ�
		return result;
	}

	public int checkAnswerCode() {
		int result = 0;
		result |= checkLinks();
		result |= checkArrows();
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE; 
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(Messages.getString("yajilin.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("yajilin.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("yajilin.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("yajilin.AnswerCheckMessage4")); //$NON-NLS-1$
		if ((result & 64) == 64)
			message.append(Messages.getString("yajilin.AnswerCheckMessage7")); //$NON-NLS-1$
		if ((result & 32) == 32)
			message.append(Messages.getString("yajilin.AnswerCheckMessage6")); //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("yajilin.AnswerCheckMessage5")); //$NON-NLS-1$
		return message.toString();
	}

}
