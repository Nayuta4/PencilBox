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
 * �p�l���̕\�����e�� png�`���̉摜�Ƃ��ăt�@�C���ɏ������ޏ������s���N���X
 */
/*
 * �Q�l�Fcore JAVA2 Vol.2 7.10 
 */
public class PanelImageWriter {

	private static final String formatName = "png";
	
	private BufferedImage image;
	private PanelBase panel;

	/**
	 * �p�l���̕\�����e�� png�`���̉摜�Ƃ��ăt�@�C���ɏ������ވ�A�̏��������s����
	 * @param panel �摜�Ƃ��ĕۑ�����p�l��
	 */
	public void run(PanelBase panel) {
		this.panel = panel;
		makePanelImage();
		saveFile();
	}

	private void makePanelImage() {
		image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
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
		final List writerSuffixes = Arrays.asList(writer.getOriginatingProvider().getFileSuffixes());

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));

		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				String name = f.getName();
				int p = name.lastIndexOf('.');
				if (p == -1)
					return false;
				String suffix = name.substring(p + 1).toLowerCase();
				return writerSuffixes.contains(suffix);
			}
			public String getDescription() {
				return formatName + " files";
			}
		});

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
