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
 * メニューコマンド処理クラス
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
	 * 初期化処理を行い，各種クラスと関連付ける
	 * オブジェクト生成直後に使用する
	 * @param puzzleType パズルの種類
	 * @param frame 関連付けるFrame
	 * @param panel 関連付けるPanel
	 * @param problem 関連付けるProblem
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
	 * フレームタイトルを設定する
	 * 編集中のパズルの種類とファイル名を表示する
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
	 * 以下，各メニュー選択時の処理
	 */
	/**
	 *  [ファイル]-[新規作成]
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
	 * 盤面サイズの確認を行う
	 * 数独の場合の盤面サイズ制限も行う。
	 * 個別パズルに関する処理を共通クラスで行うのはおかしいが，暫定的にここで処理することにする。
	 * そのうち何とかする。
	 * @param s サイズ
	 * @return 正しいサイズなら true を返す
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
	 * 問題ファイル選択ダイアログを取得し，現在のファイルパスを設定する。
	 */
	private FileChooser prepareFileChooser() {
		FileChooser fileChooser = FileChooser.getProblemFileChooser();
		File currentFile = problem.getFile();
		if (currentFile != null)
			fileChooser.setSelectedFile(currentFile);
		return fileChooser;
	}
	/**
	 *  [ファイル]-[開く]
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
	 *  [ファイル]-[閉じて開く]
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
	 *  [ファイル]-[保存]
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
	 *  [ファイル]-[複製]
	 */
	public void duplicate() {
		try {
			PencilFactory.getInstance(pencilType, this).duplicateFrame();
		} catch (PencilBoxClassException e) {
			showErrorMessage(e);
		}
	}

	/**
	 *  [ファイル]-[回転・反転]
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
	 *  [ファイル]-[サイズ変更]
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
	 *  [ファイル]-[印刷プレビュー]
	 */
	public void printPreview() {
		PrintPreviewDialog d = PrintPreviewDialog.getInstance();
		d.setPrintObject(panel);
		d.showDialog(frame);
	}
	/**
	 *  [ファイル]-[印刷]
	 */
	public void print() {
		PrintPreviewDialog d = PrintPreviewDialog.getInstance();
		d.setPrintObject(panel);
		d.print();
	}
	/**
	 *  [ファイル]-[エクスポート／インポート]
	 *  現在の問題のデータをダイアログに表示する。
	 *  ダイアログからの戻り値がOKのときは，別フレームにダイアログに入力された文字列の問題を開く。
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
	 *  [ファイル]-[画像保存]
	 */
	public void saveImage() {
		new PanelImageWriter().saveImage(panel);
	}
	/**
	 *  [ファイル]-[画像コピー]
	 */
	public void copyImage() {
		new PanelImageTransfer().copyImage(panel);
	}
	/**
	 *  [ファイル]-[プロパティ]
	 */
	public void property() {
		PropertyDialog propertyDialog = PropertyDialog.getInstance();
		propertyDialog.setPropertyToDialog(problem.getProperty());
		if (propertyDialog.showDialog(frame, Messages.getString("MenuCommand.properyDialog")) == PencilBoxDialog.OK_OPTION) //$NON-NLS-1$
			propertyDialog.getPropertyFromDialog(problem.getProperty());
	}
	/**
	 *  [ファイル]-[設定読込]
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
	 *  [ファイル]-[設定保存]
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
	 *  [ファイル]-[閉じる]
	 */
	public void close() {
		frame.dispose();
	}
	/**
	 *  [ファイル]-[全終了]
	 */
	public void quit() {
		System.exit(0);
	}
	/**
	 *  [ヘルプ]-[バージョン情報]
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
	 *  [編集]-[解答消去]
	 */
	public void clear() {
		getUndoManager().discardAllEdits();
		board.clearBoard();
		handler.resetImmediateAnswerCheckMode();
		panel.repaint();
	}
	/**
	 *  [編集]-[補助記号消去]
	 */
	public void trimAnswer() {
		board.trimAnswer();
		panel.repaint();
	}
	/**
	 *  [編集]-[対称配置]
	 */
	public void setSymmetricPlacementMode(boolean b) {
		handler.setSymmetricPlacementMode(b);
	}
	/**
	 *  [編集]-[正解判定]
	 */
	public void checkAnswer() {
		JOptionPane.showMessageDialog(
			frame,
			board.checkAnswerString(),
			Messages.getString("MenuCommand.checkAnswerDialog"), //$NON-NLS-1$
			JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 *  [編集]-[即時正解判定]
	 */
	public void setImmediateAnswerCheckMode(boolean b) {
		handler.setImmediateAnswerCheckMode(b);
		if (b == true)
			handler.checkAnswer();
	}
	/**
	 * [編集]-[UNDO]
	 */
	public void undo() {
		if (getUndoManager().canUndo())
			getUndoManager().undo();
	}
	/**
	 * [編集]-[REDO]
	 */
	public void redo() {
		if (getUndoManager().canRedo())
			getUndoManager().redo();
	}
	/**
	 * [編集]+[最初までUNDO]
	 */
	public void undoAll() {
		while (getUndoManager().canUndo()) {
			getUndoManager().undo();
		}
	}
	/**
	 * [編集]+[最後までREDO]
	 */
	public void redoAll() {
		while (getUndoManager().canRedo()) {
			getUndoManager().redo();
		}
	}
	/**
	 *  [編集]-[履歴再生]
	 */
	public void playback() {
		undoAll();
		if(playBackTimer == null)
			makePlayBackTimer();
		playBackTimer.start();
	}
	/**
	 * 履歴再生用タイマーを作成する
	 * 初めにはじめに使うときに１度だけ使用する
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
	 *  [編集]-[履歴操作]
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
	 * UNDO 可能か
	 * @return UNDO 可能か
	 */
	public boolean canUndo() {
		return getUndoManager().canUndo();
	}
	/**
	 * REDO 可能か
	 * @return REDO 可能か 
	 */
	public boolean canRedo() {
		return getUndoManager().canRedo();
	}

	/**
	 *  [編集]-[解答モード]
	 *       -[問題入力モード]
	 *       -[領域編集モード]
	 *  @param mode
	 */
	public void setEditMode(int mode) {
		if (panel.getEditMode() == mode)
			return;
		handler.setEditMode(mode);
		board.initBoard();
	}
	
	/**
	 *  [表示]-[カーソル]
	 */
	public void setCursorMode(boolean b) {
		panel.setCursorMode(b);
	}
	/**
	 *  [表示]-[表示サイズ]
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
	 *  [表示]-[行列番号表示]
	 */
	public void setIndexMode(boolean b) {
		panel.changeIndexMode(b);
		frame.resize();
	}
	/**
	 *  [表示]-[罫線表示]
	 */
	public void setGridStyle(boolean b) {
		panel.setGridStyle(b ? 1 : 0);
	}
	/**
	 *  [表示]-[色の更新]
	 */
	public void renewColor() {
		Colors.randomize();
		panel.repaint();
	}
}