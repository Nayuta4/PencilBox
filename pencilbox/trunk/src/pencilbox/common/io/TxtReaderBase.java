package pencilbox.common.io;

import java.io.IOException;
import java.io.Reader;

import pencilbox.common.core.BoardBase;


/**
 * テキスト形式の盤面読込用クラス
 */
public abstract class TxtReaderBase {
	
	/**
	 * テキスト形式の盤面を読み込む
	 * @param in 入力
	 * @return 読み込んで作成した盤面
	 * @throws IOException
	 */
	public abstract BoardBase readProblem(Reader in) throws IOException;

}
