package pencilbox.common.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;

/**
 * パネルに対するマウス，キーボードのイベント処理を行うクラス
 */
public class PanelEventHandlerBase {

	private PanelBase panel;
	private BoardBase board;

	private KeyHandler keyHandler = new KeyHandler();
	private MouseHandler mouseHandler = new MouseHandler();
	private MouseHandlerEdge mouseHandlerEdge = new MouseHandlerEdge();

	private int maxInputNumber = 99;
	private int previousInput = 0;
	private int symmetricPlacementMode = 0;

	/**
	 * PanelEventHandlerを生成する
	 */
	public PanelEventHandlerBase() {
	}

	public void setup(PanelBase panel, BoardBase board) {
		this.panel = panel;
		this.board = board;
		setBoard(board);
		panel.addKeyListener(keyHandler);
		panel.addMouseListener(mouseHandler);
		panel.addMouseMotionListener(mouseHandler);
		panel.addMouseListener(mouseHandlerEdge);
	}

	public void setup(BoardBase board) {
		this.board = board;
		setBoard(board);
		getCellCursor().resetPosition();
		resetPreviousInput();
	}

	/**
	 * 個別クラスのパネルに個別クラスの盤面を設定するためのメソッド
	 * 各個別クラスでオーバーライドする
	 * @param board 盤面
	 */
	protected void setBoard(BoardBase board) {
	}

	public PanelBase getPanel() {
		return panel;
	}

	/**
	 * @return the symmetricPlacementMode
	 */
	public boolean isSymmetricPlacementMode() {
		return symmetricPlacementMode == 1 ? true : false;
	}
	/**
	 * @param symmetricPlacementMode the symmetricPlacementMode to set
	 */
	public void setSymmetricPlacementMode(boolean symmetricPlacementMode) {
		this.symmetricPlacementMode = symmetricPlacementMode ? 1 : 0;
	}

	/**
	 * 入力可能な最大数字を設定する
	 * @param number 設定する数値
	 */
	protected void setMaxInputNumber(int number) {
		maxInputNumber = number;
	}
	/**
	 * 入力数字の一時記憶をクリアする
	 */
	public void resetPreviousInput() {
		previousInput = 0;
	}

	public int getCellSize() {
		return panel.getCellSize();
	}

	public int getHalfCellSize() {
		return panel.getHalfCellSize();
	}

	public int getOffsetx() {
		return panel.getOffsetx();
	}

	public int getOffsety() {
		return panel.getOffsety();
	}

	public boolean isProblemEditMode() {
		return panel.isProblemEditMode();
	}

	public void setProblemEditMode(boolean b) {
		panel.setProblemEditMode(b);
		resetPreviousInput();
	}

	public CellCursor getCellCursor() {
		return panel.getCellCursor();
	}

	public boolean isCursorOn() {
		return panel.isCursorOn();
	}

	public void repaint() {
		panel.repaint();
	}

	public boolean isOn(Address position) {
		return board.isOn(position);
	}

//	public boolean isOn(int r, int c, int adjustRow, int adjustCol) {
//		return board.isOn(r, c, adjustRow, adjustCol);
//	}

	public boolean isSideOn(SideAddress position) {
		return board.isSideOn(position);
	}

	/**
	 * カーソル座標が盤面上にあるか。
	 * 通常は #isOn(Address) と同じ結果を返す。
	 * SL, TS 等カーソルの座標系が異なるタイプでは，サブクラスで再定義する。
	 * @param position カーソル座標
	 * @return カーソル座標が盤面上にあれば true
	 */
	public boolean isCursorOnBoard(Address position) {
		return board.isOn(position);
	}

	/**
	 * Panel上のx方向ピクセル座標をPanel上の列方向マス座標に変換する
	 * @param x Panel上のピクセル座標のx
	 * @return xをPanel方向列座標に変換した数値
	 */
	public final int toC(int x) {
		return (x + getCellSize() - getOffsetx()) / getCellSize() - 1;
	}
	/**
	 * Panel上のｙ向ピクセル座標をPanel上の行方向マス座標に変換する
	 * @param y Panel上のピクセル座標のy
	 * @return yをPanel方向列座標に変換した数値
	 */
	public final int toR(int y) {
		return (y + getCellSize() - getOffsety()) / getCellSize() - 1;
	}

	/**
	 * 点対称位置の座標を取得する。
	 * @param pos　元座標
	 * @return posと点対称な位置の座標
	 */
	public Address getSymmetricPosition(Address pos) {
		return new Address(board.rows()-1-pos.r(), board.cols()-1-pos.c());
	}

	/**
	 * キーリスナー
	 */
	public class KeyHandler implements KeyListener {

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_SLASH:
			case KeyEvent.VK_DIVIDE:
				slashKeyEntered();
				break;
			case KeyEvent.VK_LEFT: // 0x25
				arrowKeyEntered(Direction.LT);
				break;
			case KeyEvent.VK_UP: // 0x26
				arrowKeyEntered(Direction.UP);
				break;
			case KeyEvent.VK_RIGHT: // 0x27
				arrowKeyEntered(Direction.RT);
				break;
			case KeyEvent.VK_DOWN: // 0x28
				arrowKeyEntered(Direction.DN);
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_PERIOD:
			case KeyEvent.VK_DECIMAL:
				spaceKeyEntered();
				break;
			case KeyEvent.VK_MINUS:
			case KeyEvent.VK_SUBTRACT:
				minusKeyEntered();
				break;
			case KeyEvent.VK_SEMICOLON:
			case KeyEvent.VK_ADD:
				break;
			case KeyEvent.VK_COLON:
			case KeyEvent.VK_MULTIPLY:
				break;
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
			case KeyEvent.VK_8:
			case KeyEvent.VK_NUMPAD8:
				numberKeyEntered(8);
				break;
			case KeyEvent.VK_9:
			case KeyEvent.VK_NUMPAD9:
				numberKeyEntered(9);
				break;
			}
			repaint();
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}
	}
	/**
	 * 矢印キー入力を処理する。 矢印の方向にカーソルを移動する。
	 */
	protected void arrowKeyEntered(int direction) {
		if (!isProblemEditMode() && !isCursorOn())
			return;
		Address pos = getCellCursor().getPosition();
		pos.move(direction);
		if (isCursorOnBoard(pos)) {
			getCellCursor().setPosition(pos);
			resetPreviousInput();
		}
	}
	/**
	 * 数字キー入力を処理する。 
	 * 0-9 の数字キーが入力されたときに，状況に応じて2桁の数字にして numberEnteredメソッドに渡す
	 */
	protected void numberKeyEntered(int number) {
		if (!isProblemEditMode() && !isCursorOn())
			return;
		Address pos = getCellCursor().getPosition();
		if (previousInput >= 1 && previousInput <= 9) {
			if (previousInput * 10 + number <= maxInputNumber) {
				number = previousInput * 10 + number;
			}
			if (number <= maxInputNumber) {
				numberEntered(pos, number);
				previousInput = 0;
			}
		} else {
			if (number <= maxInputNumber) {
				numberEntered(pos, number);
				previousInput = number;
			}
		}
	}
	/**
	 * 数字入力を処理する。
	 * 各サブクラスで実装する。
	 * @param pos 数字を入力したマスの座標
	 * @param num 入力した数字
	 */
	protected void numberEntered(Address pos, int num) {
	}
	/**
	 * ピリオドキーの入力を処理する。
	 * 各サブクラスで実装する。
	 * @param pos 入力マスの座標
	 */
	protected void spaceEntered(Address pos) {
	}

	protected void spaceKeyEntered() {
		Address pos = getCellCursor().getPosition();
		spaceEntered(pos);
	}

	/**
	 * マイナスキーの入力を処理する。
	 * 各サブクラスで実装する。
	 * @param pos 入力マスの座標
	 */
	protected void minusEntered(Address pos) {
	}

	protected void minusKeyEntered() {
		Address pos = getCellCursor().getPosition();
		minusEntered(pos);
	}

	/**
	 * スラッシュキーの入力を処理する。
	 * 「問題入力モード」と「解答モード」を切り替える
	 */
	protected void slashKeyEntered() {
		if (isProblemEditMode())
			board.initBoard();
		setProblemEditMode(!isProblemEditMode());
	}

	/**
	 * マウスリスナーの共通スーパークラス
	 * マウスを押したとき，ドラッグしたまま新しいマスに移動したとき，ドラッグ終了したときの動作を定義している。
	 */
	public class MouseHandler implements MouseListener, MouseMotionListener {

		private Address oldPos = new Address(-1, -1);
		private Address newPos = new Address(-1, -1);

		public void mousePressed(MouseEvent e) {

			newPos.set(toR(e.getY()), toC(e.getX()));
			if (!isOn(newPos))
				return;
			moveCursor(newPos);
			int modifier = e.getModifiers();
			if ((modifier & InputEvent.BUTTON1_MASK) != 0) {
				if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0)
					leftPressedShift(newPos);
				else
					leftPressed(newPos);
			} else if ((modifier & InputEvent.BUTTON3_MASK) != 0) {
				rightPressed(newPos);
			}

			oldPos.set(newPos); // 現在位置を更新
			repaint();
		}

		public void mouseDragged(MouseEvent e) {

			newPos.set(toR(e.getY()), toC(e.getX()));
			if (!isOn(newPos)) {
				oldPos.setNowhere();
				// この文を入れないと，盤外を経由したドラッグが無効化されない あってもなくても同じ
				return;
			}

			if (newPos.equals(oldPos))
				return; // 同じマス内に止まるイベントは無視
			// if (!newPos.isNextTo(oldPos)) return; // 隣接マス以外のイベントは無視
			// if (dragIneffective(oldPos, newPos)) return; // 隣接マス以外のイベントは無視

			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				leftDragged(oldPos, newPos);
			} else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				rightDragged(oldPos, newPos);
			}
			moveCursor(newPos);
			oldPos.set(newPos); // 現在位置を更新
			repaint();
		}

		public void mouseReleased(MouseEvent e) {

			if (!isOn(oldPos)) {
				dragFailed();
				repaint();
				return;
			}
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				leftDragFixed(oldPos);
			} else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				rightDragFixed(oldPos);
			}
			repaint();
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			if (!isOn(newPos))
				return;
			int modifier = e.getModifiers();
			if ((modifier & InputEvent.BUTTON1_MASK) != 0) {
				if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0)
					leftClickedShift(newPos);
				else
					leftClicked(newPos);
			} else if ((modifier & InputEvent.BUTTON3_MASK) != 0) {
				rightClicked(newPos);
			}
			repaint();
		}

		public void mouseMoved(MouseEvent e) {
			// movePos.set(toR(e.getY()), toC(e.getX()));
			// if (!isOn(movePos))
			// return;
			// mouseMovedTo(movePos);
			// repaint();
		}
	}

	/**
	 * 左ボタンが押されたときに呼ばれる。
	 * サブクラスで操作をオーバーライドする．
	 * @param position
	 */
	protected void leftPressed(Address position) {
	}

	protected void leftPressedShift(Address position) {
	}

	protected void leftClicked(Address position) {
	}

	protected void leftClickedShift(Address position) {
	}
	/**
	 * 左ドラッグしたままた新しいマスに移動したときに呼ばれる．
	 * 必要に応じてサブクラスで操作をオーバーライドする．
	 * @param position
	 */
	protected void leftDragged(Address position) {
		// leftPressed(position);
	}

	protected void leftDragged(Address oldPos, Address position) {
		leftDragged(position);
	}

	/**
	 * 左マウスボタンを離して左ドラッグが確定したときに呼ばれる。
	 * サブクラスでオーバーライドする。
	 * @param dragEnd
	 */
	protected void leftDragFixed(Address dragEnd) {
	}
	/**
	 * 右ボタンが押されたとき，右ドラッグしたままた新しいマスに移動したときに呼ばれる。
	 * サブクラスで操作をオーバーライドする。
	 * @param position
	 */
	protected void rightPressed(Address position) {
	}

	protected void rightClicked(Address position) {
	}
	/**
	 * 右ドラッグしたままた新しいマスに移動したときに呼ばれる。 
	 * サブクラスで操作をオーバーライドする．
	 * @param position
	 */
	protected void rightDragged(Address position) {
		// rightPressed(position);
	}

	protected void rightDragged(Address oldPos, Address position) {
		rightDragged(position);
	}
	/**
	 * 右マウスボタンを離して右ドラッグが確定したときに呼ばれる。
	 * サブクラスでオーバーライドする。
	 * @param dragEnd
	 */
	protected void rightDragFixed(Address dragEnd) {
	}
	/**
	 * 盤内でドラッグを開始したが盤外でドラッグを終了したときに呼ばれる。
	 * 必要に応じサブクラスでドラッグの後始末をする。
	 */
	protected void dragFailed() {
	}

	/**
	 * マウス操作でカーソルを移動する。
	 *@param pos
	 */
	protected void moveCursor(Address position) {
		getCellCursor().setPosition(position);
		resetPreviousInput();
	}

	/**
	 * SL, MS 用
	 * 辺の操作を行うパズル用のマウスリスナーの共通スーパークラス。
	 */
	public class MouseHandlerEdge implements MouseListener {

		private SideAddress position = new SideAddress();

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {

			int x = e.getX();
			int y = e.getY();

			if ((((y - getOffsety()) - (x - getOffsetx()) + board.cols()
					* getCellSize() * 2)
					/ getCellSize() + ((y - getOffsety()) + (x - getOffsetx()))
					/ getCellSize()) % 2 == 0) {
				position
						.set(Direction.VERT, toR(y), toC(x - getHalfCellSize())); // 縦の辺上
			} else {
				position.set(Direction.HORIZ, toR(y - getHalfCellSize()),
						toC(x)); // 横の辺上
			}
			if (!isSideOn(position))
				return;
			int modifier = e.getModifiers();
			if ((modifier & InputEvent.BUTTON1_MASK) != 0) {
				if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0)
					leftClickedShiftEdge(position);
				else
					leftClickedEdge(position);
			} else if ((modifier & InputEvent.BUTTON3_MASK) != 0) {
				rightClickedEdge(position);
			}
			repaint();
		}
	}

	protected void leftClickedEdge(SideAddress position) {
	}

	protected void leftClickedShiftEdge(SideAddress position) {
	}

	protected void rightClickedEdge(SideAddress position) {
	}

}
