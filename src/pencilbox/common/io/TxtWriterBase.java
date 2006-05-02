package pencilbox.common.io;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;


/**
 * �e�L�X�g�`���̔Ֆʏ����o������p�N���X
 */
public abstract class TxtWriterBase {
	
	/**
	 * �e�L�X�g�`���ŔՖʂ������o��
	 * @param out �o��
	 * @param board �����o���Ֆ�
	 */
	public abstract void writeProblem(PrintWriter out, BoardBase board);
	
}
