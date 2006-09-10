/**
 * 
 */
package pencilbox.common.core;

import java.util.Iterator;

/**
 * À•W‰ñ“]ŒvZ—p•â•ƒNƒ‰ƒX
 */
public class Rotator {
	
	/**
	 * ˆø”‚Å—^‚¦‚½‰ñ“]”Ô†‚ÍC”Õ–ÊƒTƒCƒY‚Ìc‰¡ŒğŠ·‚ÉŠY“–‚·‚é‚©”Û‚©‚ğ“š‚¦‚é
	 * @param n@‰ñ“]”Ô†
	 * @return c‰¡ŒğŠ·‚³‚ê‚é‚È‚ç true, ‚³‚ê‚È‚¢‚È‚ç false
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
	 * ‰ñ“]E”½“]”Ô†
	 * 0@‰ñ“]‚È‚µ
	 * 1 ¶90‹‰ñ“]
	 * 2@¶180‹‰ñ“]
	 * 3 ¶270‹‰ñ“]
	 * 4 c‚Æ‰¡‚ğŒğŠ·
	 * 5 ¶90‹‰ñ“]‚µC‚»‚ÌŒãc‚Æ‰¡‚ğŒğŠ·‚µ
	 * 6 ¶180‹‰ñ“]‚µC‚»‚ÌŒãc‚Æ‰¡‚ğŒğŠ·
	 * 7 ¶270‹‰ñ“]‚µC‚»‚ÌŒãc‚Æ‰¡‚ğŒğŠ·
	 */
	private int rotation = 0;
	private int rows; // ‘S‘Ì‚Ìs”
	private int cols; // ‘S‘Ì‚Ì—ñ”
	
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 */
	public Rotator() {
	}
	
	/**
	 * ó‘Ô‚ğİ’è‚·‚é
	 * @param rows s”
	 * @param cols —ñ”
	 * @param rotation ‰ñ“]E”½“]”Ô†
	 */
	public Rotator(int rows, int cols, int rotation) {
		this.rows = rows;
		this.cols = cols;
		this.rotation = rotation;
	}
	
	/**
	 * ó‘Ô‚ğİ’è‚·‚é
	 * @param rows s”
	 * @param cols —ñ”
	 * @param rotation ‰ñ“]E”½“]”Ô†
	 */
	public void setup(int rows, int cols, int rotation) {
		this.rows = rows;
		this.cols = cols;
		this.rotation = rotation;
	}

	/**
	 * @return Returns the rotation.
	 */
	public int getRotation() {
		return rotation;
	}
	
	/**
	 * ”Õ–ÊƒTƒCƒY‚Ìc‰¡‚ªŒğŠ·‚³‚ê‚é‚©‚Ç‚¤‚©
	 * @return c‰¡ŒğŠ·‚³‚ê‚é‚È‚ç‚Î true
	 */
	public boolean isTransposed() {
		return isTransposed(rotation);
	}

	/**
	 * •ÏŠ·‚µ‚½À•W‚ğ•Ô‚·
	 * @param pos
	 */
	public Address rotateAddress(Address pos) {
		Address dst = new Address();
		switch (rotation) {
			case 0 :
				dst.set(pos.r(), pos.c());
				break;
			case 1 :
				dst.set(cols - 1 - pos.c(), pos.r());
				break;
			case 2 :
				dst.set(rows - 1 - pos.r(), cols - 1 - pos.c());
				break;
			case 3 :
				dst.set(pos.c(), rows - 1 - pos.r());
				break;
			case 4 :
				dst.set(pos.c(), pos.r());
				break;
			case 5 :
				dst.set(pos.r(), cols - 1 - pos.c());
				break;
			case 6 :
				dst.set(cols - 1 - pos.c(), rows - 1 - pos.r());
				break;
			case 7 :
				dst.set(rows - 1 - pos.r(), pos.c());
				break;
		}
		return dst;
	}

	/**
	 * ”Õã‚Ì•ûŒü‚ğƒpƒlƒ‹ã‚Ì•ûŒü‚É•ÏŠ·‚·‚é
	 * @param direction •ÏŠ·Œ³‚Ì•ûŒü‚ğ•\‚·”’l
	 * @return •ÏŠ·Œã‚Ì•ûŒü‚ğ•\‚·”’l
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
	
	/**
	 * Area‚ğ‰ñ“]‚µ‚Ä•¡»‚·‚é
	 * @param src •¡»Œ³Area
	 * @param dst •¡»æArea
	 * @return dst‚Æ“¯‚¶
	 */
	public Area rotateArea(Area src, Area dst) {
		for (Iterator itr = src.iterator(); itr.hasNext(); ) {
			dst.add(rotateAddress((Address)itr.next()));
		}
		return dst;
	}

	/**
	 * ‚QŸŒ³intŒ^”z—ñ‚ğ‰ñ“]‚µ‚Ä•¡»‚·‚é
	 * •¡»æ”z—ñ‚Í‚ ‚ç‚©‚¶‚ßì‚Á‚Ä‚¨‚©‚È‚¢‚Æ‚¢‚¯‚È‚¢
	 * @param src •¡»Œ³‚QŸŒ³intŒ^”z—ñ
	 * @param dst •¡»æ‚QŸŒ³intŒ^”z—ñ
	 */
	public void rotateArrayInt2(int[][] src, int[][] dst) {
		Address address = new Address();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				address.set(r, c);
				address = rotateAddress(address);
				dst[address.r()][address.c()] = src[r][c];
			}
		}
	}
	
	/**
	 * •ÓÀ•WŒ^‚Ì‚RŸŒ³intŒ^”z—ñ‚ğ‰ñ“]‚µ‚Ä•¡»‚·‚é
	 * •¡»æ”z—ñ‚Í‚ ‚ç‚©‚¶‚ßì‚Á‚Ä‚¨‚©‚È‚¢‚Æ‚¢‚¯‚È‚¢
	 * @param src •¡»Œ³‚RŸŒ³intŒ^”z—ñ
	 * @param dst •¡»æ‚RŸŒ³intŒ^”z—ñ
	 */
	public void rotateArrayInt3(int[][][] src, int[][][] dst) {
		Rotator rotator2;
		switch (rotation) {
		case 0:
		case 2:
		case 5:
		case 7:
			rotator2 = new Rotator(rows, cols-1, rotation);
			rotator2.rotateArrayInt2(src[0], dst[0]);
			rotator2 = new Rotator(rows-1, cols, rotation);
			rotator2.rotateArrayInt2(src[1], dst[1]);
			break;
		case 1:
		case 3:
		case 4:
		case 6:
			rotator2 = new Rotator(rows, cols-1, rotation);
			rotator2.rotateArrayInt2(src[0], dst[1]);
			rotator2 = new Rotator(rows-1, cols, rotation);
			rotator2.rotateArrayInt2(src[1], dst[0]);
			break;
		}
	}
	
}
