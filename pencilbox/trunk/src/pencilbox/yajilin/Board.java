package pencilbox.yajilin;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.util.ArrayUtil;

/**
 * 「ヤジリン」盤面クラス
 */
public class Board extends BoardBase {
	
	private static final int HORIZ = Direction.HORIZ;
	private static final int VERT = Direction.VERT;
	
	static final int UNKNOWN = -3;
	static final int LINE = 1;
	static final int NOLINE = -1;
	static final int WHITE = -1;
	static final int BLACK = -2;
	static final int OUTER = -9;
	static final int UNDECIDED_NUMBER = -4;
	private int[][] number;  // マスの状態
	private int[][][] state; // 辺の状態

	private List linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		ArrayUtil.initArrayInt2(number, UNKNOWN);
		state = new int[2][][];
		state[VERT] = new int[rows()][cols() - 1];
		state[HORIZ] = new int[rows() - 1][cols()];
		linkList = new LinkedList();
		link = new Link[2][][];
		link[VERT] = new Link[rows()][cols() - 1];
		link[HORIZ] = new Link[rows() - 1][cols()];
	}
	
	public boolean isBlack(int r, int c) {
		return isOn(r,c) && number[r][c] == BLACK;
	}
	/**
	 * 指定したマスに数字マス，黒マス，白マス，空白マスの状態を設定する
	 * @param r 行座標
	 * @param c 列座標
	 * @param n 設定する状態
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}
	public int getNumber(int r, int c) {
		return number[r][c]; 
	}
	public int getArrowNumber(int r, int c) {
		return number[r][c] & 15; 
	}
	public void setArrowNumber(int r, int c, int n) {
		number[r][c] = n; 
	}
	public int getArrowDirection(int r, int c) {
		return (number[r][c]>>4) & 3; 
	}
	public void setArrowDirection(int r, int c, int dir) {
		if (dir < 0 || dir > 3) return;
		int clearDirection = ~(3 << 4);
		number[r][c] &= clearDirection;
		number[r][c] |= (dir << 4); 
	}
	public void toggleArrowDirection(int r, int c) {
		int t = getArrowDirection(r,c);
		t = (t+1)%4;
		setArrowDirection(r, c, t);
	}
	public boolean isNumber(int r, int c) {
		return number[r][c] >= 0 || number[r][c] == UNDECIDED_NUMBER;
	}
	public void eraseNumber(int r, int c) {
		number[r][c] = UNKNOWN;
	}
	public void enterNumber(int r, int c, int n) {
		if (getArrowNumber(r,c) == n)
			toggleArrowDirection(r,c);
		else {
			eraseLinesAround(r,c);
			setArrowNumber(r, c, n);
		}
	}
	/**
	 * @return Returns the state.
	 */
	int[][][] getState() {
		return state;
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
		state[d][r][c] = st;
	}
	public boolean isLine(int d, int r, int c) {
		if (!isSideOn(d,r,c))
			return false;
		return
		state[d][r][c] == LINE;
	}

	public Link getLink(int d, int r, int c) {
		if (isSideOn(d,r,c) ) return link[d][r][c];
		else return null;
	}
	public Link getLink(SideAddress pos) {
		return link[pos.d][pos.r][pos.c];
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
		link[pos.d][pos.r][pos.c] =  l;
	}
	/**
	 * 盤面に Link が複数あるか
	 * @return　Link が複数あるなら true
	 */
	public boolean hasMultipleLinks() {
		return linkList.size() > 1;
	}
	
	/**
	 * マスから上下左右4方向に引かれている線を消去する
	 * マスが黒マスや数字マスに変更された場合に線を消去するために使用する
	 * @param r マスの行座標
	 * @param c マスの列座標
	 */
	private void eraseLinesAround(int r, int c) {
		if (getState(VERT, r, c-1) == LINE)
			changeStateA(VERT, r, c-1, UNKNOWN);
		if (getState(VERT, r, c) == LINE)
			changeStateA(VERT, r, c, UNKNOWN);
		if (getState(HORIZ, r, c) == LINE)
			changeStateA(HORIZ, r, c, UNKNOWN);
		if (getState(HORIZ, r-1, c) == LINE)
			changeStateA(HORIZ, r-1, c, UNKNOWN);
	}
	/**
	 * 盤面状態を変更し，アンドゥリスナーに変更を通知する．
	 * @param r
	 * @param c
	 * @param st
	 */
	public void changeStateA(int r, int c, int st) {
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new PaintStep(r, c, number[r][c], st)));
		setNumber(r, c, st);	
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
			new UndoableEditEvent(this, new LineStep(d, r, c, state[d][r][c], st)));
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
	 * マスの状態を 未定⇔st で切り替える
	 * @param r 行座標
	 * @param c 列座標
	 * @param st 切り替える状態
	 */
	public void toggleState(int r, int c, int st) {
		if (isNumber(r, c))
			return;
		if (st == BLACK) {
			eraseLinesAround(r,c);
		}
		if (number[r][c] == st)
			changeStateA(r, c, UNKNOWN);
		else
			changeStateA(r, c, st);
	}
	/**
	 * 始点マスと終点マスを結んだ線上の状態を指定の状態に変更する
	 * 始点マスと終点マスは同じ行または同じ列になければならない
	 * @param pos0 始点マスの座標
	 * @param pos1 終点マスの座標
	 * @param st 変更後の状態
	 */
	public void determineInlineState(Address pos0, Address pos1, int st) {
		int ra = pos0.r<pos1.r ? pos0.r : pos1.r;
		int rb = pos0.r<pos1.r ? pos1.r : pos0.r;
		int ca = pos0.c<pos1.c ? pos0.c : pos1.c;
		int cb = pos0.c<pos1.c ? pos1.c : pos0.c;
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

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (!isNumber(r,c)) {
					number[r][c] = UNKNOWN;
				}
			}
		}
		initBoard();
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
	 * そのマスが連続黒マスかどうかを調べる
	 * つまり，そのマスが黒マスで，上下左右の隣接４マスに黒マスがあるかどうかを調べる
	 * @param r
	 * @param c
	 * @return 連続黒マスならば true
	 */
	boolean isBlock(int r, int c) {
		if (isBlack(r,c)) {
			if (isBlack(r-1,c)) return true;
			if (isBlack(r+1,c)) return true;
			if (isBlack(r,c-1)) return true;
			if (isBlack(r,c+1)) return true;
		}
		return false;
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
			initLink(r  , c  );
			longerLink = initializingLink;
			initLink(r  , c+1);
			if (initializingLink.size() > longerLink.size()) longerLink = initializingLink;
		}
		else if (d==HORIZ) {
			initLink(r  , c  );
			longerLink = initializingLink;
			initLink(r+1, c  );
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
		if (r < rows() - 1 && isLine(HORIZ, r, c))
			no++;
		if (c < cols() - 1 && isLine(VERT, r, c))
			no++;
		if (r > 0 && isLine(HORIZ, r - 1, c))
			no++;
		if (c > 0 && isLine(VERT, r, c - 1))
			no++;
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

	public int checkAnswerCode() {
		int result = 0;
		result |= checkLinks();
		result |= checkArrows();
		return result;
	}

	private int checkLinks() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				int l = countLine(r,c);
				if (l > 2) {
					result |= 1;
				} else if ( l == 1 ) {
					result |= 2; 
				}
				if (!isNumber(r,c) && !isBlack(r,c) && l == 0)
					result |= 8;
			}
		}
		if (hasMultipleLinks())
			result |= 4;
		return result;
	}
	private int checkArrows() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getNumber(r,c) >= 0) {
					result |= checkArrow(r,c);
				}
			}
		}
		return result;
	}
	private int checkArrow(int r, int c) {
		int result = 0;
		int blackCount = 0;
		int dir = getArrowDirection(r,c);
		int number = getArrowNumber(r,c);
		Address pos = new Address(r,c);
		pos.move(dir);
		while (isOn(pos)) {
			if (isBlack(pos.r,pos.c))
				blackCount++;
			pos.move(dir);
		}
		if (number == blackCount)
			result = 0;
		else
			result = 16;
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE; 
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append("線が分岐または交差している\n");
		if ((result & 2) == 2)
			message.append("閉じていない線がある\n");
		if ((result & 4) == 4)
			message.append("複数の線がある\n");
		if ((result & 8) == 8)
			message.append("線の通っていないマスがある\n");
		if ((result & 16) == 16)
			message.append("黒マスの数が数字と一致していない矢印がある\n");
		return message.toString();
	}

	/**
	 * １手の操作を表すクラス
	 * UNDO, REDO での編集の単位となる
	 */
	class Step extends AbstractUndoableEdit {
	}

	/**
	 * １手の操作を表すクラス
	 * UNDO, REDO での編集の単位となる
	 */
	  class LineStep extends Step {

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
		  public LineStep(int d, int r, int c, int b, int a) {
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
	/**
	 * １手の操作を表すクラス
	 * UNDO, REDO での編集の単位となる
	 */
	  class PaintStep extends Step {

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
		  public PaintStep(int r, int c, int b, int a) {
			  super();
			  row = r;
			  col = c;
			  before = b;
			  after = a;
		  }
		  public void undo() throws CannotUndoException {
			  super.undo();
			  setNumber(row, col, before);
		  }
		  public void redo() throws CannotRedoException {
			  super.redo();
			  setNumber(row, col, after);
		  }
	  }

}
