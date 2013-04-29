package pencilbox.tentaisho;

import pencilbox.common.core.Address;

/**
 * �u�V�̃V���[�v���p�A�h���X
 * ���̂Ƃ��낽���̖ڈ�
 */
public class StarAddress extends Address {

	public static StarAddress NOWHERE = new StarAddress(-1, -1);

	protected StarAddress(int r, int c) {
		super(r, c);
	}

	/**
	 * @param r
	 * @param c
	 */
	public static StarAddress address(int r, int c) {
		return new StarAddress(r, c);
	}

}
