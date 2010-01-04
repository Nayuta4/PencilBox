package pencilbox.common.io;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;

/**
 * ぱずぷれv3形式のURL書き出し
 * 参考： Encode.js v3.2.3, v3.2.4
 * // p.html?(pid)/(qdata)
 * //               qdata -> [(pflag)/](cols)/(rows)/(bstr)
 */
public abstract class PzprWriterBase {

	private BoardBase bd;
	protected int rows;
	protected int cols;

	/**
	 * 盤面データの文字列を生成する。
	 * 回答状態は無視して，問題データのみ書き出す
	 * @param board 書き出す盤面
	 * @return
	 */
	public String writeQuestion(BoardBase board) {
		this.bd = board;
		this.rows = board.rows(); // 異なるサイズの場合は pzlexport の中で上書きする
		this.cols = board.cols(); // 異なるサイズの場合は pzlexport の中で上書き する
		String bstr = this.pzlexport(bd);
		return getPzprName() + '/' + cols + '/' + rows + '/' + bstr;
	}

	/**
	 * ぱずぷれでの名前。各サブクラスで記述する
	 * @return
	 */
	protected String getPzprName() {
		return "";
	}

	/**
	 * 各パズルのURL出力用(オーバーライド用)
	 * @return
	 */
	protected String pzlexport(BoardBase bd) {
		return "";
	}

	//---------------------------------------------------------------------------
	// enc.encode4Cell()  quesが0～4までの場合、問題部をエンコードする
	//---------------------------------------------------------------------------
	protected String encode4Cell(){
		int count = 0;
		String cm = "";
		for(int i=0;i<rows*cols;i++){
			String pstr = "";
			if(QnC(i)>=0){
				if     (i<rows*cols-1&&(QnC(i+1)>=0||QnC(i+1)==-2)){ pstr=""+toString(QnC(i),16);}
				else if(i<rows*cols-2&&(QnC(i+2)>=0||QnC(i+2)==-2)){ pstr=""+toString((5+QnC(i)),16); i++;}
				else{ pstr=""+(toString(10+QnC(i),16)); i+=2;}
			}
			else if(QnC(i)==-2){ pstr=".";}
			else{ pstr=" "; count++;}

			if(count==0)      { cm += pstr;}
			else if(pstr!=" "){ cm += (toString((count+15),36)+pstr); count=0;}
			else if(count==20){ cm += "z"; count=0;}
		}
		if(count>0){ cm += (toString((count+15),36));}

		return cm;
	}

	//---------------------------------------------------------------------------
	// enc.encodeNumber16()  quesが0～8192?までの場合、問題部をエンコードする
	//---------------------------------------------------------------------------
	protected String encodeNumber16(){
		int count=0;
		String cm="";
		for(int i=0;i<rows*cols;i++){
			String pstr = "";
			int val = QnC(i);

			if     (val==  -2            ){ pstr = ".";}
			else if(val>=   0 && val<  16){ pstr =       toString(val, 16);}
			else if(val>=  16 && val< 256){ pstr = "-" + toString(val, 16);}
			else if(val>= 256 && val<4096){ pstr = "+" + toString(val, 16);}
			else if(val>=4096 && val<8192){ pstr = "=" + toString((val-4096),16);}
			else if(val>=8192            ){ pstr = "%" + toString((val-8192),16);}
			else{ count++;}

			if(count==0){ cm += pstr;}
			else if(pstr.length()>0 || count==20){ cm+=(toString((15+count),36)+pstr); count=0;}
		}
		if(count>0){ cm+=toString((15+count),36);}

		return cm;
	}

	protected int[] roomNumbers;
	//---------------------------------------------------------------------------
	// enc.encodeRoomNumber16()  部屋＋部屋の一つのquesが0～8192?までの場合、問題部をエンコードする
	//---------------------------------------------------------------------------
	protected String encodeRoomNumber16(){
		int count=0;
		String cm="";
		for(int i = 0; i < roomNumbers.length; i++){
			String pstr = "";
			int val = roomNumbers[i];

			if     (val>=     0 && val<    16){ pstr =       toString(val,16);}
			else if(val>=    16 && val<   256){ pstr = "-" + toString(val,16);}
			else if(val>=   256 && val<  4096){ pstr = "+" + toString(val,16);}
			else if(val>=  4096 && val<  8192){ pstr = "=" + toString((val-4096),16);}
			else if(val>=  8192 && val< 12240){ pstr = "%" + toString((val-8192),16);}
			else if(val>= 12240 && val< 77776){ pstr = "*" + toString((val-12240),16);}
			else if(val>= 77776              ){ pstr = "$" + toString((val-77776),16);} // 最大1126352
			else{ count++;}

			if(count==0){ cm += pstr;}
			else if(pstr.length()>0 || count==20){ cm+=(toString((15+count),36)+pstr); count=0;}
		}
		if(count>0){ cm+=toString((15+count),36);}

		return cm;
	}

	//---------------------------------------------------------------------------
	// enc.encodeArrowNumber16()  矢印付きquesが0～8192?までの場合、問題部をエンコードする
	//---------------------------------------------------------------------------
	protected String encodeArrowNumber16(){
		String cm = "";
		int count = 0;
		for(int c=0;c<rows*cols;c++){
			String pstr="";
			if(QnC(c)!=-1){
				if     (QnC(c)==-2){ pstr=((DiC(c)==0?0:DiC(c)  )+".");}
				else if(QnC(c)< 16){ pstr=((DiC(c)==0?0:DiC(c)  )+toString(QnC(c),16));}
				else if(QnC(c)<256){ pstr=((DiC(c)==0?5:DiC(c)+5)+toString(QnC(c),16));}
			}
			else{ pstr=" "; count++;}

			if(count==0)      { cm += pstr;}
			else if(pstr!=" "){ cm += (toString((count+9),36)+pstr); count=0;}
			else if(count==26){ cm += "z"; count=0;}
		}
		if(count>0){ cm += toString((count+9),36);}

		return cm;
	}

	//---------------------------------------------------------------------------
	// enc.encodeBorder() 問題の境界線をエンコードする
	//---------------------------------------------------------------------------
	protected String encodeBorder(){
		int num, pass;
		String cm = "";

		num = 0; pass = 0;
		for(int i=0;i<(cols-1)*rows;i++){
			if(QuB(i)==1){ pass+=1<<(4-num);}
			num++; if(num==5){ cm += toString(pass, 32); num=0; pass=0;}
		}
		if(num>0){ cm += toString(pass, 32);}

		num = 0; pass = 0;
		for(int i=(cols-1)*rows;i<(cols-1)*rows+cols*(rows-1);i++){
			if(QuB(i)==1){ pass+=1<<(4-num);}
			num++; if(num==5){ cm += toString(pass, 32); num=0; pass=0;}
		}
		if(num>0){ cm += toString(pass, 32);}

		return cm;
	}

	protected int[] borders; 

	/**
	 * 領域データから境界線ありなしデータを作成する。
	 */
	protected void makeBorderData() {
		int bdinside = (cols-1)*rows+cols*(rows-1);
		borders = new int[bdinside];
		for (int i = 0; i < bdinside; i++) {
			borders[i] = getBorder(i2b(i));
		}
	}

	/**
	 * 引数の境界座標に境界線があるか
	 * 各サブクラスで実装すること
	 * @param b
	 * @return 線がなければ 0 あれば 1
	 */
	protected int getBorder(SideAddress b) {
		return 0;
	}

	public Address i2a(int i) {
		return new Address(i/cols, i%cols);
	}

	public SideAddress i2b(int i) {
		if (i < 0) {
			return SideAddress.NOWHERE;
		} else if (i < (cols-1)*rows) {
			return new SideAddress(Direction.VERT, i/(cols-1), i%(cols-1));
		} else if (i < (cols-1)*rows+cols*(rows-1)) {
			i = i - (cols-1)*rows;
			return new SideAddress(Direction.HORIZ, i/(cols), i%(cols));
		}
		return SideAddress.NOWHERE;
	}

    public int a2i(Address a) {
    	return a.r() * cols + a.c();
    }

	public int QnC(int i) {
		return 0;
	}

	public int DiC(int i) {
		return 0;
	}

	public int QuB(int i) {
		return borders[i];
	}

	public static final String toString(int i, int radix) {
		return Integer.toString(i, radix);
	}

}
