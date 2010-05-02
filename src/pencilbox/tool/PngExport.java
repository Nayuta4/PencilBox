package pencilbox.tool;

import java.io.File;

import pencilbox.common.core.PencilBoxException;
import pencilbox.common.core.Problem;
import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilType;
import pencilbox.common.gui.EventHandlerManager;
import pencilbox.common.gui.Frame;
import pencilbox.common.gui.MenuCommand;
import pencilbox.common.gui.PanelBase;
import pencilbox.common.gui.PanelImageWriter;
import pencilbox.common.gui.PreferencesCopierBase;
import pencilbox.common.io.IOController;

/**
 *
 */
public class PngExport {
	
	public static void main(String args[]) {
		if (args.length < 2) {
			printUsage();
			return;
		}
		PencilType pencilType = PencilType.getPencilType(args[0]);
		if (pencilType == null) {
			System.out.println("Illegal puzzletype : " + args[0]);
			printUsage();
			return;
		}
		new PngExport(pencilType).run(args);
	}

	public PngExport(PencilType type) {
		pencilType = type;
	}

	public static final void printUsage() {
		System.out.println("Usage: java -jar pngexport.jar puzzletype datafiles");
	}
	/**
	 * アプリケーションを実行する
	 * 引数の文字列が?で始まる場合は，その文字列を問題データとする盤面を開く。1つのみ。
	 * 引数の文字列がファイル名の場合はそのファイルを開く。複数可能。
	 * 上記に当てはまらない場合や，引数が空配列または空文字列の場合は，空の盤面のフレームを開く。
	 * @param args
	 */
	public void run(String[] args) {
		System.out.println("puzzletype is " + args[0]);
		try {
			for (int i = 1; i < args.length; i++) {
				File file = new File(args[i]);
				if (file.isFile()) {
					System.out.println("Input file is  " + file.toString());
					this.createNewFrame(file);
					File pngFile = new File(file + ".png");
					new PanelImageWriter().saveImageToFile(this.panel, pngFile);
					System.out.println("Output file is  " + pngFile.toString());
				} else {
					System.out.println("Cannot read file : " + file.toString());
				}
			}
		} catch (PencilBoxException e) {
			e.printStackTrace();
		}
	}

	public void createNewFrame(File file) throws PencilBoxException {
		Problem problem = IOController.getInstance(pencilType).openFile(file);
		MenuCommand command = new MenuCommand();
		frame = new Frame();
//		MenuBase menu = (MenuBase) ClassUtil.createInstance(pencilType, ClassUtil.MENU_CLASS);
		panel = (PanelBase) ClassUtil.createInstance(pencilType, ClassUtil.PANEL_CLASS);
		handler = new EventHandlerManager(pencilType);
		frame.setup(panel);
		command.setup(pencilType, frame, panel, handler, problem);
		panel.setup(problem.getBoard());
		handler.setup(panel, problem.getBoard());
		copyPreferences(command);
//		menu.setup(command, frame, panel);
//		menu.updateCurrentMenuSelection();
//		ToolbarPanel toolbar = new ToolbarPanel();
//		toolbar.setup(command, frame, panel, menu);
//		frame.getContentPane().add(toolbar, BorderLayout.PAGE_START);
//		frame.resize();
//		if (caller == null) {
//			frame.locateAtCenter();
//		} else {
//			frame.locateAtShiftPosition(caller.getFrame());
//		}
//		frame.setVisible(true);
//		if (defaultProblemFile != null && defaultProblemFile.canRead()) {
//			caller = command;
//			createNewBoard(defaultProblemFile);
//		}
	}

	private void copyPreferences(MenuCommand dst) throws PencilBoxException {
		PreferencesCopierBase copier = PreferencesCopierBase.createInstance(pencilType);
		if (caller == null) {
			File iniFile = new File("pencilbox.ini");
			if (iniFile.canRead()) {
				copier.loadPreferences(dst, iniFile);
			}
		} else {
			copier.copyPreferences(caller, dst);
		}
	}

	private PencilType pencilType;
	private MenuCommand caller; // 呼び出し元メニューコマンド
	PanelBase panel;
	private EventHandlerManager handler;
//	private MenuBase menu;
	Frame frame;

}
