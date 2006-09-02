package pencilbox.tentaisho;

import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�V�̃V���[�v�����͗p�J�[�\���N���X
 */
public class TentaishoCursor extends CellCursor {

	/**
	 * �u�V�̃V���[�v�����͗p�J�[�\�����쐬����
	 * Panel�Ɗ֘A�t����
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
