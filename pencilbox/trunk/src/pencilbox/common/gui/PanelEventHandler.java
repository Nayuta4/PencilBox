package pencilbox.common.gui;

import java.awt.event.*;

import pencilbox.common.core.*;

/**
 * パネルに対するマウス，キーボードのイベント処理を行うクラス
 */
public class PanelEventHandler extends PanelBase {

	private KeyHandler keyHandler = new KeyHandler();
	protected MouseHandlerCursor mouseHandlerCursor = new MouseHandlerCursor();
	private MouseHandler mouseHandler = new MouseHandler();
	private MouseHandlerEdge mouseHandlerEdge = new MouseHandlerEdge();

	private int maxInputNumber = 99;
	private int previousInput = 0;
	
	/**
	 * Panelを生成する
	 */
	public PanelEventHandler() {
		addKeyListener(keyHandler);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		addMouseListener(mouseHandlerEdge);
		addMouseListener(mouseHandlerCursor);
		setCellCursor(createCursor());
	}
	/**
	 * mouseHandlerCorsor を mouseListenerリストから除く
	 * SL, TS では　他と座標系が異なるため，mouseHandlerCursor を外している
	 */
	protected void removeMouseHandlerCursor() {
		removeMouseListener(mouseHandlerCursor);
	}
	/**
	 *  カーソルを生成する
	 * @return 生成したカーソル
	 */
	public CellCursor createCursor() {
		return new CellCursor(this);
	}

	public void setup(BoardBase board) {
		super.setup(board);
		getCellCursor().setPosition(0,0);
	}
	/**
	 * 盤面表示の回転を設定する
	 * @param rotation 設定する回転状態
	 */
	protected void setRotation(int rotation) {
		getCellCursor().setPosition(0,0);
		super.setRotation(rotation);
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
				position.set(getCellCursor().getBoardPosition());
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
//	private Address moveNewPos = new Address();
//	private Address moveOldPos = new Address();
//	private Address movePos= new Address();
	public class MouseHandler
		implements MouseListener, MouseMotionListener {

		private Address oldPos = new Address(-1, -1);
		private Address newPos = new Address(-1, -1);

		public void mousePressed(MouseEvent e) {

			newPos.set(toR(e.getY()), toC(e.getX()));
			if (!isOn(newPos))
				return;
			p2b(newPos);
			//		System.out.println(oldPos.toString() + newPos.toString());
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
			p2b(newPos);

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
			//		repaint(newPos);
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
////			p2b(movePos);
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

	protected void mouseMovedTo(Address pos){}

	/**
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
			p2bSide(position);
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

	/**
	 * カーソル操作用のマウスリスナー
	 */
	public class MouseHandlerCursor implements MouseListener {
		/* 
		 * カーソルを移動する
		 */
		public void mousePressed(MouseEvent e) {
			getCellCursor().setPosition(toR(e.getY()), toC(e.getX()));
			repaint();
		}
		public void mouseClicked(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
	}

}
