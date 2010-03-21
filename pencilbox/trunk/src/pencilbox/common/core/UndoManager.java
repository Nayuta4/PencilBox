package pencilbox.common.core;

import java.util.Vector;

/**
 * �A���h�D�Ǘ��N���X
 * javax.swing.undo.UndoManager ���Q�l�ɕK�v�ȕ����̂ݎ��o���č쐬 
 */
public class UndoManager {

	private Vector<AbstractStep> edits;
	private int indexOfNextAdd;
	private BoardBase board;

	/**
	 * Creates a new <code>UndoManager</code>.
	 */
	public UndoManager(BoardBase b) {
		edits = new Vector<AbstractStep>();
		edits.ensureCapacity(100);
		indexOfNextAdd = 0;
		board = b;
	}

	/**
	 * Empties the undo manager.
	 */
	public synchronized void discardAllEdits() {
		edits.clear();
		indexOfNextAdd = 0;
	}

	/**
	 * Removes edits in the specified range. 
	 * @param from the minimum index to remove
	 * @param to the maximum index to remove
	 */
	protected void trimEdits(int from, int to) {
		if (from <= to) {
			for (int i = to; from <= i; i--) {
				edits.removeElementAt(i);
			}
			if (indexOfNextAdd > to) {
				indexOfNextAdd -= to - from + 1;
			} else if (indexOfNextAdd >= from) {
				indexOfNextAdd = from;
			}
		}
	}

	/**
	 * Adds an <code>AbstractStep</code> to this <code>UndoManager</code>, if it's possible. 
	 * This removes all edits from the index of the next edit to
	 * the end of the edits list. 
	 * @param anEdit the edit to be added
	 * @return true
	 */
	public synchronized boolean addEdit(AbstractStep anEdit) {
		// ���݈ʒu�������폜
        trimEdits(indexOfNextAdd, edits.size()-1);
		AbstractStep last = lastEdit();
		// �V�������삪�ŏ��̑���Ȃ�C�ǉ�����
		// �����łȂ���΁C�V����������Ō�̑���ƍ����ł��邩�𒲂ׁC
		// �����ł��Ȃ���΁C�V���������ǉ�����B
		if (last == null) {
			edits.addElement(anEdit);
		} else if (!last.addEdit(anEdit)) {
			edits.addElement(anEdit);
		} else {
		}
		// ���݈ʒu���X�V
        indexOfNextAdd = edits.size();
        return true;
	}

	protected AbstractStep lastEdit() {
		int count = edits.size();
		if (count > 0)
			return edits.elementAt(count - 1);
		else
			return null;
	}

	/**
	 * Undoes the appropriate edits. 
	 * This invokes <code>undo</code> on the last edit, 
	 * updating the index of the next edit appropriately.
	 */
	public synchronized void undo() {
		if (indexOfNextAdd > 0) {
			AbstractStep edit = edits.elementAt(indexOfNextAdd - 1);
			board.undo(edit);
			--indexOfNextAdd;
		}
	}

    /**
     * Returns true if edits may be undone.
     * @return true if there are edits to be undone
     */
	public synchronized boolean canUndo() {
		return indexOfNextAdd > 0;
	}

	/**
	 * Redoes the appropriate edits. 
	 * This invokes <code>redo</code> on the next significant edit, 
	 * updating the index of the next edit appropriately.
	 */
	public synchronized void redo() {
		if (indexOfNextAdd < edits.size()) {
			AbstractStep edit = edits.elementAt(indexOfNextAdd);
			board.redo(edit);
			indexOfNextAdd++;
		}
	}

    /**
     * Returns true if edits may be redone.
     * @return true if there are edits to be redone
     */
	public synchronized boolean canRedo() {
		return indexOfNextAdd < edits.size();
	}

	public int getEditsSize() {
		return edits.size();
	}

	public int getIndexOfNextAdd() {
		return indexOfNextAdd;
	}

	/**
	 * �����Ɏw�肵���萔�܂ŃA���h�D�܂��̓��h�D���J��Ԃ���
	 * @param n
	 */
	public void jumpTo(int n) {
		if (n >= 0 && n < indexOfNextAdd) {
//			System.out.println("undo from " + indexOfNextAdd + " to " + n);
			for (int i = indexOfNextAdd-1; i >= n; i--) {
				undo();
			}
		}
		if (n > indexOfNextAdd && n <= edits.size()) {
//			System.out.println("redo from " + indexOfNextAdd + " to " + n);
			for (int i = indexOfNextAdd; i < n; i++) {
				redo();
			}
		}
	}

	public String toString() {
		return " edits: " + edits + " indexOfNextAdd: " + indexOfNextAdd;
	}

}
