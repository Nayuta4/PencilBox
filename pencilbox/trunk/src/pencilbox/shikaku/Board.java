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
 * 「四角に切れ」盤面クラス
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
	 * 盤面上の領域の初期処理を行う
	 */
	public void initSquares() {
		for (Iterator itr = squareList.iterator(); itr.hasNext(); ) {
			initSquare1((Square)itr.next());
		}
	}
	/**
	 * 四角に数字を設定し，マスに四角を設定する
	 * @param a 追加する四角
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
	 * マスの数字を取得する
	 * @param r 行座標
	 * @param c 列座標
	 * @return マスの数字
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}
	/**
	 * マスに数字を設定する
	 * @param r 行座標
	 * @param c 列座標
	 * @param n 設定する数字
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}
	/**
	 * そのマスに数字があるか
	 * @param r 行座標
	 * @param c 列座標
	 * @return　そのマスに数字があれば true
	 */
	public boolean isNumber(int r, int c) {
		return number[r][c] > 0 || number[r][c] == UNDECIDED_NUMBER;
	}
	/**
	 * そのマスの属する Square を返す
	 * @param r 行座標
	 * @param c 列座標
	 * @return　そのマスの属する Square
	 */
	public Square getSquare(int r, int c) {
		return square[r][c];
	}
	/**
	 * 領域リストのIteratorを取得する
	 * @return 領域リストのIterator
	 */
	public Iterator getSquareListIterator() {
		return squareList.iterator();
	}
	/**
	 * 領域リストのサイズ，つまり領域数を取得する
	 * @return 領域リストのサイズ
	 */
	public int getSquareListSize() {
		return squareList.size();
	}
	
	/**
	 * 引数のマスがいずれかの四角に含まれているかどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 含まれていれば true
	 */
	public boolean isCovered(int r, int c) {
		return square[r][c] != null;
	}

	/**
	 * 引数に与えられた2つの座標を対角位置とする四角を追加する
	 * その際に，追加する四角と重なる位置に他の四角がすでにあったら，その四角を消去する
	 * @param pos0 一方の角の座標
	 * @param pos1 他方の角の座標
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
	 * 引数に与えられたマスを含む四角を消去する
	 * @param pos 座標 
	 */
	public void removeSquareIncluding(Address pos) {
		if(getSquare(pos.r(),pos.c()) != null) {
			removeSquareA(getSquare(pos.r(),pos.c()));
		}
	}
	/**
	 * 四角を追加する
	 * アンドゥリスナーに通知する
	 * @param aq 追加する四角
	 */
	public void addSquareA(Square aq){
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(aq,Step.ADDED)));
		addSquare(aq);
	}
	/**
	 * 四角を除去する
	 * アンドゥリスナーに通知する
	 * @param a 除去する四角
	 */
	public void removeSquareA(Square a){
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(a,Step.REMOVED)));
		removeSquare(a);
	}
	
	/**
	 * 四角を追加する
	 * @param sq 追加する四角
	 */
	public void addSquare(Square sq) {
		initSquare1(sq);
		squareList.add(sq);
	}
	/**
	 * 四角を消去する
	 * @param sq 消去する四角
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
			return "盤上に数字がひとつもない\n";
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append("複数の数字を含む四角がある\n");
		if ((result & 2) == 2)
			message.append("数字を含まない四角がある\n");
		if ((result & 4) == 4)
			message.append("面積が数字を超える四角がある\n");
		if ((result & 8) == 8)
			message.append("面積が数字に満たない四角がある\n");
		if ((result & 16) == 16)
			message.append("四角に含まれないマスがある\n");
		return message.toString();
	}
	
	/**
	 * １手の操作を表すクラス
	 * UNDO, REDO での編集の単位となる
	 */
	class Step extends AbstractUndoableEdit {
		
		static final int ADDED = 1;
		static final int REMOVED = 0;
		
		private Square square;
		private int operation;

		/**
		 * コンストラクタ
		 * @param sq 操作れた領域
		 * @param operation 操作の種類：追加されたのか，除去されたのか
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