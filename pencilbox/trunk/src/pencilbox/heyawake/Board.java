package pencilbox.heyawake;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SquareEditStep;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;
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
	private List<Square> squareList;  // 部屋

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
		squareList = new LinkedList<Square>();
		contH = new int[rows()][cols()];
		contV = new int[rows()][cols()];
		contWH = new int[rows()][cols()];
		contWV = new int[rows()][cols()];
		chain = new int[rows()][cols()];
		maxChain = 1;
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt2(state, UNKNOWN);
		for (int n=squareList.size()-1; n>=0; n--) {
			squareList.get(n).clear();
		}
		initCont();
		ArrayUtil.initArrayInt2(chain,0);
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == WHITE) {
				changeState(p, UNKNOWN);
			}
		}
		initCont();
		initRoomCount();
	}

	public void initBoard() {
		initCont();
		initChain();
		initRoomCount();
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

	public Square getSquare(Address pos) {
		return square[pos.r()][pos.c()];
	}

	public void setSquare(int r, int c, Square sq) {
		square[r][c] = sq;
	}

	public void setSquare(Address pos, Square sq) {
		square[pos.r()][pos.c()] = sq;
	}

	List<Square> getSquareList() {
		return squareList;
	}
	/**
	 * マスの状態を取得
	 * @param r
	 * @param c
	 * @return マスの状態
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
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

	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * 引数の座標が黒マスかどうか。
	 * @param p 座標
	 * @return 黒マスなら true を返す。
	 */
	public boolean isBlack(Address p) {
		return isOn(p) && getState(p) == BLACK;
	}

	public int getCont(Address p, int dir) {
		if (dir == Direction.VERT) {
			return contV[p.r()][p.c()];
		} else if (dir == Direction.HORIZ) {
			return contH[p.r()][p.c()];
		} else {
			return -1;
		}
	}

	public void setCont(Address p, int dir, int v) {
		if (dir == Direction.VERT) {
			contV[p.r()][p.c()] = v;
		} else if (dir == Direction.HORIZ) {
			contH[p.r()][p.c()] = v;
		}
	}

	public void setCont(int r, int c, int dir, int v) {
		if (dir == Direction.VERT) {
			contV[r][c] = v;
		} else if (dir == Direction.HORIZ) {
			contH[r][c] = v;
		}
	}

	public int getContW(Address p, int dir) {
		if (dir == Direction.VERT) {
			return contWV[p.r()][p.c()];
		} else if (dir == Direction.HORIZ) {
			return contWH[p.r()][p.c()];
		} else {
			return -1;
		}
	}

	public void setContW(Address p, int dir, int v) {
		if (dir == Direction.VERT) {
			contWV[p.r()][p.c()] = v;
		} else if (dir == Direction.HORIZ) {
			contWH[p.r()][p.c()] = v;
		}
	}

	public void setContW(int r, int c, int dir, int v) {
		if (dir == Direction.VERT) {
			contWV[r][c] = v;
		} else if (dir == Direction.HORIZ) {
			contWH[r][c] = v;
		}
	}

	int getChain(Address p) {
		return chain[p.r()][p.c()];
	}

	void setChain(Address p, int n) {
		chain[p.r()][p.c()] = n;
	}

	void initCont() {
		ArrayUtil.initArrayInt2(contH, 0);
		ArrayUtil.initArrayInt2(contV, 0);
		ArrayUtil.initArrayInt2(contWH, 0);
		ArrayUtil.initArrayInt2(contWV, 0);
		for (Address p : cellAddrs()) {
			if (getState(p) != BLACK) {
				for (int dir = 0; dir < 2; dir++) {
					if (getCont(p, dir) == 0) {
						countContinuousRoom(p, dir);
					}
				}
			}
			if (getState(p) == WHITE) {
				for (int dir = 0; dir < 2; dir++) {
					if (getCont(p, dir) == 0) {
						countContinuousRoomW(p, dir);
					}
				}
			}
		}
	}

	void initRoomCount() {
		for (Address p : cellAddrs()) {
			Square room = getSquare(p);
			if (room!=null) {
				room.setNBlack(0);
				room.setNWhite(0);
			}
		}
		for (Address p : cellAddrs()) {
			Square room = getSquare(p);
			if (room!=null)
				if (getState(p)==BLACK) {
					room.setNBlack(room.getNBlack() + 1);
				} else if (getState(p)==WHITE){
					room.setNWhite(room.getNWhite() + 1);
			}
		}
	}

	/**
	 * マスの状態を指定した状態に変更し，変更をアンドゥリスナーに通知する
	 * @param p マスの座標
	 * @param st 変更後の状態
	 */
	public void changeState(Address p, int st) {
		int prev = getState(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.STATE, p, prev, st));
		setState(p, st);
		if (st == BLACK) {
			setCont(p, Direction.HORIZ, 0);
			setCont(p, Direction.VERT, 0);
			for (int d = 0; d < 4; d++) {
				Address p1 = Address.nextCell(p, d);
				if (isOn(p1)) {
					countContinuousRoom(p1, d&1);
				}
			}
			connectChain(p);
		}
		if (prev == BLACK) {
			countContinuousRoom(p, Direction.VERT);
			countContinuousRoom(p, Direction.HORIZ);
			cutChain(p);
		}
		if (st == WHITE) {
			countContinuousRoomW(p, Direction.VERT);
			countContinuousRoomW(p, Direction.HORIZ);
		}
		if (prev == WHITE) {
			setContW(p, Direction.HORIZ, 0);
			setContW(p, Direction.VERT, 0);
			for (int d = 0; d < 4; d++) {
				Address p1 = Address.nextCell(p, d);
				if (isOn(p1)) {
					countContinuousRoomW(p1, d&1);
				}
			}
		}
		Square room = getSquare(p);
		if (room!=null) {
			if (st==BLACK) {
				room.setNBlack(room.getNBlack() + 1);
			} else if (st==WHITE){
				room.setNWhite(room.getNWhite() + 1);
			}
			if (prev==BLACK) {
				room.setNBlack(room.getNBlack() - 1);
			} else if (prev==WHITE){
				room.setNWhite(room.getNWhite() - 1);
			}
		}
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (step.getType() == EditType.STATE) {
				changeState(s.getPos(), s.getBefore());
			} else if (step.getType() == EditType.NUMBER) {
				changeNumber(s.getPos(), s.getBefore());
			}
		} else if (step instanceof SquareEditStep) {
			SquareEditStep s = (SquareEditStep) step;
			if (s.getOperation() == SquareEditStep.ADDED) {
				removeSquare(getSquare(s.getQ0()));
			} else if (s.getOperation() == SquareEditStep.REMOVED) {
				addSquare(new Square(s.getP0(), s.getP1()));
			} else if (s.getOperation() == SquareEditStep.CHANGED) {
				changeSquare(getSquare(s.getQ0()), s.getP0(), s.getP1());
			}
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (step.getType() == EditType.STATE) {
				changeState(s.getPos(), s.getAfter());
			} else if (step.getType() == EditType.NUMBER) {
				changeNumber(s.getPos(), s.getAfter());
			}
		} else if (step instanceof SquareEditStep) {
			SquareEditStep s = (SquareEditStep) step;
			if (s.getOperation() == SquareEditStep.ADDED) {
				addSquare(new Square(s.getQ0(), s.getQ1()));
			} else if (s.getOperation() == SquareEditStep.REMOVED) {
				removeSquare(getSquare(s.getP0()));
			} else if (s.getOperation() == SquareEditStep.CHANGED) {
				changeSquare(getSquare(s.getP0()), s.getQ0(), s.getQ1());
			}
		}
	}

	/**
	 * 四角を追加，変更したときにすでにある他の四角と重なる場合，その四角を除去する。
	 * @param sq 追加,変更する四角
	 * @param org 変更する場合のもとの四角
	 */
	public void removeOverlappedSquares(Square sq, Square org) {
		for (Address p : sq.cellSet()) {
			Square s = getSquare(p);
			if (s != null && s != org) {
				removeSquare(s);
			}
		}
	}

	/**
	 * 四角形の範囲に含まれるマスにSquareオブジェクトを設定する
	 * @param region 設定する四角形の範囲
	 * @param sq 設定するSquareオブジェクト
	 */
	public void setSquare(Square region, Square sq) {
		for (Address p : region.cellSet()) {
			setSquare(p, sq);
		}
	}
	/**
	 * 四角を追加する
	 * @param sq 追加する四角
	 */
	public void addSquare(Square sq) {
		if (isRecordUndo())
			fireUndoableEditUpdate(new SquareEditStep(sq.p0(), sq.p1(), SquareEditStep.ADDED));
		setSquare(sq, sq);
		squareList.add(sq);
	}
	/**
	 * 四角を変更する
	 * @param sq 変更される四角
	 * @param q0 変更後の四角の角の座標
	 * @param q1 変更後の四角の角の座標
	 */
	public void changeSquare(Square sq, Address q0, Address q1) {
		if (isRecordUndo())
			fireUndoableEditUpdate(new SquareEditStep(sq.p0(), sq.p1(), q0, q1, SquareEditStep.CHANGED));
		setSquare(sq, null);
		sq.set(q0, q1);
		setSquare(sq, sq);
	}
	/**
	 * 四角を消去する
	 * @param sq 消去する四角
	 */
	public void removeSquare(Square sq) {
		changeNumber(sq.p0(), Square.ANY);
		if (isRecordUndo())
			fireUndoableEditUpdate(new SquareEditStep(sq.p0(), sq.p1(), SquareEditStep.REMOVED));
		setSquare(sq, null);
		squareList.remove(sq);
	}
	/**
	 * 部屋の数字を変更する。
	 * @param p
	 * @param n
	 */
	public void changeNumber(Address p, int n) {
		Square square = getSquare(p);
		if (square == null)
			return;
		int prev = square.getNumber();
		if (prev == n)
			return;
		square.setNumber(n);
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new CellEditStep(EditType.NUMBER, p, prev, n));
		}
	}
	/**
	 * そのマスの上下左右の隣接４マスに黒マスがあるかどうかを調べる
	 * @param p
	 * @return 上下左右に黒マスがひとつでもあれば true
	 */
	boolean isBlock(Address p) {
		for (int d=0; d<4; d++) {
			if (isBlack(Address.nextCell(p, d)))
				return true;
		}
		return false;
	}
	/**
	 * 引数のマスから縦横に連続する部屋の個数を数えて設定する
	 * @param p0 起点マス
	 * @param dir 縦か横か
	 */
	void countContinuousRoom(Address p0, int dir) {
		int count = 0;
		Square sq = null;
		Address p = p0;
		while (isOn(p) && getState(p) != BLACK) {
			p = Address.nextCell(p, dir);
		}
		p = Address.nextCell(p, dir^2);
		while (isOn(p) && getState(p) != BLACK) {
			Square room = getSquare(p);
			if (sq == null || room != sq) {
				count++;
				sq = room;
			}
			p = Address.nextCell(p, dir^2);
		}
		p = Address.nextCell(p, dir);
		while (isOn(p) && getState(p) != BLACK) {
			setCont(p, dir, count);
			p = Address.nextCell(p, dir);
		}
	}
	/**
	 * 引数のマスから縦横に連続する部屋の個数を数えて設定する
	 * @param p0 起点マス
	 * @param dir 縦か横か
	 */
	void countContinuousRoomW(Address p0, int dir) {
		int count = 0;
		Square sq = null;
		Address p = p0;
		while (isOn(p) && getState(p) == WHITE) {
			p = Address.nextCell(p, dir);
		}
		p = Address.nextCell(p, dir^2);
		while (isOn(p) && getState(p) == WHITE) {
			Square room = getSquare(p);
			if (room != sq) {
				count++;
				sq = room;
			}
			p = Address.nextCell(p, dir^2);
		}
		p = Address.nextCell(p, dir);
		while (isOn(p) && getState(p) == WHITE) {
			setContW(p, dir, count);
			p = Address.nextCell(p, dir);
		}
	}

	/**
	 * 	chain配列を初期化する
	 */
	void initChain() {
		maxChain = 1;
		for (Address p : cellAddrs()) {
			setChain(p, 0);
		}
		for (Address p : cellAddrs()) {
			if (isOnPeriphery(p)) {
				if (isBlack(p) && getChain(p) == 0) {
					if (initChain1(p, -1, 1) == -1) {
						updateChain(p, -1);
					}
				}
			}
		}
		for (Address p : cellAddrs()) {
			if (!isOnPeriphery(p)) {
				if (isBlack(p) && getChain(p) == 0) {
					if (initChain1(p, -1, ++maxChain) == -1) {
						updateChain(p, -1);
					}
				}
			}
		}
	}
	/**
	 * 斜めにつながる黒マスをたどり，chain に番号 n を設定する
	 * 分断を発見したら，その時点で -1 を返して戻る
	 * @param p 今のマス
	 * @param d 呼び出し元のマスから今のマスを見た向き，このマスが初めなら -1
	 * @param n 設定する値
	 * @return 盤面の分断を発見したら -1 , そうでなければ n と同じ値
	 */
	int initChain1(Address p, int d, int n) {
		if (n == 1 && d != -1 && isOnPeriphery(p)) { // 輪が外周に達した
			return -1;
		}
		if (n >= 0 && isOnPeriphery(p)) {
			setChain(p, 1);
		} else {
			setChain(p, n);
		}
		for (int dd : Direction.DIAGONAL4) {
			Address pp = Address.nextCell(p, dd);
			if (dd == (d^2))
				continue; // 今来たところはとばす
			if (!isBlack(pp))
				continue; // 黒マス以外はとばす
			if (getChain(pp) == n) // 輪が閉じた
				return -1;
			if (initChain1(pp, dd, n) == -1)
				return -1;
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
	 * @param p マスの座標
	 */
	void connectChain(Address p) {
		int[] adjacent = adjacentChain;
		int k = 0;
		int newChain = Integer.MAX_VALUE;
		if (isOnPeriphery(p))
			newChain = 1;
		for (int dd : Direction.DIAGONAL4) {
			Address pp = Address.nextCell(p, dd);
			if (!isBlack(pp))
				continue; // 黒マス以外はとばす
			int c1 = getChain(pp);
			if (isOnPeriphery(p) && c1 == 1) {
				newChain = -1; // 端のマスにいるとき番号1が見つかったら分断された
			} 
			adjacent[k] = c1;
			for (int l = 0; l < k; l++) {
				if (adjacent[k] == adjacent[l]) // 同じ番号が見つかったら分断された
					newChain = -1;
			}
			k++;
			if (c1 < newChain)
				newChain = c1;
		}
		if (newChain == Integer.MAX_VALUE)
			setChain(p, ++maxChain); // 周囲に黒マスがないとき，新しい番号をつける
		else
			updateChain(p, newChain); // 周囲に黒マスがあるとき，その最小番号をつける
	}
	/**
	 * 黒マスを取り消したときに，chainを更新する
	 * 全部計算しなおすことにする
	 * @param p
	 */
	void cutChain(Address p) {
		initChain();
	}
	/**
	 * 	マスに chain番号を設定する
	 * 	斜め隣に黒マスがあれば同じ番号を設定する
	 * @param p マスの座標
	 * @param n 設定する値
	 */
	void updateChain(Address p, int n) {
		setChain(p, n);
		for (int dd : Direction.DIAGONAL4) {
			Address pp = Address.nextCell(p, dd);
			if (!isBlack(pp))
				continue; // 黒マス以外はとばす
			if (getChain(pp) == n)
				continue; // 同じ番号があったらそのまま
			updateChain(pp, n);
		}
	}

	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getState(p) == BLACK) {
				if (isBlock(p))
					result |= 1;
				if (getChain(p) == -1)
					result |= 2;
			}
		}
		for (Address p : cellAddrs()) {
			if (getCont(p, Direction.HORIZ) >= 3 || getCont(p, Direction.VERT) >= 3) {
				result |= 8;
			}
		}
		for (Square sq : squareList) {
			if (sq.getNumber()>=0 && sq.getNumber() != sq.getNBlack()) {
				result |= 4;
			}
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result&1) == 1)
			message.append(Messages.getString("heyawake.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result&2) == 2)
			message.append(Messages.getString("heyawake.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result&4) == 4)
			message.append(Messages.getString("heyawake.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result&8) == 8)
			message.append(Messages.getString("heyawake.AnswerCheckMessage4")); //$NON-NLS-1$
		return message.toString();
	}
}
