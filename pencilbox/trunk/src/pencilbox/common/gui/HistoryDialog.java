package pencilbox.common.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pencilbox.common.core.UndoManager;

/**
 * 履歴操作ダイアログ。
 * モーダルダイアログなので，1つのインスタンスのみを生成して使い回す。
 * スライダーで選択した履歴点までアンドゥまたはリドゥを実行する。
 */
public class HistoryDialog extends PencilBoxDialog {

	private static HistoryDialog instance = new HistoryDialog();

	/**
	 * HistoryDialog のインスタンスを取得する
	 * コンストラクタは使用不可で，インスタンスはこのメソッドにより取得する
	 * @return HistoryDialog インスタンス
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
	 * ダイアログが呼ばれたときの初期設定
	 * @param p パネル
	 * @param um アンドゥマネージャー
	 */
	public void init(PanelBase p, UndoManager um) {
		slider.removeChangeListener(changeListener); // 初期設定時に反応しないようにいったん切る。
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
	 * スライダーで選択した履歴まで飛ぶ
	 * @param n
	 */
	public void jumpTo(int n) {
		undoManager.jumpTo(n);
		panel.repaint();
//    	System.out.println("jump to " + n);
	}

}
