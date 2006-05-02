package pencilbox.kakuro;

import pencilbox.common.core.Direction;

/**
 * 可能数字をビットパターンで表現するヒント機能
 */
public class DigitPatternHint {

	static final int HORIZ = Direction.HORIZ; 
	static final int VERT = Direction.VERT;

	private Board board;
	private int[][] horizPattern;
	private int[][] vertPattern;
	private int[][] remPattern;
	private int[][] remNo; // 可能な数字のかず
	
	int getRemNo(int r, int c) {
		return remNo[r][c];
	}
	int getRemPattern(int r, int c) {
		return remPattern[r][c];
	}
	void init(Board board) {
		this.board = board;
		int rows = board.rows();
		int cols = board.cols();
		horizPattern = new int[rows][cols];
		vertPattern = new int[rows][cols];
		remPattern = new int[rows][cols];
		remNo = new int[rows][cols];
		helpReCalc();
	}
	// (r,c) の位置が変更（未定への変更を含む）されたため
	// このマスを含むタテヨコ両方を再計算する
	void helpCheck(int r, int c) {
		// crossする位置は行列両方変わるため、すべて可にしておく
//		remPattern[r][c] = HintTbl.D_ALL;
//		horizPattern[r][c] = HintTbl.D_ALL;
//		vertPattern[r][c] = HintTbl.D_ALL;
		helpCheckHoriz(r, c);
		helpCheckVert(r, c);
	}
	void helpCheckHoriz(int r, int c) {
		int sumPos = board.getWordHead(r,c,HORIZ);//このマスを含む数字の和の位置
		int sum = board.getWordSum(r,c,HORIZ);// 数字の和
		if (sum == 0)// 和が定義されていなければなにもしない
			return;
		int no = board.getWordSize(r,c,HORIZ);//数字の数
		if (no > 9)
			return;
		int curSum = 0;// 定義済み数字の和
		int remNo = 0;// 未定義の数字の数
		int usedDigit = 0;// 定義済み数字のパターン
		for (int i = 0; i < no; i++) {
			int n = board.getNumber(r,sumPos+1+i);// 数字の値
			if (n == 0)// 未定義の数字
				remNo++;
			else {
				curSum += n; // 現和に加算
				usedDigit |= (1 << n);//使用している数字
			}
		}
		if(remNo ==0) return; // すべて決まった
		int remPat = HintTbl.getRemainingDigit(sum - curSum, remNo, usedDigit);
		// 割り当て可能な数の組み合わせ
		// 各未定数にremPatを設定(全部に設定しても表示はされないから問題ない)
		for (int i = 0; i < no; i++) {
			horizPattern[r][sumPos+1+i] = remPat;
			setRem(r, sumPos+1+i);
		}
	}
	void helpCheckVert(int r, int c) {
		int sumPos = board.getWordHead(r,c,VERT);
		int sum = board.getWordSum(r,c,VERT);
		if (sum == 0)
			return;
		int no = board.getWordSize(r,c,VERT);
		if (no > 9)
			return;
		int curSum = 0;
		int remNo = 0;
		int usedDigit = 0;
		for (int i = 0; i < no; i++) {
			int n = board.getNumber(sumPos+1+i,c);
			if (n == 0)
				remNo++;
			else {
				curSum += n;
				usedDigit |= (1 << n);
			}
		}
		if(remNo ==0) return; // すべて決まった
		int remPat = HintTbl.getRemainingDigit(sum - curSum, remNo, usedDigit);
		// 各未定数にremPatを設定
		for (int i = 0 ; i < no; i++) {
			vertPattern[sumPos+1+i][c] = remPat;
			setRem(sumPos+1+i, c);
		}
	}
	void setRem(int r, int c) {
		// タテヨコ共通の数字が許可
		int pat = horizPattern[r][c] & vertPattern[r][c];
		remPattern[r][c] = pat;
		// 許される数字の数
		int n = 0;
		for (int i=0; i<9; i++)
			if ((pat & HintTbl.D_ARRAY[i]) != 0)
				n++;
		remNo[r][c] = n;
	}
	void helpReCalc() {
		// 全部再計算するので、全数字を可能とする
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				// 可能性がなければ×印がついてしまう。合計が０なら計算対象に
				// ならないのremNoは変更されず、patternも０のままなので
				// 何も表示されない。このようなトリックである。
				remNo[r][c] = 1;
				vertPattern[r][c] = HintTbl.D_ALL;
				horizPattern[r][c] = HintTbl.D_ALL;
			}
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.isWall(r,c)) {
					if (board.getSumH(r,c) != 0) {
						helpCheck(r, c+1);
					}
					if (board.getSumV(r,c) != 0) {
						helpCheck(r+1, c);
					}
				}
			}
		}
	}
}
