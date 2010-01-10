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
	
	public static final PencilType KAKURO = new PencilType("kakuro",      "カックロ",         12);
	public static final PencilType KURODOKO = new PencilType("kurodoko",    "黒マスはどこだ",    9);
	public static final PencilType SHIKAKU = new PencilType("shikaku",     "四角に切れ",       10);
	public static final PencilType SUDOKU = new PencilType("sudoku",      "数独",              9);
	public static final PencilType SLALOM = new PencilType("slalom", "スラローム",   10);
	public static final PencilType SLITHERLINK = new PencilType("slitherlink", "スリザーリンク",   11);
	public static final PencilType TENTAISHO = new PencilType("tentaisho",   "天体ショー",       10);
	public static final PencilType NUMBERLINK = new PencilType("numberlink",  "ナンバーリンク",   10);
	public static final PencilType NURIKABE = new PencilType("nurikabe",    "ぬりかべ",         10);
	public static final PencilType HAKYUKOKA = new PencilType("hakyukoka",   "波及効果",         10);
	public static final PencilType HASHI = new PencilType("hashi",       "橋をかけろ",        9);
	public static final PencilType BIJUTSUKAN = new PencilType("bijutsukan",  "美術館",           10);
	public static final PencilType HITORI = new PencilType("hitori",      "ひとりにしてくれ",  8);
	public static final PencilType FILLOMINO = new PencilType("fillomino",   "フィルオミノ",     10);
	public static final PencilType HEYAWAKE = new PencilType("heyawake",    "へやわけ",         10);
	public static final PencilType MASYU = new PencilType("masyu",       "ましゅ",           10);
	public static final PencilType YAJILIN = new PencilType("yajilin",     "ヤジリン",         10);
	public static final PencilType LITS = new PencilType("lits",     "ＬＩＴＳ",         10);
	
	/**
	 * パズル種類のリスト
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
