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
 * �y���V���p�Y���t���[�����[�N�� Frame �N���X
 */
public class Frame extends JFrame {

	private JLabel statusBar = new JLabel();
	/**
	 * �t���[���̏����������ŁC�t���[����������Ɏg�p�����
	 * @param panel �ݒ肷��p�l��
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
	 * �t���[������ʓ��ɓ���悤�ɑ傫����ς���B
	 */
	public void resize() {
		Dimension screenSize = getToolkit().getScreenSize();
		pack(); //�Ֆʂɍ��킹�ăT�C�Y����
		Dimension frameSize = getSize();
		if (frameSize.width > screenSize.width || frameSize.height > screenSize.height) {
			// 12 pix �傫������
			if (frameSize.width > screenSize.width)
				frameSize.width = screenSize.width;
			if (frameSize.height > screenSize.height)
				frameSize.height = screenSize.height;
			setSize(frameSize);
			frameSize = getSize();
		}
	}
	/**
	 * �t���[���̈ʒu����ʂ̒����ɂ���B
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
	 * �X�e�[�^�X�o�[�ɕ������\��
	 * @param text �\�����镶����
	 */
//	public void statusBarMessage(String text) {
//		statusBar.setText(text);
//	}

}
