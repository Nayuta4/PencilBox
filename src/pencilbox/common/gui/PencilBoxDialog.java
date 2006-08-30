package pencilbox.common.gui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * �J�X�^���_�C�A���O�p�̋��ʃX�[�p�[�N���X�B
 * �����Ɂu�����v�u����v�{�^����z�u���C�u�����v�{�^�����f�t�H���g�{�^���Ƃ���B
 * JDialog���쐬���Ă��̒��ɓ���ĕ\�����C����ꂽ�Ƃ��ɑI�����ꂽ���ʂ�Ԃ��B
 */
public class PencilBoxDialog extends JPanel {
	
    /** Return value from class method if CANCEL is chosen. */
    public static final int         CANCEL_OPTION = 2;
    /** Return value form class method if OK is chosen. */
    public static final int         OK_OPTION = 0;
    /** Return value from class method if user closes window without selecting
     * anything, more than likely this should be treated as
     * <code>CANCEL_OPTION</code>. */
    public static final int         CLOSED_OPTION = -1;
	
    /** [����]�{�^����������_�C�A���O�C���̂Ƃ���Ȃ� */
    public static final int OK_ONLY = 0;
    /** [����][���]�{�^����������_�C�A���O */
	public static final int OK_CANCEL = 1;
	
	private int dialogType = OK_CANCEL;
	private JPanel buttonPanel;
	private JButton	buttonOk;
	private JButton buttonCancel;
	private JDialog dialog;
	private int ret = CLOSED_OPTION; // �_�C�A���O�̕Ԃ�l

	/**
	 * �_�C�A���O���쐬����
	 * @throws HeadlessException
	 */
	public PencilBoxDialog() {
		super();
		makeDialog();
	}

	protected void makeDialog() {
		this.setLayout(new BorderLayout());
		makeButtonPanel();
		assignKeys();
	}
	
	private void makeButtonPanel() {
		buttonPanel = new JPanel();
		buttonOk = new JButton("����");
		buttonCancel = new JButton("���");
		ActionListener buttonAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == buttonOk) {
					accept();
				} else if (source == buttonCancel) {
					cancel();
				}
			}
		};
		buttonOk.addActionListener(buttonAction);
		buttonCancel.addActionListener(buttonAction);
//		buttonOk.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				accept();
//			}
//		});
//		buttonCancel.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				cancel();
//			}
//		});
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(buttonOk);
		if (dialogType == OK_CANCEL) {
			buttonPanel.add(buttonCancel);
		}
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * �_�C�A���O�^�C�v��ݒ肷��B���̂Ƃ���s�g�p
	 * @param dialogType The dialogType to set.
	 */
	public void setDialogType(int dialogType) {
		this.dialogType = dialogType;
	}

	/**
	 * ESC �L�[�Ń_�C�A���O�����
	 */
	private void assignKeys() {
		InputMap imap = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
		ActionMap amap = this.getActionMap();
		amap.put("close", new AbstractAction() {	
			public void actionPerformed(ActionEvent e) {
				if (dialog != null) { // �O�̂���
					ret = CLOSED_OPTION;
					dialog.setVisible(false);	
				}
			}
		});
	}

	protected void accept() {
		ret = OK_OPTION;
		dialog.setVisible(false);
	}

	protected void cancel() {
		ret = CANCEL_OPTION;
		dialog.setVisible(false);
	}
	
	/**
	 * �_�C�A���O��\������B
	 * ����JDialog����蒼���āCPencilBoxPane��JDialog�̒��ɓ���ĕ\������B
	 * @param parent �e�t���[��
	 * @param title �^�C�g��������
	 * @return ���[�U�[�̑I����������
	 */
//	�Q�l Core JAVA Vol.1 list 9-18
	public int showDialog(Component parent, String title) {
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame)parent;
		else
			owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
//      �K�v�ɉ����č�蒼�������Ńt�H�[�J�X�𖈉�ݒ肵����������������Ȃ��̂ŁC�����蒼��
//		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.getContentPane().add(this);
			dialog.pack();
			dialog.getRootPane().setDefaultButton(buttonOk);
//		}
		dialog.setTitle(title);
		dialog.setLocationRelativeTo(owner);
		setInitialFocus();
		dialog.setVisible(true);
		return ret;
	}
	
	/**
	 * �eDialog��ނ��ƂɁC�����t�H�[�J�X��ݒ肷��B
	 */
	protected void setInitialFocus() {
	}
}
	
