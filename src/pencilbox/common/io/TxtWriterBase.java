package pencilbox.common.io;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;


/**
 * テキスト形式の盤面書き出し操作用クラス
 */
public abstract class TxtWriterBase {
	
	/**
	 * テキスト形式で盤面を書き出す
	 * @param out 出力
	 * @param board 書き出す盤面
	 */
	public abstract void writeProblem(PrintWriter out, BoardBase board);
	
}
