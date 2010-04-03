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
	 * •ÏŠ·‚µ‚½À•W‚ð•Ô‚·
	 * @param pos
	 */
	public static Address translateAndRotateAddress(Address pos0, Address from, Address to, int rotation) {
		Address pos = Address.address(pos0.r() - from.r() + to.r(), pos0.c() - from.c() + to.c());
		int rows2 = to.r()*2;
		int cols2 = to.c()*2;
		switch (rotation) {
			case 0 :
				return Address.address(pos.r(), pos.c());
			case 1 :
				return Address.address((rows2 + cols2)/2 - pos.c(), (cols2 - rows2)/2 + pos.r());
			case 2 :
				return Address.address(rows2 - pos.r(), cols2 - pos.c());
			case 3 :
				return Address.address((rows2 - cols2)/2 + pos.c(), (cols2 + rows2)/2 - pos.r());
			case 4 :
				return Address.address((rows2 - cols2)/2 + pos.c(), (cols2 - rows2)/2 + pos.r());
			case 5 :
				return Address.address(pos.r(), cols2 - pos.c());
			case 6 :
				return Address.address((rows2 + cols2)/2 - pos.c(), (cols2 + rows2)/2 - pos.r());
			case 7 :
				return Address.address(rows2 - pos.r(), pos.c());
			default :
				return Address.address(pos.r(), pos.c());
		}
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
