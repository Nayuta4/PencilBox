package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * �F�̃A�C�R��
 */
public class ColorIcon implements Icon {
	
	/** �A�C�R���Ƃ��ĕ\������F */
	private Color color;
	/** �g�̐F */
	private Color borderColor;
	/** �A�C�R���̕� */
	private int width;
	/** �A�C�R���̍��� */
	private int height;
	
	
	/**
	 * �F�̃A�C�R���̃R���X�g���N�^
	 * 
	 * @param color �A�C�R���Ƃ��ĕ\������F
	 * @param width �A�C�R���̕�
	 * @param height �A�C�R���̍���
	 */
	public ColorIcon(Color color, int width, int height) {
		this(color, color, width, height);
	}
	
	/**
	 * �F�̃A�C�R���̃R���X�g���N�^
	 * 
	 * @param color �A�C�R���Ƃ��ĕ\������F
	 * @param borderColor �g�̐F
	 * @param width �A�C�R���̕�
	 * @param height �A�C�R���̍���
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
	 * @param c �R���|�[�l���g
	 * @param g �O���t�B�b�N
	 * @param x �A�C�R����`�悷��x���W
	 * @param y �A�C�R����`�悷��y���W
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
	 * �A�C�R���̕����擾����
	 * 
	 * @return �A�C�R���̕�
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return width;
	}

	/**
	 * �A�C�R���̍������擾����
	 * 
	 * @return �A�C�R���̍���
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return height;
	}
	
	/**
	 * �A�C�R���Ƃ��ĕ\������F���擾����
	 * 
	 * @return �A�C�R���Ƃ��ĕ\������F
	 */
	public Color getColor() {
		return color;
	}
}
