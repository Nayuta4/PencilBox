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

	/**
	 * �ӏ�Ԃ̎擾�B�}�X�i�X�������ł͓_�̈ʒu�j�ƌ����ō��W�w�肷��B
	 * @param pos
	 * @param d
	 * @return
	 */
	public int getStateJ(Address pos, int d) {
		return getState(SideAddress.get(pos, d));
	}
	/**
	 * �ӏ�Ԃ̐ݒ�B�}�X�i�X�������ł͓_�̈ʒu�j�ƌ����ō��W�w�肷��B
	 * @param pos
	 * @param d
	 * @param st
	 */
	public void setStateJ(Address pos, int d, int st) {
		setState(SideAddress.get(pos, d), st);
	}

	public boolean isLine(int d, int r, int c) {
		if (isSideOn(d, r, c))
			return state[d][r][c] == LINE;
		else 
			return false;
	}

	public Link getLink(int d, int r, int c) {
		if (isSideOn(d, r, c))
			return link[d][r][c];
		else
			return null;
	}
	public Link getLink(SideAddress pos) {
		return link[pos.d()][pos.r()][pos.c()];
	}
	/**
	 * ���̃}�X���܂� Link ��Ԃ�
	 */
	public Link getLink(int r, int c) {
		Link link;
		link = getLink(VERT, r, c - 1);
		if (link != null) return link;
		link = getLink(VERT, r, c);
		if (link != null) return link;
		link = getLink(HORIZ, r - 1, c);
		if (link != null) return link;
		link = getLink(HORIZ, r, c);
		if (link != null) return link;
		return null;
	}
	public void setLink(int d, int r, int c, Link l) {
		link[d][r][c] =  l;
	}
	public void setLink(SideAddress pos, Link l) {
		link[pos.d()][pos.r()][pos.c()] =  l;
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
	 * @param d �c������
	 * @param r �s���W
	 * @param c ����W
	 * @param st �ύX��̏��
	 */
	public void changeState(int d, int r, int c, int st) {
		int previousState = getState(d,r,c);
		setState(d,r,c,st);
		if (previousState == LINE) {
			cutLink(d,r,c);
		}
		if (st == LINE) {
			connectLink(d,r,c);
		}
	}

	/**
	 * �ӂ̏�Ԃ��w�肵����ԂɕύX����
	 * �A���h�D���X�i�[�ɕύX��ʒm����
	 * @param pos �Ӎ��W
	 * @param st �ύX��̏��
	 */
	public void changeStateA(SideAddress pos, int st) {
		fireUndoableEditUpdate(
			new BorderEditStep(pos.d(), pos.r(), pos.c(), getState(pos), st));
		changeState(pos.d(), pos.r(), pos.c(), st);
	}

	public void undo(AbstractStep step) {
		BorderEditStep s = (BorderEditStep) step;
		changeState(s.getDirection(), s.getRow(), s.getCol(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		BorderEditStep s = (BorderEditStep) step;
		changeState(s.getDirection(), s.getRow(), s.getCol(), s.getAfter());
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		initBoard();
	}
	
	public void trimAnswer() {
		for (int d=0; d<=1; d++)
			for (int r=0; r<rows(); r++) {
				for (int c=0; c<cols(); c++) {
					if (getState(d, r, c) == NOLINE) 
						setState(d, r, c, UNKNOWN);
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
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				initLink(r, c);
			}
		}
	}
	
	/**
	 * ����}�X���܂� Link �̏�����
	 * link[][][] �͏�������Ă�����̂Ƃ���
	 * @param r Link�������̋N�_�}�X�̍s���W
	 * @param c Link�������̋N�_�}�X�̗���W
	 */	
	void initLink (int r, int c) {
		initializingLink = new Link();
		initLink1(VERT , r  , c-1);
		initLink1(VERT , r  , c  );
		initLink1(HORIZ, r-1, c  );
		initLink1(HORIZ, r  , c  );
		if (!initializingLink.isEmpty())
			linkList.add(initializingLink);
	}
	private void initLink1(int d, int r, int c) {
		if (!isSideOn(d,r,c)) return;
		if (!isLine(d,r,c)) return;
		if (getLink(d,r,c) != null) return;
		initializingLink.add(d,r,c);
		setLink(d, r, c, initializingLink);
		if (d==VERT) {
			initLink1(VERT , r  , c-1);
			initLink1(VERT , r  , c+1);
			initLink1(HORIZ, r-1, c  );
			initLink1(HORIZ, r-1, c+1);
			initLink1(HORIZ, r  , c  );
			initLink1(HORIZ, r  , c+1);
		}
		if (d==HORIZ) {
			initLink1(HORIZ, r-1, c  );
			initLink1(HORIZ, r+1, c  );
			initLink1(VERT , r  , c-1);
			initLink1(VERT , r+1, c-1);
			initLink1(VERT , r  , c  );
			initLink1(VERT , r+1, c  );
		}
	}
	/**
	 * Link ����
	 */	
	void connectLink(int d, int r, int c) {
		Link newLink = null;
		Link link1 = null;
		Link link2 = null;
		if (d==VERT) {
			link1 = getLink(r,c);
			link2 = getLink(r,c+1);
		} else if (d==HORIZ) {
			link1 = getLink(r,c);
			link2 = getLink(r+1,c);
		}
		if (link1==null && link2 == null) {
			newLink = new Link();
			linkList.add(newLink);
		} else if (link1==null && link2!=null) {
			newLink = link2;
		} else if (link1!=null && link2==null) {
			newLink = link1;
		} else if (link1==link2) {
			newLink = link1;
		} else {
			if (link1.size() >= link2.size()) {
				newLink = link1;
				newLink.addAll(link2);
				for(SideAddress joint : link2) {
					setLink(joint, newLink);
				}
				linkList.remove(link2);
			}
			else {
				newLink = link2;
				newLink.addAll(link1);
				for(SideAddress joint : link1) {
					setLink(joint, newLink);
				}
				linkList.remove(link1);
			}
		}
		newLink.add(d,r,c);
		setLink(d,r,c, newLink);
	}

	/**
	 * Link �ؒf
	 */	
	void cutLink(int d, int r, int c) {
		Link oldLink = getLink(d,r,c);
		Link longerLink = null;
		for (SideAddress joint : oldLink) {
			setLink(joint, null);
		}
		linkList.remove(oldLink);
		if (d==VERT) {
			initLink(r, c);
			longerLink = initializingLink;
			initLink(r  , c+1);
			if (initializingLink.size() > longerLink.size())
				longerLink = initializingLink;
		}
		else if (d==HORIZ) {
			initLink(r, c);
			longerLink = initializingLink;
			initLink(r+1, c);
			if (initializingLink.size() > longerLink.size())
				longerLink = initializingLink;
		}
		longerLink.setId(oldLink.getId());
	}

	/**
	 * �}�X�̏㉺���E4�����̂����C���ݐ���������Ă��鐔��Ԃ�
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return �}�X�̏㉺���E�Ɉ�����Ă�����̐�
	 */
	public int countLine(int r, int c) {
		int no = 0;
		if (r < rows() - 1 && isLine(HORIZ, r, c))
			no++;
		if (c < cols() - 1 && isLine(VERT, r, c))
			no++;
		if (r > 0 && isLine(HORIZ, r - 1, c))
			no++;
		if (c > 0 && isLine(VERT, r, c - 1))
			no++;
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
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				int l = countLine(r,c);
				if (l > 2) {
					result |= 1;
				} else if ( l == 1 ) {
					result |= 2; 
				}

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
