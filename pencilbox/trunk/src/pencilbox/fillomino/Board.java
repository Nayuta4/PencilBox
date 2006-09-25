package pencilbox.fillomino;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.util.ArrayUtil;


/**
 *  「フィルオミノ」盤面クラス
 */
public class Board extends BoardBase {

	static final int UNSTABLE = 0;
	static final int STABLE = 1;
	static final int UNKNOWN = 0;

	private int[][] state; // 問題の数字:1, 解答すべき数字:0,
	private int[][] number;
	
	private Area[][] area;
	private List areaList;
	private Area initializingArea;

	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		number = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList();
	}

	public void clearBoard() {
		super.clearBoard();
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (!isStable(r,c))
					number[r][c] = 0;
			}
		}
		initBoard();
	}

	/**
	 * @return the number
	 */
	int[][] getNumber() {
		return number;
	}

	/**
	 * @return the state
	 */
	int[][] getState() {
		return state;
	}

	/**
	 * そのマスは問題として数字を与えられたマスかどうか
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return 問題数字のマスなら true, 解答すべきマスなら false
	 */
	public boolean isStable(int r, int c) {
		return state[r][c] == STABLE;
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
	 * Get number of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the number.
	 */
	public int getNumber(int r, int c ) {
		return number[r][c];
	}
	/**
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param n The number to set.
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}
	/**
	 * マスに数字が入っていないかどうか
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns true if the cell is empty.
	 */
	public boolean isUnknown(int r, int c) {
		return number[r][c] == 0;
	}

	public void initBoard() {
		initAreas();
	}
	/**
	 * 現在の盤面状態に基づいて，領域の設定を行う
	 */
	public void initAreas() {
		ArrayUtil.initArrayObject2(area, null);
		areaList.clear();
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getNumber(r, c) > 0 && area[r][c] == null) {
					initArea(r,c);
				}
			}
		}
	}
	/**
	 * あるマスを含む Area の初期化
	 * domain[][] は消去されているものとする
	 * @param r
	 * @param c
	 */
	void initArea(int r, int c) {
		initializingArea = new Area(getNumber(r,c));
		initializingArea.add(r,c);
		setArea(r,c,initializingArea);
		areaList.add(initializingArea);
		initArea1(r, c);
	}

	private void initArea1(int r, int c) {
		if (initArea2(r-1,c) == true) initArea1(r-1,c);
		if (initArea2(r,c-1) == true) initArea1(r,c-1);
		if (initArea2(r+1,c) == true) initArea1(r+1,c);
		if (initArea2(r,c+1) == true) initArea1(r,c+1);
	}
	
	private boolean initArea2(int r, int c) {
		if (!isOn(r,c)) return false;
		if (getNumber(r,c) != initializingArea.getNumber()) return false;
		if (getArea(r,c) == initializingArea) return false;

		initializingArea.add(r,c);
		setArea(r, c, initializingArea);
		return true;
	}
	/**
	 * そのマスの所属する領域を取得する
	 * そのマスが領域に属していない場合は null を返す
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(int r, int c ) {
		// mergeArea などから使用する場合のために，引数チェックを行う
		if (!isOn(r,c))
			return null;
		return area[r][c];
	}
	/**
	 * 盤上のマスに，そのマスの所属する領域を設定する
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c,  Area a) {
		area[r][c] = a;
	}
	
	/**
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param n The number to set.
	 */
	public void changeNumber(int r, int c, int n) {
		int prevNum = getNumber(r, c);
		setNumber(r, c, n);
		if (prevNum>0) {
			splitArea(r, c, prevNum);
		}
		if (n>0) {
			mergeArea(r, c, n);
		}
	}
	/**
	 * マスに数字を入力し，アドゥリスナーに通知する
	 * @param r 行座標
	 * @param c 列座標
	 * @param n 入力する数字
	 */
	public void enterNumberA(int r, int c, int n) {
		if (n < 0)
			return;
		if (n == number[r][c]) 
			return;
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(r, c, number[r][c], n)));
		changeNumber(r, c, n);
	}
	/**
	 * マスの数字を1増加させる
	 * @param r 行座標
	 * @param c 列座標
	 */
	public void increaseNumber(int r, int c) {
		enterNumberA(r, c, number[r][c]+1);
	}
	/**
	 * マスの数字を１減少させる
	 * @param r 行座標
	 * @param c 列座標
	 */
	public void decreaseNumber(int r, int c) {
		if (number[r][c] <= 0) 
			return ;
		enterNumberA(r, c, number[r][c]-1);
	}

	/**
	 * 数字を設定，変更したときの Area 併合処理を行う
	 * @param r 変更したマスの行座標
	 * @param c 変更したマスの列座標
	 * @param number 変更後の数字
	 */
	void mergeArea(int r, int c, int number) {
		Area mergedArea = null;
		mergedArea = mergeArea1(getArea(r-1, c), mergedArea, number);
		mergedArea = mergeArea1(getArea(r, c-1), mergedArea, number);
		mergedArea = mergeArea1(getArea(r+1, c), mergedArea, number);
		mergedArea = mergeArea1(getArea(r, c+1), mergedArea, number);
		if (mergedArea == null) {
			mergedArea = new Area(number);
			areaList.add(mergedArea);
		}
		mergedArea.add(r,c);
		setArea(r, c, mergedArea);
	}
	private Area mergeArea1(Area area, Area mergedArea, int number) {
		if (area != null && area.getNumber() == number) {
			if (mergedArea == null){
				mergedArea = area;
			} else if (mergedArea != area) {
				mergedArea.addAll(area);
				for (Iterator itr = area.iterator(); itr.hasNext(); ) {
					Address pos = (Address) itr.next();
					setArea(pos.r(), pos.c(), mergedArea);
				}
				areaList.remove(area);
			}
		}
		return mergedArea;
	}
	/**
	 * 数字を変更，消去したときの Area 分割処理を行う
	 * @param r 変更したマスの行座標
	 * @param c 変更したマスの列座標
	 * @param number 変更後の数字
	 */
	void splitArea(int r, int c, int number) {
		areaList.remove(getArea(r,c));
		for (Iterator itr = getArea(r,c).iterator(); itr.hasNext(); ) {
			Address pos = (Address) itr.next();
			setArea(pos.r(), pos.c(), null);
		}
		if (isOn(r-1,c) && getNumber(r-1,c)==number && getArea(r-1,c) == null)
			initArea(r-1,c);
		if (isOn(r,c-1) && getNumber(r,c-1)==number && getArea(r,c-1) == null)
			initArea(r,c-1);
		if (isOn(r+1,c) && getNumber(r+1,c)==number && getArea(r+1,c) == null)
			initArea(r+1,c);
		if (isOn(r,c+1) && getNumber(r,c+1)==number && getArea(r,c+1) == null)
			initArea(r,c+1);
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++){
				if (isUnknown(r,c))
					result |= 1;
			}
		}
		for (Iterator itr = areaList.iterator(); itr.hasNext(); ) {
			int status = ((Area) itr.next()).getStatus();
			if (status == -1) result |= 2;
			else if (status == 0) result |= 4;
		}
		return result;
	}
	
	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		if (result == 1)
			return "空白マスがある\n";
		StringBuffer message = new StringBuffer();
		if ((result & 2) == 2)
			message.append("数字より面積の大きい領域がある\n");
		if ((result & 4) == 4)
			message.append("数字より面積の小さい領域がある\n");
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
			if (isStable(row, col))
				return;
			changeNumber(row,col,before);
		}
		public void redo() throws CannotRedoException {
			super.redo();
			if (isStable(row, col))
				return;
			changeNumber(row, col, after);
		}
		public boolean addEdit(UndoableEdit anEdit) {
			Step edit = (Step) anEdit;
			if (edit.row == row && edit.col == col) {
				after = edit.after;
				return true;
			} else {
				return false;
			}
		}
	
	}

}
