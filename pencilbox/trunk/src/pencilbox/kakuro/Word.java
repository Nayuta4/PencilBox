package pencilbox.kakuro;

import pencilbox.common.core.Address;

/**
 * �J�b�N���̏c�܂��͉��̔��}�X�Ȃ����\���N���X
 */
public class Word {
	
	private Address head;  // �����}�X�̍��W
	private int size;    // �}�X��
	private int sum;  // ���v�̐���
	private int filledSize;   // ���܂��Ă���}�X��
	private int filledSum;     // ���܂��Ă��鐔���̍��v

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
	 * @param p
	 * @param count
	 * @param number
	 */
	public Word(Address p, int count, int number) {
		
		this.head = p;
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
	 * ������Ԃ��擾����B
	 * @return
	 * ���܂��Ă��Ȃ��}�X������ : 0
	 * ���ׂẴ}�X�����܂��Ă��āC���v�͐����� : 1
	 * ���ׂẴ}�X�����܂��Ă��āC���v�͊ԈႢ : -1
	 * �i���܂��Ă��Ȃ��}�X�������āC���v���������Ȃ肦�Ȃ��j�͍��͔��肵�Ă��Ȃ�
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
	/**
	 * ��萔�������p�B�a���m�肵�Ă���΂��̘a���C�m�肵�Ă��Ȃ����0��Ԃ��B
	 * @return
	 */
	public int extractSum() {
		 if (filledSize == size)
			  return this.filledSum;
		 else
			 return 0;
	 }
}
