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
import pencilbox.resource.Messages;

/**
 * 「新規作成」ダイアログ
 * モーダルダイアログなので，1つのインスタンスのみを生成して使い回す
 */

public class NewBoardDialog extends PencilBoxDialog {

	private static NewBoardDialog instance = new NewBoardDialog();

	/**
	 * NewBoardDialog のインスタンスを取得する
	 * コンストラクタは使用不可で，インスタンスはこのメソッドにより取得する
	 * @return NewBoardDialog インスタンス
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
	 * 現在の盤面サイズの値をダイアログに設定する
	 * @param s 現在の盤面サイズ
	 */
	public void setCurrentSize(Size s) {
		if (s.getCols() != s.getRows())
			checkBox.setSelected(false);
		spinnerCols.setValue(Integer.valueOf(s.getCols()));
		spinnerRows.setValue(Integer.valueOf(s.getRows()));
	}

	/**
	 * ダイアログに入力されたサイズを取得する
	 * @return ダイアログに入力されたサイズ
	 */
	public Size getNewSize() {
		int rows = ((Number) spinnerRows.getValue()).intValue();
		int cols = ((Number) spinnerCols.getValue()).intValue();
		return new Size(rows, cols);
	}

}
