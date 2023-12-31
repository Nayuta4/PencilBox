package pencilbox.nurikabe;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 *  「ぬりかべ」盤面クラス
 */
public class Board extends BoardBase {

	static final int SPACE = -1;
	static final int WALL = -2;
	static final int UNKNOWN = 0;
	static final int UNDECIDED_NUMBER = -3;

	private int[][] state;

	private Area[][] area; // 黒マスまたは確定白マス領域用
	private Area[][] whiteArea; // 空白マス含む白マス領域正解判定用
	private List<Area> wallAreaList;
	private List<Area> spaceAreaList;
	private Area initializingArea;

	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		whiteArea = new Area[rows()][cols()];
		wallAreaList = new LinkedList<Area>();
		spaceAreaList = new LinkedList<Area>();
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (!isNumber(p))
				setState(p, UNKNOWN);
		}
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == SPACE) {
				changeState(p, UNKNOWN);
			}
		}
		initBoard();
	}

	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}
	/**
	 * @param r row coordinate of the cell
	 * @param c column coordinate of the cell
	 * @return Returns the state of the cell.
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * @param r row coordinate of the cell
	 * @param c column coordinate of the cell
	 * @param st state to set
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}

	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}

	public boolean isNumber(Address p) {
		return isOn(p) && (getState(p) > 0 || getState(p) == UNDECIDED_NUMBER);
	}

	public boolean isWall(Address pos) {
		return isOn(pos) && (getState(pos) == WALL);
	}

	public int getSpaceOrWall(Address p) {
		int st = getState(p);
		if (st > 0)
			return SPACE;
		else if (st == UNDECIDED_NUMBER)
			return SPACE;
		else
			return st;
	}

	public boolean isSpaceOrNumber(Address p) {
		if (isOn(p)) {
			int st = getState(p);
			return (st > 0 || st == SPACE || st == UNDECIDED_NUMBER);
		} else {
			return false;
		}
	}

	public void initBoard() {
		initAreas();
	}
	/**
	 * 現在の盤面状態に基づいて，領域の設定を行う
	 */
	public void initAreas() {
		ArrayUtil.initArrayObject2(area, null);
		wallAreaList.clear();
		spaceAreaList.clear();
		for (Address p : cellAddrs()) {
			if (getState(p) != UNKNOWN && getArea(p) == null) {
				initArea(p);
			}
		}
	}
	/**
	 * 指定したマスを起点としてマスのつながりを調べてAreaを作成する
	 * @param p
	 */
	void initArea(Address p) {
		initializingArea = makeNewArea(p);
		initArea1(p);
	}

	private void initArea1(Address p) {
		if (!isOn(p))
			return;
		if (getArea(p) == initializingArea)
			return;
		if (getSpaceOrWall(p) != initializingArea.getAreaType())
			return;
		initializingArea.add(p);
		if (isNumber(p))
			initializingArea.addNumber(getState(p));
		setArea(p, initializingArea);
		for (int d=0; d<4; d++) {
			initArea1(p.nextCell(d));
		}
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
	 * マスの状態を指定した状態に変更し，変更をアンドゥリスナーに通知する
	 * @param p マス座標
	 * @param st 変更後の状態
	 */
	public void changeState(Address p, int st) {
		if (getState(p) == st)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, getState(p), st));
		int prevSt = getSpaceOrWall(p);
		setState(p, st);
		int type;
		if (st > 0)
			type = SPACE;
		else
			type = st;
		if (prevSt != UNKNOWN) {
			splitArea(p, prevSt);
		}
		if (st != UNKNOWN) {
			mergeArea(p, type);
		}
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep)step;
			changeState(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep)step;
			changeState(s.getPos(), s.getAfter());
		}
	}

	/**
	 * マスの状態を変更したときの Area 併合処理を行う
	 * 上下左右４マスの属するブロックのうち最も大きいブロックに他を追加する。なければ新しくブロックを作る。
	 * @param p 変更したマスの座標
	 * @param type 変更後の領域の種類
	 */
	void mergeArea(Address p, int type) {
		Area mergedArea = null;
		for (int d=0; d<4; d++) {
			Area a = getArea(p.nextCell(d));
			if (a != null && a.getAreaType() == type) {
				if (mergedArea == null || a.size() > mergedArea.size()) {
					mergedArea = a;
				}
			}
		}
		if (mergedArea == null) {
			mergedArea = makeNewArea(p);
		}
		for (int d=0; d<4; d++) {
			Area a = getArea(p.nextCell(d));
			if (a != null && a != mergedArea && a.getAreaType() == type) {
				mergedArea.addAll(a);
				for (Address pos : a) {
					setArea(pos, mergedArea);
					if (isNumber(pos))
						mergedArea.addNumber(getState(pos));
				}
				removeAreaFromList(a);
			}
		}
		mergedArea.add(p);
		if (isNumber(p))
			mergedArea.addNumber(getState(p));
		setArea(p, mergedArea);
	}
	/**
	 * マスの状態を変更，消去したときの Areaの分割処理を行う
	 * @param r 変更したマスの行座標
	 * @param c 変更したマスの列座標
	 * @param type 変更後の領域の種類
	 */
	void splitArea(Address p, int type) {
		Area oldArea = getArea(p);
		Area largerArea = null;
		removeAreaFromList(oldArea);
		for (Address p1 : oldArea) {
			setArea(p1, null);
		}
		for (int d=0; d<4; d++) {
			Address p1 = Address.nextCell(p, d);
			if (isOn(p1) && getSpaceOrWall(p1)==type && getArea(p1) == null) {
				initArea(p1);
				if (largerArea == null || initializingArea.size() > largerArea.size())
					largerArea = initializingArea;
			}
		}
		if (largerArea != null) {
			largerArea.setId(oldArea.getId());
		}
	}
	/**
	 * 引数に座標を与えたマスを含む Area を新しく作成する
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return 作成した領域
	 */
	private Area makeNewArea(Address p) {
		if (isWall(p)) {
			Area a = new Area(WALL);
			wallAreaList.add(a);
			return a;
		} else if (isSpaceOrNumber(p)) {
			Area a = new Area(SPACE);
			spaceAreaList.add(a);
			return a;
		} else {
			return null;
		}
	}

	private void removeAreaFromList(Area a) {
		int type = a.getAreaType();
		if (type == WALL) {
			wallAreaList.remove(a);
		} else if (type == SPACE) {
			spaceAreaList.remove(a);
		}
	}

	/**
	 * そのマスが2ｘ2の黒マスブロックの一角かどうかを調べる
	 * @param r
	 * @param c
	 * @return 2x2ブロックならば true
	 */
	boolean is2x2Block(Address p) {
		if (isWall(p)) {
			for (int d = 0; d < 4; d++) {
				Address p1 = Address.nextCell(p, d);
				if (isWall(p1)) {
					Address p2 = Address.nextCell(p1, (d+1)%4);
					if (isWall(p2)) {
						Address p3 = Address.nextCell(p2, (d+2)%4);
						if (isWall(p3)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (is2x2Block(p)) {
				result |= 64;
			}
		}
		if (wallAreaList.size() > 1) {
			result |= 32;
		}
//		result |= checkSpaceAreas();
		result |= checkWhiteAreas();
		return result;
	}

//	/**
//	 * 確定白マスのみを白マスとみなして，白マス領域に関して正解判定する。
//	 */
//	private int checkSpaceAreas() {
//		int ret = 0;
//		for (Area a : spaceAreaList) {
//			ret |= checkSpaceArea(a);
//		}
//		return ret;
//	}

	/**
	 * 空白マスと確定白マスをともとを区別せず白マスとみなして，白マス領域に関して正解判定する。
	 */
	private int checkWhiteAreas() {
		ArrayUtil.initArrayObject2(whiteArea, null);
		int ret = 0;
		for (Address p : cellAddrs()) {
			if (getState(p) != WALL && whiteArea[p.r()][p.c()] == null) {
				initializingArea = new Area(SPACE);
				initWhiteArea1(p);
				ret |= checkSpaceArea(initializingArea);
			}
		}
		return ret;
	}

	private int checkSpaceArea(Area a) {
		int ret = 0;
		int number = a.getNumber();
		if (number == 0) {
			ret |= 16;
		} else if (number == Area.MULTIPLE_NUMBER) {
			ret |= 8;
		} else if (number == UNDECIDED_NUMBER) {
			;
		} else if (number > 0) {
			if (a.size() < number) {
				ret |= 4;
			} else if (a.size() > number) {
				ret |= 2;
			}
		}
		return ret;
	}

	private void initWhiteArea1(Address p) {
		if (!isOn(p))
			return;
		if (whiteArea[p.r()][p.c()] == initializingArea)
			return;
		if (getState(p) == WALL)
			return;
		initializingArea.add(p);
		if (isNumber(p))
			initializingArea.addNumber(getState(p));
		whiteArea[p.r()][p.c()] = initializingArea;
		for (int d=0; d<4; d++) {
			initWhiteArea1(p.nextCell(d));
		}
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return COMPLETE_MESSAGE;
		if (result==1)
			return Messages.getString("nurikabe.AnswerCheckMessage1"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
//		if ((result & 2) == 2)
//			message.append("数字より面積の大きいシマがある\n");
//		if ((result & 4) == 4)
//			message.append("数字より面積の小さいシマがある\n");
		if ((result & 2) == 2 || (result & 4) == 4)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage2"));  //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage4"));  //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage5"));  //$NON-NLS-1$
		if ((result & 32) == 32)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage6"));  //$NON-NLS-1$
		if ((result & 64) == 64)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage7"));  //$NON-NLS-1$
		return message.toString();
	}
}
