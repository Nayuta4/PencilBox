package pencilbox.slalom;

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
 * 「スラローム」盤面クラス
 */
public class Board extends BoardBase {

	static final int UNKNOWN = 0;
	static final int LINE = 1;
	static final int NOLINE = -1;
	static final int BLANK = -3;
	static final int GOAL = -1;
	static final int OUTER = -9;
	static final int UNDECIDED_NUMBER = 0;
	static final int GATE_VERT = -5;
	static final int GATE_HORIZ = -4;

	private int[][] number;  // マスの状態
	private int[][][] state; // 辺の状態
	private int[][] gateNumber;  // 門の番号
	private int nGate;  // 旗門の数
	private Address goal;  // スタート／ゴール地点の座標。１か所のみとする

	private List<Link> linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		gateNumber = new int[rows()][cols()];
		ArrayUtil.initArrayInt2(number, BLANK);
		goal = Address.nowhere();
		state = new int[2][][];
		state[Direction.VERT] = new int[rows()][cols()-1];
		state[Direction.HORIZ] = new int[rows()-1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[Direction.VERT] = new Link[rows()][cols()-1];
		link[Direction.HORIZ] = new Link[rows()-1][cols()];
	}

	/**
	 * 指定したマスに数字マス，黒マス，白マス，空白マスの状態を設定する
	 * ゴールは１か所以下とする。
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
	 * 黒マスか
	 * @param p 座標
	 * @return 黒マスであれば true
	 */
	public boolean isWall(Address p) {
		return getNumber(p) >= 0 || getNumber(p) == UNDECIDED_NUMBER;
	}
	/**
	 * 旗門マスか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 数字マスか，未定数字マスであれば true
	 */
	public boolean isGate(int r, int c) {
		return number[r][c] == GATE_HORIZ || number[r][c] == GATE_VERT;
	}

	public boolean isGate(Address pos) {
		return isGate(pos.r(), pos.c());
	}
	/**
	 * 線座標の両側の2マスいずれかが黒マスかどうか
	 * @param p 線座標
	 * @return 線座標の両側の2マスいずれかが黒マスであれば true
	 */
	public boolean hasWall(SideAddress p) {
		return isWall(SideAddress.nextCellFromBorder(p, 0))
				|| isWall(SideAddress.nextCellFromBorder(p, 1));
	}
	/**
	 * @param p
	 * @return
	 */
	public int getGateNumber(Address p) {
		return gateNumber[p.r()][p.c()];
	}

	public int getGateNumber(int r, int c) {
		return gateNumber[r][c];
	}

	/**
	 * @param p
	 * @return
	 */
	public int setGateNumber(Address p, int n) {
		return gateNumber[p.r()][p.c()] = n;
	}

	public int setGateNumber(int r, int c, int n) {
		return gateNumber[r][c] = n;
	}
	/**
	 * @return the nGate
	 */
	public int getNGate() {
		return nGate;
	}

	/**
	 * @return the Goal
	 */
	public Address getGoal() {
		return goal;
	}

	/**
	 * 辺状態の取得
	 * @param d
	 * @param r
	 * @param c
	 * @return 辺の状態を返す
	 */
	public int getState(int d, int r, int c) {
		if (isSideOn(d, r, c))
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
	 * アンドゥリスナーに変更を通知する
	 * @param p 辺座標
	 * @param st 変更後の状態
	 */
	public void changeNumber(Address p, int st) {
		int prev = getNumber(p);
		if (prev == st)
			return;
		if (prev == Board.GOAL) {
			goal = Address.nowhere();
		}
		if (st == Board.GOAL) {
			if (!goal.isNowhere()) {
				changeNumber(goal, Board.BLANK);
			}
			goal = p;
		}
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		}
		setNumber(p, st);
	}

	/**
	 * マスから上下左右4方向に引かれている線を消去する
	 * マスが黒マスや数字マスに変更された場合に線を消去するために使用する
	 * @param pos マスの座標
	 */
	void eraseLinesAround(Address pos) {
		for (int d = 0; d <= 3; d++) {
			SideAddress side = SideAddress.get(pos, d);
			if (getState(side) == LINE || getState(side) == NOLINE) {
				changeState(side, UNKNOWN);
			}
		}
	}

	/**
	 * 辺の状態を指定した状態に変更する
	 * アンドゥリスナーに変更を通知する
	 * @param p 辺座標
	 * @param st 変更後の状態
	 */
	public void changeState(SideAddress p, int st) {
		int prev = getState(p);
		if (prev == st)
			return;
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new BorderEditStep(p, prev, st));
		}
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
		}
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeState(s.getPos(), s.getAfter());
		}
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getAfter());
		}
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		initBoard();
	}

	public void trimAnswer() {
		for (SideAddress p : borderAddrs()) {
			if (getState(p) == NOLINE)
				setState(p, UNKNOWN);
		}
	}

	public void initBoard() {
		initGates();
		initLinks();
	}

	/**
	 * 初期処理。 盤面全体の旗門とその番号を設定する。
	 */
	void initGates() {
		nGate = 0;
		for (Address p : cellAddrs()) {
			setGateNumber(p, 0);
		}
		for (Address p : cellAddrs()) {
			int n = getNumber(p);
			if (n == GOAL) {
				goal = p;
			} else if (n == GATE_HORIZ) {
				Address p1 = Address.nextCell(p, Direction.LT);
				if (isOn(p1) && getNumber(p1) == GATE_HORIZ) {
				} else {
					nGate ++;
				}
			} else if (n == GATE_VERT) {
				Address p1 = Address.nextCell(p, Direction.UP);
				if (isOn(p1) && getNumber(p1) == GATE_VERT) {
				} else {
					nGate ++;
				}
			}
		}
		for (Address p : cellAddrs()) {
			int n = getNumber(p);
			if (n > 0) {
				initGateNumber(p, n);
			}
		}
	}

	/**
	 * 黒マスから門の反対側の黒マスを探す
	 * @param p0 起点マス
	 * @param d 見る方向
	 * @return　門の反対側に黒マスまたは外周ならばその座標，それ以外はnull
	 */
	Address getAnotherPole(Address p0, int d) {
		Address p = p0;
		int gateType = 0;
		if (d == Direction.UP || d == Direction.DN) {
			gateType = GATE_VERT;
		} else if (d == Direction.LT || d == Direction.RT) {
			gateType = GATE_HORIZ;
		}
		while (true) {
			p = Address.nextCell(p, d);
			if (isOn(p)) {
				if (isWall(p)) {
//					System.out.println(p.toString() + "他端は黒マスだ");
					return p;
				} else if (getNumber(p) == gateType) {
				} else {
//					System.out.println(p.toString() + "他端は閉じていない");
					return null;
				}
			} else {
//				System.out.println(p.toString() + "他端は外周だ");
				return p;
			}
		}
	}

	/**
	 * 黒マスを起点に，ある方向に出ている門に番号を設定する。
	 * @param p0
	 * @param d
	 * @param n
	 */
	private void setGateNumber(Address p0, int d, int n) {
		int t = 0;
		if (d == 0 || d == 2)
			t = GATE_VERT;
		else
			t = GATE_HORIZ;
		Address p = p0;
		while (true) {
			p = p.nextCell(d);
			if (isOn(p) && getNumber(p) == t) {
				setGateNumber(p, n);
			} else {
				break;
			}
		}
	}
	/**
	 * 旗門の番号を調べる。
	 * 門の側から見るのは決めにくいので、黒マス側から見る。
	 * 数字付きの黒マスから上下左右に隣接する門をみる。
	 * 門の反対側に同じ数字の黒マスがあれば，黒マスの数字が門の番号。
	 * 門の反対側に異なる数字の黒マスがあれば，門の番号は -1。
	 * 黒マスに隣接する門が１つだけで，門の番号がまだ決まっていなければ，黒マスの数字が門の番号。
	 * @param p0s
	 * @param n0 正の数とする
	 */
	private void initGateNumber(Address p0, int n0) {
		Address p = p0;
		Address p1 = null;
//		System.out.println(p.toString() + "の黒マスについて調べる。");
		int d1 = -1; // 対岸の黒マスの向きを記録する
		int ng = 0;  // 決定した番号を記録する
		int count = 0; // マスの４方向の門の数を数える
		for (int d = 0; d <= 3; d++) {
			int t = 0;
			if (d == 0 || d == 2)
				t = GATE_VERT;
			else
				t = GATE_HORIZ;
			p = p0;
			p = p.nextCell(d);
			if (isOn(p) && getNumber(p) == t) {
//				System.out.println(d + "の向きに門があった");
				d1 = d;
				count++;
				p1 = getAnotherPole(p, d);
				if (p1 != null && isOn(p1)) {
					int n1 = getNumber(p1);
					if (n1 == n0) { // 対岸の門と同じ番号
						ng = n1;
						setGateNumber(p0, d, n1);
//						System.out.println("対岸の数字マス" + p1.toString() + "との間の門の番号は " + n1);
					} else if (n1 > 0 && n1 != n0) {
						ng = -1;
						setGateNumber(p0, d, -1);
//						System.out.println("対岸の数字マスが異なる番号であるため，門の番号は -1 とする。");
					}
				} else {
//					System.out.println("対岸に数字マスがない");
				}
			}
		}
		if (count == 0) {
//			System.out.println("上下左右４方向に門がない ");
		} else if (count == 1) {
			if (ng == 0) {
				setGateNumber(p0, d1, n0);
//				System.out.println("隣接する門が１つしかないので，その門の番号を " + n0 + "に決める");
			} else if (ng == -1) {
//				System.out.println("隣接する門が１つしかない場合でも、異なる数字にはさまれた門の番号は-1とする。 ");
			} else if (ng > 0) {
//				System.out.println("隣接する門が１つしかないが，決定済み。");
			}
		} else if (count > 1) {
//			System.out.println("複数の門に隣接するので門の番号が決まらない");
		}
	}

	void initLinks() {
		Link.resetId();
		linkList.clear();
		ArrayUtil.initArrayObject2(link[0], null);
		ArrayUtil.initArrayObject2(link[1], null);
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
		if (!initializingLink.isEmpty()) {
			linkList.add(initializingLink);
		}
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
			if (getState(b) == Board.LINE) {
				no++;
			}
		}
		return no;
	}

	/**
	 * 線に関する誤りをチェックする
	 * @return
	 */
	private int checkLinks() {
		int result = 0;
		for (Address p : cellAddrs()) {
			int l = countLine(p);
			if (l > 2) {
				result |= 1;
			} else if (l == 1) {
				result |= 2;
			}
		}
		if (linkList.size() > 1)
			result |= 4;
		else if (linkList.size() == 0)
			result |= 8;
		return result;
	}

	/**
	 * 旗門について、正しく通過しているかどうかを調べる。 
	 * 門と１箇所のみで直交する場合が正しい。
	 * 直交しない場合は誤り。
	 * 複数回直交する場合は誤り。
	 * @return 誤りがあれば正数，なければ0
	 */
	private int checkGates() {
		for (Address p : cellAddrs()) {
			if (isGate(p)) {
				int ret = checkGate1(p);
				if (ret == -1) {
					return 16;
				} else if (ret == 0) {
					return 32;
				} else if (ret > 1) {
					return 64;
				} else if (ret == -2) {
				}
			}
		}
		return 0;
	}

	/**
	 * 旗門のマスに注目して、通り方が正しいかを調べる。
	 * 縦門の上端または横門の左端のマスについて調べる。
	 * 上端または左端以外のマスの場合はすでに調査済みのはず。
	 * @param p 旗門のマスの座標
	 * @return 門の通り方が誤っていれば -1 , 調査済みは -2, 0以上の数は正常に交差した回数を返す。
	 */
	private int checkGate1(Address p0) {
		int gateType = getNumber(p0);
//		System.out.println(p.toString() + "の門を調べる。");
		int d = 0;
		if (gateType == Board.GATE_HORIZ) {
			d = Direction.RT;
		} else if (gateType == Board.GATE_VERT) {
			d = Direction.DN;
		}
		Address p2 = Address.nextCell(p0, d^2);
		if (isOn(p2) && getNumber(p2) == gateType) { // ひとつ手前のマスも同じ向きの門ならば，
			return -2; // 調査済みのはず
		}
		int count = 0;
		Address p = p0;
		while (true) {
			int ret = checkGate2(p);
			if (ret == -1) {
//				System.out.println(p.toString() + "の位置の門の通り方がおかしい。");
				return -1;
			} else if (ret == 1) {
				count ++;
			}
			p = p.nextCell(d);
			if (isOn(p) && getNumber(p) == gateType) {
			} else {
				break;
			}
		}
//		System.out.println(count + "回，門を交差した。");
		return count;
	}

	/**
	 * 門のマスに注目して、通り方が正しいかを調べる。
	 * 門と並行に線が引かれていれば誤り，直交していれば正しい。
	 * @param p　門のマスの座標
	 * @return 平行していれば -1、直交していれば 1, それ以外は 0
	 */
	private int checkGate2(Address p) {
		int type = getNumber(p);
		int st4[] = new int[4];
		for (int d = 0; d < 4; d++) {
			st4[d] = getState(SideAddress.get(p, d));
		}
		if (type == GATE_VERT) {
			if (st4[0] == LINE || st4[2] == LINE) {
//				System.out.println("ゲート" + p.toString() + "と並行に走っている");
				return -1;
			}
			if (st4[1] == LINE && st4[3] == LINE) {
//				System.out.println("ゲート" + p.toString() + "と直交している");
				return 1;
			}
		} else if (type == GATE_HORIZ) {
			if (st4[1] == LINE || st4[3] == LINE) {
//				System.out.println("ゲート" + p.toString() + "と並行に走っている");
				return -1;
			}
			if (st4[0] == LINE && st4[2] == LINE) {
//				System.out.println("ゲート" + p.toString() + "と直交している");
				return 1;
			}
		}
		return 0;
	}

	/**
	 * スタートからゴールまでの門を通った順番が正しいかどうかを調べる。
	 * スタートからゴールまでの経路が分岐なく閉じていることを仮定する。
	 * すべての門を正しく通過していることを前提とする。
	 * ゴールがないときは、順番が正しいかどうかのみ調べる。
	 * @return 正しい順序で通過していれば 0, 誤っていれば正数
	 */
	private int checkRoute() {
		int[] gateNumber = new int[nGate]; // 通過した門の番号を記録する。
		int k = 0;
		Address p0; // スタート／ゴール地点
		if (goal.isNowhere()) {
			// ゴールがない場合は，順番を数える起点とするための仮のゴールを設定する。
			p0 = ｔemporalGoal();
		} else {
			p0 = goal;
			if (getLink(p0) == null) {
				System.out.println("ゴールを通過していない。");
				return 512;
			}
		}
		System.out.println("スタート／ゴール地点は " + p0.toString());
		Address p = p0;
		int d = -1;
		while (true) {
			d = getLineDirection(p, d);
			p = Address.nextCell(p, d);
			if (isGate(p)) {
								System.out.println("ゲート " + getGateNumber(p) + " 通過");
				gateNumber[k] = getGateNumber(p);
				k++;
				if (k > nGate) { // すべての門を正しく通過していることが前提ならば、ありえない
					System.out.println("門" + nGate + "のうち" + k + "箇所めを通過した。通過した門が多すぎる");
					return 128;
				}
			}
			if (p.equals(p0)) {
				System.out.println("ゴール到達");
				break;
			}
		}
		if (k < nGate) { // すべての門を正しく通過していることが前提ならば、ありえない
			System.out.println("門" + nGate + "のうち" + k + "箇所通過した。通過した門が少なすぎる");
			return 128;
		}
		for (k = 0; k < nGate; k++) {
			System.out.print(gateNumber[k]);
			System.out.print(' ');
		}
		System.out.println(" の順番に門を通過した。");
		int gg = 1;
		// ゴールが設定されていなときは、仮ゴールは何番目に通過してもよいことにする。
		if (goal.isNowhere() && nGate >= 1) {
			gg = nGate;
		}
		for (int g = 0; g < gg; g++) {
			for (k = 0; k < nGate; k++) {
				if (gateNumber[k] > 0) { // 門に番号が指定されているとき
					if (gateNumber[k] != ((k + g) % nGate + 1)) {
						break;
					}
				}
			}
			if (k == nGate) {
				System.out.println("門の通過順が正しい。");
				return 0;
			}
		}
		for (int g = 0; g < gg; g++) {
			for (k = 0; k < nGate; k++) {
				if (gateNumber[k] > 0) {
					if (gateNumber[k] != ((nGate - 1 - k + g) % nGate + 1)) {
						break;
					}
				}
			}
			if (k == nGate) {
				System.out.println("門の通過順が正しい。");
				return 0;
			}
		}
		System.out.println("門の通過順が誤っている。");
		return 256;
	}

	/**
	 * ゴールが設定されていない盤面で、仮のゴールを設定する。
	 */
	private Address ｔemporalGoal() {
		for (Address p : cellAddrs()) {
			if (countLine(p) > 1) {
				return p;
			}
		}
		return Address.NOWHERE;
	}

	/**
	 * 今来た方向以外で線の延びている方向をひとつ返す。
	 * リンクをたどるのに用いる。
	 * @param p
	 * @param direction
	 * @return
	 */
	private int getLineDirection(Address p, int direction) {
		for (int d = 0; d < 4; d++) {
			if (getState(SideAddress.get(p, d)) == LINE && direction != (d^2))
				return d;
		}
		return -1;
	}

	public int checkAnswerCode() {
		int result = 0;
		result |= checkLinks();
		result |= checkGates();
		if (result == 0) // ループと門の通り方が正しいときのみ順番をチェックする。
			result |= checkRoute();
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1) // 線が分岐または交差している
			message.append(Messages.getString("slalom.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2) // 閉じていない線がある\n
			message.append(Messages.getString("slalom.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4) // 複数の線がある\n
			message.append(Messages.getString("slalom.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 8) == 8) // 線がない\n
			message.append(Messages.getString("slalom.AnswerCheckMessage4")); //$NON-NLS-1$
		if ((result & 16) == 16) // 門の通り方の間違いがある\n
			message.append(Messages.getString("slalom.AnswerCheckMessage5")); //$NON-NLS-1$
		if ((result & 32) == 32) // 通っていない門がある\n
			message.append(Messages.getString("slalom.AnswerCheckMessage6")); //$NON-NLS-1$
		if ((result & 64) == 64) // 複数回通った門がある\n
			message.append(Messages.getString("slalom.AnswerCheckMessage7")); //$NON-NLS-1$
		if ((result & 256) == 256) // 門を通る順番が誤っている\n
			message.append(Messages.getString("slalom.AnswerCheckMessage8")); //$NON-NLS-1$
		if ((result & 512) == 512) // ○を通っていない\n
			message.append(Messages.getString("slalom.AnswerCheckMessage9")); //$NON-NLS-1$
		return message.toString();
	}
}
