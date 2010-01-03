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

/**
 * ���ʃ��j���[�N���X
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
	private JMenuItem changeBoardSizeItem;
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
	private JMenuItem playbackItem;
	private JMenuItem checkAnswerItem;
	private JMenuItem immediateAnswerCheckItem;
	private JMenuItem cellSizeItem;
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
	private JMenu rotationMenu;
	private JMenu markStyleMenu;
	private JMenu exportDataMenu;
	private ButtonGroup markStyleGroup;
	
	private JMenuItem backgroundColorItem;
	private JMenuItem gridColorItem;

	private Frame frame;
	private MenuCommand command;
	private PanelBase panel;

	/**
	 * Menu�N���X�̏����������ŁCMenu�C���X�^���X��������Ɏg�p�����
	 * @param command �֘A�t����MenuCommand
	 * @param frame �֘A�t����Frame
	 * @param panel �֘A�t����Panel
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

	protected EventHandlerManager getEventHandlerManager() {
		return command.getEventHandlerManager();
	}

	/**
	 * ���ʃ��j���[���쐬����B
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
	 * [�t�@�C��]���j���[�쐬
	 */
	protected void buildFileMenu() {
		fileMenu = makeJMenu(Messages.getString("MenuBase.fileMenu"), 'F'); //$NON-NLS-1$
		fileMenu.add(newBoardItem = makeCommandMenuItem(Messages.getString("MenuBase.newBoardItem"), 'N')); //$NON-NLS-1$
		fileMenu.add(openItem = makeCommandMenuItem(Messages.getString("MenuBase.openItem"), 'O')); //$NON-NLS-1$
		fileMenu.add(closeAndOpenItem = makeCommandMenuItem(Messages.getString("MenuBase.closeAndOpenItem"), 'L')); //$NON-NLS-1$
		fileMenu.add(saveItem = makeCommandMenuItem(Messages.getString("MenuBase.saveItem"), 'S')); //$NON-NLS-1$
		fileMenu.addSeparator();
		fileMenu.add(duplicateItem = makeCommandMenuItem(Messages.getString("MenuBase.duplicateItem"), 'D')); //$NON-NLS-1$
		fileMenu.add(rotationMenu = makeJMenu(Messages.getString("MenuBase.rotationMenu"), 'R')); //$NON-NLS-1$
		fileMenu.add(changeBoardSizeItem = makeCommandMenuItem(Messages.getString("MenuBase.changeBoardSizeItem"), 'Z')); //$NON-NLS-1$
		buildRotationMenu();
		fileMenu.addSeparator();
		fileMenu.add(exportDataMenu = makeJMenu(Messages.getString("MenuBase.exportMenu"), 'X')); //$NON-NLS-1$
		buildExportDataMenu();
		fileMenu.add(saveImageItem = makeCommandMenuItem(Messages.getString("MenuBase.saveImageItem"), 'G')); //$NON-NLS-1$
		fileMenu.add(copyImageItem = makeCommandMenuItem(Messages.getString("MenuBase.copyImageItem"), 'M')); //$NON-NLS-1$
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
	 * [�ҏW]���j���[�쐬
	 */
	protected void buildEditMenu() {
		editMenu = makeJMenu(Messages.getString("MenuBase.editMenu"), 'E'); //$NON-NLS-1$
		editMenu.add(answerModeItem = makeCommandRadioButtonMenuItem(Messages.getString("MenuBase.answerModeItem"), 'A')); //$NON-NLS-1$
		editMenu.add(problemEditModeItem = makeCommandRadioButtonMenuItem(Messages.getString("MenuBase.problemEditModeItem"), 'E')); //$NON-NLS-1$
		editMenu.add(regionEditModeItem = makeCommandRadioButtonMenuItem(Messages.getString("MenuBase.regionEditModeItem"), 'G')); //$NON-NLS-1$
		editMenu.addSeparator();
		editMenu.add(clearItem = makeCommandMenuItem(Messages.getString("MenuBase.clearItem"), 'C')); //$NON-NLS-1$
		if (trimAnswerItem != null) 
			editMenu.add(trimAnswerItem);  // ("�⏕�L������(T)", 'T')
		if (symmetricPlacementItem != null) {
			editMenu.addSeparator();
			editMenu.add(symmetricPlacementItem); // ("�Ώ̔z�u(S)", 'S')
		}
		editMenu.addSeparator();
		editMenu.add(undoItem = makeCommandMenuItem(Messages.getString("MenuBase.undoItem"), 'U')); //$NON-NLS-1$
		editMenu.add(redoItem = makeCommandMenuItem(Messages.getString("MenuBase.redoItem"), 'R')); //$NON-NLS-1$
		// editMenu.add(undoAllItem = makeCommandMenuItem("�ŏ��܂Ŗ߂�(F)", 'F'));
		// editMenu.add(redoAllItem = makeCommandMenuItem("�Ō�܂Ői�߂�(L)", 'L'));
		editMenu.add(playbackItem = makeCommandMenuItem(Messages.getString("MenuBase.playbackItem"), 'P')); //$NON-NLS-1$
		editMenu.addSeparator();
		editMenu.add(checkAnswerItem = makeCommandMenuItem(Messages.getString("MenuBase.checkAnswerItem"), 'H')); //$NON-NLS-1$
		editMenu.add(immediateAnswerCheckItem = makeCheckBoxCommandMenuItem(Messages.getString("MenuBase.immediateAnswerCheckItem"), 'M', false)); //$NON-NLS-1$

		modeGroup = new ButtonGroup();
		modeGroup.add(answerModeItem);
		modeGroup.add(problemEditModeItem);
		modeGroup.add(regionEditModeItem);
		editMenu.addMenuListener(new EditMenuListener());
	}

	/**
	 * [�\��]���j���[�쐬
	 */
	protected void buildViewMenu() {
		viewMenu = makeJMenu(Messages.getString("MenuBase.viewMenu"), 'V'); //$NON-NLS-1$
		viewMenu.add(colorMenu = makeJMenu(Messages.getString("MenuBase.colorMenu"), 'L')); //$NON-NLS-1$
		backgroundColorItem = addColorMenuItem(Messages.getString("MenuBase.backgroundColorItem")); //$NON-NLS-1$
		gridColorItem = addColorMenuItem(Messages.getString("MenuBase.gridColorItem")); //$NON-NLS-1$
		viewMenu.add(cellSizeItem = makeCommandMenuItem(Messages.getString("MenuBase.cellSizeItem"), 'S')); //$NON-NLS-1$
		viewMenu.add(indexItem = makeCheckBoxCommandMenuItem(Messages.getString("MenuBase.indexItem"), 'I', false)); //$NON-NLS-1$
		viewMenu.add(gridStyleItem = makeCheckBoxCommandMenuItem(Messages.getString("MenuBase.gridStyleItem"), 'G', true)); //$NON-NLS-1$
		if (markStyleMenu != null)
			viewMenu.add(markStyleMenu);
		if (cursorItem != null)
			viewMenu.add(cursorItem);  // ("�J�[�\��(C)", 'C')
		viewMenu.addSeparator();
		viewMenu.addMenuListener(new ViewMenuListener());
	}

	/**
	 * �J���[���j���[�̃A�C�R����ݒ肵�܂�
	 */
	protected void buildColorMenuIcon() {
		int count = colorMenu.getMenuComponentCount();
		for (int i = 0; i < count; ++i) {
			Component component = colorMenu.getMenuComponent(i);
			
			if (component instanceof JMenuItem) {
				JMenuItem item = (JMenuItem)component;
				Color color = getColor(item);
				if (color != null) {
					setColorIcon(item, color);
				}
			}
		}
	}
	
	/**
	 * ���j���[�ɃJ���[�A�C�R����ݒ肵�܂�
	 * 
	 * @param item ���j���[
	 * @param color �J���[
	 */
	protected void setColorIcon(JMenuItem item, Color color) {

		Icon icon = item.getIcon();
		
		if (icon != null && icon instanceof ColorIcon && ((ColorIcon)icon).getColor().equals(color)) {
			return;
		}
		
		item.setIcon(new ColorIcon(color, Color.BLACK, item.getFont().getSize(), item.getFont().getSize()));
	}

	/**
	 * [�w���v]���j���[�쐬
	 */
	protected void buildHelpMenu() {
		helpMenu = makeJMenu(Messages.getString("MenuBase.helpMenu"), 'H'); //$NON-NLS-1$
		helpMenu.add(aboutItem = makeCommandMenuItem(Messages.getString("MenuBase.aboutDialog"), 'A')); //$NON-NLS-1$
	}

	/**
	 * [��]�E���]]���j���[�쐬
	 */
	protected void buildRotationMenu() {
		makeRotationItem(Messages.getString("MenuBase.rotationItem1"), '1', 1); //$NON-NLS-1$
		makeRotationItem(Messages.getString("MenuBase.rotationItem2"), '2', 2); //$NON-NLS-1$
		makeRotationItem(Messages.getString("MenuBase.rotationItem3"), '3', 3); //$NON-NLS-1$
		makeRotationItem(Messages.getString("MenuBase.rotationItem4"), '4', 4); //$NON-NLS-1$
		makeRotationItem(Messages.getString("MenuBase.rotationItem5"), '5', 5); //$NON-NLS-1$
		makeRotationItem(Messages.getString("MenuBase.rotationItem6"), '6', 6); //$NON-NLS-1$
		makeRotationItem(Messages.getString("MenuBase.rotationItem7"), '7', 7); //$NON-NLS-1$
	}
	/**
	 * [��]�E���]]���j���[�쐬
	 * �i�J�b�N���p�j
	 */
	protected void buildRotationMenu2() {
		makeRotationItem(Messages.getString("MenuBase.rotationItem4"), '4', 4); //$NON-NLS-1$
	}

	/**
	 * �u�G�N�X�|�[�g�^�C���|�[�g�v�T�u���j���[���쐬����B
	 * @param formats �I���\�ȃt�H�[�}�b�g
	 */
	protected void buildExportDataMenu(DataFormat[] formats) {
		for (DataFormat f : formats) {
			if (f == DataFormat.KANPEN) {
				makeDataExportItem(Messages.getString("MenuBase.exportItem1"), 'K', DataFormat.KANPEN); //$NON-NLS-1$
			}
		}
	}

	/**
	 * �u�G�N�X�|�[�g�^�C���|�[�g�v�T�u���j���[���쐬����B
	 * �I���\�ȃt�H�[�}�b�g���W���ƈقȂ�ꍇ�Ȃǂ͕K�v�ɉ����ăT�u�N���X�ŏ㏑�����邱�ƁB
	 */
	protected void buildExportDataMenu() {
		buildExportDataMenu(new DataFormat[] {DataFormat.KANPEN});
	}

	/**
	 * ���}�X�m��L���X�^�C�����j���[���쐬����B
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
	 * [�ҏW]���j���[���X�i�[�N���X
	 */
	protected class EditMenuListener implements MenuListener {
		/*
		 * �ҏW���j���[���J�����^�C�~���O�ŁC �K�v�ɉ����ăA���h�D�C���h�D���j���[��L���^�����ɂ���
		 * �����̓��[�h��'/'�L�[�ɂ��C���j���[��p�����ɕύX�ł���̂ŁC���j���[���J���Ƃ��Ɍ��݂̏�Ԃ𒲂ׂčX�V����B
		 */
		public void menuSelected(MenuEvent evt) {
			undoItem.setEnabled(command.canUndo());
			redoItem.setEnabled(command.canRedo());
			// undoAllItem.setEnabled(command.canUndo());
			// redoAllItem.setEnabled(command.canRedo());
			playbackItem.setEnabled(command.canUndo() || command.canRedo());

			int m = getPanelBase().getEditMode();
			if (m == PanelBase.ANSWER_INPUT_MODE) {
				answerModeItem.setSelected(true);
			} else if (m == PanelBase.PROBLEM_INPUT_MODE) {
				problemEditModeItem.setSelected(true);
			} else if (m == PanelBase.REGION_EDIT_MODE) {
				regionEditModeItem.setSelected(true);
			}
		}

		/*
		 * ���j���[�������炷�ׂėL���ɖ߂�
		 */
		public void menuDeselected(MenuEvent evt) {
			undoItem.setEnabled(true);
			redoItem.setEnabled(true);
			// undoAllItem.setEnabled(true);
			// redoAllItem.setEnabled(true);
			playbackItem.setEnabled(true);
		}

		public void menuCanceled(MenuEvent evt) {
		}
	}

	/**
	 * �V���[�g�J�b�g�L�[�̈ꊇ�ݒ�
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
		checkAnswerItem.setAccelerator(
				KeyStroke.getKeyStroke('H', InputEvent.CTRL_MASK));
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
	 * [�\��]���j���[���X�i�[�N���X
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
	 * ����ނ��̃p�Y���ɕs�K�p�̃��j���[�̍폜���s���B
	 */
	/**
	 * [�ՖʃT�C�Y�ύX]���j���[���ڂ��폜����B
	 */
	protected void removeChangeBoardSizeMenuItem() {
		fileMenu.remove(changeBoardSizeItem);
	}
	/*
	 * ����ނ��̃p�Y���ɋ��ʂȃ��j���[�̍쐬�݂̂��s���B
	 * �쐬�������ڂ̓��j���[�쐬���ɒǉ������B
	 */
	/**
	 * [�⏕�L������]���j���[���ڂ��쐬����B
	 */
	protected void addTrimAnswerMenuItem() {
		trimAnswerItem = makeCommandMenuItem(Messages.getString("MenuBase.trimAnswerItem"), 'T'); //$NON-NLS-1$
	}
	/**
	 * [�Ώ̔z�u]���j���[���ڂ��쐬����B
	 */
	protected void addSymmetricPlacementMenuItem() {
		symmetricPlacementItem = makeCheckBoxCommandMenuItem(Messages.getString("MenuBase.symmetricPlacementItem"), 'S', false); //$NON-NLS-1$
	}
	/**
	 * [�J�[�\��]���j���[���ڂ��쐬����B
	 */
	protected void addCursorMenuItem() {
		cursorItem = makeCheckBoxCommandMenuItem(Messages.getString("MenuBase.cursorItem"), 'C', panel.isCursorMode()); //$NON-NLS-1$
	}
	/**
	 * [�F�̍X�V]���j���[���ڂ��쐬���C[�F�̐ݒ�]���j���[�ɒǉ�����B
	 */
	protected void addRenewColorMenuItem() {
		renewColorItem = makeCommandMenuItem(Messages.getString("MenuBase.renewColorItem"), 'U'); //$NON-NLS-1$
		colorMenu.add(renewColorItem);
	}

	/**
	 * �w�肵�����j���[���ڂ�[�\��]���j���[�ɒǉ�����B
	 * @param item �ǉ����郁�j���[����
	 */
	protected void addToViewMenu(JMenuItem item) {
		viewMenu.add(item);
	}

	/**
	 * �u��]�E���]�v���j���[�̃T�u���j���[���ڂ��쐬���C�O���[�v�ɒǉ�����B
	 * ��]�ԍ����p�����[�^�Ƃ��ă��j���[���ڂ� action command �ɐݒ肷��B
	 * @param text ���j���[�\��������
	 * @param mnemonic
	 * @param n �ݒ肷���]�ԍ�
	 * @return �쐬�������j���[����
	 */
	protected JMenuItem makeRotationItem(String text, char mnemonic, int n) {
		JMenuItem rotationItem = new JMenuItem(text, mnemonic);
		rotationItem.addActionListener(rotationCommandAction);
		rotationItem.setActionCommand(Integer.toString(n));
		rotationMenu.add(rotationItem);
		return rotationItem;
	}

	/**
	 * ��]�R�}���h�B 
	 * ���j���[���ڂ� action command �����]�ԍ���ǂݎ���Ď��s����B
	 */
	private ActionListener rotationCommandAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem target = (JMenuItem) e.getSource();
			command.rotateBoard(Integer.parseInt(target.getActionCommand()));
			panel.repaint();
		}
	};

	/**
	 * �u��]�E���]�v���j���[�̃T�u���j���[���ڂ��쐬���C�O���[�v�ɒǉ�����B
	 * ��]�ԍ����p�����[�^�Ƃ��ă��j���[���ڂ� action command �ɐݒ肷��B
	 * @param text ���j���[�\��������
	 * @param mnemonic
	 * @param n �ݒ肷���]�ԍ�
	 * @return �쐬�������j���[����
	 */
	protected JMenuItem makeDataExportItem(String text, char mnemonic, DataFormat f) {
		JMenuItem item = new JMenuItem(text, mnemonic);
		item.addActionListener(exportCommandAction);
		item.setActionCommand(f.toString());
		exportDataMenu.add(item);
		return item;
	}

	/**
	 * ��]�R�}���h�B 
	 * ���j���[���ڂ� action command �����]�ԍ���ǂݎ���Ď��s����B
	 */
	private ActionListener exportCommandAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem target = (JMenuItem) e.getSource();
			DataFormat f = DataFormat.valueOf(target.getActionCommand());
			command.exportProblemData(f);
		}
	};

	/**
	 * �u���}�X�m��L���X�^�C���I���v�̃T�u���j���[���ڂ��쐬���C�O���[�v�ɒǉ�����B
	 * @param text ���j���[�\��������
	 * @param n �X�^�C���ԍ�
	 * @return �쐬�������j���[����
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
	 * �F�I�����j���[�쐬�p�⏕���\�b�h
	 * ���j���[���ڂ��쐬���āC[�F�̑I��]���j���[�ɒǉ�����B
	 * @param text ���j���[�\��������
	 * @return �쐬�����F�I��p���j���[����
	 */
	protected JMenuItem addColorMenuItem(final String text) {
		JMenuItem item = new JMenuItem(text);
		item.addActionListener(changeColorAction);
		colorMenu.add(item);
		return item;
	}

	/**
	 * �F�ݒ�R�}���h�B 
	 */
	private ActionListener changeColorAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			// �ǂ̃��j���[���ڂ��I�����ꂽ���ɂ���āC�ǂ̐F��ύX���邩�����̏�Ō��肷��B
			Color color = JColorChooser.showDialog(panel, e.getActionCommand(), getColor(item));
			if (color != null) {
				setColor(item, color);
				setColorIcon(item, color);
			}
			panel.repaint();
		}
	};

	/**
	 * ���j���[���ڂɑΉ������F���擾���� ���g�͊e�T�u�N���X���ƂɎ�������B
	 * @param target �I�����ꂽ���j���[
	 * @return �擾�����F
	 */
	public Color getColor(JMenuItem target) {
		if (target == backgroundColorItem)
			return getPanelBase().getBackgroundColor();
		else if (target == gridColorItem)
			return getPanelBase().getGridColor();
		else
			return null;
	}

	/**
	 * ���j���[���ڂɑΉ������F���擾���� ���g�͊e�T�u�N���X���ƂɎ�������B
	 * @param target �I�����ꂽ���j���[
	 * @param color �ݒ肷��F
	 */
	public void setColor(JMenuItem target, Color color) {
		if (target == backgroundColorItem)
			getPanelBase().setBackgroundColor(color);
		else if (target == gridColorItem)
			getPanelBase().setGridColor(color);
	}

	/**
	 * ���j���[������s�����R�}���h�B 
	 */
	private ActionListener commandAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			executeCommand((JMenuItem) e.getSource());
		}
	};

	/**
	 * �ǂ̃��j���[���I�����ꂽ�����ׂāC�Ή�����R�}���h���N������B
	 * ���s��CPanel���ĕ`�悷��B
	 * @param target �I�����ꂽ���j���[����
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
		else if (target == changeBoardSizeItem)
			command.changeBoardSize();
		else if (target == saveImageItem)
			command.saveImage();
		else if (target == copyImageItem)
			command.copyImage();
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
			command.setIndexMode(target.isSelected());
		else if (target == gridStyleItem)
			command.setGridStyle(target.isSelected());
		else if (target == cellSizeItem)
			command.cellSize();
		else
			executeCommand2(target);
		panel.repaint();
	}
	/**
	 * �ǂ̃��j���[���I�����ꂽ�����ׂāC�Ή�����R�}���h���N������B
	 * �e�T�u�N���X�ŃI�[�o�[���C�h����B
	 * @param target �I�����ꂽ���j���[����
	 */
	public void executeCommand2(JMenuItem target) {
	}

	/**
	 * CommandAction����������j���[���ڂ��쐬����
	 * @param title ���j���[���ڕ\���e�L�X�g
	 * @param mnumonic �V���[�g�J�b�g
	 * @return �쐬�������j���[����
	 */
	protected JMenuItem makeCommandMenuItem(String title, char mnumonic) {
		JMenuItem item = makeJMenuItem(title, mnumonic);
		item.addActionListener(commandAction);
		return item;
	}

	/**
	 * CommandAction����������W�I�{�^�����j���[���ڂ��쐬����
	 * @param title ���j���[���ڕ\���e�L�X�g
	 * @param mnumonic �V���[�g�J�b�g
	 * @return �쐬�������j���[����
	 */
	protected JMenuItem makeCommandRadioButtonMenuItem(String title,
			char mnumonic) {
		JMenuItem item = makeJRadioButtonMenuItem(title, mnumonic);
		item.addActionListener(commandAction);
		return item;
	}

	/**
	 * CommandAction��������`�F�b�N�{�b�N�X���j���[���ڂ��쐬����
	 * @param title ���j���[���ڕ\���e�L�X�g
	 * @param mnumonic �V���[�g�J�b�g
	 * @param initial �����I��
	 * @return �쐬�������j���[����
	 */
	protected JCheckBoxMenuItem makeCheckBoxCommandMenuItem(String title,
			char mnumonic, boolean initial) {
		JCheckBoxMenuItem item = makeJCheckBoxMenuItem(title, mnumonic, initial);
		item.addActionListener(commandAction);
		return item;
	}

	/**
	 * ���݂̐ݒ�ɍ��킹�ă��j���[���ڂ̑I����Ԃ��X�V����B
	 */
	public void updateCurrentMenuSelection() {
		indexItem.setSelected(getPanelBase().isIndexMode());
		gridStyleItem.setSelected(getPanelBase().getGridStyle() > 0);
		if (cursorItem != null)
			cursorItem.setSelected(getPanelBase().isCursorMode());
		if (symmetricPlacementItem != null)
			symmetricPlacementItem.setSelected(getEventHandlerManager().isSymmetricPlacementMode());
		if (immediateAnswerCheckItem != null)
			immediateAnswerCheckItem.setSelected(getEventHandlerManager().isImmediateAnswerCheckMode());
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
		buildColorMenuIcon();
	}

	/*
	 * �ȉ��C���j���[�쐬�p�⏕���\�b�h
	 */
	/**
	 * JMenu �쐬�p���\�b�h
	 * @param text ���j���[�̕�����
	 * @param mnemonic
	 * @return �쐬����JMenuItem
	 */
	public JMenu makeJMenu(String text, char mnemonic) {
		JMenu menu = new JMenu(text);
		menu.setMnemonic(mnemonic);
		return menu;
	}

	/**
	 * JMenuItem �쐬�p���\�b�h
	 * @param text ���j���[�̕�����
	 * @param mnemonic
	 * @return �쐬����JMenuItem
	 */
	public JMenuItem makeJMenuItem(String text, char mnemonic) {
		return new JMenuItem(text, mnemonic);
	}

	/**
	 * JRadioButtonMenuItem �쐬�p���\�b�h
	 * @param text ���j���[�̕�����
	 * @param mnemonic
	 * @return �쐬�������j���[����
	 */
	public JMenuItem makeJRadioButtonMenuItem(String text, char mnemonic) {
		JMenuItem item = new JRadioButtonMenuItem(text);
		item.setMnemonic(mnemonic);
		return item;
	}

	/**
	 * JCheckBoxMenuItem �쐬�p���\�b�h
	 * @param text ���j���[�̕�����
	 * @param mnemonic �V���[�g�J�b�g�L�[
	 * @param initial �����l
	 * @return �쐬����JMenuItem
	 */
	public JCheckBoxMenuItem makeJCheckBoxMenuItem(String text, char mnemonic,
			boolean initial) {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(text);
		item.setMnemonic(mnemonic);
		item.setSelected(initial);
		return item;
	}

}
