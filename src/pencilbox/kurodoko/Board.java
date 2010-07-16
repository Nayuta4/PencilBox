package pencilbox.kurodoko;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;

/**
 * 「黒マスはどこだ」盤面クラス
 */
public class Board extends BoardBase {

	public static final int HORIZ = Direction.HORIZ; 
	public static final int VERT = Direction.VERT;

	public static final int WHITE = -1;
	public static final int BLACK = -2;
	public static final int UNKNOWN = 0;
	public static final int OUT = -3;
	public static int UNDECIDED_NUMBER = -4;

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
			if (getState(p) == WHITE)
				setState(p, UNKNOWN);
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
	 * @param r 行座標
	 * @param c 列座標
	 * @return 黒マスなら true を返す。
	 */
	public boolean isBlack(int r, int c) {
		return isOn(r, c) && state[r][c] == BLACK;
	}

	public boolean isBlack(Address p) {
		return isOn(p) && (getState(p) == BLACK);
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
	
	public Number getNumber(Address p) {
		return number[p.r()][p.c()];
	}
	
	public void setNumber(Address p, Number n) {
		number[p.r()][p.c()] = n;
	}
	/**
	 * 黒マスの斜めつながり番号を返す
	 * @param r row coordinate
	 * @param c column coordinate
	 * @return Returns the chain.
	 */
	int getChain(int r, int c) {
		return chain[r][c];
	}
	int getChain(Address p) {
		return chain[p.r()][p.c()];
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
		int r=p.r(), c=p.c();
		setState(p, st);
		if (st == BLACK) {
			connectChain(r, c);
		} else if (prev == BLACK) {
			cutChain(r, c);
		}
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
		ArrayUtil.initArrayInt2(chain,0);
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (!isOnPeriphery(r, c))
					continue;
				if (isBlack(r, c) && chain[r][c] == 0) {
					if (initChain1(r, c, 0, 0, 1) == -1) {
						updateChain(r, c, -1);
					}
				}
			}
		}
		for (int r = 1; r < rows() - 1; r++) {
			for (int c = 1; c < cols() - 1; c++) {
				if (isBlack(r, c) && chain[r][c] == 0) {
					if (initChain1(r, c, 0, 0, ++maxChain) == -1) {
						updateChain(r, c, -1);
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
		for (int u = -1; u < 2; u += 2) {
			for (int v = -1; v < 2; v += 2) {
				if (!isBlack(r + u, c + v))
					continue; // 黒マス以外はとばす
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
			updateChain(r, c, newChain); // 周囲に黒マスがあるとき，その最小番号をつける
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
	void updateChain(int r, int c, int n) {
		chain[r][c] = n;
		for (int u = -1; u < 2; u += 2) {
			for (int v = -1; v < 2; v += 2) {
				if (!isBlack(r + u, c + v))
					continue; // 黒マス以外はとばす
				if (chain[r + u][c + v] == n)
					continue; // 同じ番号があったらそのまま
				updateChain(r + u, c + v, n);
			}
		}
	}
	
	int initNumber(Address p0, int d) {
		int n=0;
		Address p = p0;
		while(true) {
			p = p.nextCell(d);
			if (!isOn(p))
				break;
			if (isBlack(p))
				break;
			n++;
		};
		getNumber(p0).setNSpace(d, n);

		p = p0;
		n = 0;
		while(true) {
			p = p.nextCell(d);
			if (!isOn(p))
				break;
			if (!isWhiteOrNumber(p))
				break;
			n++;
		};
		getNumber(p0).setNWhite(d, n);

		if (getNumber(p0).tooSmallSpace()) return -1;
		if (getNumber(p0).tooLargeWhite()) return -1;
		return 0;
	}

	void initNumber(Address p0) {
		for (int d=0; d<4; d++) {
		  initNumber(p0, d);
		}
	}
	/**
	 * マスの状態を変更したときに，そのマスの上下左右の数字クラス属性を更新する
	 * space と white を両方更新する
	 * 誤り発生時に-1を，通常時に0を返す
	 */
	int updateSpace(Address p0) {
		int ret = 0;
		for (int d=0; d<4; d++) {
			Address p = p0;
			while(true) {
				p = Address.nextCell(p, d);
				if (!isOn(p))
					break;
				if (isBlack(p))
					break;
				if (isNumber(p)) {
					ret += initNumber(p, d^2);
				}
			}
		}
		if (ret<0)
			return -1;
		else
			return 0;
	}
	
	int getSumSpace(Address p) {
		return getNumber(p).getSumSpace();
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
				int remainder = getNumber(p).getSumSpace() - getNumber(p).getNumber();
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
