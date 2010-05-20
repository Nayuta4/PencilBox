package pencilbox.kakuro;

import pencilbox.common.core.Address;
import pencilbox.common.core.Direction;

/**
 * 可能数字をビットパターンで表現するヒント機能
 */
public class DigitPatternHint {

	private Board board;
	private int[][] horizPattern;
	private int[][] vertPattern;

	/**
	 * そのマスに配置して直接のルール違反にならない数字のビットパターンを返す。
	 * @param r　マスの行座標
	 * @param c マスの列座標
	 * @return 可能なビットパターンを返す。ただしそのマスが壁マスであるかまたはタテヨコの両方とも和が定義されていない場合は値 1 を返す。
	 */
	int getPattern(Address p) {
		int pat = horizPattern[p.r()][p.c()] & vertPattern[p.r()][p.c()];
		// 積の最下位ビット 1 であればタテヨコとも未定義ということなので，何も表示しないために 1 を返す。
		// タテヨコとも未定義の場合も 1～9　まですべて可能というドット表示をする場合は以下2行削除せよ。
		if ((pat & 1) == 1)
			return 1;
		return pat;
	}
	
	void setupHint(Board board) {
		this.board = board;
		int rows = board.rows();
		int cols = board.cols();
		horizPattern = new int[rows][cols];
		vertPattern = new int[rows][cols];
	}

	/**
	 * 引数の座標の数字が変更されたときに，このマスを含むタテヨコ両方を再計算する
	 * @param r
	 * @param c
	 */
	void updatePattern(int r, int c) {
		updatePattern(r, c, Direction.HORIZ);
		updatePattern(r, c, Direction.VERT);
	}
	
	/**
	 * 引数座標のマスを含むヨコまたはタテのワードの配置可能パターンを求める。
	 * @param r
	 * @param c
	 * @param dir
	 */
	void updatePattern(int r, int c, int dir) {
		int pattern = 0;
		int headPosition = board.getWordHead(r, c, dir);//このマスを含む数字の和を示した黒マスの列座標
		int wordSum = board.getWordSum(r, c, dir); // 数字の和
		int wordSize = board.getWordSize(r, c, dir); //　マス数
		int currentSum = 0;// 決定済み数字の和
		int currentSize = 0; // 決定済み数字の数
		int usedDigit = 0;// 使用済み数字のパターン
		if (wordSum == 0) { // 和が定義されていなければすべて可能
			pattern = HintTbl.D_ALL + 1;
		} else if (wordSize > 9) {
			pattern = 0;
		} else {
			for (int i = 0; i < wordSize; i++) {
				int n = 0;
				if (dir == Direction.HORIZ)
					n = board.getNumber(r, headPosition+1+i);
				else if (dir == Direction.VERT)
					n = board.getNumber(headPosition+1+i, c);
				if (n > 0) {
					currentSize++;
					currentSum += n; // 現和に加算
					usedDigit |= (1 << n);//使用している数字
				}
			}
			if (currentSize == wordSize) { // すべて決まっている
				pattern = 0;
			} else {
				pattern = HintTbl.getRemainingDigit(wordSum - currentSum, wordSize - currentSize, usedDigit);
			}
		}
		for (int i = 0; i < wordSize; i++) {
			if (dir == Direction.HORIZ)
				horizPattern[r][headPosition+1+i] = pattern;
			else if (dir == Direction.VERT)
				vertPattern[headPosition+1+i][c] = pattern;
		}
	}

	/**
	 * 盤面全体の可能パターンを再計算する
	 */
	void initHint() {
		// 全部再計算するので、全数字を可能とする
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				vertPattern[r][c] = HintTbl.D_ALL + 1;
				horizPattern[r][c] = HintTbl.D_ALL + 1;
			}
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.isWall(r, c)) {
					if (!board.isWall(r, c+1))
						if (board.getSumH(r,c) > 0) {
							updatePattern(r, c+1, Direction.HORIZ);
						}
					if (!board.isWall(r+1, c))
						if (board.getSumV(r,c) > 0) {
							updatePattern(r+1, c, Direction.VERT);
						}
				}
			}
		}
	}
}
