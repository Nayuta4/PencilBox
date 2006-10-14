package pencilbox.common.core;

import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;


public class ProblemCopierBase {
	
	/**
	 * ���𕡐�����B
	 * @param src �������̔Ֆ�
	 * @return ���������Ֆ�
	 */
	public Problem duplicateProblem(Problem src) throws PencilBoxClassException {
		Problem dst = new Problem();
		dst.setBoard(duplicateBoard(src.getBoard()));
		dst.setProperty(new Property(src.getProperty()));
		dst.setFile(src.getFile());
		return dst;
	}

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
	 * �Ֆʏ�Ԃ���]���ĕ�������B
	 * �e�T�u�N���X�Ŏ�������B
	 * @param src �������̔Ֆ�
	 * @param dst ������̔Ֆ�
	 * @param n ��]�ԍ�
	 */
	protected void copyBoardStates(BoardBase src, BoardBase dst, int n) {
	}

}