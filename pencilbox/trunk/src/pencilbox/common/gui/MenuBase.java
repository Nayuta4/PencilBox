package pencilbox.common.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JMenuItem coloseAndOpenItem;
	private JMenuItem saveItem;
	private JMenuItem exportProblemDataStringItem;
	private JMenuItem saveImageItem;
	private JMenuItem copyImageItem;
	private JMenuItem printItem;
	private JMenuItem propertyItem;
	private JMenuItem closeItem;
	private JMenuItem quiteItem;
	private JMenuItem aboutItem;
	private JMenuItem clearItem;
	private JMenuItem undoItem;
	private JMenuItem redoItem;
	private JMenuItem undoAllItem;
	private JMenuItem redoAllItem;
	private JMenuItem palybackItem;
	private JMenuItem checkAnswerItem;
	private JMenuItem renewColorItem;
	private JMenuItem cursorItem;
	private ButtonGroup modeGroup;
	private JMenuItem problemEditModeItem;
	private JMenuItem answerModeItem;

	private JMenu colorMenu;
	private JMenu rotationMenu;
	private ButtonGroup rotationGroup;
	private JMenu displaySizeMenu;
	private ButtonGroup displaySizeGroup;
	private JMenu gridStyleMenu;
	private ButtonGroup gridStyleGroup;
	private JMenuItem showIndexModeItem;

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

	/**
	 * 共通メニューを作成する。
	 */
	protected void buildCommonMenu() {
		buildFileMenu();
		buiildEditMenu();
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
		fileMenu.add(coloseAndOpenItem = makeCommandMenuItem("閉じて開く(L)...", 'L'));
		fileMenu.add(saveItem = makeCommandMenuItem("保存(S)...", 'S'));
		fileMenu.addSeparator();
		fileMenu.add(exportProblemDataStringItem = makeCommandMenuItem("問題データ文字列出力(E)...", 'E'));
		fileMenu.add(saveImageItem = makeCommandMenuItem("画像保存(I)...", 'I'));
		fileMenu.add(copyImageItem = makeCommandMenuItem("画像コピー(M)", 'M'));
		fileMenu.add(printItem = makeCommandMenuItem("印刷(P)...", 'P'));
		fileMenu.addSeparator();
		fileMenu.add(propertyItem = makeCommandMenuItem("プロパティ(T)", 'T'));
		fileMenu.addSeparator();
		fileMenu.add(closeItem = makeCommandMenuItem("閉じる(C)", 'C'));
		fileMenu.add(quiteItem = makeCommandMenuItem("終了(Q)", 'Q'));
	}

	/**
	 * [編集]メニュー作成
	 */
	protected void buiildEditMenu() {
		editMenu = makeJMenu("編集(E)", 'E');
		editMenu.add(answerModeItem = makeCommandRadioButtonMenuItem("解答モード(A)", 'A'));
		editMenu.add(problemEditModeItem = makeCommandRadioButtonMenuItem("問題入力モード(E)", 'E'));
		editMenu.addSeparator();
		editMenu.add(clearItem = makeCommandMenuItem("解答消去(C)", 'C'));
		editMenu.addSeparator();
		editMenu.add(undoItem = makeCommandMenuItem("元に戻す(U)", 'U'));
		editMenu.add(redoItem = makeCommandMenuItem("やり直し(R)", 'R'));
		// editMenu.add(undoAllItem = makeCommandMenuItem("最初まで戻す(F)", 'F'));
		// editMenu.add(redoAllItem = makeCommandMenuItem("最後まで進める(L)", 'L'));
		editMenu.add(palybackItem = makeCommandMenuItem("履歴再生(P)", 'P'));
		editMenu.addSeparator();
		editMenu.add(checkAnswerItem = makeCommandMenuItem("正答判定(H)", 'H'));
		editMenu.addSeparator();

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
		buildRotationMenu();
		buildDisplaySizeMenu();
		buildGridStyleMenu();
		viewMenu.add(colorMenu = makeJMenu("色の設定(L)", 'L'));
		viewMenu.add(rotationMenu);
		viewMenu.add(displaySizeMenu);
		viewMenu.add(showIndexModeItem = makeCheckBoxCommandMenuItem("行列番号表示(I)", 'I', true));
		viewMenu.addSeparator();
		viewMenu.addMenuListener(new ViewMenuListener());
	}

	/**
	 * [ヘルプ]メニュー作成
	 */
	protected void buildHelpMenu() {
		helpMenu = makeJMenu("ヘルプ(H)", 'H');
		helpMenu.add(aboutItem = makeCommandMenuItem("PencilBoxについて(A)", 'A'));
	}

	/**
	 * [回転・反転]メニュー作成
	 */
	protected void buildRotationMenu() {
		rotationMenu = makeJMenu("回転・反転(R)", 'R');
		rotationGroup = new ButtonGroup();
		makeRotationItem(0, "0°").setSelected(true);
		makeRotationItem(1, "左90°回転");
		makeRotationItem(2, "180°回転");
		makeRotationItem(3, "右90°回転");
		makeRotationItem(4, "縦横交換");
		makeRotationItem(5, "左右反転");
		makeRotationItem(6, "180°回転+縦横交換");
		makeRotationItem(7, "上下反転");
	}
	/**
	 * [回転・反転]メニュー作成
	 * （カックロ用暫定）
	 */
	protected void buildRotationMenu2() {
		rotationMenu = makeJMenu("回転・反転(R)", 'R');
		rotationGroup = new ButtonGroup();
		makeRotationItem(0, "0°").setSelected(true);
//		makeRotationItem(1, "左90°回転");
//		makeRotationItem(2, "180°回転");
//		makeRotationItem(3, "右90°回転");
		makeRotationItem(4, "縦横交換");
//		makeRotationItem(5, "左右反転");
//		makeRotationItem(6, "180°回転+縦横交換");
//		makeRotationItem(7, "上下反転");
	}

	/**
	 * [表示サイズ]メニュー作成
	 */
	void buildDisplaySizeMenu() {
		displaySizeMenu = makeJMenu("表示サイズ(S)", 'S');
		displaySizeGroup = new ButtonGroup();
		makeDisplaySizeItem(32, "大");
		makeDisplaySizeItem(26, "中").setSelected(true);
		makeDisplaySizeItem(20, "小");
		makeDisplaySizeItem(14, "極小");
	}

	/**
	 * 罫線表示スタイルメニューを作成する。
	 * 具体的内容は，必要なときのみサブクラスでオーバーライドして作成する
	 */
	protected void buildGridStyleMenu() {
		gridStyleMenu = makeJMenu("罫線表示(G)", 'G');
		gridStyleGroup = new ButtonGroup();
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
				KeyStroke.getKeyStroke('N',	KeyEvent.CTRL_MASK));
		openItem.setAccelerator(
				KeyStroke.getKeyStroke('O', KeyEvent.CTRL_MASK));
		coloseAndOpenItem.setAccelerator(
				KeyStroke.getKeyStroke('O',KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
		saveItem.setAccelerator(
				KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK));
		closeItem.setAccelerator(
				KeyStroke.getKeyStroke('W', KeyEvent.CTRL_MASK));
		undoItem.setAccelerator(
				KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_MASK));
		redoItem.setAccelerator(
				KeyStroke.getKeyStroke('Y', KeyEvent.CTRL_MASK));
		answerModeItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		problemEditModeItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F2, KeyEvent.SHIFT_MASK));
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
	 * [罫線表示]メニューを[表示]メニューの上から4番目に追加する。
	 */
	protected void addGridStyleMenu() {
		viewMenu.add(gridStyleMenu, 3);
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
	 * @param n 設定する回転状態番号
	 * @param text メニュー表示文字列
	 * @return 作成したメニュー項目
	 */
	protected JRadioButtonMenuItem makeRotationItem(final int n, String text) {
		JRadioButtonMenuItem rotationItem = new JRadioButtonMenuItem(text);
		rotationItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				panel.setRotation(n);
//				frame.pack();
			}
		});
		rotationGroup.add(rotationItem);
		rotationMenu.add(rotationItem);
		return rotationItem;
	}

	/**
	 * 「表示サイズ変更」のサブメニュー項目を作成し，グループに追加する。
	 * @param n 設定するセルサイズ
	 * @param text メニュー表示文字列
	 * @return 作成したメニュー項目
	 */
	protected JRadioButtonMenuItem makeDisplaySizeItem(final int n, String text) {
		JRadioButtonMenuItem displaySizeItem = new JRadioButtonMenuItem(text);
		displaySizeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setDisplaySize(n);
				frame.pack();
			}
		});
		displaySizeGroup.add(displaySizeItem);
		displaySizeMenu.add(displaySizeItem);
		return displaySizeItem;
	}

	/**
	 * 「罫線スタイル選択」のサブメニューを作成し，グループに追加する。
	 * @param n スタイル番号
	 * @param text メニュー表示文字列
	 * @return 作成したメニュー項目
	 */
	protected JRadioButtonMenuItem makeDisplayStyleItem(final int n, String text) {
		JRadioButtonMenuItem gridStyleItem = new JRadioButtonMenuItem(text);
		gridStyleItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setDisplayStyle(n);
				panel.repaint();
			}
		});
		gridStyleGroup.add(gridStyleItem);
		gridStyleMenu.add(gridStyleItem);
		return gridStyleItem;
	}

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
		else if (target == coloseAndOpenItem)
			command.closeAndOpen();
		else if (target == saveItem)
			command.save();
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
		else if (target == quiteItem)
			command.quit();
		else if (target == aboutItem)
			command.about();
		else if (target == clearItem)
			command.clear();
		else if (target == undoItem)
			command.undo();
		else if (target == redoItem)
			command.redo();
		else if (target == undoAllItem)
			command.undoAll();
		else if (target == redoAllItem)
			command.redoAll();
		else if (target == palybackItem)
			command.playback();
		else if (target == checkAnswerItem)
			command.checkAnswer();
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
