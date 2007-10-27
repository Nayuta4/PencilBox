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
 * PencilBox �t�@�N�g���[�N���X
 */
public class PencilFactory {
	
	private PencilType pencilType;
	private MenuCommand caller; // �Ăяo�������j���[�R�}���h
	
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
	 * PencilPuzzle�C���X�^���X���쐬���Ď擾����
	 * @param pencilType
	 * @param caller�@�Ăяo�������j���[�R�}���h
	 * @return�@�V����PencilPuzzle�C���X�^���X
	 */
	public static PencilFactory getInstance(PencilType pencilType, MenuCommand caller) {
		PencilFactory f = new PencilFactory(pencilType);
		f.pencilType = pencilType;
		f.caller = caller;
		return f;
	}
	/**
	 * �f�t�H���g�T�C�Y�ŃA�v���P�[�V�����t���[�������B
	 * Launcher, Main ����g�p�����B
	 * @throws PencilBoxException
	 */
	public void createNewFrame() throws PencilBoxClassException {
		Size size = pencilType.getDefaultSize();
		createNewFrame(size);
	}
	/**
	 * �ՖʃT�C�Y��^���ăA�v���P�[�V�����t���[�������B
	 * �u�V�K�v���j���[����̂̋N���Ŏg�p�����B
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
	 * ���t�@�C����^���ăA�v���P�[�V�����t���[�������B
	 * �u�J���v���j���[����̋N���Ŏg�p�����B
	 * @param file
	 * @throws PencilBoxException
	 */
	public void createNewFrame(File file) throws PencilBoxException {
		Problem problem = IOController.getInstance(pencilType).openFile(file);
		createNewFrame(problem);
	}
	/**
	 * Problem ��^���ăA�v���P�[�V�����t���[�������B
	 * ���̃N���X�����ōŏI�I�ɍ쐬����i�K�Ŏg�p�����B
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
	 * ���t�@�C����ǂݍ���œ����t���[���ŊJ���B
	 * �u���ĊJ���v���j���[����̋N���Ŏg�p�����B
	 * @param file
	 * @throws PencilBoxException
	 */
	public void createNewBoard(File file) throws PencilBoxException {
		Problem problem = IOController.getInstance(pencilType).openFile(file);
		createNewBoard(problem);
	}
	/**
	 * Problem ��^���ē����t���[���ŊJ���B
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
	 * �����Ֆʂ̃t���[���𕡐�����
	 * @throws PencilBoxClassException
	 */
	public void duplicateFrame() throws PencilBoxClassException {
		BoardCopierBase copier = (BoardCopierBase) ClassUtil.createInstance(pencilType, ClassUtil.BOARD_COPIER_CLASS);
		Problem src = caller.getProblem();
		Problem problem = new Problem(src, copier.duplicateBoard(src.getBoard()));
		createNewFrame(problem);
	}
	
	/**
	 * �Ֆʂ���]����
	 * @param n ��]�ԍ�
	 * @throws PencilBoxClassException
	 */
	public void rotateBoard(int n) throws PencilBoxClassException {
		BoardCopierBase copier = (BoardCopierBase) ClassUtil.createInstance(pencilType, ClassUtil.BOARD_COPIER_CLASS);
		Problem problem = caller.getProblem();
		problem.setBoard(copier.duplicateBoard(problem.getBoard(), n));
		createNewBoard(problem);
	}
	/**
	 * �ՖʃT�C�Y��ύX����
	 * @param size �ύX��̔ՖʃT�C�Y
	 * @throws PencilBoxClassException
	 */
	public void changeBoardSize(Size size) throws PencilBoxClassException {
		BoardCopierBase copier = (BoardCopierBase) ClassUtil.createInstance(pencilType, ClassUtil.BOARD_COPIER_CLASS);
		Problem problem = caller.getProblem();
		problem.setBoard(copier.duplicateBoard(problem.getBoard(), size));
		createNewBoard(problem);
	}
	/**
	 * ���̃t���[������Ăяo���ꂽ�ꍇ�́C�Ăяo�����t���[���̐ݒ�������p���B
	 * �����łȂ��ꍇ�́C�f�t�H���g�ݒ�t�@�C���̐ݒ��ǂݍ��ށB
	 * @param dst �쐬���A�v���P�[�V������ MenuCommand �C���X�^���X
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
