package pencilbox.common.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pencilbox.common.core.UndoManager;

/**
 * ���𑀍�_�C�A���O�B
 * ���[�_���_�C�A���O�Ȃ̂ŁC1�̃C���X�^���X�݂̂𐶐����Ďg���񂷁B
 * �X���C�_�[�őI����������_�܂ŃA���h�D�܂��̓��h�D�����s����B
 */
public class HistoryDialog extends PencilBoxDialog {

	private static HistoryDialog instance = new HistoryDialog();

	/**
	 * HistoryDialog �̃C���X�^���X���擾����
	 * �R���X�g���N�^�͎g�p�s�ŁC�C���X�^���X�͂��̃��\�b�h�ɂ��擾����
	 * @return HistoryDialog �C���X�^���X
	 */
	public static HistoryDialog getInstance() {
		return instance;
	}
    
    private JSlider slider;
    private PanelBase panel;
    private UndoManager undoManager;
	private ChangeListener changeListener;

	private HistoryDialog() {
		super();
	}

	protected void makeDialog() {
	    setDialogType(OK_CANCEL);
		super.makeDialog();
		makeMainPanel();
	}
    
	private void makeMainPanel() {
		JPanel mainPanel = new JPanel();
		makeSlider();
		mainPanel.add(slider);
		this.add(mainPanel, BorderLayout.NORTH);
	}
	
	private void makeSlider() {
		slider = new JSlider(SwingConstants.HORIZONTAL);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jumpTo(slider.getValue());
			}
		};
	}

	/**
	 * �_�C�A���O���Ă΂ꂽ�Ƃ��̏����ݒ�
	 * @param p �p�l��
	 * @param um �A���h�D�}�l�[�W���[
	 */
	public void init(PanelBase p, UndoManager um) {
		slider.removeChangeListener(changeListener); // �����ݒ莞�ɔ������Ȃ��悤�ɂ�������؂�B
     	panel = p;
    	undoManager = um;
    	int m = um.getEditsSize();
    	int k = um.getIndexOfNextAdd();
    	slider.setMaximum(m);
    	int ts = m/25+1;
		slider.setMinorTickSpacing(ts);
		slider.setMajorTickSpacing(ts*5);
		slider.setLabelTable(slider.createStandardLabels(ts*5));
    	slider.setValue(k);
		slider.addChangeListener(changeListener);
//    	System.out.println("edits size is " + m + " current step is " + k);
    }

	/**
	 * �X���C�_�[�őI�����������܂Ŕ��
	 * @param n
	 */
	public void jumpTo(int n) {
		undoManager.jumpTo(n);
		panel.repaint();
//    	System.out.println("jump to " + n);
	}

}
