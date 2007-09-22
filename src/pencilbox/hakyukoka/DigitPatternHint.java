package pencilbox.hakyukoka;

import pencilbox.common.core.Address;


/**
 * 可能数字をビットパターンで表現するヒント機能
 */
public class DigitPatternHint {

	private Board board;
	private int[][] pattern;
	private int maxNumber = 9;  // 暫定
	
	/**
	 * @param r row coordinate
	 * @param c colmun coordinate
	 * @return Returns the pattern.
	 */
	int getPattern(int r, int c) {
		return pattern[r][c];
	}

	/**
	 * クラスの初期化処理を行う
	 * 盤面生成時に呼ばれる
	 * @param board 関連付ける盤面
	 */
	void setupHint(Board board) {
		this.board = board;
		pattern = new int[board.rows()][board.cols()];
	}
	/**
	 * パターンのupdate
	 */
	void initHint() {
		int rows = board.rows();
		int cols = board.cols();
		Area area;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				area = board.getArea(r,c);
				if (area != null)
					pattern[r][c] = getAllDigitPattern(board.getArea(r,c).size());
				else
					pattern[r][c] = getAllDigitPattern(maxNumber);
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
	/**
	 * 1 から number までのすべての数字が可能という digitPattern を返す
	 * @param number 最大の数字
	 * @return 1 から number までのすべての数字が可能という digitPattern を返す
	 */
	private int getAllDigitPattern(int number) {
		return ~((-1 << (number+1))+1);
	}
	
	/**
	 * 指定した座標に指定した数字を配置してもルールに違反しないかどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @param n 配置可能かを調べる数字
	 * @return 配置可能なら true 配置不可なら false
	 */
	boolean canPlace(int r, int c, int n) {
		return (pattern[r][c] & (1<<n)) > 0;
	}
	/**
	 * r0行 c0列 に 数字 num が入っていることにより，
	 * 配置不可能となる数字を可能パターンから外す
	 * @param r0
	 * @param c0
	 * @param n
	 */
	void checkUsedNumber(int r0, int c0, int n) {
		int pat = ~((1 << n) + 1);  // num桁以外が1
		for (int c = c0-n; c <= c0+n; c++) {
			if (board.isOn(r0,c))
				pattern[r0][c] &= pat;
		}
		for (int r = r0-n; r <= r0+n; r++) {
			if (board.isOn(r,c0))
				pattern[r][c0] &= pat;
		}
		Area area = board.getArea(r0,c0);
		if (area != null) {
			for (Address pos : board.getArea(r0,c0)) {
				pattern[pos.r()][pos.c()] &= pat;
			}
		}
	}
}
