/**
 * 
 */
package pencilbox.common.core;

import java.util.Vector;


/**
 * �����̃A���h�D�P�ʑ�������̃A���h�D����ő��삳���P�ʂɂ܂Ƃ߂����̂�\���N���X
 *
 */
public class CompoundStep extends AbstractStep {

    protected Vector<AbstractStep> edits;

    /**
     * Creates a new <code>UndoManager</code>.
     */
    public CompoundStep() {
    	edits = new Vector<AbstractStep>();
   }

	/* (non-Javadoc)
	 * @see pencilbox.common.core.StepCommand#addEdit(pencilbox.common.core.StepCommand)
	 */
	public boolean attachEdit(AbstractStep edit) {
		edits.add(edit);
		return false;
	}

	/* (non-Javadoc)
	 * @see pencilbox.common.core.StepCommand#replaceEdit(pencilbox.common.core.StepCommand)
	 */
	public boolean replaceEdit(AbstractStep last) {
		return false;
	}

	/* (non-Javadoc)
	 * @see pencilbox.common.core.StepCommand#isNullEdit()
	 */
	public boolean isNullEdit() {
		// TODO Auto-generated method stub
		return false;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < edits.size(); i++) {
			sb.append(i + edits.get(i).toString() + ", ");
		}
		return sb.toString();
	}
}
