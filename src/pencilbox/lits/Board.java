package pencilbox.lits;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 * �u�k�h�s�r�v�ՖʃN���X
 */
public class Board extends BoardBase {
	
	public static final int WHITE = -1;
	public static final int BLACK = -2;
	public static final int UNKNOWN = 0;
	
	private List<Area> areaList;
	private Area[][] area;
	private int[][] state;
	private List<Wall> wallList;
	private Wall[][] wall;
	private Wall initializingWall;

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
		wall = new Wall[rows()][cols()];
		wallList = new LinkedList<Wall>();
	}

	public void clearBoard() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				state[r][c] = UNKNOWN;
			}
		}
		initBoard();
	}

	public void trimAnswer() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (state[r][c] == WHITE)
					state[r][c] = UNKNOWN;
				}
		}
	}

	/**
	 * Get state of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the state.
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}
	
	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * Set state to a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param st The state to set.
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	
	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	
	/**
	 * �����̍��W�����}�X���ǂ����B
	 * @param r �s���W
	 * @param c ����W
	 * @return ���}�X�Ȃ� true ��Ԃ��B
	 */
	public boolean isBlack(int r, int c) {
		return isOn(r, c) && (state[r][c] == BLACK);
	}
	/**
	 * ���̃}�X�̏�������̈���擾����
	 * ���̃}�X���̈�ɑ����Ă��Ȃ��ꍇ�� null ��Ԃ�
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the area.
	 */
	public Wall getWall(int r, int c ) {
		return wall[r][c];
	}
	/**
	 * �Տ�̃}�X�ɁC���̃}�X�̏�������̈��ݒ肷��
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setWall(int r, int c, Wall a) {
		wall[r][c] = a;
	}
	
	/**
	 * ���̃}�X�̏�������̈���擾����
	 * ���̃}�X���̈�ɑ����Ă��Ȃ��ꍇ�� null ��Ԃ�
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(int r, int c ) {
		if (!isOn(r, c))
			return null;
		return area[r][c];
	}
	
	public Area getArea(Address pos) {
		return getArea(pos.r(), pos.c());
	}
	
	/**
	 * �Տ�̃}�X�ɁC���̃}�X�̏�������̈��ݒ肷��
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c, Area a) {
		area[r][c] = a;
	}
	
	public void setArea(Address pos, Area a) {
		setArea(pos.r(), pos.c(), a);
	}
	
	/**
	 * �V�����̈��ǉ�����
	 * @param newArea
	 */
	public void addArea(Area newArea) {
		for (Address pos : newArea) {
			area[pos.r()][pos.c()] = newArea;
		}
		areaList.add(newArea);
	}

	/**
	 * �̈���폜����
	 * @param oldArea
	 */
	public void removeArea(Area oldArea) {
		for (Address pos : oldArea) {
			if (area[pos.r()][pos.c()] == oldArea)
				area[pos.r()][pos.c()] = null;
		}
		areaList.remove(oldArea);
	}
	/**
	 * �}�X��̈�ɒǉ�����
	 * @param pos �ǉ�����}�X�̍��W
	 * @param a �ǉ������̈�
	 */
	public void addCellToArea(int r, int c, Area a) {
		if (a.isEmpty()) {
			areaList.add(a);
		}
		setArea(r, c, a);
		a.add(r, c);
//		initArea(a);
	}
	
	public void addCellToArea(Address pos, Area a) {
		addCellToArea(pos.r(), pos.c(), a);
	}
	/**
	 * �}�X��̈悩���菜��
	 * @param pos ��菜���}�X�̍��W
	 * @param a ��菜�����̈�
	 */
	public void removeCellFromArea(int r, int c, Area a) {
		setArea(r, c, null);
		a.remove(r, c);
		if (a.isEmpty()) {
			areaList.remove(a);
		} else {
//			initArea(a);
		}
	}
	
	public void removeCellFromArea(Address pos, Area a) {
		removeCellFromArea(pos.r(), pos.c(), a);
	}

	public void changeState(int r, int c, int st) {
		int prevSt = getState(r, c);
		setState(r, c, st);
		Area a = getArea(r, c);
		if (a != null) {
			if (prevSt == BLACK) {
				a.getTetromino().remove(r, c);
			}
			if (st == BLACK) {
				a.getTetromino().add(r, c);
			}
		}
	}
	
	public void changeState(Address pos, int st) {
		changeState(pos.r(), pos.c(), st);
	}
	
	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX���C�ύX���A���h�D���X�i�[�ɒʒm����
	 * @param pos �}�X���W
	 * @param st �ύX��̏��
	 */
	public void changeStateA(Address pos, int st) {
		fireUndoableEditUpdate(new Step(pos.r(), pos.c(), getState(pos), st));
		changeState(pos, st);
	}

	public void undo(AbstractStep step) {
		Step s = (Step) step;
		changeState(s.row, s.col, s.before);
	}

	public void redo(AbstractStep step) {
		Step s = (Step) step;
		changeState(s.row, s.col, s.after);
	}

	/**
	 * @return Returns the areaList.
	 */
	List<Area> getAreaList() {
		return areaList;
	}

	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}
	
	public void initBoard() {
		initTetrominos();
	}

	/**
	 * ���̃}�X��2��2�̍��}�X�u���b�N�̈�p���ǂ����𒲂ׂ�
	 * @param r
	 * @param c
	 * @return 2x2�u���b�N�Ȃ�� true
	 */
	boolean is2x2Block(int r, int c) {
		if (isBlack(r, c)) {
			if (isBlack(r-1, c)) {
				if (isBlack(r, c-1)) {
					if (isBlack(r-1, c-1)) {
						return true;
					}
				}
				if (isBlack(r, c+1)) {
					if (isBlack(r-1, c+1)) {
						return true;
					}
				}
			}
			if (isBlack(r+1, c)) {
				if (isBlack(r, c-1)) {
					if (isBlack(r+1, c-1)) {
						return true;
					}
				}
				if (isBlack(r, c+1)) {
					if (isBlack(r+1, c+1)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public int checkAnswerCode() {
		int result = 0;
		if (areaList.size() == 0)
			result |= 1;
		// �u���b�N���ƂɃe�g���~�m�����邩�̃`�F�b�N
		result |= checkTetrominos();
		// ����`�e�g���~�m�אڂ̃`�F�b�N�i�O�̃`�F�b�N�ɍ��i�����Ƃ��̂݁j
		if ((result & 8) == 0)
			result |= checkAdjacentCongruousTetrominos();
		// ���}�X�P�A���̃`�F�b�N
		result |= checkConnection();
		// 2x2�̃`�F�b�N
		result |= check2x2s();
		return result;
	}
	
	public int checkTetrominos() {
		int result = 0;
		int[] count = new int[6];
		for (Area a : areaList) {
			count[a.getTetrominoType()] ++;
		}
//		System.out.println("L\tI\tT\tS\tO\tother");
//		for (int i = 0; i < 6; i++)
//			System.out.print(count[(i+1)%6]+"\t");
//		System.out.println();
		if (count[0] > 0)
			result = 8;
		return result;
	}
	
	/**
	 * ���݂̔Ֆʏ�ԂɊ�Â��āC�̈�̐ݒ���s��
	 */
	public void initTetrominos() {
		for (Area a : areaList) {
			a.getTetromino().clear();
		}
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getArea(r, c) != null) {
					if (getState(r, c) == BLACK) {
						getArea(r, c).getTetromino().add(r, c);
					}
				}
			}
		}
	}
	
	private int checkAdjacentCongruousTetrominos() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getArea(r, c) != null) {
					if (getState(r, c) == BLACK) {
						if (getArea(r, c+1) != null) {
							if (getArea(r, c) != getArea(r, c+1)) {
								if (getState(r, c+1) == BLACK) {
									if (getArea(r, c).getTetrominoType() == getArea(r, c+1).getTetrominoType()) {
										result = 16;
									}
								}
							}
						}
						if (getArea(r+1,c) != null) {
							if (getArea(r, c) != getArea(r+1, c)) {
								if (getState(r+1, c) == BLACK) {
									if (getArea(r, c).getTetrominoType() == getArea(r+1, c).getTetrominoType()) {
										result = 16;
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	private int checkConnection() {
		ArrayUtil.initArrayObject2(wall, null);
		wallList.clear();
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getState(r, c) == BLACK && getWall(r, c) == null) {
					if (wallList.size() > 0)
						return 32;
					initWall(r,c);
				}
			}
		}
		return 0;
	}
	/**
	 * ����}�X���N�_�Ƃ��鍕�}�X�̂Ȃ���𒲂ׂ�Wall���쐬����
	 * @param r
	 * @param c
	 */
	private void initWall(int r, int c) {
		initializingWall = new Wall();
		initWall1(r, c);
		wallList.add(initializingWall);
	}

	private boolean initWall1(int r, int c) {
		if (!isOn(r, c))
			return false;
		if (!isBlack(r, c))
			return false;
		if (getWall(r, c) == initializingWall)
			return false;
		initializingWall.add(r, c);
		setWall(r, c, initializingWall);
		initWall1(r-1, c);
		initWall1(r, c-1);
		initWall1(r+1, c);
		initWall1(r, c+1);
		return true;
	}
	
	private int check2x2s() {
		int result = 0;
		for (int r=rows()-1; r>=0; r--) {
			for (int c=cols()-1; c>=0; c--) {
				if (is2x2Block(r,c)) {
					result |= 64;
				}
			}
		}
		return result;
	}
	
	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(Messages.getString("lits.AnswerCheckMessage1"));  //$NON-NLS-1$
		if ((result & 2) == 2)
			;
		if ((result & 4) == 4)
			;
		if ((result & 8) == 8)
			message.append(Messages.getString("lits.AnswerCheckMessage2"));  //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("lits.AnswerCheckMessage3"));  //$NON-NLS-1$
		if ((result & 32) == 32)
			message.append(Messages.getString("lits.AnswerCheckMessage4"));  //$NON-NLS-1$
		if ((result & 64) == 64)
			message.append(Messages.getString("lits.AnswerCheckMessage5"));  //$NON-NLS-1$
		return message.toString();
	}
}

	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	class Step extends AbstractStep {

		int row;
		int col;
		int before;
		int after;

		/**
		 * �R���X�g���N�^
		 * @param r �ύX���ꂽ�}�X�̍s���W
		 * @param c �ύX���ꂽ�}�X�̗���W
		 * @param b �ύX�O�̏��
		 * @param a �ύX��̏��
		 */
		public Step(int r, int c, int b, int a) {
			super();
			row = r;
			col = c;
			before = b;
			after = a;
		}

	}
