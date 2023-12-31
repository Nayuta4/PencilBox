package pencilbox.common.gui;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 座標表示文字列
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

		letters = makeArray2("ア イ ウ エ オ カ キ ク ケ コ サ シ ス セ ソ タ チ ツ テ ト ナ ニ ヌ ネ ノ ハ ヒ フ ヘ ホ マ ミ ム メ モ ヤ ユ ヨ ラ リ ル レ ロ ワ ン".split(" "), MAX);
		addIndexLettersToList(5, letters);

		letters = makeArray2("い ろ は に ほ へ と ち り ぬ る を わ か よ た れ そ つ ね な ら む う ゐ の お く や ま け ふ こ え て あ さ き ゆ め み し ゑ ひ も せ す".split(" "), MAX);
		addIndexLettersToList(6, letters);

		letters = makeArray1("〇 一 二 三 四 五 六 七 八 九".split(" "), MAX);
		addIndexLettersToList(7, letters);

		/* 空文字 */
		letters = new String[MAX];
		Arrays.fill(letters, "");
		addIndexLettersToList(9, letters);
	}

	private static void addIndexLettersToList(int k, String[] letters) {
		IndexLetters.INDEX_LETTERS_LIST.add(new IndexLetters(k, letters));
	}

	/**
	 * 座標表示に用いる文字列の配列を作成する
	 * a b c ... z などの配列から a b c ... z aa ab ac ... zz aaa ... のような配列を作成して返す
	 * @param array 数字の代わりに用いる文字列の配列
	 * @param max 作成する最大座標
	 * @return 0からmaxの座標に対応した文字列の配列
	 */
	private static String[] makeArray2(String[] array, int max) {
		String[] result = new String[max];
		for (int n=0; n<max; n++) {
			result[n] = coordinate2(array, n);
		}
		return result;
	}

	/**
	 * 引数の座標を表す文字列を返す
	 * @param array 数字の代わりに用いる文字列の配列
	 * @param n 数字
	 * @return 数字nにあたる文字列
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
	 * 座標表示に用いる文字列の配列を作成する
	 * 0 1 2 ... 9 の配列から 0 1 2 ... 9 10 11 ... のような配列を作成して返す
	 * @param array 数字の代わりに用いる文字列の配列
	 * @param max 作成する最大座標
	 * @return 0からmaxの座標に対応した文字列の配列
	 */
	private static String[] makeArray1(String[] array, int max) {
		String[] result = new String[max];
		for (int n=0; n<max; n++) {
			result[n] = coordinate1(array, n);
		}
		return result;
	}

	/**
	 * 引数の座標を表す文字列を返す
	 * @param array 数字の代わりに用いる文字列の配列
	 * @param n 数字
	 * @return 数字nにあたる文字列
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
	 * 番号から文字群への対応
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

	private int style; // 設定保存や選択で用いる番号
	private String[] letters; // 文字列群
	private String label; // ComboBox で用いる表示

	/**
	 * @param makeLetters
	 */
	public IndexLetters(int s, String[] l) {
		this.style = s;
		this.letters = l;
		this.label = makeLettersLabel(l);
	}

	/**
	 * ComboBox のラベルにするための文字列を作成する
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