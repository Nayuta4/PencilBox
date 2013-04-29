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
	 * @param p coordinate
	 * @return Returns the pattern.
	 */
	int getPattern(Address p) {
		return pattern[p.r()][p.c()];
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
		for (Address p : board.cellAddrs()) {
			Area area = board.getArea(p);
			if (area != null)
				pattern[p.r()][p.c()] = getAllDigitPattern(board.getArea(p).size());
			else
				pattern[p.r()][p.c()] = getAllDigitPattern(maxNumber);
		}
		for (Address p : board.cellAddrs()) {
			int n = board.getNumberOrState(p);
			if (n > 0)
				checkUsedNumber(p, n);
		}
	}
	/**
	 * 1 ���� number �܂ł̂��ׂĂ̐������\�Ƃ��� digitPattern ��Ԃ�
	 * @param number �ő�̐���
	 * @return 1 ���� number �܂ł̂��ׂĂ̐������\�Ƃ��� digitPattern ��Ԃ�
	 */
	private static int getAllDigitPattern(int number) {
		return ~((-1 << (number+1))+1);
	}

	/**
	 * �w�肵�����W�Ɏw�肵��������z�u���Ă����[���Ɉᔽ���Ȃ����ǂ���
	 * @param p ���W
	 * @param n �z�u�\���𒲂ׂ鐔��
	 * @return �z�u�\�Ȃ� true �z�u�s�Ȃ� false
	 */
	boolean canPlace(Address p, int n) {
		return (pattern[p.r()][p.c()] & (1<<n)) > 0;
	}
	/**
	 * p0 �� ���� num �������Ă��邱�Ƃɂ��C
	 * �z�u�s�\�ƂȂ鐔�����\�p�^�[������O��
	 * @param p0
	 * @param n
	 */
	void checkUsedNumber(Address p0, int n) {
		int pat = ~((1 << n) + 1);  // num���ȊO��1
		for (int d = 0; d < 4; d++) {
			Address p = p0;
			for (int k = 0; k < n; k++) {
				p = Address.nextCell(p, d);
				if (board.isOn(p))
					pattern[p.r()][p.c()] &= pat;
			}
		}
		Area area = board.getArea(p0);
		if (area != null) {
			for (Address pos : board.getArea(p0)) {
				pattern[pos.r()][pos.c()] &= pat;
			}
		}
	}
}
