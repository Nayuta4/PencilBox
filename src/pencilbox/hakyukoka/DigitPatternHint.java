package pencilbox.hakyukoka;

import pencilbox.common.core.Address;


/**
 * �\�������r�b�g�p�^�[���ŕ\������q���g�@�\
 */
public class DigitPatternHint {

	private Board board;
	private int[][] pattern;
	private int maxNumber = 9;  // �b��
	
	/**
	 * @param r row coordinate
	 * @param c colmun coordinate
	 * @return Returns the pattern.
	 */
	int getPattern(int r, int c) {
		return pattern[r][c];
	}

	/**
	 * �N���X�̏������������s��
	 * �Ֆʐ������ɌĂ΂��
	 * @param board �֘A�t����Ֆ�
	 */
	void setupHint(Board board) {
		this.board = board;
		pattern = new int[board.rows()][board.cols()];
	}
	/**
	 * �p�^�[����update
	 */
	void initHint() {
		int rows = board.rows();
		int cols = board.cols();
		Area area;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				area = board.getArea(r,c);
				if (area != null)
					pattern[r][c] = getAllDigitPattern(board.getArea(r,c).size());
				else
					pattern[r][c] = getAllDigitPattern(maxNumber);
			}
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				int n = board.getNumber(r,c);
				if (n > 0)
					checkUsedNumber(r, c, n);
			}
		}
	}
	/**
	 * 1 ���� number �܂ł̂��ׂĂ̐������\�Ƃ��� digitPattern ��Ԃ�
	 * @param number �ő�̐���
	 * @return 1 ���� number �܂ł̂��ׂĂ̐������\�Ƃ��� digitPattern ��Ԃ�
	 */
	private int getAllDigitPattern(int number) {
		return ~((-1 << (number+1))+1);
	}
	
	/**
	 * �w�肵�����W�Ɏw�肵��������z�u���Ă����[���Ɉᔽ���Ȃ����ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @param n �z�u�\���𒲂ׂ鐔��
	 * @return �z�u�\�Ȃ� true �z�u�s�Ȃ� false
	 */
	boolean canPlace(int r, int c, int n) {
		return (pattern[r][c] & (1<<n)) > 0;
	}
	/**
	 * r0�s c0�� �� ���� num �������Ă��邱�Ƃɂ��C
	 * �z�u�s�\�ƂȂ鐔�����\�p�^�[������O��
	 * @param r0
	 * @param c0
	 * @param n
	 */
	void checkUsedNumber(int r0, int c0, int n) {
		int pat = ~((1 << n) + 1);  // num���ȊO��1
		for (int c = c0-n; c <= c0+n; c++) {
			if (board.isOn(r0,c))
				pattern[r0][c] &= pat;
		}
		for (int r = r0-n; r <= r0+n; r++) {
			if (board.isOn(r,c0))
				pattern[r][c0] &= pat;
		}
		Area area = board.getArea(r0,c0);
		if (area != null) {
			for (Address pos : board.getArea(r0,c0)) {
				pattern[pos.r()][pos.c()] &= pat;
			}
		}
	}
}
