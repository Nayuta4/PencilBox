package pencilbox.kakuro;

import pencilbox.common.core.Direction;

/**
 * �\�������r�b�g�p�^�[���ŕ\������q���g�@�\
 */
public class DigitPatternHint {

	static final int HORIZ = Direction.HORIZ; 
	static final int VERT = Direction.VERT;

	private Board board;
	private int[][] horizPattern;
	private int[][] vertPattern;
	private int[][] remPattern;
	private int[][] remNo; // �\�Ȑ����̂���
	
	int getRemNo(int r, int c) {
		return remNo[r][c];
	}
	int getRemPattern(int r, int c) {
		return remPattern[r][c];
	}
	void init(Board board) {
		this.board = board;
		int rows = board.rows();
		int cols = board.cols();
		horizPattern = new int[rows][cols];
		vertPattern = new int[rows][cols];
		remPattern = new int[rows][cols];
		remNo = new int[rows][cols];
		helpReCalc();
	}
	// (r,c) �̈ʒu���ύX�i����ւ̕ύX���܂ށj���ꂽ����
	// ���̃}�X���܂ރ^�e���R�������Čv�Z����
	void helpCheck(int r, int c) {
		// cross����ʒu�͍s�񗼕��ς�邽�߁A���ׂĉɂ��Ă���
//		remPattern[r][c] = HintTbl.D_ALL;
//		horizPattern[r][c] = HintTbl.D_ALL;
//		vertPattern[r][c] = HintTbl.D_ALL;
		helpCheckHoriz(r, c);
		helpCheckVert(r, c);
	}
	void helpCheckHoriz(int r, int c) {
		int sumPos = board.getWordHead(r,c,HORIZ);//���̃}�X���܂ސ����̘a�̈ʒu
		int sum = board.getWordSum(r,c,HORIZ);// �����̘a
		if (sum == 0)// �a����`����Ă��Ȃ���΂Ȃɂ����Ȃ�
			return;
		int no = board.getWordSize(r,c,HORIZ);//�����̐�
		if (no > 9)
			return;
		int curSum = 0;// ��`�ςݐ����̘a
		int remNo = 0;// ����`�̐����̐�
		int usedDigit = 0;// ��`�ςݐ����̃p�^�[��
		for (int i = 0; i < no; i++) {
			int n = board.getNumber(r,sumPos+1+i);// �����̒l
			if (n == 0)// ����`�̐���
				remNo++;
			else {
				curSum += n; // ���a�ɉ��Z
				usedDigit |= (1 << n);//�g�p���Ă��鐔��
			}
		}
		if(remNo ==0) return; // ���ׂČ��܂���
		int remPat = HintTbl.getRemainingDigit(sum - curSum, remNo, usedDigit);
		// ���蓖�ĉ\�Ȑ��̑g�ݍ��킹
		// �e���萔��remPat��ݒ�(�S���ɐݒ肵�Ă��\���͂���Ȃ�������Ȃ�)
		for (int i = 0; i < no; i++) {
			horizPattern[r][sumPos+1+i] = remPat;
			setRem(r, sumPos+1+i);
		}
	}
	void helpCheckVert(int r, int c) {
		int sumPos = board.getWordHead(r,c,VERT);
		int sum = board.getWordSum(r,c,VERT);
		if (sum == 0)
			return;
		int no = board.getWordSize(r,c,VERT);
		if (no > 9)
			return;
		int curSum = 0;
		int remNo = 0;
		int usedDigit = 0;
		for (int i = 0; i < no; i++) {
			int n = board.getNumber(sumPos+1+i,c);
			if (n == 0)
				remNo++;
			else {
				curSum += n;
				usedDigit |= (1 << n);
			}
		}
		if(remNo ==0) return; // ���ׂČ��܂���
		int remPat = HintTbl.getRemainingDigit(sum - curSum, remNo, usedDigit);
		// �e���萔��remPat��ݒ�
		for (int i = 0 ; i < no; i++) {
			vertPattern[sumPos+1+i][c] = remPat;
			setRem(sumPos+1+i, c);
		}
	}
	void setRem(int r, int c) {
		// �^�e���R���ʂ̐���������
		int pat = horizPattern[r][c] & vertPattern[r][c];
		remPattern[r][c] = pat;
		// ������鐔���̐�
		int n = 0;
		for (int i=0; i<9; i++)
			if ((pat & HintTbl.D_ARRAY[i]) != 0)
				n++;
		remNo[r][c] = n;
	}
	void helpReCalc() {
		// �S���Čv�Z����̂ŁA�S�������\�Ƃ���
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				// �\�����Ȃ���΁~�󂪂��Ă��܂��B���v���O�Ȃ�v�Z�Ώۂ�
				// �Ȃ�Ȃ���remNo�͕ύX���ꂸ�Apattern���O�̂܂܂Ȃ̂�
				// �����\������Ȃ��B���̂悤�ȃg���b�N�ł���B
				remNo[r][c] = 1;
				vertPattern[r][c] = HintTbl.D_ALL;
				horizPattern[r][c] = HintTbl.D_ALL;
			}
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.isWall(r,c)) {
					if (board.getSumH(r,c) != 0) {
						helpCheck(r, c+1);
					}
					if (board.getSumV(r,c) != 0) {
						helpCheck(r+1, c);
					}
				}
			}
		}
	}
}
