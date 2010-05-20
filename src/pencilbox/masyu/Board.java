package pencilbox.masyu;

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
 * �u�܂���v�ՖʃN���X
 */
public class Board extends BoardBase  {
	
	static final int HORIZ = Direction.HORIZ;
	static final int VERT = Direction.VERT;
	static final int UP = Direction.UP;
	static final int DN = Direction.DN;
	static final int LT = Direction.LT;
	static final int RT = Direction.RT;

	static final int UNKNOWN = 0;
	static final int LINE = 1;
	static final int NOLINE = -1;
	static final int OUTER = -9;
	static final int NO_PEARL = 0;
	static final int WHITE_PEARL = 1;
	static final int BLACK_PEARL = 2;
	static final int GRAY_PEARL = 3;

	private int[][] number;
	private int[][][] state;

	private List<Link> linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		state = new int[2][][];
		state[0] = new int[rows()][cols()- 1];
		state[1] = new int[rows()- 1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[VERT] = new Link[rows()][cols() - 1];
		link[HORIZ] = new Link[rows() - 1][cols()];
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

	/**
	 * @return Returns the state.
	 */
	int[][][] getState() {
		return state;
	}
	/**
	 * @return Returns the number.
	 */
	int[][] getNumber() {
		return number;
	}
	/**
	 * �ۂ̐ݒ�
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setNumber(int r, int c, int st) {
		number[r][c] = st;
	}
	
	public void setNumber(Address pos, int st) {
		setNumber(pos.r(), pos.c(), st);
	}
	/**
	 * �ۂ̎擾
	 * @param r
	 * @param c
	 * @return �ۈ�̎�ނ�Ԃ�
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}
	
	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}

	public boolean isNumber(int r, int c) {
		return (number[r][c] >= 1 && number[r][c] <= 3);
	}
	
	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
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
//		if (isSideOn(d,r,c))
			state[d][r][c] = st;
	}
	
	public void setState(SideAddress pos, int st) {
		setState(pos.d(), pos.r(), pos.c(), st);
	}

	public boolean isLine(int d, int r, int c) {
		if (!isSideOn(d,r,c))
			return false;
		return
		state[d][r][c] == LINE;
	}
	public boolean isNoLine(int d, int r, int c) {
		if (!isSideOn(d,r,c))
			return true;
		return
		state[d][r][c] == NOLINE;
	}
	/**
	 * �}�X���� direction �����ɐ��͂��邩
	 */
	public boolean isLineJ(int r, int c, int direction) {
		switch (direction) {
			case UP: return isLine(HORIZ,r-1,c);
			case LT: return isLine(VERT, r,c-1);
			case DN: return isLine(HORIZ,r,c);
			case RT: return isLine(VERT, r,c);
			default: return false;
		}
	}

	public Link getLink(int d, int r, int c) {
		if (isSideOn(d, r, c) )
			return link[d][r][c];
		else return null;
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
	 * �A���h�D���X�i�[�ɕύX��ʒm����
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

	public void undo(AbstractStep step) {
		BorderEditStep s = (BorderEditStep)step;
		changeState(s.getPos(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		BorderEditStep s = (BorderEditStep)step;
		changeState(s.getPos(), s.getAfter());
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
	void connectLink(SideAddress p) {
		int d=p.d(), r=p.r(), c=p.c();
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
	void cutLink(SideAddress p) {
		int d=p.d(), r=p.r(), c=p.c();
		Link oldLink = getLink(p);
		Link longerLink = null;
		for (SideAddress joint : oldLink) {
			setLink(joint, null);
		}
		linkList.remove(oldLink);
		if (d==VERT) {
			initLink(r, c);
			longerLink = initializingLink;
			initLink(r, c+1);
			if (initializingLink.size() > longerLink.size()) longerLink = initializingLink;
		}
		else if (d==HORIZ) {
			initLink(r, c);
			longerLink = initializingLink;
			initLink(r+1, c);
			if (initializingLink.size() > longerLink.size()) longerLink = initializingLink;
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
		if (isLineJ(r,c,UP)) no++;
		if (isLineJ(r,c,LT)) no++;
		if (isLineJ(r,c,DN)) no++;
		if (isLineJ(r,c,RT)) no++;
		return no;
	}
	/**
	 * ���^��}�X��̐��̏�Ԃ𒲂ׂ�
	 * ���̂Q��ނ��ԈႢ�Ɣ��肷��
	 * ����
	 *   �� �Ȃ���ꍇ
	 * �����������@���ׂŒ��i����ꍇ
	 * @param r
	 * @param c
	 * @return
	 * �����ʉ߂��ė��ׂ̂����ꂩ�ŋȂ����Ă���� +2 ��
	 * �����ʉ߂��Ă��āC�ԈႢ���Ȃ���� +1 ��
	 * �����ʉ߂��Ă��āC�ԈႢ������� -1 ��
	 * �����ʉ߂��Ă��Ȃ���� 0 ��Ԃ�
	 */
	int checkWhitePearl(int r, int c) {
		int l = countLine(r,c);
		if (l > 2)
			return -1; 
		else if (l < 2)
			return 0; 

		if (isLineJ(r,c,UP) && isLineJ(r,c,RT)) return -1;
		if (isLineJ(r,c,UP) && isLineJ(r,c,LT)) return -1;
		if (isLineJ(r,c,DN) && isLineJ(r,c,RT)) return -1;
		if (isLineJ(r,c,DN) && isLineJ(r,c,LT)) return -1;
		if (isLineJ(r,c,UP) && isLineJ(r,c,DN)) {
			if (isLineJ(r-1,c,UP) && isLineJ(r+1,c,DN)) return -1;
			if (isLineJ(r-1,c,RT)) return 2;
			if (isLineJ(r-1,c,LT)) return 2;
			if (isLineJ(r+1,c,RT)) return 2;
			if (isLineJ(r+1,c,LT)) return 2;
			return 1;
		}
		if (isLineJ(r,c,LT) &&  isLineJ(r,c,RT)) {
			if (isLineJ(r,c-1,LT) && isLineJ(r,c+1,RT)) return -1;
			if (isLineJ(r,c-1,UP)) return 2;
			if (isLineJ(r,c-1,DN)) return 2;
			if (isLineJ(r,c+1,UP)) return 2;
			if (isLineJ(r,c+1,DN)) return 2;
			return 1;
		}
		return -9;
	}
	
	/**
	 * ���^��}�X��̐��̏�Ԃ𒲂ׂ�
	 * ���̂Q��ނ��ԈႢ�Ɣ��肷��
	 * ������  ���i����ꍇ
	 * ����    �ׂ̃}�X�ŋȂ���ꍇ
	 * @param r
	 * @param c
	 * @return
	 * �����Ȃ����ė����Œ��i���Ă���� +2 ��
	 * �����ʉ߂��Ă��āC�ԈႢ���Ȃ���� +1 ��
	 * �����ʉ߂��Ă��āC�ԈႢ������� -1 ��
	 * �����ʉ߂��Ă��Ȃ���� 0 ��Ԃ�
	 */
	int checkBlackPearl(int r, int c) {
		int success = 0;

		int l = countLine(r,c);
		if (l > 2)
			return -1; 
		else if (l < 2)
			return 0; 

		if (isLineJ(r,c,UP) && isLineJ(r,c,DN)) return -1;
		if (isLineJ(r,c,RT) && isLineJ(r,c,LT)) return -1;

		if (isLineJ(r,c,UP)) {
			if (isLineJ(r-1,c,LT)) return -1;
			if (isLineJ(r-1,c,RT)) return -1;
			if (isLineJ(r-1,c,UP)) success |= 2;
			else success |= 1;
		}
		if (isLineJ(r,c,DN)) {
			if (isLineJ(r+1,c,LT)) return -1;
			if (isLineJ(r+1,c,RT)) return -1;
			if (isLineJ(r+1,c,DN)) success |= 2;
			else success |= 1;
		} 
		if (isLineJ(r,c,LT)) {
			if (isLineJ(r,c-1,UP)) return -1;
			if (isLineJ(r,c-1,DN)) return -1;
			if (isLineJ(r,c-1,LT)) success |= 8;
			else success |= 4;
		}
		if (isLineJ(r,c,RT)) {
			if (isLineJ(r,c+1,UP)) return -1;
			if (isLineJ(r,c+1,DN)) return -1;
			if (isLineJ(r,c+1,RT)) success |= 8;
			else success |= 4;
		}
		if (success == 10) return 2;
		else if (success > 0) return 1;
		return -9;
	}

	public int checkAnswerCode() {
		int result = 0;
		int p = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				int l = countLine(r,c);
				if (l > 2) {
					result |= 1;
				} else if ( l == 1 ) {
					result |= 2; 
				}
				int pearl = getNumber(r,c);
				if (pearl == WHITE_PEARL) {
					p = checkWhitePearl(r,c);
						if (p == -1)
							result |= 4;
						else if (p == 0)
							result |= 32;   
				}
				if (pearl == BLACK_PEARL) {
					p = checkBlackPearl(r,c);
					if (p == -1)
						result |= 8; 
					else if (p == 0)
						result |= 64;   
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
			return BoardBase.COMPLETE_MESSAGE; 
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(Messages.getString("masyu.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("masyu.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("masyu.AnswerCheckMessage5")); //$NON-NLS-1$
		if ((result & 128) == 128)
			message.append(Messages.getString("masyu.AnswerCheckMessage8")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("masyu.AnswerCheckMessage3")); //$NON-NLS-1$
		else if ((result & 32) == 32)
			message.append(Messages.getString("masyu.AnswerCheckMessage6")); //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("masyu.AnswerCheckMessage4")); //$NON-NLS-1$
		else if ((result & 64) == 64)
			message.append(Messages.getString("masyu.AnswerCheckMessage7")); //$NON-NLS-1$
		return message.toString();
	}
}
