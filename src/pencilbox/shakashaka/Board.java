package pencilbox.shakashaka;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;

/**
 * 「シャカシャカ」盤面クラス
 */
public class Board extends BoardBase {

	public static final int NONUMBER_WALL = -2;
	public static final int WHITE = -3;
	public static final int UNKNOWN = 0;
	public static final int BLANK = -1;
	public static final int OUTER = 6;

	/**
	 * 三角形の塗られた側の方向を表す
	 */
	public static final int LTUP = 4; // 5;
	public static final int LTDN = 5; // 2;
	public static final int RTDN = 6; // 3;
	public static final int RTUP = 7; // 4;

	public static final Area nullArea = new Area();

	public static final boolean isNumber(int n) {
		return n >= 0 && n <= 4 || n == NONUMBER_WALL;
	}

	public static final boolean isTriangle(int s) {
		return s >= 4 && s <= 7;
	}

	public static final boolean isFloor(int s) {
		return s == UNKNOWN || s == WHITE;
	}

	private int[] number;

	private int[] state;
	private Area initializingArea; // initArea() での領域作成中に用いる仮の領域
	private Area[] areas;

	private List<Area> areaList = new LinkedList<Area>();

	protected void setup() {
		super.setup();
		state = new int[rows()*cols()];
		number = new int[rows()*cols()];
//		error = new int[rows()*cols()];
//		flag = new int[rows()*cols()];
		areas = new Area[rows()*cols()];
		Arrays.fill(number, BLANK);
		Arrays.fill(state, UNKNOWN);
		Arrays.fill(areas, nullArea);
	}

	public void clearBoard() {
		super.clearBoard();
		Arrays.fill(state, UNKNOWN);
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == WHITE) {
				changeState(p, UNKNOWN);
			}
		}
	}

	public void initBoard() {
		initAreas();
	}
	/**
	 * マスの状態を取得する
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return 状態
	 */
	public int getState(int r, int c) {
		return getState(cell(r, c));
	}

	public int getState(Address pos) {
		return getState(a2i(pos));
	}

	public int getState(int p) {
		if (isOn(p))
			return state[p];
		else
			return OUTER;
	}
	/**
	 * マスの状態を設定する
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 状態
	 */
	public void setState(int r, int c, int st) {
		setState(cell(r, c), st);
	}

	public void setState(Address pos, int st) {
		setState(a2i(pos), st);
	}

	private void setState(int p, int st) {
		if (isOn(p))
			state[p] = st;
	}

	public int getNumber(int r, int c) {
		return getNumber(cell(r, c));
	}

	public int getNumber(Address p) {
		return getNumber(a2i(p));
	}

	private int getNumber(int i) {
		if (isOn(i))
			return number[i];
		else
			return OUTER;
	}

	public void setNumber(Address p, int n) {
		number[cell(p)] = n;
	}

	public void setNumber(int r, int c, int n) {
		number[cell(r,c)] = n;
	}

	public Area getArea(Address p) {
		return areas[a2i(p)];
	}

	public void setArea(Address p, Area a) {
		areas[a2i(p)] = a;
	}

	/**
	 * マスの状態を指定した状態に変更し，変更をアンドゥリスナーに通知する
	 * @param p マス座標
	 * @param st 変更後の状態
	 */
	public void changeState(Address p, int st) {
		int prev = getState(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.STATE, p, prev, st));
		setState(p, st);
		initAreas();
	}

	public void changeNumber(Address p, int n) {
		changeState(p, UNKNOWN);
		int prev = getNumber(p);
		if (n == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, prev, n));
		setNumber(p, n);
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (step.getType() == EditType.STATE) {
				changeState(s.getPos(), s.getBefore());
			} else if (step.getType() == EditType.FIXED) {
				changeNumber(s.getPos(), s.getBefore());
			}
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (step.getType() == EditType.STATE) {
				changeState(s.getPos(), s.getAfter());
			} else if (step.getType() == EditType.FIXED) {
				changeNumber(s.getPos(), s.getAfter());
			}
		}
	}
	/**
	 * 現在の盤面状態に基づいて，領域の設定を行う
	 */
	public void initAreas() {
		Arrays.fill(areas, nullArea);
		areaList.clear();
		for (Address p : cellAddrs()) {
			if (!isNumber(getNumber(p)) && getArea(p) == nullArea) {
				initArea(p);
			}
		}
	}
	/**
	 * 起点マスを含む白マス連結領域を調べる
	 * @param p0 起点マスの座標
	 * @return 訪問したマスの面積
	 */
	public int initArea(Address p) {
		this.initializingArea = new Area();
		int n = visit(p, -1);
//		System.out.print(" n = " + n );
//		System.out.println(initializingArea.toString());
		areaList.add(initializingArea);
		for (Address q : initializingArea) {
			setArea(q, initializingArea);
		}
		return n;
	}

	public int checkRectangleArea(Area a) {
		return a.checkRectangleArea();
	}

	/**
	 * @param p マス
	 * @param d 前のマスからこのマスへ移動する方向 いらないかも。
	 * @return
	 */
	private int visit(Address p, int d) {
//		System.out.println(p.toString()+"を訪れた");
		int n = 1;
//		flag[a2i(p)] ++;
		setArea(p, initializingArea);
		initializingArea.add(p);
		initializingArea.updateMinMax(p);
		int s = getState(p);
		if (s == UNKNOWN || s == WHITE)	{
//			a.nFloor ++;
		} else if (s == LTUP || s == LTDN || s == RTDN || s == RTUP) {
			initializingArea.nAreaBorder[s] ++;
		}
		for (int dd = 0; dd < 4; dd++) {
//			System.out.println(dd+"方向を見る");
			if (   (dd == Direction.UP && (s == LTUP || s == RTUP))
				|| (dd == Direction.LT && (s == LTDN || s == LTUP))
				|| (dd == Direction.DN && (s == RTDN || s == LTDN))
				|| (dd == Direction.RT && (s == RTUP || s == RTDN)) ) {
				continue;
			}
			Address pp = Address.nextCell(p, dd);
			int ss = getState(pp);
			if (!isOn(pp)) {
		//		System.out.println(i2a(pp).toString()+"は盤外なので帰る");
				initializingArea.nAreaBorder[dd] ++;
				continue;
			}
			if (isNumber(getNumber(pp))) {
		//		System.out.println(i2a(pp).toString()+"は黒マスなので帰る");
				initializingArea.nAreaBorder[dd] ++;
				continue;
			}
			if (   (dd == Direction.UP && (ss == LTDN || ss == RTDN))
				|| (dd == Direction.LT && (ss == RTDN || ss == RTUP))
				|| (dd == Direction.DN && (ss == RTUP || ss == LTUP))
				|| (dd == Direction.RT && (ss == LTUP || ss == LTDN))) {
				//		System.out.println(i2a(pp).toString()+"は正三角なので帰る");
				initializingArea.nAreaBorder[dd] ++;
				continue;
			}
			if (getArea(pp) == initializingArea) {
//			if (flag[a2i(pp)] > 0) {
		//		System.out.println(i2a(pp).toString()+"はすでに訪れているので帰る");
				continue;
			}

			n += visit(pp, dd);
		}
		return n;
	}

	/**
	 * 隣接する４マスの三角形の個数を調べる
	 * @param r 行座標
	 * @param c 列座標
	 * @return 隣接する４マスの三角形の個数
	 */
	public int countAdjacentTriangles(Address p) {
		int count = 0;
		for (int d = 0; d < 4; d++) {
			Address pp = Address.nextCell(p, d);
			if (!isOn(pp))
				continue;
			int s = getState(Address.nextCell(p, d));
			if (isTriangle(s)) {
				count++;
			}
		}
//		System.out.println(p.toString() + " に隣接するマスの三角形の個数は " + count);
		return count;
	}

	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			int n = getNumber(p);
			if (n >= 0 && n <= 4) {
				if (countAdjacentTriangles(p) != n) {
					result |= 1;
				}
			}
		}
		initAreas();
		for (Area a : areaList) {
			int ret = checkRectangleArea(a);
			if (ret < 0)
				result |= 2;
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append (Messages.getString("shakashaka.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append (Messages.getString("shakashaka.AnswerCheckMessage2")) ; //$NON-NLS-1$
		return message.toString();
	}

	public Address i2a(int i) {
		return Address.address(i / cols(), i % cols());
	}

	public int a2i(Address a) {
		return cell(a.r(), a.c());
	}

	public boolean isOn(int p) {
		return p >= 0 && p < rows() * cols();
	}

	public int cell(Address p) {
		return cell(p.r(), p.c());
	}

	public int cell(int r, int c) {
		if (r < 0 || c < 0 || r >= rows() || c >= cols())
			return -1;
		else
			return r * cols() + c;
	}
}
