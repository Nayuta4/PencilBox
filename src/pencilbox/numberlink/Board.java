package pencilbox.numberlink;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.SideAddress;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;

/**
 * 「ナンバーリンク」ヒント付き盤面クラス
 */
public class Board extends BoardBase {

	static final int UNKNOWN = 0;
	static final int LINE = 1;
	static final int NOLINE = -1;
	static final int OUTER = -9;
	static final int UNDECIDED_NUMBER = -1;
	static final int BLANK = 0;

	private int[][] number;
	private int[][][] state;

	private List<Link> linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		state = new int[2][][];
		number = new int[rows()][cols()];
		state[0] = new int[rows()][cols()- 1];
		state[1] = new int[rows()- 1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[0] = new Link[rows()][cols() - 1];
		link[1] = new Link[rows() - 1][cols()];
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
	 * @return the number
	 */
	int[][] getNumber() {
		return number;
	}
	/**
	 * @return the state
	 */
	int[][][] getState() {
		return state;
	}

	public boolean isNumber(int r, int c) {
		return (number[r][c] > 0 || number[r][c] == UNDECIDED_NUMBER);
	}

	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
	}
	public int getNumber(int r, int c) {
		return number[r][c];
	}

	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}

	public void setNumber(int r, int c, int n) {
		if (isOn(r, c))
			number[r][c] = n;
	}

	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
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
			state[d][r][c] = st;
	}

	public void setState(SideAddress pos, int st) {
		setState(pos.d(), pos.r(), pos.c(), st);
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
		link[pos.d()][pos.r()][pos.c()] = l;
	}

	/**
	 * マスの状態を指定した状態に変更する
	 * @param p 辺座標
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

	private void addNumberToLink(Link link, SideAddress b) {
		for (int d = 0; d < 2; d++) {
			Address p = SideAddress.nextCellFromBorder(b, d);
			if (isNumber(p)) {
				link.setNumber(getNumber(p));
			}
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
		if (getState(p) != LINE)
			return;
		if (getLink(p) != null)
			return;
		initializingLink.add(p);
		setLink(p, initializingLink);
		for (int d = 0; d < 6; d++) {
			initLink1(SideAddress.nextBorder(p, d));
		}
		addNumberToLink(initializingLink, p);
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
		addNumberToLink(newLink, p);
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
			if (getState(b) == Board.LINE)
				no++;
		}
		return no;
	}

	public int checkAnswerCode() {
		int result = 0;
		result |= checkBranch();
		result |= checkConnection();
		return result;
	}

	/**
	 * 正答判定の分岐チェック部分
	 */
	private int checkBranch() {
		int result= 0;
		for (Address p : cellAddrs()) {
			int l = countLine(p);
			if (isNumber(p)) {
				if (l > 1)
					result |= 4;
				else if (l == 0)
					result |= 8;
			} else {
				if (l > 2)
					result |= 1;
				else if (l == 1)
					result |= 2;
			}
		}
		return result;
	}
	/**
	 * 正答判定の数字連結チェック部分
	 */
	private int checkConnection() {
		int result = 0;
		for(Link link : linkList) {
			int number = link.getNumber();
			if (number == -1)
				result |= 16;
			else if (number == 0)
				result |= 32;
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 4) == 4)
			message.append(Messages.getString("numberlink.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("numberlink.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 1) == 1)
			message.append(Messages.getString("numberlink.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("numberlink.AnswerCheckMessage4")); //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("numberlink.AnswerCheckMessage5")); //$NON-NLS-1$
		if ((result & 32) == 32)
			message.append(Messages.getString("numberlink.AnswerCheckMessage6")); //$NON-NLS-1$
		return message.toString();
	}
}
