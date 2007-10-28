package pencilbox.common.core;

import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;


/**
 * �Ֆʕ����N���X�̐e�N���X
 */
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

	/**
	 * �̈�����B
	 * ��̓I�ȏ����̓T�u�N���X�ŋL�q����B
	 * @param board �ҏW����Ֆ�
	 * @param region �����̈�
	 */
	public void eraseRegion(BoardBase board, Area region) {
	}

	/**
	 * �̈敡�ʁB
	 * ��̓I�ȏ����̓T�u�N���X�ŋL�q����B
	 * @param srcBoard ���ʌ��Ֆ�
	 * @param board ���ʐ�Ֆ�
	 * @param region ���ʌ��̈�
	 * @param from ���ʌ��̈挴�_
	 * @param to ���_�̕��ʐ�
	 * @param rotation ��]
	 */
	public void copyRegion(BoardBase srcBoard, BoardBase board, Area region, Address from, Address to, int rotation) {
	}

	/**
	 * �̈敡�ʁB
	 * ���߂ɔՖʑS�̂𕡐����Ă��畡�ʏ������s���B
	 * @param board �ҏW����Ֆ�
	 * @param region ���ʌ��̈�
	 * @param from ���ʌ��̈挴�_
	 * @param to ���_�̕��ʐ�
	 * @param rotation ��]
	 */
	public void copyRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
		try {
			BoardBase srcBoard = duplicateBoard(board);
			copyRegion(srcBoard, board, region, from, to, rotation);
		} catch (PencilBoxClassException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �̈�ړ��B
	 * ���߂ɔՖʑS�̂𕡐����Ă��畡�ʏ������s���C�ړ���̗̈����������B
	 * @param board �ҏW����Ֆ�
	 * @param region �ړ����̈�
	 * @param from �ړ����̈挴�_
	 * @param to ���_�̈ړ���
	 * @param rotation ��]
	 */
	public void moveRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
		try {
			BoardBase srcBoard = duplicateBoard(board);
			eraseRegion(board, region);
			copyRegion(srcBoard, board, region, from, to, rotation);
		} catch (PencilBoxClassException e) {
			e.printStackTrace();
		}
	}

}
