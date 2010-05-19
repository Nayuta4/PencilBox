package pencilbox.goishi;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.resource.Messages;


/**
 *  「碁石ひろい」盤面クラス
 */
public class Board extends BoardBase {

	public static final int BLANK = 0;
	public static final int STONE = -1;
	
	private int[][] state;
	private int[][] number;
	ArrayList<Address> pickedList;
	
	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		number = new int[rows()][cols()];
		pickedList = new ArrayList<Address>();
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			setNumber(p, 0);
		}
		pickedList.clear();
//		initBoard();
	}

	/**
	 * そのマスは碁石があるか
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return 石があるマスなら true, ないマスなら false
	 */
	public boolean isStone(Address pos) {
		return getState(pos) == STONE;
	}

	public int getState(int r, int c) {
		if (!isOn(r, c))
			return Board.BLANK;
		return state[r][c];
	}

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}

	public void setState(int r, int c, int st) {
		if (!isOn(r, c))
			return;
		state[r][c] = st;
	}

	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}

	public int getNumber(int r, int c) {
		return number[r][c];
	}

	public int getNumber(Address pos) {
		return number[pos.r()][pos.c()];
	}

	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}

	public void setNumber(Address pos, int n) {
		number[pos.r()][pos.c()] = n;
	}

	/**
	 * @param pos
	 */
	public void addStone(Address pos) {
		if (getState(pos) == STONE)
			return;
		setState(pos, STONE);
		rePickUpAll();
	}

	/**
	 * 石を取り除く。
	 * もしもすでに拾った石を取り除いたときは、拾いなおす。
	 * @param pos
	 */
	public void removeStone(Address pos) {
		if (getState(pos) == BLANK)
			return;
		setState(pos, BLANK);
		if (getNumber(pos) > 0) {
			setNumber(pos, 0);
			pickedList.remove(pos);
			rePickUpAll();
		}
	}

	public void initBoard() {
		rePickUpAll();
	}

	/**
	 * 拾う
	 * @param pos
	 */
	public void pickUp(Address p) {
		pickedList.add(Address.address(p));
		setNumber(p, pickedList.size());
	}

	/**
	 * 戻す
	 */
	public void placeBack() {
		Address p = pickedList.get(pickedList.size()-1);
		setNumber(p, 0);
		pickedList.remove(pickedList.size()-1);
	}

	/**
	 * 拾えるか
	 * 拾えないのは以下の場合
	 * 石がない
	 * 石をすでに拾っている
	 * 前に拾った石と一直線上にない
	 * 前に拾った石との間に拾っていない石がある
	 * 前に拾った石とその前に拾った石とにはさまれている
	 * @param pos
	 * @return
	 */
	public boolean canPick(Address pos) {
		if (getState(pos) != STONE)
			return false;
		if (getNumber(pos) > 0)
			return false;
		if (pickedList.size() == 0)
			return true;
		Address prev = pickedList.get(pickedList.size() - 1);
		int direction = Address.getDirectionTo(prev, pos);
		if (direction < 0)
			return false;
		for (Address p = Address.address(prev); !p.equals(pos); p = p.nextCell(direction)) {
			if (isStone(p) && getNumber(p) == 0)
				return false;
		}
		if (pickedList.size() == 1)
			return true;
		Address prev2 = pickedList.get(pickedList.size() - 2);
		int direction2 = Address.getDirectionTo(prev, prev2);
		if (direction == direction2)
			return false;
		return true;
	}

	/**
	 * はじめから石を拾いなおす。
	 */
	public void rePickUpAll() {
		ArrayList<Address> copy = (ArrayList<Address>)(pickedList.clone());
		for (Address p : cellAddrs()) {
			if (getState(p) == Board.STONE)
				setNumber(p, 0);
		}
		pickedList.clear();
		for (int i = 0; i < copy.size(); i++) {
			Address p = copy.get(i);
			if (canPick(p))
				pickUp(p);
			else
				break;
		}
	}

	public int checkAnswerCode() {
		int nStone = 0;
		for (Address p : cellAddrs()) {
			if (getState(p) == Board.STONE) {
				nStone ++;
				if (getNumber(p) == 0)
					return 1;
			}
		}
		if (nStone == 0)
			return 2;
		return 0;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		else if (result == 2)
			return Messages.getString("goishi.AnswerCheckMessage2"); //$NON-NLS-1$
		else if (result == 1)
			return Messages.getString("goishi.AnswerCheckMessage1"); //$NON-NLS-1$
		return ""; //$NON-NLS-1$
	}

}
