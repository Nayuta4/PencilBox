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
 * 印刷設定，プレビューダイアログ
 * モーダルダイアログなので，1つのインスタンスのみを生成して使い回す
 */
public class PrintPreviewDialog extends JDialog {

	private static PrintPreviewDialog instance = new PrintPreviewDialog();

	private PrintRequestAttributeSet attributes;

	/**
	 * PrintPreviewDialog インスタンスを取得する。
	 * @return 印刷プレビューインスタンス
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
		 * 印刷する
		 * 終了後はダイアログを閉じる
		 */
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				print();
				setVisible(false);
			}
		});
		/**
		 * 標準のページ設定ダイアログを呼ぶ。
		 * 設定が変更されると新しいPageFormatが返されるため，プレビュー画面を更新する。 
		 */
		pageSetupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pageSetup();
			}
		});

		/**
		 * プリビューダイアログ
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
//		if (false) { //	スクロールペインに入れる場合，
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
	 * 印刷する。
	 * スケーリングなどの印刷設定を反映させるため，Panelそのものでなく，PreviewCanvasを印刷する。
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
	 * ESCキーでダイアログを閉じる
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
	 * プレビュー画面にパネルを設定する
	 * @param printObject
	 */
	public void setPrintObject(Printable printObject) {
		pcanvas.printObject = printObject;
	}

	/**
	 * ダイアログを表示する
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
 * プリントプレビューの画面と印刷用描画
 */
class PrintPreviewCanvas extends JPanel implements Printable {

	Printable printObject;
	PageFormat pageFormat;
	double scaling = 0.70;  // 印刷時の倍率
	int repeatX = 1;  // 横繰り返し回数
	int repeatY = 1;  // 縦繰り返し回数

	public PrintPreviewCanvas() {
	}

	/**
	 * はじめ，印刷を実行したとき，ページ設定を変更したときに，
	 * PageFormatに応じてプレビューを更新する。
	 * @param pageFormat
	 */
	public void setPageFormat(PageFormat pageFormat) {
		this.pageFormat = pageFormat;
	}

	/* 
	 * 印刷する
	 * ここでのPageFormatはフィールドではなく引数のものが使われることに注意
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
		if (true) {    // プレビュー紙面をダイアログのサイズに合わせてスケーリングする場合
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
		// ページ外枠
		Rectangle2D pageOutline = new Rectangle2D.Double(0, 0, px, py);
		g2.setPaint(Color.white);
		g2.fill(pageOutline);
		g2.setPaint(Color.black);
		g2.draw(pageOutline);
//		// 印刷領域の枠
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
