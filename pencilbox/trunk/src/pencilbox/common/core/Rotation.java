package pencilbox.common.core;


/**
 * 座標回転計算用補助クラス
 */
/*
 * 問題ごとにいるのか？
 */
public class Rotation {
	
	private int rotation = 0;
	private int ROWS; // Boardの行数
	private int COLS; // Boardの列数
	private int rows; // Panelの行数
	private int cols; // Panelの列数

	/**
	 * @param size
	 */
	public void setSize(Size size) {
		this.ROWS = size.getRows();
		this.COLS = size.getCols();
		setRotation(rotation);
	}
	/**
	 * Panel表示の回転状態を設定する
	 * @param i 設定する数値
	 */
	public void setRotation(int i) {
		rotation = i;
		if (isTransposed()) {
			rows = COLS;
			cols = ROWS;
		} else {
			rows = ROWS;
			cols = COLS;
		}
	}
	/**
	 * 現在のPanel表示の回転状態を取得する
	 * @return 回転状態を表す数値
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * パネルの縦横がもとの縦横に対して転置されているかどうか
	 * @return 転置されていれば true
	 */
	public boolean isTransposed() {
		switch (rotation) {
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
	 * パネル上の整数値座標を盤上の座標に変換する
	 * @param pos
	 */
	public void p2b(Address pos) {
		switch (rotation) {
			case 0 :
				pos.set(pos.r, pos.c);
				break;
			case 1 :
				pos.set(pos.c, rows - 1 - pos.r);
				break;
			case 2 :
				pos.set(rows - 1 - pos.r, cols - 1 - pos.c);
				break;
			case 3 :
				pos.set(cols - 1 - pos.c, pos.r);
				break;
			case 4 :
				pos.set(pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.r, cols - 1 - pos.c);
				break;
			case 6 :
				pos.set(cols - 1 - pos.c, rows - 1 - pos.r);
				break;
			case 7 :
				pos.set(rows - 1 - pos.r, pos.c);
				break;
		}
	}
	/**
	 * パネル上の整数値座標を現在の回転表示状態に応じた盤面の座標に変換する
	 * その際に，盤面サイズは rows()+adjustRow, cols()+adjustCol であるとみなす
	 * @param pos 座標
	 * @param adjustRow 盤面行サイズに対する補正値
	 * @param adjustCol 盤面列サイズに対する補正値
	 */
	public void p2b(Address pos, int adjustRow, int adjustCol) {
		switch (rotation) {
			case 0 :
				pos.set(pos.r, pos.c);
				break;
			case 1 :
				pos.set(pos.c, rows + adjustRow - 1 - pos.r);
				break;
			case 2 :
				pos.set(rows + adjustRow - 1 - pos.r, cols + adjustCol - 1 - pos.c);
				break;
			case 3 :
				pos.set(cols + adjustCol - 1 - pos.c, pos.r);
				break;
			case 4 :
				pos.set(pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.r, cols + adjustCol - 1 - pos.c);
				break;
			case 6 :
				pos.set(cols + adjustCol - 1 - pos.c, rows + adjustRow - 1 - pos.r);
				break;
			case 7 :
				pos.set(rows + adjustRow - 1 - pos.r, pos.c);
				break;
		}
	}
	/**
	 * パネル上の整数値辺座標を盤上の辺座標に変換する
	 * @param pos
	 */
	public void p2bSide(SideAddress pos) {
		switch (rotation) {
			case 0 :
				pos.set(pos.d, pos.r, pos.c);
				break;
			case 1 :
				pos.set(pos.d ^ 1, pos.c, rows - 1 - pos.d - pos.r);
				break;
			case 2 :
				pos.set(pos.d, rows - 1 - pos.d - pos.r, cols - 1 - (pos.d ^ 1) - pos.c);
				break;
			case 3 :
				pos.set(pos.d ^ 1, cols - 1 - (pos.d ^ 1) - pos.c, pos.r);
				break;
			case 4 :
				pos.set(pos.d ^ 1, pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.d, pos.r, cols - 1 - (pos.d ^ 1) - pos.c);
				break;
			case 6 :
				pos.set(pos.d ^ 1, cols - 1 - (pos.d ^ 1) - pos.c, rows - 1 - pos.d - pos.r);
				break;
			case 7 :
				pos.set(pos.d, rows - 1 - pos.d - pos.r, pos.c);
				break;
		}
	}
	/**
	 * 盤面上の整数値座標を現在の回転表示状態に応じたパネル上の座標に変換する
	 * その際に，盤面サイズは rows()+adjustRow, cols()+adjustCol であるとみなす
	 * @param pos 座標
	 * @param adjustRow 盤面行サイズに対する補正値
	 * @param adjustCol 盤面列サイズに対する補正値
	 */
	public void b2p(Address pos, int adjustRow, int adjustCol) {
		switch (rotation) {
			case 0 :
				pos.set(pos.r, pos.c);
				break;
			case 1 :
				pos.set(COLS + adjustCol - 1 - pos.c, pos.r);
				break;
			case 2 :
				pos.set(
					ROWS + adjustRow - 1 - pos.r,
					COLS + adjustCol - 1 - pos.c);
				break;
			case 3 :
				pos.set(pos.c, ROWS + adjustRow - 1 - pos.r);
				break;
			case 4 :
				pos.set(pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.r, COLS + adjustCol - 1 - pos.c);
				break;
			case 6 :
				pos.set(
					COLS + adjustCol - 1 - pos.c,
					ROWS + adjustRow - 1 - pos.r);
				break;
			case 7 :
				pos.set(ROWS + adjustRow - 1 - pos.r, pos.c);
				break;
		}
	}
	/**
	 * 盤上の座標をパネル上の整数値座標に変換する
	 * @param pos
	 */
	public void b2p(Address pos) {
		switch (rotation) {
			case 0 :
				pos.set(pos.r, pos.c);
				break;
			case 1 :
				pos.set(COLS - 1 - pos.c, pos.r);
				break;
			case 2 :
				pos.set(ROWS - 1 - pos.r, COLS - 1 - pos.c);
				break;
			case 3 :
				pos.set(pos.c, ROWS - 1 - pos.r);
				break;
			case 4 :
				pos.set(pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.r, COLS - 1 - pos.c);
				break;
			case 6 :
				pos.set(COLS - 1 - pos.c, ROWS - 1 - pos.r);
				break;
			case 7 :
				pos.set(ROWS - 1 - pos.r, pos.c);
				break;
		}
	}
	/**
	 * 盤上の辺座標をパネル上の整数値辺座標に変換する
	 * @param pos
	 */
	public void b2pSide(SideAddress pos) {
		switch (rotation) {
			case 0 :
				pos.set(pos.d, pos.r, pos.c);
				break;
			case 1 :
				pos.set(pos.d ^ 1, COLS - 1 - (pos.d ^ 1) - pos.c, pos.r);
				break;
			case 2 :
				pos.set(pos.d, ROWS - 1 - pos.d - pos.r, COLS - 1 - (pos.d ^ 1) - pos.c);
				break;
			case 3 :
				pos.set(pos.d ^ 1, pos.c, ROWS - 1 - pos.d - pos.r);
				break;
			case 4 :
				pos.set(pos.d ^ 1, pos.c, pos.r);
				break;
			case 5 :
				pos.set(pos.d, pos.r, COLS - 1 - (pos.d ^ 1) - pos.c);
				break;
			case 6 :
				pos.set(pos.d ^ 1, COLS - 1 - (pos.d ^ 1) - pos.c, ROWS - 1 - pos.d - pos.r);
				break;
			case 7 :
				pos.set(pos.d, ROWS - 1 - pos.d - pos.r, pos.c);
				break;
		}
	}
	/**
	 * 盤上の方向をパネル上の方向に変換する
	 * @param direction 変換元の方向を表す数値
	 * @return 変換後の方向を表す数値
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
