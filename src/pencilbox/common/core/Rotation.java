package pencilbox.common.core;


/**
 * ���W��]�v�Z�p�⏕�N���X
 */
/*
 * ��育�Ƃɂ���̂��H
 */
public class Rotation {
	
	private int rotation = 0;
	private int ROWS; // Board�̍s��
	private int COLS; // Board�̗�
	private int rows; // Panel�̍s��
	private int cols; // Panel�̗�

	/**
	 * @param size
	 */
	public void setSize(Size size) {
		this.ROWS = size.getRows();
		this.COLS = size.getCols();
		setRotation(rotation);
	}
	/**
	 * Panel�\���̉�]��Ԃ�ݒ肷��
	 * @param i �ݒ肷�鐔�l
	 */
	public void setRotation(int i) {
		rotation = i;
		if (isTransposed()) {
			rows = COLS;
			cols = ROWS;
		} else {
			rows = ROWS;
			cols = COLS;
		}
	}
	/**
	 * ���݂�Panel�\���̉�]��Ԃ��擾����
	 * @return ��]��Ԃ�\�����l
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * �p�l���̏c�������Ƃ̏c���ɑ΂��ē]�u����Ă��邩�ǂ���
	 * @return �]�u����Ă���� true
	 */
	public boolean isTransposed() {
		switch (rotation) {
			case 0 :
			case 2 :
			case 5 :
			case 7 :
				return false;
			case 1 :
			case 3 :
			case 4 :
			case 6 :
				return true;
			default :
				return false;
		}
	}
	/**
	 * �p�l����̐����l���W��Տ�̍��W�ɕϊ�����
	 * @param pos
	 */
	public void p2b(Address pos) {
		switch (rotation) {
			case 0 :
				pos.set(pos.r, pos.c);
				break;
			case 1 :
				pos.set(pos.c, rows - 1 - pos.r);
				break;
			case 2 :
				pos.set(rows - 1 - pos.r, cols - 1 - pos.c);
				break;
			case 3 :
				pos.set(cols - 1 - pos.c, pos.r);
				break;
			case 4 :
				pos.set(pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.r, cols - 1 - pos.c);
				break;
			case 6 :
				pos.set(cols - 1 - pos.c, rows - 1 - pos.r);
				break;
			case 7 :
				pos.set(rows - 1 - pos.r, pos.c);
				break;
		}
	}
	/**
	 * �p�l����̐����l���W�����݂̉�]�\����Ԃɉ������Ֆʂ̍��W�ɕϊ�����
	 * ���̍ۂɁC�ՖʃT�C�Y�� rows()+adjustRow, cols()+adjustCol �ł���Ƃ݂Ȃ�
	 * @param pos ���W
	 * @param adjustRow �Ֆʍs�T�C�Y�ɑ΂���␳�l
	 * @param adjustCol �Ֆʗ�T�C�Y�ɑ΂���␳�l
	 */
	public void p2b(Address pos, int adjustRow, int adjustCol) {
		switch (rotation) {
			case 0 :
				pos.set(pos.r, pos.c);
				break;
			case 1 :
				pos.set(pos.c, rows + adjustRow - 1 - pos.r);
				break;
			case 2 :
				pos.set(rows + adjustRow - 1 - pos.r, cols + adjustCol - 1 - pos.c);
				break;
			case 3 :
				pos.set(cols + adjustCol - 1 - pos.c, pos.r);
				break;
			case 4 :
				pos.set(pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.r, cols + adjustCol - 1 - pos.c);
				break;
			case 6 :
				pos.set(cols + adjustCol - 1 - pos.c, rows + adjustRow - 1 - pos.r);
				break;
			case 7 :
				pos.set(rows + adjustRow - 1 - pos.r, pos.c);
				break;
		}
	}
	/**
	 * �p�l����̐����l�Ӎ��W��Տ�̕Ӎ��W�ɕϊ�����
	 * @param pos
	 */
	public void p2bSide(SideAddress pos) {
		switch (rotation) {
			case 0 :
				pos.set(pos.d, pos.r, pos.c);
				break;
			case 1 :
				pos.set(pos.d ^ 1, pos.c, rows - 1 - pos.d - pos.r);
				break;
			case 2 :
				pos.set(pos.d, rows - 1 - pos.d - pos.r, cols - 1 - (pos.d ^ 1) - pos.c);
				break;
			case 3 :
				pos.set(pos.d ^ 1, cols - 1 - (pos.d ^ 1) - pos.c, pos.r);
				break;
			case 4 :
				pos.set(pos.d ^ 1, pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.d, pos.r, cols - 1 - (pos.d ^ 1) - pos.c);
				break;
			case 6 :
				pos.set(pos.d ^ 1, cols - 1 - (pos.d ^ 1) - pos.c, rows - 1 - pos.d - pos.r);
				break;
			case 7 :
				pos.set(pos.d, rows - 1 - pos.d - pos.r, pos.c);
				break;
		}
	}
	/**
	 * �Ֆʏ�̐����l���W�����݂̉�]�\����Ԃɉ������p�l����̍��W�ɕϊ�����
	 * ���̍ۂɁC�ՖʃT�C�Y�� rows()+adjustRow, cols()+adjustCol �ł���Ƃ݂Ȃ�
	 * @param pos ���W
	 * @param adjustRow �Ֆʍs�T�C�Y�ɑ΂���␳�l
	 * @param adjustCol �Ֆʗ�T�C�Y�ɑ΂���␳�l
	 */
	public void b2p(Address pos, int adjustRow, int adjustCol) {
		switch (rotation) {
			case 0 :
				pos.set(pos.r, pos.c);
				break;
			case 1 :
				pos.set(COLS + adjustCol - 1 - pos.c, pos.r);
				break;
			case 2 :
				pos.set(
					ROWS + adjustRow - 1 - pos.r,
					COLS + adjustCol - 1 - pos.c);
				break;
			case 3 :
				pos.set(pos.c, ROWS + adjustRow - 1 - pos.r);
				break;
			case 4 :
				pos.set(pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.r, COLS + adjustCol - 1 - pos.c);
				break;
			case 6 :
				pos.set(
					COLS + adjustCol - 1 - pos.c,
					ROWS + adjustRow - 1 - pos.r);
				break;
			case 7 :
				pos.set(ROWS + adjustRow - 1 - pos.r, pos.c);
				break;
		}
	}
	/**
	 * �Տ�̍��W���p�l����̐����l���W�ɕϊ�����
	 * @param pos
	 */
	public void b2p(Address pos) {
		switch (rotation) {
			case 0 :
				pos.set(pos.r, pos.c);
				break;
			case 1 :
				pos.set(COLS - 1 - pos.c, pos.r);
				break;
			case 2 :
				pos.set(ROWS - 1 - pos.r, COLS - 1 - pos.c);
				break;
			case 3 :
				pos.set(pos.c, ROWS - 1 - pos.r);
				break;
			case 4 :
				pos.set(pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.r, COLS - 1 - pos.c);
				break;
			case 6 :
				pos.set(COLS - 1 - pos.c, ROWS - 1 - pos.r);
				break;
			case 7 :
				pos.set(ROWS - 1 - pos.r, pos.c);
				break;
		}
	}
	/**
	 * �Տ�̕Ӎ��W���p�l����̐����l�Ӎ��W�ɕϊ�����
	 * @param pos
	 */
	public void b2pSide(SideAddress pos) {
		switch (rotation) {
			case 0 :
				pos.set(pos.d, pos.r, pos.c);
				break;
			case 1 :
				pos.set(pos.d ^ 1, COLS - 1 - (pos.d ^ 1) - pos.c, pos.r);
				break;
			case 2 :
				pos.set(pos.d, ROWS - 1 - pos.d - pos.r, COLS - 1 - (pos.d ^ 1) - pos.c);
				break;
			case 3 :
				pos.set(pos.d ^ 1, pos.c, ROWS - 1 - pos.d - pos.r);
				break;
			case 4 :
				pos.set(pos.d ^ 1, pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.d, pos.r, COLS - 1 - (pos.d ^ 1) - pos.c);
				break;
			case 6 :
				pos.set(pos.d ^ 1, COLS - 1 - (pos.d ^ 1) - pos.c, ROWS - 1 - pos.d - pos.r);
				break;
			case 7 :
				pos.set(pos.d, ROWS - 1 - pos.d - pos.r, pos.c);
				break;
		}
	}
	/**
	 * �Տ�̕������p�l����̕����ɕϊ�����
	 * @param direction �ϊ����̕�����\�����l
	 * @return �ϊ���̕�����\�����l
	 */
	public int rotateDirection(int direction) {
		switch (rotation) {
			case 0 :
			case 1 :
			case 2 :
			case 3 :
				direction = (direction + rotation) % 4;
				break;
			case 4 :
			case 5 :
			case 6 :
			case 7 :
				direction = (direction + rotation) % 4;
				direction = direction ^ 1;
				break;
		}
		return direction;
	}
}
