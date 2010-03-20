package pencilbox.tentaisho;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
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

	private int[][] star;
	private Area[][] area;
	private List<Area> areaList;

	protected void setup () {
		super.setup();
		star = new int[rows()*2-1][cols()*2-1];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
	}


	public void clearBoard() {
		super.clearBoard();
		areaList.clear();
		ArrayUtil.initArrayObject2(area, null);
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
	/**
	 * �����̃}�X�������ꂩ�̗̈�Ɋ܂܂�Ă��邩�ǂ���
	 * @param r
	 * @param c
	 * @return �܂܂�Ă���� true
	 */
	public boolean isCovered(int r, int c) {
		return area[r][c] != null;
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
	 * @param c Colmun coordinate of the cell.
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
			setArea(pos.r(), pos.c(), newArea);
		}
		areaList.add(newArea);
	}

	/**
	 * �̈���폜����
	 * @param oldArea
	 */
	public void removeArea(Area oldArea) {
		for (Address pos : oldArea) {
			if (getArea(pos.r(), pos.c()) == oldArea)
				setArea(pos.r(), pos.c(), null);
		}
		areaList.remove(oldArea);
	}

	void addCellToAreaA(Address pos, Area area) {
		Address p0 = Address.NOWHERE;
		if (area.size() > 0) {
			p0 = (Address)area.toArray()[0];
		}
		fireUndoableEditUpdate(new Step(pos.r(), pos.c(), p0.r(), p0.c(), Step.ADDED));
		addCellToArea(pos, area);
	}

	void removeCellFromAreaA(Address pos, Area area) {
		Address p0 = Address.NOWHERE;
		if (area.size() > 1) {
			p0 = (Address)area.toArray()[0];
			if (p0.equals(pos))
				p0 = (Address)area.toArray()[1];
		}
		fireUndoableEditUpdate(new Step(pos.r(), pos.c(), p0.r(), p0.c(), Step.REMOVED));
		removeCellFromArea(pos, area);
	}

	public void undo(AbstractStep step) {
		Step s = (Step)step;
		Area a;
		if (s.operation == Step.ADDED) {
			a = getArea(s.r, s.c);
			removeCellFromArea(s.r, s.c, a);
		} else if (s.operation == Step.REMOVED) {
			if (Address.NOWHERE.equals(s.r0, s.c0))
				a = new Area();
			else
				a = getArea(s.r0, s.c0);
			addCellToArea(s.r, s.c, a);
		}
	}

	public void redo(AbstractStep step) {
		Step s = (Step)step;
		Area a;
		if (s.operation == Step.ADDED) {
			if (Address.NOWHERE.equals(s.r0, s.c0))
				a = new Area();
			else
				a = getArea(s.r0, s.c0);
			addCellToArea(s.r, s.c, a);
		} else if (s.operation == Step.REMOVED) {
			a = getArea(s.r, s.c);
			removeCellFromArea(s.r, s.c, a);
		}
	}
	/**
	 * �}�X��̈�ɒǉ�����
	 * @param r �ǉ�����}�X�̍s���W
	 * @param c �ǉ�����}�X�̗���W
	 * @param area �ǉ������̈�
	 */
	public void addCellToArea(int r, int c, Area area) {
		if (area.isEmpty()) {
			areaList.add(area);
		}
		setArea(r, c, area);
		area.add(r, c);
		initArea(area);
	}

	public void addCellToArea(Address pos, Area area) {
		addCellToArea(pos.r(), pos.c(), area);
	}
	/**
	 * �}�X��̈悩���菜��
	 * @param r ��菜���}�X�̍s���W
	 * @param c ��菜���}�X�̗���W
	 * @param area ��菜�����̈�
	 */
	public void removeCellFromArea(int r, int c, Area area) {
		setArea(r, c, null);
		area.remove(r, c);
		if (area.isEmpty()) {
			areaList.remove(area);
		} else {
			initArea(area);
		}
	}

	public void removeCellFromArea(Address pos, Area area) {
		removeCellFromArea(pos.r(), pos.c(), area);
	}
	/**
	 * �V�K�쐬�����̈�Ɋ܂܂�鐯��ݒ肷��
	 * @param newArea
	 */
	void initArea(Area newArea) {
		int nStar = 0;
		StarAddress starPos = new StarAddress();
		for (Address pos : newArea) {
			for (int i=2*pos.r()-1; i<=2*pos.r()+1; i++) {
				for (int j=2*pos.c()-1; j<=2*pos.c()+1; j++) {
					if (isOnStar(i, j) && getStar(i, j) > 0) {
						if (nStar == 0) {
							nStar = getStar(i, j);
							starPos.set(i,j);
						} else if (nStar == 1 || nStar == 2) {
							if (!starPos.equals(i, j)) {
								nStar = -1;
								starPos.setNowhere();
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
		for (int r = 0; r < rows(); r++)
			for (int c = 0; c < cols(); c++)
				if (area[r][c] == null) result |= 2;
	
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
	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	class Step extends AbstractStep {

		static final int ADDED = 1;
		static final int REMOVED = 0;
		static final int CHANGED = 2;
		
		int r;
		int c;
		int r0;
		int c0;
		int operation;

		/**
		 * �R���X�g���N�^
		 * @param r �ύX���ꂽ�}�X�̍s���W
		 * @param c �ύX���ꂽ�}�X�̗���W
		 * @param r0 �ύX���ꂽ�̈�̑�\�}�X�̍s���W
		 * @param c0 �ύX���ꂽ�̈�̑�\�}�X�̗���W
		 */
		public Step(int r, int c, int r0, int c0, int operation) {
			super();
			this.r = r;
			this.c = c;
			this.r0 = r0;
			this.c0 = c0;
//			this.area = area;
			this.operation = operation;
		}
		
	}
