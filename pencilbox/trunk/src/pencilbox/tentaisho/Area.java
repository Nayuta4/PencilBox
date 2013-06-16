package pencilbox.tentaisho;

import pencilbox.common.core.Address;


/**
 * 「天体ショー」領域クラス
 */
public class Area extends pencilbox.common.core.AreaBase {
	/**
	 * 領域に含まれる星の種類
	 * なし 0;  白星 1; 黒星 2; 複数星 -1:
	 */
	private int starType;
	private StarAddress starPos;	// 領域に含まれる星の座標 1個のみのときのみ意味がある

	/**
	 * @return Returns the starColor.
	 */
	int getStarType() {
		return starType;
	}

	/**
	 * @param starColor The starColor to set.
	 */
	void setStarType(int starColor) {
		this.starType = starColor;
	}

	/**
	 * @return Returns the starPos.
	 */
	Address getStarPos() {
		return starPos;
	}

	/**
	 * @param starPos The starPos to set.
	 */
	void setStarPos(StarAddress starPos) {
		this.starPos = starPos;
	}

	public String toString() {
		return "星数:" + starType + " 星座標:" + starPos.toString() +"\n領域"+ super.toString();	
	}

	/**
	 * 領域が星をひとつのみ含み，かつその中心点に位置する星に対して点対称かどうかを調べる
	 * @return 点対称ならば <tt>true</tt>
	 */
	public boolean isPointSymmetry() {
		if (starPos.isNowhere()) return false;
		Address posb;
		for (Address pos : this) {
			posb = getPointSymmericAddress(pos, starPos);
			if (!contains(posb)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 領域が与えられた中心点に対して点対称かどうか
	 */
//	private boolean isPointSymmetry(StarAddress center) {
//		Address posb;
//		for (Address pos : this) {
//			posb = getPointSymmericAddress(pos, center);
//			if (!contains(posb)) {
//				return false;
//			}
//		}
//		return true;
//	}
	/**
	 * position から center に対して点対称な Address を求める
	 * @param position 座標
	 * @param center 点対称中心となる座標
	 * @return cnterに対してpositionと点対称な座標
	 */
	public Address getPointSymmericAddress(Address position, StarAddress center) {
		return Address.address(center.r()-position.r(), center.c()-position.c());
	}

	/**
	 * 	領域の中心マスを返す
	 * @return 領域の中心マス
	 */
	public Address getCenterCell() {
		return Address.address(starPos.r()/2, starPos.c()/2);
	}

}

/*
 * 領域の形状による場合わけ
 *  複数の星を含む
 *  ひとつも星を含まない
 *  ひとつだけ星を含む
 * 		点対称だ
 * 		点対称でない
 */
