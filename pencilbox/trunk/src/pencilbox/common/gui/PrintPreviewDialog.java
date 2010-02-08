package pencilbox.common.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * ����ݒ�C�v���r���[�_�C�A���O
 * ���[�_���_�C�A���O�Ȃ̂ŁC1�̃C���X�^���X�݂̂𐶐����Ďg����
 */
public class PrintPreviewDialog extends JDialog {

	private static PrintPreviewDialog instance = new PrintPreviewDialog();

	private PrintRequestAttributeSet attributes;

	/**
	 * PrintPreviewDialog �C���X�^���X���擾����B
	 * @return ����v���r���[�C���X�^���X
	 */
	public static PrintPreviewDialog getInstance() {
		return instance;
	}

	private PrintPreviewCanvas pcanvas; 

	private PrintPreviewDialog() {
		super((Frame)null, true);
		this.setSize(611, 909);
		makeDialog();
		attributes = new HashPrintRequestAttributeSet();
	}

	void makeDialog() {
		this.setTitle("Print Preview");

		pcanvas = new PrintPreviewCanvas();

		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf = job.defaultPage();
		pcanvas.setPageFormat(pf);

		JPanel buttonPanel = new JPanel();

		JButton printButton = new JButton("Print");
		JButton pageSetupButton = new JButton("Page Setup");
//		JButton previewButton = new JButton("Preview");
		JButton closeButton = new JButton("Close");
		JLabel scalingSpinnerLabel = new JLabel("Scaling:");
		JLabel repeatXSpinnerLabel = new JLabel("Repeat X:");
		JLabel repeatYSpinnerLabel = new JLabel("Repeat Y:");

		/**
		 * �������
		 * �I����̓_�C�A���O�����
		 */
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				print();
				setVisible(false);
			}
		});
		/**
		 * �W���̃y�[�W�ݒ�_�C�A���O���ĂԁB
		 * �ݒ肪�ύX�����ƐV����PageFormat���Ԃ���邽�߁C�v���r���[��ʂ��X�V����B 
		 */
		pageSetupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pageSetup();
			}
		});

		/**
		 * �v���r���[�_�C�A���O
		 */
//		previewButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent event) {
//				JDialog d = new JDialog();
//				d.setModal(true);
//				d.getContentPane().add(pcanvas);
//				d.pack();
//				d.setVisible(true);
//			}
//		});

		closeButton.addActionListener(closeAction);

		final JSpinner scalingSpinner = new JSpinner(new SpinnerNumberModel(pcanvas.scaling, 0.099, 4.00, 0.05));
		scalingSpinner.setEditor(new JSpinner.NumberEditor(scalingSpinner, "#%"));
		scalingSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				pcanvas.scaling = ((SpinnerNumberModel)scalingSpinner.getModel()).getNumber().doubleValue();
				pcanvas.repaint();
			}
		});
		final JSpinner repeatXSpinner = new JSpinner(new SpinnerNumberModel(pcanvas.repeatX, 1, 4, 1));
		repeatXSpinner.setEditor(new JSpinner.NumberEditor(repeatXSpinner, "#"));
		repeatXSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				pcanvas.repeatX = ((SpinnerNumberModel)repeatXSpinner.getModel()).getNumber().intValue();
				pcanvas.repaint();
			}
		});
		final JSpinner repeatYSpinner = new JSpinner(new SpinnerNumberModel(pcanvas.repeatY, 1, 4, 1));
		repeatYSpinner.setEditor(new JSpinner.NumberEditor(repeatYSpinner, "#"));
		repeatYSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				pcanvas.repeatY = ((SpinnerNumberModel)repeatYSpinner.getModel()).getNumber().intValue();
				pcanvas.repaint();
			}
		});

		buttonPanel.add(printButton);
		buttonPanel.add(pageSetupButton);
//		buttonPanel.add(previewButton);
		buttonPanel.add(scalingSpinnerLabel);
		buttonPanel.add(scalingSpinner);
		buttonPanel.add(repeatXSpinnerLabel);
		buttonPanel.add(repeatXSpinner);
		buttonPanel.add(repeatYSpinnerLabel);
		buttonPanel.add(repeatYSpinner);
		buttonPanel.add(closeButton);

		Container contentPane = this.getContentPane();
//		if (false) { //	�X�N���[���y�C���ɓ����ꍇ�C
//			JScrollPane jScrollPane = new JScrollPane();
//			jScrollPane.getViewport().add(pcanvas, null);
//			contentPane.add(jScrollPane, BorderLayout.CENTER);
//		} else {
			contentPane.add(pcanvas, BorderLayout.CENTER);
//		}
		contentPane.add(buttonPanel, BorderLayout.NORTH);

		assignKeys();
	}

	/**
	 * �������B
	 * �X�P�[�����O�Ȃǂ̈���ݒ�𔽉f�����邽�߁CPanel���̂��̂łȂ��CPreviewCanvas���������B
	 */
	public void print() {
		PrinterJob job = PrinterJob.getPrinterJob();
		try {
			job.setPrintable(pcanvas);
            if (job.printDialog(attributes)) {  
               job.print(attributes);
            }
		} catch (PrinterException e) {
			JOptionPane.showMessageDialog(PrintPreviewDialog.this, e);
		}
	}	

	public void pageSetup() {
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf = job.pageDialog(attributes);
		if (pf != null) {
			pcanvas.setPageFormat(pf);
			pcanvas.repaint();
		}
	}

	/**
	 * ESC�L�[�Ń_�C�A���O�����
	 */
	private void assignKeys() {
		InputMap imap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
		getRootPane().getActionMap().put("close", closeAction);
	}

	private Action closeAction = new AbstractAction() {
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
		}
	};

	/**
	 * �v���r���[��ʂɃp�l����ݒ肷��
	 * @param printObject
	 */
	public void setPrintObject(Printable printObject) {
		pcanvas.printObject = printObject;
	}

	/**
	 * �_�C�A���O��\������
	 * @param frame
	 */
	public void showDialog(Frame frame) {
//		PrinterJob job = PrinterJob.getPrinterJob();
//		PageFormat pf = job.defaultPage();
//		PageFormat pf = job.getPageFormat(attributes);
//		if (pf == null) return;
//		pcanvas.setPageFormat(pf);
		this.setLocationRelativeTo(frame);
		this.setVisible(true);
	}

}

/**
 * �v�����g�v���r���[�̉�ʂƈ���p�`��
 */
class PrintPreviewCanvas extends JPanel implements Printable {

	Printable printObject;
	PageFormat pageFormat;
	double scaling = 0.70;  // ������̔{��
	int repeatX = 1;  // ���J��Ԃ���
	int repeatY = 1;  // �c�J��Ԃ���

	public PrintPreviewCanvas() {
	}

	/**
	 * �͂��߁C��������s�����Ƃ��C�y�[�W�ݒ��ύX�����Ƃ��ɁC
	 * PageFormat�ɉ����ăv���r���[���X�V����B
	 * @param pageFormat
	 */
	public void setPageFormat(PageFormat pageFormat) {
		this.pageFormat = pageFormat;
	}

	/* 
	 * �������
	 * �����ł�PageFormat�̓t�B�[���h�ł͂Ȃ������̂��̂��g���邱�Ƃɒ���
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if (page >= 1)
			return Printable.NO_SUCH_PAGE;
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(pf.getImageableX(), pf.getImageableY());
		drawPanel(g2);
		return Printable.PAGE_EXISTS;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		double px = pageFormat.getWidth();
		double py = pageFormat.getHeight();
		double scale = 1.0; // scale factor to fit page in window
		if (true) {    // �v���r���[���ʂ��_�C�A���O�̃T�C�Y�ɍ��킹�ăX�P�[�����O����ꍇ
			double xoff; // x offset of page start in window
			double yoff; // y offset of page start in window
			double sx = getWidth() - 1;
			double sy = getHeight() - 1;
			if (px / py < sx / sy) { // center horizontally
				scale = sy / py;
				xoff = 0.5 * (sx - scale * px);
				yoff = 0;
			} else { // center vertically
				scale = sx / px;
				xoff = 0;
				yoff = 0.5 * (sy - scale * py);
			}
			g2.translate(xoff, yoff);
		}
		g2.scale(scale, scale);
		// �y�[�W�O�g
		Rectangle2D pageOutline = new Rectangle2D.Double(0, 0, px, py);
		g2.setPaint(Color.white);
		g2.fill(pageOutline);
		g2.setPaint(Color.black);
		g2.draw(pageOutline);
//		// ����̈�̘g
//		Rectangle2D imageableOutline = new Rectangle2D.Double(pageFormat.getImageableX(), pageFormat.getImageableY(), pageFormat.getImageableWidth(), pageFormat.getImageableHeight());
//		g2.setPaint(Color.gray);
//		g2.draw(imageableOutline);
		g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		drawPanel(g2);
	}

	private void drawPanel(Graphics2D g2) {
		g2.scale(scaling, scaling);
		if (printObject instanceof PanelBase) {
			PanelBase p = ((PanelBase)printObject);
			Dimension d = p.getBoardRegionSize();
			for (int r = 0; r < repeatY; r++) {
				for (int c = 0; c < repeatX; c++) {
					p.drawPanel(g2);
					g2.translate(d.width+p.getCellSize(), 0);
				}
				g2.translate((d.width+p.getCellSize())*(-repeatX), d.height+p.getCellSize());
			}
		}
	}

}
