package pencilbox.heyawake;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.BoardBase;
import pencilbox.util.ArrayUtil;


/**
 * �u�ւ�킯�v�Ֆ�
 */
public class Board extends BoardBase {

	static final int UNKNOWN = 0;
	static final int BLACK = 1;
	static final int WHITE = 2;

	private int[][] state;
	private Square[][] square;
	private List squareList;  // ����

	int[][] chain;
	int maxChain;
	int[][] contH;
	int[][] contV;
	int[][] contWH;
	int[][] contWV;

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		square = new Square[rows()][cols()];
		squareList = new LinkedList();
		contH = new int[rows()][cols()];
		contV = new int[rows()][cols()];
		contWH = new int[rows()][cols()];
		contWV = new int[rows()][cols()];
		chain = new int[rows()][cols()];
		maxChain = 1;
	}
	
	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}
	/**
	 * @param r row coordinate
	 * @param c column coordinate
	 * @return square at [r,c]
	 */
	public Square getSquare(int r, int c) {
		return square[r][c];
	}
	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt2(state, UNKNOWN);
		for (int n=squareList.size()-1; n>=0; n--) {
			((Square) squareList.get(n)).clear();
		}
		initCont();
		ArrayUtil.initArrayInt2(chain,0);
	}
	
	public void initBoard() {
		initCont();
		initChain();
		initRoomCount();
	}
	
	List getSquareList() {
		return squareList;
	}
	/**
	 * �������X�g�����񂷂�Iterator���擾����
	 * @return �������X�g��Iterator
	 */
	public Iterator getSquareListIterator() {
		return squareList.iterator();
	}
	/**
	 * �������X�g�̃T�C�Y�C�܂葍���������擾����
	 * @return �������X�g�̃T�C�Y
	 */
	public int getSquareListSize() {
		return squareList.size();
	}
	/**
	 * �}�X�̏�Ԃ��擾
	 * @param r
	 * @param c
	 * @return �}�X�̏��
	 */
	public int getState(int r, int c){
		return state[r][c];
	}
	/**
	 * �}�X�̏�Ԃ�ݒ�
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	/**
	 * ���̃}�X�̏�Ԃ����}�X���ǂ����𒲂ׂ�
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return ���}�X�Ȃ� true
	 */
	public boolean isBlack(int r, int c) {
		return state[r][c] == BLACK;
	}
	/**
	 * ���̃}�X�̏�Ԃ����}�X�m�肩�ǂ����𒲂ׂ�
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return ���}�X�m��Ȃ� true
	 */
	public boolean isWhite(int r, int c) {
		return state[r][c] == WHITE;
	}
	/**
	 * ���̃}�X�̏�Ԃ�����}�X���ǂ����𒲂ׂ�
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return ����}�X�Ȃ� true
	 */
	public boolean isUnknown(int r, int c) {
		return state[r][c] == UNKNOWN;
	}

	void initCont() {
		ArrayUtil.initArrayInt2(contH, 0);
		ArrayUtil.initArrayInt2(contV, 0);
		ArrayUtil.initArrayInt2(contWH, 0);
		ArrayUtil.initArrayInt2(contWV, 0);
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getState(r,c) != BLACK) {
					if (contH[r][c] == 0) {
						countHorizontalContinuousRoom(r,c);
					}
					if (contV[r][c] == 0) {
						countVerticalContinuousRoom(r,c);
					}
				}
				if (getState(r,c) == WHITE) {
					if (contWH[r][c] == 0) {
						countHorizontalContinuousRoomW(r,c);
					}
					if (contWV[r][c] == 0) {
						countVerticalContinuousRoomW(r,c);
					}
				}
			}
		}
	}
	
	void initRoomCount() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				Square room = getSquare(r,c);
				if (room!=null) {
					room.setNBlack(0);
					room.setNWhite(0);
				}
			}
		}
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				Square room = getSquare(r,c);
				if (room!=null)
					if (getState(r,c)==BLACK) {
						room.setNBlack(room.getNBlack() + 1);
					} else if (getState(r,c)==WHITE){
						room.setNWhite(room.getNWhite() + 1);
				}
			}
		}
	}
	
	/**
	 * �}�X�̏�Ԃ�ݒ�
	 * @param r
	 * @param c
	 * @param st
	 */
	public void changeState(int r, int c, int st) {
		int prevState = getState(r,c);
		setState(r,c,st);
		if (st==BLACK) {
			contH[r][c] = 0;
			contV[r][c] = 0;
			if (r > 0) countVerticalContinuousRoom(r - 1, c);
			if (r < rows()-1) countVerticalContinuousRoom(r + 1, c);
			if (c > 0) countHorizontalContinuousRoom(r, c - 1);
			if (c < cols()-1) countHorizontalContinuousRoom(r, c + 1);
			connectChain(r,c);
		}
		if (prevState==BLACK) {
			countVerticalContinuousRoom(r, c);
			countHorizontalContinuousRoom(r, c);
			cutChain(r,c);
		}
		if (st==WHITE) {
			countVerticalContinuousRoomW(r, c);
			countHorizontalContinuousRoomW(r, c);
		}
		if (prevState==WHITE) {
			contWH[r][c] = 0;
			contWV[r][c] = 0;
			if (r > 0) countVerticalContinuousRoomW(r - 1, c);
			if (r < rows()-1) countVerticalContinuousRoomW(r + 1, c);
			if (c > 0) countHorizontalContinuousRoomW(r, c - 1);
			if (c < cols()-1) countHorizontalContinuousRoomW(r, c + 1);
		}
		Square room = getSquare(r,c);
		if (room!=null) {
			if (st==BLACK) {
				room.setNBlack(room.getNBlack() + 1);
			} else if (st==WHITE){
				room.setNWhite(room.getNWhite() + 1);
			}
			if (prevState==BLACK) {
				room.setNBlack(room.getNBlack() - 1);
			} else if (prevState==WHITE){
				room.setNWhite(room.getNWhite() - 1);
			}
		}
	}
	
	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX���C�ύX���A���h�D���X�i�[�ɒʒm����
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @param st �ύX��̏��
	 */
	public void changeStateA(int r, int c, int st) {
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(r, c, state[r][c], st)));
		changeState(r, c, st);
	}
	/**
	 * �}�X�̏�Ԃ� �����st �ƕύX����
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @param st �؂�ւ�����
	 */
	public void toggleState(int r, int c, int st){
		if(state[r][c]==st)
			changeStateA(r,c,UNKNOWN);
		else
			changeStateA(r, c, st);
	}
	/**
	 * �l�p��ǉ�����
	 * ���̍ۂɁC�ǉ�����l�p�Əd�Ȃ�l�p�����łɂ�������C���̎l�p����������
	 * @param r0 ����̊p�̍s���W
	 * @param c0 ����̊p�̗���W
	 * @param r1 �����̊p�̍s���W
	 * @param c1 �����̊p�̗���W
	 */
	public void addSquareSpanning(int r0, int c0, int r1, int c1) {
		int ra = r0<r1 ? r0 : r1;
		int rb = r0<r1 ? r1 : r0;
		int ca = c0<c1 ? c0 : c1;
		int cb = c0<c1 ? c1 : c0;
		Square newSquare = new Square(ra, ca, rb, cb);
		for (int r = ra; r <= rb; r++ ) {
			for (int c = ca; c <= cb; c++) {
				if(square[r][c] != null) {
					removeSquare(square[r][c]);
				}
			}
		}
		addSquare(newSquare);
	}

	/**
	 * �����ɗ^����ꂽ�}�X���܂ގl�p����������
	 * @param r
	 * @param c 
	 */
	public void removeSquareIncluding(int r, int c) {
		if (square[r][c] != null) {
			removeSquare(square[r][c]);
		}
	}
	/**
	 * �l�p��ǉ�����
	 * @param sq ��������l�p
	 */
	public void addSquare(Square sq) {
		for (int r = sq.r0; r <= sq.r1; r++ ) {
			for (int c = sq.c0; c <= sq.c1; c++) {
				if(square[r][c] != null) {
					removeSquare(square[r][c]);
				}
				square[r][c] = sq;
			}
		}
		squareList.add(sq);
	}
	/**
	 * �l�p����������
	 * @param sq ��������l�p
	 */
	public void removeSquare(Square sq) {
		for (int r = sq.r0; r <= sq.r1; r++ ) {
			for (int c = sq.c0; c <= sq.c1; c++) {
				square[r][c] = null;
			}
		}
		squareList.remove(sq);
	}
	
	/**
	 * ���̃}�X���A�����}�X���ǂ����𒲂ׂ�
	 * �܂�C���̃}�X�����}�X�ŁC�㉺���E�̗אڂS�}�X�ɍ��}�X�����邩�ǂ����𒲂ׂ�
	 * @param r
	 * @param c
	 * @return �A�����}�X�Ȃ�� true
	 */
	boolean isBlock(int r, int c) {
		if (isBlack(r,c)) {
			if (r > 0 && isBlack(r-1,c)) return true;
			if (r < rows()-1 && isBlack(r+1,c)) return true;
			if (c > 0 && isBlack(r,c-1)) return true;
			if (c < cols()-1 && isBlack(r,c+1)) return true;
		}
		return false;
	}
	/**
	 * �����̃}�X���牡�ɘA�����镔���̌��𐔂��Đݒ肷��
	 */
	void countHorizontalContinuousRoom(int r, int c) {
		int c0;
		int c1;
		int count = 0;
		Square d = null;
		while (c > 0 && !isBlack(r, c-1)) {
			c--;
		}
		c0 = c;
		while (c < cols() && !isBlack(r, c)) {
			Square room = getSquare(r,c);
			if (room != d) {
				count++;
				d = room;
			}
			c++;
		}
		c1 = c;
		c = c0;
		while (c<c1) {
			contH[r][c] = count;
			c++;
		}
	}
	/**
	 * �����̃}�X����c�ɘA�����镔���̌��𐔂��Đݒ肷��
	 */
	void countVerticalContinuousRoom(int r, int c) {
		int r0;
		int r1;
		int count = 0;
		Square d = null;
		while (r > 0 && !isBlack(r-1, c)) {
			r--;
		}
		r0 = r;
		while (r < rows() && !isBlack(r, c)) {
			Square room = getSquare(r,c);
			if (room != d) {
				count++;
				d = room;
			}
			r++;
		}
		r1 = r;
		r = r0;
		while (r<r1) {
			contV[r][c] = count;
			r++;
		}
	}
	/**
	 * �����̃}�X���牡�ɔ��}�X�̘A�����镔���̌��𐔂��Đݒ肷��
	 */
	void countHorizontalContinuousRoomW(int r, int c) {
		int c0;
		int c1;
		int count = 0;
		Square d = null;
		while (c > 0 && isWhite(r, c-1)) {
			c--;
		}
		c0 = c;
		while (c < cols() && isWhite(r, c)) {
			Square room = getSquare(r,c);
			if (room != d) {
				count++;
				d = room;
			}
			c++;
		}
		c1 = c;
		c = c0;
		while (c<c1) {
			contWH[r][c] = count;
			c++;
		}
	}
	/**
	 * �����̃}�X����c�ɔ��}�X�̘A�����镔���̌��𐔂��Đݒ肷��
	 */
	void countVerticalContinuousRoomW(int r, int c) {
		int r0;
		int r1;
		int count = 0;
		Square d = null;
		while (r > 0 && isWhite(r-1, c)) {
			r--;
		}
		r0 = r;
		while (r < rows() && isWhite(r, c)) {
			Square room = getSquare(r,c);
			if (room != d) {
				count++;
				d = room;
			}
			r++;
		}
		r1 = r;
		r = r0;
		while (r<r1) {
			contWV[r][c] = count;
			r++;
		}
	}

	/**
	 * 	chain�z�������������D
	 */
	void initChain() {
		maxChain = 1;
		ArrayUtil.initArrayInt2(chain,0);
		for (int r = 0; r < rows(); r = r + rows()-1) {
			for (int c = 0; c < cols(); c++) {
				if (isBlack(r, c) && chain[r][c] == 0) {
					if (initChain1(r, c, 0, 0, 1) == -1) {
						setChain(r, c, -1);
					}
				}
			}
		}
		for (int r = 1; r < rows()-1; r++) {
			for (int c = 0; c < cols(); c = c + cols()-1) {
				if (isBlack(r, c) && chain[r][c] == 0) {
					if (initChain1(r, c, 0, 0, 1) == -1) {
						setChain(r, c, -1);
					}
				}
			}
		}
		for (int r = 1; r < rows() - 1; r++) {
			for (int c = 1; c < cols() - 1; c++) {
				if (isBlack(r, c) && chain[r][c] == 0) {
					if (initChain1(r, c, 0, 0, ++maxChain) == -1) {
						setChain(r, c, -1);
					}
				}
			}
		}
	}

	/**
	 * �΂߂ɂȂ��鍕�}�X�����ǂ�Cchain �ɔԍ� n ��ݒ肷��
	 * ���f�𔭌�������C���̎��_�� -1 ��Ԃ��Ė߂�
	 * @param r
	 * @param c
	 * @param uu
	 * @param vv
	 * @param n
	 * @return �Ֆʂ̕��f�𔭌������� -1 , �����łȂ���� n �Ɠ����l
	 */
	int initChain1(int r, int c, int uu, int vv, int n) {
		if (n == 1 && uu != 0 && isOnPeriphery(r, c)) { // �ւ��O���ɒB����
			return -1;
		}
		if (n >= 0 && isOnPeriphery(r, c)) {
			chain[r][c] = 1;
		} else {
			chain[r][c] = n;
		}
		for (int u = -1; u < 2; u += 2) {
			for (int v = -1; v < 2; v += 2) {
				if ((u == -uu) && (v == -vv))
					continue; // �������Ƃ���͂Ƃ΂�
				if (!isOn(r + u, c + v))
					continue; // �ՊO�͂Ƃ΂�
				if (!isBlack(r + u, c + v))
					continue; // ���}�X�ȊO�͂Ƃ΂�
				if (chain[r + u][c + v] == n) // �ւ�����
					return -1;
				if (initChain1(r + u, c + v, u, v, n) == -1)
					return -1;
			}
		}
		return n;
	}
	private int[] adjacentChain = new int[4];
	/**
	 * 	���Ŋm�肵���Ƃ��ɁC���̃}�X����_�Ƃ���chain���X�V����D
	 * 	���̃}�X���m�肵�����Ƃɂ��C�V�K�ɕ��f���������邩�𒲂ׁC
	 * 	��������Ȃ� chain �S�̂� -1 �ōX�V����D
	 * 	�������Ȃ��Ȃ�C�΂ߗא�4�}�X�̍ŏ��l�ɂ��킹��D
	 * 	�΂ߗׂɍ��}�X���Ȃ���΁C�V�����ԍ�������D
	 * @param r
	 * @param c
	 */
	void connectChain(int r, int c) {
		int[] adjacent = adjacentChain;
		int k = 0;
		int newChain = Integer.MAX_VALUE;
		if (isOnPeriphery(r,c)) 
			newChain = 1;
		for (int u = -1; u <= 1; u += 2) {
			for (int v = -1; v <= 1; v += 2) {
				if (!isOn(r + u, c + v)) continue;
				if (getState(r + u,c + v) != BLACK) continue; // ���}�X�ȊO�͂Ƃ΂�
				if (isOnPeriphery(r, c) && chain[r + u][c + v] == 1) {
					newChain = -1; // �[�̃}�X�ɂ���Ƃ��ԍ�1������������
				} 
				adjacent[k] = chain[r + u][c + v];
				for (int l = 0; l < k; l++) {
					if (adjacent[k] == adjacent[l]) // �����ԍ�������������
						newChain = -1;
				}
				k++;
				if (chain[r + u][c + v] < newChain)
					newChain = chain[r + u][c + v];
			}
		}
		if (newChain == Integer.MAX_VALUE)
			chain[r][c] = ++maxChain; // ���͂ɍ��}�X���Ȃ��Ƃ��C�V�����ԍ�������
		else
			setChain(r, c, newChain); // ���͂ɍ��}�X������Ƃ��C���̍ŏ��ԍ�������
	}

	/**
	 * ���}�X�����������Ƃ��ɁCchain���X�V����
	 * �S���v�Z���Ȃ������Ƃɂ���
	 * @param r
	 * @param c
	 */
	void cutChain(int r, int c) {
		initChain();
	}
	/**
	 * 	�}�X�� chain�ԍ���ݒ肷��
	 * 	�΂ߗׂɍ��}�X������Γ����ԍ���ݒ肷��
	 * @param r
	 * @param c
	 * @param n
	 */
	void setChain(int r, int c, int n) {
		chain[r][c] = n;
		for (int u = -1; u < 2; u += 2) {
			for (int v = -1; v < 2; v += 2) {
				if (!isOn(r + u, c + v))
					continue;
				if (!isBlack(r + u, c + v))
					continue; // ���}�X�ȊO�͂Ƃ΂�
				if (chain[r + u][c + v] == n)
					continue; // �����ԍ����������炻�̂܂�
				setChain(r + u, c + v, n);
			}
		}
	}
	
	/**
	 * �ՖʑS�̂ŁC�c���ɘA�����鍕�}�X���Ȃ����ǂ����𒲍�����
	 * @return�@�A�����鍕�}�X���Ȃ���� true, ����� false ��Ԃ��@
	 */
	public boolean checkContinuousBlack() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isBlock(r,c))
					return false;
			}
		}
		return true;
	}

	public int checkAnswerCode() {
		int result = 0;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isBlock(r,c))
					result |= 1;
			}
		}
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (chain[r][c] == -1) {
					result |= 2;
				}
			}
		}
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (contH[r][c] >= 3 || contV[r][c] >= 3) {
					result |= 8;
				}
			}
		}
		Square room = null;
		for (Iterator itr = getSquareListIterator(); itr.hasNext(); ) {
			room = (Square) itr.next();
			if (room.getNumber()>=0 && room.getNumber() != room.getNBlack()) {
				result |= 4;
			}
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result&1) == 1)
			message.append("�A�����鍕�}�X������\n");
		if ((result&2) == 2)
			message.append("���}�X�ɂ��Ֆʂ����f����Ă���\n");
		if ((result&4) == 4)
			message.append("�����ƍ��}�X���̈�v���Ă��Ȃ�����������\n");
		if ((result&8) == 8)
			message.append("���}�X���R�����ȏ㑱���Ă���ӏ�������\n");
		return message.toString();
	}

	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	 class Step extends AbstractUndoableEdit {

		private int row;
		private int col;
		private int before;
		private int after;
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
		
		public void undo() throws CannotUndoException {
			super.undo();
			changeState(row, col, before);
		}

		public void redo() throws CannotRedoException {
			super.redo();
			changeState(row, col, after);
		}
	}

}
