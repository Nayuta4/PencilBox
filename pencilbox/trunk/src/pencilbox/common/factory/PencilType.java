/**
 * 
 */
package pencilbox.common.factory;

import pencilbox.common.core.Size;


/**
 * �p�Y���̎�ނ�\���萔���`����N���X
 * �e�萔�ɂ́C�t������p�b�P�[�W���Ɠ��{��^�C�g������`����Ă���
 */
public final class PencilType {
	
	/**
	 * �p�Y����ނ̃��X�g
	 */
	private static final PencilType[] pencilTypeList = new PencilType[] {
		new PencilType("kakuro",      "�J�b�N��",         12),
		new PencilType("kurodoko",    "���}�X�͂ǂ���",    9),
		new PencilType("shikaku",     "�l�p�ɐ؂�",       10),
		new PencilType("sudoku",      "����",              9),
		new PencilType("slitherlink", "�X���U�[�����N",   11),
		new PencilType("tentaisho",   "�V�̃V���[",       10),
		new PencilType("numberlink",  "�i���o�[�����N",   10),
		new PencilType("nurikabe",    "�ʂ肩��",         10),
		new PencilType("hakyukoka",   "�g�y����",         10),
		new PencilType("hashi",       "����������",        9),
		new PencilType("bijutsukan",  "���p��",           10),
		new PencilType("hitori",      "�ЂƂ�ɂ��Ă���",  8),
		new PencilType("fillomino",   "�t�B���I�~�m",     10),
		new PencilType("heyawake",    "�ւ�킯",         10),
		new PencilType("masyu",       "�܂���",           10),
		new PencilType("yajilin",     "���W����",         10),
	};
	/**
	 * ���݂���PencilType�̌����擾����
	 * @return PencilType�̌�
	 */
	public static int getNPencilType() {
		return pencilTypeList.length;
	}
	/**
	 * �ԍ��ɑΉ�����PencilType�C���X�^���X���擾����
	 * @param i �ԍ�
	 * @return PencilType�C���X�^���X
	 */
	public static PencilType getPencilType(int i) {
		if (i >= 0 && i < pencilTypeList.length)
			return pencilTypeList[i];
//		��O�𓊂���ׂ���
		return null;
	}
	/**
	 * �p�b�P�[�W������Ή�����PencilType�C���X�^���X���擾����
	 * @param name pacage���̕�����
	 * @return PencilType�C���X�^���X
	 */
	public static PencilType getPencilType(String name) {
		for (int i = 0; i < pencilTypeList.length; i++) {
			if(pencilTypeList[i].getPencilName().equals(name))
				return pencilTypeList[i];
		}
//		��O�𓊂���ׂ���
		return null;
	}

	/**
	 * 
	 */
	private PencilType() {
	}
	/**
	 * @param pencilName
	 * @param title
	 * @param defaultSize
	 */
	private PencilType(String pencilName, String title, int defaultSize) {
		this.pencilName = pencilName;
		this.title = title;
		this.defaultSize = defaultSize;
	}
	/**
	 * @return Returns the pencilName.
	 */
	public String getPencilName() {
		return pencilName;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return Returns the defaultSize.
	 */
	public Size getDefaultSize() {
		return new Size(defaultSize, defaultSize);
	}

	private String pencilName;
	private String title;
	private int defaultSize;
}
