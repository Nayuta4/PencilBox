package pencilbox.fillomino;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.SideAddress;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 *  「フィルオミノ」盤面クラス
 */
public class Board extends BoardBase {

	public static final int BLANK = 0;
	public static final int UNKNOWN = 0;
	public static final int UNDETERMINED = -2;
	public static final int LINE = 1;
	public static final int NOLINE = 0;

	private int[][] state; // 解答の数字
	private int[][] number; // 問題の数字
	int[] edge;

	private Area[][] area;
	private List<Area> areaList;
	private Area initializingArea;

	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		number = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
		edge = new int[rows()*(cols()-1) + (rows()-1)*cols()];
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (!isStable(p))
				setState(p, 0);
		}
		initBoard();
		Arrays.fill(edge, 0);
	}

	public void trimAnswer() {
		for (SideAddress p : borderAddrs()) {
			if (getEdge(p) == LINE) {
				changeEdge(p, NOLINE);
			}
		}
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
		return number[r][c] != Board.BLANK;
	}
	
	public boolean isStable(Address p) {
		return isStable(p.r(), p.c());
	}
	/**
	 * Get state of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
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
	 * @param c Column coordinate of the cell.
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
	 * @param c Column coordinate of the cell.
	 * @return Returns the number.
	 */
	public int getNumber(int r, int c ) {
		return number[r][c];
	}

	public int getNumber(Address p) {
		return getNumber(p.r(), p.c());
	}

	public int getNumberOrState(Address p) {
		return isStable(p) ? getNumber(p) : getState(p);
	}
	/**
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param n The number to set.
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}

	public void setNumber(Address p, int n) {
		setNumber(p.r(), p.c(), n);
	}

	public void setEdge(SideAddress p, int i) {
		int d = p.d();
		int r = p.r();
		int c = p.c();
		int b = ( d==0 ? r*(cols()-1)+c : (rows()*(cols()-1) + r*cols()+c) );
		edge[b] = i;
	}

	public int getEdge(SideAddress p) {
		int d = p.d();
		int r = p.r();
		int c = p.c();
		int b = (d==0 ? r*(cols()-1)+c : (rows()*(cols()-1) + r*cols()+c));
		return edge[b];
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
			if (getNumberOrState(p) > 0 && getArea(p) == null) {
				initArea(p);
			}
		}
	}
	/**
	 * 指定したマスを起点としてマスのつながりを調べてAreaを作成する
	 * @param p
	 */
	void initArea(Address p) {
		initializingArea = new Area(getNumberOrState(p));
		initArea1(p);
		areaList.add(initializingArea);
	}

	private void initArea1(Address p) {
		if (!isOn(p))
			return;
		if (getArea(p) == initializingArea)
			return;
		if (getNumberOrState(p) != initializingArea.getNumber())
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
	 * @param p coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(Address p) {
		// mergeArea などから使用する場合のために，引数チェックを行う
		if (!isOn(p))
			return null;
		return area[p.r()][p.c()];
	}
	/**
	 * 盤上のマスに，そのマスの所属する領域を設定する
	 * @param p coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(Address p, Area a) {
		area[p.r()][p.c()] = a;
	}
	/**
	 * マスに解答数字を入力し，アドゥリスナーに通知する
	 * @param p マス座標
	 * @param n 入力する数字
	 */
	public void changeAnswerNumber(Address p, int n) {
		int prev = getState(p);
		if (n == prev) 
			return;
		if (isStable(p)) {
			changeFixedNumber(p, 0);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.NUMBER, p, prev, n));
		setState(p, n);
		changeNumber1(p, prev, n);
	}

	/**
	 * マスに問題数字を入力し，アドゥリスナーに通知する
	 * @param p マス座標
	 * @param n 入力する数字
	 */
	public void changeFixedNumber(Address p, int n) {
		int prev = getNumber(p);
		if (n == prev)
			return;
		if (getState(p) > 0) {
			changeAnswerNumber(p, 0);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, prev, n));
		setNumber(p, n);
		changeNumber1(p, prev, n);
	}

	private void changeNumber1(Address p, int prev, int n) {
		if (prev>0) {
			splitArea(p, prev);
		}
		if (n>0) {
			mergeArea(p, n);
		}
	}

	/**
	 * @param pos
	 * @param st
	 */
	public void changeEdge(SideAddress pos, int st) {
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new BorderEditStep(pos, getEdge(pos), st));
		}
		setEdge(pos, st);
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getBefore());
			} else if (s.getType() == EditType.FIXED) {
				changeFixedNumber(s.getPos(), s.getBefore());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getAfter());
			} else if (s.getType() == EditType.FIXED) {
				changeFixedNumber(s.getPos(), s.getAfter());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getAfter());
		}
	}

	/**
	 * 数字を設定，変更したときの Area 併合処理を行う
	 * @param p 変更したマスの座標
	 * @param number 変更後の数字
	 */
	void mergeArea(Address p, int number) {
		Area mergedArea = null;
		for (int d=0; d<4; d++) {
			mergedArea = mergeArea1(getArea(Address.nextCell(p, d)), mergedArea, number);
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
					setArea(p, mergedArea);
				}
				areaList.remove(area);
			}
		}
		return mergedArea;
	}
	/**
	 * 数字を変更，消去したときの Area 分割処理を行う
	 * @param p0 変更したマスの座標
	 * @param number 変更後の数字
	 */
	void splitArea(Address p0, int number) {
		areaList.remove(getArea(p0));
		for (Address p : getArea(p0)) {
			setArea(p, null);
		}
		for (int d=0; d<4; d++) {
			Address p = Address.nextCell(p0, d);
			if (isOn(p) && getNumberOrState(p)==number && getArea(p)==null)
				initArea(p);
		}
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getNumberOrState(p) <= 0)
				result |= 1;
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
