package pencilbox.common.gui;

/**
 * �����̑�֕����̏W��
 */
public class Letters {

	public static String getLetterSeries(int option) {
		String letters;

		switch (option) {
		case 0 :
			letters = "";
			break;
		case 1 :
			letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			break;
		case 2 :
			letters = "������������������������������������������������";
			break;
		case 3 :
			letters = "�@�A�B�C�D�E�F�G�H�I�J�K�L�M�N�O�P�Q�R�S�T�U�V�W�X�Y�Z�[�\�]�^�_�`";
			break;
		case 4 :
			letters = "�A�C�E�G�I�J�L�N�P�R�T�V�X�Z�\�^�`�c�e�g�i�j�k�l�m�n�q�t�w�z�}�~������������������������";
			break;
		case 5 :
			letters = "����͂ɂقւƂ���ʂ���킩�悽�ꂻ�˂Ȃ�ނ���̂�����܂��ӂ����Ă�������߂݂���Ђ�����";
			break;
		case 6 :
			letters = "������������������������������������������������";
			break;
		case 7 :
			letters = "�������������������������������������������������������������������������������������������";
			break;
		default :
			letters = "";
			break;
			//		letter = "�������ÃăŃƃǃȃɃʃ˃̃̓΃σЃу҃ӃԃՃ�";
			//		letter = "���O�l�ܘZ������\�S�疜����";
		}
		return letters;
	}
}
