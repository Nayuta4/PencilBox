package pencilbox.common.io;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;


/**
 * テキスト形式の盤面書き出し操作用クラス
 */
public abstract class TxtWriterBase {
	
	public static int ALL = 0;
	public static int QUESTION_ONLY = 1;
	
	/**
	 * テキスト形式で盤面を書き出す
	 * @param out 出力
	 * @param board 書き出す盤面
	 */
	public void writeProblem(PrintWriter out, BoardBase board) {
		writeProblem(out, board, ALL);
	}
	/**
	 * テキスト形式で盤面を書き出す。
	 * 回答状態は無視して，問題データのみ書き出す
	 * @param out 出力
	 * @param board 書き出す盤面
	 */
	public void writeQuestion(PrintWriter out, BoardBase board) {
		writeProblem(out, board, QUESTION_ONLY);
	}
	/**
	 * テキスト形式で盤面を書き出す
	 * @param out 出力
	 * @param board 書き出す盤面
	 * @param mode 書き出しモード（問題データのみか，回答データも含むか）
	 */
	public abstract void writeProblem(PrintWriter out, BoardBase board, int mode);
	
}
