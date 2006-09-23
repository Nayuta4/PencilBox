package pencilbox.common.factory;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import pencilbox.common.gui.FileChooser;

/**
 * PencilBox 各アプリケーション起動パネルル
 */
public class PencilBoxLauncher {

	/**
	 * PencilBox を起動する
	 * パズルの種類を選択するランチャーを表示する
	 * @param args 引数は無意味
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new PencilBoxLauncher();
		FileChooser.getProblemFileChooser(); // FileChooser作成は時間がかかるので，このタイミングで作っておく
	}
	
	/**
	 * ランチャーを作成する
	 */
	public PencilBoxLauncher() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle(Constants.TITLE);
		JPanel panel = new JPanel();
		int n = PencilType.getNPencilType();
		panel.setLayout(new GridLayout(n+1,1));
		for (int i=0; i<n; i++) {
			panel.add(makeButton(PencilType.getPencilType(i)));
		}
		panel.add(makeExitButton());
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	private JButton makeButton(final PencilType pencilType) {
		JButton button = new JButton(pencilType.getTitle());
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					PencilFactory.getInstance(pencilType).createNewFrame();
				} catch (PencilBoxClassException e) {
					JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return button;
	}
	
	private JButton makeExitButton() {
		JButton button = new JButton("終了");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		return button;
	}
	
}
