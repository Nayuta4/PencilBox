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
public class CellSizeDialog extends PencilBoxDialog implements MouseWheelListener {

	private static CellSizeDialog instance = new CellSizeDialog();

	/**
	 * CellSizeDialog �̃C���X�^���X���擾����
	 * �R���X�g���N�^�͎g�p�s�ŁC�C���X�^���X�͂��̃��\�b�h�ɂ��擾����
	 * @return CellSizeDialog �C���X�^���X
	 */
	public static CellSizeDialog getInstance() {
		return instance;
	}

    static final int VALUE_MIN = 8;
    static final int VALUE_MAX = 80;
    static final int VALUE_INIT = 26;
    
    private JSlider slider;
    private JSpinner spinner;
    private PanelBase panel;

	private CellSizeDialog() {
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
				if (slider.getValueIsAdjusting()) {
					spinner.setValue(Integer.valueOf(value));
//					panel.setDisplaySize(value);
				} else {
					spinner.setValue(Integer.valueOf(value));
					panel.setDisplaySize(value);
				}
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
	 * @return the cellSize
	 */
	public int getCellSize() {
		return slider.getValue();
	}

	/**
	 * @param cellSize the cellSize to set
	 */
	public void setCellSize(int cellSize) {
		slider.setValue(cellSize);
	}
	
	/**
	 * �}�E�X�z�C�[������]����ƌĂяo����܂��B
	 * 
	 * @param e �}�E�X�z�C�[���̃C�x���g
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		setCellSize(getCellSize() + e.getWheelRotation());
	}
}
