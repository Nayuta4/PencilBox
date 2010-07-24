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
	 * @param p マスの座標
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
		horizPattern = new int[board.rows()][board.cols()];
		vertPattern = new int[board.rows()][board.cols()];
	}

	/**
	 * 引数の座標の数字が変更されたときに，このマスを含むタテヨコ両方を再計算する
	 * @param p
	 */
	void updatePattern(Address p) {
		updatePattern(p, Direction.HORIZ);
		updatePattern(p, Direction.VERT);
	}
	
	/**
	 * 引数座標のマスを含むヨコまたはタテのワードの配置可能パターンを求める。
	 * @param p0
	 * @param dir
	 */
	void updatePattern(Address p0, int dir) {
		int pattern = 0;
		Address headPosition = board.getWordHead(p0, dir);//このマスを含む数字の和を示した黒マスの列座標
		int wordSum = board.getWordSum(p0, dir); // 数字の和
		int wordSize = board.getWordSize(p0, dir); //　マス数
		int currentSum = 0;// 決定済み数字の和
		int currentSize = 0; // 決定済み数字の数
		int usedDigit = 0;// 使用済み数字のパターン
		Address p = headPosition;
		if (wordSum == 0) { // 和が定義されていなければすべて可能
			pattern = HintTbl.D_ALL + 1;
		} else if (wordSize > 9) {
			pattern = 0;
		} else {
			for (int i = 0; i < wordSize; i++) {
				p = Address.nextCell(p, dir|2);
				int n = board.getNumber(p);
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
				horizPattern[p0.r()][headPosition.c()+1+i] = pattern;
			else if (dir == Direction.VERT)
				vertPattern[headPosition.r()+1+i][p0.c()] = pattern;
		}
	}

	/**
	 * 盤面全体の可能パターンを再計算する
	 */
	void initHint() {
		// 全部再計算するので、全数字を可能とする
		for (Address p : board.cellAddrs()) {
			vertPattern[p.r()][p.c()] = HintTbl.D_ALL + 1;
			horizPattern[p.r()][p.c()] = HintTbl.D_ALL + 1;
		}
		for (Address p : board.cellAddrs()) {
			if (board.isWall(p)) {
				for (int d = 0; d < 2; d++) {
					Address p1 = Address.nextCell(p, d|2);
					if (!board.isWall(p1));
						if (board.getSum(p, d) > 0) {
							updatePattern(p1, d);
						}
				}
			}
		}
	}
}
