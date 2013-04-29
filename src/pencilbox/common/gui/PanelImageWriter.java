package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

/**
 * パネルの表示内容を png形式の画像としてファイルに書き込む処理を行うクラス
 */
/*
 * 参考：core JAVA2 Vol.2 7.10 
 */
public class PanelImageWriter {

	private static final String formatName = "png";

	/**
	 * パネルの表示内容を png形式の画像としてファイルに書き込む一連の処理を実行する
	 * @param panel 画像として保存するパネル
	 * @param file 保存先ファイル
	 */
	public void saveImageToFile(PanelBase panel, File file) {
		BufferedImage image = makePanelImage(panel);
		try {
			ImageIO.write(image, formatName, file);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public BufferedImage makePanelImage(PanelBase panel) {
		BufferedImage image = new BufferedImage(panel.getBoardRegionSize().width, panel.getBoardRegionSize().height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
	 	// 背景を白で塗る。
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, image.getWidth(), image.getHeight());
		panel.drawPanel(g2);
		return image;
	}
}
