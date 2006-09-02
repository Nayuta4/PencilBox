package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Graphics;



/**
 * 数独等のドットヒントの表示を行うクラス
 */
public class HintDot{

	private static int[] DIGIT;
	static {
		DIGIT = new int[26];
		for (int i = 0; i <= 25; i++) {
			DIGIT[i] = 1 << i;
		}
	}
	private Color dotColor = Color.MAGENTA;
	private int dotSize = 6;
	private int dotDist = 6;
	private int dotOffset = 1;
	private int unit = 3;
	private int maxNumber = 9;
	private PanelBase panel;

	/**
	 * @param panel
	 * @param unit
	 * @param cellSize
	 */
	public void setDot(PanelBase panel, int unit, int cellSize){
		this.panel = panel;
		maxNumber = unit * unit;
		this.unit = unit;
		setDotSize(cellSize);
	}
	/**
	 * パネルのセルサイズの変更に応じて，点のサイズを変更する
	 * @param cellSize パネルのセルピッチのピクセル数
	 */
	public void setDotSize(int cellSize) {
		int s = (cellSize - 3) / unit;
		if (s <= 3)
			dotSize = 2;
		else
			dotSize = s - 2;
		dotOffset = 2;
		dotDist = (cellSize - dotOffset * 2 - dotSize) / (unit - 1);
	}
	/**
	 * @param g
	 * @param r
	 * @param c
	 */
	public void placeHintCross(Graphics g, int r, int c) {
		g.setColor(Color.red);
		panel.placeCross(g, r, c);
	}
	/**
	 * @param g
	 * @param r
	 * @param c
	 * @param pattern
	 */
	public void placeHintDot(Graphics g, int r, int c, int pattern) {
		g.setColor(dotColor);
		int x = panel.toX(c) + dotOffset;
		int y = panel.toY(r) + dotOffset;
		for (int d = 1; d <= maxNumber; d++) {
			if ((pattern & DIGIT[d]) != 0) {
				int dy = (d - 1) / unit;
				int dx = (d - 1) % unit;
				if (dotSize >= 5) {
					g.fillOval(
						x + dotDist * dx,
						y + dotDist * dy,
						dotSize,
						dotSize);
				} else {
					g.fillRect(
						x + dotDist * dx,
						y + dotDist * dy,
						dotSize,
						dotSize);
				}
			}
		}
	}
}
