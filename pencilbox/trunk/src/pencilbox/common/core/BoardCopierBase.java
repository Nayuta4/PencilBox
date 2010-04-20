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
		if (Rotator2.isTransposed(n)){
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
	 * �ՖʑO�̈��ΏۂƂ������ʂ��s���B
	 * ���̂悤�Ɉ����Ȃ�����ȏꍇ�i�J�b�N���Ȃǁj�ɂ��ẮC�e�T�u�N���X�Ŏ�������B
	 * @param src �������̔Ֆ�
	 * @param dst ������̔Ֆ�
	 * @param n ��]�ԍ�
	 */
	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Address from = Address.address(0, 0);
		Address to = this.rotateCornerAddress(src.rows(), src.cols(), n);
		Area region = src.getWholeBoardArea();
		copyRegion(src, dst, region, from, to, n);
	}

	/**
	 * �Ֆʉ�]���̍���p���W�̍s����̍��W�����߂�
	 * @param rows ���Ֆʂ̍s��
	 * @param cols ���Ֆʂ̗�
	 * @param rotation ��]�ԍ�
	 * @return
	 */
	private Address rotateCornerAddress(int rows, int cols, int rotation) {
		int R = 0;
		int C = 0;
		switch (rotation) {
			case 0 :
				R = 0;
				C = 0;
				break;
			case 1 :
				R = cols - 1;
				C = 0;
				break;
			case 2 :
				R = rows - 1;
				C = cols - 1;
				break;
			case 3 :
				R = 0;
				C = rows - 1;
				break;
			case 4 :
				R = 0;
				C = 0;
				break;
			case 5 :
				R = 0;
				C = cols - 1;
				break;
			case 6 :
				R = cols - 1;
				C = rows - 1;
				break;
			case 7 :
				R = rows - 1;
				C = 0;
				break;
		}
		return Address.address(R, C);
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


	protected Address translateAndRotateAddress(Address pos, Address from, Address to, int rotation) {
		return Rotator2.translateAndRotateAddress(pos, from, to, rotation);
	}

}
