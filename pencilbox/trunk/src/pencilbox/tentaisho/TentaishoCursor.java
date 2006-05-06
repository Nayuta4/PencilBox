package pencilbox.tentaisho;

import pencilbox.common.core.Address;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandler;

/**
 * �u�V�̃V���[�v�����͗p�J�[�\���N���X
 */
public class TentaishoCursor extends CellCursor {

	/**
	 * �u�V�̃V���[�v�����͗p�J�[�\�����쐬����
	 * Panel�Ɗ֘A�t����
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
