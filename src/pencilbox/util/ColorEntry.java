package pencilbox.util;

import java.awt.Color;

/**
 * @author joji
 */
class ColorEntry {
	String name;
	int rgb;
	Color color;
	Color revColor;
	boolean use;
	int[] hsb = new int[3];
	float brightness;
	static int LEVEL = 8; // max < 100
	static int B_LEVEL = 4; // dark if B <= B_LEVEL
	ColorEntry(int value, String name, boolean use) {
		this.name = name;
		rgb = value;
		this.use = use;
	}
	void complete() {
		color = new Color(rgb);
		int rev = rgb ^ 0xffffff;
		revColor = new Color(rev);
		float[] tmp = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getBlue(), color.getGreen(), tmp);
		brightness = tmp[2];
		hsb[0] = (int) (tmp[0] * LEVEL);
		if (hsb[0] == LEVEL)
			hsb[0] = LEVEL - 1;
		hsb[1] = (int) (tmp[1] * LEVEL);
		if (hsb[1] == LEVEL)
			hsb[1] = LEVEL - 1;
		hsb[2] = (int) (tmp[2] * LEVEL);
		if (hsb[2] == LEVEL)
			hsb[2] = LEVEL - 1;
	}
	boolean isReserved() {
		for (int i = 0; i < reserved.length; i++) {
			if (reserved[i] == rgb)
				return true;
		}
		return false;
	}
	static void complete(ColorEntry[] list) {
		for (int i = 0; i < list.length; i++) {
			list[i].complete();
		}
	}
	static ColorEntry[] namedColor;
	static ColorEntry[] rgbColor;
//	static ColorEntry[] useColor;
	static ColorEntry[] getUseAndReservedColor(ColorEntry[] list) {
		int n = 0;
		for (int i = 0; i < list.length; i++) {
			if (list[i].use || list[i].isReserved())
				n++;
		}
		ColorEntry[] useList = new ColorEntry[n];
		n = 0;
		for (int i = 0; i < list.length; i++) {
			if (list[i].use || list[i].isReserved())
				useList[n++] = list[i];
		}
		return useList;
	}
	static ColorEntry[] getUseColor(ColorEntry[] list) {
		int n = 0;
		for (int i = 0; i < list.length; i++) {
			if (list[i].use)
				n++;
		}
		ColorEntry[] useList = new ColorEntry[n];
		n = 0;
		for (int i = 0; i < list.length; i++) {
			if (list[i].use)
				useList[n++] = list[i];
		}
		return useList;
	}
	static int[] reserved =
		new int[] {
			0x000000,
			0x0000ff,
			0x00ff00,
			0x00ffff,
			0xff0000,
			0xff00ff,
			0xffff00,
			0xffffff,
			};
	static ColorEntry[] getNamedColor() {
		if (namedColor == null) {
			namedColor =
				new ColorEntry[] {
					new ColorEntry(0xf0f8ff, "aliceblue", false),
					new ColorEntry(0xfaebd7, "antiquewhite", false),
					new ColorEntry(0x7fffd4, "aquamarine", false),
					new ColorEntry(0xf0ffff, "azure", false),
					new ColorEntry(0xf5f5dc, "beige", false),
					new ColorEntry(0xffe4c4, "bisque", false),
					new ColorEntry(0x000000, "black", false),
					new ColorEntry(0xffebcd, "blanchedalmond", false),
					new ColorEntry(0x0000ff, "blue", false),
					new ColorEntry(0x8a2be2, "blueviolet", false),
					new ColorEntry(0xa52a2a, "brown", true),
					new ColorEntry(0xdeb887, "burlywood", false),
					new ColorEntry(0x5f9ea0, "cadetblue", false),
					new ColorEntry(0x7fff00, "chartreuse", false),
					new ColorEntry(0xd2691e, "chocolate", true),
					new ColorEntry(0xff7f50, "coral", false),
					new ColorEntry(0x6495ed, "cornflowerblue", false),
					new ColorEntry(0xfff8dc, "cornsilk", false),
					new ColorEntry(0xdc143c, "crimson", true),
					new ColorEntry(0x00ffff, "cyan", false),
					new ColorEntry(0x00008b, "darkblue", false),
					new ColorEntry(0x008b8b, "darkcyan", true),
					new ColorEntry(0xb8860b, "darkgoldenrod", true),
					new ColorEntry(0xa9a9a9, "darkgray", false),
					new ColorEntry(0x006400, "darkgreen", false),
					new ColorEntry(0xbdb76b, "darkkhaki", false),
					new ColorEntry(0x8b008b, "darkmagenda", false),
					new ColorEntry(0x556b2f, "darkolivegreen", false),
					new ColorEntry(0xff8c00, "darkorange", true),
					new ColorEntry(0x9932cc, "darkorkid", false),
					new ColorEntry(0x8b0000, "darkred", true),
					new ColorEntry(0xe9967a, "darksalmon", false),
					new ColorEntry(0x8fbc8f, "darkseagreen", true),
					new ColorEntry(0x483d8b, "darkslateblue", false),
					new ColorEntry(0x2f4f4f, "darkslategray", false),
					new ColorEntry(0x00ced1, "darktorquoise", false),
					new ColorEntry(0x9400d3, "darkviolet", true),
					new ColorEntry(0xff1493, "deeppink", false),
					new ColorEntry(0x00bfff, "deepskyblue", true),
					new ColorEntry(0x696969, "dimgray", false),
					new ColorEntry(0x1e90ff, "dodgerblue", false),
					new ColorEntry(0xb22222, "fireblick", false),
					new ColorEntry(0xfffaf0, "floralwhite", false),
					new ColorEntry(0x228b22, "forestgreen", false),
					new ColorEntry(0xdcdcdc, "gainsboro", false),
					new ColorEntry(0xf8f8ff, "ghostwhite", false),
					new ColorEntry(0xffd700, "gold", false),
					new ColorEntry(0xdaa520, "goldenrod", true),
					new ColorEntry(0xbebebe, "gray", false),
					new ColorEntry(0x008000, "green", true),
					new ColorEntry(0xadff2f, "greenyellow", true),
					new ColorEntry(0xf0fff0, "heneydew", false),
					new ColorEntry(0xff69b4, "hotpink", false),
					new ColorEntry(0xcd5c5c, "indianred", false),
					new ColorEntry(0x4b0082, "indigo", true),
					new ColorEntry(0xfffff0, "ivory", false),
					new ColorEntry(0xf0d58c, "khaki", true),
					new ColorEntry(0xe6e6fa, "lavender", false),
					new ColorEntry(0xfff0f5, "lavenderblush", false),
					new ColorEntry(0x7cfc00, "lawngreen", false),
					new ColorEntry(0xfffacd, "lemonchiffon", false),
					new ColorEntry(0xadd8e6, "lightblue", true),
					new ColorEntry(0xf08080, "lightcoral", false),
					new ColorEntry(0xe0ffff, "lightcyan", false),
					new ColorEntry(0xfafad2, "lightgoldenyellow", false),
					new ColorEntry(0x90ee90, "lightgreen", true),
					new ColorEntry(0xd3d3d3, "lightgray", false),
					new ColorEntry(0xffb6c1, "lightpink", false),
					new ColorEntry(0xffa07a, "lightsalmon", true),
					new ColorEntry(0x20b2aa, "lightseagreen", false),
					new ColorEntry(0x87cefa, "lightskyblue", false),
					new ColorEntry(0x778899, "lightslategray", false),
					new ColorEntry(0xb0c4de, "lightsteelblue", true),
					new ColorEntry(0xffffe0, "lightyellow", false),
					new ColorEntry(0x00ff00, "lime", true),
					new ColorEntry(0x32cd32, "limegreen", false),
					new ColorEntry(0xfaf0e6, "linen", false),
					new ColorEntry(0xff00ff, "magenta", true),
					new ColorEntry(0x800000, "maroon", false),
					new ColorEntry(0x66cdaa, "mediumaquamarine", false),
					new ColorEntry(0x0000cd, "mediumblue", false),
					new ColorEntry(0xba55d3, "mediumorchid", false),
					new ColorEntry(0x9370db, "mediumpurple", true),
					new ColorEntry(0x3cb371, "mediumseagreen", true),
					new ColorEntry(0x7b68ee, "mediumslateblue", false),
					new ColorEntry(0x00fa9a, "mediumspringgreen", false),
					new ColorEntry(0x48d1cc, "mediumturquoise", false),
					new ColorEntry(0xc71585, "mediumvioletred", true),
					new ColorEntry(0x191970, "midnightblue", false),
					new ColorEntry(0xf5fffa, "mintcream", false),
					new ColorEntry(0xffe4e1, "mistryrose", false),
					new ColorEntry(0xffe4b5, "moccasin", false),
					new ColorEntry(0xffdead, "navajowhite", false),
					new ColorEntry(0x000080, "navy", false),
					new ColorEntry(0xfdf5e6, "oldlace", false),
					new ColorEntry(0x808000, "olive", false),
					new ColorEntry(0x6b8e23, "olivedrab", false),
					new ColorEntry(0xffa500, "orange", true),
					new ColorEntry(0xff4500, "orangered", false),
					new ColorEntry(0xda70d6, "orchid", false),
					new ColorEntry(0xeee8aa, "palegoldenrod", false),
					new ColorEntry(0x98fb98, "palegreen", false),
					new ColorEntry(0xafeeee, "paleturquoise", false),
					new ColorEntry(0xdb7093, "palevioletred", false),
					new ColorEntry(0xffefd5, "papayawhip", false),
					new ColorEntry(0xffdab9, "peachpuff", false),
					new ColorEntry(0xcd853f, "peru", true),
					new ColorEntry(0xffc0cb, "pink", true),
					new ColorEntry(0xdda0dd, "plumn", false),
					new ColorEntry(0xb0e0e6, "powderblue", false),
					new ColorEntry(0x800080, "purple", true),
					new ColorEntry(0xff0000, "red", false),
					new ColorEntry(0xbc8f8f, "rosybrown", false),
					new ColorEntry(0x4169e1, "royalblue", false),
					new ColorEntry(0x8b4513, "saddlebrown", false),
					new ColorEntry(0xfa8072, "salmon", true),
					new ColorEntry(0xf4a460, "sandybrown", false),
					new ColorEntry(0x2e8b57, "seagreen", false),
					new ColorEntry(0xfff5ee, "seashell", false),
					new ColorEntry(0xa0522d, "sienna", false),
					new ColorEntry(0xc0c0c0, "silver", false),
					new ColorEntry(0x87ceeb, "skyblue", false),
					new ColorEntry(0x6a5acd, "slateblue", false),
					new ColorEntry(0x708090, "slategray", false),
					new ColorEntry(0xfffafa, "snow", false),
					new ColorEntry(0x00ff7f, "springgreen", false),
					new ColorEntry(0x4682b4, "steelblue", true),
					new ColorEntry(0xd2b48c, "tan", true),
					new ColorEntry(0x008080, "teal", false),
					new ColorEntry(0xd8bfd8, "thistle", false),
					new ColorEntry(0xff6347, "tomato", false),
					new ColorEntry(0x40e0d0, "turquoise", false),
					new ColorEntry(0xee82ee, "violet", true),
					new ColorEntry(0xf5deb3, "wheat", false),
					new ColorEntry(0xffffff, "white", false),
					new ColorEntry(0xf5f5f5, "whitesmoke", false),
					new ColorEntry(0xffff00, "yellow", true),
					new ColorEntry(0x9acd32, "yellowgreen", false),
				//			new NamedColor(0x000000, "", true),
			};
		}
		return namedColor;
	}
	static ColorEntry[] getRgbColor() {
		if (rgbColor == null) {
			rgbColor =
				new ColorEntry[] {
					new ColorEntry(0x000000, "black", false),
					new ColorEntry(0x000033, "001", false),
					new ColorEntry(0x000066, "002", false),
					new ColorEntry(0x000099, "003", false),
					new ColorEntry(0x0000cc, "004", false),
					new ColorEntry(0x0000ff, "blue", false),
					new ColorEntry(0x003300, "010", false),
					new ColorEntry(0x003333, "011", false),
					new ColorEntry(0x003366, "012", false),
					new ColorEntry(0x003399, "013", false),
					new ColorEntry(0x0033cc, "014", false),
					new ColorEntry(0x0033ff, "015", false),
					new ColorEntry(0x006600, "020", false),
					new ColorEntry(0x006633, "021", false),
					new ColorEntry(0x006666, "022", false),
					new ColorEntry(0x006699, "023", false),
					new ColorEntry(0x0066cc, "024", false),
					new ColorEntry(0x0066ff, "025", false),
					new ColorEntry(0x009900, "030", false),
					new ColorEntry(0x009933, "031", false),
					new ColorEntry(0x009966, "032", false),
					new ColorEntry(0x009999, "033", true),
					new ColorEntry(0x0099cc, "034", false),
					new ColorEntry(0x0099ff, "035", false),
					new ColorEntry(0x00cc00, "040", false),
					new ColorEntry(0x00cc33, "041", false),
					new ColorEntry(0x00cc66, "042", false),
					new ColorEntry(0x00cc99, "043", false),
					new ColorEntry(0x00cccc, "044", false),
					new ColorEntry(0x00ccff, "045", false),
					new ColorEntry(0x00ff00, "lime", true),
					new ColorEntry(0x00ff33, "051", false),
					new ColorEntry(0x00ff66, "052", false),
					new ColorEntry(0x00ff99, "053", false),
					new ColorEntry(0x00ffcc, "054", false),
					new ColorEntry(0x00ffff, "cyan", false),
					new ColorEntry(0x330000, "100", false),
					new ColorEntry(0x330033, "101", false),
					new ColorEntry(0x330066, "102", false),
					new ColorEntry(0x330099, "103", true),
					new ColorEntry(0x3300cc, "104", false),
					new ColorEntry(0x3300ff, "105", false),
					new ColorEntry(0x333300, "110", false),
					new ColorEntry(0x333333, "111", false),
					new ColorEntry(0x333366, "112", false),
					new ColorEntry(0x333399, "113", false),
					new ColorEntry(0x3333cc, "114", false),
					new ColorEntry(0x3333ff, "115", false),
					new ColorEntry(0x336600, "120", false),
					new ColorEntry(0x336633, "121", false),
					new ColorEntry(0x336666, "122", false),
					new ColorEntry(0x336699, "123", false),
					new ColorEntry(0x3366cc, "124", false),
					new ColorEntry(0x3366ff, "125", false),
					new ColorEntry(0x339900, "130", false),
					new ColorEntry(0x339933, "131", false),
					new ColorEntry(0x339966, "132", false),
					new ColorEntry(0x339999, "133", false),
					new ColorEntry(0x3399cc, "134", false),
					new ColorEntry(0x3399ff, "135", false),
					new ColorEntry(0x33cc00, "140", false),
					new ColorEntry(0x33cc33, "141", false),
					new ColorEntry(0x33cc66, "142", false),
					new ColorEntry(0x33cc99, "143", false),
					new ColorEntry(0x33cccc, "144", false),
					new ColorEntry(0x33ccff, "145", false),
					new ColorEntry(0x33ff00, "150", false),
					new ColorEntry(0x33ff33, "151", false),
					new ColorEntry(0x33ff66, "152", false),
					new ColorEntry(0x33ff99, "153", false),
					new ColorEntry(0x33ffcc, "154", false),
					new ColorEntry(0x33ffff, "155", false),
					new ColorEntry(0x660000, "200", false),
					new ColorEntry(0x660033, "201", false),
					new ColorEntry(0x660066, "202", false),
					new ColorEntry(0x660099, "203", false),
					new ColorEntry(0x6600cc, "204", false),
					new ColorEntry(0x6600ff, "205", false),
					new ColorEntry(0x663300, "210", false),
					new ColorEntry(0x663333, "211", false),
					new ColorEntry(0x663366, "212", false),
					new ColorEntry(0x663399, "213", false),
					new ColorEntry(0x6633cc, "214", false),
					new ColorEntry(0x6633ff, "215", false),
					new ColorEntry(0x666600, "220", false),
					new ColorEntry(0x666633, "221", false),
					new ColorEntry(0x666666, "222", false),
					new ColorEntry(0x666699, "223", false),
					new ColorEntry(0x6666cc, "224", false),
					new ColorEntry(0x6666ff, "225", false),
					new ColorEntry(0x669900, "230", true),
					new ColorEntry(0x669933, "231", false),
					new ColorEntry(0x669966, "232", true),
					new ColorEntry(0x669999, "233", false),
					new ColorEntry(0x6699cc, "234", true),
					new ColorEntry(0x6699ff, "235", true),
					new ColorEntry(0x66cc00, "240", true),
					new ColorEntry(0x66cc33, "241", false),
					new ColorEntry(0x66cc66, "242", false),
					new ColorEntry(0x66cc99, "243", true),
					new ColorEntry(0x66cccc, "244", false),
					new ColorEntry(0x66ccff, "245", false),
					new ColorEntry(0x66ff00, "250", false),
					new ColorEntry(0x66ff33, "251", false),
					new ColorEntry(0x66ff66, "252", true),
					new ColorEntry(0x66ff99, "253", false),
					new ColorEntry(0x66ffcc, "254", false),
					new ColorEntry(0x66ffff, "255", false),
					new ColorEntry(0x990000, "300", false),
					new ColorEntry(0x990033, "301", false),
					new ColorEntry(0x990066, "302", false),
					new ColorEntry(0x990099, "303", true),
					new ColorEntry(0x9900cc, "304", false),
					new ColorEntry(0x9900ff, "305", false),
					new ColorEntry(0x993300, "310", true),
					new ColorEntry(0x993333, "311", false),
					new ColorEntry(0x993366, "312", true),
					new ColorEntry(0x993399, "313", false),
					new ColorEntry(0x9933cc, "314", false),
					new ColorEntry(0x9933ff, "315", false),
					new ColorEntry(0x996600, "320", false),
					new ColorEntry(0x996633, "321", true),
					new ColorEntry(0x996666, "322", false),
					new ColorEntry(0x996699, "323", false),
					new ColorEntry(0x9966cc, "324", false),
					new ColorEntry(0x9966ff, "325", true),
					new ColorEntry(0x999900, "330", false),
					new ColorEntry(0x999933, "331", false),
					new ColorEntry(0x999966, "332", false),
					new ColorEntry(0x999999, "333", false),
					new ColorEntry(0x9999cc, "334", false),
					new ColorEntry(0x9999ff, "335", true),
					new ColorEntry(0x99cc00, "340", false),
					new ColorEntry(0x99cc33, "341", false),
					new ColorEntry(0x99cc66, "342", false),
					new ColorEntry(0x99cc99, "343", true),
					new ColorEntry(0x99cccc, "344", false),
					new ColorEntry(0x99ccff, "345", true),
					new ColorEntry(0x99ff00, "350", false),
					new ColorEntry(0x99ff33, "351", false),
					new ColorEntry(0x99ff66, "352", false),
					new ColorEntry(0x99ff99, "353", true),
					new ColorEntry(0x99ffcc, "354", false),
					new ColorEntry(0x99ffff, "355", false),
					new ColorEntry(0xcc0000, "400", false),
					new ColorEntry(0xcc0066, "402", true),
					new ColorEntry(0xcc0099, "403", false),
					new ColorEntry(0xcc00cc, "404", false),
					new ColorEntry(0xcc00ff, "405", true),
					new ColorEntry(0xcc3300, "410", true),
					new ColorEntry(0xcc3333, "411", false),
					new ColorEntry(0xcc3366, "412", false),
					new ColorEntry(0xcc3399, "413", false),
					new ColorEntry(0xcc33cc, "414", false),
					new ColorEntry(0xcc33ff, "415", false),
					new ColorEntry(0xcc6600, "420", false),
					new ColorEntry(0xcc6633, "421", false),
					new ColorEntry(0xcc6666, "422", true),
					new ColorEntry(0xcc6699, "423", false),
					new ColorEntry(0xcc66cc, "424", false),
					new ColorEntry(0xcc66ff, "425", false),
					new ColorEntry(0xcc9900, "430", true),
					new ColorEntry(0xcc9933, "431", false),
					new ColorEntry(0xcc9966, "432", false),
					new ColorEntry(0xcc9999, "433", true),
					new ColorEntry(0xcc99cc, "434", false),
					new ColorEntry(0xcc99ff, "435", false),
					new ColorEntry(0xcccc00, "440", true),
					new ColorEntry(0xcccc33, "441", false),
					new ColorEntry(0xcccc66, "442", true),
					new ColorEntry(0xcccc99, "443", false),
					new ColorEntry(0xcccccc, "444", false),
					new ColorEntry(0xccccff, "445", false),
					new ColorEntry(0xccff00, "450", true),
					new ColorEntry(0xccff33, "451", false),
					new ColorEntry(0xccff66, "452", false),
					new ColorEntry(0xccff99, "453", true),
					new ColorEntry(0xccffcc, "454", false),
					new ColorEntry(0xccffff, "455", false),
					new ColorEntry(0xff0000, "red", false),
					new ColorEntry(0xff0033, "501", false),
					new ColorEntry(0xff0066, "502", false),
					new ColorEntry(0xff0099, "503", false),
					new ColorEntry(0xff00cc, "504", false),
					new ColorEntry(0xff00ff, "magenda", false),
					new ColorEntry(0xff3300, "510", false),
					new ColorEntry(0xff3333, "511", false),
					new ColorEntry(0xff3366, "512", true),
					new ColorEntry(0xff3399, "513", false),
					new ColorEntry(0xff33cc, "514", false),
					new ColorEntry(0xff33ff, "515", false),
					new ColorEntry(0xff6600, "520", false),
					new ColorEntry(0xff6633, "521", true),
					new ColorEntry(0xff6666, "522", false),
					new ColorEntry(0xff6699, "523", false),
					new ColorEntry(0xff66cc, "524", false),
					new ColorEntry(0xff66ff, "525", false),
					new ColorEntry(0xff9900, "530", true),
					new ColorEntry(0xff9933, "531", false),
					new ColorEntry(0xff9966, "532", true),
					new ColorEntry(0xff9999, "533", true),
					new ColorEntry(0xff99cc, "534", true),
					new ColorEntry(0xff99ff, "535", false),
					new ColorEntry(0xffcc00, "540", true),
					new ColorEntry(0xffcc33, "541", false),
					new ColorEntry(0xffcc66, "542", false),
					new ColorEntry(0xffcc99, "543", false),
					new ColorEntry(0xffcccc, "544", true),
					new ColorEntry(0xffccff, "545", false),
					new ColorEntry(0xffff00, "yellow", true),
					new ColorEntry(0xffff33, "551", false),
					new ColorEntry(0xffff66, "552", false),
					new ColorEntry(0xffff99, "553", false),
					new ColorEntry(0xffffcc, "554", false),
					new ColorEntry(0xffffff, "white", false),
					};
		}
		return rgbColor;
	}
}