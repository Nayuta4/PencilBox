package pencilbox.masyu;

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
 * 「ましゅ」盤面クラス
 */
public class Board extends BoardBase {

	static final int UP = Direction.UP;
	static final int DN = Direction.DN;
	static final int LT = Direction.LT;
	static final int RT = Direction.RT;

	static final int UNKNOWN = 0;
	static final int LINE = 1;
	static final int NOLINE = -1;
	static final int OUTER = -9;
	static final int BLANK = 0;
	static final int WHITE_PEARL = 1;
	static final int BLACK_PEARL = 2;
	static final int GRAY_PEARL = 3;

	private int[][] number;
	private int[][][] state;

	private List<Link> linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		state = new int[2][][];
		state[0] = new int[rows()][cols()- 1];
		state[1] = new int[rows()- 1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[Direction.VERT] = new Link[rows()][cols() - 1];
		link[Direction.HORIZ] = new Link[rows() - 1][cols()];
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		initBoard();
	}

	public void trimAnswer() {
		for (SideAddress p : borderAddrs()) {
			if (getState(p) == NOLINE) {
				changeState(p, UNKNOWN);
			}
		}
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
	int[][] getNumber() {
		return number;
	}
	/**
	 * 丸の設定
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setNumber(int r, int c, int st) {
		number[r][c] = st;
	}

	public void setNumber(Address pos, int st) {
		setNumber(pos.r(), pos.c(), st);
	}
	/**
	 * 丸の取得
	 * @param r
	 * @param c
	 * @return 丸印の種類を返す
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}

	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}

	public boolean isNumber(Address pos) {
		int n = getNumber(pos);
		return n>=1 && n<=3;
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
//		if (isSideOn(d,r,c))
			state[d][r][c] = st;
	}

	public void setState(SideAddress pos, int st) {
		setState(pos.d(), pos.r(), pos.c(), st);
	}

	/**
	 * マスから direction 方向に線はあるか
	 */
	public boolean isLineJ(Address p0, int direction) {
		SideAddress p = SideAddress.get(p0, direction);
		return (isSideOn(p)) && (getState(p) == LINE);
	}

	public Link getLink(SideAddress pos) {
		if (isSideOn(pos))
			return link[pos.d()][pos.r()][pos.c()];
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

	public void setLink(SideAddress pos, Link l) {
		link[pos.d()][pos.r()][pos.c()] =  l;
	}

	/**
	 * マスの状態を指定した状態に変更する
	 * @param p マス座標
	 * @param n 変更後の状態
	 */
	public void changeNumber(Address p, int n) {
		int prev = getNumber(p);
		if (prev == n)
			return;
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new CellEditStep(p, prev, n));
		}
		setNumber(p, n);
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
		if (getState(p) != Board.LINE)
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
	/**
	 * 白真珠マス上の線の状態を調べる
	 * 次の２種類を間違いと判定する
	 * ━○
	 *   ┃ 曲がる場合
	 * ━━○━━　両隣で直進する場合
	 * @param p
	 * @return
	 * 線が通過して両隣のいずれかで曲がっていれば +2 を
	 * 線が通過していて，間違いがなければ +1 を
	 * 線が通過していて，間違いがあれば -1 を
	 * 線が通過していなければ 0 を返す
	 */
	int checkWhitePearl(Address p) {
		int l = countLine(p);
		if (l > 2)
			return -1;
		else if (l < 2)
			return 0;
		if (isLineJ(p,UP) && isLineJ(p,RT)) return -1;
		if (isLineJ(p,UP) && isLineJ(p,LT)) return -1;
		if (isLineJ(p,DN) && isLineJ(p,RT)) return -1;
		if (isLineJ(p,DN) && isLineJ(p,LT)) return -1;
		if (isLineJ(p,UP) && isLineJ(p,DN)) {
			Address pu = Address.nextCell(p,UP);
			Address pd = Address.nextCell(p,DN);
			if (isLineJ(pu,UP) && isLineJ(pd,DN)) return -1;
			if (isLineJ(pu,RT)) return 2;
			if (isLineJ(pu,LT)) return 2;
			if (isLineJ(pd,RT)) return 2;
			if (isLineJ(pd,LT)) return 2;
			return 1;
		}
		if (isLineJ(p,LT) &&  isLineJ(p,RT)) {
			Address pl = Address.nextCell(p,LT);
			Address pr = Address.nextCell(p,RT);
			if (isLineJ(pl,LT) && isLineJ(pr,RT)) return -1;
			if (isLineJ(pl,UP)) return 2;
			if (isLineJ(pl,DN)) return 2;
			if (isLineJ(pr,UP)) return 2;
			if (isLineJ(pr,DN)) return 2;
			return 1;
		}
		return -9;
	}

	/**
	 * 黒真珠マス上の線の状態を調べる
	 * 次の２種類を間違いと判定する
	 * ━●━  直進する場合
	 * ┏●    隣のマスで曲がる場合
	 * @param p
	 * @return
	 * 線が曲がって両側で直進していれば +2 を
	 * 線が通過していて，間違いがなければ +1 を
	 * 線が通過していて，間違いがあれば -1 を
	 * 線が通過していなければ 0 を返す
	 */
	int checkBlackPearl(Address p) {
		int success = 0;

		int l = countLine(p);
		if (l > 2)
			return -1;
		else if (l < 2)
			return 0;

		if (isLineJ(p,UP) && isLineJ(p,DN)) return -1;
		if (isLineJ(p,RT) && isLineJ(p,LT)) return -1;

		if (isLineJ(p,UP)) {
			Address pu = Address.nextCell(p,UP);
			if (isLineJ(pu,LT)) return -1;
			if (isLineJ(pu,RT)) return -1;
			if (isLineJ(pu,UP)) success |= 2;
			else success |= 1;
		}
		if (isLineJ(p,DN)) {
			Address pd = Address.nextCell(p,DN);
			if (isLineJ(pd,LT)) return -1;
			if (isLineJ(pd,RT)) return -1;
			if (isLineJ(pd,DN)) success |= 2;
			else success |= 1;
		} 
		if (isLineJ(p,LT)) {
			Address pl = Address.nextCell(p,LT);
			if (isLineJ(pl,UP)) return -1;
			if (isLineJ(pl,DN)) return -1;
			if (isLineJ(pl,LT)) success |= 8;
			else success |= 4;
		}
		if (isLineJ(p,RT)) {
			Address pr = Address.nextCell(p,RT);
			if (isLineJ(pr,UP)) return -1;
			if (isLineJ(pr,DN)) return -1;
			if (isLineJ(pr,RT)) success |= 8;
			else success |= 4;
		}
		if (success == 10) return 2;
		else if (success > 0) return 1;
		return -9;
	}

	public int checkAnswerCode() {
		int result = 0;
		int n = 0;
		for (Address p : cellAddrs()) {
			int l = countLine(p);
			if (l > 2) {
				result |= 1;
			} else if ( l == 1 ) {
				result |= 2;
			}
			int pearl = getNumber(p);
			if (pearl == WHITE_PEARL) {
				n = checkWhitePearl(p);
					if (n == -1)
						result |= 4;
					else if (n == 0)
						result |= 32;
			}
			if (pearl == BLACK_PEARL) {
				n = checkBlackPearl(p);
				if (n == -1)
					result |= 8;
				else if (n == 0)
					result |= 64;
			}
		}
		if (linkList.size() > 1)
			result |= 16;
		else if (linkList.size() == 0)
			result |= 128;
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(Messages.getString("masyu.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("masyu.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("masyu.AnswerCheckMessage5")); //$NON-NLS-1$
		if ((result & 128) == 128)
			message.append(Messages.getString("masyu.AnswerCheckMessage8")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("masyu.AnswerCheckMessage3")); //$NON-NLS-1$
		else if ((result & 32) == 32)
			message.append(Messages.getString("masyu.AnswerCheckMessage6")); //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("masyu.AnswerCheckMessage4")); //$NON-NLS-1$
		else if ((result & 64) == 64)
			message.append(Messages.getString("masyu.AnswerCheckMessage7")); //$NON-NLS-1$
		return message.toString();
	}
}
