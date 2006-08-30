/**
 * 
 */
package pencilbox.common.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 */
public class DataExportDialog extends PencilBoxDialog {
	
	private JTextArea textArea;

	/**
	 * 
	 */
	public DataExportDialog() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void makeDialog() {
	    setDialogType(OK_ONLY);
		super.makeDialog();
		makeMainPanel();
	}

	private void makeMainPanel() {
		JPanel mainPanel = new JPanel();
		textArea = new JTextArea(20,40);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
		textArea.selectAll();
		mainPanel.add(textArea);
		JScrollPane jScrollPane = new JScrollPane(textArea);
		this.add(jScrollPane, BorderLayout.NORTH);
	}
	
	public void setText(String s) {
		textArea.append(s);
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(s.length());
	}
}
