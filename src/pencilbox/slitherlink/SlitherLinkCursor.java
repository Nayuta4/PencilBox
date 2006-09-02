package pencilbox.slitherlink;

import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�X���U�[�����N�v�����͗p�J�[�\���N���X
 */
public class SlitherLinkCursor extends CellCursor {

	/**
	 * @param aPanel
	 */
	public SlitherLinkCursor(PanelEventHandlerBase aPanel) {
		super(aPanel);
	}

	public void setPosition(int r, int c) {
		if (isOn(r, c, -1, -1)) 
			pos.set(r, c);
	}

}
