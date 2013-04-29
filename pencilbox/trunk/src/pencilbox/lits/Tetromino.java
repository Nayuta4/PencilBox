package pencilbox.lits;

import pencilbox.common.core.Address;


/**
 * 「ＬＩＴＳ」領域クラス
 */
public class Tetromino extends pencilbox.common.core.AreaBase {

	public static final int NOT_TETROMINO = 0;
	public static final int TYPE_L = 1;
	public static final int TYPE_I = 2;
	public static final int TYPE_T = 3;
	public static final int TYPE_S = 4;
	public static final int TYPE_O = 5;

	private static final char[] TetrominoTypeChar = new char[] { '-', 'L', 'I', 'T', 'S', 'O' };

	public static final char getTetrominoTypechar (int i) {
		if ( i >= 0 && i <= 5 )
			return TetrominoTypeChar[i];
		else 
			return '?';
	}

	/**
	 * テトロミノのタイプを判別する。
	 * @return テトロミノの場合はタイプに応じた定数を返す。テトロミノでなければ 0 を返す。
	 */
	public int getTetrominoType() {
		if (size() != 4)
			return 0;
		Object[] address = this.toArray();
		int value = (function((Address)address[1], (Address)address[0]) << 8)
				| (function((Address)address[2], (Address)address[0]) << 4)
				| (function((Address)address[3], (Address)address[0]) << 0);
		switch (value) {
		case 0x0123 :
			return TYPE_I;
		case 0x0126 :
			return TYPE_L;
		case 0x0127 :
			return TYPE_T;
		case 0x0128 :
			return TYPE_L;
		case 0x0156 :
			return TYPE_S;
		case 0x0167 :
			return TYPE_O;
		case 0x016A :
			return TYPE_L;
		case 0x0178 :
			return TYPE_S;
		case 0x017B :
			return TYPE_L;
		case 0x0456 :
			return TYPE_L;
		case 0x0567 :
			return TYPE_T;
		case 0x0569 :
			return TYPE_S;
		case 0x056A :
			return TYPE_T;
		case 0x0678 :
			return TYPE_L;
		case 0x067A :
			return TYPE_T;
		case 0x067B :
			return TYPE_S;
		case 0x069A :
			return TYPE_L;
		case 0x06AB :
			return TYPE_L;
		case 0x06AC :
			return TYPE_I;
		default :
			return 0;
		}
	}

	/*
	 *  |-3-2-1 0 1 2 3 
	 *--+---------------x
	 *-3|       .       
	 *-2|     . . .     
	 *-1|   . . . . .   
	 * 0| . . . 0 1 2 3 
	 * 1|   4 5 6 7 8   
	 * 2|     9 A B     
	 * 3|       C       
	 *  y
	 *  f(y, x) = (7-y)*y + x if |x|+|y| <= 3
	 */
	private int function(Address position, Address origin) {
		int y = position.r()-origin.r();
		int x = position.c()-origin.c();
		if (y == 0) {
			if (x >= 0 && x <= 3)
				return x;
		} else if (y == 1) {
			if (x >= -2 && x <= 2)
				return x + 6;
		} else if (y == 2) {
			if (x >= -1 && x <= 1)
				return x + 10;
		} else if (y == 3) {
			if (x == 0)
				return x + 12;
		}
		return 0xF;
	}
}
