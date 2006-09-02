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
//	private BoardBase board;

	private KeyHandler keyHandler = new KeyHandler();
	private MouseHandler mouseHandler = new MouseHandler();
	private MouseHandlerEdge mouseHandlerEdge = new MouseHandlerEdge();

	private int maxInputNumber = 99;
	private int previousInput = 0;
	
	/**
	 * Panelを生成する
	 */
	public PanelEventHandlerBase() {
	}
	
	public void setup(PanelBase panel, BoardBase board) {
		this.panel = panel;
		setBoard(board);
		panel.addKeyListener(keyHandler);
		panel.addMouseListener(mouseHandler);
		panel.addMouseMotionListener(mouseHandler);
		panel.addMouseListener(mouseHandlerEdge);
		panel.setCellCursor(createCursor());
	}
	
	public void setup(BoardBase board) {
		setBoard(board);
		getCellCursor().setPosition(0,0);
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
	 *  カーソルを生成する
	 * @return 生成したカーソル
	 */
	public CellCursor createCursor() {
		return new CellCursor(this);
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
	
	public boolean isOn(int r, int c) {
		return panel.isOn(r, c);
	}
	
	public boolean isOn(int r, int c, int a, int b) {
		return panel.isOn(r, c, a, b);
	}
	
	public boolean isOn(Address position) {
		return panel.isOn(position);
	}
	
	public boolean isSideOn(SideAddress position) {
		return panel.isSideOn(position);
	}
	
	public int rows() {
		return panel.rows();
	}
	
	public int cols() {
		return panel.cols();
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
	 * キーリスナー
	 */
	public class KeyHandler implements KeyListener {

		private Address position = new Address();

		/**
		 * キーボード入力処理
		 * 0-9 の数字キーがタイプされたときには入力数字を受けとり，
		 * 状況に応じて，2桁の数字にして numberEntered メソッドに渡す
		 */
		public void keyPressed(KeyEvent e) {
			int keyChar = e.getKeyChar();
			if (keyChar == '/')
				slashEntered();
			if (isProblemEditMode() || isCursorOn()) {
				moveCursor(e);
				position.set(getCellCursor().getPosition());
				if (keyChar == ' ') {
					spaceEntered(position);
				} else 	if (keyChar == '.') {
					spaceEntered(position);
				} else 	if (keyChar == '-') {
					minusEntered(position);
				} else if (
					keyChar >= '0' && keyChar <= '9') { // 0 - 9 のキーが入力されたときのみ問題にしている
					int number = keyChar - '0';
					if (previousInput >= 1 && previousInput <= 9) {
	//					&& position.equals(previousPosition)) {
						if (previousInput * 10 + number <= maxInputNumber) {
							number = previousInput * 10 + number;
						}
						if (number <= maxInputNumber) {
							numberEntered(position, number);
							previousInput = 0;
						}
					} else {
						if (number <= maxInputNumber) {
							numberEntered(position, number);
							previousInput = number;
						}
					}
				}
			}
			repaint();
		}
		private void moveCursor(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
				case KeyEvent.VK_LEFT :
						getCellCursor().moveLt();
					break;
				case KeyEvent.VK_UP :
						getCellCursor().moveUp();
					break;
				case KeyEvent.VK_RIGHT :
						getCellCursor().moveRt();
					break;
				case KeyEvent.VK_DOWN :
						getCellCursor().moveDn();
					break;
				default :
					break;
			}
		}

		public void keyTyped(KeyEvent e) {
		}
		public void keyReleased(KeyEvent e) {
		}
	}
	/**
	 * 数字キーを入力したときに呼ばれる
	 * 処理はパズルの種類ごとにサブクラスで記述する
	 * @param pos 数字を入力したマスの座標
	 * @param num 入力した数字
	 */
	protected void numberEntered(Address pos, int num) {
	}
	/**
	 * スペースキーを入力したときに呼ばれる
	 * 処理はパズルの種類ごとにサブクラスで記述する
	 * @param pos 数字を入力したマスの座標
	 */
	protected void spaceEntered(Address pos) {
	}
	/**
	 * '-'キーを入力したときに呼ばれる
	 * 処理はパズルの種類ごとにサブクラスで記述する
	 * @param pos 数字を入力したマスの座標
	 */
	protected void minusEntered(Address pos) {
	}
	/**
	 * '/'キーを入力したときに呼ばれる
	 * 「問題入力モード」と「解答モード」を切り替える
	 */
	protected void slashEntered() {
		setProblemEditMode(!isProblemEditMode());
	}

	/**
	 * マウスリスナーの共通スーパークラス
	 * マウスを押したとき，
	 * ドラッグしたまま新しいマスに移動したとき，
	 * ドラッグ終了したとき，の動作を，
	 * サブクラスでオーバーライドして用いる
	 */
	public class MouseHandler
		implements MouseListener, MouseMotionListener {

		private Address oldPos = new Address(-1, -1);
		private Address newPos = new Address(-1, -1);

		public void mousePressed(MouseEvent e) {

			newPos.set(toR(e.getY()), toC(e.getX()));
			if (!isOn(newPos))
				return;
			int modifier = e.getModifiers();
			if ((modifier & InputEvent.BUTTON1_MASK) != 0) {
				if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0)
					leftPressedShift(newPos);
				else
					leftPressed(newPos);
			} else if ((modifier & InputEvent.BUTTON3_MASK) != 0) {
				rightPressed(newPos);
			}
			moveCursor(newPos);

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
			//			if (!newPos.isNextTo(oldPos)) return; // 隣接マス以外のイベントは無視
			//			if (dragIneffective(oldPos, newPos)) return; // 隣接マス以外のイベントは無視

			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				leftDragged(oldPos, newPos);
			} else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				rightDragged(oldPos, newPos);
			}

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
//			movePos.set(toR(e.getY()), toC(e.getX()));
//			if (!isOn(movePos))
//				return;
//			mouseMovedTo(movePos);
//			repaint();
		}
	}
	/**
	 * 左ボタンが押されたとき，
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
	 * オーバーライドしなければ，leftPressed と同じ動作
	 * @param position
	 */
	protected void leftDragged(Address position) {
//		leftPressed(position);
	}
	protected void leftDragged(Address oldPos, Address position) {
		leftDragged(position);
	}

	/**
	 * 左マウスボタンを離して左ドラッグが確定したときに呼ばれる
	 * サブクラスでオーバーライドする
	 * @param dragEnd
	 */
	protected void leftDragFixed(Address dragEnd) {
	}
	/**
	 * 右ボタンが押されたとき，右ドラッグしたままた新しいマスに移動したときに呼ばれる．
	 * サブクラスで操作をオーバーライドする．
	 * @param position
	 */
	protected void rightPressed(Address position) {
	}
	protected void rightClicked(Address position) {
	}
	/**
	 * 右ドラッグしたままた新しいマスに移動したときに呼ばれる．
	 * サブクラスで操作をオーバーライドする．
	 * オーバーライドしなければ，rightPressed と同じ動作
	 * @param position
	 */
	protected void rightDragged(Address position) {
//		rightPressed(position);
	}
	protected void rightDragged(Address oldPos, Address position) {
		rightDragged(position);
	}
	/**
	 * 右マウスボタンを離して右ドラッグが確定したときに呼ばれる
	 * サブクラスでオーバーライドする
	 * @param dragEnd
	 */
	protected void rightDragFixed(Address dragEnd) {
	}
	/**
	 * 盤内でドラッグを開始したが盤外でドラッグを終了したときに呼ばれる．
	 * 必要に応じサブクラスでドラッグの後始末をする
	 */
	protected void dragFailed() {
	}

	protected void moveCursor(Address pos) {
		getCellCursor().setPosition(pos);
	}

	/**
	 * SL, MS 用
	 * 辺の操作を行うパズル用のマウスリスナーの共通スーパークラス
	 * 辺をクリックしたときの動作をサブクラスでオーバーライドして使用する
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

			if ((((y - getOffsety()) - (x - getOffsetx()) + cols() * getCellSize() * 2) / getCellSize()
				+ ((y - getOffsety()) + (x - getOffsetx())) / getCellSize())
				% 2
				== 0) {
				position.set(Direction.VERT, toR(y), toC(x - getHalfCellSize())); // 縦の辺上
			} else {
				position.set(Direction.HORIZ, toR(y - getHalfCellSize()), toC(x)); // 横の辺上
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
