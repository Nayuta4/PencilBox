package pencilbox.masyu;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.*;

import pencilbox.common.core.*;
import pencilbox.util.*;


/**
 * 「ましゅ」盤面クラス
 */
public class Board extends BoardBase  {
	
	static final int HORIZ = Direction.HORIZ;
	static final int VERT = Direction.VERT;
	static final int UP = Direction.UP;
	static final int DN = Direction.DN;
	static final int LT = Direction.LT;
	static final int RT = Direction.RT;

	static final int UNKNOWN = 0;
	static final int LINE = 1;
	static final int NOLINE = -1;
	static final int OUTER = -9;
	static final int NO_PEARL = 0;
	static final int WHITE_PEARL = 1;
	static final int BLACK_PEARL = 2;
	static final int GRAY_PEARL = 3;

	private int[][] pearl;
	private int[][][] state;

	private List linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		pearl = new int[rows()][cols()];
		state = new int[2][][];
		state[0] = new int[rows()][cols()- 1];
		state[1] = new int[rows()- 1][cols()];
		linkList = new LinkedList();
		link = new Link[2][][];
		link[VERT] = new Link[rows()][cols() - 1];
		link[HORIZ] = new Link[rows() - 1][cols()];
	}
	
	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		initBoard();
	}
	
	/**
	 * @return Returns the state.
	 */
	int[][][] getState() {
		return state;
	}
	/**
	 * @return Returns the number.
	 */
	int[][] getPearl() {
		return pearl;
	}
	/**
	 * 丸の設定
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setPearl(int r, int c, int st) {
		pearl[r][c] = st;
	}
	/**
	 * 丸の取得
	 * @param r
	 * @param c
	 * @return 丸印の種類を返す
	 */
	public int getPearl(int r, int c) {
		return pearl[r][c];
	}
	/**
	 * 辺状態の取得
	 * @param d
	 * @param r
	 * @param c
	 * @return 辺の状態を返す
	 */
	public int getState(int d, int r, int c) {
		if (isSideOn(d,r,c))
			return state[d][r][c];
		else
			return OUTER;
	}
	/**
	 * 辺状態の設定
	 * @param d
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setState(int d, int r, int c, int st) {
//		if (isSideOn(d,r,c))
			state[d][r][c] = st;
	}
	public boolean isLine(int d, int r, int c) {
		if (!isSideOn(d,r,c))
			return false;
		return
		state[d][r][c] == LINE;
	}
	public boolean isNoLine(int d, int r, int c) {
		if (!isSideOn(d,r,c))
			return true;
		return
		state[d][r][c] == NOLINE;
	}
	/**
	 * マスから direction 方向の線状態取得
	 */
	public int getStateJ(int r, int c, int direction) {
		switch (direction) {
			case UP: return getState(HORIZ,r-1,c);
			case LT: return getState(VERT, r,c-1);
			case DN: return getState(HORIZ,r,c);
			case RT: return getState(VERT, r,c);
			default: return -2;
		}
	}
	/**
	 * マスから direction 方向に線はあるか
	 */
	public boolean isLineJ(int r, int c, int direction) {
		switch (direction) {
			case UP: return isLine(HORIZ,r-1,c);
			case LT: return isLine(VERT, r,c-1);
			case DN: return isLine(HORIZ,r,c);
			case RT: return isLine(VERT, r,c);
			default: return false;
		}
	}
	/**
	 * マスから direction 方向に×はあるか
	 */
	public boolean isNoLineJ(int r, int c, int direction) {
		switch (direction) {
			case UP: return isNoLine(HORIZ,r-1,c);
			case LT: return isNoLine(VERT, r,c-1);
			case DN: return isNoLine(HORIZ,r,c);
			case RT: return isNoLine(VERT, r,c);
			default: return false;
		}
	}
	public Link getLink(int d, int r, int c) {
		if (isSideOn(d,r,c) )
			return link[d][r][c];
		else return null;
	}
	public Link getLink(SideAddress pos) {
		return link[pos.d()][pos.r()][pos.c()];
	}
	/**
	 * そのマスを含む Link を返す
	 */
	public Link getLink(int r, int c) {
		Link link;
		link = getLink(VERT, r, c - 1);
		if (link != null) return link;
		link = getLink(VERT, r, c);
		if (link != null) return link;
		link = getLink(HORIZ, r - 1, c);
		if (link != null) return link;
		link = getLink(HORIZ, r, c);
		if (link != null) return link;
		return null;
	}
	public void setLink(int d, int r, int c, Link l) {
		link[d][r][c] =  l;
	}
	public void setLink(SideAddress pos, Link l) {
		link[pos.d()][pos.r()][pos.c()] =  l;
	}
	/**
	 * 盤面に Link が複数あるか
	 * @return　Link が複数あるなら true
	 */
	public boolean hasMultipleLinks() {
		return linkList.size() > 1;
	}
	
	/**
	 * 辺の状態を指定した状態に変更する
	 * @param d 縦か横か
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 変更後の状態
	 */
	public void changeState(int d, int r, int c, int st) {
		int previousState = getState(d,r,c);
		setState(d,r,c,st);
		if (previousState == LINE) {
			cutLink(d,r,c);
		}
		if (st == LINE) {
			connectLink(d,r,c);
		}
	}
	
	/**
	 * 辺の状態を指定した状態に変更する
	 * アンドゥリスナーに変更を通知する
	 * @param d 縦か横か
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 変更後の状態
	 */
	public void changeStateA(int d, int r, int c, int st) {
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(d, r, c, getState(d,r,c), st)));
		changeState(d, r, c, st);
	}
	/**
	 * 辺の状態を 未定⇔st で切り替える
	 * @param d 縦か横か
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 切り替える状態
	 */
	public void toggleState(int d, int r, int c, int st) {
		if (getState(d, r, c) == st)
			changeStateA(d, r, c, UNKNOWN);
		else
			changeStateA(d, r, c, st);
	}
	/**
	 * 始点マスと終点マスを結んだ線上の状態を指定の状態に変更する
	 * 始点マスと終点マスは同じ行または同じ列になければならない
	 * @param pos0 始点マスの座標
	 * @param pos1 終点マスの座標
	 * @param st 変更後の状態
	 */
	public void determineInlineState(Address pos0, Address pos1, int st) {
		int ra = pos0.r()<pos1.r() ? pos0.r() : pos1.r();
		int rb = pos0.r()<pos1.r() ? pos1.r() : pos0.r();
		int ca = pos0.c()<pos1.c() ? pos0.c() : pos1.c();
		int cb = pos0.c()<pos1.c() ? pos1.c() : pos0.c();
		if (ra == rb) 
			for (int c = ca; c < cb; c++) {
				if (getState(VERT, ra, c) != st)
					changeStateA(VERT, ra, c, st);
			}
		if (ca == cb) 
			for (int r = ra; r < rb; r++) {
				if (getState(HORIZ, r, ca) != st)
					changeStateA(HORIZ, r, ca, st);
			}
	}

	public void initBoard() {
		Link.resetID();
		linkList.clear();
		ArrayUtil.initArrayObject2(link[0],null);
		ArrayUtil.initArrayObject2(link[1],null);
		initLinks();
	}
	
	void initLinks() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				initLink(r, c);
			}
		}
	}
	
	/**
	 * あるマスを含む Link の初期化
	 * link[][][] は消去されているものとする
	 * @param r Link初期化の起点マスの行座標
	 * @param c Link初期化の起点マスの列座標
	 */	
	void initLink (int r, int c) {
		initializingLink = new Link();
		initLink1(VERT , r  , c-1);
		initLink1(VERT , r  , c  );
		initLink1(HORIZ, r-1, c  );
		initLink1(HORIZ, r  , c  );
		if (!initializingLink.isEmpty())
			linkList.add(initializingLink);
//		printLink(d,r,c);
	}
	private void initLink1(int d, int r, int c) {
		if (!isSideOn(d,r,c)) return;
		if (!isLine(d,r,c)) return;
		if (getLink(d,r,c) != null) return;
		initializingLink.add(d,r,c);
		setLink(d, r, c, initializingLink);
		if (d==VERT) {
			initLink1(VERT , r  , c-1);
			initLink1(VERT , r  , c+1);
			initLink1(HORIZ, r-1, c  );
			initLink1(HORIZ, r-1, c+1);
			initLink1(HORIZ, r  , c  );
			initLink1(HORIZ, r  , c+1);
		}
		if (d==HORIZ) {
			initLink1(HORIZ, r-1, c  );
			initLink1(HORIZ, r+1, c  );
			initLink1(VERT , r  , c-1);
			initLink1(VERT , r+1, c-1);
			initLink1(VERT , r  , c  );
			initLink1(VERT , r+1, c  );
		}
	}
	/**
	 * Link 併合
	 */	
	void connectLink(int d, int r, int c) {
		Link newLink = null;
		Link link1 = null;
		Link link2 = null;
		if (d==VERT) {
			link1 = getLink(r,c);
			link2 = getLink(r,c+1);
		} else if (d==HORIZ) {
			link1 = getLink(r,c);
			link2 = getLink(r+1,c);
		}
		if (link1==null && link2 == null) {
			newLink = new Link();
			linkList.add(newLink);
		} else if (link1==null && link2!=null) {
			newLink = link2;
		} else if (link1!=null && link2==null) {
			newLink = link1;
		} else if (link1==link2) {
			newLink = link1;
		} else {
			if (link1.size() >= link2.size()) {
				newLink = link1;
				newLink.addAll(link2);
				for(Iterator itr = link2.iterator(); itr.hasNext(); ) {
					setLink((SideAddress) itr.next(), newLink);
				}
				linkList.remove(link2);
			}
			else {
				newLink = link2;
				newLink.addAll(link1);
				for(Iterator itr = link1.iterator(); itr.hasNext(); ) {
					setLink((SideAddress) itr.next(), newLink);
				}
				linkList.remove(link1);
			}
		}
		newLink.add(d,r,c);
		setLink(d,r,c, newLink);
//		printLink(d,r,c);
	}


	/**
	 * Link 切断
	 */	
	void cutLink(int d, int r, int c) {
		Link oldLink = getLink(d,r,c);
		Link longerLink = null;
		for (Iterator itr = oldLink.iterator(); itr.hasNext(); ) {
			setLink((SideAddress) itr.next(), null);
		}
		linkList.remove(oldLink);
		if (d==VERT) {
			initLink(r, c);
			longerLink = initializingLink;
			initLink(r, c+1);
			if (initializingLink.size() > longerLink.size()) longerLink = initializingLink;
		}
		else if (d==HORIZ) {
			initLink(r, c);
			longerLink = initializingLink;
			initLink(r+1, c);
			if (initializingLink.size() > longerLink.size()) longerLink = initializingLink;
		}
		longerLink.setID(oldLink.getID()); 
	}

	/**
	 * マスの上下左右4方向のうち，現在線が引かれている数を返す
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return マスの上下左右に引かれている線の数
	 */
	public int countLine(int r, int c) {
		int no = 0;
		if (isLineJ(r,c,UP)) no++;
		if (isLineJ(r,c,LT)) no++;
		if (isLineJ(r,c,DN)) no++;
		if (isLineJ(r,c,RT)) no++;
		return no;
	}
	/**
	 * 線分の両端のいずれかの位置で線が分岐しているかどうかを調べる
	 * @param d 辺位置は縦か横か
	 * @param r 辺の行座標
	 * @param c 辺の列座標
	 * @return 両端のいずれかで線が分岐していれば true 分岐していなければ false を返す
	 */
	boolean isBranchedLink(int d, int r, int c) {
		if (countLine(r,c) > 2 ) return true;
		if (d==VERT) {
			if (countLine(r,c+1) > 2 ) return true;
		} else if (d==HORIZ) {
			if (countLine(r+1,c) > 2 ) return true;
		}
		return false;
	}
	/**
	 * 白真珠マス上の線の状態を調べる
	 * 次の２種類を間違いと判定する
	 * ━○
	 *   ┃ 曲がる場合
	 * ━━○━━　両隣で直進する場合
	 * @param r
	 * @param c
	 * @return
	 * 完成していれば +2 を
	 * 線が通過していて，間違いがなければ +1 を
	 * 間違いがあれば -1 を
	 * それ以外は 0 を返す
	 */
	int checkWhitePearl(int r, int c) {
		int miss = 0;
		int success = 0;
		int result = 0;

		if (isLineJ(r,c,UP)) {
			if (isLineJ(r,c,RT)) miss = -1;
			if (isLineJ(r,c,LT)) miss = -1;
		}
		if (isLineJ(r,c,DN)) {
			if (isLineJ(r,c,RT)) miss = -1;
			if (isLineJ(r,c,LT)) miss = -1;
		} 
		if (isLineJ(r,c,UP) && isLineJ(r,c,DN)) {
			if (isLineJ(r-1,c,UP) && isLineJ(r+1,c,DN)) miss = -1;
			if (isLineJ(r-1,c,RT)) success = 2;
			if (isLineJ(r-1,c,LT)) success = 2;
			if (isLineJ(r+1,c,RT)) success = 2;
			if (isLineJ(r+1,c,LT)) success = 2;
			else success = 1;
		}
		if (isLineJ(r,c,LT) &&  isLineJ(r,c,RT)) {
			if (isLineJ(r,c-1,LT) && isLineJ(r,c+1,RT)) miss = -1;
			if (isLineJ(r,c-1,UP)) success = 2;
			if (isLineJ(r,c-1,DN)) success = 2;
			if (isLineJ(r,c+1,UP)) success = 2;
			if (isLineJ(r,c+1,DN)) success = 2;
			else success = 1;
		}
		if (success > 0) result = success;
		if (miss == -1) result = -1;
		return result;
	}
	
	/**
	 * 黒真珠マス上の線の状態を調べる
	 * 次の２種類を間違いと判定する
	 * ━●━  直進する場合
	 * ┏●    隣のマスで曲がる場合
	 * @param r
	 * @param c
	 * @return
	 * 完成していれば +2 を
	 * 線が通過していて，間違いがなければ +1 を
	 * 間違いがあれば -1 を
	 * それ以外は 0 を返す
	 */
	int checkBlackPearl(int r, int c) {
		int miss = 0;
		int success = 0;
		int result = 0;

		if (isLineJ(r,c,UP) && isLineJ(r,c,DN)) miss = -1;
		if (isLineJ(r,c,RT) && isLineJ(r,c,LT)) miss = -1;

		if (isLineJ(r,c,UP)) {
			if (isLineJ(r-1,c,LT)) miss = -1;
			if (isLineJ(r-1,c,RT)) miss = -1;
			if (isLineJ(r-1,c,UP)) success |= 2;
			else success |= 1;
		}
		if (isLineJ(r,c,DN)) {
			if (isLineJ(r+1,c,LT)) miss = -1;
			if (isLineJ(r+1,c,RT)) miss = -1;
			if (isLineJ(r+1,c,DN)) success |= 2;
			else success |= 1;
		} 
		if (isLineJ(r,c,LT)) {
			if (isLineJ(r,c-1,UP)) miss = -1;
			if (isLineJ(r,c-1,DN)) miss = -1;
			if (isLineJ(r,c-1,LT)) success |= 8;
			else success |= 4;
		}
		if (isLineJ(r,c,RT)) {
			if (isLineJ(r,c+1,UP)) miss = -1;
			if (isLineJ(r,c+1,DN)) miss = -1;
			if (isLineJ(r,c+1,RT)) success |= 8;
			else success |= 4;
		}
		if (success == 10) result = 2;
		else if (success == 9 || success == 6) result = 1;
		else result = 0;
		if (miss < 0 ) result = -1;
		return result;
	}

	public int checkAnswerCode() {
		int result = 0;
		int p = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				int l = countLine(r,c);
				if (l > 2) {
					result |= 1;
				} else if ( l == 1 ) {
					result |= 2; 
				}
				int pearl = getPearl(r,c);
				if (pearl == WHITE_PEARL) {
					p = checkWhitePearl(r,c);
						if (p == -1)
							result |= 4;
						else if (p == 0)
							result |= 32;   
				}
				if (pearl == BLACK_PEARL) {
					p = checkBlackPearl(r,c);
					if (p == -1)
						result |= 8; 
					else if (p == 0)
						result |= 64;   
				}
			}
		}
		if (linkList.size() > 1) result |= 16;
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE; 
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append("線が分岐または交差している\n");
		if ((result & 2) == 2)
			message.append("閉じていない線がある\n");
		if ((result & 16) == 16)
			message.append("複数の線がある\n");
		if ((result & 4) == 4)
			message.append("○の通り方の間違いがある\n");
		else if ((result & 32) == 32)
			message.append("線が通っていない○がある\n");
		if ((result & 8) == 8)
			message.append("●の通り方の間違いがある\n");
		else if ((result & 64) == 64)
			message.append("線が通っていない●がある\n");
		return message.toString();
	}

 /**
   * １手の操作を表すクラス
   * UNDO, REDO での編集の単位となる
   */
	class Step extends AbstractUndoableEdit {

		private int direction;
		private int row;
		private int col;
		private int before;
		private int after;
		/**
		 * コンストラクタ
		 * @param d 横か縦か
		 * @param r 変更されたマスの行座標
		 * @param c 変更されたマスの列座標
		 * @param b 変更前の状態
		 * @param a 変更後の状態
		 */
		public Step(int d, int r, int c, int b, int a) {
			super();
			direction = d;
			row = r;
			col = c;
			before = b;
			after = a;
		}
		public void undo() throws CannotUndoException {
			super.undo();
			changeState(direction, row, col, before);
		}
		public void redo() throws CannotRedoException {
			super.redo();
			changeState(direction, row, col, after);
		}
	}

}
