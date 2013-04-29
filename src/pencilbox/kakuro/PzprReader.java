package pencilbox.kakuro;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.Size;
import pencilbox.common.io.PzprReaderBase;


/**
 * 参考：pzprv3 kakuro.js
 */
public class PzprReader extends PzprReaderBase {

	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		bd.setSize(new Size(rows+1, cols+1));
		decodeKakuro();
	}

	protected void decodeKakuro(){
		// 盤面内数字のデコード
		int cell=0, a=0;
		String bstr = outbstr;
		for(int i=0;i<bstr.length();i++){
			char ca = bstr.charAt(i);
			if(ca>='k' && ca<='z'){ cell+=(parseInt(ca,36)-19);}
			else{
				sQuC(cell,51);
				if(ca!='.'){
					sDiC(cell,this.decval(ca));
					sQnC(cell,this.decval(bstr.charAt(i+1)));
					i++;
				}
				cell++;
			}
			if(cell>=rows*cols){ a=i+1; break;}
		}

		// 盤面外数字のデコード
		cell=0;
		for(int i=a;i<bstr.length();i++){
			char ca = bstr.charAt(i);
			while(cell<cols){
				if(QuC(cnum(cell,0))!=51){ sDiE(cell,this.decval(ca)); cell++; i++; break;}
				cell++;
			}
			if(cell>=cols){ a=i; break;}
			i--;
		}
		for(int i=a;i<bstr.length();i++){
			char ca = bstr.charAt(i);
			while(cell<cols+rows){
				if(QuC(cnum(0,cell-cols))!=51){ sQnE(cell,this.decval(ca)); cell++; i++; break;}
				cell++;
			}
			if(cell>=cols+rows){ a=i; break;}
			i--;
		}

		this.outbstr(bstr, a);
	}

	private int decval(char ca){
		if     (ca>='0'&&ca<='9'){ return parseInt(ca,36);}
		else if(ca>='a'&&ca<='j'){ return parseInt(ca,36);}
		else if(ca>='A'&&ca<='Z'){ return parseInt(ca,36)+10;}
		return -1;
	}

	public int cnum(int cx, int cy) {
		return (cx>=0&&cx<=cols-1&&cy>=0&&cy<=rows-1)?cx+cy*cols:-1;
	}

	public int QuC(int i) {
		if (bd.isWall(i2aKK(i)))
			return 51;
		else
			return -1;
	}

	protected void sQuC(int i, int n) {
		if (n == 51)
			bd.setWall(i2aKK(i), 0, 0);
	}

	protected void sQnC(int i, int n) {
		bd.setSum(i2aKK(i), Direction.HORIZ, n);
	}

	protected void sDiC(int i, int n) {
		bd.setSum(i2aKK(i), Direction.VERT, n);
	}

	public void sQnE(int i, int n) {
		if (i >= 0 && i < cols)
			bd.setSum(Address.address(0, i+1), Direction.HORIZ, n);
		else if (i >= cols && i < cols+rows)
			bd.setSum(Address.address(i-cols+1, 0), Direction.HORIZ, n);
	}

	public void sDiE(int i, int n) {
		if (i >= 0 && i < cols)
			bd.setSum(Address.address(0, i+1), Direction.VERT, n);
		else if (i >= cols && i < cols+rows)
			bd.setSum(Address.address(i-cols+1, 0), Direction.VERT, n);
	}

	protected Address i2aKK(int i) {
		return Address.address((i/cols)+1, (i%cols)+1);
	}

}
