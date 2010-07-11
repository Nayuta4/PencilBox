package pencilbox.lits;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.AreaEditStep;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 * 「ＬＩＴＳ」盤面クラス
 */
public class Board extends BoardBase {
	
	public static final int WHITE = -1;
	public static final int BLACK = -2;
	public static final int UNKNOWN = 0;
	
	private List<Area> areaList;
	private Area[][] area;
	private int[][] state;
	private List<Wall> wallList;
	private Wall[][] wall;
	private Wall initializingWall;

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
		wall = new Wall[rows()][cols()];
		wallList = new LinkedList<Wall>();
	}

	public void clearBoard() {
		for (Address p : cellAddrs()) {
			setState(p, UNKNOWN);
		}
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == WHITE)
				setState(p, UNKNOWN);
		}
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
	 * 引数の座標が黒マスかどうか。
	 * @param r 行座標
	 * @param c 列座標
	 * @return 黒マスなら true を返す。
	 */
	public boolean isBlack(int r, int c) {
		return isOn(r, c) && (state[r][c] == BLACK);
	}
	public boolean isBlack(Address p) {
		return isOn(p) && getState(p) == BLACK;
	}
	/**
	 * そのマスの所属する領域を取得する
	 * そのマスが領域に属していない場合は null を返す
	 * @param p coordinate of the cell.
	 * @return Returns the area.
	 */
	public Wall getWall(Address p ) {
		return wall[p.r()][p.c()];
	}
	/**
	 * 盤上のマスに，そのマスの所属する領域を設定する
	 * @param p coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setWall(Address p, Wall a) {
		wall[p.r()][p.c()] = a;
	}
	
	/**
	 * そのマスの所属する領域を取得する
	 * そのマスが領域に属していない場合は null を返す
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(int r, int c) {
		if (!isOn(r, c))
			return null;
		return area[r][c];
	}

	public Area getArea(Address p) {
		return getArea(p.r(), p.c());
	}

	/**
	 * 盤上のマスに，そのマスの所属する領域を設定する
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c, Area a) {
		area[r][c] = a;
	}
	
	public void setArea(Address p, Area a) {
		setArea(p.r(), p.c(), a);
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
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		setState(p, st);
		Area a = getArea(p);
		if (a != null) {
			if (prev == BLACK) {
				a.getTetromino().remove(p);
			}
			if (st == BLACK) {
				a.getTetromino().add(p);
			}
		}
	}

	/**
	 * 新しい領域を追加する
	 * @param newArea
	 */
	public void addArea(Area newArea) {
		for (Address p : newArea) {
			area[p.r()][p.c()] = newArea;
		}
		areaList.add(newArea);
	}

	/**
	 * 領域を削除する
	 * @param oldArea
	 */
	public void removeArea(Area oldArea) {
		for (Address p : oldArea) {
			if (area[p.r()][p.c()] == oldArea)
				area[p.r()][p.c()] = null;
		}
		areaList.remove(oldArea);
	}
	/**
	 * マスを領域に追加する
	 * @param p 追加するマスの座標
	 * @param a 追加される領域
	 */
	public void addCellToArea(Address p, Area a) {
		if (isRecordUndo()) {
			Address p0 = Address.NOWHERE;
			if (a.size() > 0) {
				p0 = a.getTopCell(Address.NOWHERE);
			}
			fireUndoableEditUpdate(new AreaEditStep(p, p0, AreaEditStep.ADDED));
		}
		if (a.isEmpty()) {
			areaList.add(a);
		}
		setArea(p, a);
		a.add(p);
		if (getState(p) == Board.BLACK) {
			a.getTetromino().add(p);
		}
//		initArea(a);
	}
	/**
	 * マスを領域から取り除く
	 * @param p 取り除くマスの座標
	 * @param a 取り除かれる領域
	 */
	public void removeCellFromArea(Address p, Area a) {
		if (isRecordUndo()) {
			Address p0 = Address.NOWHERE;
			if (a.size() > 1) {
				p0 = a.getTopCell(p);
			}
			fireUndoableEditUpdate(new AreaEditStep(p, p0, AreaEditStep.REMOVED));
		}
		setArea(p, null);
		a.remove(p);
		if (getState(p) == Board.BLACK) {
			a.getTetromino().remove(p);
		}
		if (a.isEmpty()) {
			areaList.remove(a);
		} else {
//			initArea(a);
		}
	}
	/**
	 * マスp を p0 と同じ領域にする。ただし p0が NOWHWERならば新しい領域を作る
	 * @param p0
	 * @param p
	 */
	void addCell(Address p0, Address p) {
		if (Address.NOWHERE.equals(p0)) { 
			Area a = new Area();
			addCellToArea(p, a);
		} else {
			Area a = getArea(p0);
			if (a != null) {
				addCellToArea(p, a);
			}
		}
	}
	/**
	 * マス p を領域から取り除く。
	 * @param p
	 */
	void removeCell(Address p) {
		Area a = getArea(p);
		if (a != null) {
			removeCellFromArea(p, a);
		}
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getBefore());
		} else if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				removeCell(s.getPos());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				addCell(s.getP0(), s.getPos());
			}
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getAfter());
		} else if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				addCell(s.getP0(), s.getPos());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				removeCell(s.getPos());
			}
		}
	}

	/**
	 * @return Returns the areaList.
	 */
	List<Area> getAreaList() {
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
	boolean is2x2Block(Address p) {
		if (isBlack(p)) {
			for (int d = 0; d < 4; d++) {
				Address p1 = Address.nextCell(p, d);
				if (isBlack(p1)) {
					Address p2 = Address.nextCell(p1, (d+1)%4);
					if (isBlack(p2)) {
						Address p3 = Address.nextCell(p2, (d+2)%4);
						if (isBlack(p3)) {
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
		for (Area a : areaList) {
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
		for (Area a : areaList) {
			a.getTetromino().clear();
		}
		for (Address p : cellAddrs()) {
			if (getArea(p) != null) {
				if (getState(p) == BLACK) {
					getArea(p).getTetromino().add(p);
				}
			}
		}
	}

	private int checkAdjacentCongruousTetrominos() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getArea(p) != null) {
				if (getState(p) == BLACK) {
					for (int d : Direction.DN_RT) {
						Address p1 = Address.nextCell(p, d);
						if (getArea(p1) != null) {
							if (getArea(p) != getArea(p1)) {
								if (getState(p1) == BLACK) {
									if (getArea(p).getTetrominoType() == getArea(p1).getTetrominoType()) {
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
		for (Address p : cellAddrs()) {
			if (getState(p) == BLACK && getWall(p) == null) {
				if (wallList.size() > 0)
					return 32;
				initWall(p);
			}
		}
		return 0;
	}
	/**
	 * あるマスを起点とする黒マスのつながりを調べてWallを作成する
	 * @param p
	 */
	private void initWall(Address p) {
		initializingWall = new Wall();
		initWall1(p);
		wallList.add(initializingWall);
	}

	private boolean initWall1(Address p) {
		if (!isOn(p))
			return false;
		if (!isBlack(p))
			return false;
		if (getWall(p) == initializingWall)
			return false;
		initializingWall.add(p);
		setWall(p, initializingWall);
		for (int d = 0; d < 4; d++) {
			initWall1(Address.nextCell(p, d));
		}
		return true;
	}
	
	private int check2x2s() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (is2x2Block(p)) {
				result |= 64;
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
			message.append(Messages.getString("lits.AnswerCheckMessage1"));  //$NON-NLS-1$
		if ((result & 2) == 2)
			;
		if ((result & 4) == 4)
			;
		if ((result & 8) == 8)
			message.append(Messages.getString("lits.AnswerCheckMessage2"));  //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("lits.AnswerCheckMessage3"));  //$NON-NLS-1$
		if ((result & 32) == 32)
			message.append(Messages.getString("lits.AnswerCheckMessage4"));  //$NON-NLS-1$
		if ((result & 64) == 64)
			message.append(Messages.getString("lits.AnswerCheckMessage5"));  //$NON-NLS-1$
		return message.toString();
	}
}
