package pencilbox.tentaisho;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.AreaEditStep;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.SideAddress;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 *  �u�V�̃V���[�v�ՖʃN���X
 */
public class Board extends BoardBase {

	/** �����Ȃ����� */
	static final int NOSTAR = 0;
	/** ������ */
	static final int WHITESTAR = 1;
	/** ������ */
	static final int BLACKSTAR = 2;

	static final int LINE = 1;
	static final int NOLINE = 0;

	private int[][] star;
	private int[][][] edge;
	private Area[][] area;
	private List<Area> areaList;

	protected void setup () {
		super.setup();
		star = new int[rows()*2-1][cols()*2-1];
		edge = new int[2][][];
		edge[0] = new int[rows()][cols() - 1];
		edge[1] = new int[rows() - 1][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
	}

	public void clearBoard() {
		super.clearBoard();
		areaList.clear();
		ArrayUtil.initArrayObject2(area, null);
		for (SideAddress p : borderAddrs()) {
			setEdge(p, NOLINE);
		}
	}

	public void trimAnswer() {
		for (SideAddress p : borderAddrs()) {
			if (getEdge(p) == LINE) {
				changeEdge(p, NOLINE);
			}
		}
	}

	public void initBoard() {
		initAreas();
	}

	/**
	 * �Ֆʏ�̗̈�̏����������s��
	 */
	public void initAreas() {
		for (Area a : areaList) {
			initArea(a);
		}
	}

	/**
	 * @return the star
	 */
	int[][] getStar() {
		return star;
	}
	/**
	 * �����̍��W�̐����擾����
	 * @param r
	 * @param c
	 * @return ��
	 */
	public int getStar(int r, int c) {
		return star[r][c];
	}

	public int getStar(Address pos) {
		return getStar(pos.r(), pos.c());
	}
	/**
	 * �����̍��W�ɐ���ݒ肷��
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setStar(int r, int c, int st) {
		star[r][c] = st;
	}

	public void setStar(Address pos, int st) {
		setStar(pos.r(), pos.c(), st);
	}

	public void changeStar(Address p, int st) {
		int prev = getStar(p);
		if (prev == st)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		setStar(p, st);
		Address p0 = starAddressToSuperAddress(p);
		if (getArea(p0) != null)
			initArea(getArea(p0));
	}

	private Address starAddressToSuperAddress(Address p) {
		return Address.address(p.r()/2, p.c()/2);
	}

	/**
	 * �����̐����W���Տ�ɂ��邩
	 * 0<=r<rows*2-1, 0<=c<cols*2-1 �ł���ΔՏ�ł���
	 * @param r
	 * @param c
	 * @return�@�Տ�ɂ���� true
	 */
	public boolean isOnStar(int r, int c) {
		return (r>=0 && r<rows()*2-1 && c>=0 && c<cols()*2-1);
	}

	public boolean isOnStar(Address pos) {
		return isOnStar(pos.r(), pos.c());
	}

	public int getEdge(SideAddress p) {
		return edge[p.d()][p.r()][p.c()];
	}

	public void setEdge(SideAddress p, int i) {
		edge[p.d()][p.r()][p.c()] = i;
	}

	/**
	 * �����ɗ^����ꂽ�}�X�̏����̈���擾����
	 * @param r
	 * @param c
	 * @return �}�X�̏����̈�
	 */
	public Area getArea(int r, int c) {
		return area[r][c];
	}

	public Area getArea(Address pos) {
		return getArea(pos.r(), pos.c());
	}
	/**
	 * �Տ�̃}�X�ɁC���̃}�X�̏�������̈��ݒ肷��
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c, Area a) {
		area[r][c] = a;
	}

	public void setArea(Address pos, Area a) {
		setArea(pos.r(), pos.c(), a);
	}
	/**
	 * �ՖʂɐV�����̈��ǉ�����
	 * @param newArea �ǉ�����̈�
	 */
	public void addArea(Area newArea) {
		for (Address pos : newArea) {
			setArea(pos, newArea);
		}
		areaList.add(newArea);
	}
	/**
	 * �̈�̂��ׂẴ}�X��̈悩�珜���ė̈���폜����
	 * @param oldArea
	 */
	public void removeWholeArea(Area oldArea) {
		Address[] cells = oldArea.toArray(new Address[0]);
		for (Address p : cells) {
			removeCellFromArea(p, oldArea);
		}
	}

	/**
	 * @param p
	 * @param st
	 */
	public void changeEdge(SideAddress p, int st) {
		int prev = getEdge(p);
		if (prev == st)
			return;
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new BorderEditStep(p, prev, st));
		}
		setEdge(p, st);
	}

	public void undo(AbstractStep step) {
		if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				removeCell(s.getPos());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				addCell(s.getPos(), s.getP0());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getBefore());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeStar(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				addCell(s.getPos(), s.getP0());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				removeCell(s.getPos());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getAfter());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeStar(s.getPos(), s.getAfter());
		}
	}
	/**
	 * p �� p0 �Ɠ����̈�ɂ���B������ p0�� NOWHWER�Ȃ�ΐV�����̈�����
	 * @param p
	 * @param p0
	 */
	void addCell(Address p, Address p0) {
		if (p0.equals(Address.NOWHERE)) {
			Area a = new Area();
			addCellToArea(p, a);
		} else {
			Area a = getArea(p0);
			if (a != null) {
				addCellToArea(p, a);
			}
		}
	}
	/**
	 * �}�X��̈悩���菜���B
	 * @param p
	 */
	void removeCell(Address p) {
		Area a = getArea(p);
		if (a != null) {
			removeCellFromArea(p, a);
		}
	}
	/**
	 * �}�X��̈�ɒǉ�����
	 * @param p �ǉ�����}�X�̍��W
	 * @param a �ǉ������̈�
	 */
	public void addCellToArea(Address p, Area a) {
		if (isRecordUndo()) {
			Address p0 = Address.NOWHERE;
			if (a.size() > 0) {
				p0 = a.getTopCell(Address.NOWHERE);
			}
			fireUndoableEditUpdate(new AreaEditStep(p, p0, AreaEditStep.ADDED));
		}
		if (a.isEmpty()) {
			areaList.add(a);
		}
		setArea(p, a);
		a.add(p);
		initArea(a);
	}

	/**
	 * �}�X��̈悩���菜��
	 * @param p ��菜���}�X�̍��W
	 * @param a ��菜�����̈�
	 */
	public void removeCellFromArea(Address p, Area a) {
		if (isRecordUndo()) {
			Address p0 = Address.NOWHERE;
			if (a.size() > 1) {
				p0 = a.getTopCell(p);
			}
			fireUndoableEditUpdate(new AreaEditStep(p, p0, AreaEditStep.REMOVED));
		}
		setArea(p, null);
		a.remove(p);
		if (a.isEmpty()) {
			areaList.remove(a);
		} else {
			initArea(a);
		}
	}

	/**
	 * �V�K�쐬�����̈�Ɋ܂܂�鐯��ݒ肷��
	 * @param newArea
	 */
	void initArea(Area newArea) {
		int nStar = 0;
		StarAddress starPos = StarAddress.NOWHERE;
		for (Address pos : newArea) {
			for (int i=2*pos.r()-1; i<=2*pos.r()+1; i++) {
				for (int j=2*pos.c()-1; j<=2*pos.c()+1; j++) {
					if (isOnStar(i, j) && getStar(i, j) > 0) {
						if (nStar == 0) {
							nStar = getStar(i, j);
							starPos = StarAddress.address(i,j);
						} else if (nStar == 1 || nStar == 2) {
							if (!starPos.equals(i, j)) {
								nStar = -1;
								starPos = StarAddress.NOWHERE;
							}
						}
					}
				}
			}
		}
		newArea.setStarType(nStar);
		newArea.setStarPos(starPos);
	}

	public int checkAnswerCode() {
		int result = 0;
		for (Area a : areaList) {
			if (a.isPointSymmetry() == false) {
				result |= 1;
			}
		}
		for (Address p : cellAddrs()) {
			if (getArea(p) == null) {
				result |= 2;
			}
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result ==0 )
			return COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result&1) == 1)
			message.append(Messages.getString("tentaisho.AnswerCheckMessage1")); //$NON-NLS-1$
		else if ((result&2) == 2)
			message.append(Messages.getString("tentaisho.AnswerCheckMessage2")); //$NON-NLS-1$
		return message.toString();
	}
	/**
	 * @return Returns the areaList.
	 */
	List<Area> getAreaList() {
		return areaList;
	}
	/**
	 * @return Returns the domain.
	 */
	Area[][] getArea() {
		return area;
	}
}
