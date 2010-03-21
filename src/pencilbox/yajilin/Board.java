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
	
	private static final int HORIZ = Direction.HORIZ;
	private static final int VERT = Direction.VERT;
	
	static final int UNKNOWN = 0;
	static final int LINE = 1;
	static final int NOLINE = -1; // GUI�ł͕s�g�p
	static final int BLANK = -3;
	static final int WHITE = -1;
	static final int BLACK = -2;
	static final int OUTER = -9;
	static final int UNDECIDED_NUMBER = -4;

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
		state[VERT] = new int[rows()][cols() - 1];
		state[HORIZ] = new int[rows() - 1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[VERT] = new Link[rows()][cols() - 1];
		link[HORIZ] = new Link[rows() - 1][cols()];
	}
	
	/**
	 * �����̍��W�����}�X���ǂ����B
	 * @param r �s���W
	 * @param c ����W
	 * @return ���}�X�Ȃ� true ��Ԃ��B
	 */
	public boolean isBlack(int r, int c) {
		return isOn(r,c) && number[r][c] == BLACK;
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
	 * @param r �s���W
	 * @param c ����W
	 * @return �}�X�̐�����Ԃ��B�����}�X�łȂ���� -1
	 */
	public int getArrowNumber(int r, int c) {
		return number[r][c] >=0 ? number[r][c] & 15 : -1; 
	}
	
	public int getArrowNumber(Address pos) {
		return getArrowNumber(pos.r(), pos.c());
	}
	/**
	 * �w�肵���}�X�ɏ�������̐�����ݒ肷��B
	 * @param r �s���W
	 * @param c ����W
	 */
	public void setArrowNumber(int r, int c, int n) {
		number[r][c] = n; 
	}
	
	public void setArrowNumber(Address pos, int n) {
		setArrowNumber(pos.r(), pos.c(), n);
	}
	/**
	 * �w�肵���}�X�̖��̕��p��Ԃ��B�����}�X�ɑ΂��Ďg�p����B
	 * @param r �s���W
	 * @param c ����W
	 * @return ���̕��p�萔�Ԃ��B�����}�X�łȂ���� -1
	 */
	public int getArrowDirection(int r, int c) {
		return number[r][c] >= 0 ? (number[r][c]>>4) & 3 : -1; 
	}
	
	public int getArrowDirection(Address pos) {
		return getArrowDirection(pos.r(), pos.c());
	}
	/**
	 * �w�肵���}�X�̖��̕��p��ݒ肷��B�����}�X�ɑ΂��Ďg�p����B
	 * @param r �s���W
	 * @param c ����W
	 */
	public void setArrowDirection(int r, int c, int dir) {
		if (dir < 0 || dir > 3) return;
		number[r][c] &= ~(3 << 4);
		number[r][c] |= (dir << 4); 
	}
	
	public void setArrowDirection(Address pos, int dir) {
		setArrowDirection(pos.r(), pos.c(), dir);
	}
	/**
	 * �w�肵���}�X�̖��̌������@�と���������E���� �̏��ɕύX����B�����}�X�ɑ΂��Ďg�p����B
	 * @param pos �}�X���W
	 */
	public void toggleArrowDirection(Address pos) {
		int t = getArrowDirection(pos);
		if (t<0) return;
		t = (t+1)%4;
		setArrowDirection(pos, t);
	}
	/**
	 * �����}�X��
	 * @param r �s���W
	 * @param c ����W
	 * @return �����}�X���C���萔���}�X�ł���� true
	 */
	public boolean isNumber(int r, int c) {
		return number[r][c] >= 0 || number[r][c] == UNDECIDED_NUMBER;
	}
	
	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
	}
	/**
	 * �����W�̗�����2�}�X�����ꂩ�������}�X�Ȃ������}�X���ǂ���
	 * @param d
	 * @param r
	 * @param c
	 * @return �����W�̗�����2�}�X�����ꂩ�������}�X�Ȃ������}�X�ł���� true
	 */
	public boolean hasNumberOrBlack(int d, int r, int c) {
		if (d == VERT)
			return isNumber(r, c) || isNumber(r, c+1) || isBlack(r, c) || isBlack(r, c+1);
		else if (d == HORIZ)
			return isNumber(r, c) || isNumber(r+1, c) || isBlack(r, c) || isBlack(r+1, c);
		return false;
	}

	public boolean hasNumberOrBlack(SideAddress side) {
		return hasNumberOrBlack(side.d(), side.r(), side.c());
	}

	/**
	 * �󔒃}�X�ɂ���
	 * @param r �s���W
	 * @param c ����W
	 */
	public void eraseNumber(int r, int c) {
		number[r][c] = BLANK;
	}
	
	public void eraseNumber(Address pos) {
		eraseNumber(pos.r(), pos.c());
	}
	/**
	 * �����̓��͂��󂯕t����
	 * ���̐����Ɠ��������ł���Ζ��̌�����ς���B
	 * �����łȂ���ΐV������������t���̐�����ݒ肷��B
	 * @param pos �}�X���W
	 * @param n ���͂��ꂽ����
	 */
	public void enterNumber(Address pos, int n) {
		if (getArrowNumber(pos) == n)
			toggleArrowDirection(pos);
		else {
			eraseLinesAround(pos);
			setArrowNumber(pos, n);
		}
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
	 * �ӏ�Ԃ̎擾�B�}�X�ƌ����ō��W�w�肷��B
	 * @param pos
	 * @param d
	 * @return
	 */
	public int getStateJ(Address pos, int d) {
		return getState(SideAddress.get(pos, d));
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

	/**
	 * �ӏ�Ԃ̐ݒ�B�}�X�ƌ����ō��W�w�肷��B
	 * @param pos
	 * @param d
	 * @param st
	 */
	public void setStateJ(Address pos, int d, int st) {
		setState(SideAddress.get(pos, d), st);
	}

	public boolean isLine(int d, int r, int c) {
		if (!isSideOn(d,r,c))
			return false;
		return
		state[d][r][c] == LINE;
	}

	public Link getLink(int d, int r, int c) {
		if (isSideOn(d,r,c) ) return link[d][r][c];
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
	 * �}�X����㉺���E4�����Ɉ�����Ă��������������
	 * �}�X�����}�X�␔���}�X�ɕύX���ꂽ�ꍇ�ɐ����������邽�߂Ɏg�p����
	 * @param pos �}�X�̍��W
	 */
	void eraseLinesAround(Address pos) {
		for (int d = 0; d <= 3; d++) {
			SideAddress side = SideAddress.get(pos, d);
			if (getState(side) == LINE) {
				changeState(side, UNKNOWN);
			}
		}
	}

	void eraseLinesAroundA(Address pos) {
		for (int d = 0; d <= 3; d++) {
			SideAddress side = SideAddress.get(pos, d);
			if (getState(side) == LINE) {
				changeStateA(side, UNKNOWN);
			}
		}
	}
	/**
	 * �Ֆʏ�Ԃ�ύX���C�A���h�D���X�i�[�ɕύX��ʒm����D
	 * @param pos
	 * @param st
	 */
	public void changeStateA(Address pos, int st) {
		fireUndoableEditUpdate(
			new CellEditStep(pos, getNumber(pos), st));
		setNumber(pos, st);	
	}

	public void undo(AbstractStep step) {
		if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeState(s.getPos(), s.getBefore());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			setNumber(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeState(s.getPos(), s.getAfter());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			setNumber(s.getPos(), s.getAfter());
		}
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

	public void changeState(SideAddress pos, int st) {
		changeState(pos.d(), pos.r(), pos.c(), st);
	}

	/**
	 * �ӂ̏�Ԃ��w�肵����ԂɕύX����
	 * �A���h�D���X�i�[�ɕύX��ʒm����
	 * @param pos �Ӎ��W
	 * @param st �ύX��̏��
	 */
	public void changeStateA(SideAddress pos, int st) {
		fireUndoableEditUpdate(
			new BorderEditStep(pos, getState(pos), st));
		changeState(pos, st);
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (!isNumber(r,c)) {
					number[r][c] = BLANK;
				}
			}
		}
		initBoard();
	}
	
	public void trimAnswer() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getNumber(r, c) == WHITE)
					setNumber(r, c, BLANK);
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
	 * ���̃}�X�̏㉺���E�̗אڂS�}�X�ɍ��}�X�����邩�ǂ����𒲂ׂ�
	 * @param r
	 * @param c
	 * @return �㉺���E�ɍ��}�X���ЂƂł������ true
	 */
	boolean isBlock(int r, int c) {
		if (isBlack(r-1, c) || isBlack(r+1, c) || isBlack(r, c-1) || isBlack(r, c+1))
			return true;
		return false;
	}

	boolean isBlock(Address pos) {
		return isBlock(pos.r(), pos.c());
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
//		printLink(d,r,c);
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
//		printLink(d,r,c);
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
			initLink(r  , c  );
			longerLink = initializingLink;
			initLink(r  , c+1);
			if (initializingLink.size() > longerLink.size()) longerLink = initializingLink;
		}
		else if (d==HORIZ) {
			initLink(r  , c  );
			longerLink = initializingLink;
			initLink(r+1, c  );
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
	
	public int countLine(Address pos) {
		return countLine(pos.r(), pos.c());
	}

	private int checkLinks() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				int l = countLine(r,c);
				if (l > 2) {
					result |= 1;
				} else if ( l == 1 ) {
					result |= 2; 
				}
				if (isBlack(r,c) && (l > 0))
					result |= 64;
				if (!isNumber(r,c) && !isBlack(r,c) && (l == 0))
					result |= 8;
			}
		}
		if (hasMultipleLinks())
			result |= 4;
		return result;
	}
	
	private int checkArrows() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getNumber(r, c) >= 0) {
					result |= checkArrow(r,c);
				}
				if (isBlack(r, c)) {
					if (isBlock(r, c)) {
						result |= 32;
					}
				}
			}
		}
		return result;
	}
	
	int checkArrow(int r, int c) {
		int result = 0;
		int blackCount = 0;
		int dir = getArrowDirection(r,c);
		int number = getArrowNumber(r,c);
		Address pos = new Address(r,c);
		pos.move(dir);
		while (isOn(pos)) {
			if (isBlack(pos.r(),pos.c()))
				blackCount++;
			pos.move(dir);
		}
		if (number == blackCount)
			result = 0;
		else
			result = 16;
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
