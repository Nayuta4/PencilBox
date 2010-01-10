package pencilbox.heyawake;
import javax.swing.JOptionPane;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Size;

/**
 * へやわけアプレットURL書き出し
 * 参考：HeyawakeBox 1.1.3
 */
public final class HeyawakeWriter {
	
	/** URLの最大長 */
	public static final int MAX_URL_LENGTH = 2083;

	private static String targetUrl = "http://www.geocities.jp/heyawake/";
	
	/**
	 * URLを生成する
	 */
	public String writeQuestion(BoardBase b) {
		Board board = (Board)b;
		
		Size size = board.getSize();
		int height = size.getRows();
		int width = size.getCols();
		String err = null;
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(width);
		buffer.append('x');
		buffer.append(height);
		
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				Square square = board.getSquare(y, x);
				if (square != null) {
					if (square.c0() == x && square.r0() == y) {
						buffer.append('/');
						if (square.getNumber() >= 0) {
							buffer.append(square.getNumber());
							buffer.append("in");
						}
						buffer.append(square.sizeC());
						buffer.append('x');
						buffer.append(square.sizeR());
					}
				} else {
					err = "すべてのマスが埋まっていません";
					break;
				}
			}
		}
		String urlString = targetUrl + "?problem=" + buffer.toString();
		if (urlString.length() > MAX_URL_LENGTH) {
			JOptionPane.showMessageDialog(null,
					"ＵＲＬが長くなりすぎてしまいました。\n" +
					urlString.length() + "文字です。\n",
					null, JOptionPane.ERROR_MESSAGE);
		}
		if (err != null) {
			JOptionPane.showMessageDialog(null,err,"Error",JOptionPane.ERROR_MESSAGE);
		}
		return urlString;
	}
}
