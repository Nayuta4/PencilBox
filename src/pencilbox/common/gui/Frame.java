package pencilbox.common.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;


/**
 * ペンシルパズルフレームワークの Frame クラス
 */
public class Frame extends JFrame {

	private JLabel statusBar = new JLabel();
	/**
	 * フレームの初期化処理で，フレーム生成直後に使用される
	 * @param panel 設定するパネル
	 */
	public void setup(PanelEventHandler panel) {
		JScrollPane jScrollPane = new JScrollPane();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(jScrollPane, BorderLayout.CENTER);
		contentPane.add(statusBar, BorderLayout.SOUTH);
		statusBar.setText(" ");
		jScrollPane.getViewport().add(panel, null);
		setJMenuBar(new JMenuBar());
	}
	
	/**
	 * フレームが画面内に入るように大きさを変える。
	 */
	public void resize() {
		Dimension screenSize = getToolkit().getScreenSize();
		pack(); //盤面に合わせてサイズ調節
		Dimension frameSize = getSize();
		if (frameSize.width > screenSize.width || frameSize.height > screenSize.height) {
			// 12 pix 大きすぎる
			if (frameSize.width > screenSize.width)
				frameSize.width = screenSize.width;
			if (frameSize.height > screenSize.height)
				frameSize.height = screenSize.height;
			setSize(frameSize);
			frameSize = getSize();
		}
	}
	/**
	 * フレームの位置を画面の中央にする。
	 */
	public void centering() {
		Dimension screenSize = getToolkit().getScreenSize();
		Dimension frameSize = getSize();
		Point pos = new Point();
		pos.x = (screenSize.width - frameSize.width) / 2;
		pos.y = (screenSize.height - frameSize.height) / 2;
		setLocation(pos);
	}
	
	/**
	 * ステータスバーに文字列を表示
	 * @param text 表示する文字列
	 */
//	public void statusBarMessage(String text) {
//		statusBar.setText(text);
//	}

}
