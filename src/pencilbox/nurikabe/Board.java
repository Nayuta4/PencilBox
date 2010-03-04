package pencilbox.nurikabe;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.util.ArrayUtil;
import pencilbox.resource.Messages;


/**
 *  「ぬりかべ」盤面クラス
 */
public class Board extends BoardBase {

	public static final int SPACE = -1;
	public static final int WALL = -2;
	public static final int UNKNOWN = 0;
	public static final int UNDECIDED_NUMBER = -3;
	
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
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (!isNumber(r,c))
					state[r][c] = UNKNOWN;
				}
		}
		initBoard();
	}

	public void trimAnswer() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (state[r][c] == SPACE)
					state[r][c] = UNKNOWN;
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

	public boolean isNumber(int r, int c) {
		return isOn(r,c) && (state[r][c] > 0 || state[r][c] == UNDECIDED_NUMBER);
	}
	
	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
	}
	
	public boolean isWall(int r, int c) {
		return isOn(r,c) && (state[r][c] == WALL);
	}
	public boolean isUnknown(int r, int c) {
		return (state[r][c] == UNKNOWN);
	}
	public int getSpaceOrWall(int r, int c) {
		if (state[r][c] > 0)
			return SPACE;
		else if (state[r][c] == UNDECIDED_NUMBER)
			return SPACE;
		else
			return state[r][c];
	}
	public boolean isSpaceOrNumber(int r, int c) {
		return isOn(r,c) && (state[r][c] > 0 || state[r][c] == SPACE || state[r][c] == UNDECIDED_NUMBER);
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
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getState(r, c) != UNKNOWN && area[r][c] == null) {
					initArea(r,c);
				}
			}
		}
	}
	/**
	 * 指定したマスを起点としてマスのつながりを調べてAreaを作成する
	 * @param r
	 * @param c
	 */
	void initArea(int r, int c) {
		initializingArea = makeNewArea(r, c);
		initArea1(r, c);
	}

	private void initArea1(int r, int c) {
		if (!isOn(r, c))
			return;
		if (getArea(r, c) == initializingArea)
			return;
		if (getSpaceOrWall(r, c) != initializingArea.getAreaType())
			return;
		initializingArea.add(r, c);
		if (isNumber(r, c))
			initializingArea.addNumber(getState(r, c));
		setArea(r, c, initializingArea);
		initArea1(r - 1, c);
		initArea1(r, c - 1);
		initArea1(r + 1, c);
		initArea1(r, c + 1);
	}
	/**
	 * そのマスの所属する領域を取得する
	 * そのマスが領域に属していない場合は null を返す
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(int r, int c) {
		// mergeArea などから使用する場合のために，引数チェックを行う
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

	public void changeState(int r, int c, int st) {
		int prevSt = getSpaceOrWall(r, c);
		setState(r, c, st);
		int type;
		if (st > 0)
			type = SPACE;
		else
			type = st;
		if (prevSt != UNKNOWN) {
			splitArea(r, c, prevSt);
		}
		if (st != UNKNOWN) {
			mergeArea(r, c, type);
		}
	}
	
	public void changeState(Address pos, int st) {
		changeState(pos.r(), pos.c(), st);
	}
	/**
	 * マスの状態を指定した状態に変更し，変更をアンドゥリスナーに通知する
	 * @param pos マス座標
	 * @param st 変更後の状態
	 */
	public void changeStateA(Address pos, int st) {
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(pos.r(), pos.c(), getState(pos), st)));
		changeState(pos, st);
	}

	/**
	 * マスの状態を変更したときの Area 併合処理を行う
	 * @param r 変更したマスの行座標
	 * @param c 変更したマスの列座標
	 * @param type 変更後の領域の種類
	 */
	void mergeArea(int r, int c, int type) {
		Area mergedArea = null;
		mergedArea = mergeArea1(getArea(r-1, c), mergedArea, type);
		mergedArea = mergeArea1(getArea(r, c-1), mergedArea, type);
		mergedArea = mergeArea1(getArea(r+1, c), mergedArea, type);
		mergedArea = mergeArea1(getArea(r, c+1), mergedArea, type);
		if (mergedArea == null) {
			mergedArea = makeNewArea(r,c);
		}
		mergedArea.add(r,c);
		if (isNumber(r,c))
			mergedArea.addNumber(getState(r,c));
		setArea(r, c, mergedArea);
	}

	private Area mergeArea1(Area a, Area mergedArea, int type) {
		if (a != null && a.getAreaType() == type) {
			if (mergedArea == null){
				mergedArea = a;
			} else if (mergedArea != a) {
				mergedArea.addAll(a);
				for (Address pos : a) {
					setArea(pos.r(), pos.c(), mergedArea);
					if (isNumber(pos.r(),pos.c()))
						mergedArea.addNumber(getState(pos.r(),pos.c()));
				}
				removeAreaFromList(a);
			}
		}
		return mergedArea;
	}
	/**
	 * マスの状態を変更，消去したときの Areaの分割処理を行う
	 * @param r 変更したマスの行座標
	 * @param c 変更したマスの列座標
	 * @param type 変更後の領域の種類
	 */
	void splitArea(int r, int c, int type) {
		Area oldArea = getArea(r, c);
		Area largerArea = null;
		removeAreaFromList(oldArea);
		for (Address pos : oldArea) {
			setArea(pos.r(), pos.c(), null);
		}
		if (isOn(r-1,c) && getSpaceOrWall(r-1,c)==type && getArea(r-1,c) == null) {
			initArea(r-1,c);
			if (largerArea == null || initializingArea.size() > largerArea.size())
				largerArea = initializingArea;
		}
		if (isOn(r,c-1) && getSpaceOrWall(r,c-1)==type && getArea(r,c-1) == null) {
			initArea(r,c-1);
			if (largerArea == null || initializingArea.size() > largerArea.size())
				largerArea = initializingArea;
		}
		if (isOn(r+1,c) && getSpaceOrWall(r+1,c)==type && getArea(r+1,c) == null) {
			initArea(r+1,c);
			if (largerArea == null || initializingArea.size() > largerArea.size())
				largerArea = initializingArea;
		}
		if (isOn(r,c+1) && getSpaceOrWall(r,c+1)==type && getArea(r,c+1) == null) {
			initArea(r,c+1);
			if (largerArea == null || initializingArea.size() > largerArea.size())
				largerArea = initializingArea;
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
	private Area makeNewArea(int r, int c) {
		if (isWall(r,c)) {
			Area a = new Area(WALL);
			wallAreaList.add(a);
			return a;
		} else if (isSpaceOrNumber(r,c)) {
			Area a = new Area(SPACE);
			spaceAreaList.add(a);
			return a;
		} else {
			return null;
		}
	}

	private void removeAreaFromList(Area a) {
		int type = a.getAreaType();
		if (type == WALL)
			wallAreaList.remove(a);
		else if (type == SPACE) {
			spaceAreaList.remove(a);
		}
	}

	/**
	 * そのマスが2ｘ2の黒マスブロックの一角かどうかを調べる
	 * @param r
	 * @param c
	 * @return 2x2ブロックならば true
	 */
	boolean is2x2Block(int r, int c) {
		if (isWall(r,c)) {
			if (isWall(r-1,c)) {
				if (isWall(r,c-1)) {
					if (isWall(r-1,c-1)) {
						return true;
					}
				}
				if (isWall(r,c+1)) {
					if (isWall(r-1,c+1)) {
						return true;
					}
				}
			}
			if (isWall(r+1,c)) {
				if (isWall(r,c-1)) {
					if (isWall(r+1,c-1)) {
						return true;
					}
				}
				if (isWall(r,c+1)) {
					if (isWall(r+1,c+1)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public int checkAnswerCode() {
		int result = 0;
		for (int r=rows()-1; r>=0; r--) {
			for (int c=cols()-1; c>=0; c--) {
//				if (isUnknown(r,c)) {
//					result |= 1;
//				}
				if (is2x2Block(r,c)) {
					result |= 64;
				}
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
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (getState(r, c) != WALL && whiteArea[r][c] == null) {
					initializingArea = new Area(SPACE);
					initWhiteArea1(r, c);
					ret |= checkSpaceArea(initializingArea);
				}
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

	private void initWhiteArea1(int r, int c) {
		if (!isOn(r, c))
			return;
		if (whiteArea[r][c] == initializingArea)
			return;
		if (getState(r, c) == WALL)
			return;
		initializingArea.add(r, c);
		if (isNumber(r, c))
			initializingArea.addNumber(getState(r, c));
		whiteArea[r][c] = initializingArea;
		initWhiteArea1(r - 1, c);
		initWhiteArea1(r, c - 1);
		initWhiteArea1(r + 1, c);
		initWhiteArea1(r, c + 1);
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
