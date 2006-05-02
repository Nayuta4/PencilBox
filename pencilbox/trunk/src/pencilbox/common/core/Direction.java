package pencilbox.common.core;

/**
 * 全パズル共通で用いる定数を記述
 * 
 * !!!!!!変更不可!!!!!!
 */

public class Direction {
	
	private Direction(){};

	public static final int VERT = 0;
	public static final int HORIZ = 1;

	public static final int UP = 0;
	public static final int LT = 1;
	public static final int DN = 2;
	public static final int RT = 3;

}

/*
	使い方メモ
	UP, DN, LT, RT を VERT, HORIZ に変換するには &1 する
	UP <-> DN, LT <-> RT を変換するには， ^2 する
 */

	
