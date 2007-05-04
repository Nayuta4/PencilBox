package pencilbox.lits;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.util.ArrayUtil;


/**
 * 「ＬＩＴＳ」盤面クラス
 */
public class Board extends BoardBase {
	
	public static final int WHITE = -1;
	public static final int BLACK = -2;
	public static final int UNKNOWN = 0;
	
	private List areaList;
	private Area[][] area;
	private int[][] state;
	private List wallList;
	private Wall[][] wall;
	private Wall initializingWall;

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList();
		wall = new Wall[rows()][cols()];
		wallList = new LinkedList();
	}

	public void clearBoard() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				state[r][c] = UNKNOWN;
			}
		}
		initBoard();
	}

	public void trimAnswer() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (state[r][c] == WHITE)
					state[r][c] = UNKNOWN;
				}
		}
	}

	/**
	 * Get state of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the state.
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}
	/**
	 * Set state to a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param st The state to set.
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	
	/**
	 * 引数の座標が黒マスかどうか。
	 * @param r 行座標
	 * @param c 列座標
	 * @return 黒マスなら true を返す。
	 */
	public boolean isBlack(int r, int c) {
		return isOn(r, c) && (state[r][c] == BLACK);
	}
	/**
	 * そのマスの所属する領域を取得する
	 * そのマスが領域に属していない場合は null を返す
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the area.
	 */
	public Wall getWall(int r, int c ) {
		return wall[r][c];
	}
	/**
	 * 盤上のマスに，そのマスの所属する領域を設定する
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setWall(int r, int c, Wall a) {
		wall[r][c] = a;
	}
	
	/**
	 * そのマスの所属する領域を取得する
	 * そのマスが領域に属していない場合は null を返す
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(int r, int c ) {
		if (!isOn(r, c))
			return null;
		return area[r][c];
	}
	
	/**
	 * 盤上のマスに，そのマスの所属する領域を設定する
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c, Area a) {
		area[r][c] = a;
	}
	
	/**
	 * 新しい領域を追加する
	 * @param newArea
	 */
	public void addArea(Area newArea) {
		for (Iterator itr = newArea.iterator(); itr.hasNext();) {
			Address pos = (Address) itr.next();
			area[pos.r()][pos.c()] = newArea;
		}
		areaList.add(newArea);
	}

	/**
	 * 領域を削除する
	 * @param oldArea
	 */
	public void removeArea(Area oldArea) {
		for (Iterator itr = oldArea.iterator(); itr.hasNext();) {
			Address pos = (Address) itr.next();
			if (area[pos.r()][pos.c()] == oldArea)
				area[pos.r()][pos.c()] = null;
		}
		areaList.remove(oldArea);
	}
	/**
	 * マスを領域に追加する
	 * @param pos 追加するマスの座標
	 * @param a 追加される領域
	 */
	public void addCellToArea(Address pos, Area a) {
		if (a.isEmpty()) {
			areaList.add(a);
		}
		setArea(pos.r(), pos.c(), a);
		a.add(pos);
//		initArea(a);
	}
	/**
	 * マスを領域から取り除く
	 * @param pos 取り除くマスの座標
	 * @param a 取り除かれる領域
	 */
	public void removeCellFromArea(Address pos, Area a) {
		setArea(pos.r(), pos.c(), null);
		a.remove(pos);
		if (a.isEmpty()) {
			areaList.remove(a);
		} else {
//			initArea(a);
		}
	}

	public void changeState(int r, int c, int st) {
		int prevSt = getState(r, c);
		setState(r, c, st);
		Area a = getArea(r, c);
		if (a != null) {
			if (prevSt == BLACK) {
				a.getTetromino().remove(r, c);
			}
			if (st == BLACK) {
				a.getTetromino().add(r, c);
			}
		}
	}
	
	/**
	 * マスの状態を指定した状態に変更し，変更をアンドゥリスナーに通知する
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 変更後の状態
	 */
	public void changeStateA(int r, int c, int st) {
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(r, c, state[r][c], st)));
		changeState(r, c, st);
	}

	/**
	 * マスの状態を 未定 ⇔ st と変更する
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 切り替える状態
	 */
	public void toggleState(int r, int c, int st) {
		if (state[r][c] == st)
			changeStateA(r, c, UNKNOWN);
		else
			changeStateA(r, c, st);
	}
	
	/**
	 * @return Returns the areaList.
	 */
	List getAreaList() {
		return areaList;
	}

	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}
	
	public void initBoard() {
		initTetrominos();
	}

	/**
	 * そのマスが2ｘ2の黒マスブロックの一角かどうかを調べる
	 * @param r
	 * @param c
	 * @return 2x2ブロックならば true
	 */
	boolean is2x2Block(int r, int c) {
		if (isBlack(r, c)) {
			if (isBlack(r-1, c)) {
				if (isBlack(r, c-1)) {
					if (isBlack(r-1, c-1)) {
						return true;
					}
				}
				if (isBlack(r, c+1)) {
					if (isBlack(r-1, c+1)) {
						return true;
					}
				}
			}
			if (isBlack(r+1, c)) {
				if (isBlack(r, c-1)) {
					if (isBlack(r+1, c-1)) {
						return true;
					}
				}
				if (isBlack(r, c+1)) {
					if (isBlack(r+1, c+1)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public int checkAnswerCode() {
		int result = 0;
		if (areaList.size() == 0)
			result |= 1;
		// ブロックごとにテトロミノがあるかのチェック
		result |= checkTetrominos();
		// 同一形テトロミノ隣接のチェック（前のチェックに合格したときのみ）
		if ((result & 8) == 0)
			result |= checkAdjacentCongruousTetrominos();
		// 黒マス単連結のチェック
		result |= checkConnection();
		// 2x2のチェック
		result |= check2x2s();
		return result;
	}
	
	public int checkTetrominos() {
		int result = 0;
		int[] count = new int[6];
		Area a;
		for (Iterator itr = areaList.iterator(); itr.hasNext(); ) {
			a = (Area) itr.next();
			count[a.getTetrominoType()] ++;
		}
//		System.out.println("L\tI\tT\tS\tO\tother");
//		for (int i = 0; i < 6; i++)
//			System.out.print(count[(i+1)%6]+"\t");
//		System.out.println();
		if (count[0] > 0)
			result = 8;
		return result;
	}
	
	/**
	 * 現在の盤面状態に基づいて，領域の設定を行う
	 */
	public void initTetrominos() {
		for (Iterator itr = areaList.iterator(); itr.hasNext(); ) {
			((Area) itr.next()).getTetromino().clear();
		}
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getArea(r, c) != null) {
					if (getState(r, c) == BLACK) {
						getArea(r, c).getTetromino().add(r, c);
					}
				}
			}
		}
	}
	
	private int checkAdjacentCongruousTetrominos() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getArea(r, c) != null) {
					if (getState(r, c) == BLACK) {
						if (getArea(r, c+1) != null) {
							if (getArea(r, c) != getArea(r, c+1)) {
								if (getState(r, c+1) == BLACK) {
									if (getArea(r, c).getTetrominoType() == getArea(r, c+1).getTetrominoType()) {
										result = 16;
									}
								}
							}
						}
						if (getArea(r+1,c) != null) {
							if (getArea(r, c) != getArea(r+1, c)) {
								if (getState(r+1, c) == BLACK) {
									if (getArea(r, c).getTetrominoType() == getArea(r+1, c).getTetrominoType()) {
										result = 16;
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	private int checkConnection() {
		ArrayUtil.initArrayObject2(wall, null);
		wallList.clear();
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getState(r, c) == BLACK && getWall(r, c) == null) {
					if (wallList.size() > 0)
						return 32;
					initWall(r,c);
				}
			}
		}
		return 0;
	}
	/**
	 * あるマスを含む Wall の初期化
	 * @param r
	 * @param c
	 */
	private void initWall(int r, int c) {
		initializingWall = new Wall();
		initializingWall.add(r, c);
		setWall(r, c, initializingWall);
		wallList.add(initializingWall);
		initWall1(r, c);
	}

	private void initWall1(int r, int c) {
		if (initWall2(r-1,c) == true)
			initWall1(r-1,c);
		if (initWall2(r,c-1) == true)
			initWall1(r,c-1);
		if (initWall2(r+1,c) == true)
			initWall1(r+1,c);
		if (initWall2(r,c+1) == true)
			initWall1(r,c+1);
	}
	
	private boolean initWall2(int r, int c) {
		if (!isBlack(r,c))
			return false;
		if (getWall(r,c) == initializingWall)
			return false;
		initializingWall.add(r,c);
		setWall(r, c, initializingWall);
		return true;
	}
	
	private int check2x2s() {
		int result = 0;
		for (int r=rows()-1; r>=0; r--) {
			for (int c=cols()-1; c>=0; c--) {
				if (is2x2Block(r,c)) {
					result |= 64;
				}
			}
		}
		return result;
	}
	
	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append("ブロックが１つもない\n"); 
		if ((result & 2) == 2)
			;
		if ((result & 4) == 4)
			;
		if ((result & 8) == 8)
			message.append("テトロミノを含まないブロックがある\n"); 
		if ((result & 16) == 16)
			message.append("同じ形のテトロミノが隣接している\n"); 
		if ((result & 32) == 32)
			message.append("黒マスがひとつながりになっていない\n"); 
		if ((result & 64) == 64)
			message.append("黒マスが２ｘ２マスのカタマリになっている\n"); 
		return message.toString();
	}

	/**
	 * １手の操作を表すクラス
	 * UNDO, REDO での編集の単位となる
	 */
	class Step extends AbstractUndoableEdit {

		private int row;
		private int col;
		private int before;
		private int after;

		/**
		 * コンストラクタ
		 * @param r 変更されたマスの行座標
		 * @param c 変更されたマスの列座標
		 * @param b 変更前の状態
		 * @param a 変更後の状態
		 */
		public Step(int r, int c, int b, int a) {
			super();
			row = r;
			col = c;
			before = b;
			after = a;
		}

		public void undo() throws CannotUndoException {
			super.undo();
			changeState(row, col, before);
		}

		public void redo() throws CannotRedoException {
			super.redo();
			changeState(row, col, after);
		}
	}
	
}
