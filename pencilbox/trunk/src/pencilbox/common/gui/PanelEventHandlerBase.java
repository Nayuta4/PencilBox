package pencilbox.common.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.resource.Messages;

/**
 * パネルに対するマウス，キーボードのイベント処理を行うクラス
 */
public class PanelEventHandlerBase implements KeyListener, MouseListener, MouseMotionListener {

	private PanelBase panel;
	private BoardBase board;

	private int maxInputNumber = 99;
	private int previousInput = 0;
	private int symmetricPlacementMode = 0;
	private int immediateAnswerCheckMode = -1; // -1:OFF, 0:ON, 1:ALREADY_CHECKED

	private Address oldPos = new Address(-1, -1);
	private Address newPos = new Address(-1, -1);
	private SideAddress sidePos = new SideAddress();

	/**
	 * PanelEventHandlerを生成する
	 */
	public PanelEventHandlerBase() {
	}

	public void setup(PanelBase panel, BoardBase board) {
		this.panel = panel;
		this.board = board;
		setBoard(board);
		resetPreviousInput();
		resetImmediateAnswerCheckMode();
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
	 * @param b the symmetricPlacementMode to set
	 */
	public void setSymmetricPlacementMode(boolean b) {
		this.symmetricPlacementMode = b ? 1 : 0;
	}

	/**
	 * @return the immediateAnswerCheckMode
	 */
	public boolean isImmediateAnswerCheckMode() {
		return this.immediateAnswerCheckMode >= 0 ? true : false;
	}
	/**
	 * @param b the immediateAnswerCheckMode to set
	 */
	public void setImmediateAnswerCheckMode(boolean b) {
		this.immediateAnswerCheckMode = b ? 0 : -1;
	}
	/**
	 * 即時正解判定モードの場合に，正解済み状態から未正解状態に戻す。
	 */
	public void resetImmediateAnswerCheckMode() {
		if (immediateAnswerCheckMode == 1)
			immediateAnswerCheckMode = 0;
	}
	/**
	 * 入力可能な最大数字を取得する。
	 */
	protected int getMaxInputNumber() {
		return maxInputNumber;
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

	public boolean isProblemEditMode() {
		return panel.getEditMode() == PanelBase.PROBLEM_INPUT_MODE;
	}

	public CellCursor getCellCursor() {
		return panel.getCellCursor();
	}

	public boolean isCursorOn() {
		return panel.isCursorMode();
	}

	public void repaint() {
		panel.repaint();
	}

	public boolean isOn(Address position) {
		return board.isOn(position);
	}

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
	 * 点対称位置の座標を取得する。
	 * @param pos　元座標
	 * @return posと点対称な位置の座標
	 */
	public Address getSymmetricPosition(Address pos) {
		return new Address(board.rows()-1-pos.r(), board.cols()-1-pos.c());
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
			plusKeyEntered();
			break;
		case KeyEvent.VK_COLON:
		case KeyEvent.VK_MULTIPLY:
			starKeyEntered();
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
		checkAnswer();
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
		int maxInput = getMaxInputNumber();
		if (previousInput * 10 + number <= maxInput) {
			number = previousInput * 10 + number;
		}
		if (number <= maxInput) {
			Address pos = getCellCursor().getPosition();
			numberEntered(pos, number);
			previousInput = number;
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
		resetPreviousInput();
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
	 * プラスキーの入力を処理する。
	 * 各サブクラスで実装する。
	 * @param pos 入力マスの座標
	 */
	protected void plusEntered(Address pos) {
	}

	protected void plusKeyEntered() {
		Address pos = getCellCursor().getPosition();
		plusEntered(pos);
	}

	/**
	 * アスタリスクキーの入力を処理する。
	 * 各サブクラスで実装する。
	 * @param pos 入力マスの座標
	 */
	protected void starEntered(Address pos) {
	}

	protected void starKeyEntered() {
		Address pos = getCellCursor().getPosition();
		starEntered(pos);
	}

	/**
	 * スラッシュキーの入力を処理する。
	 * 「問題入力モード」と「解答モード」を切り替える
	 */
	protected void slashKeyEntered() {
		if (isProblemEditMode()) {
			board.initBoard();
			panel.setEditMode(PanelBase.ANSWER_INPUT_MODE);
		} else {
			panel.setEditMode(PanelBase.PROBLEM_INPUT_MODE);
		}
		resetPreviousInput();
		resetImmediateAnswerCheckMode();
	}

	/**
	 * マウスイベントから押されたボタンの番号を取得する補助メソッド
	 * シフトキーで左右のボタンを入れ替える
	 * @param e
	 * @return 左ボタンなら 1 右ボタンなら 3 中ボタンなら 2 それ以外は -1
	 */
	public int getMouseButton(MouseEvent e) {
		int modifier = e.getModifiers();
		if ((modifier & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
			if (e.isShiftDown())
				return 3;
			else
				return 1;
		} else if ((modifier & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
			if (e.isShiftDown())
				return 1;
			else
				return 3;
		} else if ((modifier & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK) {
			return 2;
		} else {
			return -1;
		}
	}
	/*
	 * マウスリスナー
	 */
	public void mousePressed(MouseEvent e) {
		mousePressed2(e); // 辺の操作
		newPos.set(panel.pointToAddress(e.getX(), e.getY()));
		if (!isOn(newPos))
			return;
		int button = getMouseButton(e);
		if (button == 1) {
			leftPressed(newPos);
		} else if (button == 3) {
			rightPressed(newPos);
		}
		moveCursor(newPos);
		oldPos.set(newPos); // 現在位置を更新
		repaint();
	}

	/*
	 * 辺の操作用。 SL, MS で使用
	 */
	private void mousePressed2(MouseEvent e) {
		sidePos.set(panel.pointToSideAddress(e.getX(), e.getY()));
		if (!isSideOn(sidePos))
			return;
		int button = getMouseButton(e);
		if (button == 1) {
			leftPressedEdge(sidePos);
		} else if (button == 3) {
			rightPressedEdge(sidePos);
		}
	}

	public void mouseDragged(MouseEvent e) {
		newPos.set(panel.pointToAddress(e.getX(), e.getY()));
		if (!isOn(newPos)) {
			oldPos.setNowhere();
			return;
		}
		if (newPos.equals(oldPos))
			return; // 同じマス内に止まるイベントは無視
		// if (!newPos.isNextTo(oldPos)) return; // 隣接マス以外のイベントは無視
		// if (dragIneffective(oldPos, newPos)) return; // 隣接マス以外のイベントは無視
		int button = getMouseButton(e);
		if (button == 1) {
			leftDragged(oldPos, newPos);
		} else if (button == 3) {
			rightDragged(oldPos, newPos);
		}
		moveCursor(newPos);
		oldPos.set(newPos); // 現在位置を更新
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		int button = getMouseButton(e);
		if (button == 1) {
			leftReleased(oldPos);
		} else if (button == 3) {
			rightReleased(oldPos);
		}
		repaint();
		checkAnswer();
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		mouseClicked1(e);
//		mouseClicked2(e);
		repaint();
		checkAnswer();
	}

	public void mouseClicked1(MouseEvent e) {
		if (!isOn(newPos))
			return;
		int button = getMouseButton(e);
		if (button == 1) {
			leftClicked(newPos);
		} else if (button == 3) {
			rightClicked(newPos);
//		} else if (button == 2) {
//			slashKeyEntered();
		}
	}
	/**
	 * 辺の位置のクリック操作を処理する。
	 * @param e
	 */
//	public void mouseClicked2(MouseEvent e) {
//		sidePos.set(panel.pointToSideAddress(e.getX(), e.getY()));
//		if (!isSideOn(sidePos))
//			return;
//		int modifier = e.getModifiers();
//		if ((modifier & InputEvent.BUTTON1_MASK) != 0) {
//			if (e.isShiftDown())
//				rightClickedEdge(sidePos);
//			else
//				leftClickedEdge(sidePos);
//		} else if ((modifier & InputEvent.BUTTON3_MASK) != 0) {
//			if (e.isShiftDown())
//				leftClickedEdge(sidePos);
//			else
//				rightClickedEdge(sidePos);
//		}
//	}

	public void mouseMoved(MouseEvent e) {
		// movePos.set(panel.pointToAddress(e));
		// if (!isOn(movePos))
		// return;
		// mouseMovedTo(movePos);
		// repaint();
	}

	/**
	 * 左ボタンが押されたときに呼ばれる。
	 * サブクラスで操作をオーバーライドする．
	 * @param position
	 */
	protected void leftPressed(Address position) {
	}

	protected void leftClicked(Address position) {
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
	 * 左マウスボタンを離したときに呼ばれる。
	 * サブクラスでオーバーライドする。
	 * @param position
	 */
	protected void leftReleased(Address position) {
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
	 * 右マウスボタンを離したときに呼ばれる。
	 * サブクラスでオーバーライドする。
	 * @param position
	 */
	protected void rightReleased(Address position) {
	}

	/**
	 * マウス操作でカーソルを移動する。
	 * @param position
	 */
	protected void moveCursor(Address position) {
		getCellCursor().setPosition(position);
		resetPreviousInput();
	}
//
//	/**
//	 * 辺の位置を左クリックしたときの動作を定める。
//	 * @param position
//	 */
//	protected void leftClickedEdge(SideAddress position) {
//	}
//
//	/**
//	 * 辺の位置を右クリックしたときの動作を定める。
//	 * @param position
//	 */
//	protected void rightClickedEdge(SideAddress position) {
//	}

	/**
	 * 辺の位置を左クリックしたときの動作を定める。
	 * @param position
	 */
	protected void leftPressedEdge(SideAddress position) {
	}

	/**
	 * 辺の位置を右クリックしたときの動作を定める。
	 * @param position
	 */
	protected void rightPressedEdge(SideAddress position) {
	}

	/**
	 * 	即時正解判定
	 */
	public void checkAnswer() {
		if (isProblemEditMode())
			return;
		if (immediateAnswerCheckMode == 0) {
			if (board.checkAnswerCode() == 0) {
				JOptionPane.showMessageDialog(panel, BoardBase.COMPLETE_MESSAGE,
						Messages.getString("PanelEventHandlerBase.checkAnswerDialog"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				immediateAnswerCheckMode = 1;
			}
		}
	}

}
