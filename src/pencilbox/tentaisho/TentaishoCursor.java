package pencilbox.tentaisho;

import pencilbox.common.core.Address;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandler;

/**
 * 「天体ショー」問題入力用カーソルクラス
 */
public class TentaishoCursor extends CellCursor {

	/**
	 * 「天体ショー」問題入力用カーソルを作成する
	 * Panelと関連付ける
	 * @param panel
	 */
	public TentaishoCursor(PanelEventHandler panel) {
		super(panel);
	}
	
	public void setPosition(int r, int c) {
		if (isOn(r, c, rows()-1, cols()-1)) 
			pos.set(r, c);
	}
	
	public Address getBoardPosition() {
		Address boardPos = new Address(pos);
		panel.p2b(boardPos, rows()-1, cols()-1);
		return boardPos;
	}

}
