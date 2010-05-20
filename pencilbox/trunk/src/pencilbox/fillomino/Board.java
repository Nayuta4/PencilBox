package pencilbox.fillomino;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellNumberEditStep;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 *  「フィルオミノ」盤面クラス
 */
public class Board extends BoardBase {

	static final int UNSTABLE = 0;
	static final int STABLE = 1;
	static final int UNKNOWN = 0;
	static final int UNDETERMINED = -2;

	private int[][] state; // 問題の数字:1, 解答すべき数字:0,
	private int[][] number;
	
	private Area[][] area;
	private List<Area> areaList;
	private Area initializingArea;

	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		number = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (!isStable(p))
				setNumber(p, 0);
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
	
	public boolean isStable(Address p) {
		return isStable(p.r(), p.c());
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

	public int getState(Address p) {
		return getState(p.r(), p.c());
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

	public void setState(Address p, int st) {
		setState(p.r(), p.c(), st);
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

	public int getNumber(Address p) {
		return getNumber(p.r(), p.c());
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

	public void setNumber(Address p, int n) {
		setNumber(p.r(), p.c(), n);
	}

	public void setFixedNumber(int r, int c, int n) {
		state[r][c] = Board.STABLE;
		if (n == Board.UNDETERMINED)
			n=0;
		number[r][c] = n;
	}
	public void setFixedNumber(Address p, int n) {
		setFixedNumber(p.r(), p.c(), n);
	}
	public int getFixedNumber(Address p) {
		int n = 0;
		if (isStable(p)) {
			n = getNumber(p);
			if (n == 0)
				n = Board.UNDETERMINED;
			return n;
		} else {
			return 0;
		}
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
		for (Address p : cellAddrs()) {
			if (getNumber(p) > 0 && getArea(p) == null) {
				initArea(p);
			}
		}
	}
	/**
	 * 指定したマスを起点としてマスのつながりを調べてAreaを作成する
	 * @param p
	 */
	void initArea(Address p) {
		initializingArea = new Area(getNumber(p));
		initArea1(p);
		areaList.add(initializingArea);
	}

	private void initArea1(Address p) {
		if (!isOn(p))
			return;
		if (getArea(p) == initializingArea)
			return;
		if (getNumber(p) != initializingArea.getNumber())
			return;
		initializingArea.add(p);
		setArea(p, initializingArea);
		for (int d=0; d<4; d++) {
			Address p1 = Address.nextCell(p, d);
			initArea1(p1);
		}
		return;
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
	public Area getArea(Address p) {
		return getArea(p.r(), p.c());
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
	public void setArea(Address p, Area a) {
		setArea(p.r(), p.c(), a);
	}
	/**
	 * マスに数字を入力し，アドゥリスナーに通知する
	 * @param p マス座標
	 * @param n 入力する数字
	 */
	public void changeNumber(Address p, int n) {
		if (n < 0)
			return;
		if (n == getNumber(p)) 
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellNumberEditStep(p, getNumber(p), n));
		changeNumber1(p, n);
	}

	private void changeNumber1(Address p, int n) {
		int prevNum = getNumber(p);
		setNumber(p, n);
		if (prevNum>0) {
			splitArea(p, prevNum);
		}
		if (n>0) {
			mergeArea(p, n);
		}
	}
	/**
	 * マスに数字を入力し，アドゥリスナーに通知する
	 * @param p マス座標
	 * @param n 入力する数字
	 */
	public void changeFixedNumber(Address p, int n) {
		if (n == Board.UNKNOWN)
			setState(p, Board.UNSTABLE);
		else
			setState(p, Board.STABLE);
		if (n == Board.UNDETERMINED)
			n = 0;
		changeNumber1(p, n);
	}

	public void undo(AbstractStep step) {
		CellNumberEditStep s = (CellNumberEditStep) step;
		if (isStable(s.getPos()))
			return;
		changeNumber(s.getPos(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		CellNumberEditStep s = (CellNumberEditStep) step;
		if (isStable(s.getPos()))
			return;
		changeNumber(s.getPos(), s.getAfter());
	}

	/**
	 * 数字を設定，変更したときの Area 併合処理を行う
	 * @param r 変更したマスの行座標
	 * @param c 変更したマスの列座標
	 * @param number 変更後の数字
	 */
	void mergeArea(Address p, int number) {
		Area mergedArea = null;
		for (int d=0; d<4; d++) {
			Address p1 = Address.nextCell(p, d);
			mergedArea = mergeArea1(getArea(p), mergedArea, number);
		}
		if (mergedArea == null) {
			mergedArea = new Area(number);
			areaList.add(mergedArea);
		}
		mergedArea.add(p);
		setArea(p, mergedArea);
	}
	private Area mergeArea1(Area area, Area mergedArea, int number) {
		if (area != null && area.getNumber() == number) {
			if (mergedArea == null){
				mergedArea = area;
			} else if (mergedArea != area) {
				mergedArea.addAll(area);
				for (Address p : area) {
					setArea(p.r(), p.c(), mergedArea);
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
	void splitArea(Address p0, int number) {
		areaList.remove(getArea(p0));
		for (Address p : getArea(p0)) {
			setArea(p, null);
		}
		for (int d=0; d<4; d++) {
			Address p = Address.nextCell(p0, d);
			if (isOn(p) && getNumber(p)==number && getArea(p) == null)
				initArea(p);
		}
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++){
				if (isUnknown(r,c))
					result |= 1;
			}
		}
		for (Area area : areaList) {
			int status = area.getStatus();
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
			return Messages.getString("fillomino.AnswerCheckMessage1"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
		if ((result & 2) == 2)
			message.append(Messages.getString("fillomino.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("fillomino.AnswerCheckMessage3")); //$NON-NLS-1$
		return message.toString();
	}
}
