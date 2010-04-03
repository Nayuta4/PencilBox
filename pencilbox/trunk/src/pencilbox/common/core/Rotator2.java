/**
 * 
 */
package pencilbox.common.core;


/**
 * À•W‰ñ“]ŒvŽZ—p•â•ƒNƒ‰ƒX
 */
public class Rotator2 {
	
    private Rotator2() {}

	/**
	 * ˆø”‚Å—^‚¦‚½‰ñ“]”Ô†‚ÍC”Õ–ÊƒTƒCƒY‚Ìc‰¡ŒðŠ·‚ÉŠY“–‚·‚é‚©”Û‚©‚ð“š‚¦‚é
	 * @param n@‰ñ“]”Ô†
	 * @return c‰¡ŒðŠ·‚³‚ê‚é‚È‚ç true, ‚³‚ê‚È‚¢‚È‚ç false
	 */
	public static boolean isTransposed(int n) {
		switch (n) {
		case 0 :
		case 2 :
		case 5 :
		case 7 :
			return false;
		case 1 :
		case 3 :
		case 4 :
		case 6 :
			return true;
		default :
			return false;
		}
	}
	
	/**
	 * ‰ñ“]•ÏŠ·‚ÌŒ‹‡
	 * @param a@1‚Â‚ß‚Ì‰ñ“]”Ô†
	 * @param b@2‚Â‚ß‚Ì‰ñ“]”Ô†
	 * @return@‰ñ“]”Ô†‚ÌÏ
	 */
	public static int combine(int a, int b) {
		int r = a;
		if (a >= 0 && a <= 3) {
			r = (b / 4) * 4  + (a + b) % 4;
		} else if (a >= 4 && a <= 7) {
			r = ((7-b) / 4) * 4  + (a + (8-b)) % 4;
		}
		return r;
	}
	
	/**
	 * —^‚¦‚ç‚ê‚½À•Wpos‚ÉC
	 * À•Wfrom‚©‚çÀ•Wto‚Ö‚ÌˆÚ“®‚Æ“¯‚¶•½sˆÚ“®‚µ‚½‚Ì‚¿‚ÉC
	 * to‚ð’†S‚Æ‚µ‚½‰ñ“]”Ô†rotation‚Ì‰ñ“]‚ðŽ{‚µ‚½À•W‚ð•Ô‚·
	 * @param pos •ÏŠ·Œ³À•W
	 * @param from •½sˆÚ“®‚Ì‹N“_
	 * @param to •½sˆÚ“®‚ÌI“_
	 * @param rotation ‰ñ“]”Ô†
	 * @return •ÏŠ·Œã‚ÌÀ•W
	 */
	public static Address translateAndRotateAddress(Address pos, Address from, Address to, int rotation) {
		int a = to.r();
		int b = to.c();
		int r = pos.r() - from.r() + to.r();
		int c = pos.c() - from.c() + to.c();
		int R = r;
		int C = c;
		switch (rotation) {
			case 0 :
				R = +r;
				C = +c;
				break;
			case 1 :
				R = -c +b +a;
				C = +r -a +b;
				break;
			case 2 :
				R = -r +a +a;
				C = -c +b +b;
				break;
			case 3 :
				R = +c -b +a;
				C = -r +a +b;
				break;
			case 4 :
				R = +c -b +a;
				C = +r -a +b;
				break;
			case 5 :
				R = +r      ;
				C = -c +b +b;
				break;
			case 6 :
				R = -c +b +a;
				C = -r +a +b;
				break;
			case 7 :
				R = -r +a +a;
				C = +c      ;
				break;
		}
		return Address.address(R, C);
	}

	/**
	 * ”Õã‚Ì•ûŒü‚ðƒpƒlƒ‹ã‚Ì•ûŒü‚É•ÏŠ·‚·‚é
	 * @param direction •ÏŠ·Œ³‚Ì•ûŒü‚ð•\‚·”’l
	 * @return •ÏŠ·Œã‚Ì•ûŒü‚ð•\‚·”’l
	 */
	public static int rotateDirection(int direction, int rotation) {
		switch (rotation) {
			case 0 :
			case 1 :
			case 2 :
			case 3 :
				direction = (direction + rotation) % 4;
				break;
			case 4 :
			case 5 :
			case 6 :
			case 7 :
				direction = (direction + rotation) % 4;
				direction = direction ^ 1;
				break;
		}
		return direction;
	}
	
}
