/**
 * 
 */
package pencilbox.common.factory;

import pencilbox.common.core.Size;


/**
 * パズルの種類を表す定数を定義するクラス
 * 各定数には，付随するパッケージ名と日本語タイトルが定義されている
 */
public final class PencilType {
	
	/**
	 * パズル種類のリスト
	 */
	private static final PencilType[] pencilTypeList = new PencilType[] {
		new PencilType("kakuro",      "カックロ",         12),
		new PencilType("kurodoko",    "黒マスはどこだ",    9),
		new PencilType("shikaku",     "四角に切れ",       10),
		new PencilType("sudoku",      "数独",              9),
		new PencilType("slitherlink", "スリザーリンク",   11),
		new PencilType("tentaisho",   "天体ショー",       10),
		new PencilType("numberlink",  "ナンバーリンク",   10),
		new PencilType("nurikabe",    "ぬりかべ",         10),
		new PencilType("hakyukoka",   "波及効果",         10),
		new PencilType("hashi",       "橋をかけろ",        9),
		new PencilType("bijutsukan",  "美術館",           10),
		new PencilType("hitori",      "ひとりにしてくれ",  8),
		new PencilType("fillomino",   "フィルオミノ",     10),
		new PencilType("heyawake",    "へやわけ",         10),
		new PencilType("masyu",       "ましゅ",           10),
		new PencilType("yajilin",     "ヤジリン",         10),
	};
	/**
	 * 存在するPencilTypeの個数を取得する
	 * @return PencilTypeの個数
	 */
	public static int getNPencilType() {
		return pencilTypeList.length;
	}
	/**
	 * 番号に対応するPencilTypeインスタンスを取得する
	 * @param i 番号
	 * @return PencilTypeインスタンス
	 */
	public static PencilType getPencilType(int i) {
		if (i >= 0 && i < pencilTypeList.length)
			return pencilTypeList[i];
//		例外を投げるべきか
		return null;
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
