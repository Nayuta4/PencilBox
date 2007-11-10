package pencilbox.bijutsukan;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.util.ArrayUtil;


/**
 * 「美術館」盤面クラス
 */
public class Board extends BoardBase {

	static final int NONUMBER_WALL = 5;
	static final int BULB = -2;
	static final int NOBULB = -3;
	static final int UNKNOWN = -1;
	static final int OUTER = 6;

	private int[][] state;
	private int[][] illuminatedH;
	private int[][] illuminatedV;

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()]; 
		illuminatedV = new int[rows()][cols()];
		illuminatedH = new int[rows()][cols()];
		ArrayUtil.initArrayInt2(state, UNKNOWN);
	}
	
	public void clearBoard() {
		super.clearBoard();
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (isFloor(r, c)) {
					setState(r,c,UNKNOWN);
				}
			}
		}
		initBoard();
	}

	public void trimAnswer() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getState(r, c) == NOBULB)
					setState(r, c, UNKNOWN);
				}
		}
	}

	public void initBoard() {
		initIlluminations();
	}
	/**
	 * 現在の照明配置をもとに，illuminatedV illuminatedHを再設定する
	 */
	void initIlluminations() {
		ArrayUtil.initArrayInt2(illuminatedV, 0);
		ArrayUtil.initArrayInt2(illuminatedH, 0);
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (isBulb(r,c)) {
					illuminate(r, c, true);
				}
			}
		}
	}
	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}
	/**
	 * マスの状態を取得する 
	 * @param r 行座標
	 * @param c 列座標
	 * @return 状態
	 */
	public int getState(int r, int c) {
		if (isOn(r,c)) return state[r][c];
		else return OUTER;
	}
	
	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * マスの状態のみを設定する
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 状態
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	
	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * そのマスが壁かどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 壁なら true
	 */
	public boolean isWall(int r, int c){
		return state[r][c]>=0 && state[r][c]<=4 || state[r][c]==NONUMBER_WALL;
	}
	/**
	 * そのマスが数字つきの壁かどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 壁なら true
	 */
	public boolean isNumberedWall(int r, int c){
		return state[r][c]>=0 && state[r][c]<=4;
	}
	/**
	 * そのマスが壁のないマスかどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 盤内でかつ壁でないなら true
	 */
	public boolean isFloor(int r, int c){
		return isOn(r,c) && (state[r][c] == UNKNOWN || state[r][c] == NOBULB || state[r][c] == BULB);
	}
	/**
	 * そのマスに照明が置かれているかどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 照明が置かれているなら true
	 */
	public boolean isBulb(int r, int c){
		return isOn(r,c) && state[r][c] == BULB;
	}
	/**
	 * そのマスの照明配置が未定かどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 未定なら true
	 */
	public boolean isUnknown(int r, int c) {
		return state[r][c] == UNKNOWN;
	}
	/**
	 * そのマスが横方向から照らされているか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 未定なら true
	 */
	public int getHorizIlluminated(int r, int c) {
		return illuminatedH[r][c];
	}
	/**
	 * そのマスが縦方向から照らされているか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 未定なら true
	 */
	public int getVertIlluminated(int r, int c) {
		return illuminatedV[r][c];
	}
	/**
	 * そのマスが同列の複数の照明により照らされているか？？？
	 * @param r 行座標
	 * @param c 列座標
	 * @return 未定なら true
	 */
	public boolean isMultiIlluminated(int r, int c) {
		return illuminatedV[r][c]>1 || illuminatedH[r][c] > 1;
	}
	/**
	 * マスが照らされているか，つまりそのマスの上下左右に照明があるかを調べる
	 * @param r 行座標
	 * @param c 列座標
	 * @return 照らされていれば true
	 */
	public boolean isIlluminated(int r, int c) {
			return (illuminatedV[r][c] > 0 || illuminatedH[r][c] > 0);
	}
	/**
	 * マスの照明配置が変更された場合に，上下左右の光線を更新する
	 * @param r0 照明配置が変更されたマスの行座標
	 * @param c0 照明配置が変更されたマスの列座標
	 * @param on 照明が配置されたときには true, 取り除かれたときは false 
	 */
	private void illuminate(int r0, int c0, boolean on) {
		int k = on ? 1 : -1;
		int r = r0;
		int c = c0;
		while (isFloor(r, c)) {
			r--;
		}
		r++;
		while (isFloor(r, c)) {
			illuminatedV[r][c] += k;
			r++;
		}
		r = r0;
		while(isFloor(r,c)) {
			c--;
		}
		c++;
		while(isFloor(r,c)) {
			illuminatedH[r][c] += k;
			c++;
		}
	}
	/**
	 * マスの状態を設定する
	 * マスからの光線を更新する
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 状態
	 */
	public void changeState(int r, int c, int st) {
		if (st == BULB && state[r][c] != BULB)
			illuminate(r, c, true);
		else if (state[r][c] == BULB && st != BULB)
			illuminate(r, c, false);
		setState(r, c, st);
	}
	/**
	 * マスの状態を指定した状態に変更し，変更をアンドゥリスナーに通知する
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 変更後の状態
	 */
	public void changeStateA(int r, int c, int st) {
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(r,c,getState(r,c),st)));
		changeState(r, c, st);
	}
	/**
	 * マスの状態を 未定 ⇔ st と切り替える
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 切り替える状態
	 */
	public void toggleState(int r, int c, int st) {
		if (isWall(r,c))
			return;
		if (getState(r,c) == st) {
			changeStateA(r, c, UNKNOWN);
		} else {
			changeStateA(r, c, st);
		}
	}

	/**
	 * 隣接する４マスの照明個数を調べる
	 * @param r 行座標
	 * @param c 列座標
	 * @return 隣接する４マスの照明個数
	 */
	public int countAdjacentBulbs(int r, int c) {
		int count = 0;
		if (isBulb(r-1,c)) count++;
		if (isBulb(r+1,c)) count++;
		if (isBulb(r,c-1)) count++;
		if (isBulb(r,c+1)) count++;
		return count;
	}
	/**
	 * 壁に隣接する4マスの照明個数が正しいかどうかを調査する
	 * @param r 行座標
	 * @param c 列座標
	 * @return 照明個数が数字と等しいなら，1, 
	 * 照明個数が多すぎるなら -1, 
	 * 照明個数が数字より小さいなら 0
	 */
	public int checkAdjacentBulbs(int r, int c) {
		int nBulb = countAdjacentBulbs(r,c);
		int number = getState(r,c);
		if (nBulb > number) {
			return -1;
		} else if (nBulb == number) {
			return 1;
		} else if (nBulb < number) {
			return 0;
		}
		return 0;
	}

	public int checkAnswerCode() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (!isWall(r,c)) {
					if (isMultiIlluminated(r,c)) {
						result |= 1;
					}
					else if (!isIlluminated(r,c)) {
						result |= 2;
					}
				}
				else if (isNumberedWall(r,c)) {
					if (countAdjacentBulbs(r,c) > getState(r,c)) {
						result |= 4;
					}
					else if (countAdjacentBulbs(r,c) < getState(r,c)) {
						result |= 8;
					}
				}
			}
		}
		return result;
	}
	
	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result&1) == 1)
			message.append(ERR_MULTI_ILLUMINATION);
		if ((result&2) == 2)
			message.append(YET_NOT_ILLUMINATED);
		if ((result&4) == 4 || (result&8) == 8)
			message.append(ERR_WRONG_NUMBER);
		return message.toString();
	}

	static final String ERR_MULTI_ILLUMINATION = "複数の照明のある列がある\n";
	static final String YET_NOT_ILLUMINATED = "照らされていないマスがある\n";
	static final String ERR_WRONG_NUMBER =  "照明個数の合っていない数字がある\n";

	
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
