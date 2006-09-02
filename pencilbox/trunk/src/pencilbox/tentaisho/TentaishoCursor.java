package pencilbox.tentaisho;

import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * 「天体ショー」問題入力用カーソルクラス
 */
public class TentaishoCursor extends CellCursor {

	/**
	 * 「天体ショー」問題入力用カーソルを作成する
	 * Panelと関連付ける
	 * @param panel
	 */
	public TentaishoCursor(PanelEventHandlerBase panel) {
		super(panel);
	}
	
	public void setPosition(int r, int c) {
		if (isOn(r, c, rows()-1, cols()-1)) 
			pos.set(r, c);
	}
	
}
