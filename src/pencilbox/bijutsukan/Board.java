package pencilbox.bijutsukan;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;
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

	public static boolean isWall(int st) {
		return (st >= 0 && st <= 4) || st == NONUMBER_WALL;
	}

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		illuminatedV = new int[rows()][cols()];
		illuminatedH = new int[rows()][cols()];
		ArrayUtil.initArrayInt2(state, UNKNOWN);
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (isFloor(p)) {
				setState(p, UNKNOWN);
			}
		}
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == NOBULB) {
				changeState(p, UNKNOWN);
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
		for (Address p : cellAddrs()) {
			if (getState(p) == Board.BULB) {
				illuminate(p, true);
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
	 * @param p マス座標
	 * @return 壁なら true
	 */
	public boolean isWall(Address p) {
		int n = getState(p);
		return n>=0 && n<=4 || n==NONUMBER_WALL;
	}
	/**
	 * そのマスが数字つきの壁かどうか
	 * @param p マス座標
	 * @return 壁なら true
	 */
	public boolean isNumberedWall(Address p){
		int n = getState(p);
		return n>=0 && n<=4;
	}
	/**
	 * そのマスが壁のないマスかどうか
	 * @param p マス座標
	 * @return 盤内でかつ壁でないなら true
	 */
	public boolean isFloor(Address p){
		if (isOn(p)) {
			int n = getState(p);
			return n == UNKNOWN || n == NOBULB || n == BULB;
		} else {
			return false;
		}
	}
	/**
	 * そのマスが横方向から照らされているか
	 * @param p マス座標
	 * @return 未定なら true
	 */
	public int getHorizIlluminated(Address p) {
		return illuminatedH[p.r()][p.c()];
	}
	/**
	 * そのマスが縦方向から照らされているか
	 * @param p マス座標
	 * @return 未定なら true
	 */
	public int getVertIlluminated(Address p) {
		return illuminatedV[p.r()][p.c()];
	}
	/**
	 * そのマスが同列の複数の照明により照らされているか？？？
	 * @param p マス座標
	 * @return 未定なら true
	 */
	public boolean isMultiIlluminated(Address p) {
		return illuminatedV[p.r()][p.c()]>1 || illuminatedH[p.r()][p.c()] > 1;
	}
	/**
	 * マスが照らされているか，つまりそのマスの上下左右に照明があるかを調べる
	 * @param p マス座標
	 * @return 照らされていれば true
	 */
	public boolean isIlluminated(Address p) {
			return (illuminatedV[p.r()][p.c()] > 0 || illuminatedH[p.r()][p.c()] > 0);
	}
	/**
	 * マスの照明配置が変更された場合に，上下左右の光線を更新する
	 * @param p0 照明配置が変更されたマスの座標
	 * @param on 照明が配置されたときには true, 取り除かれたときは false 
	 */
	private void illuminate(Address p0, boolean on) {
		int k = on ? 1 : -1;
		for (int d=0; d<4; d++) {
			Address p = p0;
			p = p0.nextCell(d);
			while (isFloor(p)) {
				increaseIlluminated(p, d, k);
				p = p.nextCell(d);
			}
		}
		increaseIlluminated(p0, 0, k);
		increaseIlluminated(p0, 1, k);
	}

	public void increaseIlluminated(Address p, int d, int k) {
		if ((d&1) == Direction.VERT) {
			illuminatedV[p.r()][p.c()] += k;
		} else if ((d&1) == Direction.HORIZ){
			illuminatedH[p.r()][p.c()] += k;
		}
	}

	/**
	 * マスの黒マス配置が変更された場合に，上下左右の照明からの光線を更新する
	 * @param p0 黒マス配置が変更されたマスの座標
	 * @param on 黒マス配置されたときには false, 取り除かれたときは true 
	 */
	private void illuminate4(Address p0, boolean on) {
		for (int d = 0; d < 4; d++) {
			Address p = p0.nextCell(d);
			while (isFloor(p)) {
				if (getState(p) == Board.BULB)
					illuminate(p, on);
				p = p.nextCell(d);
			}
		}
	}
	/**
	 * マスの状態を指定した状態に変更する
	 * マスからの光線を更新する
	 * 黒マスを操作したときは、上下４方向の明かりからの照明状態をすべて更新する
	 * @param p マス座標
	 * @param st 変更後の状態
	 */
	public void changeState(Address p, int st) {
		int prev = getState(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		if (isWall(st) || isWall(prev)) {
			illuminate4(p, false);
		}
		if (prev == BULB && st != BULB) {
			illuminate(p, false);
		}
		setState(p, st);
		if (st == BULB && prev != BULB) {
			illuminate(p, true);
		}
		if (isWall(st) || isWall(prev)) {
			illuminate4(p, true);
		}
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getAfter());
		}
	}

	/**
	 * 隣接する４マスの照明個数を調べる
	 * @param p マス座標
	 * @return 隣接する４マスの照明個数
	 */
	public int countAdjacentBulbs(Address p0) {
		int count = 0;
		for (int d=0; d<4; d++) {
			Address p = Address.nextCell(p0, d);
			if (getState(p) == Board.BULB) {
				count++;
			}
		}
		return count;
	}
	/**
	 * 壁に隣接する4マスの照明個数が正しいかどうかを調査する
	 * @param p マス座標
	 * @return 照明個数が数字と等しいなら，1, 
	 * 照明個数が多すぎるなら -1, 
	 * 照明個数が数字より小さいなら 0
	 */
	public int checkAdjacentBulbs(Address p) {
		int nBulb = countAdjacentBulbs(p);
		int number = getState(p);
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
		for (Address p : cellAddrs()) {
			if (!isWall(p)) {
				if (isMultiIlluminated(p)) {
					result |= 1;
				}
				else if (!isIlluminated(p)) {
					result |= 2;
				}
			} else if (isNumberedWall(p)) {
				if (countAdjacentBulbs(p) > getState(p)) {
					result |= 4;
				}
				else if (countAdjacentBulbs(p) < getState(p)) {
					result |= 8;
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

	static final String ERR_MULTI_ILLUMINATION = Messages.getString("bijutsukan.AnswerCheckMessage1"); //$NON-NLS-1$
	static final String YET_NOT_ILLUMINATED = Messages.getString("bijutsukan.AnswerCheckMessage2"); //$NON-NLS-1$
	static final String ERR_WRONG_NUMBER =  Messages.getString("bijutsukan.AnswerCheckMessage3"); //$NON-NLS-1$
}
