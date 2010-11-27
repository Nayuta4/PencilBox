/**
 * 
 */
package pencilbox.common.gui;


/**
 * 
 */
public enum PreferenceKey {

	EDIT_MODE("EditMode"),
	SYMMETRIC_PLACEMENT_MODE("SymmetricPlacementMode"),
	IMMEDIATE_ANSWER_CHECK_MODE("ImmediateAnswerCheckMode"),

	CELL_SIZE("CellSize"),
	INDEX_MODE("IndexMode"),
	GRID_STYLE("GridStyle"),
	CURSOR_MODE("CursorMode"),
	MARK_STYLE("MarkStyle"),
	LINK_WIDTH("LinkWidth"),

	BACKGROUND_COLOR("BackgroundColor"),
	GRID_COLOR("GridColor"),
	NUMBER_COLOR("NumberColor"),
	INPUT_COLOR("InputColor"),
	WALL_COLOR("WallColor"),
	AREA_BORDER_COLOR("AreaBorderColor"),
	AREA_PAINT_COLOR("AreaPaintColor"),
	WHITE_AREA_COLOR("WhiteAreacolor"),
	BLACK_AREA_COLOR("BlackAreaColor"),
	ILLUMINATED_CELL_COLOR("IlluminatedCellColor"),
	LINE_COLOR("LineColor"),
	CROSS_COLOR("CrossColor"),
	BULB_COLOR("BulbColor"),
	NO_BULB_COLOR("NoBulbColor"),
	PAINT_COLOR("PaintColor"),
	NO_PAINT_COLOR("NoPaintColor"),
	GATE_COLOR("GateColor"),
	BORDER_COLOR("BorderColor"),

	LETTERS("Letters"),

	HIGHLIGHT_SELECTION_MODE("HighlightSelectionMode"),
	INDICATE_ERROR_MODE("IndicateErrorMode"),
	COUNT_AREA_SIZE_MODE("CountAreaSizeMode"),
	DOT_HINT_MODE("DotHintMode"),
	HIDE_SOLE_NUMBER_MODE("HideSoleNumberMode"),
	HIDE_STAR_MODE("HideStarMode"),
	SHOW_AREA_BORDER_MODE("ShowAreaBorderMode"),
	SHOW_BEAM_MODE("ShowBeamMode"),
	PAINT_ILLUMINATED_CELL_MODE("PaintIlluminatedCellMode"),
	SEPARATE_AREA_COLOR_MODE("SeparateAreaColorMode"),
	SEPARATE_LINK_COLOR_MODE("SeparateLinkColorMode"),
	SEPARATE_TETROMINO_COLOR_MODE("SeparateTetrominoColorMode"),
	;

	private PreferenceKey(String k) {
		this.key = k;
	}
	public String getKey() {
		return key;
	}
	private String key;
}
