package pencilbox.common.gui;

import java.awt.event.*;

import pencilbox.common.core.*;

/**
 * �p�l���ɑ΂���}�E�X�C�L�[�{�[�h�̃C�x���g�������s���N���X
 */
public class PanelEventHandler extends PanelBase {

	private KeyHandler keyHandler = new KeyHandler();
	protected MouseHandlerCursor mouseHandlerCursor = new MouseHandlerCursor();
	private MouseHandler mouseHandler = new MouseHandler();
	private MouseHandlerEdge mouseHandlerEdge = new MouseHandlerEdge();

	private int maxInputNumber = 99;
	private int previousInput = 0;
	
	/**
	 * Panel�𐶐�����
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
	 * mouseHandlerCorsor �� mouseListener���X�g���珜��
	 * SL, TS �ł́@���ƍ��W�n���قȂ邽�߁CmouseHandlerCursor ���O���Ă���
	 */
	protected void removeMouseHandlerCursor() {
		removeMouseListener(mouseHandlerCursor);
	}
	/**
	 *  �J�[�\���𐶐�����
	 * @return ���������J�[�\��
	 */
	public CellCursor createCursor() {
		return new CellCursor(this);
	}

	public void setup(BoardBase board) {
		super.setup(board);
		getCellCursor().setPosition(0,0);
	}
	/**
	 * �Ֆʕ\���̉�]��ݒ肷��
	 * @param rotation �ݒ肷���]���
	 */
	protected void setRotation(int rotation) {
		getCellCursor().setPosition(0,0);
		super.setRotation(rotation);
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
	 * �L�[���X�i�[
	 */
	public class KeyHandler implements KeyListener {

		private Address position = new Address();

		/**
		 * �L�[�{�[�h���͏���
		 * 0-9 �̐����L�[���^�C�v���ꂽ�Ƃ��ɂ͓��͐������󂯂Ƃ�C
		 * �󋵂ɉ����āC2���̐����ɂ��� numberEntered ���\�b�h�ɓn��
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
					keyChar >= '0' && keyChar <= '9') { // 0 - 9 �̃L�[�����͂��ꂽ�Ƃ��̂ݖ��ɂ��Ă���
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
	 * �����L�[����͂����Ƃ��ɌĂ΂��
	 * �����̓p�Y���̎�ނ��ƂɃT�u�N���X�ŋL�q����
	 * @param pos ��������͂����}�X�̍��W
	 * @param num ���͂�������
	 */
	protected void numberEntered(Address pos, int num) {
	}
	/**
	 * �X�y�[�X�L�[����͂����Ƃ��ɌĂ΂��
	 * �����̓p�Y���̎�ނ��ƂɃT�u�N���X�ŋL�q����
	 * @param pos ��������͂����}�X�̍��W
	 */
	protected void spaceEntered(Address pos) {
	}
	/**
	 * '-'�L�[����͂����Ƃ��ɌĂ΂��
	 * �����̓p�Y���̎�ނ��ƂɃT�u�N���X�ŋL�q����
	 * @param pos ��������͂����}�X�̍��W
	 */
	protected void minusEntered(Address pos) {
	}
	/**
	 * '/'�L�[����͂����Ƃ��ɌĂ΂��
	 * �u�����̓��[�h�v�Ɓu�𓚃��[�h�v��؂�ւ���
	 */
	protected void slashEntered() {
		setProblemEditMode(!isProblemEditMode());
	}

	/**
	 * �}�E�X���X�i�[�̋��ʃX�[�p�[�N���X
	 * �}�E�X���������Ƃ��C
	 * �h���b�O�����܂ܐV�����}�X�Ɉړ������Ƃ��C
	 * �h���b�O�I�������Ƃ��C�̓�����C
	 * �T�u�N���X�ŃI�[�o�[���C�h���ėp����
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
			p2b(newPos);

			if (newPos.equals(oldPos))
				return; // �����}�X���Ɏ~�܂�C�x���g�͖���
			//			if (!newPos.isNextTo(oldPos)) return; // �אڃ}�X�ȊO�̃C�x���g�͖���
			//			if (dragIneffective(oldPos, newPos)) return; // �אڃ}�X�ȊO�̃C�x���g�͖���

			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				leftDragged(oldPos, newPos);
			} else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				rightDragged(oldPos, newPos);
			}

			oldPos.set(newPos); // ���݈ʒu���X�V
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
	 * ���{�^���������ꂽ�Ƃ��C
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
	 * �I�[�o�[���C�h���Ȃ���΁CleftPressed �Ɠ�������
	 * @param position
	 */
	protected void leftDragged(Address position) {
//		leftPressed(position);
	}
	protected void leftDragged(Address oldPos, Address position) {
		leftDragged(position);
	}

	/**
	 * ���}�E�X�{�^���𗣂��č��h���b�O���m�肵���Ƃ��ɌĂ΂��
	 * �T�u�N���X�ŃI�[�o�[���C�h����
	 * @param dragEnd
	 */
	protected void leftDragFixed(Address dragEnd) {
	}
	/**
	 * �E�{�^���������ꂽ�Ƃ��C�E�h���b�O�����܂܂��V�����}�X�Ɉړ������Ƃ��ɌĂ΂��D
	 * �T�u�N���X�ő�����I�[�o�[���C�h����D
	 * @param position
	 */
	protected void rightPressed(Address position) {
	}
	protected void rightClicked(Address position) {
	}
	/**
	 * �E�h���b�O�����܂܂��V�����}�X�Ɉړ������Ƃ��ɌĂ΂��D
	 * �T�u�N���X�ő�����I�[�o�[���C�h����D
	 * �I�[�o�[���C�h���Ȃ���΁CrightPressed �Ɠ�������
	 * @param position
	 */
	protected void rightDragged(Address position) {
//		rightPressed(position);
	}
	protected void rightDragged(Address oldPos, Address position) {
		rightDragged(position);
	}
	/**
	 * �E�}�E�X�{�^���𗣂��ĉE�h���b�O���m�肵���Ƃ��ɌĂ΂��
	 * �T�u�N���X�ŃI�[�o�[���C�h����
	 * @param dragEnd
	 */
	protected void rightDragFixed(Address dragEnd) {
	}
	/**
	 * �Փ��Ńh���b�O���J�n�������ՊO�Ńh���b�O���I�������Ƃ��ɌĂ΂��D
	 * �K�v�ɉ����T�u�N���X�Ńh���b�O�̌�n��������
	 */
	protected void dragFailed() {
	}

	protected void mouseMovedTo(Address pos){}

	/**
	 * �ӂ̑�����s���p�Y���p�̃}�E�X���X�i�[�̋��ʃX�[�p�[�N���X
	 * �ӂ��N���b�N�����Ƃ��̓�����T�u�N���X�ŃI�[�o�[���C�h���Ďg�p����
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
				position.set(Direction.VERT, toR(y), toC(x - getHalfCellSize())); // �c�̕ӏ�
			} else {
				position.set(Direction.HORIZ, toR(y - getHalfCellSize()), toC(x)); // ���̕ӏ�
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
	 * �J�[�\������p�̃}�E�X���X�i�[
	 */
	public class MouseHandlerCursor implements MouseListener {
		/* 
		 * �J�[�\�����ړ�����
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
