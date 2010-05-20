package pencilbox.hitori;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 * 「ひとりにしてくれ」盤面クラス
 */
public class Board extends BoardBase {

	static final int WHITE = -1;
	static final int BLACK = -2;
	static final int UNKNOWN = 0;
	static final int UNDECIDED_NUMBER = -1;

	private int[][] state;
	private int[][] number;
	private int[][] multiH; // 横方向の重複数
	private int[][] multiV; // 縦方向の重複数
	private boolean[][] single; // 最初からひとりか
	private int[][] chain; // 黒マス斜め連鎖
	private int maxNumber; // 使用可能数字数
	private int maxChain = 1; // 現在使用している最大のchain番号

	protected void setup() {
		super.setup();
		int rows = rows();
		int cols = cols();
		number = new int[rows][cols];
		state = new int[rows][cols];
		single = new boolean[rows][cols];
		multiH = new int[rows][cols];
		multiV = new int[rows][cols];
		chain = new int[rows][cols];
		maxNumber = (rows > cols) ? rows : cols;
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt2(state, UNKNOWN);
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == WHITE)
				setState(p, UNKNOWN);
		}
	}

	public void initBoard() {
		initSingle();
		initMulti();
		initChain();
	}
	
	/**
	 * マスの状態を取得する
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return マスの状態を表す state[r][c] の値
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}
	
	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * マスの状態を設定する
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @param st state[r][c] に設定する値
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	
	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * マスの数字を取得する
	 * @param r row coordinate of the cell.
	 * @param c columun coordinate of the cell.
	 * @return Returns the number of the cell.
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}
	
	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	/**
	 * マスに数字を設定する
	 * @param r row coordinate of the cell.
	 * @param c columun coordinate of the cell.
	 * @param n the number to set.
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}
	
	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
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
		return isOn(p) && state[p.r()][p.c()] == BLACK;
	}
	/**
	 * そのマスと縦または横の同じ列に，黒マスで消されていない同じ数字があるか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 黒マスで消されていない同じ数字があれば true, なければ false
	 */
	public boolean isRedundantNumber(Address p) {
		return multiH[p.r()][p.c()] > 1 || multiV[p.r()][p.c()] > 1;
	}
	/**
	 * そのマスの数字が最初からひとりかどうか，
	 * つまり縦横の同じ列に同一の数字が存在しないかどうかを返す
	 * @param r 行座標 
	 * @param c 列座標
	 * @return 最初からひとりなら true, そうでなければ false
	 */
	public boolean isSingle(Address p) {
		return single[p.r()][p.c()];
	}
	int getChain(Address p) {
		return chain[p.r()][p.c()];
	}
	
	/**
	 *  マスを黒マスに確定する
	 *  以前の状態が黒マス確定でないことは前提とする
	 *  hmulit, vmulti, chain を更新する
	 * @param r 行座標
	 * @param c 列座標
	 */
	void setBlack(int r, int c) {
		state[r][c] = BLACK;
		decreseMulti(r, c);
		connectChain(r,c);
	}
	/**
	 *  マスを白マスに確定する
	 *  以前の状態がシロマス確定でないことを前提とする
	 *  hmulit, vmulti, chain を更新する
	 * @param r 行座標
	 * @param c 列座標
	 */
	void setWhite(int r, int c) {
		int before = state[r][c];
		state[r][c] = WHITE;
		if (before == BLACK) {
			increaseMulti(r, c);
			cutChain(r, c);
		}
	}

	/**
	 *  マスの確定を取り消し未定状態とする
	 *  以前の状態が未定状態でないことを前提とする
	 *  hmulit, vmulti, chain を更新する
	 * @param r 行座標
	 * @param c 列座標
	 */
	public void erase(int r, int c) {
		int before = getState(r,c);
		state[r][c] = UNKNOWN;
		if (before == BLACK) {
			increaseMulti(r, c);
			cutChain(r, c);
		}
	}
	/**
	 * マスの状態を指定した状態に変更し，変更をアンドゥリスナーに通知する
	 * @param pos マス座標
	 * @param st 変更後の状態
	 */
	public void changeState(Address p, int st) {
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, getState(p), st));
		int r = p.r();
		int c = p.c();
		if (st == BLACK) setBlack(r,c);
		else if (st == WHITE) setWhite(r,c);
		else if (st == UNKNOWN) erase(r,c);
//		initChain();
//		checkContinuousBlack();
	}
	
	public void undo(AbstractStep step) {
		CellEditStep s = (CellEditStep) step;
		changeState(s.getPos(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		CellEditStep s = (CellEditStep) step;
		changeState(s.getPos(), s.getAfter());
	}

	/**
	 * そのマスの上下左右の隣接４マスに黒マスがあるかどうかを調べる
	 * @param r
	 * @param c
	 * @return 上下左右に黒マスがひとつでもあれば true
	 */
	boolean isBlock(Address p) {
		for (int d=0; d<4; d++) {
			if (isBlack(p.nextCell(d)))
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
				if (isOnPeriphery(r, c)) {
					if (isBlack(r, c) && chain[r][c] == 0) {
						if (initChain1(r, c, 0, 0, 1) == -1) {
							updateChain(r, c, -1);
						}
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
		if (isOnPeriphery(r,c)) newChain = 1;
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
	 * 黒マスを取り消したときに，斜め隣のchainを更新する．
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

	/**
	 * 黒マスにより盤面が分断されていないかどうかを調査する
	 * @return 黒マスにより盤面が分断されていなければ true 分断されていれば false を返す
	 */
	boolean checkDivision() {
		boolean ret = true;
		for (Address p : cellAddrs()) {
			if (getChain(p) == -1) {
				ret = false;
			}
		}
		return ret;
	}
	/**
	 * 盤面全体で，縦横に連続する黒マスがないかどうかを調査する
	 * @return　連続する黒マスがなければ true, あれば false を返す　
	 */
	boolean checkContinuousBlack() {
		boolean ret = true;
		for (Address p : cellAddrs()) { 
			if (isBlack(p)) {
				if (isBlock(p)) {
					ret = false;
				}
			}
		}
		return ret;
	}
	/**
	 * 盤面全体で，縦横に黒マスで消されずに重複する数字がないかを調査する
	 * @return 重複数字がなければ true あれば false
	 */
	boolean checkMulti() {
		for (Address p : cellAddrs()) {
			if (isRedundantNumber(p))
				return false;
		}
		return true;
	}
	
	public int checkAnswerCode() {
		int result = 0;
		if (!checkContinuousBlack())
			result |= 1;
		if (!checkDivision())
			result |= 2;
		if (!checkMulti())
			result |= 4;
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append ( Messages.getString("hitori.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append (Messages.getString("hitori.AnswerCheckMessage2")) ; //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append (Messages.getString("hitori.AnswerCheckMessage3")) ; //$NON-NLS-1$
		return message.toString();
	}

	/**
	 *  single配列を初期化する
	 */
	void initSingle() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				single[r][c] = true;
			}
		}
		int[] used = new int[maxNumber + 1];
		for (int r = 0; r < rows(); r++) {
			for (int i = 1; i <= maxNumber; i++)
				used[i] = 0;
			for (int c = 0; c < cols(); c++) {
				used[number[r][c]]++;
			}
			for (int c = 0; c < cols(); c++) {
				if (used[number[r][c]] > 1)
					single[r][c] = false;
			}
		}
		for (int c = 0; c < cols(); c++) {
			for (int i = 1; i <= maxNumber; i++)
				used[i] = 0;
			for (int r = 0; r < rows(); r++) {
				used[number[r][c]]++;
			}
			for (int r = 0; r < rows(); r++) {
				if (used[number[r][c]] > 1)
					single[r][c] = false;
			}
		}
	}
	/**
	 * 	hmulti, vmulti 配列を初期化する
	 */
	void initMulti() {
		int[] used = new int[maxNumber + 1];
		for (int r = 0; r < rows(); r++) {
			for (int i = 0; i <= maxNumber; i++)
				used[i] = 0;
			for (int c = 0; c < cols(); c++) {
				if (state[r][c] != BLACK && number[r][c] > 0)
					used[number[r][c]]++;
			}
			for (int c = 0; c < cols(); c++) {
				multiH[r][c] = used[number[r][c]];
			}
		}
		for (int c = 0; c < cols(); c++) {
			for (int i = 0; i <= maxNumber; i++)
				used[i] = 0;
			for (int r = 0; r < rows(); r++) {
				if (state[r][c] != BLACK && number[r][c] > 0)
					used[number[r][c]]++;
			}
			for (int r = 0; r < rows(); r++) {
				multiV[r][c] = used[number[r][c]];
			}
		}
	}
	/**
	 * 	マス(rr,cc)を黒で確定したときに，同じ行，列の hmulti, vmulti を1減らす
	 * @param r0 状態を変更したマスの行座標
	 * @param c0 状態を変更したマスの列座標
	 */
	private void decreseMulti(int r0, int c0) {
		for (int c = 0; c < cols(); c++) {
			if (number[r0][c] == number[r0][c0]) {
				multiH[r0][c]--;
			}
		}
		for (int r = 0; r < rows(); r++) {
			if (number[r][c0] == number[r0][c0]) {
				multiV[r][c0]--;
			}
		}
	}
	/**
	 * マス(rr,cc)を黒を消したときに，同じ行，列の hmulti, vmulti を1増やす
	 * @param r0 状態を変更したマスの行座標
	 * @param c0 状態を変更したマスの列座標
	 */
	private void increaseMulti(int r0, int c0) {
		for (int c = 0; c < cols(); c++) {
			if (number[r0][c] == number[r0][c0]) {
				multiH[r0][c]++;
			}
		}
		for (int r = 0; r < rows(); r++) {
			if (number[r][c0] == number[r0][c0]) {
				multiV[r][c0]++;
			}
		}
	}

	/**
	 * @return Returns the maxNumber.
	 */
	int getMaxNumber() {
		return maxNumber;
	}
	/**
	 * @return Returns the chain.
	 */
	int[][] getChain() {
		return chain;
	}
	/**
	 * @return Returns the number.
	 */
	int[][] getNumber() {
		return number;
	}
	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}
}
