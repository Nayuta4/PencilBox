package pencilbox.common.io;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;


/**
 * �e�L�X�g�`���̔Ֆʏ����o������p�N���X
 */
public abstract class TxtWriterBase {
	
	public static int ALL = 0;
	public static int QUESTION_ONLY = 1;
	
	/**
	 * �e�L�X�g�`���ŔՖʂ������o��
	 * @param out �o��
	 * @param board �����o���Ֆ�
	 */
	public void writeProblem(PrintWriter out, BoardBase board) {
		writeProblem(out, board, ALL);
	}
	/**
	 * �e�L�X�g�`���ŔՖʂ������o���B
	 * �񓚏�Ԃ͖������āC���f�[�^�̂ݏ����o��
	 * @param out �o��
	 * @param board �����o���Ֆ�
	 */
	public void writeQuestion(PrintWriter out, BoardBase board) {
		writeProblem(out, board, QUESTION_ONLY);
	}
	/**
	 * �e�L�X�g�`���ŔՖʂ������o��
	 * @param out �o��
	 * @param board �����o���Ֆ�
	 * @param mode �����o�����[�h�i���f�[�^�݂̂��C�񓚃f�[�^���܂ނ��j
	 */
	public abstract void writeProblem(PrintWriter out, BoardBase board, int mode);
	
}
