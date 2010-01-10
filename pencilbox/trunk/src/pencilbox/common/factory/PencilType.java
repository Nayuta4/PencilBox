/**
 * 
 */
package pencilbox.common.factory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pencilbox.common.core.Size;


/**
 * �p�Y���̎�ނ�\���萔���`����N���X
 * �e�萔�ɂ́C�t������p�b�P�[�W���Ɠ��{��^�C�g������`����Ă���
 */
public final class PencilType {
	
	public static final PencilType KAKURO = new PencilType("kakuro",      "�J�b�N��",         12);
	public static final PencilType KURODOKO = new PencilType("kurodoko",    "���}�X�͂ǂ���",    9);
	public static final PencilType SHIKAKU = new PencilType("shikaku",     "�l�p�ɐ؂�",       10);
	public static final PencilType SUDOKU = new PencilType("sudoku",      "����",              9);
	public static final PencilType SLALOM = new PencilType("slalom", "�X�����[��",   10);
	public static final PencilType SLITHERLINK = new PencilType("slitherlink", "�X���U�[�����N",   11);
	public static final PencilType TENTAISHO = new PencilType("tentaisho",   "�V�̃V���[",       10);
	public static final PencilType NUMBERLINK = new PencilType("numberlink",  "�i���o�[�����N",   10);
	public static final PencilType NURIKABE = new PencilType("nurikabe",    "�ʂ肩��",         10);
	public static final PencilType HAKYUKOKA = new PencilType("hakyukoka",   "�g�y����",         10);
	public static final PencilType HASHI = new PencilType("hashi",       "����������",        9);
	public static final PencilType BIJUTSUKAN = new PencilType("bijutsukan",  "���p��",           10);
	public static final PencilType HITORI = new PencilType("hitori",      "�ЂƂ�ɂ��Ă���",  8);
	public static final PencilType FILLOMINO = new PencilType("fillomino",   "�t�B���I�~�m",     10);
	public static final PencilType HEYAWAKE = new PencilType("heyawake",    "�ւ�킯",         10);
	public static final PencilType MASYU = new PencilType("masyu",       "�܂���",           10);
	public static final PencilType YAJILIN = new PencilType("yajilin",     "���W����",         10);
	public static final PencilType LITS = new PencilType("lits",     "�k�h�s�r",         10);
	
	/**
	 * �p�Y����ނ̃��X�g
	 */
	private static final PencilType[] pencilTypeList = new PencilType[] {
		KAKURO,
		KURODOKO,
		SHIKAKU,
		SUDOKU,
		SLALOM,
		SLITHERLINK,
		TENTAISHO,
		NUMBERLINK,
		NURIKABE,
		HAKYUKOKA,
		HASHI,
		BIJUTSUKAN,
		HITORI,
		FILLOMINO,
		HEYAWAKE,
		MASYU,
		YAJILIN,
		LITS,
	};

	public static final List<PencilType> getPencilTypeList() {
		return Collections.unmodifiableList(Arrays.asList(pencilTypeList));
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
