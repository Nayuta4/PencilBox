package pencilbox.hashi;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;


/**
 * 「橋をかけろ」盤面クラス
 */
public class Board extends BoardBase {

	static final int UNDECIDED_NUMBER = 9;

	private Pier[][] pier;
	private Bridge[][] bridgeV;
	private Bridge[][] bridgeH;
	private int maxChain;

	protected void setup() {
		super.setup();
		bridgeV = new Bridge[rows()][cols()];
		bridgeH = new Bridge[rows()][cols()];
		pier = new Pier[rows()][cols()];
		maxChain = 1;
	}

	public void clearBoard() {
		super.clearBoard();
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c))
					pier[r][c].clear();
			}
		}
	}

	public void initBoard() {
		initChain();
	}
	/**
	 * マスに数字を設定する
	 * @param r 設定するマスの行座標
	 * @param c 設定するマスの列座標
	 * @param n 設定する数字
	 */
	public void setNumber(int r, int c, int n) {
		if (n == 0) {
			if (isPier(r, c)) {
				removePier(r, c);
			}
		} else if (n > 0) {
			if (isPier(r, c))
				pier[r][c].setNumber(n);
			else
				addPier(r, c, n);
		}
	}
	
	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * マスの数字を取得する
	 * @param r 設定するマスの行座標
	 * @param c 設定するマスの列座標
	 * @return マスの数字
	 */
	public int getNumber(int r, int c) {
		if (pier[r][c] == null)
			return 0;
		else
			return pier[r][c].getNumber();
	}
	
	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	/**
	 * そのマスが数字マス（橋脚）かどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 数字マスなら true
	 */
	public boolean isPier(int r, int c) {
		return pier[r][c] != null;
	}
	/**
	 * そのマスが数字マス（橋脚）かどうか
	 * @param pos 座標
	 * @return 数字マスなら true
	 */
	public boolean isPier(Address pos) {
		return isPier(pos.r(), pos.c());
	}
	
	/**
	 * マスの状態を取得する
	 * @param r 行座標
	 * @param c 列座標
	 * @return マスの状態
	 */
	public int getState(int r, int c) {
		int ret = 0;
		if (bridgeV[r][c] != null)
			ret += bridgeV[r][c].getLine();
		if (bridgeH[r][c] != null)
			ret += (bridgeH[r][c].getLine() << 2);
		return ret;
	}
	
	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * マスの状態を設定する
	 * @param r 行座標
	 * @param c 列座標
	 * @param n マスの状態
	 */
	public void setState(int r, int c, int n) {
		if (bridgeV[r][c] != null)
			bridgeV[r][c].setLine(n & 0x3);
		if (bridgeH[r][c] != null)
			bridgeH[r][c].setLine((n>>2) & 0x3);
	}
	
	public void setState(Address pos, int n) {
		setState(pos.r(), pos.c(), n);
	}
	/**
	 * そのマスを通過する縦方向の橋の数を返す
	 * @param r 行座標
	 * @param c 列座標
	 * @return　そのマスを通過する縦方向の橋の数
	 */
	public int getVertBridge(int r, int c) {
		if (bridgeV[r][c] == null)
			return -1;
		else
			return bridgeV[r][c].getLine();
	}
	/**
	 * そのマスを通過する横方向の橋の数を返す
	 * @param r 行座標
	 * @param c 列座標
	 * @return　そのマスを通過する横方向の橋の数
	 */
	public int getHorizBridge(int r, int c) {
		if (bridgeH[r][c] == null)
			return -1;
		else
			return bridgeH[r][c].getLine();
	}
	/**
	 * そのマスの上で橋が交差しているかかっているかどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return そのマスの上で橋が交差していれば true
	 */
	public boolean hasCrossedBridge(int r, int c) {
		return getHorizBridge(r, c) > 0 && getVertBridge(r, c) > 0;
	}
	
	/**
	 * @param r 行座標
	 * @param c 列座標
	 * @return Returns the pier.
	 */
	public Pier getPier(int r, int c) {
		return pier[r][c];
	}
	public Pier getPier(Address p) {
		return pier[p.r()][p.c()];
	}
	/**
	 * そのマス上の橋を取得する
	 * @param r 行座標
	 * @param c 列座標
	 * @param dir 縦の橋か横の橋か
	 * @return　そのマスの上の橋
	 */
	public Bridge getBridge(int r, int c, int dir) {
		if (dir == Direction.HORIZ) {
			return bridgeH[r][c];
		} else if (dir == Direction.VERT) {
			return bridgeV[r][c];
		}
		return null;
	}
	/**
	 * マス上にそのマスを通る橋を設定する
	 * @param r 行座標
	 * @param c 列座標
	 * @param dir 縦の橋か横の橋か
	 * @param b 設定するBridge
	 */
	public void setBridge(int r, int c, int dir, Bridge b) {
		if (dir == Direction.HORIZ) {
			bridgeH[r][c] = b;
		} else if (dir == Direction.VERT) {
			bridgeV[r][c] = b;
		}
	}
	/**
	 * 橋の通る盤上の各マスに，その橋を設定する
	 * @param pos0 始点マス
	 * @param pos1 終点マス
	 * @param d 始点から終点を見た向き
	 * @param b Bridge
	 */
	void setBridge(Address pos0, Address pos1, int d, Bridge b) {
		Address pos = Address.address(pos0);
		while(true) {
			pos = pos.nextCell(d);
			if (pos.equals(pos1)) break;
			setBridge(pos.r(), pos.c(), d&1, b);
		}
	}

	/**
	 * あるマスから指定した方向へ出ている橋の数を返す。数字マス以外を指定したときは -1 を返す
	 * @param p 数字マスの座標
	 * @param d 向き
	 * @return 数字マスからある向きへの橋の本数。数字マス以外であれば-1
	 */
	public int getLine(Address p, int d) {
		if (!isPier(p)) {
			return -1;
		} else {
			return getPier(p).getLine(d);
		}
	}

	/**
	 * 指定した座標に橋脚を新規に作成する
	 * そのマスにもともとかかっていた橋は除去される
	 * @param r 行座標
	 * @param c 列座標
	 * @param n 数字
	 */
	void addPier(int r, int c, int n) {
		Pier p = new Pier(r, c, n);
		pier[r][c] = p;
		for (int d = 0; d < 4; d++) {
			Pier next = findPier(r, c, d);
			if (next != null) {
				next.setNextPier(d^2,p);
				p.setNextPier(d,next);
				Bridge b = new Bridge(p, next);
				next.setBridge(d^2, b);
				p.setBridge(d, b);
				setBridge(p.getPos(), next.getPos(), d, b);
			}
		}
	}
	/**
	 * 指定した座標にある橋脚を除去する
	 * その橋から出ていた橋は除去される
	 * @param r 行座標
	 * @param c 列座標
	 */
	void removePier(int r, int c) {
		
		Pier p = pier[r][c];
		
		for (int d=0; d<4; d++) {
			Pier p1 = p.getNextPier(d);
			Pier p2 = p.getNextPier(d^2);
			if (p1 != null) {
				if (p2 != null)
					p1.setNextPier(d^2, p2);
				else 
					p1.setNextPier(d^2, null);
			}
		}
		for (int d=0; d<2; d++) {
			Pier p1 = p.getNextPier(d);
			Pier p2 = p.getNextPier(d^2);
			if (p1 != null) {
				if (p2 != null) {
					Bridge b = new Bridge(p1, p2);
					setBridge(p2.getPos(), p1.getPos(), d, b);
					p1.setBridge(d^2, b);
					p2.setBridge(d, b);
				} else {
					setBridge(p.getPos(), p1.getPos(), d, null);
					p1.setBridge(d^2, null);
				}
			} else {
				if (p2 != null) {
					setBridge(p.getPos(), p2.getPos(), d^2, null);
					p2.setBridge(d, null);
				} else {
				}
			}
		}

		pier[r][c] = null;
	}

	/**
	 * @return Returns the maxChain.
	 */
	int getMaxChain() {
		return maxChain;
	}

	/**
	 * 起点から指定した方向にある最初の橋脚を返す
	 * @param r 起点の行座標
	 * @param c 起点の列座標
	 * @param direction 橋脚を探す向き
	 * @return 起点から指定した方向にある最初の橋脚
	 */
	Pier findPier(int r, int c, int direction) {
		Address pos = Address.address(r, c);
		pos = pos.nextCell(direction);
		while (isOn(pos)) {
			if (isPier(pos)) {
				return pier[pos.r()][pos.c()];
			}
			pos = pos.nextCell(direction);
		}
		return null;
	}
	/**
	 * 橋をかける／除く，アンドゥリスナーに通知する
	 * @param p 起点の座標
	 * @param d 方向（上下左右）
	 * @param n 橋の増減数
	 */
	public void changeLine(Address p, int d, int n) {
		if (!isPier(p))
			return;
		Pier pp= pier[p.r()][p.c()];
		if (pp.getNextPier(d) == null)
			return;
		int prev = pp.getLine(d);
		if (n < 0 || n > 2)
			return;
		if (prev == n)
			return;
		pp.changeLine(d, n);
		if (n == 0)
			cutChain(pp, pp.getNextPier(d));
		if (prev == 0)
			connectChain(pp, pp.getNextPier(d));
		if (isRecordUndo())
			fireUndoableEditUpdate(new BridgeEditStep(p, d, prev, n));
	}

	public void undo(AbstractStep step) {
		BridgeEditStep s = (BridgeEditStep) step;
		changeLine(s.getPos(), s.getDirection(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		BridgeEditStep s = (BridgeEditStep) step;
		changeLine(s.getPos(), s.getDirection(), s.getAfter());
	}

	/**
	 * 橋の連結番号を初期化する
	 */
	void initChain() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c)) {
					pier[r][c].setChain(0);
				}
			}
		}
		maxChain = 1;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c)) {
					if (pier[r][c].totalLines() == 0)
						pier[r][c].setChain(0);
					else if (pier[r][c].getChain() == 0) {
						initChain1(pier[r][c], maxChain++);
					}
				}
			}
		}
	}

	/**
	 * あるマスを始点とする chain番号の初期化
	 * @param p 始点となる橋脚
	 * @param chain 番号
	 */
	void initChain1(Pier p, int chain) {
		if (p.getChain() == chain)
			return;
		p.setChain(chain);
		for (int d = 0; d < 4; d++) {
			if (p.getLine(d) > 0)
				initChain1(p.getNextPier(d), chain);
		}
	}
	/**
	 * chain 併合
	 * ドラッグ元に合わせる
	 * @param pierA
	 * @param pierB
	 */
	void connectChain(Pier pierA, Pier pierB) {

		int a = pierA.getChain();
		int b = pierB.getChain();

		if (a == 0) {
			if (b == 0) {
				pierA.setChain(maxChain);
				pierB.setChain(maxChain++);
			} else if (b > 0) {
				pierA.setChain(b);
			}
		} else if (a > 0) {
			if (b == 0) {
				pierB.setChain(a);
			} else if (b > 0) {
				initChain1(pierB, a);
			}
		}
	}

	/**
	 * chain 切断
	 * ドラッグ元番号を元の番号に残す
	 * @param pierA
	 * @param pierB
	 */
	void cutChain(Pier pierA, Pier pierB) {

		int a = pierA.totalLines();
		int b = pierB.totalLines();

		if (a == 0) {
			pierA.setChain(0);
			if (b == 0) {
				pierB.setChain(0);
			} else if (b > 0) {
			}
		} else if (a > 0) {
			if (b == 0) {
				pierB.setChain(0);
			} else if (b > 0) {
				initChain1(pierB, maxChain++);
			}
		}
	}

	/**
	 * そのマスから出る橋の数が正解に達しているかどうか調べる
	 * @param r 行座標
	 * @param c 列座標
	 * @return >0: 少なすぎる, =0: ちょうど, <0 多すぎる
	 */
	public int checkPier(Address p) {
		int number = getPier(p).getNumber();
		int bridges = getPier(p).totalLines();
		if (number == UNDECIDED_NUMBER)
			return 1;
		return number - bridges;
	}

	public int checkAnswerCode() {
		int result = 0;
		if (checkCross() == false)
			result |= 1;
		if (checkConnection() == false)
			result |= 2;
		if (checkNumbers() == false)
			result |= 4;
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(ERR_CROSS_BRIDGE);
		if ((result & 2) == 2)
			message.append(YET_MULTIPLE_LINE);
		if ((result & 4) == 4)
			message.append(ERR_WRONG_NUMBER);
		return message.toString();
	}
	private boolean checkNumbers() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c)) {
					if (pier[r][c].getNumber() == UNDECIDED_NUMBER)
						continue;
					if (pier[r][c].totalLines() != pier[r][c].getNumber())
						return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkConnection() {
		int n = 0;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c)) {
					int m = pier[r][c].getChain();
					if (m == 0)
						return false;
					else if (n == 0)
						n = m;
					else if (n != m)
						return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkCross() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (hasCrossedBridge(r, c))
					return false;
			}
		}
		return true;
	}
	
	static final String ERR_CROSS_BRIDGE = Messages.getString("hashi.AnswerCheckMessage1"); //$NON-NLS-1$
	static final String YET_MULTIPLE_LINE = Messages.getString("hashi.AnswerCheckMessage2"); //$NON-NLS-1$
	static final String ERR_WRONG_NUMBER = Messages.getString("hashi.AnswerCheckMessage3"); //$NON-NLS-1$
//	static final String ERR_TOO_MANY_LINE = "橋の数が多すぎる数字がある\n";
//	static final String YET_TOO_FEW_LINE= "橋の数が足りない数字がある\n";
	
}
