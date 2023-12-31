package pencilbox.hitori;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprWriterBase;


/**
 * 参考：pzprv3 hitori.js
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "hitori";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		this.encodeHitori();
	}

	private void encodeHitori(){
		int count=0;
		String cm="";
		for(int i=0;i<bd.rows()*bd.cols();i++){
			String pstr = "";
			int val = QnC(i);

			if     (val==-2           ){ pstr = "%";}
			else if(val>= 0 && val< 16){ pstr =       toString(val, 36);}
			else if(val>=16 && val<256){ pstr = "-" + toString(val, 36);}
			else{ count++;}

			if(count==0){ cm += pstr;}
			else{ cm+="."; count=0;}
		}
		if(count>0){ cm+=".";}

		outbstr(cm);
	}

	protected int QnC(int i) {
		int n = bd.getNumber(i2a(i));
		if (n == 0)
			return -1;
		else if (n == -1)
			return -2;
		else
			return n;
	}

}
