package pencilbox.shikaku;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.util.ArrayUtil;


/**
 * �u�l�p�ɐ؂�v�ՖʃN���X
 */
public class Board extends BoardBase {
	
	static final int UNDECIDED_NUMBER = -1;
	
	private int[][] number;
	private Square[][] square;
	private List squareList;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		square = new Square[rows()][cols()];
		squareList = new LinkedList();
	}

	public void clearBoard() {
		super.clearBoard();
		squareList.clear();
		ArrayUtil.initArrayObject2(square, null);
	}

	public void initBoard() {
		initSquares();
	}

	/**
	 * �Ֆʏ�̗̈�̏����������s��
	 */
	public void initSquares() {
		for (Iterator itr = squareList.iterator(); itr.hasNext(); ) {
			initSquare1((Square)itr.next());
		}
	}
	/**
	 * �l�p�ɐ�����ݒ肵�C�}�X�Ɏl�p��ݒ肷��
	 * @param a �ǉ�����l�p
	 */
	public void initSquare1(Square a) {
		int n = 0;
		for (int r = a.r0; r <= a.r1; r++ ) {
			for (int c = a.c0; c <= a.c1; c++) {
				if (isNumber(r,c)) {
					if (n != 0)
						n = Square.MULTIPLE_NUMBER;
					else
						n = number[r][c];
				}
				square[r][c] = a;
			}
		}
		a.setNumber(n);
	}

	/**
	 * �}�X�̐������擾����
	 * @param r �s���W
	 * @param c ����W
	 * @return �}�X�̐���
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}
	/**
	 * �}�X�ɐ�����ݒ肷��
	 * @param r �s���W
	 * @param c ����W
	 * @param n �ݒ肷�鐔��
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}
	/**
	 * ���̃}�X�ɐ��������邩
	 * @param r �s���W
	 * @param c ����W
	 * @return�@���̃}�X�ɐ���������� true
	 */
	public boolean isNumber(int r, int c) {
		return number[r][c] > 0 || number[r][c] == UNDECIDED_NUMBER;
	}
	/**
	 * ���̃}�X�̑����� Square ��Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @return�@���̃}�X�̑����� Square
	 */
	public Square getSquare(int r, int c) {
		return square[r][c];
	}
	/**
	 * �̈惊�X�g��Iterator���擾����
	 * @return �̈惊�X�g��Iterator
	 */
	public Iterator getSquareListIterator() {
		return squareList.iterator();
	}
	/**
	 * �̈惊�X�g�̃T�C�Y�C�܂�̈搔���擾����
	 * @return �̈惊�X�g�̃T�C�Y
	 */
	public int getSquareListSize() {
		return squareList.size();
	}
	
	/**
	 * �����̃}�X�������ꂩ�̎l�p�Ɋ܂܂�Ă��邩�ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return �܂܂�Ă���� true
	 */
	public boolean isCovered(int r, int c) {
		return square[r][c] != null;
	}

	/**
	 * �����ɗ^����ꂽ2�̍��W��Ίp�ʒu�Ƃ���l�p��ǉ�����
	 * ���̍ۂɁC�ǉ�����l�p�Əd�Ȃ�ʒu�ɑ��̎l�p�����łɂ�������C���̎l�p����������
	 * @param pos0 ����̊p�̍��W
	 * @param pos1 �����̊p�̍��W
	 */
	public void addSquareSpanning(Address pos0, Address pos1) {
		int ra = pos0.r()<pos1.r() ? pos0.r() : pos1.r();
		int rb = pos0.r()<pos1.r() ? pos1.r() : pos0.r();
		int ca = pos0.c()<pos1.c() ? pos0.c() : pos1.c();
		int cb = pos0.c()<pos1.c() ? pos1.c() : pos0.c();
		Square newArea = new Square(ra, ca, rb, cb);
		for (int r = ra; r <= rb; r++ ) {
			for (int c = ca; c <= cb; c++) {
				if(getSquare(r,c) != null) {
					removeSquareA(getSquare(r,c));
				}
			}
		}
		addSquareA(newArea);
	}
	/**
	 * �����ɗ^����ꂽ�}�X���܂ގl�p����������
	 * @param pos ���W 
	 */
	public void removeSquareIncluding(Address pos) {
		if(getSquare(pos.r(),pos.c()) != null) {
			removeSquareA(getSquare(pos.r(),pos.c()));
		}
	}
	/**
	 * �l�p��ǉ�����
	 * �A���h�D���X�i�[�ɒʒm����
	 * @param aq �ǉ�����l�p
	 */
	public void addSquareA(Square aq){
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(aq,Step.ADDED)));
		addSquare(aq);
	}
	/**
	 * �l�p����������
	 * �A���h�D���X�i�[�ɒʒm����
	 * @param a ��������l�p
	 */
	public void removeSquareA(Square a){
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(a,Step.REMOVED)));
		removeSquare(a);
	}
	
	/**
	 * �l�p��ǉ�����
	 * @param sq �ǉ�����l�p
	 */
	public void addSquare(Square sq) {
		initSquare1(sq);
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

	public int checkAnswerCode() {
		int errorCode = 0;
		Square a;
		int nNumber = 0;
		for (Iterator itr = squareList.iterator(); itr.hasNext(); ) {
			a = (Square)itr.next();
			int n = a.getNumber();
			if (n == Square.MULTIPLE_NUMBER) {
				errorCode |= 1; 
			} else if (n == Square.NO_NUMBER) {
				errorCode |= 2; 
			} else if (n == UNDECIDED_NUMBER) {
				;
			} else if (n < a.getSquareSize()) {
				errorCode |= 4; 
			} else if (n > a.getSquareSize()) {
				errorCode |= 8; 
			}
		}
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (isNumber(r,c))
					nNumber ++;
					if(square[r][c] == null)
						errorCode |= 16; 
			}
		}
		if (nNumber==0)
			errorCode = 32; 
		return errorCode;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		if (result == 32)
			return "�Տ�ɐ������ЂƂ��Ȃ�\n";
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append("�����̐������܂ގl�p������\n");
		if ((result & 2) == 2)
			message.append("�������܂܂Ȃ��l�p������\n");
		if ((result & 4) == 4)
			message.append("�ʐς������𒴂���l�p������\n");
		if ((result & 8) == 8)
			message.append("�ʐς������ɖ����Ȃ��l�p������\n");
		if ((result & 16) == 16)
			message.append("�l�p�Ɋ܂܂�Ȃ��}�X������\n");
		return message.toString();
	}
	
	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	class Step extends AbstractUndoableEdit {
		
		static final int ADDED = 1;
		static final int REMOVED = 0;
		
		private Square square;
		private int operation;

		/**
		 * �R���X�g���N�^
		 * @param sq ����ꂽ�̈�
		 * @param operation ����̎�ށF�ǉ����ꂽ�̂��C�������ꂽ�̂�
		 */
		public Step(Square sq, int operation) {
			super();
			this.square = sq;
			this.operation = operation;
		}
		
		public void undo() throws CannotUndoException {
			super.undo();
			if (operation==ADDED) {
				removeSquare(square);
			} else if (operation==REMOVED){
				addSquare(square);
			}
		}

		public void redo() throws CannotRedoException {
			super.redo();
			if (operation==ADDED) {
				addSquare(square);
			} else if (operation==REMOVED){
				removeSquare(square);
			}
		}
	}

}