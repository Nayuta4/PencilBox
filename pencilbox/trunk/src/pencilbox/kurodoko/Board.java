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
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (state[r][c] == BLACK || state[r][c] == WHITE)
					state[r][c] = UNKNOWN;
			}
		}
		initBoard();
	}

	public void trimAnswer() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getState(r, c) == WHITE)
					setState(r, c, UNKNOWN);
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
	 * @param r 行座標
	 * @param c 列座標
	 * @return 黒マスなら true を返す。
	 */
	public boolean isBlack(int r, int c) {
		return isOn(r, c) && state[r][c] == BLACK;
	}
	public boolean isWhiteOrNumber(int r, int c) {
		return state[r][c] == WHITE || state[r][c]>0 || state[r][c] == UNDECIDED_NUMBER;
	}

	public void initBoard() {
		initChain();
		initNumber();
	}
	void initNumber() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isNumber(r,c)) {
					number[r][c] = new Number(getState(r,c));
					initNumber(r, c);
				} else
					number[r][c] = null;
			}
		}
	}
	public Number getNumber(int r, int c) {
		return number[r][c];
	}
	
	public Number getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	
	public void setNumber(int r, int c, int n) {
		setState(r,c,n);
		number[r][c] = new Number(n);
		initNumber(r, c);
	}
	
	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
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
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, getState(p), st));
		int r=p.r(), c=p.c();
		int prevState = getState(r,c);
		setState(r,c,st);
		if (st == BLACK) {
			connectChain(r, c);
		} else if (prevState == BLACK) {
			cutChain(r, c);
		}
		updateSpace(r, c);
	}
	
	public void undo(AbstractStep step) {
		CellEditStep s = (CellEditStep)step;
		changeState(s.getPos(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		CellEditStep s = (CellEditStep)step;
		changeState(s.getPos(), s.getAfter());
	}

	/**
	 * そのマスの上下左右の隣接４マスに黒マスがあるかどうかを調べる
	 * @param r
	 * @param c
	 * @return 上下左右に黒マスがひとつでもあれば true
	 */
	boolean isBlock(int r, int c) {
		if (isBlack(r-1, c) || isBlack(r+1, c) || isBlack(r, c-1) || isBlack(r, c+1))
			return true;
		return false;
	}
	
	boolean isBlock(Address pos) {
		return isBlock(pos.r(), pos.c());
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
	
	int initNumber(int r0, int c0, int direction) {
		int n=0;
		Address pos = Address.address(r0, c0);
		while(true) {
			pos = pos.nextCell(direction);
			if (!isOn(pos.r(),pos.c()))
				break;
			if (isBlack(pos.r(), pos.c()))
				break;
			n++;
		};
		number[r0][c0].setNSpace(direction, n);

		pos = Address.address(r0, c0);
		n = 0;
		while(true) {
			pos = pos.nextCell(direction);
			if (!isOn(pos.r(),pos.c()))
				break;
			if (!isWhiteOrNumber(pos.r(), pos.c()))
				break;
			n++;
		};
		number[r0][c0].setNWhite(direction, n);

		if (number[r0][c0].tooSmallSpace()) return -1;
		if (number[r0][c0].tooLargeWhite()) return -1;
		return 0;
	}

	void initNumber(int r0, int c0) {
		  initNumber(r0, c0, Direction.UP);
		  initNumber(r0, c0, Direction.DN);
		  initNumber(r0, c0, Direction.LT);
		  initNumber(r0, c0, Direction.RT);
	}
	/**
	 * マスの状態を変更したときに，そのマスの上下左右の数字クラス属性を更新する
	 * space と white を両方更新する
	 * 誤り発生時に-1を，通常時に0を返す
	 */
	int updateSpace(int r0, int c0) {
		int ret = 0;
		for (int d=0; d<4; d++) {
			Address pos = Address.address(r0, c0);
			while(true) {
				pos = pos.nextCell(d);
				if (!isOn(pos.r(), pos.c()))
					break;
				if (isBlack(pos.r(), pos.c()))
					break;
				if (isNumber(pos.r(), pos.c())) {
					ret += initNumber(pos.r(), pos.c(), d^2);
				}
			}
		}
		if (ret<0) return -1;
		else return 0;
	}
	
	int getSumSpace(Address p) {
		return getNumber(p).getSumSpace();
	}
	int getSumWhite(Address p) {
		return getNumber(p).getSumWhite();
	}

	public int checkAnswerCode() {
		int result = 0;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isBlack(r, c)) {
					if (isBlock(r,c))
						result |= (1<<0);
					if (chain[r][c] == -1)
						result |= (1<<1);
				}
				if (isNumber(r, c)) {
					int remainder = number[r][c].getSumSpace() - number[r][c].getNumber();
					if (remainder < 0)
						result |= (1<<2);
					else if (remainder > 0)
						result |= (1<<3);
				}
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
