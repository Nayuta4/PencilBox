package pencilbox.common.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import pencilbox.common.core.Address;
import pencilbox.common.core.Area;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator2;
import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;

/**
 * �̈�ҏW���[�h�ł̃p�l���ɑ΂���}�E�X�C�L�[�{�[�h�̃C�x���g�������s���N���X
 */
public class RegionEditHandler implements KeyListener, MouseListener, MouseMotionListener {

	private PanelBase panel;
	private BoardBase board;
	private BoardCopierBase boardCopier;
	
	private EventHandlerManager eventHandlerManager;

	private Address oldPos = Address.address(-1, -1);
	private Address newPos = Address.address(-1, -1);

	private Area copyRegion;
	private Area pasteRegion;
	private Address copyRegionOrigin;
	private Address pasteRegionOrigin;
	private Address pivot = Address.address();
	private int pasteRotation;

	/**
	 * RegionEditHandler�𐶐�����
	 */
	public RegionEditHandler() {
	}

	public void setup(PanelBase panel, BoardBase board, EventHandlerManager eventHandlerManager) {
		this.eventHandlerManager = eventHandlerManager;
		this.panel = panel;
		this.board = board;
		try {
			boardCopier = (BoardCopierBase) ClassUtil.createInstance(board.getClass(), ClassUtil.BOARD_COPIER_CLASS);
		} catch (PencilBoxClassException e) {
			boardCopier = new BoardCopierBase();
		}
		this.copyRegion = panel.getCopyRegion();
		this.pasteRegion = panel.getPasteRegion();
		this.copyRegionOrigin = panel.getCopyRegionOrigin();
		this.pasteRegionOrigin = panel.getPasteRegionOrigin();
		this.pasteRotation = 0;
		init();
	}

	public void init() {
		copyRegion.clear();
		pasteRegion.clear();
		copyRegionOrigin = Address.nowhere();
		pasteRegionOrigin = Address.nowhere();
	}

	/**
	 * �h���b�O�ɂ��̈�ړ��Œ����ǂ����B
	 * @return
	 */
	private boolean isMovingRegion() {
		return ! pasteRegion.isEmpty();
	}

	public void repaint() {
		panel.repaint();
	}

	public boolean isOn(Address position) {
		return board.isOn(position);
	}

	/*
	 * �L�[���X�i�[
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_SLASH:
		case KeyEvent.VK_DIVIDE:
			slashKeyEntered();
			break;
//		case KeyEvent.VK_LEFT: // 0x25
//			arrowKeyEntered(Direction.LT);
//			break;
//		case KeyEvent.VK_UP: // 0x26
//			arrowKeyEntered(Direction.UP);
//			break;
//		case KeyEvent.VK_RIGHT: // 0x27
//			arrowKeyEntered(Direction.RT);
//			break;
//		case KeyEvent.VK_DOWN: // 0x28
//			arrowKeyEntered(Direction.DN);
//			break;
		case KeyEvent.VK_SPACE:
		case KeyEvent.VK_PERIOD:
		case KeyEvent.VK_DECIMAL:
			spaceKeyEntered();
			break;
//		case KeyEvent.VK_MINUS:
//		case KeyEvent.VK_SUBTRACT:
//			minusKeyEntered();
//			break;
//		case KeyEvent.VK_SEMICOLON:
//		case KeyEvent.VK_ADD:
//			break;
//		case KeyEvent.VK_COLON:
//		case KeyEvent.VK_MULTIPLY:
//			break;
		case KeyEvent.VK_0:
		case KeyEvent.VK_NUMPAD0:
			numberKeyEntered(0);
			break;
		case KeyEvent.VK_1:
		case KeyEvent.VK_NUMPAD1:
			numberKeyEntered(1);
			break;
		case KeyEvent.VK_2:
		case KeyEvent.VK_NUMPAD2:
			numberKeyEntered(2);
			break;
		case KeyEvent.VK_3:
		case KeyEvent.VK_NUMPAD3:
			numberKeyEntered(3);
			break;
		case KeyEvent.VK_4:
		case KeyEvent.VK_NUMPAD4:
			numberKeyEntered(4);
			break;
		case KeyEvent.VK_5:
		case KeyEvent.VK_NUMPAD5:
			numberKeyEntered(5);
			break;
		case KeyEvent.VK_6:
		case KeyEvent.VK_NUMPAD6:
			numberKeyEntered(6);
			break;
		case KeyEvent.VK_7:
		case KeyEvent.VK_NUMPAD7:
			numberKeyEntered(7);
			break;
//		case KeyEvent.VK_8:
//		case KeyEvent.VK_NUMPAD8:
//			numberKeyEntered(8);
//			break;
//		case KeyEvent.VK_9:
//		case KeyEvent.VK_NUMPAD9:
//			numberKeyEntered(9);
//			break;
		}
		repaint();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

//	/**
//	 * ���L�[���͂���������B 
//	 */
//	protected void arrowKeyEntered(int direction) {
//	}

	/**
	 * �����L�[���͂���������B 
	 * �I��̈����]����B
	 */
	protected void numberKeyEntered(int number) {
		if (number >= 8)
			return;
		if (isMovingRegion()) {
			rotateArea(pasteRegion, pasteRegionOrigin, number);
			pasteRotation = Rotator2.combine(pasteRotation, number);
		}
	}

	/**
	 * �s���I�h�L�[�̓��͂���������B
	 */
	protected void spaceKeyEntered() {
		boardCopier.eraseRegion(board, copyRegion);
		board.initBoard();
	}

//	/**
//	 * �}�C�i�X�L�[�̓��͂���������B
//	 */
//	protected void minusKeyEntered() {
//	}

	/**
	 * �X���b�V���L�[�̓��͂���������B 
	 * �u�����̓��[�h�v�ɐ؂�ւ���
	 */
	protected void slashKeyEntered() {
		eventHandlerManager.setEditMode(PanelBase.PROBLEM_INPUT_MODE);
	}

	/*
	 * �}�E�X���X�i�[
	 */
	public void mousePressed(MouseEvent e) {
		newPos = panel.pointToAddress(e.getX(), e.getY());
		if (!isOn(newPos))
			return;
		boolean shift = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;
		boolean ctrl = (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
		if ((e.getButton() == MouseEvent.BUTTON1)) {
			leftPressed(newPos, shift, ctrl);
		} else if ((e.getButton() == MouseEvent.BUTTON3)) {
			rightPressed(newPos, shift, ctrl);
		}
		oldPos = newPos; // ���݈ʒu���X�V
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		newPos = panel.pointToAddress(e.getX(), e.getY());
		if (!isOn(newPos)) {
			return;
		}
		if (newPos.equals(oldPos))
			return; // �����}�X���Ɏ~�܂�C�x���g�͖���
		boolean shift = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;
//		boolean ctrl = (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
			leftDragged(newPos, shift);
		} else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			rightDragged(newPos);
		}
		oldPos = newPos; // ���݈ʒu���X�V
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		boolean shift = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;
		boolean ctrl = (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
		if ((e.getButton() == MouseEvent.BUTTON1)) {
			leftDragFixed(oldPos, shift, ctrl);
		}
		repaint();
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * ���{�^���������ꂽ�Ƃ��ɌĂ΂��B
	 * �I���ςݗ̈���I�������ꍇ�́C���̗̈���ړ��ΏۂƂ���B
	 * ����ȊO�̏ꍇ�͗̈�I�����̎n�_�Ƃ���B
	 * @param position
	 * @param shift
	 * @param ctrl
	 */
	protected void leftPressed(Address position, boolean shift, boolean ctrl) {
		if (copyRegion.contains(position)) {
			copyArea(copyRegion, pasteRegion);
			copyRegionOrigin = position;
			pasteRegionOrigin = position;
		} else {
			if (! ctrl) {
				copyRegion.clear();
			}
			copyRegion.add(position);
			pivot = position;
		}
	}

	/**
	 * ���h���b�O�����܂܂��V�����}�X�Ɉړ������Ƃ��ɌĂ΂��D
	 * �̈�ړ����́C�ړ�����B
	 * �̈�I�𒆂́C�}�X��̈�ɉ�����B
	 * @param position
	 * @param shift
	 */
	protected void leftDragged(Address position, boolean shift) {
		if (isMovingRegion()) {
			translateArea(pasteRegion, oldPos, newPos);
			pasteRegionOrigin = newPos;
		} else {
			if (shift) {
				selectRectangularArea(position);
			} else {
				copyRegion.add(position);
				pivot = position;
			}
		}
	}

	/**
	 * ��`�I��
	 * @param position
	 */
	private void selectRectangularArea(Address position) {
		int r0 = pivot.r() < position.r() ? pivot.r() : position.r();
		int c0 = pivot.c() < position.c() ? pivot.c() : position.c();
		int r1 = pivot.r() < position.r() ? position.r() : pivot.r();
		int c1 = pivot.c() < position.c() ? position.c() : pivot.c();
		for (int r = r0; r <= r1; r++) {
			for (int c = c0; c <= c1; c++) {
				copyRegion.add(Address.address(r, c));
			}
		}
	}

	/**
	 * ���}�E�X�{�^���𗣂��č��h���b�O���m�肵���Ƃ��ɌĂ΂��B 
	 * �̈�ړ����́C�ړ����m�肷��B
	 * CTRL�L�[�������Ȃ���{�^���𗣂��ƃR�s�[�ɁC����ȊO�͈ړ��ɂȂ�B
	 * @param dragEnd
	 * @param shift
	 * @param ctrl
	 */
	protected void leftDragFixed(Address dragEnd, boolean shift, boolean ctrl) {
		if (isMovingRegion()) {
			if (ctrl) {
				boardCopier.copyRegion(board, copyRegion, copyRegionOrigin, pasteRegionOrigin, pasteRotation);
			} else {
				boardCopier.moveRegion(board, copyRegion, copyRegionOrigin, pasteRegionOrigin, pasteRotation);
			}
			board.initBoard();
			copyArea(pasteRegion, copyRegion);
			pasteRegion.clear();
			copyRegionOrigin = Address.nowhere();
			pasteRegionOrigin = Address.nowhere();
			pasteRotation = 0;
		}
	}

	/**
	 * �E�{�^���������ꂽ�Ƃ��ɌĂ΂��B 
	 * �I��̈�S�̂���������B
	 * CTRL�������Ȃ���̏ꍇ�́C���̃}�X�̂ݗ̈悩�珜������B
	 * @param position
	 * @param shift
	 * @param ctrl
	 */
	protected void rightPressed(Address position, boolean shift, boolean ctrl) {
		if (ctrl) {
			copyRegion.remove(position);
		} else {
			copyRegion.clear();
			pasteRegion.clear();
			copyRegionOrigin = Address.nowhere();
			pasteRegionOrigin = Address.nowhere();
			pasteRotation = 0;
		}
	}

	/**
	 * �E�h���b�O�����܂܂��V�����}�X�Ɉړ������Ƃ��ɌĂ΂��B
	 * @param position
	 */
	protected void rightDragged(Address position) {
		copyRegion.remove(position);
	}

	/**
	 * �̈�𕡎ʂ���B
	 */
	private void copyArea(Area src, Area dst) {
		dst.clear();
		for (Address p : src) {
			if (isOn(p)) {
				dst.add(Address.address(p));
			}
		}
	}
	
	/**
	 * �̈�𕽍s�ړ�����B
	 * Address���̂𓮂����Ă���̂Œ��ӂ��Ďg�p���邱�ƁB
	 */
	private void translateArea(Area area, Address from, Address to) {
		for (Address pos : area) {
			pos.set(pos.r() + to.r() - from.r(), pos.c() + to.c() - from.c());
		}
	}

	/**
	 * �̈����]����B
	 */
	private void rotateArea(Area area, Address center, int rotation) {
		Area dst = new Area();
		for (Address p : area) {
			Address d = Rotator2.translateAndRotateAddress(p, center, center, rotation);
			dst.add(d);
		}
		area.clear();
		area.addAll(dst);
	}

}
