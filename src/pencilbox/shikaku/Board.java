package pencilbox.shikaku;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SquareEditStep;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 * 「四角に切れ」盤面クラス
 */
public class Board extends BoardBase {
	
	static final int UNDECIDED_NUMBER = -1;
	
	private int[][] number;
	private Square[][] square;
	private List<Square> squareList;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		square = new Square[rows()][cols()];
		squareList = new LinkedList<Square>();
	}

	public void clearBoard() {
		super.clearBoard();
		squareList.clear();
		ArrayUtil.initArrayObject2(square, null);
	}

	/**
	 * @return the number
	 */
	int[][] getNumber() {
		return number;
	}

	public void initBoard() {
		initSquares();
	}

	/**
	 * 盤面上の領域の初期処理を行う
	 */
	public void initSquares() {
		for (Square sq : squareList) {
			initSquare1(sq);
		}
	}
	/**
	 * 四角に数字を設定し，マスに四角を設定する
	 * @param sq 追加する四角
	 */
	public void initSquare1(Square sq) {
		int n = 0;
		for (int r = sq.r0(); r <= sq.r1(); r++ ) {
			for (int c = sq.c0(); c <= sq.c1(); c++) {
				if (isNumber(r,c)) {
					if (n != 0)
						n = Square.MULTIPLE_NUMBER;
					else
						n = number[r][c];
				}
				square[r][c] = sq;
			}
		}
		sq.setNumber(n);
	}

	public void clearSquare1(Square sq) {
		for (int r = sq.r0(); r <= sq.r1(); r++ ) {
			for (int c = sq.c0(); c <= sq.c1(); c++) {
				square[r][c] = null;
			}
		}
	}
	
	/**
	 * @return Returns the squareList.
	 */
	List<Square> getSquareList() {
		return squareList;
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

	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
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

	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
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
	
	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
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

	public void setSquare(int r, int c, Square s) {
		square[r][c] = s;
	}

	public Square getSquare(Address pos) {
		return square[pos.r()][pos.c()];
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
	 * 四角を追加，変更したときにすでにある他の四角と重なる場合，その四角を除去する。
	 * @param sq 追加,変更する四角
	 * @param org 変更する場合のもとの四角
	 */
	void removeOverlappedSquares(Square sq, Square org) {
		for (int r = sq.r0(); r <= sq.r1(); r++ ) {
			for (int c = sq.c0(); c <= sq.c1(); c++) {
				Square s = getSquare(r, c);
				if (s != null && s != org) {
					removeSquareA(s);
				}
			}
		}
	}
	
	/**
	 * 四角を追加する
	 * アンドゥリスナーに通知する
	 * @param sq
	 */
	public void addSquareA(Square sq) {
		fireUndoableEditUpdate(new SquareEditStep(sq.r0(), sq.c0(), sq.r1(), sq.c1(), SquareEditStep.ADDED));
		addSquare(sq);
	}

	/**
	 * 四角を変更する
	 * アンドゥリスナーに通知する
	 * @param sq
	 * @param newSq
	 */
	public void changeSquareA(Square sq, Square newSq) {
		int rOld = -1;
		int cOld= -1;
		int rNew = -1;
		int cNew= -1;
		if (sq.r0() == newSq.r0()) {
			rOld = sq.r1();
			rNew = newSq.r1();
		} else if (sq.r1() == newSq.r0()) {
			rOld = sq.r0();
			rNew = newSq.r1();
		} else if (sq.r0() == newSq.r1()) {
			rOld = sq.r1();
			rNew = newSq.r0();
		} else if (sq.r1() == newSq.r1()) {
			rOld = sq.r0();
			rNew = newSq.r0();
		}
		if (sq.c0() == newSq.c0()) {
			cOld = sq.c1();
			cNew = newSq.c1();
		} else if (sq.c1() == newSq.c0()) {
			cOld = sq.c0();
			cNew = newSq.c1();
		} else if (sq.c0() == newSq.c1()) {
			cOld = sq.c1();
			cNew = newSq.c0();
		} else if (sq.c1() == newSq.c1()) {
			cOld = sq.c0();
			cNew = newSq.c0();
		}
		fireUndoableEditUpdate(new SquareEditStep(rOld, cOld, rNew, cNew, SquareEditStep.CHANGED));
		changeSquare(sq, newSq);
	}

	/**
	 * 四角を除去する
	 * アンドゥリスナーに通知する
	 * @param sq 除去する四角
	 */
	public void removeSquareA(Square sq) {
		fireUndoableEditUpdate(new SquareEditStep(sq.r0(), sq.c0(), sq.r1(), sq.c1(), SquareEditStep.REMOVED));
		removeSquare(sq);
	}
	
	public void undo(AbstractStep step) {
		SquareEditStep s = (SquareEditStep) step;
		if (s.getOperation() == SquareEditStep.ADDED) {
			removeSquare(s.getR0(), s.getC0());
		} else if (s.getOperation() == SquareEditStep.REMOVED) {
			addSquare(s.getR0(), s.getC0(), s.getR1(), s.getC1());
		} else if (s.getOperation() == SquareEditStep.CHANGED) {
			changeSquare(s.getR1(), s.getC1(), s.getR0(), s.getC0());
		}
	}

	public void redo(AbstractStep step) {
		SquareEditStep s = (SquareEditStep) step;
		if (s.getOperation() == SquareEditStep.ADDED) {
			addSquare(s.getR0(), s.getC0(), s.getR1(), s.getC1());
		} else if (s.getOperation() == SquareEditStep.REMOVED) {
			removeSquare(s.getR0(), s.getC0());
		} else if (s.getOperation() == SquareEditStep.CHANGED) {
			changeSquare(s.getR0(), s.getC0(), s.getR1(), s.getC1());
		}
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
	 * 座標を指定して四角を作成して追加する
	 * @param r0
	 * @param c0
	 * @param r1
	 * @param c1
	 */
	public void addSquare(int r0, int c0, int r1, int c1) {
		Square sq = new Square(r0, c0, r1, c1);
		addSquare(sq);
	}

	/**
	 * 四角の1つの頂点を固定したまま，他の1つの頂点を変更する。
	 * @param rOld
	 * @param cOld
	 * @param rNew
	 * @param cNew
	 */
	public void changeSquare(int rOld, int cOld, int rNew, int cNew) {
		Square sq = getSquare(rOld, cOld);
		clearSquare1(sq);
		sq.changeCorner(rOld, cOld, rNew, cNew);
		initSquare1(sq);
	}
	
	/**
	 * 四角を変更する
	 * @param sq 変更される四角
	 * @param newSq 変更後の四角の形
	 */
	public void changeSquare(Square sq, Square newSq) {
		clearSquare1(sq);
		sq.set(newSq.r0(), newSq.c0(), newSq.r1(), newSq.c1());
		initSquare1(sq);
	}

	/**
	 * 四角を消去する
	 * @param sq 消去する四角
	 */
	public void removeSquare(Square sq) {
		clearSquare1(sq);
		squareList.remove(sq);
	}

	/**
	 * 四角を消去する
	 * @param r0
	 * @param c0
	 */
	public void removeSquare(int r0, int c0) {
		Square sq = getSquare(r0, c0);
		removeSquare(sq);
	}

	public int checkAnswerCode() {
		int errorCode = 0;
		int nNumber = 0;
		for (Square sq : squareList) {
			int n = sq.getNumber();
			if (n == Square.MULTIPLE_NUMBER) {
				errorCode |= 1; 
			} else if (n == Square.NO_NUMBER) {
				errorCode |= 2; 
			} else if (n == UNDECIDED_NUMBER) {
				;
			} else if (n < sq.getSquareSize()) {
				errorCode |= 4; 
			} else if (n > sq.getSquareSize()) {
				errorCode |= 8; 
			}
		}
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (isNumber(r,c))
					nNumber ++;
					if (square[r][c] == null)
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
			return Messages.getString("shikaku.AnswerCheckMessage6"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(Messages.getString("shikaku.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("shikaku.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("shikaku.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("shikaku.AnswerCheckMessage4")); //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("shikaku.AnswerCheckMessage5")); //$NON-NLS-1$
		return message.toString();
	}
}	
