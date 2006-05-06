package pencilbox.slitherlink;

import pencilbox.common.core.Address;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandler;

/**
 * 「スリザーリンク」問題入力用カーソルクラス
 */
public class SlitherLinkCursor extends CellCursor {

	/**
	 * @param aPanel
	 */
	public SlitherLinkCursor(PanelEventHandler aPanel) {
		super(aPanel);
	}

	public void setPosition(int r, int c) {
		if (isOn(r, c, -1, -1)) 
			pos.set(r, c);
	}

	public Address getBoardPosition() {
		Address boardPos = new Address(pos);
		panel.p2b(boardPos, -1, -1);
		return boardPos;
	}

}
