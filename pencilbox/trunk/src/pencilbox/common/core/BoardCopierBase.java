package pencilbox.common.core;

import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;


/**
 * 盤面複製クラスの親クラス
 */
public class BoardCopierBase {
	
	/**
	 * 盤面を複製する。
	 * @param src 複製元の盤面
	 * @return 複製した盤面
	 */
	public BoardBase duplicateBoard(BoardBase src) throws PencilBoxClassException {
		return duplicateBoard(src, 0);
	}
	/**
	 * 盤面を回転して複製する。
	 * @param src 複製元の盤面
	 * @return 複製した盤面
	 */
	public BoardBase duplicateBoard(BoardBase src, int n) throws PencilBoxClassException {
		BoardBase dst = (BoardBase) ClassUtil.createInstance(this.getClass(), ClassUtil.BOARD_CLASS);
		if (Rotator2.isTransposed(n)){
			dst.setSize(new Size(src.cols(), src.rows()));
		} else {
			dst.setSize(new Size(src.rows(), src.cols()));
		}
		copyBoardStates(src, dst, n);
		return dst;
	}
	
	/**
	 * 盤面をサイズ変更して複製する。
	 * @param src 複製元の盤面
	 * @param size 変更後のサイズ
	 * @return 複製した盤面
	 * @throws PencilBoxClassException
	 */
	public BoardBase duplicateBoard(BoardBase src, Size size) throws PencilBoxClassException {
		BoardBase dst = (BoardBase) ClassUtil.createInstance(this.getClass(), ClassUtil.BOARD_CLASS);
		dst.setSize(size);
		copyBoardStates(src, dst, 0);
		return dst;
	}
	
	/**
	 * 盤面状態を回転して複製する。
	 * 盤面前領域を対象とした複写を行う。
	 * そのように扱えない特殊な場合（カックロなど）については，各サブクラスで実装する。
	 * @param src 複製元の盤面
	 * @param dst 複製先の盤面
	 * @param n 回転番号
	 */
	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Address from = Address.address(0, 0);
		Address to = this.rotateCornerAddress(src.rows(), src.cols(), n);
		Area region = src.getWholeBoardArea();
		copyRegion(src, dst, region, from, to, n);
	}

	/**
	 * 盤面回転時の左上角座標の行き先の座標を求める
	 * @param rows 元盤面の行数
	 * @param cols 元盤面の列数
	 * @param rotation 回転番号
	 * @return
	 */
	private Address rotateCornerAddress(int rows, int cols, int rotation) {
		int R = 0;
		int C = 0;
		switch (rotation) {
			case 0 :
				R = 0;
				C = 0;
				break;
			case 1 :
				R = cols - 1;
				C = 0;
				break;
			case 2 :
				R = rows - 1;
				C = cols - 1;
				break;
			case 3 :
				R = 0;
				C = rows - 1;
				break;
			case 4 :
				R = 0;
				C = 0;
				break;
			case 5 :
				R = 0;
				C = cols - 1;
				break;
			case 6 :
				R = cols - 1;
				C = rows - 1;
				break;
			case 7 :
				R = rows - 1;
				C = 0;
				break;
		}
		return Address.address(R, C);
	}

	/**
	 * 領域消去。
	 * 具体的な処理はサブクラスで記述する。
	 * @param board 編集する盤面
	 * @param region 消去領域
	 */
	public void eraseRegion(BoardBase board, Area region) {
	}

	/**
	 * 領域複写。
	 * 具体的な処理はサブクラスで記述する。
	 * @param srcBoard 複写元盤面
	 * @param board 複写先盤面
	 * @param region 複写元領域
	 * @param from 複写元領域原点
	 * @param to 原点の複写先
	 * @param rotation 回転
	 */
	public void copyRegion(BoardBase srcBoard, BoardBase board, Area region, Address from, Address to, int rotation) {
	}

	/**
	 * 領域複写。
	 * 初めに盤面全体を複製してから複写処理を行う。
	 * @param board 編集する盤面
	 * @param region 複写元領域
	 * @param from 複写元領域原点
	 * @param to 原点の複写先
	 * @param rotation 回転
	 */
	public void copyRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
		try {
			BoardBase srcBoard = duplicateBoard(board);
			copyRegion(srcBoard, board, region, from, to, rotation);
		} catch (PencilBoxClassException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 領域移動。
	 * 初めに盤面全体を複製してから複写処理を行い，移動後の領域を消去する。
	 * @param board 編集する盤面
	 * @param region 移動元領域
	 * @param from 移動元領域原点
	 * @param to 原点の移動先
	 * @param rotation 回転
	 */
	public void moveRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
		try {
			BoardBase srcBoard = duplicateBoard(board);
			eraseRegion(board, region);
			copyRegion(srcBoard, board, region, from, to, rotation);
		} catch (PencilBoxClassException e) {
			e.printStackTrace();
		}
	}


	protected Address translateAndRotateAddress(Address pos, Address from, Address to, int rotation) {
		return Rotator2.translateAndRotateAddress(pos, from, to, rotation);
	}

}
