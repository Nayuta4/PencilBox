package pencilbox.tentaisho;

import pencilbox.common.core.Address;


/**
 * �u�V�̃V���[�v�̈�N���X
 */
public class Area extends pencilbox.common.core.AreaBase {
	/**
	 * �̈�Ɋ܂܂�鐯�̎��
	 * �Ȃ� 0;  ���� 1; ���� 2; ������ -1:
	 */
	private int starType;
	private StarAddress starPos;	// �̈�Ɋ܂܂�鐯�̍��W 1�݂̂̂Ƃ��݈̂Ӗ�������

	/**
	 * @return Returns the starColor.
	 */
	int getStarType() {
		return starType;
	}

	/**
	 * @param starColor The starColor to set.
	 */
	void setStarType(int starColor) {
		this.starType = starColor;
	}

	/**
	 * @return Returns the starPos.
	 */
	Address getStarPos() {
		return starPos;
	}

	/**
	 * @param starPos The starPos to set.
	 */
	void setStarPos(StarAddress starPos) {
		this.starPos = starPos;
	}

	public String toString() {
		return "����:" + starType + " �����W:" + starPos.toString() +"\n�̈�"+ super.toString();	
	}

	/**
	 * �̈悪�����ЂƂ̂݊܂݁C�����̒��S�_�Ɉʒu���鐯�ɑ΂��ē_�Ώ̂��ǂ����𒲂ׂ�
	 * @return �_�Ώ̂Ȃ�� <tt>true</tt>
	 */
	public boolean isPointSymmetry() {
		if (starPos.isNowhere()) return false;
		Address posb;
		for (Address pos : this) {
			posb = getPointSymmericAddress(pos, starPos);
			if (!contains(posb)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * �̈悪�^����ꂽ���S�_�ɑ΂��ē_�Ώ̂��ǂ���
	 */
//	private boolean isPointSymmetry(StarAddress center) {
//		Address posb;
//		for (Address pos : this) {
//			posb = getPointSymmericAddress(pos, center);
//			if (!contains(posb)) {
//				return false;
//			}
//		}
//		return true;
//	}
	/**
	 * position ���� center �ɑ΂��ē_�Ώ̂� Address �����߂�
	 * @param position ���W
	 * @param center �_�Ώ̒��S�ƂȂ���W
	 * @return cnter�ɑ΂���position�Ɠ_�Ώ̂ȍ��W
	 */
	public Address getPointSymmericAddress(Address position, StarAddress center) {
		return Address.address(center.r()-position.r(), center.c()-position.c());
	}

	/**
	 * 	�̈�̒��S�}�X��Ԃ�
	 * @return �̈�̒��S�}�X
	 */
	public Address getCenterCell() {
		return Address.address(starPos.r()/2, starPos.c()/2);
	}

}

/*
 * �̈�̌`��ɂ��ꍇ�킯
 *  �����̐����܂�
 *  �ЂƂ������܂܂Ȃ�
 *  �ЂƂ��������܂�
 * 		�_�Ώ̂�
 * 		�_�Ώ̂łȂ�
 */
