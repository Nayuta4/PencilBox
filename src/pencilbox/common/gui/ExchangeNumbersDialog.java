package pencilbox.common.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import pencilbox.common.core.BoardBase;
import pencilbox.resource.Messages;

/**
 * �u���������v�_�C�A���O
 * ���[�_���_�C�A���O�Ȃ̂ŁC1�̃C���X�^���X�݂̂𐶐����Ďg����
 */

public class ExchangeNumbersDialog extends PencilBoxDialog {

	private static ExchangeNumbersDialog instance = new ExchangeNumbersDialog();

	/**
	 * ExchangeNumberDialog �̃C���X�^���X���擾����
	 * �R���X�g���N�^�͎g�p�s�ŁC�C���X�^���X�͂��̃��\�b�h�ɂ��擾����
	 * @return ExchangeNumberDialog �C���X�^���X
	 */
	public static ExchangeNumbersDialog getInstance() {
		return instance;
	}

	private JPanel mainPanel;
	private JSpinner[] spinner; // = new JSpinner[2];
	private SpinnerModel[] spinnerModel; // = new SpinnerModel[2];
	private JButton buttonUpdate;

	private ExchangeNumbersDialog() {
		super();
	}

	protected void makeDialog() {
	    setDialogType(OK_ONLY);
		super.makeDialog();
		makeMainPanel();
	}

	private void makeMainPanel() {

		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		spinner = new JSpinner[2];
		spinnerModel = new SpinnerModel[2];
		for (int i=0; i<2; i++) {
			spinner[i] = new JSpinner();
			spinnerModel[i] = new SpinnerNumberModel(1, 1, 99, 1);
			spinner[i].setModel(spinnerModel[i]);
		}
		JLabel label1 = new JLabel(Messages.getString("ExchangeNumbersDialog.label1"));
		JLabel label2 = new JLabel(Messages.getString("ExchangeNumbersDialog.label2"));
//		label1.setHorizontalAlignment(SwingConstants.TRAILING);
//		label2.setHorizontalAlignment(SwingConstants.TRAILING);

		buttonUpdate = new JButton(Messages.getString("ExchangeNumbersDialog.buttonUpdate"));
		buttonUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int v0 = ((Number) spinner[0].getValue()).intValue();
				int v1 = ((Number) spinner[1].getValue()).intValue();
				board.exchangeNumbers(v0, v1);
				panel.repaint();
			}
		});

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets.top = 2;
		constraints.insets.bottom = 2;
		constraints.insets.left = 0;
		constraints.insets.right = 0;
		constraints.fill = GridBagConstraints.BOTH;

		addComponent(label1,   constraints, 0, 0, 1, 1);
		addComponent(spinner[0], constraints, 1, 0, 1, 1);
		addComponent(label2,   constraints, 0, 1, 1, 1);
		addComponent(spinner[1], constraints, 1, 1, 1, 1);
		addComponent(buttonUpdate,    constraints, 1, 2, 1, 1);
		this.add(mainPanel, BorderLayout.CENTER);
	}

	private void addComponent(Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		mainPanel.add(c, constraints);
	}

	public void setup(BoardBase b, PanelBase p) {
		this.board = b;
		this.panel = p;
	}

	/**
	 * �I���\�����̏���C������ݒ肷��
	 * @param min ����
	 * @param max ���
	 */
	public void setRange(int min, int max) {
		for (int i=0; i<2; i++) {
			SpinnerNumberModel model = (SpinnerNumberModel)spinnerModel[i];
			model.setMinimum(min);
			model.setMaximum(max);
			int v = model.getNumber().intValue();
			if (v < min || v > max)
				model.setValue(min);
		}
	}

	private BoardBase board;
	private PanelBase panel;
}
