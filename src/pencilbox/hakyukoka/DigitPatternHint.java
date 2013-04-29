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
	 * @param p coordinate
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
	}
	/**
	 * パターンのupdate
	 */
	void initHint() {
		for (Address p : board.cellAddrs()) {
			Area area = board.getArea(p);
			if (area != null)
				pattern[p.r()][p.c()] = getAllDigitPattern(board.getArea(p).size());
			else
				pattern[p.r()][p.c()] = getAllDigitPattern(maxNumber);
		}
		for (Address p : board.cellAddrs()) {
			int n = board.getNumberOrState(p);
			if (n > 0)
				checkUsedNumber(p, n);
		}
	}
	/**
	 * 1 から number までのすべての数字が可能という digitPattern を返す
	 * @param number 最大の数字
	 * @return 1 から number までのすべての数字が可能という digitPattern を返す
	 */
	private static int getAllDigitPattern(int number) {
		return ~((-1 << (number+1))+1);
	}

	/**
	 * 指定した座標に指定した数字を配置してもルールに違反しないかどうか
	 * @param p 座標
	 * @param n 配置可能かを調べる数字
	 * @return 配置可能なら true 配置不可なら false
	 */
	boolean canPlace(Address p, int n) {
		return (pattern[p.r()][p.c()] & (1<<n)) > 0;
	}
	/**
	 * p0 に 数字 num が入っていることにより，
	 * 配置不可能となる数字を可能パターンから外す
	 * @param p0
	 * @param n
	 */
	void checkUsedNumber(Address p0, int n) {
		int pat = ~((1 << n) + 1);  // num桁以外が1
		for (int d = 0; d < 4; d++) {
			Address p = p0;
			for (int k = 0; k < n; k++) {
				p = Address.nextCell(p, d);
				if (board.isOn(p))
					pattern[p.r()][p.c()] &= pat;
			}
		}
		Area area = board.getArea(p0);
		if (area != null) {
			for (Address pos : board.getArea(p0)) {
				pattern[pos.r()][pos.c()] &= pat;
			}
		}
	}
}
