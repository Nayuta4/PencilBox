package pencilbox.common.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.PencilBoxException;
import pencilbox.common.core.Problem;
import pencilbox.common.core.Size;
import pencilbox.common.core.UndoManager;
import pencilbox.common.factory.Constants;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilFactory;
import pencilbox.common.factory.PencilType;
import pencilbox.common.io.IOController;
import pencilbox.common.io.IOController.DataFormat;
import pencilbox.resource.Messages;
import pencilbox.util.Colors;


/**
 * ���j���[�R�}���h�����N���X
 */
public class MenuCommand {

	private PencilType pencilType;

	private Frame frame;
	private PanelBase panel;
	private EventHandlerManager handler;
	private Problem problem;
	private BoardBase board;
//	private UndoManager undoManager;

	private Timer playBackTimer;

	/**
	 * �������������s���C�e��N���X�Ɗ֘A�t����
	 * �I�u�W�F�N�g��������Ɏg�p����
	 * @param puzzleType �p�Y���̎��
	 * @param frame �֘A�t����Frame
	 * @param panel �֘A�t����Panel
	 * @param problem �֘A�t����Problem
	 */
	public void setup(PencilType puzzleType, Frame frame, PanelBase panel, EventHandlerManager handler, Problem problem) {
		this.pencilType = puzzleType;
		this.frame = frame;
		this.panel = panel;
		this.handler = handler;
		this.problem = problem;
		this.board = problem.getBoard();
		setFrameTitle();
		if (board.getUndoManager() == null) {
			board.setUndoManager(new UndoManager(board));
		}
		board.initBoard();
	}
	public void setup(PencilType puzzleType, Problem problem) {
		setup(puzzleType, this.frame, this.panel, this.handler, problem);
	}
	/**
	 * @return Returns the panel.
	 */
	public PanelBase getPanelBase() {
		return panel;
	}
	/**
	 * @return Returns the handler.
	 */
	public EventHandlerManager getEventHandlerManager() {
		return handler;
	}
	/**
	 * @return Returns the frame.
	 */
	public Frame getFrame() {
		return frame;
	}
	/**
	 * @return Returns the problem.
	 */
	public Problem getProblem() {
		return problem;
	}
	/**
	 * @return Returns the undoManager.
	 */
	private UndoManager getUndoManager() {
		return board.getUndoManager();
	}

	/**
	 * �t���[���^�C�g����ݒ肷��
	 * �ҏW���̃p�Y���̎�ނƃt�@�C������\������
	 */
	private void setFrameTitle() {
//		frame.setTitle(problem.getFileName() + " - " + pencilType.getTitle() + " - " + Constants.TITLE);
		frame.setTitle(problem.getFileName() + " - " + Constants.TITLE);
	}
	/**
	 * @param e
	 */
	public void showErrorMessage(Exception e) {
		JOptionPane.showMessageDialog(frame,e.getMessage(),Messages.getString("MenuCommand.Message_Error"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	}
	/**
	 * @param s
	 */
	public void showErrorMessage(String s) {
		JOptionPane.showMessageDialog(frame,s,Messages.getString("MenuCommand.Message_Error"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	}
	
	/*
	 * �ȉ��C�e���j���[�I�����̏���
	 */
	/**
	 *  [�t�@�C��]-[�V�K�쐬]
	 */
	public void newBoard() {
		try {
			NewBoardDialog newBoardDialog = NewBoardDialog.getInstance();
//			newBoardDialog.setPencilType(pencilType);
			newBoardDialog.setCurrentSize(board.getSize());
			if (newBoardDialog.showDialog(frame, Messages.getString("MenuCommand.newBoardDialog")) == PencilBoxDialog.OK_OPTION) { //$NON-NLS-1$
				Size newSize = newBoardDialog.getNewSize();
				if (newSize != null && isValidSize(newSize)) {
					PencilFactory.getInstance(pencilType, this).createNewFrame(newSize);
				}
			}
		} catch (PencilBoxClassException e) {
			showErrorMessage(e);
		}
	}
	/**
	 * �ՖʃT�C�Y�̊m�F���s��
	 * ���Ƃ̏ꍇ�̔ՖʃT�C�Y�������s���B
	 * �ʃp�Y���Ɋւ��鏈�������ʃN���X�ōs���̂͂����������C�b��I�ɂ����ŏ������邱�Ƃɂ���B
	 * ���̂������Ƃ�����B
	 * @param s �T�C�Y
	 * @return �������T�C�Y�Ȃ� true ��Ԃ�
	 */
	private boolean isValidSize(Size s) {
		int rows = s.getRows();
		int cols = s.getCols();
		if (rows < 0 || cols < 0) {
			showErrorMessage(Messages.getString("MenuCommand.Message_InappropriateSize")); //$NON-NLS-1$
			return false;
		} else if (rows > 200 || cols > 200) {
			showErrorMessage(Messages.getString("MenuCommand.Message_TooLarge")); //$NON-NLS-1$
			return false;
		}
		if (pencilType == PencilType.SUDOKU) {
			if (rows == cols) {
				for (int n = 1; n < 10; n++) {
					if (rows == n*n)
						return true;
				}
			}
			showErrorMessage(Messages.getString("MenuCommand.Message_InappropriateSize")); //$NON-NLS-1$
			return false;
		}
		return true;
	}
	/**
	 * ���t�@�C���I���_�C�A���O���擾���C���݂̃t�@�C���p�X��ݒ肷��B
	 */
	private FileChooser prepareFileChooser() {
		FileChooser fileChooser = FileChooser.getProblemFileChooser();
		File currentFile = problem.getFile();
		if (currentFile != null)
			fileChooser.setSelectedFile(currentFile);
		return fileChooser;
	}
	/**
	 *  [�t�@�C��]-[�J��]
	 */
	public void open() {
		FileChooser fileChooser = prepareFileChooser();
		try {
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				PencilFactory.getInstance(pencilType, this).createNewFrame(file);
			}
		} catch (PencilBoxException e) {
			showErrorMessage(e);
		}
	}
	/**
	 *  [�t�@�C��]-[���ĊJ��]
	 */
	public void closeAndOpen() {
		FileChooser fileChooser = prepareFileChooser();
		try {
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				PencilFactory.getInstance(pencilType, this).createNewBoard(file);
			}
		} catch (PencilBoxException e) {
			showErrorMessage(e);
		}
	}
	/**
	 *  [�t�@�C��]-[�ۑ�]
	 */
	public void save() {
		FileChooser fileChooser = prepareFileChooser();
		try {
			if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				IOController.getInstance(pencilType).saveFile(problem, file);
			}
		} catch (PencilBoxException e) {
			showErrorMessage(e);
		}
		setFrameTitle();
	}

	/**
	 *  [�t�@�C��]-[����]
	 */
	public void duplicate() {
		try {
			PencilFactory.getInstance(pencilType, this).duplicateFrame();
		} catch (PencilBoxClassException e) {
			showErrorMessage(e);
		}
	}

	/**
	 *  [�t�@�C��]-[��]�E���]]
	 */
	public void rotateBoard() {
		try {
			RotateBoardDialog dialog = RotateBoardDialog.getInstance();
			if (dialog.showDialog(frame, Messages.getString("MenuCommand.rotateBoardDialog")) == PencilBoxDialog.OK_OPTION) { //$NON-NLS-1$
				int n = dialog.getSelection();
				if (n >= 1 && n <= 7) {
					PencilFactory.getInstance(pencilType, this).rotateBoard(n);
				}
			}
		} catch (PencilBoxClassException e) {
			showErrorMessage(e);
		}
	}

	public void rotateBoard(int n) {
		try {
			PencilFactory.getInstance(pencilType, this).rotateBoard(n);
		} catch (PencilBoxClassException e) {
			showErrorMessage(e);
		}
	}
	/**
	 *  [�t�@�C��]-[�T�C�Y�ύX]
	 */
	public void changeBoardSize() {
		try {
			NewBoardDialog newBoardDialog = NewBoardDialog.getInstance();
			newBoardDialog.setCurrentSize(board.getSize());
			if (newBoardDialog.showDialog(frame, Messages.getString("MenuCommand.changeBoardSizeDialog")) == PencilBoxDialog.OK_OPTION) { //$NON-NLS-1$
				Size newSize = newBoardDialog.getNewSize();
				if (newSize != null && isValidSize(newSize)) {
					PencilFactory.getInstance(pencilType, this).changeBoardSize(newSize);
				}
			}
		} catch (PencilBoxClassException e) {
			showErrorMessage(e);
		}
	}
	/**
	 *  [�t�@�C��]-[����v���r���[]
	 */
	public void printPreview() {
		PrintPreviewDialog d = PrintPreviewDialog.getInstance();
		d.setPrintObject(panel);
		d.showDialog(frame);
	}
	/**
	 *  [�t�@�C��]-[���]
	 */
	public void print() {
		PrintPreviewDialog d = PrintPreviewDialog.getInstance();
		d.setPrintObject(panel);
		d.print();
	}
	/**
	 *  [�t�@�C��]-[�G�N�X�|�[�g�^�C���|�[�g]
	 *  ���݂̖��̃f�[�^���_�C�A���O�ɕ\������B
	 *  �_�C�A���O����̖߂�l��OK�̂Ƃ��́C�ʃt���[���Ƀ_�C�A���O�ɓ��͂��ꂽ������̖����J���B
	 */
	public void exportProblemData(DataFormat f) {
		DataExportDialog dataExportFrame = new DataExportDialog();
		try {
			IOController io = IOController.getInstance(pencilType);
			String url = io.exportProblemData(board, f);
			dataExportFrame.setText(url);
			int ret = dataExportFrame.showDialog(frame, Messages.getString("MenuCommand.dataExportDialog")); //$NON-NLS-1$
			if (ret == PencilBoxDialog.OK_OPTION) {
				String s = dataExportFrame.getText();
				Problem problem = io.importProblemData(s, f);
				PencilFactory.getInstance(pencilType, this).createNewFrame(problem);
			}
		} catch (PencilBoxException e) {
			e.printStackTrace();
		}
	}
	/**
	 *  [�t�@�C��]-[�摜�ۑ�]
	 */
	public void saveImage() {
		new PanelImageWriter().saveImage(panel);
	}
	/**
	 *  [�t�@�C��]-[�摜�R�s�[]
	 */
	public void copyImage() {
		new PanelImageTransfer().copyImage(panel);
	}
	/**
	 *  [�t�@�C��]-[�v���p�e�B]
	 */
	public void property() {
		PropertyDialog propertyDialog = PropertyDialog.getInstance();
		propertyDialog.setPropertyToDialog(problem.getProperty());
		if (propertyDialog.showDialog(frame, Messages.getString("MenuCommand.properyDialog")) == PencilBoxDialog.OK_OPTION) //$NON-NLS-1$
			propertyDialog.getPropertyFromDialog(problem.getProperty());
	}
	/**
	 *  [�t�@�C��]-[�ݒ�Ǎ�]
	 */
	public void loadPreferences() {
		PreferencesCopierBase copier = PreferencesCopierBase.createInstance(pencilType);
		FileChooser fileChooser = FileChooser.getPreferenceFileChooser();
		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			copier.loadPreferences(this, file);
			frame.resize();
//			menu.updateCurrentMenuSelection();
		}
	}
	/**
	 *  [�t�@�C��]-[�ݒ�ۑ�]
	 */
	public void storePreferences() {
		PreferencesCopierBase copier = PreferencesCopierBase.createInstance(pencilType);
		FileChooser fileChooser = FileChooser.getPreferenceFileChooser();
		if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			copier.storePreferences(this, file);
		}
	}
	
	/**
	 *  [�t�@�C��]-[����]
	 */
	public void close() {
		frame.dispose();
	}
	/**
	 *  [�t�@�C��]-[�S�I��]
	 */
	public void quit() {
		System.exit(0);
	}
	/**
	 *  [�w���v]-[�o�[�W�������]
	 */
	public void about() {
//		AboutDialog dialog = AboutDialog.getInstance();
//		dialog.setPuzzleType(puzzleType);
//		showDialog(dialog);
		JOptionPane.showMessageDialog(
				frame,
				getAboutText(),
				Messages.getString("MenuCommand.aboutDialog"), //$NON-NLS-1$
				JOptionPane.INFORMATION_MESSAGE);
	}
	private String getAboutText() {
		return "" //$NON-NLS-1$
		+ Constants.TITLE + " version " + Constants.VERSION + '\n'
		+ Constants.COPYRIGHT + '\n'
		+ Constants.URL +'\n'
		;
	}

	/**
	 *  [�ҏW]-[�𓚏���]
	 */
	public void clear() {
		getUndoManager().discardAllEdits();
		board.clearBoard();
		handler.resetImmediateAnswerCheckMode();
		panel.repaint();
	}
	/**
	 *  [�ҏW]-[�⏕�L������]
	 */
	public void trimAnswer() {
		board.trimAnswer();
		panel.repaint();
	}
	/**
	 *  [�ҏW]-[�Ώ̔z�u]
	 */
	public void setSymmetricPlacementMode(boolean b) {
		handler.setSymmetricPlacementMode(b);
	}
	/**
	 *  [�ҏW]-[���𔻒�]
	 */
	public void checkAnswer() {
		JOptionPane.showMessageDialog(
			frame,
			board.checkAnswerString(),
			Messages.getString("MenuCommand.checkAnswerDialog"), //$NON-NLS-1$
			JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 *  [�ҏW]-[�������𔻒�]
	 */
	public void setImmediateAnswerCheckMode(boolean b) {
		handler.setImmediateAnswerCheckMode(b);
		if (b == true)
			handler.checkAnswer();
	}
	/**
	 * [�ҏW]-[UNDO]
	 */
	public void undo() {
		if (getUndoManager().canUndo())
			getUndoManager().undo();
	}
	/**
	 * [�ҏW]-[REDO]
	 */
	public void redo() {
		if (getUndoManager().canRedo())
			getUndoManager().redo();
	}
	/**
	 * [�ҏW]+[�ŏ��܂�UNDO]
	 */
	public void undoAll() {
		while (getUndoManager().canUndo()) {
			getUndoManager().undo();
		}
	}
	/**
	 * [�ҏW]+[�Ō�܂�REDO]
	 */
	public void redoAll() {
		while (getUndoManager().canRedo()) {
			getUndoManager().redo();
		}
	}
	/**
	 *  [�ҏW]-[�����Đ�]
	 */
	public void playback() {
		undoAll();
		if(playBackTimer == null)
			makePlayBackTimer();
		playBackTimer.start();
	}
	/**
	 * �����Đ��p�^�C�}�[���쐬����
	 * ���߂ɂ͂��߂Ɏg���Ƃ��ɂP�x�����g�p����
	 * 
	 */
	private void makePlayBackTimer() {
		playBackTimer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (getUndoManager().canRedo()) {
					getUndoManager().redo();
					panel.repaint();
				} else {
					playBackTimer.stop();
				}
			}
		});
	}
	/**
	 *  [�ҏW]-[���𑀍�]
	 */
	public void history() {
		HistoryDialog historyDialog = HistoryDialog.getInstance();
		historyDialog.init(getPanelBase(), getUndoManager());
		int k = getUndoManager().getIndexOfNextAdd();
		int ret = historyDialog.showDialog(frame, Messages.getString("MenuCommand.historyDialog")); //$NON-NLS-1$
		if (ret == PencilBoxDialog.OK_OPTION) {
		} else if (ret == PencilBoxDialog.CANCEL_OPTION || ret == PencilBoxDialog.CLOSED_OPTION) {
			getUndoManager().jumpTo(k);
			panel.repaint();
		}
	}
	/**
	 * UNDO �\��
	 * @return UNDO �\��
	 */
	public boolean canUndo() {
		return getUndoManager().canUndo();
	}
	/**
	 * REDO �\��
	 * @return REDO �\�� 
	 */
	public boolean canRedo() {
		return getUndoManager().canRedo();
	}

	/**
	 *  [�ҏW]-[�𓚃��[�h]
	 *       -[�����̓��[�h]
	 *       -[�̈�ҏW���[�h]
	 *  @param mode
	 */
	public void setEditMode(int mode) {
		if (panel.getEditMode() == mode)
			return;
		handler.setEditMode(mode);
		board.initBoard();
	}
	
	/**
	 *  [�\��]-[�J�[�\��]
	 */
	public void setCursorMode(boolean b) {
		panel.setCursorMode(b);
	}
	/**
	 *  [�\��]-[�\���T�C�Y]
	 */
	public void cellSize() {
		int currentCellSize = getPanelBase().getCellSize();
		CellSizeDialog cellSizeDialog = CellSizeDialog.getInstance();
		cellSizeDialog.setPanel(getPanelBase());
		cellSizeDialog.setCellSize(currentCellSize);
		int result = cellSizeDialog.showDialog(frame, Messages.getString("MenuCommand.cellSizeDialog")); //$NON-NLS-1$
		int newCellSize = cellSizeDialog.getCellSize();
		if (result == PencilBoxDialog.OK_OPTION) {
			getPanelBase().setDisplaySize(newCellSize);
			getFrame().resize();
		} else if (result == PencilBoxDialog.CANCEL_OPTION || result == PencilBoxDialog.CLOSED_OPTION) {
			if (currentCellSize != newCellSize) {
				getPanelBase().setDisplaySize(currentCellSize);
				getFrame().resize();
			}
		}
	}
	/**
	 *  [�\��]-[�s��ԍ��\��]
	 */
	public void setIndexMode(boolean b) {
		panel.changeIndexMode(b);
		frame.resize();
	}
	/**
	 *  [�\��]-[�r���\��]
	 */
	public void setGridStyle(boolean b) {
		panel.setGridStyle(b ? 1 : 0);
	}
	/**
	 *  [�\��]-[�F�̍X�V]
	 */
	public void renewColor() {
		Colors.randomize();
		panel.repaint();
	}
}