package pencilbox.kakuro;

import pencilbox.common.core.Address;
import pencilbox.common.core.Direction;

/**
 * �\�������r�b�g�p�^�[���ŕ\������q���g�@�\
 */
public class DigitPatternHint {

	private Board board;
	private int[][] horizPattern;
	private int[][] vertPattern;

	/**
	 * ���̃}�X�ɔz�u���Ē��ڂ̃��[���ᔽ�ɂȂ�Ȃ������̃r�b�g�p�^�[����Ԃ��B
	 * @param p �}�X�̍��W
	 * @return �\�ȃr�b�g�p�^�[����Ԃ��B���������̃}�X���ǃ}�X�ł��邩�܂��̓^�e���R�̗����Ƃ��a����`����Ă��Ȃ��ꍇ�͒l 1 ��Ԃ��B
	 */
	int getPattern(Address p) {
		int pat = horizPattern[p.r()][p.c()] & vertPattern[p.r()][p.c()];
		// �ς̍ŉ��ʃr�b�g 1 �ł���΃^�e���R�Ƃ�����`�Ƃ������ƂȂ̂ŁC�����\�����Ȃ����߂� 1 ��Ԃ��B
		// �^�e���R�Ƃ�����`�̏ꍇ�� 1�`9�@�܂ł��ׂĉ\�Ƃ����h�b�g�\��������ꍇ�͈ȉ�2�s�폜����B
		if ((pat & 1) == 1)
			return 1;
		return pat;
	}
	
	void setupHint(Board board) {
		this.board = board;
		horizPattern = new int[board.rows()][board.cols()];
		vertPattern = new int[board.rows()][board.cols()];
	}

	/**
	 * �����̍��W�̐������ύX���ꂽ�Ƃ��ɁC���̃}�X���܂ރ^�e���R�������Čv�Z����
	 * @param p
	 */
	void updatePattern(Address p) {
		updatePattern(p, Direction.HORIZ);
		updatePattern(p, Direction.VERT);
	}
	
	/**
	 * �������W�̃}�X���܂ރ��R�܂��̓^�e�̃��[�h�̔z�u�\�p�^�[�������߂�B
	 * @param p0
	 * @param dir
	 */
	void updatePattern(Address p0, int dir) {
		int pattern = 0;
		Address headPosition = board.getWordHead(p0, dir);//���̃}�X���܂ސ����̘a�����������}�X�̗���W
		int wordSum = board.getWordSum(p0, dir); // �����̘a
		int wordSize = board.getWordSize(p0, dir); //�@�}�X��
		int currentSum = 0;// ����ςݐ����̘a
		int currentSize = 0; // ����ςݐ����̐�
		int usedDigit = 0;// �g�p�ςݐ����̃p�^�[��
		Address p = headPosition;
		if (wordSum == 0) { // �a����`����Ă��Ȃ���΂��ׂĉ\
			pattern = HintTbl.D_ALL + 1;
		} else if (wordSize > 9) {
			pattern = 0;
		} else {
			for (int i = 0; i < wordSize; i++) {
				p = Address.nextCell(p, dir|2);
				int n = board.getNumber(p);
				if (n > 0) {
					currentSize++;
					currentSum += n; // ���a�ɉ��Z
					usedDigit |= (1 << n);//�g�p���Ă��鐔��
				}
			}
			if (currentSize == wordSize) { // ���ׂČ��܂��Ă���
				pattern = 0;
			} else {
				pattern = HintTbl.getRemainingDigit(wordSum - currentSum, wordSize - currentSize, usedDigit);
			}
		}
		for (int i = 0; i < wordSize; i++) {
			if (dir == Direction.HORIZ)
				horizPattern[p0.r()][headPosition.c()+1+i] = pattern;
			else if (dir == Direction.VERT)
				vertPattern[headPosition.r()+1+i][p0.c()] = pattern;
		}
	}

	/**
	 * �ՖʑS�̂̉\�p�^�[�����Čv�Z����
	 */
	void initHint() {
		// �S���Čv�Z����̂ŁA�S�������\�Ƃ���
		for (Address p : board.cellAddrs()) {
			vertPattern[p.r()][p.c()] = HintTbl.D_ALL + 1;
			horizPattern[p.r()][p.c()] = HintTbl.D_ALL + 1;
		}
		for (Address p : board.cellAddrs()) {
			if (board.isWall(p)) {
				for (int d = 0; d < 2; d++) {
					Address p1 = Address.nextCell(p, d|2);
					if (!board.isWall(p1));
						if (board.getSum(p, d) > 0) {
							updatePattern(p1, d);
						}
				}
			}
		}
	}
}
