package pencilbox.tentaisho;

import pencilbox.common.core.Address;
import pencilbox.common.io.PzprWriterBase;


/**
 * éQçlÅFpzprv3 tentaisho.js
 */
public class PzprWriter extends PzprWriterBase {
	
	private Board bd;
	
	protected String getPzprName() {
		return "tentaisho";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		this.encodeStar();
	}

	private void encodeStar(){
		int count = 0;
		String cm = "";

		for(int s=0;s<(2*cols-1)*(2*rows-1);s++){
			String pstr = "";
			if(getStar(s)>0){
				for(int i=0;i<=6;i++){
					if(getStar(s+i+1)>0){ pstr=""+toString((2*i+(getStar(s)-1)),16); s+=i; break;}
				}
				if(pstr==""){ pstr=toString((13+getStar(s)),16); s+=7;}
			}
			else{ pstr=" "; count++;}

			if(count==0)      { cm += pstr;}
			else if(pstr!=" "){ cm += (toString((count+15),36)+pstr); count=0;}
			else if(count==20){ cm += "z"; count=0;}
		}
		if(count>0){ cm += (toString((count+15),36));}

		outbstr(cm);
	}

	protected int getStar(int id) {
		if(id<0||(2*cols-1)*(2*rows-1)<=id){ return -1;}
		return bd.getStar(Address.address(id/(cols*2-1), id%(cols*2-1)));
	}

}
