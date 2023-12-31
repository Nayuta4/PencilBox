package pencilbox.kakuro;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.io.PzprWriterBase;


/**
 * 参考：pzprv3 kakuro.js
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "kakuro";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows()-1, bd.cols()-1);
		this.encodeKakuro();
	}

	protected void encodeKakuro(){
		String cm="";

		// 盤面内側の数字部分のエンコード
		int count=0;
		for(int c=0;c<rows*cols;c++){
			String pstr = "";

			if(QuC(c)==51){
				if(QnC(c)<=0 && DiC(c)<=0){ pstr = ".";}
				else{ pstr = ""+this.encval(DiC(c))+this.encval(QnC(c));}
			}
			else{ pstr=" "; count++;}

			if     (count== 0){ cm += pstr;}
			else if(pstr!=" "){ cm += (toString((count+19),36)+pstr); count=0;}
			else if(count==16){ cm += "z"; count=0;}
		}
		if(count>0){ cm += toString((count+19),36);}

		// 盤面外側の数字部分のエンコード
		for(int c=0;c<cols;c++){ if(QuC(cnum(c,0))!=51){ cm+=this.encval(DiE(c));} }
		for(int c=cols;c<cols+rows;c++){ if(QuC(cnum(0,c-cols))!=51){ cm+=this.encval(QnE(c));} }

		outbstr(cm);
	}

	String encval(int val){
		if     (val>= 1&&val<=19){ return toString(val,36).toLowerCase();}
		else if(val>=20&&val<=45){ return toString((val-10),36).toUpperCase();}
		return "0";
	}

	public int cnum(int cx, int cy) {
		return (cx>=0&&cx<=cols-1&&cy>=0&&cy<=rows-1)?cx+cy*cols:-1;
	}

	protected int QuC(int i) {
		if (bd.isWall(i2aKK(i)))
			return 51;
		else
			return -1;
	}

	protected int QnC(int i) {
		int n = bd.getSum(i2aKK(i), Direction.HORIZ);
		return n;
	}

	protected int DiC(int i) {
		int n = bd.getSum(i2aKK(i), Direction.VERT);
		return n;
	}

	protected int QnE(int i) {
		int n = 0;
		if (i >= 0 && i < cols)
			n = bd.getSum(Address.address(0, i+1), Direction.HORIZ);
		else if (i >= cols && i < cols+rows)
			n = bd.getSum(Address.address(i-cols+1, 0), Direction.HORIZ);
		return n;
	}

	protected int DiE(int i) {
		int n = 0;
		if (i >= 0 && i < cols)
			n = bd.getSum(Address.address(0, i+1), Direction.VERT);
		else if (i >= cols && i < cols+rows)
			n = bd.getSum(Address.address(i-cols+1, 0), Direction.VERT);
		return n;
	}

	protected Address i2aKK(int i) {
		return Address.address((i/cols)+1, (i%cols)+1);
	}
}
