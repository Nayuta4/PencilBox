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
 * �p�l���ɑ΂���}�E�X�C�L�[�{�[�h�̃C�x���g�������s���N���X
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
	 * PanelEventHandler�𐶐�����
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
	 * �������𔻒胂�[�h�̏ꍇ�ɁC�����ςݏ�Ԃ��疢������Ԃɖ߂��B
	 */
	public void resetImmediateAnswerCheckMode() {
		if (immediateAnswerCheckMode == 1)
			immediateAnswerCheckMode = 0;
	}
	/**
	 * ���͉\�ȍő吔�����擾����B
	 */
	protected int getMaxInputNumber() {
		return maxInputNumber;
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
	 * �_�Ώ̈ʒu�̍��W���擾����B
	 * @param pos�@�����W
	 * @return pos�Ɠ_�Ώ̂Ȉʒu�̍��W
	 */
	public Address getSymmetricPosition(Address pos) {
		return new Address(board.rows()-1-pos.r(), board.cols()-1-pos.c());
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
		resetPreviousInput();
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
	 * �v���X�L�[�̓��͂���������B
	 * �e�T�u�N���X�Ŏ�������B
	 * @param pos ���̓}�X�̍��W
	 */
	protected void plusEntered(Address pos) {
	}

	protected void plusKeyEntered() {
		Address pos = getCellCursor().getPosition();
		plusEntered(pos);
	}

	/**
	 * �A�X�^���X�N�L�[�̓��͂���������B
	 * �e�T�u�N���X�Ŏ�������B
	 * @param pos ���̓}�X�̍��W
	 */
	protected void starEntered(Address pos) {
	}

	protected void starKeyEntered() {
		Address pos = getCellCursor().getPosition();
		starEntered(pos);
	}

	/**
	 * �X���b�V���L�[�̓��͂���������B
	 * �u�����̓��[�h�v�Ɓu�𓚃��[�h�v��؂�ւ���
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
	 * �}�E�X�C�x���g���牟���ꂽ�{�^���̔ԍ����擾����⏕���\�b�h
	 * �V�t�g�L�[�ō��E�̃{�^�������ւ���
	 * @param e
	 * @return ���{�^���Ȃ� 1 �E�{�^���Ȃ� 3 ���{�^���Ȃ� 2 ����ȊO�� -1
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
	 * �}�E�X���X�i�[
	 */
	public void mousePressed(MouseEvent e) {
		mousePressed2(e); // �ӂ̑���
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
		oldPos.set(newPos); // ���݈ʒu���X�V
		repaint();
	}

	/*
	 * �ӂ̑���p�B SL, MS �Ŏg�p
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
			return; // �����}�X���Ɏ~�܂�C�x���g�͖���
		// if (!newPos.isNextTo(oldPos)) return; // �אڃ}�X�ȊO�̃C�x���g�͖���
		// if (dragIneffective(oldPos, newPos)) return; // �אڃ}�X�ȊO�̃C�x���g�͖���
		int button = getMouseButton(e);
		if (button == 1) {
			leftDragged(oldPos, newPos);
		} else if (button == 3) {
			rightDragged(oldPos, newPos);
		}
		moveCursor(newPos);
		oldPos.set(newPos); // ���݈ʒu���X�V
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
	 * �ӂ̈ʒu�̃N���b�N�������������B
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
	 * ���{�^���������ꂽ�Ƃ��ɌĂ΂��B
	 * �T�u�N���X�ő�����I�[�o�[���C�h����D
	 * @param position
	 */
	protected void leftPressed(Address position) {
	}

	protected void leftClicked(Address position) {
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
	 * ���}�E�X�{�^���𗣂����Ƃ��ɌĂ΂��B
	 * �T�u�N���X�ŃI�[�o�[���C�h����B
	 * @param position
	 */
	protected void leftReleased(Address position) {
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
	 * �E�}�E�X�{�^���𗣂����Ƃ��ɌĂ΂��B
	 * �T�u�N���X�ŃI�[�o�[���C�h����B
	 * @param position
	 */
	protected void rightReleased(Address position) {
	}

	/**
	 * �}�E�X����ŃJ�[�\�����ړ�����B
	 * @param position
	 */
	protected void moveCursor(Address position) {
		getCellCursor().setPosition(position);
		resetPreviousInput();
	}
//
//	/**
//	 * �ӂ̈ʒu�����N���b�N�����Ƃ��̓�����߂�B
//	 * @param position
//	 */
//	protected void leftClickedEdge(SideAddress position) {
//	}
//
//	/**
//	 * �ӂ̈ʒu���E�N���b�N�����Ƃ��̓�����߂�B
//	 * @param position
//	 */
//	protected void rightClickedEdge(SideAddress position) {
//	}

	/**
	 * �ӂ̈ʒu�����N���b�N�����Ƃ��̓�����߂�B
	 * @param position
	 */
	protected void leftPressedEdge(SideAddress position) {
	}

	/**
	 * �ӂ̈ʒu���E�N���b�N�����Ƃ��̓�����߂�B
	 * @param position
	 */
	protected void rightPressedEdge(SideAddress position) {
	}

	/**
	 * 	�������𔻒�
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
