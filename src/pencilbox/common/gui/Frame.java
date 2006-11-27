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
 * �y���V���p�Y���t���[�����[�N�� Frame �N���X
 */
public class Frame extends JFrame {

	private static int signx = 1;
	private static int signy = 1;
	private static int shift = 30;

//	private JLabel statusBar = new JLabel();
	/**
	 * �t���[���̏����������ŁC�t���[����������Ɏg�p�����
	 * @param panel �ݒ肷��p�l��
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
	 * ���݂̏�Ԃɍ��킹�ăt���[���̑傫����ς���B
	 * ���̂Ƃ��C�t���[������ʓ��ɓ���悤�ɂ���B
	 * pack() �̑���Ɏg�p����B
	 */
	public void resize() {
		if ((getExtendedState() & java.awt.Frame.MAXIMIZED_BOTH) == java.awt.Frame.MAXIMIZED_BOTH)
			return;
		pack(); //�Ֆʂɍ��킹�ăT�C�Y����
		Dimension screenSize = getToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		setSize(frameSize);
	}
	/**
	 * �t���[���̈ʒu����ʂ̒����ɂ���B
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
	 * ���݂Ɠ����ʒu�ɂ���B
	 * �������C���̌��ʉ�ʂ���͂ݏo���ꍇ�͂��̕ӂ���ʒ[�ɐڂ���悤�ɂ��炷�B
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
	 * �Ăяo�����t���[���ɑ΂��Ă��炵���ʒu�ɂ���B
	 * ��ʂ���͂ݏo���ꍇ�͂��̕ӂ���ʒ[�ɐڂ���ʒu�ɂ��āC����͋t�����ɂ��炷�B
	 * @param org�@�Ăяo�����t���[��
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
