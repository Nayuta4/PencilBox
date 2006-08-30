package pencilbox.common.factory;

import java.io.File;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.PencilBoxException;
import pencilbox.common.core.Problem;
import pencilbox.common.core.Size;
import pencilbox.common.gui.Frame;
import pencilbox.common.gui.MenuBase;
import pencilbox.common.gui.MenuCommand;
import pencilbox.common.gui.PanelEventHandler;
import pencilbox.common.io.IOController;


/**
 * PencilBox ファクトリークラス
 */
public class PencilFactory {
	
	private PencilType pencilType;
	
	/**
	 * @param pencilType
	 */
	public PencilFactory(PencilType pencilType) {
		this.pencilType = pencilType;
	}
	/**
	 * PencilPuzzleインスタンスを作成して取得する
	 * @param pencilType 
	 * @return 新しいPencilPuzzleインスタンス
	 */
	public static PencilFactory getInstance(PencilType pencilType) {
		return new PencilFactory(pencilType);
	}
	
	/**
	 * デフォルトサイズでアプリケーションフレームを作る。
	 * Launcher, Main から使用される。
	 * @throws PencilBoxException
	 */
	public void createNewFrame() throws PencilBoxClassException {
//		try {
//			PuzzleCommon puzzleCommon = (PuzzleCommon) ClassUtil.createInstance(pencilType , "Puzzle");
//			Size size = puzzleCommon.getDefaultSize();
			Size size = pencilType.getDefaultSize();
			createNewFrame(size);
//		} catch (PencilBoxException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 盤面サイズを与えてアプリケーションフレームを作る。
	 * 「新規」メニューからのの起動で使用される。
	 * @param size
	 * @throws PencilBoxClassException
	 */
	public void createNewFrame(Size size) throws PencilBoxClassException {
//		try {
			BoardBase board = (BoardBase) ClassUtil.createInstance(pencilType, ClassUtil.BOARD_CLASS);
			board.setSize(size);
			Problem problem = new Problem(board);	
			createNewFrame(problem);
//		} catch (PencilBoxException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 問題ファイルを与えてアプリケーションフレームを作る。
	 * 「開く」メニューからの起動で使用される。
	 * @param file
	 * @throws PencilBoxException
	 */
	public void createNewFrame(File file) throws PencilBoxException {
//		try {
			Problem problem = IOController.getInstance(pencilType).openFile(file);
			createNewFrame(problem);
//		} catch (PencilBoxException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * Problem を与えてアプリケーションフレームを作る。
	 * このクラス内部で最終的に作成する段階で使用される。
	 * @param problem
	 * @throws PencilBoxClassException
	 */
	public void createNewFrame(Problem problem) throws PencilBoxClassException {
//		try {
			MenuCommand command = new MenuCommand();
			Frame frame = new Frame();
			MenuBase menu = (MenuBase) ClassUtil.createInstance(pencilType, ClassUtil.MENU_CLASS);
			PanelEventHandler panel = (PanelEventHandler) ClassUtil.createInstance(pencilType, ClassUtil.PANEL_CLASS);
			frame.setup(panel);
			menu.setup(command, frame, panel);
			command.setup(pencilType, frame, panel, problem);
			panel.setup(problem.getBoard());
			frame.resize();
			frame.centering();
			frame.setVisible(true);
//		} catch (PencilBoxException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 問題ファイルを読み込んで同じフレームで開く。
	 * 「閉じて開く」メニューからの起動で使用される。
	 * @param file
	 * @throws PencilBoxException
	 */
	public void createNewBoard(MenuCommand command, File file) throws PencilBoxException {
//		try {
			Problem problem = IOController.getInstance(pencilType).openFile(file);
			createNewBoard(command, problem);
//		} catch (PencilBoxException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * Problem を与えて同じフレームで開く。
	 * @param problem
	 * @throws PencilBoxClassException
	 */
	public void createNewBoard(MenuCommand command, Problem problem) throws PencilBoxClassException {
//		try {
//			MenuCommand command = new MenuCommand();
//			MenuBase menu = (MenuBase) ClassUtil.createInstance(pencilType, "Menu");
			PanelEventHandler panel = command.getPanelBase();
			Frame frame = command.getFrame();
//			frame.setup(panel);
//			menu.setup(command, frame, panel);
			command.setup(pencilType, problem);
			panel.setup(problem.getBoard());
			frame.resize();
//			frame.centering();
//			frame.setVisible(true);
//		} catch (PencilBoxException e) {
//			e.printStackTrace();
//		}
	}

}
