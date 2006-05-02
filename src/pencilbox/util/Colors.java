package pencilbox.util;

import java.awt.Color;
import java.util.Random;

/**
 * @author joji
 */
public class Colors {
	private static Color ERROR = Color.RED;
	private static Color WALL = Color.BLUE;
	private static Color SMALL_WALL = Color.CYAN;
	static ColorEntry[] LIST, DARK_LIST, BRIGHT_LIST;
	private static int SIZE, DARK_SIZE, BRIGHT_SIZE;
	private static int[] INDEX, DARK_INDEX, BRIGHT_INDEX;
	private static int secondIndex;
	private static Random random = new Random();
	static {
		LIST = ColorEntry.getUseColor(ColorEntry.getNamedColor());
		ColorEntry.complete(LIST);
		SIZE = LIST.length;
		INDEX = makeIndex(LIST);
		// 上で選ばれた使用する色の中からbrightnessで選択する
		DARK_LIST = selectDarkColor(LIST, 0.91f, true);
		DARK_INDEX = makeIndex(DARK_LIST);
		DARK_SIZE = DARK_LIST.length;
		BRIGHT_LIST = selectDarkColor(LIST, 0.9f, false);
		BRIGHT_INDEX = makeIndex(BRIGHT_LIST);
		BRIGHT_SIZE = BRIGHT_LIST.length;
	}
	private static ColorEntry[] selectDarkColor(
		ColorEntry[] list,
		float brightLevel,
		boolean darker) {
		int n = 0;
		for (int i = 0; i < list.length; i++) {
			ColorEntry e = list[i];
			if (darker
				&& e.brightness < brightLevel
				|| !darker
				&& e.brightness > brightLevel)
				n++;
		}
		ColorEntry[] selection = new ColorEntry[n];
		n = 0;
		for (int i = 0; i < list.length; i++) {
			ColorEntry e = list[i];
			if (darker
				&& e.brightness < brightLevel
				|| !darker
				&& e.brightness > brightLevel)
				selection[n++] = list[i];
		}
		return selection;
	}
	private static int[] makeIndex(ColorEntry[] list) {
		int size = list.length;
		int[] index = new int[size];
		for (int i = 0; i < size; i++) {
			index[i] = i;
		}
		return index;
	}
	private static int getIndex(int i) {
		int j = i / SIZE * secondIndex + i;
		if (j < 0)
			j = -j;
		return j % SIZE;
	}
	private static int getIndex(int i, int size) {
		int j = i / size * secondIndex + i;
		if (j < 0)
			j = -j;
		return j % size;
	}
	public static Color getDarkColor(int i) {
//bug		return DARK_LIST[INDEX[getIndex(i, DARK_SIZE )]].color;
		return DARK_LIST[DARK_INDEX[getIndex(i, DARK_SIZE )]].color;
	}
	public static Color getBrightColor(int i) {
//bug		return BRIGHT_LIST[INDEX[getIndex(i, BRIGHT_SIZE)]].color;
		return BRIGHT_LIST[BRIGHT_INDEX[getIndex(i, BRIGHT_SIZE)]].color;
	}
	public static Color get(int i) {
		if (i == 0)
			return WALL;
		return LIST[INDEX[getIndex(i)]].color;
	}
	public static Color getColor(int i) {
		return LIST[INDEX[getIndex(i)]].color;
	}
	public static Color getRevColor(int i) {
		return LIST[INDEX[getIndex(i)]].revColor;
	}
	public static Color getError() {
		return ERROR;
	}
	public static Color getSmallWall() {
		return SMALL_WALL;
	}
	public static Color getDefault() {
		return WALL;
	}
	public static void randomize(){
		randomizeIndex(INDEX);
		randomizeIndex(DARK_INDEX);
		randomizeIndex(BRIGHT_INDEX);
	}
	public static void randomizeDarkColor(){
		randomizeIndex(DARK_INDEX);
	}
	public static void randomizeBrightColor(){
		randomizeIndex(BRIGHT_INDEX);
	}
	private static void randomizeIndex(int[] index) {
		int size = index.length;
		for (int i = 0; i < 1000; i++) {
			int x = random.nextInt(size);
			if (x == 0)
				x = 1;
			int y = random.nextInt(size);
			if (y == 0)
				y = 1;
			int t = index[x];
			index[x] = index[y];
			index[y] = t;
		}
		/*
		* colorと対応させるのにSIZEのmodを取るだけだと、modが同じ場合、
		* 同じ色である関係が固定してしまうので、SIZEによる商も考慮する。
		*/
		secondIndex = random.nextInt(100000);
	}
	public static int getNewNo() {
		int no = random.nextInt();
		if (no < 0)
			no = -no;
		return no;
	}
}
