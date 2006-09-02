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
 * ���ʃ��j���[�N���X
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
	 * ���ʃ��j���[���쐬����B
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
	 * �X�̃p�Y���p���j���[���쐬����B 
	 * �e�T�u�N���X�ŃI�[�o�[���C�h����B
	 */
	protected void buildIndividualMenu() {
	}

	/**
	 * [�t�@�C��]���j���[�쐬
	 */
	protected void buildFileMenu() {
		fileMenu = makeJMenu("�t�@�C��(F)", 'F');
		fileMenu.add(newBoardItem = makeCommandMenuItem("�V�K�쐬(N)...", 'N'));
		fileMenu.add(openItem = makeCommandMenuItem("�J��(O)...", 'O'));
		fileMenu.add(coloseAndOpenItem = makeCommandMenuItem("���ĊJ��(L)...", 'L'));
		fileMenu.add(saveItem = makeCommandMenuItem("�ۑ�(S)...", 'S'));
		fileMenu.addSeparator();
		fileMenu.add(exportProblemDataStringItem = makeCommandMenuItem("���f�[�^������o��(E)...", 'E'));
		fileMenu.add(saveImageItem = makeCommandMenuItem("�摜�ۑ�(I)...", 'I'));
		fileMenu.add(copyImageItem = makeCommandMenuItem("�摜�R�s�[(M)", 'M'));
		fileMenu.add(printItem = makeCommandMenuItem("���(P)...", 'P'));
		fileMenu.addSeparator();
		fileMenu.add(propertyItem = makeCommandMenuItem("�v���p�e�B(T)", 'T'));
		fileMenu.addSeparator();
		fileMenu.add(closeItem = makeCommandMenuItem("����(C)", 'C'));
		fileMenu.add(quiteItem = makeCommandMenuItem("�I��(Q)", 'Q'));
	}

	/**
	 * [�ҏW]���j���[�쐬
	 */
	protected void buiildEditMenu() {
		editMenu = makeJMenu("�ҏW(E)", 'E');
		editMenu.add(answerModeItem = makeCommandRadioButtonMenuItem("�𓚃��[�h(A)", 'A'));
		editMenu.add(problemEditModeItem = makeCommandRadioButtonMenuItem("�����̓��[�h(E)", 'E'));
		editMenu.addSeparator();
		editMenu.add(clearItem = makeCommandMenuItem("�𓚏���(C)", 'C'));
		editMenu.addSeparator();
		editMenu.add(undoItem = makeCommandMenuItem("���ɖ߂�(U)", 'U'));
		editMenu.add(redoItem = makeCommandMenuItem("��蒼��(R)", 'R'));
		// editMenu.add(undoAllItem = makeCommandMenuItem("�ŏ��܂Ŗ߂�(F)", 'F'));
		// editMenu.add(redoAllItem = makeCommandMenuItem("�Ō�܂Ői�߂�(L)", 'L'));
		editMenu.add(palybackItem = makeCommandMenuItem("�����Đ�(P)", 'P'));
		editMenu.addSeparator();
		editMenu.add(checkAnswerItem = makeCommandMenuItem("��������(H)", 'H'));
		editMenu.addSeparator();

		modeGroup = new ButtonGroup();
		modeGroup.add(answerModeItem);
		modeGroup.add(problemEditModeItem);
		editMenu.addMenuListener(new EditMenuListener());
	}

	/**
	 * [�\��]���j���[�쐬
	 */
	protected void buildViewMenu() {
		viewMenu = makeJMenu("�\��(V)", 'V');
		buildRotationMenu();
		buildDisplaySizeMenu();
		buildGridStyleMenu();
		viewMenu.add(colorMenu = makeJMenu("�F�̐ݒ�(L)", 'L'));
		viewMenu.add(rotationMenu);
		viewMenu.add(displaySizeMenu);
		viewMenu.add(showIndexModeItem = makeCheckBoxCommandMenuItem("�s��ԍ��\��(I)", 'I', true));
		viewMenu.addSeparator();
		viewMenu.addMenuListener(new ViewMenuListener());
	}

	/**
	 * [�w���v]���j���[�쐬
	 */
	protected void buildHelpMenu() {
		helpMenu = makeJMenu("�w���v(H)", 'H');
		helpMenu.add(aboutItem = makeCommandMenuItem("PencilBox�ɂ���(A)", 'A'));
	}

	/**
	 * [��]�E���]]���j���[�쐬
	 */
	protected void buildRotationMenu() {
		rotationMenu = makeJMenu("��]�E���](R)", 'R');
		rotationGroup = new ButtonGroup();
		makeRotationItem(0, "0��").setSelected(true);
		makeRotationItem(1, "��90����]");
		makeRotationItem(2, "180����]");
		makeRotationItem(3, "�E90����]");
		makeRotationItem(4, "�c������");
		makeRotationItem(5, "���E���]");
		makeRotationItem(6, "180����]+�c������");
		makeRotationItem(7, "�㉺���]");
	}
	/**
	 * [��]�E���]]���j���[�쐬
	 * �i�J�b�N���p�b��j
	 */
	protected void buildRotationMenu2() {
		rotationMenu = makeJMenu("��]�E���](R)", 'R');
		rotationGroup = new ButtonGroup();
		makeRotationItem(0, "0��").setSelected(true);
//		makeRotationItem(1, "��90����]");
//		makeRotationItem(2, "180����]");
//		makeRotationItem(3, "�E90����]");
		makeRotationItem(4, "�c������");
//		makeRotationItem(5, "���E���]");
//		makeRotationItem(6, "180����]+�c������");
//		makeRotationItem(7, "�㉺���]");
	}

	/**
	 * [�\���T�C�Y]���j���[�쐬
	 */
	void buildDisplaySizeMenu() {
		displaySizeMenu = makeJMenu("�\���T�C�Y(S)", 'S');
		displaySizeGroup = new ButtonGroup();
		makeDisplaySizeItem(32, "��");
		makeDisplaySizeItem(26, "��").setSelected(true);
		makeDisplaySizeItem(20, "��");
		makeDisplaySizeItem(14, "�ɏ�");
	}

	/**
	 * �r���\���X�^�C�����j���[���쐬����B
	 * ��̓I���e�́C�K�v�ȂƂ��̂݃T�u�N���X�ŃI�[�o�[���C�h���č쐬����
	 */
	protected void buildGridStyleMenu() {
		gridStyleMenu = makeJMenu("�r���\��(G)", 'G');
		gridStyleGroup = new ButtonGroup();
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
			palybackItem.setEnabled(command.canUndo() || command.canRedo());

			boolean b = getPanelBase().isProblemEditMode();
			problemEditModeItem.setSelected(b);
			answerModeItem.setSelected(!b);
		}

		/*
		 * ���j���[�������炷�ׂėL���ɖ߂�
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
	 * �V���[�g�J�b�g�L�[�̈ꊇ�ݒ�
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
	 * ����ނ��̃p�Y���ɋ��ʂȃ��j���[�̍쐬�ƒǉ����s��
	 */
	/**
	 * [�J�[�\��]���j���[���ڂ�[�\��]���j���[�̏ォ��5�Ԗڂɒǉ�����B
	 */
	protected void addCursorMenu() {
		cursorItem = makeJCheckBoxMenuItem("�J�[�\��(C)", 'C', panel.isCursorOn());
		cursorItem.addActionListener(commandAction);
		viewMenu.add(cursorItem, 4);
	}
	/**
	 * [�F�̍X�V]���j���[���ڂ�[�\��]���j���[�ɒǉ�����B
	 */
	protected void addRenewColorMenu() {
		renewColorItem = makeCommandMenuItem("�F�̍X�V(U)", 'U');
		viewMenu.add(renewColorItem);
	}
	/**
	 * [�r���\��]���j���[��[�\��]���j���[�̏ォ��4�Ԗڂɒǉ�����B
	 */
	protected void addGridStyleMenu() {
		viewMenu.add(gridStyleMenu, 3);
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
	 * @param n �ݒ肷���]��Ԕԍ�
	 * @param text ���j���[�\��������
	 * @return �쐬�������j���[����
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
	 * �u�\���T�C�Y�ύX�v�̃T�u���j���[���ڂ��쐬���C�O���[�v�ɒǉ�����B
	 * @param n �ݒ肷��Z���T�C�Y
	 * @param text ���j���[�\��������
	 * @return �쐬�������j���[����
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
	 * �u�r���X�^�C���I���v�̃T�u���j���[���쐬���C�O���[�v�ɒǉ�����B
	 * @param n �X�^�C���ԍ�
	 * @param text ���j���[�\��������
	 * @return �쐬�������j���[����
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
			// �ǂ̃��j���[���ڂ��I�����ꂽ���ɂ���āC�ǂ̐F��ύX���邩�����̏�Ō��肷��B
			Color color = JColorChooser.showDialog(panel, e.getActionCommand(),
					getColor((JMenuItem) e.getSource()));
			if (color != null)
				setColor((JMenuItem) e.getSource(), color);
			panel.repaint();
		}
	};

	/**
	 * ���j���[���ڂɑΉ������F���擾���� ���g�͊e�T�u�N���X���ƂɎ�������B
	 * @param name �I�����ꂽ���j���[
	 * @return �擾�����F
	 */
	public Color getColor(JMenuItem name) {
		return null;
	}

	/**
	 * ���j���[���ڂɑΉ������F���擾���� ���g�͊e�T�u�N���X���ƂɎ�������B
	 * @param name �I�����ꂽ���j���[
	 * @param color �ݒ肷��F
	 */
	public void setColor(JMenuItem name, Color color) {
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
