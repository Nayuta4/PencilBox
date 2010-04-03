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
 * 領域編集モードでのパネルに対するマウス，キーボードのイベント処理を行うクラス
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
	 * RegionEditHandlerを生成する
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
	 * ドラッグによる領域移動最中かどうか。
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
	 * キーリスナー
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
//	 * 矢印キー入力を処理する。 
//	 */
//	protected void arrowKeyEntered(int direction) {
//	}

	/**
	 * 数字キー入力を処理する。 
	 * 選択領域を回転する。
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
	 * ピリオドキーの入力を処理する。
	 */
	protected void spaceKeyEntered() {
		boardCopier.eraseRegion(board, copyRegion);
		board.initBoard();
	}

//	/**
//	 * マイナスキーの入力を処理する。
//	 */
//	protected void minusKeyEntered() {
//	}

	/**
	 * スラッシュキーの入力を処理する。 
	 * 「問題入力モード」に切り替える
	 */
	protected void slashKeyEntered() {
		eventHandlerManager.setEditMode(PanelBase.PROBLEM_INPUT_MODE);
	}

	/*
	 * マウスリスナー
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
		oldPos = newPos; // 現在位置を更新
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		newPos = panel.pointToAddress(e.getX(), e.getY());
		if (!isOn(newPos)) {
			return;
		}
		if (newPos.equals(oldPos))
			return; // 同じマス内に止まるイベントは無視
		boolean shift = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;
//		boolean ctrl = (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
			leftDragged(newPos, shift);
		} else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			rightDragged(newPos);
		}
		oldPos = newPos; // 現在位置を更新
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
	 * 左ボタンが押されたときに呼ばれる。
	 * 選択済み領域上を選択した場合は，その領域を移動対象とする。
	 * それ以外の場合は領域選択をの始点とする。
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
	 * 左ドラッグしたままた新しいマスに移動したときに呼ばれる．
	 * 領域移動中は，移動する。
	 * 領域選択中は，マスを領域に加える。
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
	 * 矩形選択
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
	 * 左マウスボタンを離して左ドラッグが確定したときに呼ばれる。 
	 * 領域移動中は，移動を確定する。
	 * CTRLキーを押しながらボタンを離すとコピーに，それ以外は移動になる。
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
	 * 右ボタンが押されたときに呼ばれる。 
	 * 選択領域全体を消去する。
	 * CTRLを押しながらの場合は，そのマスのみ領域から除去する。
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
	 * 右ドラッグしたままた新しいマスに移動したときに呼ばれる。
	 * @param position
	 */
	protected void rightDragged(Address position) {
		copyRegion.remove(position);
	}

	/**
	 * 領域を複写する。
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
	 * 領域を平行移動する。
	 * Address自体を動かしているので注意して使用すること。
	 */
	private void translateArea(Area area, Address from, Address to) {
		for (Address pos : area) {
			pos.set(pos.r() + to.r() - from.r(), pos.c() + to.c() - from.c());
		}
	}

	/**
	 * 領域を回転する。
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
