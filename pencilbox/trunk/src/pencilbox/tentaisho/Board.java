package pencilbox.tentaisho;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.AreaEditStep;
import pencilbox.common.core.BoardBase;
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

	private int[][] star;
	private Area[][] area;
	private List<Area> areaList;

	protected void setup () {
		super.setup();
		star = new int[rows()*2-1][cols()*2-1];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
	}


	public void clearBoard() {
		super.clearBoard();
		areaList.clear();
		ArrayUtil.initArrayObject2(area, null);
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
	 * 領域を削除する
	 * @param oldArea
	 */
	public void removeArea(Area oldArea) {
		for (Address pos : oldArea) {
			if (getArea(pos) == oldArea)
				setArea(pos, null);
		}
		areaList.remove(oldArea);
	}

	public void undo(AbstractStep step) {
		AreaEditStep s = (AreaEditStep)step;
		Area a;
		if (s.getOperation() == AreaEditStep.ADDED) {
			a = getArea(s.getPos());
			if (a != null) {
				removeCellFromArea(s.getPos(), a);
			}
		} else if (s.getOperation() == AreaEditStep.REMOVED) {
			if (Address.NOWHERE.equals(s.getP0()))
				a = new Area();
			else
				a = getArea(s.getP0());
			if (a != null) {
				addCellToArea(s.getPos(), a);
			}
		}
	}

	public void redo(AbstractStep step) {
		AreaEditStep s = (AreaEditStep)step;
		Area a;
		if (s.getOperation() == AreaEditStep.ADDED) {
			if (Address.NOWHERE.equals(s.getP0()))
				a = new Area();
			else
				a = getArea(s.getP0());
			if (a != null) {
				addCellToArea(s.getPos(), a);
			}
		} else if (s.getOperation() == AreaEditStep.REMOVED) {
			a = getArea(s.getPos());
			if (a != null) {
				removeCellFromArea(s.getPos(), a);
			}
		}
	}
	/**
	 * マスを領域に追加する
	 * @param r 追加するマスの行座標
	 * @param c 追加するマスの列座標
	 * @param area 追加される領域
	 */
	public void addCellToArea(Address pos, Area area) {
		Address p0 = Address.NOWHERE;
		if (area.size() > 0) {
			p0 = (Address)area.toArray()[0];
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new AreaEditStep(pos, p0, AreaEditStep.ADDED));
		if (area.isEmpty()) {
			areaList.add(area);
		}
		setArea(pos, area);
		area.add(pos);
		initArea(area);
	}

	/**
	 * マスを領域から取り除く
	 * @param r 取り除くマスの行座標
	 * @param c 取り除くマスの列座標
	 * @param area 取り除かれる領域
	 */
	public void removeCellFromArea(Address pos, Area area) {
		Address p0 = Address.NOWHERE;
		if (area.size() > 1) {
			p0 = (Address)area.toArray()[0];
			if (p0.equals(pos))
				p0 = (Address)area.toArray()[1];
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new AreaEditStep(pos, p0, AreaEditStep.REMOVED));
		setArea(pos, null);
		area.remove(pos);
		if (area.isEmpty()) {
			areaList.remove(area);
		} else {
			initArea(area);
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
		for (int r = 0; r < rows(); r++)
			for (int c = 0; c < cols(); c++)
				if (area[r][c] == null) result |= 2;
	
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
