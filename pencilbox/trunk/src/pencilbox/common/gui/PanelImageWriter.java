package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * �p�l���̕\�����e�� png�`���̉摜�Ƃ��ăt�@�C���ɏ������ޏ������s���N���X
 */
/*
 * �Q�l�Fcore JAVA2 Vol.2 7.10 
 */
public class PanelImageWriter {

	private static final String formatName = "png";
	
	/**
	 * �p�l���̕\�����e�� png�`���̉摜�Ƃ��ăt�@�C���ɏ������ވ�A�̏��������s����
	 * @param panel �摜�Ƃ��ĕۑ�����p�l��
	 */
	public void saveImage(PanelBase panel) {
		BufferedImage image = makePanelImage(panel);
		JFileChooser chooser = FileChooser.getImageFileChooser();
		int r = chooser.showSaveDialog(null);
		if (r != JFileChooser.APPROVE_OPTION)
			return;
		File file = chooser.getSelectedFile();
		try {
			ImageIO.write(image, formatName, file);
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(null, exception);
		}
	}

	public BufferedImage makePanelImage(PanelBase panel) {
		BufferedImage image = new BufferedImage(panel.getBoardRegionSize().width, panel.getBoardRegionSize().height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
	 	// �w�i�𔒂œh��B
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, image.getWidth(), image.getHeight());
		panel.drawPanel(g2);
		return image;
	}
}
