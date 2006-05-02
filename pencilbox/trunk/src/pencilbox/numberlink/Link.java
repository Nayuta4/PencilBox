package pencilbox.numberlink;

import java.util.Collection;

/**
 * �u�i���o�[�����N�v�����N�N���X
 */
public class Link extends pencilbox.common.core.Link {
	
	private int number;
	
	/**
	 * �����N���쐬����
	 * �����̏����l�� 0
	 */
	public Link() {
		super();
		number = 0;
	}

	public void clear() {
		super.clear();
		number = 0;
	}

	public boolean addAll(Collection c) {
		setNumber(((Link) c).number);
		return super.addAll(c);
	}

	/**
	 * �F�����p�� Link �̏�Ԃ�Ԃ�
	 * @return
	 * 1��ނ̐����ɂ݂̂Ȃ����Ă���΂��̐�����Ԃ�
	 * �����ɂȂ����Ă��Ȃ���� 0 ��Ԃ�
	 * �����̐����ɂȂ����Ă���� -1 ��Ԃ�
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * �F�����p�� Link �̐�����ݒ肷��
	 * @param i �ݒ肷�鐔��
	 */
	public void setNumber(int i) {
		if (number == 0)
			number = i;
		else if (number == i)
			;
		else if (number > 0 && i > 0)
			number = -1;
	}

}
