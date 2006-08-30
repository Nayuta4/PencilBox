package pencilbox.common.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.undo.UndoManager;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.PencilBoxException;
import pencilbox.common.core.Problem;
import pencilbox.common.core.Size;
import pencilbox.common.factory.Constants;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilFactory;
import pencilbox.common.factory.PencilType;
import pencilbox.common.io.IOController;
import pencilbox.util.Colors;


/**
 * ���j���[�R�}���h�����N���X
 */
public class MenuCommand {

	private PencilType pencilType;

	private Frame frame;
	private PanelEventHandler panel;
	private Problem problem;
	private BoardBase board;
	private UndoManager undoManager;

	private JFileChooser fileChooser;
	private Timer playBackTimer;

	/**
	 * �������������s���C�e��N���X�Ɗ֘A�t����
	 * �I�u�W�F�N�g��������Ɏg�p����
	 * @param puzzleType �p�Y���̎��
	 * @param frame �֘A�t����Frame
	 * @param panel �֘A�t����Panel
	 * @param problem �֘A�t����Problem
	 */
	public void setup(PencilType puzzleType, Frame frame, PanelEventHandler panel, Problem problem) {
		this.pencilType = puzzleType;
		this.frame = frame;
		this.panel = panel;
		this.problem = problem;
		this.board = problem.getBoard();
		setFrameTitle();
		undoManager = new UndoManager();
		undoManager.setLimit(1000);
		board.setUndoManager(undoManager);
		board.initBoard();
	}
	public void setup(PencilType puzzleType, Problem problem) {
		this.pencilType = puzzleType;
//		this.frame = frame;
//		this.panel = panel;
		this.problem = problem;
		this.board = problem.getBoard();
		setFrameTitle();
		undoManager = new UndoManager();
		undoManager.setLimit(1000);
		board.setUndoManager(undoManager);
		board.initBoard();
	}
	/**
	 * @return Returns the panel.
	 */
	public PanelEventHandler getPanelBase() {
		return panel;
	}
	/**
	 * @return Returns the frame.
	 */
	public Frame getFrame() {
		return frame;
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
		JOptionPane.showMessageDialog(frame,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * @param s
	 */
	public void showErrorMessage(String s) {
		JOptionPane.showMessageDialog(frame,s,"Error",JOptionPane.ERROR_MESSAGE);
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
			newBoardDialog.setPencilType(pencilType);
			newBoardDialog.setCurrentSize(board.getSize());
			if (newBoardDialog.showDialog(frame, "�V�K�Ֆ�") == PencilBoxDialog.OK_OPTION) {
				Size newSize = newBoardDialog.getNewSize();
				if (newSize != null && isValidSize(newSize)) {
					PencilFactory.getInstance(pencilType).createNewFrame(newSize);
					if (problem.getFile() == null)
						frame.dispose();
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
			showErrorMessage("�s���ȃT�C�Y�ł�");
			return false;
		} else if (rows > 200 || cols > 200) {
			showErrorMessage("�������ɑ傫�����ł���");
			return false;
		}
		if (pencilType.getPencilName().equals("sudoku")) {
			if (rows == cols) {
				for (int n = 1; n < 10; n++) {
					if (rows == n*n)
						return true;
				}
			}
			showErrorMessage("�s���ȃT�C�Y�ł�");
			return false;
		}
		return true;
	}
	/**
	 * �t�@�C���I���_�C�A���O�Ɍ��݂̃t�@�C���p�X��ݒ肷��
	 */
	private void prepareFileChooser() {
		if (fileChooser == null)
			fileChooser = FileChooser.getInstance();
		File currentFile = problem.getFile();
		if (currentFile != null)
			fileChooser.setSelectedFile(currentFile);
	}
	/**
	 *  [�t�@�C��]-[�J��]
	 *  ���Ƃ̖�肪�t�@�C���������ł���ꍇ�́C���Ƃ̃t���[���͕���B�܂�m���ĊJ���n�Ɠ��������ƂȂ�
	 */
	public void open() {
		if (problem.getFile() == null)
			open1(1);
		else
			open1(0);
	}
	/**
	 *  [�t�@�C��]-[���ĊJ��]
	 */
	public void closeAndOpen() {
//		open1(1);
		prepareFileChooser();
		try {
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				PencilFactory.getInstance(pencilType).createNewBoard(this, file);
			}
		} catch (PencilBoxException e) {
			showErrorMessage(e);
		}
	}
	/**
	 * open()�̎��ۂ̏������s��
	 * @param mode 0: �J������
	 * 	           1: �J���āi����������j���Ƃ̃t���[���͕���
	 */
	private void open1(int mode) {
		prepareFileChooser();
		try {
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				PencilFactory.getInstance(pencilType).createNewFrame(file);
				if (mode == 1)
					frame.dispose();
			}
		} catch (PencilBoxException e) {
			showErrorMessage(e);
		}
	}
	/**
	 *  [�t�@�C��]-[�ۑ�]
	 */
	public void save() {
		prepareFileChooser();
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
	 *  [�t�@�C��]-[���]
	 * ���������Ȃ���
	 */
	public void print() {
		try {
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(panel);
			if (job.printDialog()) {
				job.print();
			}
		} catch (PrinterException e) {
			showErrorMessage(e);
		}
	}
	/**
	 *  [�t�@�C��]-[���f�[�^������o��]
	 */
	void exporProblemDatatString() {
		DataExportDialog dataExportFrame = new DataExportDialog();
		try {
			String problemDataS = IOController.getInstance(pencilType).getProblemDataString(board);
			dataExportFrame.setText("problem=" + problemDataS);
			dataExportFrame.showDialog(frame, "���f�[�^������o��");
		} catch (PencilBoxClassException e) {
			e.printStackTrace();
		}
	}
	/**
	 *  [�t�@�C��]-[�摜�ۑ�]
	 */
	public void saveImage() {
		new PanelImageWriter().run(panel);
	}
	/**
	 *  [�t�@�C��]-[�摜�R�s�[]
	 */
	public void copyImage() {
		new PanelImageTransfer().run(panel);
	}
	/**
	 *  [�t�@�C��]-[�v���p�e�B]
	 */
	public void property() {
		PropertyDialog propertyDialog = PropertyDialog.getInstance();
		propertyDialog.setPropertyToDialog(problem.getProperty());
		if (propertyDialog.showDialog(frame, "�v���p�e�B") == PencilBoxDialog.OK_OPTION)
			propertyDialog.getPropertyFromDialog(problem.getProperty());
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
				"PencilBox�ɂ���",
				JOptionPane.INFORMATION_MESSAGE);
	}
	private String getAboutText() {
		return ""
		+ Constants.TITLE + " version " + Constants.VERSION + '\n'
		+ Constants.COPYRIGHT + '\n'
		+ Constants.URL +'\n'
		;
	}

	/**
	 *  [�ҏW]-[�𓚏���]
	 */
	public void clear() {
		undoManager.discardAllEdits();
		board.clearBoard();
		panel.repaint();
	}
//	/**
//	 *  [�ҏW]-[�𓚐��`]
//	 */
//	public void trimAnswer() {
//		board.trimAnswer();
//		panel.repaint();
//	}
	/**
	 *  [�ҏW]-[��������]
	 */
	public void checkAnswer() {
		JOptionPane.showMessageDialog(
			frame,
			board.checkAnswerString(),
			"��������",
			JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * [�ҏW]-[UNDO]
	 */
	public void undo() {
		if (undoManager.canUndo())
			undoManager.undo();
	}
	/**
	 * [�ҏW]-[REDO]
	 */
	public void redo() {
		if (undoManager.canRedo())
			undoManager.redo();
	}
	/**
	 * [�ҏW]+[�ŏ��܂�UNDO]
	 */
	public void undoAll() {
		while (undoManager.canUndo()) {
			undoManager.undo();
		}
	}
	/**
	 * [�ҏW]+[�Ō�܂�REDO]
	 */
	public void redoAll() {
		while (undoManager.canRedo()) {
			undoManager.redo();
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
				if (undoManager.canRedo()) {
					undoManager.redo();
					panel.repaint();
				} else {
					playBackTimer.stop();
				}
			}
		});
	}
	/**
	 * UNDO �\��
	 * @return UNDO �\��
	 */
	public boolean canUndo() {
		return undoManager.canUndo();
	}
	/**
	 * REDO �\��
	 * @return REDO �\�� 
	 */
	public boolean canRedo() {
		return undoManager.canRedo();
	}
	/**
	 *  [�ҏW]-[�����̓��[�h]
	 *  @param b true �Ŗ����̓��[�h�ɂ��Cfalse �ŉ𓚃��[�h�ɂ���
	 */
	public void setProblemEditMode(boolean b) {
		if(panel.isProblemEditMode() == b)
			return;
		if (b==false)
			board.initBoard();
		panel.setProblemEditMode(b);
		panel.repaint();
	}
	/**
	 *  [�\��]-[�J�[�\��ON]
	 */
	public void setCursorOn(boolean b) {
		panel.setCursorOn(b);
		panel.repaint();
	}
	/**
	 *  [�\��]-[�s��ԍ��\��]
	 */
	public void setShowIndexMode(boolean b) {
		panel.changeShowIndexMode(b);
		frame.pack();
	}
	/**
	 *  [�\��]-[�F�̍X�V]
	 */
	public void renewColor() {
		Colors.randomize();
		panel.repaint();
	}
}
