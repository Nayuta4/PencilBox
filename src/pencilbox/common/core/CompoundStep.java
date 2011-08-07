/**
 * 
 */
package pencilbox.common.core;

import java.util.Vector;


/**
 * 複数のアンドゥ単位操作を一回のアンドゥ操作で操作される単位にまとめたものを表すクラス
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

	/**
	 * @param edit
	 * @return
	 */
	public boolean addEdit(AbstractStep edit) {
		edits.add(edit);
		return true;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < edits.size(); i++) {
			sb.append(i + edits.get(i).toString() + ", ");
		}
		return sb.toString();
	}
}
