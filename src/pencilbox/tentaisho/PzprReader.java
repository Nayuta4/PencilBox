package pencilbox.tentaisho;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * 参考：pzprv3 tentaisho.js
 */
public class PzprReader extends PzprReaderBase {

	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		decodeStar();
	}

	protected void decodeStar(){
		int s=0;
		String bstr = outbstr;
		int i = 0;
		for(i=0;i<bstr.length();i++){
			char ca = bstr.charAt(i);
			if(include(ca,'0','f')){
				int val = parseInt(ca,16);
				setStar(s,val%2+1);
				s+=(mf(val/2)+1);
			}
			else if(include(ca,'g','z')){ s+=(parseInt(ca,36)-15);}

			if(s>=(2*cols-1)*(2*rows-1)){ break;}
		}
		this.outbstr(bstr, i+1);
	}

	public void setStar(int i, int n) {
		bd.setStar(Address.address(i/(cols*2-1), i%(cols*2-1)), n);
	}

}
