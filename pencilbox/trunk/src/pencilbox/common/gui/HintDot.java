package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Graphics2D;



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
	private Color dotColor = new Color(255, 175, 175);
	private int dotSize = 3;
	private int dotPitch = 5;
	private int dotOffset = 1;
	private int unit = 3;
	private int maxNumber = 9;
	private PanelBase panel;

	/**
	 * @param panel
	 * @param unit
	 * @param cellSize
	 */
	public void setDot(PanelBase panel, int unit, int cellSize) {
		this.panel = panel;
		this.unit = unit;
		this.maxNumber = unit * unit;
		setDotSize(cellSize);
	}
	/**
	 * パネルのセルサイズの変更に応じて，点のサイズを変更する
	 * @param cellSize パネルのセルピッチのピクセル数
	 */
	public void setDotSize(int cellSize) {
		dotPitch = cellSize / unit;
		if (dotPitch <= 3)
			dotSize = 2;
		else
			dotSize = (dotPitch + 1) / 2;
		dotOffset = (cellSize - dotPitch * (unit-1) - dotSize) / 2;
	}
	/**
	 * @param g
	 * @param r
	 * @param c
	 */
	public void placeHintCross(Graphics2D g, int r, int c) {
		g.setColor(Color.RED);
		panel.placeCross(g, r, c);
	}
	/**
	 * @param g
	 * @param r
	 * @param c
	 * @param pattern
	 */
	public void placeHintDot(Graphics2D g, int r, int c, int pattern) {
		g.setColor(dotColor);
		int x = panel.toX(c);
		int y = panel.toY(r);
		for (int d = 1; d <= maxNumber; d++) {
			if ((pattern & DIGIT[d]) != 0) {
				int dy = (d - 1) / unit;
				int dx = (d - 1) % unit;
				g.fillRect(
					x + dotOffset + dotPitch * dx,
					y + dotOffset + dotPitch * dy,
					dotSize,
					dotSize);
			}
		}
	}
}
