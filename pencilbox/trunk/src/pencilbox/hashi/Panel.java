package pencilbox.hashi;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandler;
import pencilbox.util.Colors;


/**
 * �u����������v�p�l���N���X
 */
public class Panel extends PanelEventHandler {

	static final int HORIZ = Direction.HORIZ;
	static final int VERT = Direction.VERT;
	static final int UP = Direction.UP;
	static final int DN = Direction.DN;
	static final int LT = Direction.LT;
	static final int RT = Direction.RT;

	private Board board;

	private boolean colorForEachLink = false;
	private boolean showNumberHint = false;

	private Color bridgeColor = Color.BLUE;
	private Color crossBridgeColor = Color.RED;
	private Color errorColor = Color.RED;
	private Color successColor = Color.GREEN;

	private Address pos0 = new Address();

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
		setMaxInputNumber(8);
		setDisplayStyle(2);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/**
	 * @return Returns the bridgeColor.
	 */
	public Color getBridgeColor() {
		return bridgeColor;
	}

	/**
	 * @param bridgeColor The bridgeColor to set.
	 */
	public void setBridgeColor(Color bridgeColor) {
		this.bridgeColor = bridgeColor;
	}

	/**
	 * @param colorForEachLink The colorForEachLink to set.
	 */
	public void setColorForEachLink(boolean colorForEachLink) {
		this.colorForEachLink = colorForEachLink;
	}

	/**
	 * @param showNumberHint The showNumberHint to set.
	 */
	public void setShowNumberHint(boolean showNumberHint) {
		this.showNumberHint = showNumberHint;
	}

	public void drawPanel(Graphics g) {
		paintBackground(g);
		drawIndex(g);
		if (getDisplayStyle() == 0)
			drawGrid(g); // �r���Ȃ��ł��悢��
		drawBoard(g);
		drawBorder(g);
		if (getCellCursor() != null) {
			drawCursor(g);
		}
	}
	/**
	 * �Ֆʂ�`�悷��
	 * @param g
	 */
	void drawBoard(Graphics g) {
		g.setFont(getNumberFont());
		g.setColor(getNumberColor());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.isPier(r, c)) {
					placeBridgeAndPier(g, r, c, board.getNumber(r, c));
				}
			}
		}
	}
	
	void placeBridgeAndPier(Graphics g, int r0, int c0, int n) {

		Pier pier = board.getPier(r0,c0);
		if (colorForEachLink)
			g.setColor(Colors.getColor(pier.getChain()));
		else
			g.setColor(bridgeColor);
		int r = r0;
		int c = c0;
		if (pier.getNBridge(DN) > 0) {
			while (!board.isPier(++r, c)) {
				placeMidlines(g, r, c, VERT, pier.getNBridge(DN));
			}
		}
		r = r0;
		c = c0;
		if (pier.getNBridge(RT) > 0) {
			while (!board.isPier(r, ++c)) {
				placeMidlines(g, r, c, HORIZ, pier.getNBridge(RT));
			}
		}
		placePier(g, r0, c0, n);
	}
	/**
	 * ���r��z�u����
	 * @param g
	 * @param r
	 * @param c
	 * @param n
	 */
	void placePier(Graphics g, int r, int c, int n) {
		if (showNumberHint) {
			if (board.checkPier(r, c) < 0) {
				g.setColor(errorColor);
				placeFilledCircle(g, r, c, getCellSize());
			} else if (board.checkPier(r, c) == 0) {
				g.setColor(successColor);
				placeFilledCircle(g, r, c, getCellSize());
			}
		}
		g.setColor(getNumberColor());
		placeCircle(g, r, c, getCellSize());
		if (n <= 8)
			placeNumber(g, r, c, n);
	}
	/**
	 * ����z�u����
	 * @param g
	 * @param r
	 * @param c
	 */
	void placeBridge(Graphics g, int r, int c) {
		int v = board.getVertBridge(r, c);
		int h = board.getHorizBridge(r, c);
		if (v > 0 && h > 0)
			g.setColor(crossBridgeColor);
		else
			g.setColor(bridgeColor);
		if (v > 0)
			placeMidlines(g, r, c, VERT, v);
		if (h > 0)
			placeMidlines(g, r, c, HORIZ, h);
	}
	/**
	 * �}�X�̒��S�ɉ��܂��͏c�̐���z�u����
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 * @param dir �����Ȃ� HORIZ �c���Ȃ� VERT
	 * @param n ���̖{��(1or2)
	 */
	public void placeMidlines(Graphics g, int r, int c, int dir, int n) {
		pos0.set(r, c);
		b2p(pos0);
		int direction = (dir == HORIZ ^ isTransposed()) ? HORIZ : VERT;
		if (n == 1) {
			drawMidline(g, toX(pos0.c), toY(pos0.r), direction);
		} else if (n == 2) {
			drawMidline2(g, toX(pos0.c), toY(pos0.r), direction);
		}
	}
	/**
	 * �����̍��W������p�Ƃ���Z���ɁC�Z���̂P�ӂ̒����Ɠ��������̉��܂��͏c�̐���`��
	 * @param x
	 * @param y
	 * @param direction �������c����
	 */
	public void drawMidline(Graphics g, int x, int y, int direction) {
		if (direction == HORIZ) {
			g.fillRect(x, y + getHalfCellSize() - 1, getCellSize() + 1, 3);
		} else if (direction == VERT) {
			g.fillRect(x + getHalfCellSize() - 1, y, 3, getCellSize() + 1);
		}
	}
	/**
	 * �����̍��W������p�Ƃ���Z���ɁC�Z���̂P�ӂ̒����Ɠ�������+2pixel�̉��܂��͏c�̐���2�{�`��
	 * @param g
	 * @param x
	 * @param y
	 * @param direction �������c����
	 */
	public void drawMidline2(Graphics g, int x, int y, int direction) {
		if (direction == HORIZ) {
			g.fillRect(x, y + getCellSize() / 3 - 1, getCellSize() + 1, 3);
			g.fillRect(x, y + getCellSize() * 2 / 3 - 1, getCellSize() + 1, 3);
		} else if (direction == VERT) {
			g.fillRect(x + getCellSize() / 3 - 1, y, 3, getCellSize() + 1);
			g.fillRect(x + getCellSize() * 2 / 3 - 1, y, 3, getCellSize() + 1);
		}
	}

	/*
	 * �u����������v�p�}�E�X���[�V�������X�i�[
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (!board.isOn(dragStart.r, dragStart.c))
			return;
		if (!board.isPier(dragStart.r, dragStart.c))
			return;
		if (dragStart.r == dragEnd.r) {
			if (dragStart.c < dragEnd.c) {
				board.addBridgeA(dragStart.r, dragStart.c, RT);
			} else if (dragStart.c > dragEnd.c) {
				board.addBridgeA(dragStart.r, dragStart.c, LT);
			}
		} else if (dragStart.c == dragEnd.c) {
			if (dragStart.r < dragEnd.r) {
				board.addBridgeA(dragStart.r, dragStart.c, DN);
			} else if (dragStart.r > dragEnd.r) {
				board.addBridgeA(dragStart.r, dragStart.c, UP);
			}
		}
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (!board.isOn(dragStart.r, dragStart.c))
			return;
		if (!board.isPier(dragStart.r, dragStart.c))
			return;
		if (dragStart.r == dragEnd.r) {
			if (dragStart.c < dragEnd.c) {
				board.removeBridgeA(dragStart.r, dragStart.c, RT);
			} else if (dragStart.c > dragEnd.c) {
				board.removeBridgeA(dragStart.r, dragStart.c, LT);
			}
		} else if (dragStart.c == dragEnd.c) {
			if (dragStart.r < dragEnd.r) {
				board.removeBridgeA(dragStart.r, dragStart.c, DN);
			} else if (dragStart.r > dragEnd.r) {
				board.removeBridgeA(dragStart.r, dragStart.c, UP);
			}
		}
	}

	/*
	 * �u����������v�L�[���X�i�[
	 * 
	 * �����̓��[�h�̂Ƃ��̂ݐ������͉\
	 * 1-8 : ���̐���
	 * - : �����̂Ȃ���
	 * 0, . : ��������
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num>=1 && num<=8)
				board.setNumber(pos.r, pos.c, num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r, pos.c, 0);
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r, pos.c, Board.UNDECIDED_NUMBER);
	}

}