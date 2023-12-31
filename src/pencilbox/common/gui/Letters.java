package pencilbox.common.gui;

/**
 * 数字の代替文字の集合
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
			letters = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
			break;
		case 3 :
			letters = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
			break;
		case 4 :
			letters = "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワ";
			break;
		case 5 :
			letters = "いろはにほへとちりぬるをわかよたれそつねならむうゐのおくやまけふこえてあさきゆめみしゑひもせす";
			break;
		case 6 :
			letters = "☆★○●◎◇◆□■△▲▽▼〒§＠＊＆＃％£¢＄￥";
			break;
		case 7 :
			letters = "鱸鱶鱧鱠鱚鰾鱆鰲鱇鰰鰡鰤鰥鰮鰄鰊鰒鰈鰆鰌鰓鰉鰔鰕鯰鯱鯲鰺鯡鯔鯤鯢鯣鯒鯑鯏鯆鮹鯊鯊鯀鮴鮨鮠鮟鮗鮖鮑鮃鮓魴";
			break;
		default :
			letters = "";
			break;
			//		letter = "αβγδεζηθικλμνξοπρστυφχψω";
			//		letter = "一二三四五六七八九十百千万億兆";
		}
		return letters;
	}
}
