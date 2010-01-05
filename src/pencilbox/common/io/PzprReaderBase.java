package pencilbox.common.io;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.PencilBoxException;
import pencilbox.common.core.SideAddress;
import pencilbox.common.core.Size;


/**
 * ぱずぷれv3形式のURL読み込み
 * 参考： Encode.js v3.2.3, v3.2.4
 * // p.html?(pid)/(qdata)
 * //               qdata -> [(pflag)/](cols)/(rows)/(bstr)
 */
public abstract class PzprReaderBase {
	
	private BoardBase bd;
//	String pid;
//	private String qdata;
	private String pflag;	// 入力されたURLのフラグ部分、現在対応している範囲では不使用
	protected int cols;
	protected int rows;
	private String bstr;
	/**
	 * テキスト形式の盤面を読み込む
	 * @param in 入力
	 * @return 読み込んで作成した盤面
	 * @throws IOException
	 */
	public BoardBase readProblem(String url) throws IOException, PencilBoxException {
		// pid はとばしてその次から渡す。パズルの種類は今開いてるものと同じものに限る。
		parseURI_pzpr(url.substring(url.indexOf('/', url.indexOf('?'))+1));
		bd = makeBoard();
		bd.setSize(new Size(rows, cols)); // pzprとPencilBoxでサイズが違う場合は、pzlimportの中でサイズ設定しなおす。
		pzlimport(bstr);
		return bd;
	}

	protected BoardBase makeBoard() {
		return null;
	}
	
	/**
	 * 	// enc.parseURI_pzpr()   pzlURI部をpflag,bstr等の部分に分割する
	 * 	// qstr = (pflag /) cols / rows / bstr(bstrの中に'/'を含みうる)
	 * @param qstr
	 */
	public void parseURI_pzpr(String qstr){
//		type = 0; // 0はぱずぷれv3
//		qdata = qstr;
		String[] inpa = qstr.split("/");
		LinkedList<String> inp	= new LinkedList<String>(Arrays.asList(inpa));
		try {
			parseInt(inp.getFirst());
			pflag = "";
		} catch (NumberFormatException e) {
			pflag = inp.removeFirst();
		}
		this.cols = parseInt(inp.removeFirst());
		this.rows = parseInt(inp.removeFirst());
		StringBuffer sb = new StringBuffer();
		// joinの代わり
		for (String s : inp) {
			if (sb.length()>0) { sb.append('/'); }
			sb.append(s);
		}
		this.bstr = sb.toString();
		System.out.println("bstr = " + bstr);
	}

	/**
	 * 各パズルのURL入力用(オーバーライド用)
	 * @return
	 */
	protected void pzlimport(String s) {
	}

	//---------------------------------------------------------------------------
	// enc.decode4Cell()  quesが0～4までの場合、デコードする
	//---------------------------------------------------------------------------
	protected String decode4Cell(String bstr){
		int c=0, i=0;
		for(i=0;i<bstr.length();i++){
			char ca = bstr.charAt(i);
			if     (include(ca,'0','4')){ sQnC(c, parseInt(ca,16));    c++; }
			else if(include(ca,'5','9')){ sQnC(c, parseInt(ca,16)-5);  c+=2;}
			else if(include(ca,'a','e')){ sQnC(c, parseInt(ca,16)-10); c+=3;}
			else if(include(ca,'g','z')){ c+=(parseInt(ca,36)-15);}
			else if(ca=='.'){ sQnC(c, -2); c++;}

			if(c >= rows*cols){ break;}
		}
		if (i+1 < bstr.length())
			return bstr.substring(i+1);
		else
			return "";
	}

	//---------------------------------------------------------------------------
	// enc.decodeNumber16()  quesが0～8192?までの場合、デコードする
	//---------------------------------------------------------------------------
	protected String decodeNumber16(String bstr){
		int c=0, i=0;
		for(i=0;i<bstr.length();i++){
			char ca = bstr.charAt(i);

			if(include(ca,'0','9')||include(ca,'a','f'))
							  { sQnC(c, parseInt(bstr.substring(i,  i+1),16));      c++;}
			else if(ca == '.'){ sQnC(c, -2);                                        c++;      }
			else if(ca == '-'){ sQnC(c, parseInt(bstr.substring(i+1,i+3),16));      c++; i+=2;}
			else if(ca == '+'){ sQnC(c, parseInt(bstr.substring(i+1,i+4),16));      c++; i+=3;}
			else if(ca == '='){ sQnC(c, parseInt(bstr.substring(i+1,i+4),16)+4096); c++; i+=3;}
			else if(ca == '%'){ sQnC(c, parseInt(bstr.substring(i+1,i+4),16)+8192); c++; i+=3;}
			else if(ca >= 'g' && ca <= 'z'){ c += (parseInt(ca,36)-15);}
			else{ c++;}

			if(c > rows*cols){ break;}
		}
		if (i < bstr.length())
			return bstr.substring(i);
		else
			return "";
	}

	protected int[] roomNumbers;
	//---------------------------------------------------------------------------
	// enc.decodeRoomNumber16()  部屋＋部屋の一つのquesが0～8192?までの場合、デコードする
	//---------------------------------------------------------------------------
	protected String decodeRoomNumber16(String bstr){
		int r = 0, i=0;
		for(i=0;i<bstr.length();i++){
			char ca = bstr.charAt(i);

			if(include(ca,'0','9')||include(ca,'a','f'))
							  { roomNumbers[r] = parseInt(bstr.substring(i,  i+1),16);      r++;}
			else if(ca == '-'){ roomNumbers[r] = parseInt(bstr.substring(i+1,i+2),16);      r++; i+=2;}
			else if(ca == '+'){ roomNumbers[r] = parseInt(bstr.substring(i+1,i+3),16);      r++; i+=3;}
			else if(ca == '='){ roomNumbers[r] = parseInt(bstr.substring(i+1,i+3),16)+4096; r++; i+=3;}
			else if(ca == '%'){ roomNumbers[r] = parseInt(bstr.substring(i+1,i+3),16)+8192; r++; i+=3;}
			else if(ca == '*'){ roomNumbers[r] = parseInt(bstr.substring(i+1,i+3),16)+12240; r++; i+=4;}
			else if(ca == '$'){ roomNumbers[r] = parseInt(bstr.substring(i+1,i+3),16)+77776; r++; i+=5;}
			else if(ca >= 'g' && ca <= 'z'){ r += (parseInt(ca,36)-15);}
			else{ r++;}

			if(r >= roomNumbers.length){ break;}
		}
		if (i < bstr.length())
			return bstr.substring(i);
		else
			return "";
	}

	protected String decodeArrowNumber16(String bstr){
		int c=0, i=0;
		for(i=0;i<bstr.length();i++){
			char ca = bstr.charAt(i);

			if     (ca=='0'){ sQnC(c, parseInt(bstr.substring(i+1,i+2),16)); c++; i++; }
			else if(ca=='5'){ sQnC(c, parseInt(bstr.substring(i+1,i+3),16)); c++; i+=2;}
			else if(include(ca,'1','4')){
				sDiC(c, parseInt(ca,16));
				if(bstr.charAt(i+1)!='.'){ sQnC(c, parseInt(bstr.substring(i+1,i+2),16));}
				else{ sQnC(c,-2);}
				c++; i++;
			}
			else if(include(ca,'6','9')){
				sDiC(c, parseInt(ca,16)-5);
				sQnC(c, parseInt(bstr.substring(i+1,i+3),16));
				c++; i+=2;
			}
			else if(ca>='a' && ca<='z'){ c+=(parseInt(ca,36)-9);}
			else{ c++;}

			if(c > rows*cols){ break;}
		}
		if (i < bstr.length())
			return bstr.substring(i);
		else
			return "";
	}

	//---------------------------------------------------------------------------
	// enc.decodeBorder() 問題の境界線をデコードする
	//---------------------------------------------------------------------------
	protected String decodeBorder(String bstr){
		int bdinside = (cols-1)*rows+cols*(rows-1);
		borders = new int[bdinside];

		int  pos1, pos2;

		if(bstr.length()>0){
			pos1 = Math.min(mf(((cols-1)*rows+4)/5)     , bstr.length());
			pos2 = Math.min(mf((cols*(rows-1)+4)/5)+pos1, bstr.length());
		}
		else{ pos1 = 0; pos2 = 0;}

		for(int i=0;i<pos1;i++){
			int ca = parseInt(bstr.charAt(i),32);
			for(int w=0;w<5;w++){
				if(i*5+w<(cols-1)*rows){ sQuB(i*5+w,(((ca&(1<<(4-w)))>0)?1:0));}
			}
		}

		int oft = (cols-1)*rows;
		for(int i=0;i<pos2-pos1;i++){
			int ca = parseInt(bstr.charAt(i+pos1),32);
			for(int w=0;w<5;w++){
				if(i*5+w<cols*(rows-1)){ sQuB(i*5+w+oft,(((ca&(1<<(4-w)))>0)?1:0));}
			}
		}
		if (pos2 < bstr.length())
			return bstr.substring(pos2);
		else
			return "";
	}

	protected int[] borders; 
	protected int[] areaIds;
	protected int nArea;

	/**
	 * 境界線ありなしデータを読み取った配列 borders を領域番号データの配列 areaIds に変換する。
	 */
	protected void makeAreaIDsFromBorders() {
		areaIds = new int[rows*cols];
		for (int i = 0; i < rows*cols; i++) {
			if (areaIds[i] == 0) {
				nArea ++;
				visit(i2a(i), nArea);
			}
		}
	}

	private void visit(Address a, int n) {
		if (areaIds[a2i(a)] == n)
			return;
		areaIds[a2i(a)] = n;
		for (int d = 0; d < 4; d++) {
			Address aa = Address.nextCell(a, d);
			SideAddress bb = SideAddress.get(a, d);
			if (isOn(aa) && borders[b2i(bb)] == 0)
				visit(aa, n);
		}
	}

	//---------------------------------------------------------------------------
	// enc.decodeCircle41_42() 白丸・黒丸をデコードする
	//---------------------------------------------------------------------------
	protected String decodeCircle41_42(String bstr){
		int pos = bstr.length()>0?Math.min(mf((bd.cols()*bd.rows()+2)/3), bstr.length()):0;
		for(int i=0;i<pos;i++){
			int ca = parseInt(bstr.charAt(i),27);
			for(int w=0;w<3;w++){
				if(i*3+w<bd.cols()*bd.rows()){
					if     (Math.floor(ca/Math.pow(3,2-w))%3==1){ sQuC(i*3+w,41);}
					else if(Math.floor(ca/Math.pow(3,2-w))%3==2){ sQuC(i*3+w,42);}
				}
			}
		}
		return bstr.substring(pos);
	}

	/**
	 * Boardクラスの行、列とこのクラスの行、列が異なる場合があるため、Board.isOn()は使えない。
	 * @param a
	 * @return
	 */
	public boolean isOn(Address a) {
		int r = a.r();
		int c = a.c();
		return (r >= 0 && r < rows && c >= 0 && c < cols);
	}

    public Address i2a(int i) {
    	return Address.address(i/cols, i%cols);
    }

    public int a2i(Address a) {
    	return a.r() * cols + a.c();
    }

    public int b2i(SideAddress b) {
		if (b.d() == 0)
			return b.r()*(cols-1)+b.c();
		else if (b.d() == 1)
			return rows*(cols-1)+b.r()*cols+b.c();
		else
			return -1;
    }

	public void sQuC(int id, int num) {
	}

	public void sQnC(int id, int num) {
	}

	public void sDiC(int id, int num) {
	}

	public void sQuB(int id, int num) {
		borders[id] = num;
	}

    public int mf(int i) {
    	return i;
    }

	//---------------------------------------------------------------------------
	// enc.include()    文字列caはbottomとupの間にあるか
	//---------------------------------------------------------------------------
	public static final boolean include(char ca, char bottom, char up){
		if(bottom <= ca && ca <= up) return true;
		return false;
	}

	public static final int parseInt(char c, int radix) {
		return Integer.parseInt(new String(new char[] {c}), radix);
	}

	public static final int parseInt(String s, int radix) {
		return Integer.parseInt(s, radix);
	}

	public static final int parseInt(String s) {
		return Integer.parseInt(s);
	}

}
