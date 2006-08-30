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
 * メニューコマンド処理クラス
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
	 * 初期化処理を行い，各種クラスと関連付ける
	 * オブジェクト生成直後に使用する
	 * @param puzzleType パズルの種類
	 * @param frame 関連付けるFrame
	 * @param panel 関連付けるPanel
	 * @param problem 関連付けるProblem
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
		JOptionPane.showMessageDialog(frame,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * @param s
	 */
	public void showErrorMessage(String s) {
		JOptionPane.showMessageDialog(frame,s,"Error",JOptionPane.ERROR_MESSAGE);
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
			newBoardDialog.setPencilType(pencilType);
			newBoardDialog.setCurrentSize(board.getSize());
			if (newBoardDialog.showDialog(frame, "新規盤面") == PencilBoxDialog.OK_OPTION) {
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
			showErrorMessage("不正なサイズです");
			return false;
		} else if (rows > 200 || cols > 200) {
			showErrorMessage("さすがに大きすぎでしょ");
			return false;
		}
		if (pencilType.getPencilName().equals("sudoku")) {
			if (rows == cols) {
				for (int n = 1; n < 10; n++) {
					if (rows == n*n)
						return true;
				}
			}
			showErrorMessage("不正なサイズです");
			return false;
		}
		return true;
	}
	/**
	 * ファイル選択ダイアログに現在のファイルパスを設定する
	 */
	private void prepareFileChooser() {
		if (fileChooser == null)
			fileChooser = FileChooser.getInstance();
		File currentFile = problem.getFile();
		if (currentFile != null)
			fileChooser.setSelectedFile(currentFile);
	}
	/**
	 *  [ファイル]-[開く]
	 *  もとの問題がファイル名無しである場合は，もとのフレームは閉じる。つまり［閉じて開く］と同じ処理となる
	 */
	public void open() {
		if (problem.getFile() == null)
			open1(1);
		else
			open1(0);
	}
	/**
	 *  [ファイル]-[閉じて開く]
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
	 * open()の実際の処理を行う
	 * @param mode 0: 開くだけ
	 * 	           1: 開いて（成功したら）もとのフレームは閉じる
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
	 *  [ファイル]-[保存]
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
	 *  [ファイル]-[印刷]
	 * いい加減なつくり
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
	 *  [ファイル]-[問題データ文字列出力]
	 */
	void exporProblemDatatString() {
		DataExportDialog dataExportFrame = new DataExportDialog();
		try {
			String problemDataS = IOController.getInstance(pencilType).getProblemDataString(board);
			dataExportFrame.setText("problem=" + problemDataS);
			dataExportFrame.showDialog(frame, "問題データ文字列出力");
		} catch (PencilBoxClassException e) {
			e.printStackTrace();
		}
	}
	/**
	 *  [ファイル]-[画像保存]
	 */
	public void saveImage() {
		new PanelImageWriter().run(panel);
	}
	/**
	 *  [ファイル]-[画像コピー]
	 */
	public void copyImage() {
		new PanelImageTransfer().run(panel);
	}
	/**
	 *  [ファイル]-[プロパティ]
	 */
	public void property() {
		PropertyDialog propertyDialog = PropertyDialog.getInstance();
		propertyDialog.setPropertyToDialog(problem.getProperty());
		if (propertyDialog.showDialog(frame, "プロパティ") == PencilBoxDialog.OK_OPTION)
			propertyDialog.getPropertyFromDialog(problem.getProperty());
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
				"PencilBoxについて",
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
	 *  [編集]-[解答消去]
	 */
	public void clear() {
		undoManager.discardAllEdits();
		board.clearBoard();
		panel.repaint();
	}
//	/**
//	 *  [編集]-[解答整形]
//	 */
//	public void trimAnswer() {
//		board.trimAnswer();
//		panel.repaint();
//	}
	/**
	 *  [編集]-[正答判定]
	 */
	public void checkAnswer() {
		JOptionPane.showMessageDialog(
			frame,
			board.checkAnswerString(),
			"正答判定",
			JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * [編集]-[UNDO]
	 */
	public void undo() {
		if (undoManager.canUndo())
			undoManager.undo();
	}
	/**
	 * [編集]-[REDO]
	 */
	public void redo() {
		if (undoManager.canRedo())
			undoManager.redo();
	}
	/**
	 * [編集]+[最初までUNDO]
	 */
	public void undoAll() {
		while (undoManager.canUndo()) {
			undoManager.undo();
		}
	}
	/**
	 * [編集]+[最後までREDO]
	 */
	public void redoAll() {
		while (undoManager.canRedo()) {
			undoManager.redo();
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
	 * UNDO 可能か
	 * @return UNDO 可能か
	 */
	public boolean canUndo() {
		return undoManager.canUndo();
	}
	/**
	 * REDO 可能か
	 * @return REDO 可能か 
	 */
	public boolean canRedo() {
		return undoManager.canRedo();
	}
	/**
	 *  [編集]-[問題入力モード]
	 *  @param b true で問題入力モードにし，false で解答モードにする
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
	 *  [表示]-[カーソルON]
	 */
	public void setCursorOn(boolean b) {
		panel.setCursorOn(b);
		panel.repaint();
	}
	/**
	 *  [表示]-[行列番号表示]
	 */
	public void setShowIndexMode(boolean b) {
		panel.changeShowIndexMode(b);
		frame.pack();
	}
	/**
	 *  [表示]-[色の更新]
	 */
	public void renewColor() {
		Colors.randomize();
		panel.repaint();
	}
}
