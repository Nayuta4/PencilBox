package pencilbox.common.core;

/**
 * �S�p�Y�����ʂŗp����萔���L�q
 * 
 * !!!!!!�ύX�s��!!!!!!
 */

public class Direction {
	
	private Direction(){};

	public static final int VERT = 0;
	public static final int HORIZ = 1;

	public static final int UP = 0;
	public static final int LT = 1;
	public static final int DN = 2;
	public static final int RT = 3;

	public static final int LTUP = 4;
	public static final int LTDN = 5;
	public static final int RTDN = 6;
	public static final int RTUP = 7;

	public static final int[] UP_LT_DN_RT = {UP, LT, DN, RT};
	public static final int[] DIAGONAL4 = {LTUP, LTDN, RTDN, RTUP};
	public static final int[] DN_RT = {DN, RT};

}

/*
	�g��������
	UP, DN, LT, RT �� VERT, HORIZ �ɕϊ�����ɂ� &1 ����
	UP <-> DN, LT <-> RT ��ϊ�����ɂ́C ^2 ����
 */

	
