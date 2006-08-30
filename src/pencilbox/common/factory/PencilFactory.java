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
 * PencilBox �t�@�N�g���[�N���X
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
	 * PencilPuzzle�C���X�^���X���쐬���Ď擾����
	 * @param pencilType 
	 * @return �V����PencilPuzzle�C���X�^���X
	 */
	public static PencilFactory getInstance(PencilType pencilType) {
		return new PencilFactory(pencilType);
	}
	
	/**
	 * �f�t�H���g�T�C�Y�ŃA�v���P�[�V�����t���[�������B
	 * Launcher, Main ����g�p�����B
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
	 * �ՖʃT�C�Y��^���ăA�v���P�[�V�����t���[�������B
	 * �u�V�K�v���j���[����̂̋N���Ŏg�p�����B
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
	 * ���t�@�C����^���ăA�v���P�[�V�����t���[�������B
	 * �u�J���v���j���[����̋N���Ŏg�p�����B
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
	 * Problem ��^���ăA�v���P�[�V�����t���[�������B
	 * ���̃N���X�����ōŏI�I�ɍ쐬����i�K�Ŏg�p�����B
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
	 * ���t�@�C����ǂݍ���œ����t���[���ŊJ���B
	 * �u���ĊJ���v���j���[����̋N���Ŏg�p�����B
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
	 * Problem ��^���ē����t���[���ŊJ���B
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
