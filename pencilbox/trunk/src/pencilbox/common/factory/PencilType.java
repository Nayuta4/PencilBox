/**
 * 
 */
package pencilbox.common.factory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pencilbox.common.core.Size;


/**
 * パズルの種類を表す定数を定義するクラス
 * 各定数には，付随するパッケージ名と日本語タイトルが定義されている
 */
public final class PencilType {

	public static final PencilType KAKURO      = new PencilType("kakuro",      Messages.getString("PencilType.kakuro"),      12); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType KURODOKO    = new PencilType("kurodoko",    Messages.getString("PencilType.kurodoko"),     9); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType GOISHI      = new PencilType("goishi",      Messages.getString("PencilType.goishi"),       8); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType SHIKAKU     = new PencilType("shikaku",     Messages.getString("PencilType.shikaku"),     10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType SUDOKU      = new PencilType("sudoku",      Messages.getString("PencilType.sudoku"),       9); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType SLALOM      = new PencilType("slalom",      Messages.getString("PencilType.slalom"),      10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType SLITHERLINK = new PencilType("slitherlink", Messages.getString("PencilType.slitherlink"), 11); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType TENTAISHO   = new PencilType("tentaisho",   Messages.getString("PencilType.tentaisho"),   10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType NUMBERLINK  = new PencilType("numberlink",  Messages.getString("PencilType.numberlink"),  10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType NURIKABE    = new PencilType("nurikabe",    Messages.getString("PencilType.nurikabe"),    10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType HAKYUKOKA   = new PencilType("hakyukoka",   Messages.getString("PencilType.hakyukoka"),   10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType HASHI       = new PencilType("hashi",       Messages.getString("PencilType.hashi"),        9); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType BIJUTSUKAN  = new PencilType("bijutsukan",  Messages.getString("PencilType.bijutsukan"),  10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType HITORI      = new PencilType("hitori",      Messages.getString("PencilType.hitori"),       8); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType FILLOMINO   = new PencilType("fillomino",   Messages.getString("PencilType.fillomino"),   10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType HEYAWAKE    = new PencilType("heyawake",    Messages.getString("PencilType.heyawake"),    10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType MASYU       = new PencilType("masyu",       Messages.getString("PencilType.masyu"),       10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType YAJILIN     = new PencilType("yajilin",     Messages.getString("PencilType.yajilin"),     10); //$NON-NLS-1$ //$NON-NLS-2$
	public static final PencilType LITS        = new PencilType("lits",        Messages.getString("PencilType.lits"),        10); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * パズル種類のリスト
	 */
	private static final PencilType[] pencilTypeList = new PencilType[] {
		KAKURO,
		KURODOKO,
		GOISHI,
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
	 * パッケージ名から対応するPencilTypeインスタンスを取得する
	 * @param name pacage名の文字列
	 * @return PencilTypeインスタンス
	 */
	public static PencilType getPencilType(String name) {
		for (int i = 0; i < pencilTypeList.length; i++) {
			if(pencilTypeList[i].getPencilName().equals(name))
				return pencilTypeList[i];
		}
//		例外を投げるべきか
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
