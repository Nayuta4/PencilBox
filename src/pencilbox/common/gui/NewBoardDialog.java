package pencilbox.common.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import pencilbox.common.core.Size;

/**
 * �u�V�K�쐬�v�_�C�A���O
 * ���[�_���_�C�A���O�Ȃ̂ŁC1�̃C���X�^���X�݂̂𐶐����Ďg����
 */

public class NewBoardDialog extends PencilBoxDialog {

	private static NewBoardDialog instance = new NewBoardDialog();

	/**
	 * NewBoardDialog �̃C���X�^���X���擾����
	 * �R���X�g���N�^�͎g�p�s�ŁC�C���X�^���X�͂��̃��\�b�h�ɂ��擾����
	 * @return NewBoardDialog �C���X�^���X
	 */
	public static NewBoardDialog getInstance() {
		return instance;
	}

	private JPanel mainPanel;
	private JSpinner spinnerRows;
	private JSpinner spinnerCols;
	private JCheckBox checkBox;

	private NewBoardDialog() {
		super();
	}

	protected void makeDialog() {
		super.makeDialog();
		makeMainPanel();
	}
	
	private void makeMainPanel() {
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		spinnerCols = new JSpinner();
		spinnerRows = new JSpinner();
		final SpinnerModel spinnerModelCols = new SpinnerNumberModel(10, 1, 200, 1);
		final SpinnerModel spinnerModelRows = new SpinnerNumberModel(10, 1, 200, 1);
		spinnerCols.setModel(spinnerModelCols);
		spinnerRows.setModel(spinnerModelRows);
		JLabel labelCols = new JLabel(Messages.getString("NewBoardDialog.labelCols")); //$NON-NLS-1$
		JLabel labelRows = new JLabel(Messages.getString("NewBoardDialog.labelRows")); //$NON-NLS-1$
		labelCols.setHorizontalAlignment(SwingConstants.TRAILING);
		labelRows.setHorizontalAlignment(SwingConstants.TRAILING);

		checkBox = new JCheckBox(Messages.getString("NewBoardDialog.checkBoxSquare")); //$NON-NLS-1$
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (checkBox.isSelected()) {
					spinnerRows.setModel(spinnerModelCols);
//					spinnerRows.setEnabled(false);
				} else {
					spinnerModelRows.setValue(spinnerModelCols.getValue());
					spinnerRows.setModel(spinnerModelRows);
//					spinnerRows.setEnabled(true);
				}
			}
		});

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets.top = 2;
		constraints.insets.bottom = 2;
		constraints.insets.left = 0;
		constraints.insets.right = 0;
		constraints.fill = GridBagConstraints.BOTH; 
		
		addComponent(labelCols,   constraints, 0, 0, 1, 1);
		addComponent(spinnerCols, constraints, 1, 0, 1, 1);
		addComponent(labelRows,   constraints, 0, 1, 1, 1);
		addComponent(spinnerRows, constraints, 1, 1, 1, 1);
		addComponent(checkBox,    constraints, 1, 2, 1, 1);
		this.add(mainPanel, BorderLayout.CENTER); 
	}

	private void addComponent(Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		mainPanel.add(c, constraints);
	}

	/**
	 * ���݂̔ՖʃT�C�Y�̒l���_�C�A���O�ɐݒ肷��
	 * @param s ���݂̔ՖʃT�C�Y
	 */
	public void setCurrentSize(Size s) {
		if (s.getCols() != s.getRows())
			checkBox.setSelected(false);
		spinnerCols.setValue(Integer.valueOf(s.getCols()));
		spinnerRows.setValue(Integer.valueOf(s.getRows()));
	}

	/**
	 * �_�C�A���O�ɓ��͂��ꂽ�T�C�Y���擾����
	 * @return �_�C�A���O�ɓ��͂��ꂽ�T�C�Y
	 */
	public Size getNewSize() {
		int rows = ((Number) spinnerRows.getValue()).intValue();
		int cols = ((Number) spinnerCols.getValue()).intValue();
		return new Size(rows, cols);
	}

}
