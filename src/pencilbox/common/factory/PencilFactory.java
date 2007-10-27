package pencilbox.common.factory;

import java.io.File;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.PencilBoxException;
import pencilbox.common.core.Problem;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Size;
import pencilbox.common.gui.EventHandlerManager;
import pencilbox.common.gui.Frame;
import pencilbox.common.gui.MenuBase;
import pencilbox.common.gui.MenuCommand;
import pencilbox.common.gui.PanelBase;
import pencilbox.common.gui.PreferencesCopierBase;
import pencilbox.common.io.IOController;


/**
 * PencilBox ファクトリークラス
 */
public class PencilFactory {
	
	private PencilType pencilType;
	private MenuCommand caller; // 呼び出し元メニューコマンド
	
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
	 * PencilPuzzleインスタンスを作成して取得する
	 * @param pencilType
	 * @param caller　呼び出し元メニューコマンド
	 * @return　新しいPencilPuzzleインスタンス
	 */
	public static PencilFactory getInstance(PencilType pencilType, MenuCommand caller) {
		PencilFactory f = new PencilFactory(pencilType);
		f.pencilType = pencilType;
		f.caller = caller;
		return f;
	}
	/**
	 * デフォルトサイズでアプリケーションフレームを作る。
	 * Launcher, Main から使用される。
	 * @throws PencilBoxException
	 */
	public void createNewFrame() throws PencilBoxClassException {
		Size size = pencilType.getDefaultSize();
		createNewFrame(size);
	}
	/**
	 * 盤面サイズを与えてアプリケーションフレームを作る。
	 * 「新規」メニューからのの起動で使用される。
	 * @param size
	 * @throws PencilBoxClassException
	 */
	public void createNewFrame(Size size) throws PencilBoxClassException {
		BoardBase board = (BoardBase) ClassUtil.createInstance(pencilType, ClassUtil.BOARD_CLASS);
		board.setSize(size);
		Problem problem = new Problem(board);	
		createNewFrame(problem);
	}
	/**
	 * 問題ファイルを与えてアプリケーションフレームを作る。
	 * 「開く」メニューからの起動で使用される。
	 * @param file
	 * @throws PencilBoxException
	 */
	public void createNewFrame(File file) throws PencilBoxException {
		Problem problem = IOController.getInstance(pencilType).openFile(file);
		createNewFrame(problem);
	}
	/**
	 * Problem を与えてアプリケーションフレームを作る。
	 * このクラス内部で最終的に作成する段階で使用される。
	 * @param problem
	 * @throws PencilBoxClassException
	 */
	public void createNewFrame(Problem problem) throws PencilBoxClassException {
		MenuCommand command = new MenuCommand();
		Frame frame = new Frame();
		MenuBase menu = (MenuBase) ClassUtil.createInstance(pencilType, ClassUtil.MENU_CLASS);
		PanelBase panel = (PanelBase) ClassUtil.createInstance(pencilType, ClassUtil.PANEL_CLASS);
		EventHandlerManager handler = new EventHandlerManager(pencilType);
		frame.setup(panel);
		command.setup(pencilType, frame, panel, handler, problem);
		panel.setup(problem.getBoard());
		handler.setup(panel, problem.getBoard());
		copyPreferences(command);
		menu.setup(command, frame, panel);
		menu.updateCurrentMenuSelection();
		frame.resize();
		if (caller == null) {
			frame.locateAtCenter();
		} else {
			frame.locateAtShiftPosition(caller.getFrame());
		}
		frame.setVisible(true);
	}
	/**
	 * 問題ファイルを読み込んで同じフレームで開く。
	 * 「閉じて開く」メニューからの起動で使用される。
	 * @param file
	 * @throws PencilBoxException
	 */
	public void createNewBoard(File file) throws PencilBoxException {
		Problem problem = IOController.getInstance(pencilType).openFile(file);
		createNewBoard(problem);
	}
	/**
	 * Problem を与えて同じフレームで開く。
	 * @param problem
	 * @throws PencilBoxClassException
	 */
	public void createNewBoard(Problem problem) throws PencilBoxClassException {
		PanelBase panel = caller.getPanelBase();
		EventHandlerManager handler = caller.getEventHandlerManager();
		Frame frame = caller.getFrame();
		caller.setup(pencilType, problem);
		panel.setup(problem.getBoard());
		handler.setup(problem.getBoard());
		frame.resize();
		frame.locateAtSamePosition();
	}
	
	/**
	 * 同じ盤面のフレームを複製する
	 * @throws PencilBoxClassException
	 */
	public void duplicateFrame() throws PencilBoxClassException {
		BoardCopierBase copier = (BoardCopierBase) ClassUtil.createInstance(pencilType, ClassUtil.BOARD_COPIER_CLASS);
		Problem src = caller.getProblem();
		Problem problem = new Problem(src, copier.duplicateBoard(src.getBoard()));
		createNewFrame(problem);
	}
	
	/**
	 * 盤面を回転する
	 * @param n 回転番号
	 * @throws PencilBoxClassException
	 */
	public void rotateBoard(int n) throws PencilBoxClassException {
		BoardCopierBase copier = (BoardCopierBase) ClassUtil.createInstance(pencilType, ClassUtil.BOARD_COPIER_CLASS);
		Problem problem = caller.getProblem();
		problem.setBoard(copier.duplicateBoard(problem.getBoard(), n));
		createNewBoard(problem);
	}
	/**
	 * 盤面サイズを変更する
	 * @param size 変更後の盤面サイズ
	 * @throws PencilBoxClassException
	 */
	public void changeBoardSize(Size size) throws PencilBoxClassException {
		BoardCopierBase copier = (BoardCopierBase) ClassUtil.createInstance(pencilType, ClassUtil.BOARD_COPIER_CLASS);
		Problem problem = caller.getProblem();
		problem.setBoard(copier.duplicateBoard(problem.getBoard(), size));
		createNewBoard(problem);
	}
	/**
	 * 他のフレームから呼び出された場合は，呼び出し元フレームの設定を引き継ぐ。
	 * そうでない場合は，デフォルト設定ファイルの設定を読み込む。
	 * @param dst 作成中アプリケーションの MenuCommand インスタンス
	 */
	private void copyPreferences(MenuCommand dst) {
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

}
