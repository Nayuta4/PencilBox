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
 * �p�l���ɑ΂���}�E�X�C�L�[�{�[�h�̃C�x���g�������s���N���X
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
	 * PanelEventHandler�𐶐�����
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
	 * �ʃN���X�̃p�l���ɌʃN���X�̔Ֆʂ�ݒ肷�邽�߂̃��\�b�h
	 * �e�ʃN���X�ŃI�[�o�[���C�h����
	 * @param board �Ֆ�
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
	 * ���͉\�ȍő吔����ݒ肷��
	 * @param number �ݒ肷�鐔�l
	 */
	protected void setMaxInputNumber(int number) {
		maxInputNumber = number;
	}
	/**
	 * ���͐����̈ꎞ�L�����N���A����
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
	 * �J�[�\�����W���Ֆʏ�ɂ��邩�B
	 * �ʏ�� #isOn(Address) �Ɠ������ʂ�Ԃ��B
	 * SL, TS ���J�[�\���̍��W�n���قȂ�^�C�v�ł́C�T�u�N���X�ōĒ�`����B
	 * @param position �J�[�\�����W
	 * @return �J�[�\�����W���Ֆʏ�ɂ���� true
	 */
	public boolean isCursorOnBoard(Address position) {
		return board.isOn(position);
	}

	/**
	 * Panel���x�����s�N�Z�����W��Panel��̗�����}�X���W�ɕϊ�����
	 * @param x Panel��̃s�N�Z�����W��x
	 * @return x��Panel��������W�ɕϊ��������l
	 */
	public final int toC(int x) {
		return (x + getCellSize() - getOffsetx()) / getCellSize() - 1;
	}
	/**
	 * Panel��̂����s�N�Z�����W��Panel��̍s�����}�X���W�ɕϊ�����
	 * @param y Panel��̃s�N�Z�����W��y
	 * @return y��Panel��������W�ɕϊ��������l
	 */
	public final int toR(int y) {
		return (y + getCellSize() - getOffsety()) / getCellSize() - 1;
	}

	/**
	 * �_�Ώ̈ʒu�̍��W���擾����B
	 * @param pos�@�����W
	 * @return pos�Ɠ_�Ώ̂Ȉʒu�̍��W
	 */
	public Address getSymmetricPosition(Address pos) {
		return new Address(board.rows()-1-pos.r(), board.cols()-1-pos.c());
	}

	/**
	 * �L�[���X�i�[
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
	 * ���L�[���͂���������B ���̕����ɃJ�[�\�����ړ�����B
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
	 * �����L�[���͂���������B 
	 * 0-9 �̐����L�[�����͂��ꂽ�Ƃ��ɁC�󋵂ɉ�����2���̐����ɂ��� numberEntered���\�b�h�ɓn��
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
	 * �������͂���������B
	 * �e�T�u�N���X�Ŏ�������B
	 * @param pos ��������͂����}�X�̍��W
	 * @param num ���͂�������
	 */
	protected void numberEntered(Address pos, int num) {
	}
	/**
	 * �s���I�h�L�[�̓��͂���������B
	 * �e�T�u�N���X�Ŏ�������B
	 * @param pos ���̓}�X�̍��W
	 */
	protected void spaceEntered(Address pos) {
	}

	protected void spaceKeyEntered() {
		Address pos = getCellCursor().getPosition();
		spaceEntered(pos);
	}

	/**
	 * �}�C�i�X�L�[�̓��͂���������B
	 * �e�T�u�N���X�Ŏ�������B
	 * @param pos ���̓}�X�̍��W
	 */
	protected void minusEntered(Address pos) {
	}

	protected void minusKeyEntered() {
		Address pos = getCellCursor().getPosition();
		minusEntered(pos);
	}

	/**
	 * �X���b�V���L�[�̓��͂���������B
	 * �u�����̓��[�h�v�Ɓu�𓚃��[�h�v��؂�ւ���
	 */
	protected void slashKeyEntered() {
		if (isProblemEditMode())
			board.initBoard();
		setProblemEditMode(!isProblemEditMode());
	}

	/**
	 * �}�E�X���X�i�[�̋��ʃX�[�p�[�N���X
	 * �}�E�X���������Ƃ��C�h���b�O�����܂ܐV�����}�X�Ɉړ������Ƃ��C�h���b�O�I�������Ƃ��̓�����`���Ă���B
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

			oldPos.set(newPos); // ���݈ʒu���X�V
			repaint();
		}

		public void mouseDragged(MouseEvent e) {

			newPos.set(toR(e.getY()), toC(e.getX()));
			if (!isOn(newPos)) {
				oldPos.setNowhere();
				// ���̕������Ȃ��ƁC�ՊO���o�R�����h���b�O������������Ȃ� �����Ă��Ȃ��Ă�����
				return;
			}

			if (newPos.equals(oldPos))
				return; // �����}�X���Ɏ~�܂�C�x���g�͖���
			// if (!newPos.isNextTo(oldPos)) return; // �אڃ}�X�ȊO�̃C�x���g�͖���
			// if (dragIneffective(oldPos, newPos)) return; // �אڃ}�X�ȊO�̃C�x���g�͖���

			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				leftDragged(oldPos, newPos);
			} else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				rightDragged(oldPos, newPos);
			}
			moveCursor(newPos);
			oldPos.set(newPos); // ���݈ʒu���X�V
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
	 * ���{�^���������ꂽ�Ƃ��ɌĂ΂��B
	 * �T�u�N���X�ő�����I�[�o�[���C�h����D
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
	 * ���h���b�O�����܂܂��V�����}�X�Ɉړ������Ƃ��ɌĂ΂��D
	 * �K�v�ɉ����ăT�u�N���X�ő�����I�[�o�[���C�h����D
	 * @param position
	 */
	protected void leftDragged(Address position) {
		// leftPressed(position);
	}

	protected void leftDragged(Address oldPos, Address position) {
		leftDragged(position);
	}

	/**
	 * ���}�E�X�{�^���𗣂��č��h���b�O���m�肵���Ƃ��ɌĂ΂��B
	 * �T�u�N���X�ŃI�[�o�[���C�h����B
	 * @param dragEnd
	 */
	protected void leftDragFixed(Address dragEnd) {
	}
	/**
	 * �E�{�^���������ꂽ�Ƃ��C�E�h���b�O�����܂܂��V�����}�X�Ɉړ������Ƃ��ɌĂ΂��B
	 * �T�u�N���X�ő�����I�[�o�[���C�h����B
	 * @param position
	 */
	protected void rightPressed(Address position) {
	}

	protected void rightClicked(Address position) {
	}
	/**
	 * �E�h���b�O�����܂܂��V�����}�X�Ɉړ������Ƃ��ɌĂ΂��B 
	 * �T�u�N���X�ő�����I�[�o�[���C�h����D
	 * @param position
	 */
	protected void rightDragged(Address position) {
		// rightPressed(position);
	}

	protected void rightDragged(Address oldPos, Address position) {
		rightDragged(position);
	}
	/**
	 * �E�}�E�X�{�^���𗣂��ĉE�h���b�O���m�肵���Ƃ��ɌĂ΂��B
	 * �T�u�N���X�ŃI�[�o�[���C�h����B
	 * @param dragEnd
	 */
	protected void rightDragFixed(Address dragEnd) {
	}
	/**
	 * �Փ��Ńh���b�O���J�n�������ՊO�Ńh���b�O���I�������Ƃ��ɌĂ΂��B
	 * �K�v�ɉ����T�u�N���X�Ńh���b�O�̌�n��������B
	 */
	protected void dragFailed() {
	}

	/**
	 * �}�E�X����ŃJ�[�\�����ړ�����B
	 *@param pos
	 */
	protected void moveCursor(Address position) {
		getCellCursor().setPosition(position);
		resetPreviousInput();
	}

	/**
	 * SL, MS �p
	 * �ӂ̑�����s���p�Y���p�̃}�E�X���X�i�[�̋��ʃX�[�p�[�N���X�B
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
						.set(Direction.VERT, toR(y), toC(x - getHalfCellSize())); // �c�̕ӏ�
			} else {
				position.set(Direction.HORIZ, toR(y - getHalfCellSize()),
						toC(x)); // ���̕ӏ�
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
