package pencilbox.common.gui;

import pencilbox.common.core.Address;
/**
 * カーソルクラス
 * 盤面の入力対象となっているマスを示すのに用いる
 */
public class CellCursor {
	
	protected Address pos;
	protected PanelEventHandler panel;

	/**
	 * カーソルを作成する
	 * Panelと関連付ける
	 * @param panel
	 */
	public CellCursor(PanelEventHandler panel) {
		pos = new Address();
		this.panel = panel;
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
		return pos;
	}
	/**
	 * @param r
	 * @param c
	 */
	public void setPosition(int r, int c) {
		if (panel.isOn(r, c)) {
			pos.set(r, c);
			panel.resetPreviousInput();
		}
	}
	/**
	 * @param address
	 */
	public void setPosition(Address address) {
		pos.set(address.r(), address.c());
	}
	/**
	 * 
	 */
	public void moveUp() {
		setPosition(pos.r() - 1, pos.c());
	}
	/**
	 * 
	 */
	public void moveLt() {
		setPosition(pos.r(), pos.c() - 1);
	}
	/**
	 * 
	 */
	public void moveDn() {
		setPosition(pos.r() + 1, pos.c());
	}
	/**
	 * 
	 */
	public void moveRt() {
		setPosition(pos.r(), pos.c() + 1);
	}
	protected int rows() {
		return panel.rows();
	}
	protected int cols() {
		return panel.cols();
	}
	protected boolean isOn(int r, int c) {
		return panel.isOn(r, c);
	}
	protected boolean isOn(int r, int c, int adjustRow, int adjustCol){
		return panel.isOn(r, c, adjustRow, adjustCol);
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
