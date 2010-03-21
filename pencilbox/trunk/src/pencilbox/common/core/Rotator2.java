/**
 * 
 */
package pencilbox.common.core;


/**
 * À•W‰ñ“]ŒvŽZ—p•â•ƒNƒ‰ƒX
 * ƒ}ƒX’†S‚ðÝ’è‚µ‚Ä‰ñ“]‚·‚é
 */
public class Rotator2 {
	
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
	 * ‰ñ“]E”½“]”Ô†
	 * 0@‰ñ“]‚È‚µ
	 * 1 ¶90‹‰ñ“]
	 * 2@¶180‹‰ñ“]
	 * 3 ¶270‹‰ñ“]
	 * 4 c‚Æ‰¡‚ðŒðŠ·
	 * 5 ¶90‹‰ñ“]‚µC‚»‚ÌŒãc‚Æ‰¡‚ðŒðŠ·‚µ
	 * 6 ¶180‹‰ñ“]‚µC‚»‚ÌŒãc‚Æ‰¡‚ðŒðŠ·
	 * 7 ¶270‹‰ñ“]‚µC‚»‚ÌŒãc‚Æ‰¡‚ðŒðŠ·
	 */
	private int rotation = 0;
//	private int rows; // ‰ñ“]’†S
//	private int cols; // ‰ñ“]’†S
	private int rows2; // ‰ñ“]’†S‚Ì2”{
	private int cols2; // ‰ñ“]’†S‚Ì2”{
	
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 */
	public Rotator2() {
	}
	
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 * @param rows s”
	 * @param cols —ñ”
	 * @param rotation ‰ñ“]E”½“]”Ô†
	 */
	public Rotator2(int rows, int cols, int rotation) {
//		this.rows = rows;
//		this.cols = cols;
		this.rows2 = rows * 2;
		this.cols2 = cols * 2;
		this.rotation = rotation;
	}
	
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 * @param center ’†SÀ•W
	 * @param rotation ‰ñ“]E”½“]”Ô†
	 */
	public Rotator2(Address center, int rotation) {
		this(center.r(), center.c(), rotation);
	}
	
	/**
	 * ó‘Ô‚ðÝ’è‚·‚é
	 * @param rows s”
	 * @param cols —ñ”
	 * @param rotation ‰ñ“]E”½“]”Ô†
	 */
	public void setup(int rows, int cols, int rotation) {
//		this.rows = rows;
//		this.cols = cols;
		this.rows2 = rows * 2;
		this.cols2 = cols * 2;
		this.rotation = rotation;
	}

	/**
	 * @return Returns the rotation.
	 */
	public int getRotation() {
		return rotation;
	}
	
	/**
	 * ”Õ–ÊƒTƒCƒY‚Ìc‰¡‚ªŒðŠ·‚³‚ê‚é‚©‚Ç‚¤‚©
	 * @return c‰¡ŒðŠ·‚³‚ê‚é‚È‚ç‚Î true
	 */
	public boolean isTransposed() {
		return isTransposed(rotation);
	}

	/**
	 * •ÏŠ·‚µ‚½À•W‚ð•Ô‚·
	 * @param pos
	 */
	public Address rotateAddress(Address pos) {
		Address dst = Address.address();
		switch (rotation) {
			case 0 :
				dst.set(pos.r(), pos.c());
				break;
			case 1 :
				dst.set((rows2 + cols2)/2 - pos.c(), (cols2 - rows2)/2 + pos.r());
				break;
			case 2 :
				dst.set(rows2 - pos.r(), cols2 - pos.c());
				break;
			case 3 :
				dst.set((rows2 - cols2)/2 + pos.c(), (cols2 + rows2)/2 - pos.r());
				break;
			case 4 :
				dst.set((rows2 - cols2)/2 + pos.c(), (cols2 - rows2)/2 + pos.r());
				break;
			case 5 :
				dst.set(pos.r(), cols2 - pos.c());
				break;
			case 6 :
				dst.set((rows2 + cols2)/2 - pos.c(), (cols2 + rows2)/2 - pos.r());
				break;
			case 7 :
				dst.set(rows2 - pos.r(), pos.c());
				break;
		}
		return dst;
	}

	/**
	 * ”Õã‚Ì•ûŒü‚ðƒpƒlƒ‹ã‚Ì•ûŒü‚É•ÏŠ·‚·‚é
	 * @param direction •ÏŠ·Œ³‚Ì•ûŒü‚ð•\‚·”’l
	 * @return •ÏŠ·Œã‚Ì•ûŒü‚ð•\‚·”’l
	 */
	public int rotateDirection(int direction) {
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
