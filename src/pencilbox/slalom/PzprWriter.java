package pencilbox.slalom;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.Direction;
import pencilbox.common.io.PzprWriterBase;

/**
 * 参考：pzprv3 slalom.js
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "slalom";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		int ver = 1;
		if (ver == 1)
			outpflag("p");
		encodeSlalom(ver);
	}

	void encodeSlalom (int ver){
		String cm="";
		int count=0;
		for(int i=0;i<rows*cols;i++){
			String pstr="";
			int n = QuC(i);
			if     (n == 1){ pstr = "1";}
			else if(n ==21){ pstr = "2";}
			else if(n ==22){ pstr = "3";}
			else{ pstr = ""; count++;}

			if(count==0){ cm += pstr;}
			else if(pstr.length() > 0 || count==32){ cm+=(toString(3+count,36)+pstr); count=0;}
		}
		if(count>0){ cm+=toString(3+count, 36);}
		bd.initBoard();

		count=0;
		if (ver==0) { // 旧形式
			ArrayList<Address> gateList = makeGateList();

			for (int r=0; r<bd.getNGate(); r++) {
				String pstr = "";
				int val = bd.getGateNumber(gateList.get(r));
				if     (val>= 1 && val< 16){ pstr =       toString(val,16);}
				else if(val>=16 && val<256){ pstr = "-" + toString(val,16);}
				else{ count++;}

				if(count==0){ cm += pstr;}
				else if(pstr.length()>0 || count==20){ cm+=(toString((15+count),36)+pstr); count=0;}
			}
			if(count>0){ cm+=toString((15+count),36);}
		}
		else if (ver==1) {
			for(int c=0;c<rows*cols;c++){
				if(QuC(c)!=1){ continue;}

				String pstr = "";
				int val = QnC(c);

				if     (val>= 1 && val< 16){ pstr =       toString(val,16);}
				else if(val>=16 && val<256){ pstr = "-" + toString(val,16);}
				else{ count++;}

				if(count==0){ cm += pstr;}
				else if(pstr.length()>0 || count==20){ cm+=(toString((15+count),36)+pstr); count=0;}
			}
			if(count>0){ cm+=toString((15+count),36);}
		}
		int startid = a2i(bd.getGoal());
		if (bd.getGoal().isNowhere())
			startid = 0;
		cm += ("/"+startid);

		outbstr(cm);
	}

	private ArrayList<Address> makeGateList() {
		ArrayList<Address> gateList = new ArrayList<Address>();
		for (Address a : bd.cellAddrs()) {
			int n = bd.getNumber(a);
			if (n == Board.GATE_HORIZ) {
				Address aa = Address.nextCell(a, Direction.LT);
				if (bd.isOn(aa) && bd.getNumber(aa) == Board.GATE_HORIZ) {
				} else {
					gateList.add(a);
				}
			} else if (n == Board.GATE_VERT) {
				Address aa = Address.nextCell(a, Direction.UP);
				if (bd.isOn(aa) && bd.getNumber(aa) == Board.GATE_VERT) {
				} else {
					gateList.add(a);
				}
			}
		}
		return gateList;
	}

	protected int QuC(int i) {
		int n = bd.getNumber(i2a(i));
		if (n >= 0)
			return 1;
		else if (n == Board.GATE_VERT)
			return 21;
		else if (n == Board.GATE_HORIZ)
			return 22;
		return 0;
	}

	protected int QnC(int i) {
		int n = bd.getNumber(i2a(i));
		if (n > 0)
			return n;
		else if (n == 0)
			return -2;
		else
			return -1;
	}
}
