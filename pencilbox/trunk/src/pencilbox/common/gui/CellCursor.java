package pencilbox.common.gui;

import pencilbox.common.core.Address;
/**
 * カーソルクラス
 * 盤面の入力対象となっているマスを示すのに用いる
 */
public class CellCursor {
	
	protected Address pos;

	/**
	 * カーソルを作成する
	 * Panelと関連付ける
	 * @param panel
	 */
	public CellCursor() {
		pos = new Address();
	}
	/**
	 * 現在のカーソル位置の行座標を取得する
	 * @return 行座標
	 */
	public int r() {
		return pos.r();
	}
	/**
	 * 現在のカーソル位置の列座標を取得する
	 * @return 列座標
	 */
	public int c() {
		return pos.c();
	}
	/**
	 * 現在のカーソル位置を取得する
	 * @return カーソル位置
	 */
	public Address getPosition() {
		return new Address(pos);
	}
	/**
	 * @param r
	 * @param c
	 */
	public void setPosition(int r, int c) {
		pos.set(r, c);
	}
	
	public void resetPosition() {
		setPosition(0, 0);
	}
	/**
	 * @param address
	 */
	public void setPosition(Address address) {
		pos.set(address);
	}
	/**
	 * カーソルが引数に盤面座標を与えられたマスにいるか
	 * @param position 調べる盤面座標
	 * @return 	 カーソルがpositionにいれば true
	 */
	public boolean isAt(Address position) {
		return pos.equals(position);
	}
}
