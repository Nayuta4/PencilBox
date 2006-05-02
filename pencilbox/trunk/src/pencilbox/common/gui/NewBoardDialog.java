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
 * �u�V�K�v�_�C�A���O
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
		freeSizePanel.add(new JLabel("���R"));
		freeSizePanel.add(fieldCols);
		freeSizePanel.add(new JLabel("�^�e"));
		freeSizePanel.add(fieldRows);
		this.add(freeSizePanel, BorderLayout.CENTER); 
	}

	/**
	 * �p�Y���̎�ނ�ݒ肷��
	 * @param pencilType �p�Y���̎��
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
	 * ���݂̔ՖʃT�C�Y�̒l���C�_�C�A���O�̎��R�ݒ�T�C�Y�̃t�B�[���h�ɒl��ݒ肷��
	 * @param s ���݂̔ՖʃT�C�Y
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
	 * �_�C�A���O�őI�����ꂽ�T�C�Y���擾����
	 * @return �_�C�A���O�őI�����ꂽ�T�C�Y
	 */
	public Size getNewSize() {
		return newSize;
	}
	
}
