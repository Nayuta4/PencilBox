package pencilbox.tentaisho;

import java.util.*;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.*;

import pencilbox.common.core.*;
import pencilbox.util.*;


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
	private List areaList;

	protected void setup () {
		super.setup();
		star = new int[rows()*2-1][cols()*2-1];
		area = new Area[rows()][cols()];
		areaList = new LinkedList();
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
		for (Iterator itr = areaList.iterator(); itr.hasNext(); ) {
			setContainedStar((Area) itr.next());
		}
	}
	
	/**
	 * @return �Ֆʂ̐��̐�
	 */
	public int nStar() {
		int nStar = 0;
		for (int r = 0; r < rows()*2-1; r++)
			for (int c = 0; c < cols()*2-1; c++)
				if (hasStar(r,c)) nStar ++;
		return nStar;
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
	/**
	 * �����̍��W�ɐ���ݒ肷��
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setStar(int r, int c, int st) {
		star[r][c] = st;
	}
	/**
	 * �����ɗ^����ꂽ���W�ɐ��͂��邩
	 * @param r
	 * @param c
	 * @return ��������� true
	 */
	public boolean hasStar(int r, int c) {
		return star[r][c] > 0;
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
	/**
	 * �Տ�̃}�X�ɁC���̃}�X�̏�������̈��ݒ肷��
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c, Area a) {
		area[r][c] = a;
	}
	/**
	 * �ՖʂɐV�����̈��ǉ�����
	 * �ύX���A���h�D���X�i�[�ɒʒm����
	 * @param newArea �ǉ�����̈�
	 */
	public void addAreaA(Area newArea) {
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(newArea, Step.ADDED)));
		addArea(newArea);
	}
	/**
	 * �Ֆʂ̗̈���P��������
	 * �ύX���A���h�D���X�i�[�ɒʒm����
	 * @param oldArea ��������̈�
	 */
	public void removeAreaA(Area oldArea) {
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(oldArea, Step.REMOVED)));
		removeArea(oldArea);
	}
	/**
	 * �ՖʂɐV�����̈��ǉ�����
	 * @param newArea �ǉ�����̈�
	 */
	public void addArea(Area newArea) {
		for (Iterator itr = newArea.iterator(); itr.hasNext(); ) {
			Address pos = (Address) itr.next();
			area[pos.r][pos.c] = newArea;
		}
		setContainedStar(newArea);
		areaList.add(newArea);
	}
	/**
	 * �Ֆʂ̗̈���P��������
	 * @param oldArea ��������̈�
	 */
	public void removeArea(Area oldArea) {
		for (Iterator itr = oldArea.iterator(); itr.hasNext(); ) {
			Address pos = (Address) itr.next();
			if (area[pos.r][pos.c] == oldArea)
				area[pos.r][pos.c] = null;
		}
		areaList.remove(oldArea);
	}
	/**
	 * �V�K�쐬�����̈�Ɋ܂܂�鐯��ݒ肷��
	 * @param newArea
	 */
	void setContainedStar(Area newArea) {
		Address pos;
		int nStar = 0;
		StarAddress starPos = new StarAddress();
		for (Iterator itr = newArea.iterator(); itr.hasNext(); ) {
			pos = (Address) itr.next();
			for (int i=2*pos.r-1; i<=2*pos.r+1; i++) {
				for (int j=2*pos.c-1; j<=2*pos.c+1; j++) {
					if (isOnStar(i, j) && hasStar(i, j)) {
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
		Area a;
		for (Iterator itr = areaList.iterator(); itr.hasNext(); ) {
			a = (Area) itr.next();
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
			message.append("�������Ȃ��̈悪����\n");
		else if ((result&2) == 2)
			message.append("������\n");
		return message.toString();
	}
	/**
	 * @return Returns the areaList.
	 */
	List getAreaList() {
		return areaList;
	}
	/**
	 * @return Returns the domain.
	 */
	Area[][] getArea() {
		return area;
	}
	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	class Step extends AbstractUndoableEdit {

		static final int ADDED = 1;
		static final int REMOVED = 0;
		static final int CHANGED = 2;

		private Area area;
		private int operation;

		/**
		 * �R���X�g���N�^
		 * @param area Area
		 * @param operation ����
		 */
		public Step(Area area, int operation) {
			super();
			this.area = area;
			this.operation = operation;
		}
		public void undo() throws CannotUndoException {
			super.undo();
			if (operation == ADDED) {
				removeArea(area);
			} else if (operation == REMOVED) {
				addArea(area);
			} else if (operation == CHANGED) {
				addArea(area);
			}
		}
		public void redo() throws CannotRedoException {
			super.redo();
			if (operation == ADDED) {
				addArea(area);
			} else if (operation == REMOVED) {
				removeArea(area);
			}
		}
	}

}

