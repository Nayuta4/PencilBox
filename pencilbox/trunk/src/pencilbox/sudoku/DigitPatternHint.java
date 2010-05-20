package pencilbox.sudoku;

import pencilbox.common.core.Address;

/**
 * 可能数字をビットパターンで表現するヒント機能
 */
public class DigitPatternHint {

	private int allDigitPattern = 0x3fe;
	private Board board;
	private int[][] pattern;

	/**
	 * @param r row coordinate
	 * @param c colmun coordinate
	 * @return Returns the pattern.
	 */
	int getPattern(Address p) {
		return pattern[p.r()][p.c()];
	}

	/**
	 * クラスの初期化処理を行う
	 * 盤面生成時に呼ばれる
	 * @param board 関連付ける盤面
	 */
	void setupHint(Board board) {
		this.board = board;
		pattern = new int[board.rows()][board.cols()];
		int maxNumber = board.rows();
		allDigitPattern = ~((-1 << (maxNumber+1))+1);
	}
	/**
	 * 盤面全体の可能パターンを再計算する
	 */
	void initHint() {
		int rows = board.rows();
		int cols = board.cols();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				pattern[r][c] = allDigitPattern;
			}
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				int n = board.getNumber(r,c);
				if (n > 0)
					checkUsedNumber(r, c, n);
			}
		}
	}
	
	boolean canPlace(Address p, int n) {
		return (pattern[p.r()][p.c()] & (1<<n)) > 0;
	}
	/**
	 * [r0, c0]と同じ行，列，ボックスについて，数字nを使用済みとする
	 * @param r0
	 * @param c0
	 * @param n
	 */
	void checkUsedNumber(int r0, int c0, int n) {
		int rows = board.rows();
		int cols = board.cols();
		int unit = board.getUnit();
		int pat = ~(1 << n);
		for (int cc = 0; cc < cols; cc++) {
			pattern[r0][cc] &= pat;
		}
		for (int rr = 0; rr < rows; rr++) {
			pattern[rr][c0] &= pat;
		}
		int boxR = r0 / unit * unit;
		int boxC = c0 / unit * unit;
		for (int rr = 0; rr < unit; rr++) {
			for (int cc = 0; cc < unit; cc++) {
				pattern[boxR + rr][boxC + cc] &= pat;
			}
		}
	}
	/**
	 *	[r0, c0]にnを入れるときに変化があったときにhintを更新する
	 * @param r0
	 * @param c0
	 * @param n
	 */
	void updateHint(int r0, int c0, int n) {
		int prevNum = board.getNumber(r0, c0);
		if (prevNum > 0) {
			deleteHint(r0, c0, prevNum);
		}
		if (n > 0) {
			checkUsedNumber(r0, c0, n);
		}
	}
	/**
	 *	[r0, c0]に入っていた数字n0を消すときに，
	 * 盤面全体の可能パターンについて，数字 n に関する部分のみを再計算する
	 * @param r0
	 * @param c0
	 * @param n0
	 */
	void deleteHint(int r0, int c0, int n0) {
		int pat = (1 << n0);
		int rows = board.rows();
		int cols = board.cols();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				pattern[r][c] |= pat;
			}
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				int n = board.getNumber(r,c);
				if (n == n0) {
					if (r==r0 && c==c0) continue; // このマスはn0だが今から消すところ
					checkUsedNumber(r, c, n);
				}
			}
		}
	}
}
