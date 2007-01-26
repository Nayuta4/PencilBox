package pencilbox.common.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import pencilbox.common.factory.Constants;

/**
 * 共通メニュークラス
 */
public class MenuBase {

	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu viewMenu;
	private JMenu helpMenu;
	
	private JMenuItem newBoardItem;
	private JMenuItem openItem;
	private JMenuItem closeAndOpenItem;
	private JMenuItem saveItem;
	private JMenuItem duplicateItem;
	private JMenuItem exportProblemDataStringItem;
	private JMenuItem saveImageItem;
	private JMenuItem copyImageItem;
	private JMenuItem printItem;
	private JMenuItem propertyItem;
	private JMenuItem closeItem;
	private JMenuItem quitItem;
	private JMenuItem aboutItem;
	private JMenuItem clearItem;
	private JMenuItem trimAnswerItem;
	private JMenuItem symmetricPlacementItem;
	private JMenuItem undoItem;
	private JMenuItem redoItem;
//	private JMenuItem undoAllItem;
//	private JMenuItem redoAllItem;
	private JMenuItem palybackItem;
	private JMenuItem checkAnswerItem;
	private JMenuItem immediateAnswerCheckModeItem;
	private JMenuItem cellSizeItem;
	private JMenuItem showIndexModeItem;
	private JMenuItem gridStyleItem;
	private JMenuItem renewColorItem;
	private JMenuItem cursorItem;
	private ButtonGroup modeGroup;
	private JMenuItem problemEditModeItem;
	private JMenuItem answerModeItem;

	private JMenu colorMenu;
	private JMenu rotationMenu;

	private Frame frame;
	private MenuCommand command;
	private PanelBase panel;

	/**
	 * Menuクラスの初期化処理で，Menuインスタンス生成直後に使用される
	 * @param command 関連付けるMenuCommand
	 * @param frame 関連付けるFrame
	 * @param panel 関連付けるPanel
	 */
	public void setup(MenuCommand command, Frame frame, PanelBase panel) {
		this.frame = frame;
		this.command = command;
		this.panel = panel;
		buildCommonMenu();
		buildIndividualMenu();
		setAccelerators();
	}

	/**
	 * @return Returns the panel.
	 */
	protected PanelBase getPanelBase() {
		return panel;
	}

	protected PanelEventHandlerBase getPanelEventHandlerBase() {
		return command.getPanelEventHandlerBase();
	}

	/**
	 * 共通メニューを作成する。
	 */
	protected void buildCommonMenu() {
		buildFileMenu();
		buildEditMenu();
		buildViewMenu();
		buildHelpMenu();

		JMenuBar jMenuBar = frame.getJMenuBar();
		jMenuBar.add(fileMenu);
		jMenuBar.add(editMenu);
		jMenuBar.add(viewMenu);
		jMenuBar.add(helpMenu);
	}

	/**
	 * 個々のパズル用メニューを作成する。 
	 * 各サブクラスでオーバーライドする。
	 */
	protected void buildIndividualMenu() {
	}

	/**
	 * [ファイル]メニュー作成
	 */
	protected void buildFileMenu() {
		fileMenu = makeJMenu("ファイル(F)", 'F');
		fileMenu.add(newBoardItem = makeCommandMenuItem("新規作成(N)...", 'N'));
		fileMenu.add(openItem = makeCommandMenuItem("開く(O)...", 'O'));
		fileMenu.add(closeAndOpenItem = makeCommandMenuItem("閉じて開く(L)...", 'L'));
		fileMenu.add(saveItem = makeCommandMenuItem("保存(S)...", 'S'));
		fileMenu.add(duplicateItem = makeCommandMenuItem("複製(D)", 'D'));
		fileMenu.add(rotationMenu = makeJMenu("回転・反転(R)", 'R'));
		buildRotationMenu();
		fileMenu.addSeparator();
		fileMenu.add(exportProblemDataStringItem = makeCommandMenuItem("問題データ文字列出力(E)...", 'E'));
		fileMenu.add(saveImageItem = makeCommandMenuItem("画像保存(I)...", 'I'));
		fileMenu.add(copyImageItem = makeCommandMenuItem("画像コピー(M)", 'M'));
		fileMenu.add(printItem = makeCommandMenuItem("印刷(P)...", 'P'));
		fileMenu.addSeparator();
		fileMenu.add(propertyItem = makeCommandMenuItem("プロパティ(T)", 'T'));
		fileMenu.addSeparator();
		fileMenu.add(closeItem = makeCommandMenuItem("閉じる(C)", 'C'));
		fileMenu.add(quitItem = makeCommandMenuItem("終了(Q)", 'Q'));
	}

	/**
	 * [編集]メニュー作成
	 */
	protected void buildEditMenu() {
		editMenu = makeJMenu("編集(E)", 'E');
		editMenu.add(answerModeItem = makeCommandRadioButtonMenuItem("解答モード(A)", 'A'));
		editMenu.add(problemEditModeItem = makeCommandRadioButtonMenuItem("問題入力モード(E)", 'E'));
		editMenu.addSeparator();
		editMenu.add(clearItem = makeCommandMenuItem("解答消去(C)", 'C'));
//		editMenu.add(trimAnswerItem = makeCommandMenuItem("補助記号消去(T)", 'T'));
//		editMenu.add(symmetricPlacementItem = makeCheckBoxCommandMenuItem("対称配置(S)", 'S'));
		editMenu.addSeparator();
		editMenu.add(undoItem = makeCommandMenuItem("元に戻す(U)", 'U'));
		editMenu.add(redoItem = makeCommandMenuItem("やり直し(R)", 'R'));
		// editMenu.add(undoAllItem = makeCommandMenuItem("最初まで戻す(F)", 'F'));
		// editMenu.add(redoAllItem = makeCommandMenuItem("最後まで進める(L)", 'L'));
		editMenu.add(palybackItem = makeCommandMenuItem("履歴再生(P)", 'P'));
		editMenu.addSeparator();
		editMenu.add(checkAnswerItem = makeCommandMenuItem("正解判定(H)", 'H'));
		editMenu.add(immediateAnswerCheckModeItem = makeCheckBoxCommandMenuItem("即時正解判定(M)", 'M', false));

		modeGroup = new ButtonGroup();
		modeGroup.add(answerModeItem);
		modeGroup.add(problemEditModeItem);
		editMenu.addMenuListener(new EditMenuListener());
	}

	/**
	 * [表示]メニュー作成
	 */
	protected void buildViewMenu() {
		viewMenu = makeJMenu("表示(V)", 'V');
		viewMenu.add(colorMenu = makeJMenu("色の設定(L)", 'L'));
		viewMenu.add(cellSizeItem = makeCommandMenuItem("表示サイズ(S)...", 'S'));
		viewMenu.add(showIndexModeItem = makeCheckBoxCommandMenuItem("行列番号表示(I)", 'I', true));
		viewMenu.add(gridStyleItem = makeCheckBoxCommandMenuItem("罫線表示(G)", 'G', true));
		viewMenu.addSeparator();
		viewMenu.addMenuListener(new ViewMenuListener());
	}

	/**
	 * [ヘルプ]メニュー作成
	 */
	protected void buildHelpMenu() {
		helpMenu = makeJMenu("ヘルプ(H)", 'H');
		helpMenu.add(aboutItem = makeCommandMenuItem(Constants.TITLE + "について(A)", 'A'));
	}

	/**
	 * [回転・反転]メニュー作成
	 */
	protected void buildRotationMenu() {
		makeRotationItem("左90°回転(1)", '1', "1");
		makeRotationItem("180°回転(2)", '2', "2");
		makeRotationItem("右90°回転(3)", '3', "3");
		makeRotationItem("縦横交換(4)", '4', "4");
		makeRotationItem("左右反転(5)", '5', "5");
		makeRotationItem("180°回転+縦横交換(6)", '6', "6");
		makeRotationItem("上下反転(7)", '7', "7");
	}
	/**
	 * [回転・反転]メニュー作成
	 * （カックロ用）
	 */
	protected void buildRotationMenu2() {
		makeRotationItem("縦横交換(4)", '4', "4");
	}

	/**
	 * [編集]メニューリスナークラス
	 */
	protected class EditMenuListener implements MenuListener {
		/*
		 * 編集メニューを開いたタイミングで， 必要に応じてアンドゥ，リドゥメニューを有効／無効にする
		 * 問題入力モードは'/'キーにより，メニューを用いずに変更できるので，メニューを開くときに現在の状態を調べて更新する。
		 */
		public void menuSelected(MenuEvent evt) {
			undoItem.setEnabled(command.canUndo());
			redoItem.setEnabled(command.canRedo());
			// undoAllItem.setEnabled(command.canUndo());
			// redoAllItem.setEnabled(command.canRedo());
			palybackItem.setEnabled(command.canUndo() || command.canRedo());

			boolean b = getPanelBase().isProblemEditMode();
			problemEditModeItem.setSelected(b);
			answerModeItem.setSelected(!b);
		}

		/*
		 * メニューが閉じたらすべて有効に戻す
		 */
		public void menuDeselected(MenuEvent evt) {
			undoItem.setEnabled(true);
			redoItem.setEnabled(true);
			// undoAllItem.setEnabled(true);
			// redoAllItem.setEnabled(true);
			palybackItem.setEnabled(true);
		}

		public void menuCanceled(MenuEvent evt) {
		}
	}

	/**
	 * ショートカットキーの一括設定
	 */
	private void setAccelerators() {
		newBoardItem.setAccelerator(
				KeyStroke.getKeyStroke('N',	InputEvent.CTRL_MASK));
		openItem.setAccelerator(
				KeyStroke.getKeyStroke('O', InputEvent.CTRL_MASK));
		closeAndOpenItem.setAccelerator(
				KeyStroke.getKeyStroke('O', InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		saveItem.setAccelerator(
				KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
		closeItem.setAccelerator(
				KeyStroke.getKeyStroke('W', InputEvent.CTRL_MASK));
		undoItem.setAccelerator(
				KeyStroke.getKeyStroke('Z', InputEvent.CTRL_MASK));
		redoItem.setAccelerator(
				KeyStroke.getKeyStroke('Y', InputEvent.CTRL_MASK));
		answerModeItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		problemEditModeItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.SHIFT_MASK));
		if (renewColorItem != null)
			renewColorItem.setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
	}

	/**
	 * [表示]メニューリスナークラス
	 */
	protected class ViewMenuListener implements MenuListener {

		public void menuSelected(MenuEvent evt) {
		}

		public void menuDeselected(MenuEvent evt) {
		}

		public void menuCanceled(MenuEvent evt) {
		}
	}

	/*
	 * 何種類かのパズルに共通なメニューの作成と追加を行う
	 */
	/**
	 * [補助記号消去]メニュー項目を[編集]メニューの上から?番目に追加する。
	 */
	protected void addTrimAnswerMenuItem() {
		trimAnswerItem = makeCommandMenuItem("補助記号消去(T)", 'T');
		editMenu.add(trimAnswerItem, 4);
	}
	/**
	 * [対称配置]メニュー項目を[編集]メニューの上から?番目に追加する。
	 */
	protected void addSymmetricPlacementMenuItem() {
		symmetricPlacementItem = makeCheckBoxCommandMenuItem("対称配置(S)", 'S', false);
		editMenu.insertSeparator(8);
		editMenu.add(symmetricPlacementItem, 9);
	}
	/**
	 * [カーソル]メニュー項目を[表示]メニューの上から5番目に追加する。
	 */
	protected void addCursorMenu() {
		cursorItem = makeJCheckBoxMenuItem("カーソル(C)", 'C', panel.isCursorOn());
		cursorItem.addActionListener(commandAction);
		viewMenu.add(cursorItem, 4);
	}
	/**
	 * [色の更新]メニュー項目を[表示]メニューに追加する。
	 */
	protected void addRenewColorMenu() {
		renewColorItem = makeCommandMenuItem("色の更新(U)", 'U');
		viewMenu.add(renewColorItem);
	}

	/**
	 * 指定したメニュー項目を[表示]メニューに追加する。
	 * @param item 追加するメニュー項目
	 */
	protected void addToViewMenu(JMenuItem item) {
		viewMenu.add(item);
	}

	/**
	 * 「回転・反転」メニューのサブメニュー項目を作成し，グループに追加する。
	 * 回転番号をパラメータとしてメニュー項目の action command に設定する。
	 * @param text メニュー表示文字列
	 * @param mnemonic
	 * @param n 設定する回転番号
	 * @return 作成したメニュー項目
	 */
	protected JMenuItem makeRotationItem(String text, char mnemonic, String n) {
		JMenuItem rotationItem = new JMenuItem(text, mnemonic);
		rotationItem.addActionListener(rotationCommandAction);
		rotationItem.setActionCommand(n);
		rotationMenu.add(rotationItem);
		return rotationItem;
	}

	/**
	 * 回転コマンド。 
	 * メニュー項目の action command から回転番号を読み取って実行する。
	 */
	private ActionListener rotationCommandAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem target = (JMenuItem) e.getSource();
			command.rotateBoard(Integer.parseInt(target.getActionCommand()));
			panel.repaint();
		}
	};

	/**
	 * 色選択メニュー作成用補助メソッド
	 * メニュー項目を作成して，[色の選択]メニューに追加する。
	 * @param text メニュー表示文字列
	 * @return 作成した色選択用メニュー項目
	 */
	protected JMenuItem addColorMenuItem(final String text) {
		JMenuItem item = new JMenuItem(text);
		item.addActionListener(changeColorAction);
		colorMenu.add(item);
		return item;
	}

	/**
	 * 色設定コマンド。 
	 */
	private ActionListener changeColorAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			// どのメニュー項目が選択されたかによって，どの色を変更するかをその場で決定する。
			Color color = JColorChooser.showDialog(panel, e.getActionCommand(),
					getColor((JMenuItem) e.getSource()));
			if (color != null)
				setColor((JMenuItem) e.getSource(), color);
			panel.repaint();
		}
	};

	/**
	 * メニュー項目に対応した色を取得する 中身は各サブクラスごとに実装する。
	 * @param name 選択されたメニュー
	 * @return 取得した色
	 */
	public Color getColor(JMenuItem name) {
		return null;
	}

	/**
	 * メニュー項目に対応した色を取得する 中身は各サブクラスごとに実装する。
	 * @param name 選択されたメニュー
	 * @param color 設定する色
	 */
	public void setColor(JMenuItem name, Color color) {
	}

	/**
	 * メニューから実行されるコマンド。 
	 */
	private ActionListener commandAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			executeCommand((JMenuItem) e.getSource());
		}
	};

	/**
	 * どのメニューが選択されたか調べて，対応するコマンドを起動する。
	 * 実行後，Panelを再描画する。
	 * @param target 選択されたメニュー項目
	 */
	public void executeCommand(JMenuItem target) {
		if (target == newBoardItem)
			command.newBoard();
		else if (target == openItem)
			command.open();
		else if (target == closeAndOpenItem)
			command.closeAndOpen();
		else if (target == saveItem)
			command.save();
		else if (target == duplicateItem)
			command.duplicate();
		else if (target == exportProblemDataStringItem)
			command.exporProblemDatatString();
		else if (target == saveImageItem)
			command.saveImage();
		else if (target == copyImageItem)
			command.copyImage();
		else if (target == printItem)
			command.print();
		else if (target == propertyItem)
			command.property();
		else if (target == closeItem)
			command.close();
		else if (target == quitItem)
			command.quit();
		else if (target == aboutItem)
			command.about();
		else if (target == clearItem)
			command.clear();
		else if (target == trimAnswerItem)
			command.trimAnswer();
		else if (target == symmetricPlacementItem)
			command.setSymmetricPlacementMode(target.isSelected());
		else if (target == undoItem)
			command.undo();
		else if (target == redoItem)
			command.redo();
//		else if (target == undoAllItem)
//			command.undoAll();
//		else if (target == redoAllItem)
//			command.redoAll();
		else if (target == palybackItem)
			command.playback();
		else if (target == checkAnswerItem)
			command.checkAnswer();
		else if (target == immediateAnswerCheckModeItem)
			command.setCheckAnswerImmediatelyMode(target.isSelected());
		else if (target == renewColorItem)
			command.renewColor();
		else if (target == cursorItem)
			command.setCursorOn(target.isSelected());
		else if (target == answerModeItem)
			command.setProblemEditMode(false);
		else if (target == problemEditModeItem)
			command.setProblemEditMode(true);
		else if (target == showIndexModeItem)
			command.setShowIndexMode(target.isSelected());
		else if (target == gridStyleItem)
			command.setGridStyle(target.isSelected());
		else if (target == cellSizeItem)
			command.cellSize();
		else
			executeCommand2(target);
		panel.repaint();
	}
	/**
	 * どのメニューが選択されたか調べて，対応するコマンドを起動する。
	 * 各サブクラスでオーバーライドする。
	 * @param target 選択されたメニュー項目
	 */
	public void executeCommand2(JMenuItem target) {
	}

	/**
	 * CommandActionを備えたメニュー項目を作成する
	 * @param title メニュー項目表示テキスト
	 * @param mnumonic ショートカット
	 * @return 作成したメニュー項目
	 */
	protected JMenuItem makeCommandMenuItem(String title, char mnumonic) {
		JMenuItem item = makeJMenuItem(title, mnumonic);
		item.addActionListener(commandAction);
		return item;
	}

	/**
	 * CommandActionを備えたラジオボタンメニュー項目を作成する
	 * @param title メニュー項目表示テキスト
	 * @param mnumonic ショートカット
	 * @return 作成したメニュー項目
	 */
	protected JMenuItem makeCommandRadioButtonMenuItem(String title,
			char mnumonic) {
		JMenuItem item = makeJRadioButtonMenuItem(title, mnumonic);
		item.addActionListener(commandAction);
		return item;
	}

	/**
	 * CommandActionを備えたチェックボックスメニュー項目を作成する
	 * @param title メニュー項目表示テキスト
	 * @param mnumonic ショートカット
	 * @param initial 初期選択
	 * @return 作成したメニュー項目
	 */
	protected JCheckBoxMenuItem makeCheckBoxCommandMenuItem(String title,
			char mnumonic, boolean initial) {
		JCheckBoxMenuItem item = makeJCheckBoxMenuItem(title, mnumonic, initial);
		item.addActionListener(commandAction);
		return item;
	}

	/**
	 * 現在の設定に合わせてメニュー項目の選択状態を更新する。
	 */
	public void updateCurrentMenuSelection() {
		showIndexModeItem.setSelected(getPanelBase().isShowIndexMode());
		gridStyleItem.setSelected(getPanelBase().getGridStyle() > 0);
		if (cursorItem != null)
			cursorItem.setSelected(getPanelBase().isCursorOn());
		if (symmetricPlacementItem != null)
			symmetricPlacementItem.setSelected(getPanelEventHandlerBase().isSymmetricPlacementMode());
		if (immediateAnswerCheckModeItem != null)
			immediateAnswerCheckModeItem.setSelected(getPanelEventHandlerBase().isImmediateAnswerCheckMode());
		problemEditModeItem.setSelected(getPanelEventHandlerBase().isProblemEditMode());
	}

	/*
	 * 以下，メニュー作成用補助メソッド
	 */
	/**
	 * JMenu 作成用メソッド
	 * @param text メニューの文字列
	 * @param mnemonic
	 * @return 作成したJMenuItem
	 */
	public JMenu makeJMenu(String text, char mnemonic) {
		JMenu menu = new JMenu(text);
		menu.setMnemonic(mnemonic);
		return menu;
	}

	/**
	 * JMenuItem 作成用メソッド
	 * @param text メニューの文字列
	 * @param mnemonic
	 * @return 作成したJMenuItem
	 */
	public JMenuItem makeJMenuItem(String text, char mnemonic) {
		return new JMenuItem(text, mnemonic);
	}

	/**
	 * JRadioButtonMenuItem 作成用メソッド
	 * @param text メニューの文字列
	 * @param mnemonic
	 * @return 作成したメニュー項目
	 */
	public JMenuItem makeJRadioButtonMenuItem(String text, char mnemonic) {
		JMenuItem item = new JRadioButtonMenuItem(text);
		item.setMnemonic(mnemonic);
		return item;
	}

	/**
	 * JCheckBoxMenuItem 作成用メソッド
	 * @param text メニューの文字列
	 * @param mnemonic ショートカットキー
	 * @param initial 初期値
	 * @return 作成したJMenuItem
	 */
	public JCheckBoxMenuItem makeJCheckBoxMenuItem(String text, char mnemonic,
			boolean initial) {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(text);
		item.setMnemonic(mnemonic);
		item.setSelected(initial);
		return item;
	}

}
