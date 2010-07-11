package pencilbox.yajilin;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;

/**
 * 「ヤジリン」盤面クラス
 */
public class Board extends BoardBase {
	
	public static final int UNKNOWN = 0;
	public static final int LINE = 1;
	public static final int NOLINE = -1; // GUIでは不使用
	public static final int BLANK = -3;
	public static final int WHITE = -1;
	public static final int BLACK = -2;
	public static final int OUTER = -9;
	public static final int UNDECIDED_NUMBER = -4;

	private int[][] number;  // マスの状態
	private int[][][] state; // 辺の状態

	private List<Link> linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		ArrayUtil.initArrayInt2(number, BLANK);
		state = new int[2][][];
		state[0] = new int[rows()][cols() - 1];
		state[1] = new int[rows() - 1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[0] = new Link[rows()][cols() - 1];
		link[1] = new Link[rows() - 1][cols()];
	}

	/**
	 * 引数の座標が黒マスかどうか。
	 * @param p 座標
	 * @return 黒マスなら true を返す。
	 */
	public boolean isBlack(Address p) {
		return isOn(p) && getNumber(p) == BLACK;
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

	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * 指定したマスの状態を取得する
	 * @param r 行座標
	 * @param c 列座標
	 */
	public int getNumber(int r, int c) {
		return number[r][c]; 
	}

	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	/**
	 * 指定したマスの数字部分のみを返す。数字マスに対して使用する。
	 * @param p 座標
	 * @return マスの数字を返す。数字マスでなければ -1
	 */
	public int getArrowNumber(Address p) {
		int n = getNumber(p);
		return n >=0 ? n&15 : -1; 
	}
	
	/**
	 * 指定したマスに上向き矢印の数字を設定する。
	 * @param p 座標
	 */
	public void setArrowNumber(Address p, int n) {
		setNumber(p, n);
	}
	/**
	 * 指定したマスの矢印の方角を返す。数字マスに対して使用する。
	 * @param p 座標
	 * @return 矢印の方角定数返す。数字マスでなければ -1
	 */
	public int getArrowDirection(Address p) {
		int n = getNumber(p);
		return n >= 0 ? (n>>4) & 3 : -1; 
	}
	/**
	 * 指定したマスの矢印の方角を設定する。数字マスに対して使用する。
	 * @param p 座標
	 */
	public void setArrowDirection(Address p, int dir) {
		if (dir < 0 || dir > 3) return;
		int n = (getNumber(p) & ~(3 << 4)) | (dir << 4); 
		setNumber(p, n);
	}
	/**
	 * 数字マスか
	 * @param p 座標
	 * @return 数字マスか，未定数字マスであれば true
	 */
	public boolean isNumber(Address p) {
		int n = getNumber(p);
		return n >= 0 || n == UNDECIDED_NUMBER;
	}

	/**
	 * 線座標の両側の2マスいずれかが数字マスないし黒マスかどうか
	 * @param b
	 * @return 線座標の両側の2マスいずれかが数字マスないし黒マスであれば true
	 */
	public boolean hasNumberOrBlack(SideAddress b) {
		for (int d = 0; d < 2; d++) {
			Address p = SideAddress.nextCellFromBorder(b, d);
			if (isNumber(p) || isBlack(p))
				return true;
		}
		return false;
	}
	
	/**
	 * 空白マスにする
	 * @param p 座標
	 */
	public void eraseNumber(Address p) {
		setNumber(p, BLANK);
	}
	/**
	 * 数字の入力を受け付ける
	 * 今の数字と同じ数字であれば矢印の向きを変える。
	 * そうでなければ新しく上向き矢印付きの数字を設定する。
	 * 2桁数字を入力するには？
	 * 数字を消去する
	 * @param p マス座標
	 * @param n 入力された数字
	 */
	public void enterNumber(Address p, int n) {
		int v = -99;
		if (n >= 0) {
			int t = getArrowDirection(p);
			if (getArrowNumber(p) == n) {
				t = (t+1)%4;
			} else {
			}
			v = getNumberValue(n, t);
		} else if (n == Board.UNDECIDED_NUMBER) {
			v = n;
		} else if (n == Board.BLANK) {
			v = n;
		}
		if (v != Board.BLANK) {
			eraseLinesAround(p);
		}
		changeNumber(p, v);
	}

	/**
	 * 引数の数字と向きのデータ上を持つマスの内部データ上の値を取得する
	 * 0以上の数字以外を受けたときはそのままの値を返す
	 * @param n  数字　
	 * @param d 向き
	 * @return 値
	 */
	static int getNumberValue(int n, int d) {
		if (d >= 0 && d <= 3) {
			n &= ~(3 << 4);
			n |= (d << 4); 
		}
		return n;
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

	public int getState(SideAddress pos) {
		return getState(pos.d(), pos.r(), pos.c());
	}
	/**
	 * 辺状態の設定
	 * @param d
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setState(int d, int r, int c, int st) {
		if (isSideOn(d, r, c))
			state[d][r][c] = st;
	}

	public void setState(SideAddress pos, int st) {
		setState(pos.d(), pos.r(), pos.c(), st);
	}

	public Link getLink(SideAddress p) {
		if (isSideOn(p))
			return link[p.d()][p.r()][p.c()];
		else
			return null;
	}
	/**
	 * そのマスを含む Link を返す
	 */
	public Link getLink(Address p) {
		for (int d = 0; d < 4; d++) {
			Link link = getLink(SideAddress.get(p, d));
			if (link != null)
				return link;
		}
		return null;
	}

	public void setLink(SideAddress p, Link l) {
		link[p.d()][p.r()][p.c()] =  l;
	}
	/**
	 * マスから上下左右4方向に引かれている線を消去する
	 * マスが黒マスや数字マスに変更された場合に線を消去するために使用する
	 * @param p マスの座標
	 */
	void eraseLinesAround(Address p) {
		for (int d = 0; d < 4; d++) {
			SideAddress side = SideAddress.get(p, d);
			if (getState(side) == LINE) {
				changeState(side, UNKNOWN);
			}
		}
	}
	/**
	 * 盤面状態を変更し，アンドゥリスナーに変更を通知する．
	 * @param p
	 * @param st
	 */
	public void changeNumber(Address p, int st) {
		int prev = getNumber(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		setNumber(p, st);
	}

	/**
	 * 辺の状態を指定した状態に変更する
	 * アンドゥリスナーに変更を通知する
	 * @param p 辺座標
	 * @param st 変更後の状態
	 */
	public void changeState(SideAddress p, int st) {
		int prev = getState(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new BorderEditStep(p, prev, st));
		setState(p, st);
		if (prev == LINE) {
			cutLink(p);
		}
		if (st == LINE) {
			connectLink(p);
		}
	}

	public void undo(AbstractStep step) {
		if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeState(s.getPos(), s.getBefore());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeState(s.getPos(), s.getAfter());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getAfter());
		}
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		for (Address p : cellAddrs()) {
			if (!isNumber(p)) {
				setNumber(p, BLANK);
			}
		}
		initBoard();
	}
	
	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getNumber(p) == WHITE) {
				setNumber(p, BLANK);
			}
		}
	}

	public void initBoard() {
		initLinks();
	}
	
	void initLinks() {
		Link.resetId();
		linkList.clear();
		ArrayUtil.initArrayObject2(link[0],null);
		ArrayUtil.initArrayObject2(link[1],null);
		for (Address p : cellAddrs()) {
			initLink(p);
		}
	}
	
	/**
	 * そのマスの上下左右の隣接４マスに黒マスがあるかどうかを調べる
	 * @param p
	 * @return 上下左右に黒マスがひとつでもあれば true
	 */
	boolean isBlock(Address p) {
		for (int d=0; d<4; d++) {
			Address p1 = p.nextCell(d);
			if (isBlack(p1))
				return true;
		}
		return false;
	}

	/**
	 * あるマスを含む Link の初期化
	 * link[][][] は消去されているものとする
	 * @param p Link初期化の起点マスの座標
	 */
	void initLink(Address p) {
		initializingLink = new Link();
		for (int d = 0; d < 4; d++) {
			initLink1(SideAddress.get(p, d));
		}
		if (!initializingLink.isEmpty())
			linkList.add(initializingLink);
	}

	private void initLink1(SideAddress p) {
		if (!isSideOn(p))
			return;
		if (getState(p) != LINE)
			return;
		if (getLink(p) != null)
			return;
		initializingLink.add(p);
		setLink(p, initializingLink);
		for (int d = 0; d < 6; d++) {
			initLink1(SideAddress.nextBorder(p, d));
		}
	}
	/**
	 * Link 併合
	 */	
	void connectLink(SideAddress p) {
		Link newLink = new Link();
		for (int d = 0; d < 2; d++) {
			Link link = getLink(SideAddress.nextCellFromBorder(p, d));
			if (link != null && (link.size() > newLink.size()))
				newLink = link;
		}
		if (newLink.isEmpty()) {
			linkList.add(newLink);
		}
		for (int d = 0; d < 2; d++) {
			Link link = getLink(SideAddress.nextCellFromBorder(p, d));
			if (link != null && link != newLink) {
				for(SideAddress b : link) {
					setLink(b, newLink);
					newLink.add(b);
				}
				linkList.remove(link);
			}
		}
		newLink.add(p);
		setLink(p, newLink);
	}
	/**
	 * Link 切断
	 */
	void cutLink(SideAddress p) {
		Link oldLink = getLink(p);
		Link longerLink = new Link();
		for (SideAddress b : oldLink) {
			setLink(b, null);
		}
		linkList.remove(oldLink);
		for (int d = 0; d < 2; d++) {
			Address p1 = SideAddress.nextCellFromBorder(p, d);
			initLink(p1);
			if (initializingLink.size() > longerLink.size())
				longerLink = initializingLink;
		}
		longerLink.setId(oldLink.getId());
	}

	/**
	 * マスの上下左右4方向のうち，現在線が引かれている数を返す
	 * @param p マスの座標
	 * @return マスの上下左右に引かれている線の数
	 */
	public int countLine(Address p) {
		int no = 0;
		for (int d = 0; d < 4; d++) {
			SideAddress b = SideAddress.get(p, d);
			if (isSideOn(b) && getState(b) == LINE)
				no ++;
		}
		return no;
	}

	private int checkLinks() {
		int result = 0;
		for (Address p : cellAddrs()) {
			int l = countLine(p);
			if (l > 2) {
				result |= 1;
			} else if ( l == 1 ) {
				result |= 2; 
			}
			if (isBlack(p) && (l > 0))
				result |= 64;
			if (!isNumber(p) && !isBlack(p) && (l == 0))
				result |= 8;
		}
		if (linkList.size() > 1)
			result |= 4;
		return result;
	}
	
	private int checkArrows() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getNumber(p) >= 0) {
				result |= checkArrow(p);
			}
			if (isBlack(p)) {
				if (isBlock(p)) {
					result |= 32;
				}
			}
		}
		return result;
	}
	
	int checkArrow(Address p0) {
		int result = 0;
		int blackCount = 0;
		int dir = getArrowDirection(p0);
		int number = getArrowNumber(p0);
		Address p = Address.nextCell(p0, dir);
		while (isOn(p)) {
			if (isBlack(p))
				blackCount++;
			p = Address.nextCell(p, dir);
		}
		if (number == blackCount)
			result = 0; // 数字と黒マス数が一致する
		else
			result = 16; // 数字と黒マス数が一致しない
		return result;
	}

	public int checkAnswerCode() {
		int result = 0;
		result |= checkLinks();
		result |= checkArrows();
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE; 
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(Messages.getString("yajilin.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("yajilin.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("yajilin.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("yajilin.AnswerCheckMessage4")); //$NON-NLS-1$
		if ((result & 64) == 64)
			message.append(Messages.getString("yajilin.AnswerCheckMessage7")); //$NON-NLS-1$
		if ((result & 32) == 32)
			message.append(Messages.getString("yajilin.AnswerCheckMessage6")); //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("yajilin.AnswerCheckMessage5")); //$NON-NLS-1$
		return message.toString();
	}

}
