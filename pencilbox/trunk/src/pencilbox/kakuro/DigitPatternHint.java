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
	 * @param r�@�}�X�̍s���W
	 * @param c �}�X�̗���W
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
		int rows = board.rows();
		int cols = board.cols();
		horizPattern = new int[rows][cols];
		vertPattern = new int[rows][cols];
	}

	/**
	 * �����̍��W�̐������ύX���ꂽ�Ƃ��ɁC���̃}�X���܂ރ^�e���R�������Čv�Z����
	 * @param r
	 * @param c
	 */
	void updatePattern(int r, int c) {
		updatePattern(r, c, Direction.HORIZ);
		updatePattern(r, c, Direction.VERT);
	}
	
	/**
	 * �������W�̃}�X���܂ރ��R�܂��̓^�e�̃��[�h�̔z�u�\�p�^�[�������߂�B
	 * @param r
	 * @param c
	 * @param dir
	 */
	void updatePattern(int r, int c, int dir) {
		int pattern = 0;
		int headPosition = board.getWordHead(r, c, dir);//���̃}�X���܂ސ����̘a�����������}�X�̗���W
		int wordSum = board.getWordSum(r, c, dir); // �����̘a
		int wordSize = board.getWordSize(r, c, dir); //�@�}�X��
		int currentSum = 0;// ����ςݐ����̘a
		int currentSize = 0; // ����ςݐ����̐�
		int usedDigit = 0;// �g�p�ςݐ����̃p�^�[��
		if (wordSum == 0) { // �a����`����Ă��Ȃ���΂��ׂĉ\
			pattern = HintTbl.D_ALL + 1;
		} else if (wordSize > 9) {
			pattern = 0;
		} else {
			for (int i = 0; i < wordSize; i++) {
				int n = 0;
				if (dir == Direction.HORIZ)
					n = board.getNumber(r, headPosition+1+i);
				else if (dir == Direction.VERT)
					n = board.getNumber(headPosition+1+i, c);
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
				horizPattern[r][headPosition+1+i] = pattern;
			else if (dir == Direction.VERT)
				vertPattern[headPosition+1+i][c] = pattern;
		}
	}

	/**
	 * �ՖʑS�̂̉\�p�^�[�����Čv�Z����
	 */
	void initHint() {
		// �S���Čv�Z����̂ŁA�S�������\�Ƃ���
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				vertPattern[r][c] = HintTbl.D_ALL + 1;
				horizPattern[r][c] = HintTbl.D_ALL + 1;
			}
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.isWall(r, c)) {
					if (!board.isWall(r, c+1))
						if (board.getSumH(r,c) > 0) {
							updatePattern(r, c+1, Direction.HORIZ);
						}
					if (!board.isWall(r+1, c))
						if (board.getSumV(r,c) > 0) {
							updatePattern(r+1, c, Direction.VERT);
						}
				}
			}
		}
	}
}
