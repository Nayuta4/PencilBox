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
 * 「盤面回転」ダイアログ
 * モーダルダイアログなので，1つのインスタンスのみを生成して使い回す
 */

public class RotateBoardDialog extends PencilBoxDialog {

	private static RotateBoardDialog instance = new RotateBoardDialog();
	/**
	 * RotateBoardDialog のインスタンスを取得する
	 * コンストラクタは使用不可で，インスタンスはこのメソッドにより取得する
	 * @return RotateBoardDialog インスタンス
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
	 * 選択されたボタンを取得する。
	 * @return 選択されたボタンのアクションコマンド
	 */
	public int getSelection() {
		return Integer.parseInt(typePanel.getSelection());
	}

}

/**
 * ラジオボタンのパネルを生成する。
 * 参考：core Java 2 Vol.1 リスト 9-16
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
