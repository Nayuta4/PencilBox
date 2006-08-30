package pencilbox.common.gui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * カスタムダイアログ用の共通スーパークラス。
 * 下部に「了解」「取消」ボタンを配置し，「了解」ボタンをデフォルトボタンとする。
 * JDialogを作成してその中に入れて表示し，閉じられたときに選択された結果を返す。
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
	
    /** [了解]ボタンを備えたダイアログ，今のところない */
    public static final int OK_ONLY = 0;
    /** [了解][取消]ボタンを備えたダイアログ */
	public static final int OK_CANCEL = 1;
	
	private int dialogType = OK_CANCEL;
	private JPanel buttonPanel;
	private JButton	buttonOk;
	private JButton buttonCancel;
	private JDialog dialog;
	private int ret = CLOSED_OPTION; // ダイアログの返り値

	/**
	 * ダイアログを作成する
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
		buttonOk = new JButton("了解");
		buttonCancel = new JButton("取消");
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
	 * ダイアログタイプを設定する。今のところ不使用
	 * @param dialogType The dialogType to set.
	 */
	public void setDialogType(int dialogType) {
		this.dialogType = dialogType;
	}

	/**
	 * ESC キーでダイアログを閉じる
	 */
	private void assignKeys() {
		InputMap imap = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
		ActionMap amap = this.getActionMap();
		amap.put("close", new AbstractAction() {	
			public void actionPerformed(ActionEvent e) {
				if (dialog != null) { // 念のため
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
	 * ダイアログを表示する。
	 * 毎回JDialogを作り直して，PencilBoxPaneをJDialogの中に入れて表示する。
	 * @param parent 親フレーム
	 * @param title タイトル文字列
	 * @return ユーザーの選択した結果
	 */
//	参考 Core JAVA Vol.1 list 9-18
	public int showDialog(Component parent, String title) {
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame)parent;
		else
			owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
//      必要に応じて作り直す方式でフォーカスを毎回設定し直すやり方が分からないので，毎回作り直す
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
	 * 各Dialog種類ごとに，初期フォーカスを設定する。
	 */
	protected void setInitialFocus() {
	}
}
	
