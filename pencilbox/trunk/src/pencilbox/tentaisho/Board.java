package pencilbox.tentaisho;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.AreaEditStep;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.SideAddress;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 *  「天体ショー」盤面クラス
 */
public class Board extends BoardBase {

	/** 星がないこと */
	static final int NOSTAR = 0;
	/** 白い星 */
	static final int WHITESTAR = 1;
	/** 黒い星 */
	static final int BLACKSTAR = 2;

	static final int LINE = 1;
	static final int NOLINE = 0;

	private int[][] star;
	private int[][][] edge;
	private Area[][] area;
	private List<Area> areaList;

	protected void setup () {
		super.setup();
		star = new int[rows()*2-1][cols()*2-1];
		edge = new int[2][][];
		edge[0] = new int[rows()][cols() - 1];
		edge[1] = new int[rows() - 1][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
	}

	public void clearBoard() {
		super.clearBoard();
		areaList.clear();
		ArrayUtil.initArrayObject2(area, null);
		for (SideAddress p : borderAddrs()) {
			setEdge(p, NOLINE);
		}
	}

	public void trimAnswer() {
		for (SideAddress p : borderAddrs()) {
			if (getEdge(p) == LINE) {
				changeEdge(p, NOLINE);
			}
		}
	}

	public void initBoard() {
		initAreas();
	}

	/**
	 * 盤面上の領域の初期処理を行う
	 */
	public void initAreas() {
		for (Area a : areaList) {
			initArea(a);
		}
	}

	/**
	 * @return the star
	 */
	int[][] getStar() {
		return star;
	}
	/**
	 * 引数の座標の星を取得する
	 * @param r
	 * @param c
	 * @return 星
	 */
	public int getStar(int r, int c) {
		return star[r][c];
	}

	public int getStar(Address pos) {
		return getStar(pos.r(), pos.c());
	}
	/**
	 * 引数の座標に星を設定する
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setStar(int r, int c, int st) {
		star[r][c] = st;
	}

	public void setStar(Address pos, int st) {
		setStar(pos.r(), pos.c(), st);
	}

	public void changeStar(Address p, int st) {
		int prev = getStar(p);
		if (prev == st)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		setStar(p, st);
		Address p0 = starAddressToSuperAddress(p);
		if (getArea(p0) != null)
			initArea(getArea(p0));
	}

	private Address starAddressToSuperAddress(Address p) {
		return Address.address(p.r()/2, p.c()/2);
	}

	/**
	 * 引数の星座標が盤上にあるか
	 * 0<=r<rows*2-1, 0<=c<cols*2-1 であれば盤上である
	 * @param r
	 * @param c
	 * @return　盤上にあれば true
	 */
	public boolean isOnStar(int r, int c) {
		return (r>=0 && r<rows()*2-1 && c>=0 && c<cols()*2-1);
	}

	public boolean isOnStar(Address pos) {
		return isOnStar(pos.r(), pos.c());
	}

	public int getEdge(SideAddress p) {
		return edge[p.d()][p.r()][p.c()];
	}

	public void setEdge(SideAddress p, int i) {
		edge[p.d()][p.r()][p.c()] = i;
	}

	/**
	 * 引数に与えられたマスの所属領域を取得する
	 * @param r
	 * @param c
	 * @return マスの所属領域
	 */
	public Area getArea(int r, int c) {
		return area[r][c];
	}

	public Area getArea(Address pos) {
		return getArea(pos.r(), pos.c());
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

	public void setArea(Address pos, Area a) {
		setArea(pos.r(), pos.c(), a);
	}
	/**
	 * 盤面に新しい領域を追加する
	 * @param newArea 追加する領域
	 */
	public void addArea(Area newArea) {
		for (Address pos : newArea) {
			setArea(pos, newArea);
		}
		areaList.add(newArea);
	}
	/**
	 * 領域のすべてのマスを領域から除いて領域を削除する
	 * @param oldArea
	 */
	public void removeWholeArea(Area oldArea) {
		Address[] cells = oldArea.toArray(new Address[0]);
		for (Address p : cells) {
			removeCellFromArea(p, oldArea);
		}
	}

	/**
	 * @param p
	 * @param st
	 */
	public void changeEdge(SideAddress p, int st) {
		int prev = getEdge(p);
		if (prev == st)
			return;
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new BorderEditStep(p, prev, st));
		}
		setEdge(p, st);
	}

	public void undo(AbstractStep step) {
		if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				removeCell(s.getPos());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				addCell(s.getPos(), s.getP0());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getBefore());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeStar(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				addCell(s.getPos(), s.getP0());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				removeCell(s.getPos());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getAfter());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeStar(s.getPos(), s.getAfter());
		}
	}
	/**
	 * p を p0 と同じ領域にする。ただし p0が NOWHWERならば新しい領域を作る
	 * @param p
	 * @param p0
	 */
	void addCell(Address p, Address p0) {
		if (p0.equals(Address.NOWHERE)) {
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
	 * マスを領域から取り除く。
	 * @param p
	 */
	void removeCell(Address p) {
		Area a = getArea(p);
		if (a != null) {
			removeCellFromArea(p, a);
		}
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
		initArea(a);
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
		if (a.isEmpty()) {
			areaList.remove(a);
		} else {
			initArea(a);
		}
	}

	/**
	 * 新規作成した領域に含まれる星を設定する
	 * @param newArea
	 */
	void initArea(Area newArea) {
		int nStar = 0;
		StarAddress starPos = StarAddress.NOWHERE;
		for (Address pos : newArea) {
			for (int i=2*pos.r()-1; i<=2*pos.r()+1; i++) {
				for (int j=2*pos.c()-1; j<=2*pos.c()+1; j++) {
					if (isOnStar(i, j) && getStar(i, j) > 0) {
						if (nStar == 0) {
							nStar = getStar(i, j);
							starPos = StarAddress.address(i,j);
						} else if (nStar == 1 || nStar == 2) {
							if (!starPos.equals(i, j)) {
								nStar = -1;
								starPos = StarAddress.NOWHERE;
							}
						}
					}
				}
			}
		}
		newArea.setStarType(nStar);
		newArea.setStarPos(starPos);
	}

	public int checkAnswerCode() {
		int result = 0;
		for (Area a : areaList) {
			if (a.isPointSymmetry() == false) {
				result |= 1;
			}
		}
		for (Address p : cellAddrs()) {
			if (getArea(p) == null) {
				result |= 2;
			}
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result ==0 )
			return COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result&1) == 1)
			message.append(Messages.getString("tentaisho.AnswerCheckMessage1")); //$NON-NLS-1$
		else if ((result&2) == 2)
			message.append(Messages.getString("tentaisho.AnswerCheckMessage2")); //$NON-NLS-1$
		return message.toString();
	}
	/**
	 * @return Returns the areaList.
	 */
	List<Area> getAreaList() {
		return areaList;
	}
	/**
	 * @return Returns the domain.
	 */
	Area[][] getArea() {
		return area;
	}
}
