package pencilbox.common.gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pencilbox.common.core.Size;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;


/**
 * 「新規」ダイアログ
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
	
	private JPanel freeSizePanel;
	private JTextField fieldRows;
	private JTextField fieldCols;

//	private boolean squareOnly = false;
	private boolean freeSize = true;
	private Size newSize;

	private NewBoardDialog() {
		super();
	}

	protected void makeDialog() {
		super.makeDialog();
		makeFreeSizePanel();
	}
	
	private void makeFreeSizePanel() {
		freeSizePanel = new JPanel();
		freeSizePanel.setLayout(new FlowLayout());
		fieldCols = new JTextField(3);
		fieldRows = new JTextField(3);
		freeSizePanel.add(new JLabel("ヨコ"));
		freeSizePanel.add(fieldCols);
		freeSizePanel.add(new JLabel("タテ"));
		freeSizePanel.add(fieldRows);
		this.add(freeSizePanel, BorderLayout.CENTER); 
	}

	/**
	 * パズルの種類を設定する
	 * @param pencilType パズルの種類
	 * @throws PencilBoxClassException
	 */
	public void setPencilType(PencilType pencilType) throws PencilBoxClassException {
//		PuzzleCommon puzzleCommon = (PuzzleCommon) ClassUtil.createInstance(pencilType , "Puzzle");
//		squareOnly = puzzleCommon.isSquarePuzzle();
//		if (squareOnly) {
//			fieldRows.setEditable(false);
//			fieldRows.setFocusable(false);
//		} else {
//			fieldRows.setEditable(true);
//			fieldRows.setFocusable(true);
//		}
	}
	/**
	 * 現在の盤面サイズの値を，ダイアログの自由設定サイズのフィールドに値を設定する
	 * @param s 現在の盤面サイズ
	 */
	public void setCurrentSize(Size s) {
		newSize = null;
		if (freeSize) {
			fieldCols.setText(Integer.toString(s.getCols()));
			fieldCols.selectAll();
			fieldRows.setText(Integer.toString(s.getRows()));
			fieldRows.selectAll();
		}
	}

	protected void setInitialFocus() {
		fieldCols.requestFocusInWindow();
	}
	
	protected void accept() {
		try {
			int rows = Integer.parseInt(fieldRows.getText());
			int cols = Integer.parseInt(fieldCols.getText());
//			if (squareOnly) {
//				rows = cols;
//			}
			newSize = new Size(rows, cols);
		} catch (NumberFormatException e) {
			showErrorMessage(e.toString());
		} finally {
			super.accept();
		}
	}
	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this,message,"Error",JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * ダイアログで選択されたサイズを取得する
	 * @return ダイアログで選択されたサイズ
	 */
	public Size getNewSize() {
		return newSize;
	}
	
}
