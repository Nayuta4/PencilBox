package pencilbox.hitori;

/**
 * �����̑�֕����̏W��
 */
class Letters {

	static char[] getLetterSeries(int option) {
		char[] letter;

		switch (option) {
		case 0 :
			letter = new char[] {};
			break;
		case 1 :
			letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			break;
		case 2 :
			letter = "������������������������������������������������".toCharArray();
			break;
		case 3 :
			letter = "�@�A�B�C�D�E�F�G�H�I�J�K�L�M�N�O�P�Q�R�S�T�U�V�W�X�Y�Z�[�\�]�^�_�`".toCharArray();
			break;
		case 4 :
			letter = "�A�C�E�G�I�J�L�N�P�R�T�V�X�Z�\�^�`�c�e�g�i�j�k�l�m�n�q�t�w�z�}�~������������������������".toCharArray();
			break;
		case 5 :
			letter = "����͂ɂقւƂ���ʂ���킩�悽�ꂻ�˂Ȃ�ނ���̂�����܂��ӂ����Ă�������߂݂���Ђ�����".toCharArray();
			break;
		case 6 :
			letter = "������������������������������������������������".toCharArray();
			break;
		case 7 :
			letter = "�������������������������������������������������������������������������������������������".toCharArray();
			break;
		default :
			letter = new char[] {};
			break;
			//		letter = "�������ÃăŃƃǃȃɃʃ˃̃̓΃σЃу҃ӃԃՃ�".toCharArray();
			//		letter = "���O�l�ܘZ������\�S�疜����".toCharArray();
		}
		return letter;
	}
}
