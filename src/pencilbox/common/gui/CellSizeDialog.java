package pencilbox.common.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * マスの大きさを設定するダイアログ。
 * モーダルダイアログなので，1つのインスタンスのみを生成して使い回す。
 * 互いに連動したスライダーまたはスピナーでマスの大きさを設定する。
 * 値はマスのピッチのピクセル数で，偶数に制限する。
 */
public class CellSizeDialog extends PencilBoxDialog {

	private static CellSizeDialog instance = new CellSizeDialog();

	/**
	 * CellSizeDialog のインスタンスを取得する
	 * コンストラクタは使用不可で，インスタンスはこのメソッドにより取得する
	 * @return CellSizeDialog インスタンス
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
	}
	
	private void makeSlider() {
		slider = new JSlider(JSlider.HORIZONTAL, VALUE_MIN, VALUE_MAX, VALUE_INIT);
		slider.setMajorTickSpacing(12);
		slider.setMinorTickSpacing(2);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
//		slider.setSnapToTicks(true); // キーボード操作等に対して期待通りに動作しないので不採用
		slider.addChangeListener(new ChangeListener() {
	       public void stateChanged(ChangeEvent e) {
				int value = slider.getValue() / 2 * 2; // 偶数に丸める
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
        SpinnerModel numberModel = new SpinnerNumberModel(VALUE_INIT, VALUE_MIN, VALUE_MAX, 2);
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

}
