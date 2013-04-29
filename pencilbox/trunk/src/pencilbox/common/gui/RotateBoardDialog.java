package pencilbox.common.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pencilbox.resource.Messages;

/**
 * �u�Ֆʉ�]�v�_�C�A���O
 * ���[�_���_�C�A���O�Ȃ̂ŁC1�̃C���X�^���X�݂̂𐶐����Ďg����
 */

public class RotateBoardDialog extends PencilBoxDialog {

	private static RotateBoardDialog instance = new RotateBoardDialog();
	/**
	 * RotateBoardDialog �̃C���X�^���X���擾����
	 * �R���X�g���N�^�͎g�p�s�ŁC�C���X�^���X�͂��̃��\�b�h�ɂ��擾����
	 * @return RotateBoardDialog �C���X�^���X
	 */
	public static RotateBoardDialog getInstance() {
		return instance;
	}

	private JPanel mainPanel;
	private ButtonPanel typePanel;

	private RotateBoardDialog() {
		super();
	}

	protected void makeDialog() {
		super.makeDialog();
		makeMainPanel();
	}

	private void makeMainPanel() {
		mainPanel = new JPanel(new FlowLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		typePanel = new ButtonPanel(Messages.getString("MenuCommand.rotateBoardDialog"),
				new String[] {
					Messages.getString("MenuBase.rotationItem1"),
					Messages.getString("MenuBase.rotationItem2"),
					Messages.getString("MenuBase.rotationItem3"),
					Messages.getString("MenuBase.rotationItem4"),
					Messages.getString("MenuBase.rotationItem5"),
					Messages.getString("MenuBase.rotationItem6"),
					Messages.getString("MenuBase.rotationItem7") }, 
				new String[] { "1", "2", "3", "4", "5", "6", "7" }, 
				new char[] { '1', '2', '3', '4', '5', '6', '7' }
		);
		mainPanel.add(typePanel);
		this.add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * �I�����ꂽ�{�^�����擾����B
	 * @return �I�����ꂽ�{�^���̃A�N�V�����R�}���h
	 */
	public int getSelection() {
		return Integer.parseInt(typePanel.getSelection());
	}

}

/**
 * ���W�I�{�^���̃p�l���𐶐�����B
 * �Q�l�Fcore Java 2 Vol.1 ���X�g 9-16
 */
class ButtonPanel extends JPanel {

	private ButtonGroup group;

	public ButtonPanel(String title, String[] name, String[] command, char[] mnumonic) {
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		group = new ButtonGroup();

		for (int i = 0; i < command.length; i++) {
			JRadioButton b = new JRadioButton(name[i]);
			b.setActionCommand(command[i]);
			b.setMnemonic(mnumonic[i]);
			add(b);
			group.add(b);
			b.setSelected(i == 0);
		}
	}

	public String getSelection() {
		return group.getSelection().getActionCommand();
	}

}
