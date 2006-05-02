package pencilbox.tentaisho;

import java.util.*;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.*;

import pencilbox.common.core.*;
import pencilbox.util.*;


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
	private List areaList;

	protected void setup () {
		super.setup();
		star = new int[rows()*2-1][cols()*2-1];
		area = new Area[rows()][cols()];
		areaList = new LinkedList();
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
		for (Iterator itr = areaList.iterator(); itr.hasNext(); ) {
			setContainedStar((Area) itr.next());
		}
	}
	
	/**
	 * @return 盤面の星の数
	 */
	public int nStar() {
		int nStar = 0;
		for (int r = 0; r < rows()*2-1; r++)
			for (int c = 0; c < cols()*2-1; c++)
				if (hasStar(r,c)) nStar ++;
		return nStar;
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
	/**
	 * 引数の座標に星を設定する
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setStar(int r, int c, int st) {
		star[r][c] = st;
	}
	/**
	 * 引数に与えられた座標に星はあるか
	 * @param r
	 * @param c
	 * @return 星があれば true
	 */
	public boolean hasStar(int r, int c) {
		return star[r][c] > 0;
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
	/**
	 * 引数のマスがいずれかの領域に含まれているかどうか
	 * @param r
	 * @param c
	 * @return 含まれていれば true
	 */
	public boolean isCovered(int r, int c) {
		return area[r][c] != null;
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
	/**
	 * 盤上のマスに，そのマスの所属する領域を設定する
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c, Area a) {
		area[r][c] = a;
	}
	/**
	 * 盤面に新しい領域を追加する
	 * 変更をアンドゥリスナーに通知する
	 * @param newArea 追加する領域
	 */
	public void addAreaA(Area newArea) {
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(newArea, Step.ADDED)));
		addArea(newArea);
	}
	/**
	 * 盤面の領域を１つ消去する
	 * 変更をアンドゥリスナーに通知する
	 * @param oldArea 消去する領域
	 */
	public void removeAreaA(Area oldArea) {
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(oldArea, Step.REMOVED)));
		removeArea(oldArea);
	}
	/**
	 * 盤面に新しい領域を追加する
	 * @param newArea 追加する領域
	 */
	public void addArea(Area newArea) {
		for (Iterator itr = newArea.iterator(); itr.hasNext(); ) {
			Address pos = (Address) itr.next();
			area[pos.r][pos.c] = newArea;
		}
		setContainedStar(newArea);
		areaList.add(newArea);
	}
	/**
	 * 盤面の領域を１つ消去する
	 * @param oldArea 消去する領域
	 */
	public void removeArea(Area oldArea) {
		for (Iterator itr = oldArea.iterator(); itr.hasNext(); ) {
			Address pos = (Address) itr.next();
			if (area[pos.r][pos.c] == oldArea)
				area[pos.r][pos.c] = null;
		}
		areaList.remove(oldArea);
	}
	/**
	 * 新規作成した領域に含まれる星を設定する
	 * @param newArea
	 */
	void setContainedStar(Area newArea) {
		Address pos;
		int nStar = 0;
		StarAddress starPos = new StarAddress();
		for (Iterator itr = newArea.iterator(); itr.hasNext(); ) {
			pos = (Address) itr.next();
			for (int i=2*pos.r-1; i<=2*pos.r+1; i++) {
				for (int j=2*pos.c-1; j<=2*pos.c+1; j++) {
					if (isOnStar(i, j) && hasStar(i, j)) {
						if (nStar == 0) {
							nStar = getStar(i, j);
							starPos.set(i,j);
						} else if (nStar == 1 || nStar == 2) {
							if (!starPos.equals(i, j)) {
								nStar = -1;
								starPos.setNowhere();
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
		Area a;
		for (Iterator itr = areaList.iterator(); itr.hasNext(); ) {
			a = (Area) itr.next();
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
			message.append("正しくない領域がある\n");
		else if ((result&2) == 2)
			message.append("未完成\n");
		return message.toString();
	}
	/**
	 * @return Returns the areaList.
	 */
	List getAreaList() {
		return areaList;
	}
	/**
	 * @return Returns the domain.
	 */
	Area[][] getArea() {
		return area;
	}
	/**
	 * １手の操作を表すクラス
	 * UNDO, REDO での編集の単位となる
	 */
	class Step extends AbstractUndoableEdit {

		static final int ADDED = 1;
		static final int REMOVED = 0;
		static final int CHANGED = 2;

		private Area area;
		private int operation;

		/**
		 * コンストラクタ
		 * @param area Area
		 * @param operation 操作
		 */
		public Step(Area area, int operation) {
			super();
			this.area = area;
			this.operation = operation;
		}
		public void undo() throws CannotUndoException {
			super.undo();
			if (operation == ADDED) {
				removeArea(area);
			} else if (operation == REMOVED) {
				addArea(area);
			} else if (operation == CHANGED) {
				addArea(area);
			}
		}
		public void redo() throws CannotRedoException {
			super.redo();
			if (operation == ADDED) {
				addArea(area);
			} else if (operation == REMOVED) {
				removeArea(area);
			}
		}
	}

}

