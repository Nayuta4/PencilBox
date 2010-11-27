/**
 * 
 */
package pencilbox.common.gui;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;

/**
 * アプリケーションの設定の保存，読込，複写を行うためのクラス
 */
public class PreferencesCopierBase {
	
	private String pencilName;
	private Properties properties = new Properties();
	
	/**
	 * PreferencesCopier のインスタンスを作成する。
	 * @param pencilType 種類
	 * @return 作成した PreferencesCopier インスタンス
	 */
	public static PreferencesCopierBase createInstance(PencilType pencilType) {
		PreferencesCopierBase copier;
		try {
			copier = (PreferencesCopierBase) ClassUtil.createInstance(pencilType, ClassUtil.PREFERENCES_COPIER_CLASS);
		} catch (PencilBoxClassException ex) {
			copier = new PreferencesCopierBase();
		}
		copier.pencilName = pencilType.getPencilName() + '.';
		return copier;
	}
	
	/**
	 * 現在の properties に格納されている設定をアプリケーションに適用する。
	 * @param command
	 */
	public void applyCurrentPreferences1(MenuCommand command) {
		PanelBase panel = command.getPanelBase();
		EventHandlerManager handler = command.getEventHandlerManager();
		handler.setEditMode(getIntProperty(PreferenceKey.EDIT_MODE));
		handler.setSymmetricPlacementMode(getBooleanProperty(PreferenceKey.SYMMETRIC_PLACEMENT_MODE));
		handler.setImmediateAnswerCheckMode(getBooleanProperty(PreferenceKey.IMMEDIATE_ANSWER_CHECK_MODE));
		panel.setDisplaySize(getIntProperty(PreferenceKey.CELL_SIZE));
		panel.changeIndexMode(getBooleanProperty(PreferenceKey.INDEX_MODE));
		panel.setGridStyle(getIntProperty(PreferenceKey.GRID_STYLE));
		panel.setMarkStyle(getIntProperty(PreferenceKey.MARK_STYLE));
		panel.setCursorMode(getBooleanProperty(PreferenceKey.CURSOR_MODE));
		panel.setBackgroundColor(getColorProperty(PreferenceKey.BACKGROUND_COLOR));
		panel.setGridColor(getColorProperty(PreferenceKey.GRID_COLOR));
	}
	
	/**
	 * 現在のアプリケーションの設定を取得して properties に格納する。 
	 * @param command
	 */
	public void acquireCurrentPreferences1(MenuCommand command) {
		PanelBase panel = command.getPanelBase();
		EventHandlerManager handler = command.getEventHandlerManager();
		setIntProperty(PreferenceKey.EDIT_MODE, panel.getEditMode());
		setBooleanProperty(PreferenceKey.SYMMETRIC_PLACEMENT_MODE, handler.isSymmetricPlacementMode());
		setBooleanProperty(PreferenceKey.IMMEDIATE_ANSWER_CHECK_MODE, handler.isImmediateAnswerCheckMode());
		setIntProperty(PreferenceKey.CELL_SIZE, panel.getCellSize());
		setBooleanProperty(PreferenceKey.INDEX_MODE, panel.isIndexMode());
		setIntProperty(PreferenceKey.GRID_STYLE, panel.getGridStyle());
		setIntProperty(PreferenceKey.MARK_STYLE, panel.getMarkStyle());
		setBooleanProperty(PreferenceKey.CURSOR_MODE, panel.isCursorMode());
		setColorProperty(PreferenceKey.BACKGROUND_COLOR, panel.getBackgroundColor());
		setColorProperty(PreferenceKey.GRID_COLOR, panel.getGridColor());
	}
	
	/**
	 * メニュー選択をコピーする
	 * @param src コピー元フレームの MenuCommand インスタンス
	 * @param dst コピー先フレームの MenuCommand インスタンス
	 */
	public void copyPreferences(MenuCommand src, MenuCommand dst) {
		acquireCurrentPreferences(src);
		applyCurrentPreferences(dst);
	}

	/**
	 * 設定をファイルから読み込んでアプリケーションに適用する。
	 * ファイルにない項目は現在の設定のまま。
	 * @param command
	 * @param file 読み込むファイル
	 */
	public void loadPreferences(MenuCommand command, File file) {
		acquireCurrentPreferences(command);
		try {
			properties.load(new FileInputStream(file));
//			properties.list(System.out);
			applyCurrentPreferences(command);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * アプリケーションの設定をファイルに保存する。
	 * 保存するファイルがすでに存在する場合は，
	 * 同じ種類の項目については上書きし，それ以外の項目については元のファイルの値を保持する。
	 * @param command
	 * @param file 保存するファイル
	 */
	public void storePreferences(MenuCommand command, File file) {
		try {
			if (file.canRead()) {
				properties.load(new FileInputStream(file));
			}
			acquireCurrentPreferences(command);
//			properties.list(System.out);
//			properties.store(new FileOutputStream(file), "PencilBox preferences");
			// キーで整列するために，Properties を　TreeMap　に変換してから保存する。
			TreeMapA map = new TreeMapA(properties);
			map.store(new FileOutputStream(file), "PencilBox preferences");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String getStringProperty(PreferenceKey key) {
		return properties.getProperty(pencilName + key.getKey());
	}
	
	protected boolean getBooleanProperty(PreferenceKey key) {
		return Integer.parseInt(properties.getProperty(pencilName + key.getKey())) > 0;
//		return Boolean.parseBoolean(properties.getProperty(pencilName + key.getKey()));
	}
	
	protected int getIntProperty(PreferenceKey key) {
		return Integer.parseInt(properties.getProperty(pencilName + key.getKey()));
	}
	
	protected Color getColorProperty(PreferenceKey key) {
		return Color.decode(properties.getProperty(pencilName + key.getKey()));
	}
	
	protected void setStringProperty(PreferenceKey key, String value) {
		properties.setProperty(pencilName + key.getKey(), value);
	}

	protected void setBooleanProperty(PreferenceKey key, boolean value) {
		int i = value ? 1 : 0;
		properties.setProperty(pencilName + key.getKey(), Integer.toString(i));
//		properties.setProperty(pencilName + key.getKey(), Boolean.toString(value));
	}
	
	protected void setIntProperty(PreferenceKey key, int value) {
		properties.setProperty(pencilName + key.getKey(), Integer.toString(value));
	}
	
	protected void setColorProperty(PreferenceKey key, Color value) {
		properties.setProperty(pencilName + key.getKey(), getColorString(value));
	}

	public String getColorString(Color color) {
//		return Integer.toString(color.getRGB() & 0xFFFFFF);
		return String.format("0x%06X", new Object[] {Integer.valueOf(color.getRGB() & 0xFFFFFF)});
	}

	protected static List<PreferenceKey> usedKeys; // = Arrays.asList(new Preference[] {});

	public void applyCurrentPreferences(MenuCommand command) {
		applyCurrentPreferences1(command);
		PanelBase panel = command.getPanelBase();
		for (PreferenceKey key : usedKeys) {
			if (false)
				;
			else if (key == PreferenceKey.LINK_WIDTH)
				panel.setLinkWidth(getIntProperty(key));
			else if (key == PreferenceKey.COUNT_AREA_SIZE_MODE)
				panel.setCountAreaSizeMode(getBooleanProperty(key));
			else if (key == PreferenceKey.DOT_HINT_MODE)
				panel.setDotHintMode(getBooleanProperty(key));
			else if (key == PreferenceKey.HIDE_SOLE_NUMBER_MODE)
				panel.setHideSoleNumberMode(getBooleanProperty(key));
			else if (key == PreferenceKey.HIDE_STAR_MODE)
				panel.setHideStarMode(getBooleanProperty(key));
			else if (key == PreferenceKey.HIGHLIGHT_SELECTION_MODE)
				panel.setHighlightSelectionMode(getBooleanProperty(key));
			else if (key == PreferenceKey.INDICATE_ERROR_MODE)
				panel.setIndicateErrorMode(getBooleanProperty(key));
			else if (key == PreferenceKey.PAINT_ILLUMINATED_CELL_MODE)
				panel.setPaintIlluminatedCellMode(getBooleanProperty(key));
			else if (key == PreferenceKey.SEPARATE_AREA_COLOR_MODE)
				panel.setSeparateAreaColorMode(getBooleanProperty(key));
			else if (key == PreferenceKey.SEPARATE_LINK_COLOR_MODE)
				panel.setSeparateLinkColorMode(getBooleanProperty(key));
			else if (key == PreferenceKey.SEPARATE_TETROMINO_COLOR_MODE)
				panel.setSeparateTetrominoColorMode(getBooleanProperty(key));
			else if (key == PreferenceKey.SHOW_AREA_BORDER_MODE)
				panel.setShowAreaBorderMode(getBooleanProperty(key));
			else if (key == PreferenceKey.SHOW_BEAM_MODE)
				panel.setShowBeamMode(getBooleanProperty(key));
			else if (key == PreferenceKey.AREA_BORDER_COLOR)
				panel.setAreaBorderColor(getColorProperty(key));
			else if (key == PreferenceKey.AREA_PAINT_COLOR)
				panel.setAreaPaintColor(getColorProperty(key));
			else if (key == PreferenceKey.BLACK_AREA_COLOR)
				panel.setBlackAreaColor(getColorProperty(key));
			else if (key == PreferenceKey.BORDER_COLOR)
				panel.setBorderColor(getColorProperty(key));
			else if (key == PreferenceKey.BULB_COLOR)
				panel.setBulbColor(getColorProperty(key));
			else if (key == PreferenceKey.CROSS_COLOR)
				panel.setCrossColor(getColorProperty(key));
			else if (key == PreferenceKey.GATE_COLOR)
				panel.setGateColor(getColorProperty(key));
			else if (key == PreferenceKey.ILLUMINATED_CELL_COLOR)
				panel.setIlluminatedCellColor(getColorProperty(key));
			else if (key == PreferenceKey.INPUT_COLOR)
				panel.setInputColor(getColorProperty(key));
			else if (key == PreferenceKey.LINE_COLOR)
				panel.setLineColor(getColorProperty(key));
			else if (key == PreferenceKey.NO_BULB_COLOR)
				panel.setNoBulbColor(getColorProperty(key));
			else if (key == PreferenceKey.NO_PAINT_COLOR)
				panel.setCircleColor(getColorProperty(key));
			else if (key == PreferenceKey.NUMBER_COLOR)
				panel.setNumberColor(getColorProperty(key));
			else if (key == PreferenceKey.PAINT_COLOR)
				panel.setPaintColor(getColorProperty(key));
			else if (key == PreferenceKey.WALL_COLOR)
				panel.setWallColor(getColorProperty(key));
			else if (key == PreferenceKey.WHITE_AREA_COLOR)
				panel.setWhiteAreaColor(getColorProperty(key));
			else if (key == PreferenceKey.LETTERS)
				panel.setLetters(getStringProperty(key));
		}
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		acquireCurrentPreferences1(command);
		PanelBase panel = command.getPanelBase();
		for (PreferenceKey key : usedKeys) {
			if (false)
				;
			else if (key == PreferenceKey.LINK_WIDTH)
				setIntProperty(key, panel.getLinkWidth());
			else if (key == PreferenceKey.COUNT_AREA_SIZE_MODE)
				setBooleanProperty(key, panel.isCountAreaSizeMode());
			else if (key == PreferenceKey.DOT_HINT_MODE)
				setBooleanProperty(key, panel.isDotHintMode());
			else if (key == PreferenceKey.HIGHLIGHT_SELECTION_MODE)
				setBooleanProperty(key, panel.isHighlightSelectionMode());
			else if (key == PreferenceKey.HIDE_SOLE_NUMBER_MODE)
				setBooleanProperty(key, panel.isHideSoleNumberMode());
			else if (key == PreferenceKey.HIDE_STAR_MODE)
				setBooleanProperty(key, panel.isHideStarMode());
			else if (key == PreferenceKey.INDICATE_ERROR_MODE)
				setBooleanProperty(key, panel.isIndicateErrorMode());
			else if (key == PreferenceKey.PAINT_ILLUMINATED_CELL_MODE)
				setBooleanProperty(key, panel.isPaintIlluminatedCellMode());
			else if (key == PreferenceKey.SEPARATE_AREA_COLOR_MODE)
				setBooleanProperty(key, panel.isSeparateAreaColorMode());
			else if (key == PreferenceKey.SEPARATE_LINK_COLOR_MODE)
				setBooleanProperty(key, panel.isSeparateLinkColorMode());
			else if (key == PreferenceKey.SEPARATE_TETROMINO_COLOR_MODE)
				setBooleanProperty(key, panel.isSeparateTetrominoColorMode());
			else if (key == PreferenceKey.SHOW_AREA_BORDER_MODE)
				setBooleanProperty(key, panel.isShowAreaBorderMode());
			else if (key == PreferenceKey.SHOW_BEAM_MODE)
				setBooleanProperty(key, panel.isShowBeamMode());
			else if (key == PreferenceKey.AREA_BORDER_COLOR)
				setColorProperty(key, panel.getAreaBorderColor());
			else if (key == PreferenceKey.AREA_PAINT_COLOR)
				setColorProperty(key, panel.getAreaPaintColor());
			else if (key == PreferenceKey.BLACK_AREA_COLOR)
				setColorProperty(key, panel.getBlackAreaColor());
			else if (key == PreferenceKey.BORDER_COLOR)
				setColorProperty(key, panel.getBorderColor());
			else if (key == PreferenceKey.BULB_COLOR)
				setColorProperty(key, panel.getBulbColor());
			else if (key == PreferenceKey.CROSS_COLOR)
				setColorProperty(key, panel.getCrossColor());
			else if (key == PreferenceKey.PAINT_COLOR)
				setColorProperty(key, panel.getPaintColor());
			else if (key == PreferenceKey.ILLUMINATED_CELL_COLOR)
				setColorProperty(key, panel.getIlluminatedCellColor());
			else if (key == PreferenceKey.INPUT_COLOR)
				setColorProperty(key, panel.getInputColor());
			else if (key == PreferenceKey.LINE_COLOR)
				setColorProperty(key, panel.getLineColor());
			else if (key == PreferenceKey.NO_BULB_COLOR)
				setColorProperty(key, panel.getNoBulbColor());
			else if (key == PreferenceKey.NO_PAINT_COLOR)
				setColorProperty(key, panel.getCircleColor());
			else if (key == PreferenceKey.NUMBER_COLOR)
				setColorProperty(key, panel.getNumberColor());
			else if (key == PreferenceKey.WALL_COLOR)
				setColorProperty(key, panel.getWallColor());
			else if (key == PreferenceKey.WHITE_AREA_COLOR)
				setColorProperty(key, panel.getWhiteAreaColor());
			else if (key == PreferenceKey.LETTERS)
				setStringProperty(key, panel.getLetters());
		}
	}

}

/**
 * java.util.TreeMap クラスに java.util.Properties クラスと同様の store() メソッドを追加したもの。
 */
class TreeMapA extends java.util.TreeMap<Object, Object> {

    /**
     * @param m
     */
    public TreeMapA(Map<Object, Object> m) {
        super(m);
    }

	/**
	 * Map を ファイルに書き出す。
	 * java.util.Properties クラスの　store()を真似た。
	 * @param out
	 * @param comments
	 * @throws IOException
	 */
	public void store(OutputStream out, String comments) throws IOException {
        BufferedWriter awriter;
        awriter = new BufferedWriter(new OutputStreamWriter(out, "8859_1"));
        if (comments != null)
            writeln(awriter, "#" + comments);
        writeln(awriter, "#" + new Date().toString());
        for (Object k : keySet()) {
        	String key = (String)k;
            String val = (String)get(key);
            key = saveConvert(key, true);

	    /* No need to escape embedded and trailing spaces for value, hence
	     * pass false to flag.
	     */
            val = saveConvert(val, false);
            writeln(awriter, key + "=" + val);
        }
        awriter.flush();
    }

    private static void writeln(BufferedWriter bw, String s) throws IOException {
        bw.write(s);
        bw.newLine();
    }

    /*
     * Converts unicodes to encoded &#92;uxxxx and escapes
     * special characters with a preceding slash
     */
    private String saveConvert(String theString, boolean escapeSpace) {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);

        for(int x=0; x<len; x++) {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\'); outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch(aChar) {
		case ' ':
		    if (x == 0 || escapeSpace) 
			outBuffer.append('\\');
		    outBuffer.append(' ');
		    break;
                case '\t':outBuffer.append('\\'); outBuffer.append('t');
                          break;
                case '\n':outBuffer.append('\\'); outBuffer.append('n');
                          break;
                case '\r':outBuffer.append('\\'); outBuffer.append('r');
                          break;
                case '\f':outBuffer.append('\\'); outBuffer.append('f');
                          break;
                case '=': // Fall through
                case ':': // Fall through
                case '#': // Fall through
                case '!':
                    outBuffer.append('\\'); outBuffer.append(aChar);
                    break;
                default:
                    if ((aChar < 0x0020) || (aChar > 0x007e)) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >>  8) & 0xF));
                        outBuffer.append(toHex((aChar >>  4) & 0xF));
                        outBuffer.append(toHex( aChar        & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    /**
     * Convert a nibble to a hex character
     * @param	nibble	the nibble to convert.
     */
    private static char toHex(int nibble) {
	return hexDigit[(nibble & 0xF)];
    }

    /** A table of hex digits */
    private static final char[] hexDigit = {
	'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
    };

}
