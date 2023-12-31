package pencilbox.slalom;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.io.PzprReaderBase;


/**
 * 参考：pzprv3 slalom.js
 */
public class PzprReader extends PzprReaderBase {

	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport() {
		int ver = checkpflag("p") ? 1 : 0;
		decodeSlalom(ver);
	}

	void decodeSlalom(int ver){
		String bstr = outbstr;
		String[] array = bstr.split("/");

		int c=0, i=0;
		for(i=0; i<array[0].length(); i++){
			char ca = array[0].charAt(i);
			System.out.println(c + " : " + ca + " : " + (ca-'0'-3));

			if     (ca=='1'){ sQuC(c,  1); c++;}
			else if(ca=='2'){ sQuC(c, 21); c++;}
			else if(ca=='3'){ sQuC(c, 22); c++;}
			else if(include(ca,'4','z')){ c += (parseInt(ca,36)-3);}
			else{ c++;}

			if(c >= bd.rows() * bd.cols()){ break;}
		}
		bd.initGates(); // 門の数を数えるためにいったん門を調べる
		int nGate = bd.getNGate();
		System.out.println(nGate);

		if (ver==0) {
			int[] gateNumbers = new int[nGate]; // 門番号を順番に記録するローカル変数配列。

			int i0 = i+1, r = 0;
			for(i=i0;i<array[0].length();i++){
				char ca = array[0].charAt(i);
				System.out.println("ca: " + ca);

				if(include(ca,'0','f')){
					gateNumbers[r] = parseInt(array[0].substring(i, i+1), 16); r++;
				}
				else if(ca == '-'){
					gateNumbers[r] = parseInt(array[0].substring(i+1, i+3), 16); r++; i+=2;
				}
				else if(include(ca,'g','z')){				// 番号なしの門の数 gなら1つ
					r += parseInt(ca,36)-15;
				}
				else{ r++;}
				System.out.println((r-1) + " : " + gateNumbers[(r-1)]);
				if(r > nGate){ break;}
			}

			this.setGateNumbers(gateNumbers); // ローカル変数に記録した門番号を盤面に設定する.
		}
		else if(ver==1) { // pflag == "p"
			c=0;
			int i0=i+1, spare=0;
			for(i=i0;i<array[0].length();i++){
				if(!bd.isWall(i2a(c))){ i--;}
				else if(spare>0){ i--; spare--;}
				else{
					char ca = array[0].charAt(i);

					if(include(ca,'0','9')||include(ca,'a','f')){ sQnC(c, parseInt(bstr.substring(i,i+1),16));}
					else if(ca=='-'){ sQnC(c, parseInt(bstr.substring(i+1,i+3),16)); i+=2;}
					else if(ca>='g' && ca<='z'){ spare = (parseInt(ca,36)-15) - 1;}
				}
				c++;
				if(c > rows*cols){ break;}
			}
		}

		if (array.length>=2) {
			int startid = parseInt(array[1]);
			if (isOn(i2a(startid)))
				bd.setNumber(i2a(startid), Board.GOAL);
		}
		outbstr(array[0], i);
	}

	/**
	 * 門に番号を設定する。
	 * 門にIDがついていないので、まとめて処理することにする。
	 * 引数に与えた配列の数字を，左上にある門から順番にわりあてる。
	 * @param gateNumbers 門番号の配列
	 */
	private void setGateNumbers(int[] gateNumbers) {
		int k = 0;
		for (Address a : bd.cellAddrs()) {
			int n = bd.getNumber(a);
			if (n == Board.GATE_HORIZ) {
				Address aa = Address.nextCell(a, Direction.LT);
				if (bd.isOn(aa) && bd.getNumber(aa) == Board.GATE_HORIZ) {
				} else {
					setGateNumber(a, Direction.LT, gateNumbers[k]);
					setGateNumber(a, Direction.RT, gateNumbers[k]);
					k++;
				}
			} else if (n == Board.GATE_VERT) {
				Address aa = Address.nextCell(a, Direction.UP);
				if (bd.isOn(aa) && bd.getNumber(aa) == Board.GATE_VERT) {
				} else {
					setGateNumber(a, Direction.UP, gateNumbers[k]);
					setGateNumber(a, Direction.DN, gateNumbers[k]);
					k++;
				}
			}
		}
	}

	/**
	 * 起点マスから与えられた方向の先の門の上に番号を書く。
	 * 複数の数字が該当する場合は小さい数字を優先する。
	 * @param p　座標
	 * @param direction 見る方向
	 * @param v 値
	 */
	private void setGateNumber(Address p, int d, int v) {
		Address pole = bd.getAnotherPole(p, d);
		if (isOn(pole)) {
			int n = bd.getNumber(pole);
			if (n ==0 || (n > 0 && v > 0 && v < n)) {
				bd.setNumber(pole, v);
			}
		}
	}

	protected void sQuC(int id, int num) {
		Address a = i2a(id);
		if (num == 1) {
			bd.setNumber(a, 0); // 仮に番号なしの黒マスとする
		} else if (num == 21) {
			bd.setNumber(a, Board.GATE_VERT);
		} else if (num == 22) {
			bd.setNumber(a, Board.GATE_HORIZ);
		}
	}

	protected void sQnC(int id, int num) {
		Address a = i2a(id);
		if (num > 0) {
			bd.setNumber(a, num);
		}
	}
}
