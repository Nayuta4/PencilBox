package pencilbox.goishi;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * éQçlÅFpzprv3 goishi.js
 */
public class PzprReader extends PzprReaderBase {
	
	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		decodeBoard();
	}

	protected void decodeBoard() {
		String bstr = this.outbstr;
		int i = 0;
		for(i=0;i<bstr.length();i++){
			int num = parseInt(bstr.charAt(i),32);
			for(int w=0;w<5;w++){ if((i*5+w)<rows*cols){ sQuC(i*5+w,((num&(1<<(4-w)))>0?0:7));} }
			if((i*5+5)>=cols*rows){ break;}
		}
		outbstr(bstr,(i+1));
	}
	protected void sQuC(int id, int num) {
		if (num == 7)
			bd.setState(i2a(id), Board.STONE);
		else if (num == 0)
			bd.setState(i2a(id), Board.BLANK);
	}

}
