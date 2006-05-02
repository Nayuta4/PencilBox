package pencilbox.common.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * ファイルチューザー
 * 1つのインスタンスのみ生成して使い回す
 */
public class FileChooser extends JFileChooser {
	
	private static FileChooser instance = new FileChooser();

    /**
     * ファイルチューザーインスタンスを取得する
     * @return ファイルチューザー
     */
	public static FileChooser getInstance() {
		return instance;
	}

	private FileChooser() {
		setFileFilter(new TxtFilter());
		setCurrentDirectory(new File("."));
	}
}

class TxtFilter extends FileFilter {
	public boolean accept(File f) {
		String ext = f.getName().toLowerCase();
		return ext.endsWith(".txt") || ext.endsWith(".xml") || ext.endsWith(".pcl") || f.isDirectory();
	}
	public String getDescription() {
		return "txt or xml file";
//		return "txt or xml or pcl file";
	}
}
