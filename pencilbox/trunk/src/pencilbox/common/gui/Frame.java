package pencilbox.common.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;


/**
 * ペンシルパズルフレームワークの Frame クラス
 */
public class Frame extends JFrame {

	private static int signx = 1;
	private static int signy = 1;
	private static int shift = 30;

//	private JLabel statusBar = new JLabel();
	/**
	 * フレームの初期化処理で，フレーム生成直後に使用される
	 * @param panel 設定するパネル
	 */
	public void setup(PanelBase panel) {
		JScrollPane jScrollPane = new JScrollPane();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(jScrollPane, BorderLayout.CENTER);
//		contentPane.add(statusBar, BorderLayout.SOUTH);
//		statusBar.setText(" ");
		jScrollPane.getViewport().add(panel, null);
		setJMenuBar(new JMenuBar());
	}
	
	/**
	 * 現在の状態に合わせてフレームの大きさを変える。
	 * このとき，フレームが画面内に入るようにする。
	 * pack() の代わりに使用する。
	 */
	public void resize() {
		if ((getExtendedState() & java.awt.Frame.MAXIMIZED_BOTH) == java.awt.Frame.MAXIMIZED_BOTH)
			return;
		pack(); //盤面に合わせてサイズ調節
		Dimension screenSize = getToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		setSize(frameSize);
	}
	/**
	 * フレームの位置を画面の中央にする。
	 */
	public void locateAtCenter() {
		Point point = new Point();
		Dimension frameSize = this.getSize();
		Dimension screenSize = getToolkit().getScreenSize();
		point.x = (screenSize.width - frameSize.width) / 2;
		point.y = (screenSize.height - frameSize.height) / 2;
		this.setLocation(point);
	}
	
	/**
	 * 現在と同じ位置にする。
	 * ただし，その結果画面からはみ出す場合はその辺が画面端に接するようにずらす。
	 */
	public void locateAtSamePosition() {
		if ((getExtendedState() & java.awt.Frame.MAXIMIZED_BOTH) == java.awt.Frame.MAXIMIZED_BOTH)
			return;
		Point point = this.getLocation();
		Dimension frameSize = this.getSize();
		Dimension screenSize = getToolkit().getScreenSize();
//		Rectangle bound = org.getBounds();
//		point.x = bound.x + (bound.width - frameSize.width) / 2;
//		point.y = bound.y + (bound.height - frameSize.height) / 2;
		if (point.x + frameSize.width > screenSize.width) {
			point.x = screenSize.width - frameSize.width;
		}
		if (point.x < 0){
			point.x = 0;
		}
		if (point.y + frameSize.height > screenSize.height) {
			point.y = screenSize.height - frameSize.height;
		}
		if (point.y < 0){
			point.y = 0;
		}
		this.setLocation(point);
	}
	/**
	 * 呼び出し元フレームに対してずらした位置にする。
	 * 画面からはみ出す場合はその辺を画面端に接する位置にして，次回は逆方向にずらす。
	 * @param org　呼び出し元フレーム
	 */
	public void locateAtShiftPosition(JFrame org) {
		Point point = new Point();
		Dimension frameSize = this.getSize();
		Rectangle bound = org.getBounds();
		Dimension screenSize = getToolkit().getScreenSize();
		point.x = bound.x + signx * shift;
		point.y = bound.y + signy * shift;
		if (point.x + frameSize.width > screenSize.width) {
			point.x = screenSize.width - frameSize.width;
			signx = -1;
		}
		if (point.x < 0) {
			point.x = 0;
			signx = +1;
		}
		if (point.y + frameSize.height > screenSize.height) {
			point.y = screenSize.height - frameSize.height;
			signy = -1;
		}
		if (point.y < 0) {
			point.y = 0;
			signy = +1;
		}
		this.setLocation(point);
	}
}
