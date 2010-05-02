package pencilbox.common.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * �}�X�̑傫����ݒ肷��_�C�A���O�B
 * ���[�_���_�C�A���O�Ȃ̂ŁC1�̃C���X�^���X�݂̂𐶐����Ďg���񂷁B
 * �݂��ɘA�������X���C�_�[�܂��̓X�s�i�[�Ń}�X�̑傫����ݒ肷��B
 * �l�̓}�X�̃s�b�`�̃s�N�Z�����B
 */
public class LinkWidthDialog extends PencilBoxDialog implements MouseWheelListener {

	private static LinkWidthDialog instance = new LinkWidthDialog();

	/**
	 * CellSizeDialog �̃C���X�^���X���擾����
	 * �R���X�g���N�^�͎g�p�s�ŁC�C���X�^���X�͂��̃��\�b�h�ɂ��擾����
	 * @return CellSizeDialog �C���X�^���X
	 */
	public static LinkWidthDialog getInstance() {
		return instance;
	}

    static final int VALUE_MIN = 1;
    static final int VALUE_MAX = 7;
    static final int VALUE_INIT = 3;
    
    private JSlider slider;
    private JSpinner spinner;
    private PanelBase panel;

	private LinkWidthDialog() {
		super();
	}

	protected void makeDialog() {
	    setDialogType(OK_CANCEL);
		super.makeDialog();
		makeMainPanel();
	}
    
    public void setPanel(PanelBase p) {
    	panel = p;
    }

	private void makeMainPanel() {
		JPanel mainPanel = new JPanel();
		makeSlider();
		makeSpinner();
		mainPanel.add(slider);
		mainPanel.add(spinner);
		this.add(mainPanel, BorderLayout.NORTH);
		addMouseWheelListener(this);
	}
	
	private void makeSlider() {
		slider = new JSlider(SwingConstants.HORIZONTAL, VALUE_MIN, VALUE_MAX, VALUE_INIT);
		slider.setMajorTickSpacing(12);
		slider.setMinorTickSpacing(2);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
//		slider.setSnapToTicks(true); // �L�[�{�[�h���쓙�ɑ΂��Ċ��Ғʂ�ɓ��삵�Ȃ��̂ŕs�̗p
		slider.addChangeListener(new ChangeListener() {
	       public void stateChanged(ChangeEvent e) {
				int value = slider.getValue();
//				if (slider.getValueIsAdjusting()) {
					spinner.setValue(Integer.valueOf(value));
					panel.setLinkWidth(value);
					panel.repaint();
//				}
			}
		});
	}
	
	private void makeSpinner() {
        SpinnerModel numberModel = new SpinnerNumberModel(VALUE_INIT, VALUE_MIN, VALUE_MAX, 1);
		spinner = new JSpinner(numberModel);
        spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));
		spinner.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
				int value = ((SpinnerNumberModel)spinner.getModel()).getNumber().intValue();
				slider.setValue(value);
			}
		});
	}

	/**
	 * �_�C�A���O���Ă΂ꂽ�Ƃ��̏����ݒ�
	 * @param p �p�l��
	 * @param um �A���h�D�}�l�[�W���[
	 */
	public void init() {
//		slider.removeChangeListener(changeListener); // �����ݒ莞�ɔ������Ȃ��悤�ɂ�������؂�B
//		int m = panel.getCellSize()-1;
		int m = VALUE_MAX;
    	slider.setMaximum(m);
    	int ts = 1;
		slider.setMinorTickSpacing(ts);
		slider.setMajorTickSpacing(ts*2);
		slider.setLabelTable(slider.createStandardLabels(ts*2));
    	slider.setValue(panel.getLinkWidth());
//		slider.addChangeListener(changeListener);
//    	System.out.println("edits size is " + m + " current step is " + k);
    }

	/**
	 * @return the cellSize
	 */
	public int getValue() {
		return slider.getValue();
	}

	/**
	 * @param cellSize the cellSize to set
	 */
	public void setValue(int i) {
		slider.setValue(i);
	}
	
	/**
	 * �}�E�X�z�C�[������]����ƌĂяo����܂��B
	 * 
	 * @param e �}�E�X�z�C�[���̃C�x���g
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		setValue(getValue() - e.getWheelRotation());
	}
}
