package pencilbox.common.factory;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import pencilbox.common.gui.FileChooser;
import pencilbox.resource.Messages;

/**
 * PencilBox �e�A�v���P�[�V�����N���p�l����
 */
public class PencilBoxLauncher {

	/**
	 * PencilBox ���N������
	 * �p�Y���̎�ނ�I�����郉���`���[��\������
	 * @param args �����͖��Ӗ�
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new PencilBoxLauncher();
		FileChooser.getProblemFileChooser(); // FileChooser�쐬�͎��Ԃ�������̂ŁC���̃^�C�~���O�ō���Ă���
	}
	
	/**
	 * �����`���[���쐬����
	 */
	public PencilBoxLauncher() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle(Constants.TITLE);
		JPanel panel = new JPanel();
		List<PencilType> pencilTypeList = PencilType.getPencilTypeList();
		int n = pencilTypeList.size();
		panel.setLayout(new GridLayout(n,1));
		for (PencilType p : pencilTypeList) {
			panel.add(makeButton(p));
		}
//		panel.add(makeExitButton());
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
		JButton button = new JButton(Messages.getString("PencilBoxLauncher.quit")); //$NON-NLS-1$
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		return button;
	}
	
}
