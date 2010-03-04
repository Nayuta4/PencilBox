/**
 * 
 */
package pencilbox.common.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import pencilbox.resource.Messages;

/**
 * 
 */
public class DataExportDialog extends JPanel {
	
	private JTextArea textArea;

	/**
	 * 
	 */
	public DataExportDialog() {
		super();
		makeDialog();
	}

	protected void makeDialog() {
		this.setLayout(new BorderLayout());
		makeButtonPanel();
		assignKeys();
		makeMainPanel();
	}

	private void makeMainPanel() {
		JPanel mainPanel = new JPanel();
		textArea = new JTextArea(20,40);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); //$NON-NLS-1$
		textArea.selectAll();
		mainPanel.add(textArea);
		JScrollPane jScrollPane = new JScrollPane(textArea);
		this.add(jScrollPane, BorderLayout.NORTH);
	}
	
	public void setText(String s) {
		textArea.append(s);
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(s.length());
	}
	
	public String getText() {
		return textArea.getText();
	}

	private JPanel buttonPanel;
	private JButton	buttonOpen;
	private JButton buttonCancel;
	private JButton buttonCopy;
	private JDialog dialog;
	private int ret = PencilBoxDialog.CLOSED_OPTION; // �_�C�A���O�̕Ԃ�l

	private void makeButtonPanel() {
		buttonPanel = new JPanel();
		buttonOpen = new JButton(Messages.getString("DataExportDialog.buttonOpen")); //$NON-NLS-1$
		buttonCopy = new JButton(Messages.getString("DataExportDialog.buttonCopy")); //$NON-NLS-1$
		buttonCancel = new JButton(Messages.getString("DataExportDialog.buttonCancel")); //$NON-NLS-1$
		ActionListener buttonAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == buttonOpen) {
					open();
				} else if (source == buttonCancel) {
					cancel();
				} else if (source == buttonCopy) {
					copyToClipboard();
				}
			}
		};
		buttonOpen.addActionListener(buttonAction);
		buttonCancel.addActionListener(buttonAction);
		buttonCopy.addActionListener(buttonAction);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(buttonCancel);
		buttonPanel.add(buttonOpen);
		buttonPanel.add(buttonCopy);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * ESC �L�[�Ń_�C�A���O�����
	 */
	private void assignKeys() {
		InputMap imap = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close"); //$NON-NLS-1$
		ActionMap amap = this.getActionMap();
		amap.put("close", new AbstractAction() {	 //$NON-NLS-1$
			public void actionPerformed(ActionEvent e) {
				if (dialog != null) { // �O�̂���
					ret = PencilBoxDialog.CLOSED_OPTION;
					dialog.setVisible(false);	
				}
			}
		});
	}

	protected void open() {
		ret = PencilBoxDialog.OK_OPTION;
		dialog.setVisible(false);
	}

	protected void cancel() {
		ret = PencilBoxDialog.CANCEL_OPTION;
		dialog.setVisible(false);
	}
	
	protected void copyToClipboard() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection str = new StringSelection(getText());
		clipboard.setContents(str , null);
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
			dialog.getRootPane().setDefaultButton(buttonCancel);
//		}
		dialog.setTitle(title);
		dialog.setLocationRelativeTo(owner);
		dialog.setVisible(true);
		return ret;
	}

}
