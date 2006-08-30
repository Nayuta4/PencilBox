package pencilbox.common.gui;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;



/**
 * �p�l���̕\�����e�� �N���b�v�{�[�h�ɃR�s�[���鏈�����s���N���X
 */
/*
 * �Q�l�Fcore JAVA2 Vol.2 7.13 
 */
public class PanelImageTransfer {  

	private BufferedImage image;
	private PanelBase panel;	

	/**
	 * �p�l���̕\�����e�� �N���b�v�{�[�h�ɃR�s�[�����A�̏������s��
	 * @param panel �N���b�v�{�[�h�ɃR�s�[����p�l��
	 */
	public void run(PanelBase panel) {
		this.panel = panel;
		makePanelImage();
		copy();
	}

	private void makePanelImage() {
		image = new BufferedImage(panel.getBoardRegionSize().width, panel.getBoardRegionSize().height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
	 	// �w�i�𔒂œh��B�������Ȃ��ƁC�w�i�������Ȃ��Ă��܂�
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		panel.drawPanel(g2);
	}

   /**
	  Copies the current image to the system clipboard.
   */
   private void copy() {  
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  ImageSelection selection = new ImageSelection(image);
	  clipboard.setContents(selection, null);
   }

}

/**
   This class is a wrapper for the data transfer of image objects.
*/
class ImageSelection implements Transferable
{  

	private Image theImage;

   /**
	  Constructs the selection.
	  @param image an image
   */
   public ImageSelection(Image image)
   {  
	  theImage = image;
   }

   public DataFlavor[] getTransferDataFlavors()
   {  
	  return new DataFlavor[] { DataFlavor.imageFlavor };
   }

   public boolean isDataFlavorSupported(DataFlavor flavor)
   {  
	  return flavor.equals(DataFlavor.imageFlavor);
   }

   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
   {  
	  if(flavor.equals(DataFlavor.imageFlavor))
	  {  
		 return theImage;
	  }
	  else
	  {  
		 throw new UnsupportedFlavorException(flavor);
	  }
   }

}
