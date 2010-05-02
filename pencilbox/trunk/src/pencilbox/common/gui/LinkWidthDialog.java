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
 * マスの大きさを設定するダイアログ。
 * モーダルダイアログなので，1つのインスタンスのみを生成して使い回す。
 * 互いに連動したスライダーまたはスピナーでマスの大きさを設定する。
 * 値はマスのピッチのピクセル数。
 */
public class LinkWidthDialog extends PencilBoxDialog implements MouseWheelListener {

	private static LinkWidthDialog instance = new LinkWidthDialog();

	/**
	 * CellSizeDialog のインスタンスを取得する
	 * コンストラクタは使用不可で，インスタンスはこのメソッドにより取得する
	 * @return CellSizeDialog インスタンス
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
//		slider.setSnapToTicks(true); // キーボード操作等に対して期待通りに動作しないので不採用
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
	 * ダイアログが呼ばれたときの初期設定
	 * @param p パネル
	 * @param um アンドゥマネージャー
	 */
	public void init() {
//		slider.removeChangeListener(changeListener); // 初期設定時に反応しないようにいったん切る。
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
	 * マウスホイールが回転すると呼び出されます。
	 * 
	 * @param e マウスホイールのイベント
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		setValue(getValue() - e.getWheelRotation());
	}
}
