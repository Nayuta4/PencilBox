package pencilbox.common.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * ファイルチューザー
 * 用途ごとに１つずつのインスタンスを生成して使い回す
 */
public class FileChooser extends JFileChooser {
	
	private static FileChooser problemFileChooser = null;
	private static FileChooser imageFileChooser = null;
	private static FileChooser preferenceFileChooser = null;

	private FileChooser() {
	}
	
	public static FileChooser getProblemFileChooser() {
		if (problemFileChooser == null) {
			problemFileChooser = new FileChooser();
			problemFileChooser.setCurrentDirectory(new File("."));
			problemFileChooser.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					String name = f.getName().toLowerCase();
					return name.endsWith(".txt") || name.endsWith(".xml") || name.endsWith(".pcl") || f.isDirectory();
				}
				public String getDescription() {
					return "txt or xml files";
				}
			});
		}
		return problemFileChooser;
	}

	public static FileChooser getImageFileChooser() {
		if (imageFileChooser == null) {
			imageFileChooser = new FileChooser();
			imageFileChooser.setCurrentDirectory(new File("."));
			imageFileChooser.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					String name = f.getName().toLowerCase();
					return name.endsWith(".png") || name.endsWith(".jpg") || f.isDirectory();
				}
				public String getDescription() {
					return "png files";
				}
			});
		}
		return imageFileChooser;
	}

	public static FileChooser getPreferenceFileChooser() {
		if (preferenceFileChooser == null) {
			preferenceFileChooser = new FileChooser();
			preferenceFileChooser.setCurrentDirectory(new File("."));
			preferenceFileChooser.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					String name = f.getName().toLowerCase();
					return name.endsWith(".ini");
				}
				public String getDescription() {
					return "ini files";
				}
			});
		}
		return preferenceFileChooser;
	}

}
