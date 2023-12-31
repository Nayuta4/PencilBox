package pencilbox.common.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pencilbox.resource.Messages;

/**
 * 座標表示文字を設定するダイアログ。
 * モーダルダイアログなので，1つのインスタンスのみを生成して使い回す。
 */
public class IndexLettersDialog extends PencilBoxDialog {

	private static IndexLettersDialog instance = new IndexLettersDialog();

	/**
	 * ダイアログのインスタンスを取得する
	 * コンストラクタは使用不可で，インスタンスはこのメソッドにより取得する
	 * @return 
	 */
	public static IndexLettersDialog getInstance() {
		return instance;
	}

	private JPanel mainPanel;
	private JCheckBox checkBox;
	private JComboBox[] comboBox;

    private PanelBase panel;

	private IndexLettersDialog() {
		super();
	}

	protected void makeDialog() {
	    setDialogType(OK_ONLY);
		super.makeDialog();
		makeMainPanel();
	}

    public void setPanel(PanelBase p) {
    	panel = p;
    	checkBox.setSelected(p.isIndexMode());
		for (int i=0; i<2; i++) {
			comboBox[i].setSelectedItem(IndexLetters.getIndexLetters(p.getIndexStyle(i)));
			comboBox[i].setEnabled(checkBox.isSelected());
		}
    }

	private void makeMainPanel() {

		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel labelCols = new JLabel(Messages.getString("IndexLettersDialog.labelCols")); //$NON-NLS-1$
		JLabel labelRows = new JLabel(Messages.getString("IndexLettersDialog.labelRows")); //$NON-NLS-1$
		labelCols.setHorizontalAlignment(SwingConstants.TRAILING);
		labelRows.setHorizontalAlignment(SwingConstants.TRAILING);

		checkBox = new JCheckBox(Messages.getString("IndexLettersDialog.checkBox")); //$NON-NLS-1$
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				panel.changeIndexMode(checkBox.isSelected());
				panel.repaint();
				for (int i=0; i<2; i++) {
					comboBox[i].setEnabled(checkBox.isSelected());
				}
			}
		});

		comboBox = new JComboBox[2];
		for (int i=0; i<2; i++) {
			comboBox[i] = new JComboBox();
			for (IndexLetters letters : IndexLetters.INDEX_LETTERS_LIST) {
				comboBox[i].addItem(letters);
			}
			final int ii = i;
			comboBox[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panel.setIndexStyle(ii, ((IndexLetters)(comboBox[ii].getSelectedItem())).getStyle() );
					panel.repaint();
				}
			});
		}

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets.top = 2;
		constraints.insets.bottom = 2;
		constraints.insets.left = 0;
		constraints.insets.right = 0;
		constraints.fill = GridBagConstraints.BOTH;

		addComponent(checkBox,    constraints, 1, 0, 1, 1);
		addComponent(labelCols,   constraints, 0, 1, 1, 1);
		addComponent(comboBox[0], constraints, 1, 1, 1, 1);
		addComponent(labelRows,   constraints, 0, 2, 1, 1);
		addComponent(comboBox[1], constraints, 1, 2, 1, 1);
		this.add(mainPanel, BorderLayout.CENTER);
	}

	private void addComponent(Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		mainPanel.add(c, constraints);
	}

	protected void accept() {
		super.accept();
	}

}
