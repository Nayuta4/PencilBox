package pencilbox.hashi;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;


/**
 * 「橋をかけろ」盤面クラス
 */
public class Board extends BoardBase {

	static final int UNDECIDED_NUMBER = 9;

	private Pier[][] pier;
	private Bridge[][] bridgeV;
	private Bridge[][] bridgeH;
	private int maxChain;
	private int nPier;
	private int nBridge;

	protected void setup() {
		super.setup();
		bridgeV = new Bridge[rows()][cols()];
		bridgeH = new Bridge[rows()][cols()];
		pier = new Pier[rows()][cols()];
		maxChain = 1;
		nPier = 0;
		nBridge = 0;
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
			ret += bridgeV[r][c].getBridge();
		if (bridgeH[r][c] != null)
			ret += (bridgeH[r][c].getBridge() << 2);
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
			bridgeV[r][c].setBridge(n & 0x3);
		if (bridgeH[r][c] != null)
			bridgeH[r][c].setBridge((n>>2) & 0x3);
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
			return bridgeV[r][c].getBridge();
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
			return bridgeH[r][c].getBridge();
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
		Address pos = new Address(pos0);
		while(true) {
			pos.move(d);
			if (pos.equals(pos1)) break;
			setBridge(pos.r(), pos.c(), d&1, b);
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
				nBridge ++;
			}
		}
		nPier ++;
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
					nBridge --;
				} else {
					setBridge(p.getPos(), p1.getPos(), d, null);
					p1.setBridge(d^2, null);
					nBridge --;
				}
			} else {
				if (p2 != null) {
					setBridge(p.getPos(), p2.getPos(), d^2, null);
					p2.setBridge(d, null);
					nBridge --;
				} else {
				}
			}
		}

		pier[r][c] = null;
		nPier --;
	}

	/**
	 * @param nBridge The nBridge to set.
	 */
	void setNBridge(int nBridge) {
		this.nBridge = nBridge;
	}

	/**
	 * @return Returns the nBridge.
	 */
	int getNBridge() {
		return nBridge;
	}

	/**
	 * @param nPier The nPier to set.
	 */
	void setNPier(int nPier) {
		this.nPier = nPier;
	}

	/**
	 * @return Returns the nPier.
	 */
	int getNPier() {
		return nPier;
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
		Address pos = new Address(r, c);
		pos.move(direction);
		while (isOn(pos)) {
			if (isPier(pos)) {
				return pier[pos.r()][pos.c()];
			}
			pos.move(direction);
		}
		return null;
	}
	/**
	 * 橋をかける
	 * @param r 起点の行座標
	 * @param c 起点の列座標
	 * @param direction 方向（上下左右）
	 */
	public void addBridge(int r, int c, int direction) {
		if (!isPier(r, c))
			return;
		if (pier[r][c].getNextPier(direction) == null)
			return;
		if (pier[r][c].getNBridge(direction) == 2)
			return;
		pier[r][c].increaseBridge(direction);
		if (pier[r][c].getNBridge(direction) == 1)
			connectChain(pier[r][c], pier[r][c].getNextPier(direction));
	}

	/**
	 * 橋を除く
	 * @param r 起点の行座標
	 * @param c 起点の列座標
	 * @param direction 方向（上下左右）
	 */
	public void removeBridge(int r, int c, int direction) {
		if (!isPier(r, c))
			return;
		if (pier[r][c].getNextPier(direction) == null)
			return;
		if (pier[r][c].getNBridge(direction) == 0)
			return;
		pier[r][c].decreaseBridge(direction);
		if (pier[r][c].getNBridge(direction) == 0)
			cutChain(pier[r][c], pier[r][c].getNextPier(direction));
	}

	/**
	 * 橋をかける，アンドゥリスナーに通知する
	 * @param pos 起点の座標
	 * @param direction 方向（上下左右）
	 */
	public void addBridgeA(Address pos, int direction) {
		addBridge(pos.r(), pos.c(), direction);
		fireUndoableEditUpdate(new UndoableEditEvent(this,
				new Step(pos.r(), pos.c(), direction, Step.ADDED)));
	}
	/**
	 * 橋を除く，アンドゥリスナーに通知する
	 * @param pos 起点の座標
	 * @param direction 方向（上下左右）
	 */
	public void removeBridgeA(Address pos, int direction) {
		removeBridge(pos.r(), pos.c(), direction);
		fireUndoableEditUpdate(new UndoableEditEvent(this, 
				new Step(pos.r(), pos.c(), direction, Step.REMOVED)));
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
					if (pier[r][c].totalBridges() == 0)
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
			if (p.getNBridge(d) > 0)
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

		int a = pierA.totalBridges();
		int b = pierB.totalBridges();

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
	public int checkPier(int r, int c) {
		int number = pier[r][c].getNumber();
		int bridges = pier[r][c].totalBridges();
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
					if (pier[r][c].totalBridges() != pier[r][c].getNumber())
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
	
	static final String ERR_CROSS_BRIDGE = "橋が交差している\n";
	static final String YET_MULTIPLE_LINE = "全体がひとつながりになっていない\n";
	static final String ERR_WRONG_NUMBER = "橋の数が数字と一致していない\n";
//	static final String ERR_TOO_MANY_LINE = "橋の数が多すぎる数字がある\n";
//	static final String YET_TOO_FEW_LINE= "橋の数が足りない数字がある\n";
	
	int sumAllNumbers() {
		int ret = 0;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r,c))
					ret += pier[r][c].getNumber(); 
			}
		}
		return ret/2;
	}

	/**
	 * １手の操作を表すクラス
	 * UNDO, REDO での編集の単位となる
	 */
	class Step extends AbstractUndoableEdit {

		static final int ADDED = 1;
		static final int REMOVED = -1;

		int row;
		int col;
		int direction;
		int change;

		/**
		 * コンストラクタ
		 * @param r 変更された橋の起点マスの行座標
		 * @param c 変更された橋の起点マスの列座標
		 * @param dir マスからみた変更された橋の方向
		 * @param ch 追加されたのか，除去されたのか
		 */
		public Step(int r, int c, int dir, int ch) {
			super();
			row = r;
			col = c;
			direction = dir;
			change = ch;
		}
		
		public void undo() throws CannotUndoException {
			super.undo();
			if (change == ADDED) {
				removeBridge(row, col, direction);
			} else if (change == REMOVED) {
				addBridge(row, col, direction);
			}
		}
		
		public void redo() throws CannotRedoException {
			super.redo();
			if (change == ADDED) {
				addBridge(row, col, direction);
			} else if (change == REMOVED) {
				removeBridge(row, col, direction);
			}
		}
	}
}
