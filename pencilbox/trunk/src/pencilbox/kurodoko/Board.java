package pencilbox.kurodoko;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;

/**
 * 「黒マスはどこだ」盤面クラス
 */
public class Board extends BoardBase {

	static final int HORIZ = Direction.HORIZ; 
	static final int VERT = Direction.VERT;

	static final int WHITE = -1;
	static final int BLACK = -2;
	static final int UNKNOWN = 0;
	static final int OUT = -3;
	static int UNDECIDED_NUMBER = -4;

	private int[][] state;
	private int[][] chain; // 黒マスの斜めつながりを記録する
	private int maxChain;
	private Number[][] number;

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		chain = new int[rows()][cols()];
		number = new Number[rows()][cols()];
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (!isNumber(p))
				setState(p,UNKNOWN);
		}
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == WHITE) {
				changeState(p, UNKNOWN);
			}
		}
		initNumber();
	}

	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}

	public int getState(int r, int c) {
		return state[r][c];
	}

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}

	public void setState(int r, int c, int st ) {
		state[r][c] = st;
	}

	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * 引数のマスが数字かどうか
	 */
	public boolean isNumber(int r, int c) {
		return state[r][c] > 0;
	}
	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
	}
	/**
	 * 引数の座標が黒マスかどうか。
	 * @param p 座標
	 * @return 黒マスなら true を返す。
	 */
	public boolean isBlack(Address p) {
		return isOn(p) && getState(p) == BLACK;
	}

	public boolean isWhiteOrNumber(Address p) {
		int n = getState(p);
		return n == WHITE || n>0 || n == UNDECIDED_NUMBER;
	}

	public void initBoard() {
		initChain();
		initNumber();
	}

	void initNumber() {
		for (Address p : cellAddrs()) {
			if (isNumber(p)) {
				setNumber(p, new Number(getState(p)));
				initNumber(p);
			} else {
				setNumber(p, null);
			}
		}
	}

	/**
	 * マス p0 から d 方向に見たときの白マスの数を調べる
	 * @param p0
	 */
	void initNumber(Address p0) {
		int n=1;
		for (int d=0; d<4; d++) {
			Address p = p0;
			while(isOn(p) && !isBlack(p)) {
				p = p.nextCell(d);
				if (!isOn(p))
					break;
				if (isBlack(p))
					break;
				n++;
			};
		}
		getNumber(p0).setNSpace(n);
		n=1;
		for (int d=0; d<4; d++) {
			Address p = p0;
			while(true) {
				p = p.nextCell(d);
				if (!isOn(p))
					break;
				if (!isWhiteOrNumber(p))
					break;
				n++;
			};
		}
		getNumber(p0).setNWhite(n);
	}

	public Number getNumber(Address p) {
		return number[p.r()][p.c()];
	}

	public void setNumber(Address p, Number n) {
		number[p.r()][p.c()] = n;
	}
	/**
	 * 黒マスの斜めつながり番号を返す
	 * @param p cell coordinate
	 * @return Returns the chain.
	 */
	int getChain(Address p) {
		return chain[p.r()][p.c()];
	}

	void setChain(Address p, int n) {
		chain[p.r()][p.c()] = n;
	}

	/**
	 * マスの状態を指定した状態に変更し，変更をアンドゥリスナーに通知する
	 * @param p マス座標
	 * @param st 変更後の状態
	 */
	public void changeState(Address p, int st) {
		int prev = getState(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		setState(p, st);
		if (st == BLACK) {
			connectChain(p);
		} else if (prev == BLACK) {
			cutChain(p);
		}
		if (st > 0) {
			setNumber(p, new Number(st));
			initNumber(p);
		} else if (prev > 0) {
			setNumber(p, null);
		}
		if (st == BLACK || prev == BLACK)
			updateSpace(p);
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getAfter());
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
	/**
	 * マスの状態を変更したときに，そのマスの上下左右の数字マスを探して白マス数を数え直す
	 */
	void updateSpace(Address p0) {
		for (int d=0; d<4; d++) {
			Address p = p0;
			while(true) {
				p = Address.nextCell(p, d);
				if (!isOn(p))
					break;
				if (isBlack(p))
					break;
				if (isNumber(p)) {
					initNumber(p);
				}
			}
		}
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (isBlack(p)) {
				if (isBlock(p))
					result |= (1<<0);
				if (getChain(p) == -1)
					result |= (1<<1);
			}
			if (isNumber(p)) {
				int remainder = getNumber(p).getNSpace() - getNumber(p).getNumber();
				if (remainder < 0)
					result |= (1<<2);
				else if (remainder > 0)
					result |= (1<<3);
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
			message.append(ERR_CONTINUOUS_BLACK);
		if ((result&2) == 2)
			message.append(ERR_DIVIDED_BOARD);
		if ((result&4) == 4)
			message.append(ERR_SMALL_SIZE);
		if ((result&8) == 8)
			message.append(YET_LARGE_SIZE);
		return message.toString();
	}

	static final String ERR_CONTINUOUS_BLACK = Messages.getString("kurodoko.AnswerCheckMessage1"); //$NON-NLS-1$
	static final String ERR_DIVIDED_BOARD = Messages.getString("kurodoko.AnswerCheckMessage2"); //$NON-NLS-1$
	static final String ERR_SMALL_SIZE = Messages.getString("kurodoko.AnswerCheckMessage3"); //$NON-NLS-1$
	static final String YET_LARGE_SIZE = Messages.getString("kurodoko.AnswerCheckMessage4"); //$NON-NLS-1$
}
