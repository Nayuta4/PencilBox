package pencilbox.common.io;

import java.io.IOException;
import java.io.Reader;

import pencilbox.common.core.BoardBase;


/**
 * �e�L�X�g�`���̔ՖʓǍ��p�N���X
 */
public abstract class TxtReaderBase {
	
	/**
	 * �e�L�X�g�`���̔Ֆʂ�ǂݍ���
	 * @param in ����
	 * @return �ǂݍ���ō쐬�����Ֆ�
	 * @throws IOException
	 */
	public abstract BoardBase readProblem(Reader in) throws IOException;

}
