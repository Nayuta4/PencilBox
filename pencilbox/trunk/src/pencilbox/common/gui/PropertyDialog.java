package pencilbox.common.gui;

import java.awt.*;

import javax.swing.*;

import pencilbox.common.core.Property;
import pencilbox.resource.Messages;


/**
 * プロパティ表示，編集用ダイアログ
 * モーダルダイアログなので，1つのインスタンスのみを生成して使い回す
 */
public class PropertyDialog extends PencilBoxDialog {

	private static PropertyDialog instance = new PropertyDialog();

	/**
	 * PropertyDialog のインスタンスを取得する
	 * @return PropertyDialog インスタンス
	 */
	public static PropertyDialog getInstance() {
		return instance;
	}

	private JPanel mainPanel;
	private JTextField fieldAuthor;
	private JTextField fieldDifficulty;
	private JTextField fieldSource;

	private PropertyDialog() {
		super();
	}

	protected void makeDialog() {
		super.makeDialog();
		makeMainPanel();
	}

	private void makeMainPanel() {

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel labelAuthor = new JLabel(Messages.getString("PropertyDialog.labelAuthor")); //$NON-NLS-1$
		JLabel labelDifficulty = new JLabel(Messages.getString("PropertyDialog.labelDifficulty")); //$NON-NLS-1$
		JLabel labelSource = new JLabel(Messages.getString("PropertyDialog.labelSource")); //$NON-NLS-1$

		fieldAuthor = new JTextField(12);
		fieldDifficulty = new JTextField(12);
		fieldSource = new JTextField(12);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets.top = 2;
		constraints.insets.bottom = 2;
		constraints.insets.left = 2;
		constraints.insets.right = 2;

		addComponent(labelAuthor, constraints, 0, 0, 1, 1);
		addComponent(fieldAuthor, constraints, 1, 0, 3, 1);
		addComponent(labelDifficulty, constraints, 0, 1, 1, 1);
		addComponent(fieldDifficulty, constraints, 1, 1, 3, 1);
		addComponent(labelSource, constraints, 0, 2, 1, 1);
		addComponent(fieldSource, constraints, 1, 2, 3, 1);

		this.add(mainPanel, BorderLayout.NORTH);
	}

	private void addComponent(Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		mainPanel.add(c, constraints);
	}
	/**
	 * ダイアログの各TextFieldに現在のPropertyの内容を設定する。
	 * @param p Property
	 */
	public void setPropertyToDialog(Property p) {
		fieldAuthor.setText(p.getAuthor());
		fieldDifficulty.setText(p.getDifficulty());
		fieldSource.setText(p.getSource());
	}

	/**
	 * ダイアログの各TextFieldからPropertyを読み取る
	 * @param p Property
	 */
	public void getPropertyFromDialog(Property p) {
		p.setAuthor(fieldAuthor.getText());
		p.setDifficulty(fieldDifficulty.getText());
		p.setSource(fieldSource.getText());
	}

	protected void setInitialFocus() {
		fieldAuthor.requestFocusInWindow();
	}

	protected void accept(){
		super.accept();
	}
}