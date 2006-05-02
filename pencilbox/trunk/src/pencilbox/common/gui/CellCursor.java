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
		return pos.r;
	}
	/**
	 * 現在のカーソル位置の列座標を取得する
	 * @return 列座標
	 */
	public int c() {
		return pos.c;
	}
	/**
	 * 現在のカーソル位置を取得する
	 * @return カーソル位置
	 */
	public Address getPosition() {
		return pos;
	}
	/**
	 * カーソル位置の盤面座標を返す
	 * @return 	 カーソル位置の盤面座標を返す
	 */
	public Address getBoardPosition() {
		Address boardPos = new Address(pos);
		panel.p2b(boardPos);
		return boardPos;
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
		pos.set(address.r, address.c);
	}
	/**
	 * 
	 */
	public void moveUp() {
		setPosition(pos.r - 1, pos.c);
	}
	/**
	 * 
	 */
	public void moveLt() {
		setPosition(pos.r, pos.c - 1);
	}
	/**
	 * 
	 */
	public void moveDn() {
		setPosition(pos.r + 1, pos.c);
	}
	/**
	 * 
	 */
	public void moveRt() {
		setPosition(pos.r, pos.c + 1);
	}
	/**
	 * 
	 */
	public void moveLU() {
		setPosition(pos.r - 1, pos.c - 1);
	}
	/**
	 * 
	 */
	public void moveLD() {
		setPosition(pos.r + 1, pos.c - 1);
	}
	/**
	 * 
	 */
	public void moveRU() {
		setPosition(pos.r - 1, pos.c + 1);
	}
	/**
	 * 
	 */
	public void moveRD() {
		setPosition(pos.r + 1, pos.c + 1);
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
		return getBoardPosition().equals(position);
	}
}
