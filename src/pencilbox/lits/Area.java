package pencilbox.lits;

/**
 * 「ＬＩＴＳ」領域クラス
 */
public class Area extends pencilbox.common.core.AreaBase {

	private Tetromino tetromino = new Tetromino();

	/**
	 * @return the tetromino
	 */
	public Tetromino getTetromino() {
		return tetromino;
	}

	public int getTetrominoType() {
		return tetromino.getTetrominoType();
	}

}
