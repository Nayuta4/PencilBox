/**
 * 
 */
package pencilbox.common.gui;


/**
 * 
 */
public class PreferenceKey {

	public static final PreferenceKey EDIT_MODE = new PreferenceKey("EditMode");
	public static final PreferenceKey SYMMETRIC_PLACEMENT_MODE = new PreferenceKey("SymmetricPlacementMode");
	public static final PreferenceKey IMMEDIATE_ANSWER_CHECK_MODE = new PreferenceKey("ImmediateAnswerCheckMode");

	public static final PreferenceKey CELL_SIZE = new PreferenceKey("CellSize");
	public static final PreferenceKey INDEX_MODE = new PreferenceKey("IndexMode");
	public static final PreferenceKey INDEX_STYLE_0 = new PreferenceKey("IndexStyle0");
	public static final PreferenceKey INDEX_STYLE_1 = new PreferenceKey("IndexStyle1");
	public static final PreferenceKey GRID_STYLE = new PreferenceKey("GridStyle");
	public static final PreferenceKey CURSOR_MODE = new PreferenceKey("CursorMode");
	public static final PreferenceKey MARK_STYLE = new PreferenceKey("MarkStyle");
	public static final PreferenceKey LINK_WIDTH = new PreferenceKey("LinkWidth");

	public static final PreferenceKey BACKGROUND_COLOR = new PreferenceKey("BackgroundColor");
	public static final PreferenceKey GRID_COLOR = new PreferenceKey("GridColor");
	public static final PreferenceKey NUMBER_COLOR = new PreferenceKey("NumberColor");
	public static final PreferenceKey INPUT_COLOR = new PreferenceKey("InputColor");
	public static final PreferenceKey WALL_COLOR = new PreferenceKey("WallColor");
	public static final PreferenceKey AREA_BORDER_COLOR = new PreferenceKey("AreaBorderColor");
	public static final PreferenceKey AREA_PAINT_COLOR = new PreferenceKey("AreaPaintColor");
	public static final PreferenceKey WHITE_AREA_COLOR = new PreferenceKey("WhiteAreacolor");
	public static final PreferenceKey BLACK_AREA_COLOR = new PreferenceKey("BlackAreaColor");
	public static final PreferenceKey ILLUMINATED_CELL_COLOR = new PreferenceKey("IlluminatedCellColor");
	public static final PreferenceKey LINE_COLOR = new PreferenceKey("LineColor");
	public static final PreferenceKey CROSS_COLOR = new PreferenceKey("CrossColor");
	public static final PreferenceKey BULB_COLOR = new PreferenceKey("BulbColor");
	public static final PreferenceKey NO_BULB_COLOR = new PreferenceKey("NoBulbColor");
	public static final PreferenceKey PAINT_COLOR = new PreferenceKey("PaintColor");
	public static final PreferenceKey NO_PAINT_COLOR = new PreferenceKey("NoPaintColor");
	public static final PreferenceKey GATE_COLOR = new PreferenceKey("GateColor");
	public static final PreferenceKey BORDER_COLOR = new PreferenceKey("BorderColor");

	public static final PreferenceKey LETTERS = new PreferenceKey("Letters");

	public static final PreferenceKey HIGHLIGHT_SELECTION_MODE = new PreferenceKey("HighlightSelectionMode");
	public static final PreferenceKey INDICATE_ERROR_MODE = new PreferenceKey("IndicateErrorMode");
	public static final PreferenceKey COUNT_AREA_SIZE_MODE = new PreferenceKey("CountAreaSizeMode");
	public static final PreferenceKey DOT_HINT_MODE = new PreferenceKey("DotHintMode");
	public static final PreferenceKey HIDE_SOLE_NUMBER_MODE = new PreferenceKey("HideSoleNumberMode");
	public static final PreferenceKey HIDE_STAR_MODE = new PreferenceKey("HideStarMode");
	public static final PreferenceKey SHOW_AREA_BORDER_MODE = new PreferenceKey("ShowAreaBorderMode");
	public static final PreferenceKey SHOW_BEAM_MODE = new PreferenceKey("ShowBeamMode");
	public static final PreferenceKey PAINT_ILLUMINATED_CELL_MODE = new PreferenceKey("PaintIlluminatedCellMode");
	public static final PreferenceKey SEPARATE_AREA_COLOR_MODE = new PreferenceKey("SeparateAreaColorMode");
	public static final PreferenceKey SEPARATE_LINK_COLOR_MODE = new PreferenceKey("SeparateLinkColorMode");
	public static final PreferenceKey SEPARATE_TETROMINO_COLOR_MODE = new PreferenceKey("SeparateTetrominoColorMode");

	private PreferenceKey(String k) {
		this.key = k;
	}
	public String getKey() {
		return key;
	}
	private String key;
}
