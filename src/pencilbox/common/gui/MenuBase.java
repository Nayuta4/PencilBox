package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import pencilbox.common.io.IOController.DataFormat;
import pencilbox.resource.Messages;

/**
 * 共通メニュークラス
 */
public class MenuBase {

	private JMenu fileMenu;
	protected JMenu editMenu;
	private JMenu viewMenu;
	private JMenu helpMenu;

	private JMenuItem newBoardItem;
	private JMenuItem openItem;
	private JMenuItem closeAndOpenItem;
	private JMenuItem saveItem;
	private JMenuItem duplicateItem;
	private JMenuItem rotationItem;
	private JMenuItem changeBoardSizeItem;
	private JMenuItem saveImageItem;
	private JMenuItem copyImageItem;
	private JMenuItem printPreviewItem;
	private JMenuItem printItem;
	private JMenuItem propertyItem;
	private JMenuItem closeItem;
	private JMenuItem quitItem;
	private JMenuItem aboutItem;
	private JMenuItem clearItem;
	private JMenuItem trimAnswerItem;
	private JMenuItem clearQuestionItem;
	private JMenuItem reconstructQuestionItem;
	protected JMenuItem exchangeNumbersItem;
	private JMenuItem symmetricPlacementItem;
	private JMenuItem undoItem;
	private JMenuItem redoItem;
//	private JMenuItem undoAllItem;
//	private JMenuItem redoAllItem;
	private JMenuItem playbackItem;
	private JMenuItem historyItem;
	private JMenuItem checkAnswerItem;
	private JMenuItem immediateAnswerCheckItem;
	private JMenuItem cellSizeItem;
	private JMenuItem linkWidthItem;
	private JMenuItem indexItem;
	private JMenuItem gridStyleItem;
	private JMenuItem renewColorItem;
	private JMenuItem cursorItem;
	private ButtonGroup modeGroup;
	private JMenuItem problemEditModeItem;
	private JMenuItem answerModeItem;
	private JMenuItem regionEditModeItem;
	private JMenuItem loadPreferencesItem;
	private JMenuItem storePreferencesItem;

	private JMenu colorMenu;
	private JMenu markStyleMenu;
	private JMenu exportDataMenu;
	private ButtonGroup markStyleGroup;

	private JMenuItem backgroundColorItem;
	private JMenuItem gridColorItem;

	protected JMenuItem illuminatedCellColorItem;
	protected JMenuItem numberColorItem;
	protected JMenuItem inputColorItem;
	protected JMenuItem wallColorItem;
	protected JMenuItem areaBorderColorItem;
	protected JMenuItem areaPaintColorItem;
	protected JMenuItem whiteAreaColorItem;
	protected JMenuItem blackAreaColorItem;
	protected JMenuItem paintColorItem;
	protected JMenuItem circleColorItem;
	protected JMenuItem lineColorItem;
	protected JMenuItem crossColorItem;
	protected JMenuItem bulbColorItem;
	protected JMenuItem noBulbColorItem;
	protected JMenuItem gateColorItem;
	protected JMenuItem borderColorItem;

	protected JMenuItem highlightSelectionItem;
	protected JMenuItem indicateErrorItem;
	protected JMenuItem countAreaSizeItem;
	protected JMenuItem dotHintItem;
	protected JMenuItem hideSoleNumberItem;
	protected JMenuItem hideStarItem;
	protected JMenuItem showAreaBorderItem;
	protected JMenuItem showBeamItem;
	protected JMenuItem paintIlluminatedCellItem;
	protected JMenuItem separateAreaColorItem;
	protected JMenuItem separateLinkColorItem;
	protected JMenuItem separateTetrominoColorItem;

	protected JMenuItem selectLetterItem;

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
		setAccelerators();
	}

	/**
	 * @return Returns the panel.
	 */
	protected PanelBase getPanelBase() {
		return panel;
	}

	protected PanelBase getPanel() {
		return panel;
	}

	protected EventHandlerManager getEventHandlerManager() {
		return command.getEventHandlerManager();
	}

	/**
	 * 共通メニューを作成する。
	 */
	protected void buildCommonMenu() {
		buildFileMenu();
		buildEditMenu();
		buildViewMenu();
		buildHelpMenu();

		buildColorMenuIcon();

		JMenuBar jMenuBar = frame.getJMenuBar();
		jMenuBar.add(fileMenu);
		jMenuBar.add(editMenu);
		jMenuBar.add(viewMenu);
		jMenuBar.add(helpMenu);
	}

	/**
	 * [ファイル]メニュー作成
	 */
	protected void buildFileMenu() {
		fileMenu = makeJMenu(Messages.getString("MenuBase.fileMenu"), 'F'); //$NON-NLS-1$
		fileMenu.add(newBoardItem = makeCommandMenuItem(Messages.getString("MenuBase.newBoardItem"), 'N')); //$NON-NLS-1$
		fileMenu.add(openItem = makeCommandMenuItem(Messages.getString("MenuBase.openItem"), 'O')); //$NON-NLS-1$
		fileMenu.add(closeAndOpenItem = makeCommandMenuItem(Messages.getString("MenuBase.closeAndOpenItem"), 'L')); //$NON-NLS-1$
		fileMenu.add(saveItem = makeCommandMenuItem(Messages.getString("MenuBase.saveItem"), 'S')); //$NON-NLS-1$
		fileMenu.addSeparator();
		fileMenu.add(duplicateItem = makeCommandMenuItem(Messages.getString("MenuBase.duplicateItem"), 'D')); //$NON-NLS-1$
		fileMenu.add(rotationItem = makeCommandMenuItem(Messages.getString("MenuBase.rotationItem"), 'R')); //$NON-NLS-1$
		fileMenu.add(changeBoardSizeItem = makeCommandMenuItem(Messages.getString("MenuBase.changeBoardSizeItem"), 'Z')); //$NON-NLS-1$
		fileMenu.addSeparator();
		fileMenu.add(exportDataMenu = makeJMenu(Messages.getString("MenuBase.exportMenu"), 'X')); //$NON-NLS-1$
		buildExportDataMenu();
		fileMenu.add(saveImageItem = makeCommandMenuItem(Messages.getString("MenuBase.saveImageItem"), 'G')); //$NON-NLS-1$
		fileMenu.add(copyImageItem = makeCommandMenuItem(Messages.getString("MenuBase.copyImageItem"), 'M')); //$NON-NLS-1$
		fileMenu.add(printPreviewItem = makeCommandMenuItem(Messages.getString("MenuBase.printPreviewItem"), 'V')); //$NON-NLS-1$
		fileMenu.add(printItem = makeCommandMenuItem(Messages.getString("MenuBase.printItem"), 'P')); //$NON-NLS-1$
		fileMenu.addSeparator();
		fileMenu.add(loadPreferencesItem = makeCommandMenuItem(Messages.getString("MenuBase.loadPreferencesItem"), 'F')); //$NON-NLS-1$
		fileMenu.add(storePreferencesItem = makeCommandMenuItem(Messages.getString("MenuBase.storePreferencesItem"),'T')); //$NON-NLS-1$
		fileMenu.addSeparator();
		fileMenu.add(propertyItem = makeCommandMenuItem(Messages.getString("MenuBase.propertyItem"), 'I')); //$NON-NLS-1$
		fileMenu.addSeparator();
		fileMenu.add(closeItem = makeCommandMenuItem(Messages.getString("MenuBase.closeItem"), 'C')); //$NON-NLS-1$
		fileMenu.add(quitItem = makeCommandMenuItem(Messages.getString("MenuBase.quitItem"), 'Q')); //$NON-NLS-1$
	}

	/**
	 * [編集]メニュー作成
	 */
	protected void buildEditMenu() {
		editMenu = makeJMenu(Messages.getString("MenuBase.editMenu"), 'E'); //$NON-NLS-1$
		editMenu.add(answerModeItem = makeCommandRadioButtonMenuItem(Messages.getString("MenuBase.answerModeItem"), 'A')); //$NON-NLS-1$
		editMenu.add(problemEditModeItem = makeCommandRadioButtonMenuItem(Messages.getString("MenuBase.problemEditModeItem"), 'E')); //$NON-NLS-1$
		editMenu.add(regionEditModeItem = makeCommandRadioButtonMenuItem(Messages.getString("MenuBase.regionEditModeItem"), 'G')); //$NON-NLS-1$
		editMenu.addSeparator();
		editMenu.add(clearItem = makeCommandMenuItem(Messages.getString("MenuBase.clearItem"), 'C')); //$NON-NLS-1$
		if (trimAnswerItem != null) 
			editMenu.add(trimAnswerItem);  // ("補助記号消去(T)", 'T')
		if (clearQuestionItem != null)
			editMenu.add(clearQuestionItem); // "問題復元(X)", 'X')
		if (reconstructQuestionItem != null)
			editMenu.add(reconstructQuestionItem); // "問題復元(X)", 'X')
		if (exchangeNumbersItem != null) 
			editMenu.add(exchangeNumbersItem);  // "数字交換(X)", 'X')
		if (symmetricPlacementItem != null) {
			editMenu.addSeparator();
			editMenu.add(symmetricPlacementItem); // ("対称配置(S)", 'S')
		}
		editMenu.addSeparator();
		editMenu.add(undoItem = makeCommandMenuItem(Messages.getString("MenuBase.undoItem"), 'U')); //$NON-NLS-1$
		editMenu.add(redoItem = makeCommandMenuItem(Messages.getString("MenuBase.redoItem"), 'R')); //$NON-NLS-1$
		// editMenu.add(undoAllItem = makeCommandMenuItem("最初まで戻す(F)", 'F'));
		// editMenu.add(redoAllItem = makeCommandMenuItem("最後まで進める(L)", 'L'));
		editMenu.add(playbackItem = makeCommandMenuItem(Messages.getString("MenuBase.playbackItem"), 'P')); //$NON-NLS-1$
		editMenu.add(historyItem = makeCommandMenuItem(Messages.getString("MenuBase.historyItem"), 'H')); //$NON-NLS-1$
		editMenu.addSeparator();
		editMenu.add(checkAnswerItem = makeCommandMenuItem(Messages.getString("MenuBase.checkAnswerItem"), 'K')); //$NON-NLS-1$
		editMenu.add(immediateAnswerCheckItem = makeCheckBoxCommandMenuItem(Messages.getString("MenuBase.immediateAnswerCheckItem"), 'M', false)); //$NON-NLS-1$

		modeGroup = new ButtonGroup();
		modeGroup.add(answerModeItem);
		modeGroup.add(problemEditModeItem);
		modeGroup.add(regionEditModeItem);
		editMenu.addMenuListener(new EditMenuListener());
	}

	/**
	 * [表示]メニュー作成
	 */
	protected void buildViewMenu() {
		viewMenu = makeJMenu(Messages.getString("MenuBase.viewMenu"), 'V'); //$NON-NLS-1$
		viewMenu.add(colorMenu = makeJMenu(Messages.getString("MenuBase.colorMenu"), 'L')); //$NON-NLS-1$
		backgroundColorItem = addColorMenuItem(Messages.getString("MenuBase.backgroundColorItem")); //$NON-NLS-1$
		gridColorItem = addColorMenuItem(Messages.getString("MenuBase.gridColorItem")); //$NON-NLS-1$
		viewMenu.add(cellSizeItem = makeCommandMenuItem(Messages.getString("MenuBase.cellSizeItem"), 'S')); //$NON-NLS-1$
		viewMenu.add(indexItem = makeCommandMenuItem(Messages.getString("MenuBase.indexItem"), 'I')); //$NON-NLS-1$
		viewMenu.add(gridStyleItem = makeCheckBoxCommandMenuItem(Messages.getString("MenuBase.gridStyleItem"), 'G', true)); //$NON-NLS-1$
		if (linkWidthItem != null)
			viewMenu.add(linkWidthItem);
		if (markStyleMenu != null)
			viewMenu.add(markStyleMenu);
		if (cursorItem != null)
			viewMenu.add(cursorItem);  // ("カーソル(C)", 'C')
		viewMenu.addSeparator();
		viewMenu.addMenuListener(new ViewMenuListener());
	}

	/**
	 * カラーメニューのアイコンを設定します
	 */
	protected void buildColorMenuIcon() {
		int count = colorMenu.getMenuComponentCount();
		for (int i = 0; i < count; ++i) {
			Component component = colorMenu.getMenuComponent(i);
			if (component instanceof JMenuItem) {
				JMenuItem item = (JMenuItem) component;
				Color color = getColor(item);
				if (color != null) {
					setColorIcon(item, color);
				}
			}
		}
	}

	/**
	 * メニューにカラーアイコンを設定します
	 * 
	 * @param item メニュー
	 * @param color カラー
	 */
	protected void setColorIcon(JMenuItem item, Color color) {

		Icon icon = item.getIcon();

		if (icon != null && icon instanceof ColorIcon && ((ColorIcon)icon).getColor().equals(color)) {
			return;
		}

		item.setIcon(new ColorIcon(color, Color.BLACK, item.getFont().getSize(), item.getFont().getSize()));
	}

	/**
	 * [ヘルプ]メニュー作成
	 */
	protected void buildHelpMenu() {
		helpMenu = makeJMenu(Messages.getString("MenuBase.helpMenu"), 'H'); //$NON-NLS-1$
		helpMenu.add(aboutItem = makeCommandMenuItem(Messages.getString("MenuBase.aboutDialog"), 'A')); //$NON-NLS-1$
	}

	/**
	 * 「エクスポート／インポート」サブメニューを作成する。
	 * 選択可能なフォーマットが標準と異なる場合などは必要に応じてサブクラスで上書きすること。
	 */
	protected void buildExportDataMenu() {
		makeDataExportItem(Messages.getString("MenuBase.exportItemKanpen"), 'K', DataFormat.KANPEN); //$NON-NLS-1$
		makeDataExportItem(Messages.getString("MenuBase.exportItemPzprv3"), 'Z', DataFormat.PZPRV3); //$NON-NLS-1$
	}

	/**
	 * 白マス確定記号スタイルメニューを作成する。
	 */
	protected void buildMarkStyleMenu(String text, char mnemonic, int[] styles) {
		markStyleMenu = makeJMenu(text, mnemonic);
		markStyleGroup = new ButtonGroup();
		for (int i = 1; i <= styles.length; i++) {
			switch (styles[i-1]) {
			case 0:
				makeMarkStyleItem(i + " " + Messages.getString("MenuBase.markStyle0"), (char)('0'+i), 0); //$NON-NLS-1$
				break;
			case 1:
				makeMarkStyleItem(i + " " + Messages.getString("MenuBase.markStyle1"), (char)('0'+i), 1); //$NON-NLS-1$
				break;
			case 2:
				makeMarkStyleItem(i + " " + Messages.getString("MenuBase.markStyle2"), (char)('0'+i), 2); //$NON-NLS-1$
				break;
			case 3:
				makeMarkStyleItem(i +  " " + Messages.getString("MenuBase.markStyle3"), (char)('0'+i), 3); //$NON-NLS-1$
				break;
			case 4:
				makeMarkStyleItem(i +  " " + Messages.getString("MenuBase.markStyle4"), (char)('0'+i), 4); //$NON-NLS-1$
				break;
			case 5:
				makeMarkStyleItem(i +  " " + Messages.getString("MenuBase.markStyle5"), (char)('0'+i), 5); //$NON-NLS-1$
				break;
			}
		}
	}

	protected void addNoPaintMarkStyleMenu() {
		buildMarkStyleMenu(Messages.getString("MenuBase.markStyleMenu"), 'N', new int[] {1, 2, 3, 4, 5}); //$NON-NLS-1$
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
			playbackItem.setEnabled(command.canUndo() || command.canRedo());

			int m = getPanelBase().getEditMode();
			if (m == PanelBase.ANSWER_INPUT_MODE) {
				if (symmetricPlacementItem != null)
					symmetricPlacementItem.setEnabled(false);
				if (clearQuestionItem != null)
					clearQuestionItem.setEnabled(false);
				if (reconstructQuestionItem != null)
					reconstructQuestionItem.setEnabled(false);
			}
			if (m == PanelBase.ANSWER_INPUT_MODE) {
				answerModeItem.setSelected(true);
			} else if (m == PanelBase.PROBLEM_INPUT_MODE) {
				problemEditModeItem.setSelected(true);
			} else if (m == PanelBase.REGION_EDIT_MODE) {
				regionEditModeItem.setSelected(true);
			}
		}

		/*
		 * メニューが閉じたらすべて有効に戻す
		 */
		public void menuDeselected(MenuEvent evt) {
			undoItem.setEnabled(true);
			redoItem.setEnabled(true);
			// undoAllItem.setEnabled(true);
			// redoAllItem.setEnabled(true);
			playbackItem.setEnabled(true);
			if (symmetricPlacementItem != null)
				symmetricPlacementItem.setEnabled(true);
			if (clearQuestionItem != null)
				clearQuestionItem.setEnabled(true);
			if (reconstructQuestionItem != null)
				reconstructQuestionItem.setEnabled(true);
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
		historyItem.setAccelerator(
				KeyStroke.getKeyStroke('H', InputEvent.CTRL_MASK));
		checkAnswerItem.setAccelerator(
				KeyStroke.getKeyStroke('K', InputEvent.CTRL_MASK));
		answerModeItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		problemEditModeItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		regionEditModeItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
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
	 * 何種類かのパズルに不適用のメニューの削除を行う。
	 */
	/**
	 * [盤面サイズ変更]メニュー項目を削除する。
	 */
	protected void removeChangeBoardSizeMenuItem() {
		fileMenu.remove(changeBoardSizeItem);
	}
	/*
	 * 何種類かのパズルに共通なメニューの作成のみを行う。
	 * 作成した項目はメニュー作成時に追加される。
	 */
	/**
	 * [補助記号消去]メニュー項目を作成する。
	 */
	protected void addTrimAnswerMenuItem() {
		trimAnswerItem = makeCommandMenuItem(Messages.getString("MenuBase.trimAnswerItem"), 'T'); //$NON-NLS-1$
	}
	/**
	 * [問題数字消去]メニュー項目を作成する。
	 */
	protected void addClearQuestionMenuItem() {
		clearQuestionItem = makeCommandMenuItem(Messages.getString("MenuBase.clearQuestionItem"), 'L'); //$NON-NLS-1$
	}
	/**
	 * [問題数字復元]メニュー項目を作成する。
	 */
	protected void addReconstructQuestionMenuItem() {
		reconstructQuestionItem = makeCommandMenuItem(Messages.getString("MenuBase.reconstructQuestionItem"), 'T'); //$NON-NLS-1$
	}
	/**
	 * [対称配置]メニュー項目を作成する。
	 */
	protected void addSymmetricPlacementMenuItem() {
		symmetricPlacementItem = makeCheckBoxCommandMenuItem(Messages.getString("MenuBase.symmetricPlacementItem"), 'S', false); //$NON-NLS-1$
	}
	/**
	 * [カーソル]メニュー項目を作成する。
	 */
	protected void addCursorMenuItem() {
		cursorItem = makeCheckBoxCommandMenuItem(Messages.getString("MenuBase.cursorItem"), 'C', panel.isCursorMode()); //$NON-NLS-1$
	}
	/**
	 * [線の幅]メニュー項目を作成する。
	 */
	protected void addLinkWidthMenuItem() {
		linkWidthItem = makeCommandMenuItem(Messages.getString("MenuBase.linkWidthItem"), 'W'); // "色の更新(U)"
	}
	/**
	 * [色の更新]メニュー項目を作成し，[色の設定]メニューに追加する。
	 */
	protected void addRenewColorMenuItem() {
		renewColorItem = makeCommandMenuItem(Messages.getString("MenuBase.renewColorItem"), 'U'); //$NON-NLS-1$
		colorMenu.add(renewColorItem);
	}

	/**
	 * 指定したメニュー項目を[表示]メニューに追加する。
	 * @param item 追加するメニュー項目
	 */
	protected void addToViewMenu(JMenuItem item) {
		viewMenu.add(item);
	}

	/**
	 * 「エクスポート／インポート」メニューのサブメニュー項目を作成し，グループに追加する。
	 * データフォーマットをパラメータとしてメニュー項目の action command に設定する。
	 * @param text メニュー表示文字列
	 * @param mnemonic
	 * @param f データフォーマット
	 * @return 作成したメニュー項目
	 */
	protected JMenuItem makeDataExportItem(String text, char mnemonic, DataFormat f) {
		JMenuItem item = new JMenuItem(text, mnemonic);
		item.addActionListener(exportCommandAction);
		item.setActionCommand(f.toString());
		exportDataMenu.add(item);
		return item;
	}

	private ActionListener exportCommandAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem target = (JMenuItem) e.getSource();
			DataFormat f = DataFormat.valueOf(target.getActionCommand());
			command.exportProblemData(f);
		}
	};

	/**
	 * 「白マス確定記号スタイル選択」のサブメニュー項目を作成し，グループに追加する。
	 * @param text メニュー表示文字列
	 * @param n スタイル番号
	 * @return 作成したメニュー項目
	 */
	protected JRadioButtonMenuItem makeMarkStyleItem(String text, char mnemonic, int n) {
		JRadioButtonMenuItem markStyleItem = new JRadioButtonMenuItem(text);
		markStyleItem.setMnemonic(mnemonic);
		markStyleItem.addActionListener(markStyleAction);
		markStyleItem.setActionCommand(Integer.toString(n));
		markStyleGroup.add(markStyleItem);
		markStyleMenu.add(markStyleItem);
		return markStyleItem;
	}

	private ActionListener markStyleAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem target = (JMenuItem) e.getSource();
			panel.setMarkStyle(Integer.parseInt(target.getActionCommand()));
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
			JMenuItem item = (JMenuItem) e.getSource();
			// どのメニュー項目が選択されたかによって，どの色を変更するかをその場で決定する。
			Color color = JColorChooser.showDialog(panel, e.getActionCommand(), getColor(item));
			if (color != null) {
				setColor(item, color);
				setColorIcon(item, color);
			}
			panel.repaint();
		}
	};

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
		else if (target == rotationItem)
			command.rotateBoard();
		else if (target == changeBoardSizeItem)
			command.changeBoardSize();
		else if (target == saveImageItem)
			command.saveImage();
		else if (target == copyImageItem)
			command.copyImage();
		else if (target == printPreviewItem)
			command.printPreview();
		else if (target == printItem)
			command.print();
		else if (target == loadPreferencesItem) {
			command.loadPreferences();
			this.updateCurrentMenuSelection();
		}
		else if (target == storePreferencesItem)
			command.storePreferences();
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
		else if (target == clearQuestionItem)
			command.clearQuestion();
		else if (target == reconstructQuestionItem)
			command.reconstructQuestion();
		else if (target == exchangeNumbersItem)
			command.exchangeNumbers();
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
		else if (target == playbackItem)
			command.playback();
		else if (target == historyItem)
			command.history();
		else if (target == checkAnswerItem)
			command.checkAnswer();
		else if (target == immediateAnswerCheckItem)
			command.setImmediateAnswerCheckMode(target.isSelected());
		else if (target == renewColorItem)
			command.renewColor();
		else if (target == cursorItem)
			command.setCursorMode(target.isSelected());
		else if (target == answerModeItem)
			command.setEditMode(PanelBase.ANSWER_INPUT_MODE);
		else if (target == problemEditModeItem)
			command.setEditMode(PanelBase.PROBLEM_INPUT_MODE);
		else if (target == regionEditModeItem)
			command.setEditMode(PanelBase.REGION_EDIT_MODE);
		else if (target == indexItem)
			command.selectIndexLetters();
		else if (target == gridStyleItem)
			command.setGridStyle(target.isSelected());
		else if (target == cellSizeItem)
			command.cellSize();
		else if (target == linkWidthItem)
			command.changeLinkWidth();
		else
			executeCommand2(target);
		panel.repaint();
	}

	public void executeCommand2(JMenuItem target) {
		if (false)
			;
		else if (target == countAreaSizeItem)
			getPanel().setCountAreaSizeMode(target.isSelected());
		else if (target == dotHintItem)
			getPanel().setDotHintMode(target.isSelected());
		else if (target == hideSoleNumberItem)
			getPanel().setHideSoleNumberMode(target.isSelected());
		else if (target == hideStarItem)
			getPanel().setHideStarMode(target.isSelected());
		else if (target == highlightSelectionItem)
			getPanel().setHighlightSelectionMode(target.isSelected());
		else if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
		else if (target == paintIlluminatedCellItem)
			getPanel().setPaintIlluminatedCellMode(target.isSelected());
		else if (target == showAreaBorderItem)
			getPanel().setShowAreaBorderMode(target.isSelected());
		else if (target == showBeamItem)
			getPanel().setShowBeamMode(target.isSelected());
		else if (target == separateAreaColorItem)
			getPanel().setSeparateAreaColorMode(target.isSelected());
		else if (target == separateLinkColorItem)
			getPanel().setSeparateLinkColorMode(target.isSelected());
		else if (target == separateTetrominoColorItem)
			getPanel().setSeparateTetrominoColorMode(target.isSelected());
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

	/**
	 * メニュー項目に対応した色を取得する
	 * @param target 選択されたメニュー
	 * @return 取得した色
	 */
	public Color getColor(JMenuItem target) {
		if (false)
			return null;
		else if (target == backgroundColorItem)
			return getPanelBase().getBackgroundColor();
		else if (target == gridColorItem)
			return getPanelBase().getGridColor();
		else if (target == areaBorderColorItem)
			return getPanel().getAreaBorderColor();
		else if (target == areaPaintColorItem)
			return getPanel().getAreaPaintColor();
		else if (target == blackAreaColorItem)
			return getPanel().getBlackAreaColor();
		else if (target == borderColorItem)
			return getPanel().getBorderColor();
		else if (target == bulbColorItem)
			return getPanel().getBulbColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		else if (target == crossColorItem)
			return getPanel().getCrossColor();
		else if (target == gateColorItem)
			return getPanel().getGateColor();
		else if (target == illuminatedCellColorItem)
			return getPanel().getIlluminatedCellColor();
		else if (target == inputColorItem)
			return getPanel().getInputColor();
		else if (target == lineColorItem)
			return getPanel().getLineColor();
		else if (target == noBulbColorItem)
			return getPanel().getNoBulbColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == wallColorItem)
			return getPanel().getWallColor();
		else if (target == whiteAreaColorItem)
			return getPanel().getWhiteAreaColor();
		else
			return null;
	}

	/**
	 * メニュー項目に対応した色を取得する
	 * @param target 選択されたメニュー
	 * @param color 設定する色
	 */
	public void setColor(JMenuItem target, Color color) {
		if (false)
			;
		if (target == backgroundColorItem)
			getPanelBase().setBackgroundColor(color);
		else if (target == gridColorItem)
			getPanelBase().setGridColor(color);
		else if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else if (target == areaPaintColorItem)
			getPanel().setAreaPaintColor(color);
		else if (target == blackAreaColorItem)
			getPanel().setBlackAreaColor(color);
		else if (target == borderColorItem)
			getPanel().setBorderColor(color);
		else if (target == bulbColorItem)
			getPanel().setBulbColor(color);
		else if (target == circleColorItem)
			getPanel().setCircleColor(color);
		else if (target == crossColorItem)
			getPanel().setCrossColor(color);
		else if (target == gateColorItem)
			getPanel().setGateColor(color);
		else if (target == illuminatedCellColorItem)
			getPanel().setIlluminatedCellColor(color);
		else if (target == inputColorItem)
			getPanel().setInputColor(color);
		else if (target == lineColorItem)
			getPanel().setLineColor(color);
		else if (target == noBulbColorItem)
			getPanel().setNoBulbColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else if (target == wallColorItem)
			getPanel().setWallColor(color);
		else if (target == whiteAreaColorItem)
			getPanel().setWhiteAreaColor(color);
	}

	public void updateCurrentMenuSelection() {
//		indexItem.setSelected(getPanelBase().isIndexMode());
		gridStyleItem.setSelected(getPanelBase().getGridStyle() > 0);
		setItemSelected(cursorItem, getPanelBase().isCursorMode());
		setItemSelected(symmetricPlacementItem, getEventHandlerManager().isSymmetricPlacementMode());
		setItemSelected(immediateAnswerCheckItem, getEventHandlerManager().isImmediateAnswerCheckMode());
		if (markStyleMenu != null) {
			int s = getPanelBase().getMarkStyle();
			for (int i = 0, count = markStyleMenu.getItemCount(); i < count; i++) {
				JRadioButtonMenuItem item = (JRadioButtonMenuItem) markStyleMenu.getItem(i);
				if (Integer.parseInt(item.getActionCommand()) == s) {
					item.setSelected(true);
					break;
				}
			}
		}

		setItemSelected(countAreaSizeItem, getPanel().isCountAreaSizeMode());
		setItemSelected(dotHintItem, getPanel().isDotHintMode());
		setItemSelected(hideSoleNumberItem, getPanel().isHideSoleNumberMode());
		setItemSelected(hideStarItem, getPanel().isHideStarMode());
		setItemSelected(highlightSelectionItem, getPanel().isHighlightSelectionMode());
		setItemSelected(indicateErrorItem, getPanel().isIndicateErrorMode());
		setItemSelected(paintIlluminatedCellItem, getPanel().isPaintIlluminatedCellMode());
		setItemSelected(separateAreaColorItem, getPanel().isSeparateAreaColorMode());
		setItemSelected(separateLinkColorItem, getPanel().isSeparateLinkColorMode());
		setItemSelected(separateTetrominoColorItem, getPanel().isSeparateTetrominoColorMode());
		setItemSelected(showAreaBorderItem, getPanel().isShowAreaBorderMode());
		setItemSelected(showBeamItem, getPanel().isShowBeamMode());

		buildColorMenuIcon();
	}

	public void setItemSelected(JMenuItem item, boolean b) {
		if (item != null)
			item.setSelected(b);
	}
}
