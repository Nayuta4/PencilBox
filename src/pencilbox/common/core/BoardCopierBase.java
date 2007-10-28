package pencilbox.common.core;

import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;


public class BoardCopierBase {
	
	/**
	 * �Ֆʂ𕡐�����B
	 * @param src �������̔Ֆ�
	 * @return ���������Ֆ�
	 */
	public BoardBase duplicateBoard(BoardBase src) throws PencilBoxClassException {
		return duplicateBoard(src, 0);
	}
	/**
	 * �Ֆʂ���]���ĕ�������B
	 * @param src �������̔Ֆ�
	 * @return ���������Ֆ�
	 */
	public BoardBase duplicateBoard(BoardBase src, int n) throws PencilBoxClassException {
		BoardBase dst = (BoardBase) ClassUtil.createInstance(this.getClass(), ClassUtil.BOARD_CLASS);
		if (Rotator.isTransposed(n)){
			dst.setSize(new Size(src.cols(), src.rows()));
		} else {
			dst.setSize(new Size(src.rows(), src.cols()));
		}
		copyBoardStates(src, dst, n);
		return dst;
	}
	
	/**
	 * �Ֆʂ��T�C�Y�ύX���ĕ�������B
	 * @param src �������̔Ֆ�
	 * @param size �ύX��̃T�C�Y
	 * @return ���������Ֆ�
	 * @throws PencilBoxClassException
	 */
	public BoardBase duplicateBoard(BoardBase src, Size size) throws PencilBoxClassException {
		BoardBase dst = (BoardBase) ClassUtil.createInstance(this.getClass(), ClassUtil.BOARD_CLASS);
		dst.setSize(size);
		copyBoardStates(src, dst, 0);
		return dst;
	}
	
	/**
	 * �Ֆʏ�Ԃ���]���ĕ�������B
	 * �e�T�u�N���X�Ŏ�������B
	 * @param src �������̔Ֆ�
	 * @param dst ������̔Ֆ�
	 * @param n ��]�ԍ�
	 */
	protected void copyBoardStates(BoardBase src, BoardBase dst, int n) {
	}

	public void copyRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
	}
	
	public void moveRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
	}

	public void eraseRegion(BoardBase board, Area region) {
	}
	
}
