package pencilbox.lits;

/**
 * u‚k‚h‚s‚rv—ÌˆæƒNƒ‰ƒX
 */
public class Area extends pencilbox.common.core.Area {

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
