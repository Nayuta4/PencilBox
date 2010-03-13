package pencilbox.numberlink;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.util.ArrayUtil;
import pencilbox.resource.Messages;


/**
 * �u�i���o�[�����N�v�q���g�t���ՖʃN���X
 */
public class Board extends BoardBase {
	
	static final int HORIZ = Direction.HORIZ; 
	static final int VERT = Direction.VERT;
	static final int UNKNOWN = 0;
	static final int LINE = 1;
	static final int NOLINE = -1;
	static final int OUTER = -9;
	static final int UNDECIDED_NUMBER = -1;
	static final int BLANK = 0;

	private int[][] number;
	private int[][][] state;
	
	private List<Link> linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		state = new int[2][][];
		number = new int[rows()][cols()];
		state[0] = new int[rows()][cols()- 1];
		state[1] = new int[rows()- 1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[0] = new Link[rows()][cols() - 1];
		link[1] = new Link[rows() - 1][cols()];
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
	 * @return the number
	 */
	int[][] getNumber() {
		return number;
	}
	/**
	 * @return the state
	 */
	int[][][] getState() {
		return state;
	}

	public boolean isNumber(int r, int c) {
		return (number[r][c] > 0 || number[r][c] == UNDECIDED_NUMBER);
	}
	
	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
	}
	public int getNumber(int r, int c) {
		return number[r][c];
	}
	
	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}

	public void setNumber(int r, int c, int n) {
		if (isOn(r, c))
			number[r][c] = n;
	}
	
	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
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
		return state[d][r][c] == LINE;
	}

	public Link getLink(int d, int r, int c) {
		if (isSideOn(d,r,c) )
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
	
	public Link getLink(Address pos) {
		return getLink(pos.r(), pos.c());
	}
	
	public void setLink(int d, int r, int c, Link l) {
		link[d][r][c] =  l;
	}
	public void setLink(SideAddress pos, Link l) {
		link[pos.d()][pos.r()][pos.c()] = l;
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
			new Step(pos.d(), pos.r(), pos.c(), getState(pos), st));
		changeState(pos.d(), pos.r(), pos.c(), st);
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
	
	private void addNumberToLink(Link link, int d, int r, int c) {
		if (d==VERT) {
			if (isNumber(r,c))
				link.setNumber(getNumber(r,c));
			if (isNumber(r,c+1))
				link.setNumber(getNumber(r,c+1));
		} else {
			if (isNumber(r,c))
				link.setNumber(getNumber(r,c));
			if (isNumber(r+1,c))
				link.setNumber(getNumber(r+1,c));
		}
	}
	private void addNumberToLink(Link link, int r, int c) {
		if (isNumber(r,c))
			link.setNumber(getNumber(r,c));
	}
	
	/**
	 * ����}�X���܂� Link �̏�����
	 * link[][][] �͏�������Ă�����̂Ƃ���
	 * @param r Link�������̋N�_�}�X�̍s���W
	 * @param c Link�������̋N�_�}�X�̗���W
	 */	
	void initLink (int r, int c) {
		initializingLink = new Link();
		addNumberToLink(initializingLink, r, c);
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
		addNumberToLink(initializingLink, d, r, c);
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
		addNumberToLink(newLink, d, r, c);
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
	
	public int checkAnswerCode() {
		int result = 0;
		result |= checkBranch();
		result |= checkConnection();
		return result;
	}
	
	/**
	 * ��������̕���`�F�b�N����
	 */
	private int checkBranch() {
		int result= 0;
		for (int r= 0; r < rows(); r++) {
			for (int c= 0; c < cols(); c++) {
				int l= countLine(r, c);
				if (isNumber(r, c)) {
					if (l > 1)
						result |= 4;
					else if (l == 0)
						result |= 8;
				} else {
					if (l > 2)
						result |= 1;
					else if (l == 1)
						result |= 2;
				}
			}
		}
		return result;
	}
	/**
	 * ��������̐����A���`�F�b�N����
	 */
	private int checkConnection() {
		int result = 0;
		for(Link link : linkList) {
			int number = link.getNumber();
			if (number == -1)
				result |= 16;
			else if (number == 0)
				result |= 32;
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE; 
		StringBuffer message = new StringBuffer();
		if ((result & 4) == 4)
			message.append(Messages.getString("numberlink.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("numberlink.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 1) == 1)
			message.append(Messages.getString("numberlink.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("numberlink.AnswerCheckMessage4")); //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("numberlink.AnswerCheckMessage5")); //$NON-NLS-1$
		if ((result & 32) == 32)
			message.append(Messages.getString("numberlink.AnswerCheckMessage6")); //$NON-NLS-1$
		return message.toString();
	}

	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	class Step extends AbstractUndoableEdit {

		private int direction;
		private int row;
		private int col;
		private int before;
		private int after;
		/**
		 * �R���X�g���N�^
		 * @param d �����Ȃ�true �c���Ȃ�false
		 * @param r �ύX���ꂽ�}�X�̍s���W
		 * @param c �ύX���ꂽ�}�X�̗���W
		 * @param b �ύX�O�̏��
		 * @param a �ύX��̏��
		 */
		public Step(int d, int r, int c, int b, int a) {
			super();
			direction = d;
			row = r;
			col = c;
			before = b;
			after = a;
		}
		public void undo() throws CannotUndoException {
			super.undo();
			changeState(direction, row, col, before);
		}
		public void redo() throws CannotRedoException {
			super.redo();
			changeState(direction, row, col, after);
		}
	}
	
}
