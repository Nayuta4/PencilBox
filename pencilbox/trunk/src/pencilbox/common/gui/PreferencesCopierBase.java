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
import java.util.Map;
import java.util.Properties;

import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;

/**
 * �A�v���P�[�V�����̐ݒ�̕ۑ��C�Ǎ��C���ʂ��s�����߂̃N���X
 */
public class PreferencesCopierBase {
	
	private String pencilName;
	private Properties properties = new Properties();
	
	/**
	 * PreferencesCopier �̃C���X�^���X���쐬����B
	 * @param pencilType ���
	 * @return �쐬���� PreferencesCopier �C���X�^���X
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
	 * ���݂� properties �Ɋi�[����Ă���ݒ���A�v���P�[�V�����ɓK�p����B
	 * @param command
	 */
	public void applyCurrentPreferences(MenuCommand command) {
		PanelBase panel = command.getPanelBase();
		PanelEventHandlerBase handler = command.getPanelEventHandlerBase();
		handler.setProblemEditMode(getBooleanProperty(PreferencesKeys.EDIT_MODE));
		handler.setSymmetricPlacementMode(getBooleanProperty(PreferencesKeys.SYMMETRIC_PLACEMENT_MODE));
		handler.setImmediateAnswerCheckMode(getBooleanProperty(PreferencesKeys.IMMEDIATE_ANSWER_CHECK_MODE));
		panel.setDisplaySize(getIntProperty(PreferencesKeys.CELL_SIZE));
		panel.changeIndexMode(getBooleanProperty(PreferencesKeys.INDEX_MODE));
		panel.setGridStyle(getIntProperty(PreferencesKeys.GRID_STYLE));
		panel.setMarkStyle(getIntProperty(PreferencesKeys.MARK_STYLE));
		panel.setCursorMode(getBooleanProperty(PreferencesKeys.CURSOR_MODE));
		panel.setBackgroundColor(getColorProperty(PreferencesKeys.BACKGROUND_COLOR));
		panel.setGridColor(getColorProperty(PreferencesKeys.GRID_COLOR));
	}
	
	/**
	 * ���݂̃A�v���P�[�V�����̐ݒ���擾���� properties �Ɋi�[����B 
	 * @param command
	 */
	public void acquireCurrentPreferences(MenuCommand command) {
		PanelBase panel = command.getPanelBase();
		PanelEventHandlerBase handler = command.getPanelEventHandlerBase();
		setBooleanProperty(PreferencesKeys.EDIT_MODE, panel.isProblemEditMode());
		setBooleanProperty(PreferencesKeys.SYMMETRIC_PLACEMENT_MODE, handler.isSymmetricPlacementMode());
		setBooleanProperty(PreferencesKeys.IMMEDIATE_ANSWER_CHECK_MODE, handler.isImmediateAnswerCheckMode());
		setIntProperty(PreferencesKeys.CELL_SIZE, panel.getCellSize());
		setBooleanProperty(PreferencesKeys.INDEX_MODE, panel.isIndexMode());
		setIntProperty(PreferencesKeys.GRID_STYLE, panel.getGridStyle());
		setIntProperty(PreferencesKeys.MARK_STYLE, panel.getMarkStyle());
		setBooleanProperty(PreferencesKeys.CURSOR_MODE, panel.isCursorMode());
		setColorProperty(PreferencesKeys.BACKGROUND_COLOR, panel.getBackgroundColor());
		setColorProperty(PreferencesKeys.GRID_COLOR, panel.getGridColor());
	}
	
	/**
	 * ���j���[�I�����R�s�[����
	 * @param src �R�s�[���t���[���� MenuCommand �C���X�^���X
	 * @param dst �R�s�[��t���[���� MenuCommand �C���X�^���X
	 */
	public void copyPreferences(MenuCommand src, MenuCommand dst) {
		acquireCurrentPreferences(src);
		applyCurrentPreferences(dst);
	}

	/**
	 * �ݒ���t�@�C������ǂݍ���ŃA�v���P�[�V�����ɓK�p����B
	 * �t�@�C���ɂȂ����ڂ͌��݂̐ݒ�̂܂܁B
	 * @param command
	 * @param file �ǂݍ��ރt�@�C��
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
	 * �A�v���P�[�V�����̐ݒ���t�@�C���ɕۑ�����B
	 * �ۑ�����t�@�C�������łɑ��݂���ꍇ�́C
	 * ������ނ̍��ڂɂ��Ă͏㏑�����C����ȊO�̍��ڂɂ��Ă͌��̃t�@�C���̒l��ێ�����B
	 * @param command
	 * @param file �ۑ�����t�@�C��
	 */
	public void storePreferences(MenuCommand command, File file) {
		try {
			if (file.canRead()) {
				properties.load(new FileInputStream(file));
			}
			acquireCurrentPreferences(command);
//			properties.list(System.out);
//			properties.store(new FileOutputStream(file), "PencilBox preferences");
			// �L�[�Ő��񂷂邽�߂ɁCProperties ���@TreeMap�@�ɕϊ����Ă���ۑ�����B
			TreeMapA map = new TreeMapA(properties);
			map.store(new FileOutputStream(file), "PencilBox preferences");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String getStringProperty(String key) {
		return properties.getProperty(pencilName + key);
	}
	
	protected boolean getBooleanProperty(String key) {
		return Integer.parseInt(properties.getProperty(pencilName + key)) > 0;
//		return Boolean.parseBoolean(properties.getProperty(pencilName + key));
	}
	
	protected int getIntProperty(String key) {
		return Integer.parseInt(properties.getProperty(pencilName + key));
	}
	
	protected Color getColorProperty(String key) {
		return Color.decode(properties.getProperty(pencilName + key));
	}
	
	protected void setStringProperty(String key, String value) {
		properties.setProperty(pencilName + key, value);
	}

	protected void setBooleanProperty(String key, boolean value) {
		int i = value ? 1 : 0;
		properties.setProperty(pencilName + key, Integer.toString(i));
//		properties.setProperty(pencilName + key, Boolean.toString(value));
	}
	
	protected void setIntProperty(String key, int value) {
		properties.setProperty(pencilName + key, Integer.toString(value));
	}
	
	protected void setColorProperty(String key, Color value) {
		properties.setProperty(pencilName + key, getColorString(value));
	}

	public String getColorString(Color color) {
//		return Integer.toString(color.getRGB() & 0xFFFFFF);
		return String.format("0x%06X", new Object[] {Integer.valueOf(color.getRGB() & 0xFFFFFF)});
	}

}

/**
 * java.util.TreeMap �N���X�� java.util.Properties �N���X�Ɠ��l�� store() ���\�b�h��ǉ��������́B
 */
class TreeMapA extends java.util.TreeMap<Object, Object> {

    /**
     * @param m
     */
    public TreeMapA(Map<Object, Object> m) {
        super(m);
    }

	/**
	 * Map �� �t�@�C���ɏ����o���B
	 * java.util.Properties �N���X�́@store()��^�����B
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
