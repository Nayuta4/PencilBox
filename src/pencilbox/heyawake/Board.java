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
 * 「へやわけ」盤面
 */
public class Board extends BoardBase {

	static final int UNKNOWN = 0;
	static final int BLACK = 1;
	static final int WHITE = 2;

	private int[][] state;
	private Square[][] square;
	private List squareList;  // 部屋

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
	 * 部屋リストを巡回するIteratorを取得する
	 * @return 部屋リストのIterator
	 */
	public Iterator getSquareListIterator() {
		return squareList.iterator();
	}
	/**
	 * 部屋リストのサイズ，つまり総部屋数を取得する
	 * @return 部屋リストのサイズ
	 */
	public int getSquareListSize() {
		return squareList.size();
	}
	/**
	 * マスの状態を取得
	 * @param r
	 * @param c
	 * @return マスの状態
	 */
	public int getState(int r, int c){
		return state[r][c];
	}
	/**
	 * マスの状態を設定
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	/**
	 * そのマスの状態が黒マスかどうかを調べる
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return 黒マスなら true
	 */
	public boolean isBlack(int r, int c) {
		return state[r][c] == BLACK;
	}
	/**
	 * そのマスの状態が白マス確定かどうかを調べる
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return 白マス確定なら true
	 */
	public boolean isWhite(int r, int c) {
		return state[r][c] == WHITE;
	}
	/**
	 * そのマスの状態が未定マスかどうかを調べる
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return 未定マスなら true
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
	 * マスの状態を設定
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
	 * マスの状態を指定した状態に変更し，変更をアンドゥリスナーに通知する
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @param st 変更後の状態
	 */
	public void changeStateA(int r, int c, int st) {
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(r, c, state[r][c], st)));
		changeState(r, c, st);
	}
	/**
	 * マスの状態を 未定⇔st と変更する
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @param st 切り替える状態
	 */
	public void toggleState(int r, int c, int st){
		if(state[r][c]==st)
			changeStateA(r,c,UNKNOWN);
		else
			changeStateA(r, c, st);
	}
	/**
	 * 四角を追加する
	 * その際に，追加する四角と重なる四角がすでにあったら，その四角を消去する
	 * @param r0 一方の角の行座標
	 * @param c0 一方の角の列座標
	 * @param r1 他方の角の行座標
	 * @param c1 他方の角の列座標
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
	 * 引数に与えられたマスを含む四角を消去する
	 * @param r
	 * @param c 
	 */
	public void removeSquareIncluding(int r, int c) {
		if (square[r][c] != null) {
			removeSquare(square[r][c]);
		}
	}
	/**
	 * 四角を追加する
	 * @param sq 消去する四角
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
	
	/**
	 * そのマスが連続黒マスかどうかを調べる
	 * つまり，そのマスが黒マスで，上下左右の隣接４マスに黒マスがあるかどうかを調べる
	 * @param r
	 * @param c
	 * @return 連続黒マスならば true
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
	 * 引数のマスから横に連続する部屋の個数を数えて設定する
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
	 * 引数のマスから縦に連続する部屋の個数を数えて設定する
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
	 * 引数のマスから横に白マスの連続する部屋の個数を数えて設定する
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
	 * 引数のマスから縦に白マスの連続する部屋の個数を数えて設定する
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
	 * 	chain配列を初期化する．
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
	 * 斜めにつながる黒マスをたどり，chain に番号 n を設定する
	 * 分断を発見したら，その時点で -1 を返して戻る
	 * @param r
	 * @param c
	 * @param uu
	 * @param vv
	 * @param n
	 * @return 盤面の分断を発見したら -1 , そうでなければ n と同じ値
	 */
	int initChain1(int r, int c, int uu, int vv, int n) {
		if (n == 1 && uu != 0 && isOnPeriphery(r, c)) { // 輪が外周に達した
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
					continue; // 今来たところはとばす
				if (!isOn(r + u, c + v))
					continue; // 盤外はとばす
				if (!isBlack(r + u, c + v))
					continue; // 黒マス以外はとばす
				if (chain[r + u][c + v] == n) // 輪が閉じた
					return -1;
				if (initChain1(r + u, c + v, u, v, n) == -1)
					return -1;
			}
		}
		return n;
	}
	private int[] adjacentChain = new int[4];
	/**
	 * 	黒で確定したときに，そのマスを基点としてchainを更新する．
	 * 	そのマスを確定したことにより，新規に分断が発生するかを調べ，
	 * 	発生するなら chain 全体を -1 で更新する．
	 * 	発生しないなら，斜め隣接4マスの最小値にあわせる．
	 * 	斜め隣に黒マスがなければ，新しい番号をつける．
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
				if (getState(r + u,c + v) != BLACK) continue; // 黒マス以外はとばす
				if (isOnPeriphery(r, c) && chain[r + u][c + v] == 1) {
					newChain = -1; // 端のマスにいるとき番号1が見つかったら
				} 
				adjacent[k] = chain[r + u][c + v];
				for (int l = 0; l < k; l++) {
					if (adjacent[k] == adjacent[l]) // 同じ番号が見つかったら
						newChain = -1;
				}
				k++;
				if (chain[r + u][c + v] < newChain)
					newChain = chain[r + u][c + v];
			}
		}
		if (newChain == Integer.MAX_VALUE)
			chain[r][c] = ++maxChain; // 周囲に黒マスがないとき，新しい番号をつける
		else
			setChain(r, c, newChain); // 周囲に黒マスがあるとき，その最小番号をつける
	}

	/**
	 * 黒マスを取り消したときに，chainを更新する
	 * 全部計算しなおすことにする
	 * @param r
	 * @param c
	 */
	void cutChain(int r, int c) {
		initChain();
	}
	/**
	 * 	マスに chain番号を設定する
	 * 	斜め隣に黒マスがあれば同じ番号を設定する
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
					continue; // 黒マス以外はとばす
				if (chain[r + u][c + v] == n)
					continue; // 同じ番号があったらそのまま
				setChain(r + u, c + v, n);
			}
		}
	}
	
	/**
	 * 盤面全体で，縦横に連続する黒マスがないかどうかを調査する
	 * @return　連続する黒マスがなければ true, あれば false を返す　
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
			message.append("連続する黒マスがある\n");
		if ((result&2) == 2)
			message.append("黒マスにより盤面が分断されている\n");
		if ((result&4) == 4)
			message.append("数字と黒マス数の一致していない部屋がある\n");
		if ((result&8) == 8)
			message.append("白マスが３部屋以上続いている箇所がある\n");
		return message.toString();
	}

	/**
	 * １手の操作を表すクラス
	 * UNDO, REDO での編集の単位となる
	 */
	 class Step extends AbstractUndoableEdit {

		private int row;
		private int col;
		private int before;
		private int after;
		/**
		 * コンストラクタ
		 * @param r 変更されたマスの行座標
		 * @param c 変更されたマスの列座標
		 * @param b 変更前の状態
		 * @param a 変更後の状態
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
