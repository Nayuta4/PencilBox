package pencilbox.common.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;



/**
 * パネルの表示内容を png形式の画像としてファイルに書き込む処理を行うクラス
 */
/*
 * 参考：core JAVA2 Vol.2 7.10 
 */
public class PanelImageWriter {

	private static final String formatName = "png";
	
	private BufferedImage image;
	private PanelBase panel;

	/**
	 * パネルの表示内容を png形式の画像としてファイルに書き込む一連の処理を実行する
	 * @param panel 画像として保存するパネル
	 */
	public void run(PanelBase panel) {
		this.panel = panel;
		makePanelImage();
		saveFile();
	}

	private void makePanelImage() {
		image = new BufferedImage(panel.getBoardRegionSize().width, panel.getBoardRegionSize().height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		panel.drawPanel(g2);
	}

	/**
		  Save the current image in a file
	 */
	private void saveFile() {
		if (image == null)
			return;
		Iterator iter = ImageIO.getImageWritersByFormatName(formatName);
		ImageWriter writer = (ImageWriter) iter.next();

		JFileChooser chooser = FileChooser.getImageFileChooser();

		int r = chooser.showSaveDialog(null);
		if (r != JFileChooser.APPROVE_OPTION)
			return;
		File f = chooser.getSelectedFile();
		try {
			ImageOutputStream imageOut = ImageIO.createImageOutputStream(f);
			writer.setOutput(imageOut);

			writer.write(new IIOImage(image, null, null));

			imageOut.close();

		} catch (IOException exception) {
			JOptionPane.showMessageDialog(null, exception);
		}
	}
}
