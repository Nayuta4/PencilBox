package pencilbox.common.gui;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ���W�\��������
 */
public class IndexLetters {

	public static final ArrayList<IndexLetters> INDEX_LETTERS_LIST = new ArrayList<IndexLetters>();
	public static final int MAX = 200;

	static {
		String[] letters;
		/* 1 2 3 ... */
		letters = new String[MAX];
		for (int n=0; n<MAX; n++)
			letters[n] = Integer.toString(n+1);
		addIndexLettersToList(1, letters);

		/* 0 1 2 ... */
		letters = new String[MAX];
		for (int n=0; n<MAX; n++)
			letters[n] = Integer.toString(n);
		addIndexLettersToList(2, letters);

		letters = makeArray2("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" "), MAX);
		addIndexLettersToList(3, letters);

		letters = makeArray2("a b c d e f g h i j k l m n o p q r s t u v w x y z".split(" "), MAX);
		addIndexLettersToList(4, letters);

		letters = makeArray2("�A �C �E �G �I �J �L �N �P �R �T �V �X �Z �\ �^ �` �c �e �g �i �j �k �l �m �n �q �t �w �z �} �~ �� �� �� �� �� �� �� �� �� �� �� �� ��".split(" "), MAX);
		addIndexLettersToList(5, letters);

		letters = makeArray2("�� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� ��".split(" "), MAX);
		addIndexLettersToList(6, letters);

		letters = makeArray1("�Z �� �� �O �l �� �Z �� �� ��".split(" "), MAX);
		addIndexLettersToList(7, letters);

		/* �󕶎� */
		letters = new String[MAX];
		Arrays.fill(letters, "");
		addIndexLettersToList(9, letters);
	}

	private static void addIndexLettersToList(int k, String[] letters) {
		IndexLetters.INDEX_LETTERS_LIST.add(new IndexLetters(k, letters));
	}

	/**
	 * ���W�\���ɗp���镶����̔z����쐬����
	 * a b c ... z �Ȃǂ̔z�񂩂� a b c ... z aa ab ac ... zz aaa ... �̂悤�Ȕz����쐬���ĕԂ�
	 * @param array �����̑���ɗp���镶����̔z��
	 * @param max �쐬����ő���W
	 * @return 0����max�̍��W�ɑΉ�����������̔z��
	 */
	private static String[] makeArray2(String[] array, int max) {
		String[] result = new String[max];
		for (int n=0; n<max; n++) {
			result[n] = coordinate2(array, n);
		}
		return result;
	}

	/**
	 * �����̍��W��\���������Ԃ�
	 * @param array �����̑���ɗp���镶����̔z��
	 * @param n ����
	 * @return ����n�ɂ����镶����
	 */
	private static String coordinate2(String[] array, int n) {
		String a = "";
		int d = array.length;
		while (n >= 0) {
			a = array[n%d] + a;
			n = n / d - 1;
		}
		return a;
	}

	/**
	 * ���W�\���ɗp���镶����̔z����쐬����
	 * 0 1 2 ... 9 �̔z�񂩂� 0 1 2 ... 9 10 11 ... �̂悤�Ȕz����쐬���ĕԂ�
	 * @param array �����̑���ɗp���镶����̔z��
	 * @param max �쐬����ő���W
	 * @return 0����max�̍��W�ɑΉ�����������̔z��
	 */
	private static String[] makeArray1(String[] array, int max) {
		String[] result = new String[max];
		for (int n=0; n<max; n++) {
			result[n] = coordinate1(array, n);
		}
		return result;
	}

	/**
	 * �����̍��W��\���������Ԃ�
	 * @param array �����̑���ɗp���镶����̔z��
	 * @param n ����
	 * @return ����n�ɂ����镶����
	 */
	private static String coordinate1(String[] array, int n) {
		String a = "";
		int d = array.length;
		n = n + 1;
		while (n > 0) {
			a = array[n%d] + a;
			n = n / d;
		}
		return a;
	}

	/**
	 * �ԍ����當���Q�ւ̑Ή�
	 * @param s
	 * @return
	 */
	public static final IndexLetters getIndexLetters(int s) {
		for (IndexLetters letters : INDEX_LETTERS_LIST) {
			if (letters.getStyle() == s)
				return letters;
		}
		return INDEX_LETTERS_LIST.get(0);
	}

	private int style; // �ݒ�ۑ���I���ŗp����ԍ�
	private String[] letters; // ������Q
	private String label; // ComboBox �ŗp����\��

	/**
	 * @param makeLetters
	 */
	public IndexLetters(int s, String[] l) {
		this.style = s;
		this.letters = l;
		this.label = makeLettersLabel(l);
	}

	/**
	 * ComboBox �̃��x���ɂ��邽�߂̕�������쐬����
	 * @param l
	 * @return
	 */
	private String makeLettersLabel(String[] l) {
		String label = "";
		for (int i=0; i<3 && i<l.length; i++) {
			label = label + l[i] + " ";
		}
		label = label + "...";
		return label;
	}

	public String[] getLetters() {
		return letters;
	}

	public int getStyle() {
		return style;
	}

	public String toString() {
		return label;
	}

}