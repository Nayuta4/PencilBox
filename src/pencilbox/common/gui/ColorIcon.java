package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * 色のアイコン
 */
public class ColorIcon implements Icon {

	/** アイコンとして表示する色 */
	private Color color;
	/** 枠の色 */
	private Color borderColor;
	/** アイコンの幅 */
	private int width;
	/** アイコンの高さ */
	private int height;


	/**
	 * 色のアイコンのコンストラクタ
	 * 
	 * @param color アイコンとして表示する色
	 * @param width アイコンの幅
	 * @param height アイコンの高さ
	 */
	public ColorIcon(Color color, int width, int height) {
		this(color, color, width, height);
	}

	/**
	 * 色のアイコンのコンストラクタ
	 * 
	 * @param color アイコンとして表示する色
	 * @param borderColor 枠の色
	 * @param width アイコンの幅
	 * @param height アイコンの高さ
	 */
	public ColorIcon(Color color, Color borderColor, int width, int height) {
		if (color == null) {
			throw new NullPointerException();
		}
		if (borderColor == null) {
			throw new NullPointerException();
		}

		this.color = color;
		this.borderColor = borderColor;
		this.width = width;
		this.height = height;
	}

	/**
	 * @param c コンポーネント
	 * @param g グラフィック
	 * @param x アイコンを描画するx座標
	 * @param y アイコンを描画するy座標
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {

		g.setColor(color);
		g.fillRect(x, y, width, height);
		if (!color.equals(borderColor)) {
			g.setColor(borderColor);
			g.drawRect(x, y, width - 1, height - 1);
		}
	}

	/**
	 * アイコンの幅を取得する
	 * 
	 * @return アイコンの幅
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return width;
	}

	/**
	 * アイコンの高さを取得する
	 * 
	 * @return アイコンの高さ
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return height;
	}

	/**
	 * アイコンとして表示する色を取得する
	 * 
	 * @return アイコンとして表示する色
	 */
	public Color getColor() {
		return color;
	}
}
