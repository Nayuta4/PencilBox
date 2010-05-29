package pencilbox.kakuro;

import pencilbox.common.core.Address;

/**
 * カックロの縦または横の白マスつながりを表すクラス
 */
public class Word {
	
	private Address head;  // 数字マスの座標
	private int size;    // マス数
	private int sum;  // 合計の数字
	private int filledSize;   // 埋まっているマス数
	private int filledSum;     // 埋まっている数字の合計

	/**
	 * @return Returns the size.
	 */
	int getSize() {
		return size;
	}
	/**
	 * @return Returns the sum.
	 */
	int getSum() {
		return sum;
	}
	/**
	 * @return Returns the head.
	 */
	Address getHead() {
		return head;
	}
	/**
	 * @param r
	 * @param c
	 * @param count
	 * @param number
	 */
	public Word(int r, int c, int count, int number) {
		
		this.head = Address.address(r, c);
		this.size = count;
		this.sum = number; 
		
	}
	public void clear() {
		filledSize = 0;
		filledSum = 0;
	}

	public void changeNumber(int before, int after) {
		if (before == 0) {
			if (after > 0) {
				filledSize++;
				filledSum += after;
			} 
		} else if (before > 0 ) {
			if (after == 0) {
				filledSize--;
				filledSum -= before;
			} else if (after > 0) {
				filledSum = filledSum - before + after;
			}
		}
	}
	/**
	 * 完成状態を取得する。
	 * @return
	 * うまっていないマスがある : 0
	 * すべてのマスがうまっていて，合計は正しい : 1
	 * すべてのマスがうまっていて，合計は間違い : -1
	 * （うまっていないマスがあって，合計が正しくなりえない）は今は判定していない
	 */
	 public int getStatus() {
	 	int status = 0;
	 	if (filledSize < size) status = 0;
	 	else {
	 		if (filledSum == sum) status = 1;
	 		else status = -1;
	 	} 
	 	return status;
	 }
}
