package pencilbox.sudoku;

import pencilbox.common.core.Address;

/**
 * �\�������r�b�g�p�^�[���ŕ\������q���g�@�\
 */
public class DigitPatternHint {

	private int allDigitPattern = 0x3fe;
	private Board board;
	private int[][] pattern;

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
		int maxNumber = board.rows();
		allDigitPattern = ~((-1 << (maxNumber+1))+1);
	}
	/**
	 * �ՖʑS�̂̉\�p�^�[�����Čv�Z����
	 */
	void initHint() {
		for (Address p : board.cellAddrs()) {
			pattern[p.r()][p.c()] = allDigitPattern;
		}
		for (Address p : board.cellAddrs()) {
			int n = board.getNumberOrState(p);
			if (n > 0)
				checkUsedNumber(p, n);
		}
	}

	boolean canPlace(Address p, int n) {
		return (pattern[p.r()][p.c()] & (1<<n)) > 0;
	}
	/**
	 * p0�Ɠ����s�C��C�{�b�N�X�ɂ��āC����n���g�p�ς݂Ƃ���
	 * @param p0
	 * @param n
	 */
	void checkUsedNumber(Address p0, int n) {
		int unit = board.getUnit();
		int pat = ~(1 << n);
		for (int cc = 0; cc < board.cols(); cc++) {
			pattern[p0.r()][cc] &= pat;
		}
		for (int rr = 0; rr < board.rows(); rr++) {
			pattern[rr][p0.c()] &= pat;
		}
		int boxR = p0.r() / unit * unit;
		int boxC = p0.c() / unit * unit;
		for (int rr = 0; rr < unit; rr++) {
			for (int cc = 0; cc < unit; cc++) {
				pattern[boxR + rr][boxC + cc] &= pat;
			}
		}
	}
	/**
	 *	p0��n������Ƃ��ɕω����������Ƃ���hint���X�V����
	 * @param p0
	 * @param n
	 */
	void updateHint(Address p0, int n) {
		int prev = board.getNumberOrState(p0);
		if (prev > 0) {
			deleteHint(p0, prev);
		}
		if (n > 0) {
			checkUsedNumber(p0, n);
		}
	}
	/**
	 *	p0�ɓ����Ă�������n0�������Ƃ��ɁC
	 * �ՖʑS�̂̉\�p�^�[���ɂ��āC���� n �Ɋւ��镔���݂̂��Čv�Z����
	 * @param p0
	 * @param n0
	 */
	void deleteHint(Address p0, int n0) {
		int pat = (1 << n0);
		for (Address p : board.cellAddrs()) {
			pattern[p.r()][p.c()] |= pat;
		}
		for (Address p : board.cellAddrs()) {
			int n = board.getNumberOrState(p);
			if (n == n0) {
				if (p.equals(p0)) continue; // ���̃}�X��n0��������������Ƃ���
				checkUsedNumber(p, n);
			}
		}
	}
}
