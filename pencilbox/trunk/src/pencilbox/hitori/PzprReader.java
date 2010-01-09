package pencilbox.hitori;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * éQçlÅFpzprv3 hitori.js
 */
public class PzprReader extends PzprReaderBase {
	
	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		decodeHitori();
	}

	private void decodeHitori(){
		int c=0, i=0;
		String bstr = this.outbstr;
		for(i=0;i<bstr.length();i++){
			char ca = bstr.charAt(i);

			if(include(ca,'0','9')||include(ca,'a','z')){ sQnC(c, parseInt(bstr.substring(i,i+1),36)); c++;}
			else if(ca == '-'){ sQnC(c, parseInt(bstr.substring(i+1,i+3),36)); c++; i+=2;}
			else if(ca == '%'){ sQnC(c, -2);                                   c++;      }
			else{ c++;}

			if(c > rows*cols){ break;}
		}
		this.outbstr(bstr, i);
	}

	protected void sQnC(int id, int num) {
		if (num == -2)
			bd.setNumber(i2a(id), Board.UNDECIDED_NUMBER);
		else
			bd.setNumber(i2a(id), num);
	}

}
