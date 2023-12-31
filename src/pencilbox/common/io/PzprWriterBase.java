package pencilbox.common.io;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;

/**
 * �ς��Ղ�v3�`����URL�����o��
 * �Q�l�F Encode.js v3.2.3, v3.2.4
 * // p.html?(pid)/(qdata)
 * //               qdata -> [(pflag)/](cols)/(rows)/(bstr)
 */
public abstract class PzprWriterBase {

	protected BoardBase boardBase;
	protected int rows;
	protected int cols;

	private String outpflag = "";
	private String outbstr = "";
	/**
	 * �Ֆʃf�[�^�̕�����𐶐�����B
	 * �񓚏�Ԃ͖������āC���f�[�^�̂ݏ����o��
	 * @param board �����o���Ֆ�
	 * @return
	 */
	public String writeQuestion(BoardBase board) {
		this.boardBase = board;
		this.pzlexport();
		String pflag = (outpflag.length()>0) ? "/"+outpflag : "";
		return getPzprName() + pflag + '/' + cols + '/' + rows + '/' + outbstr;
	}

	/**
	 * �ς��Ղ�ł̖��O�B�e�T�u�N���X�ŋL�q����
	 * @return
	 */
	protected String getPzprName() {
		return "";
	}

	/**
	 * �e�p�Y����URL�o�͗p(�I�[�o�[���C�h�p)
	 * @return
	 */
	protected void pzlexport() {
	}

	/**
	 * �T�C�Y���o�͂���B
	 * @param r
	 * @param c
	 */
	protected void outSize(int r, int c) {
		this.rows = r;
		this.cols = c;
	}

	/**
	 * ������f�[�^�ɒǉ�����B
	 * @param s ������
	 */
	protected void outbstr(String s) {
		this.outbstr += s;
	}

	/**
	 * �t���O���o�͂���B
	 * @param s
	 */
	protected void outpflag(String s) {
		this.outpflag = s;
	}

	//---------------------------------------------------------------------------
	// enc.encode4Cell()  ques��0�`4�܂ł̏ꍇ�A��蕔���G���R�[�h����
	//---------------------------------------------------------------------------
	protected void encode4Cell(){
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

		outbstr(cm);
	}

	//---------------------------------------------------------------------------
	// enc.encodeNumber16()  ques��0�`8192?�܂ł̏ꍇ�A��蕔���G���R�[�h����
	//---------------------------------------------------------------------------
	protected void encodeNumber16(){
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

		outbstr(cm);
	}

	protected int[] roomNumbers;
	//---------------------------------------------------------------------------
	// enc.encodeRoomNumber16()  �����{�����̈��ques��0�`8192?�܂ł̏ꍇ�A��蕔���G���R�[�h����
	//---------------------------------------------------------------------------
	protected void encodeRoomNumber16(){
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
			else if(val>= 77776              ){ pstr = "$" + toString((val-77776),16);} // �ő�1126352
			else{ count++;}

			if(count==0){ cm += pstr;}
			else if(pstr.length()>0 || count==20){ cm+=(toString((15+count),36)+pstr); count=0;}
		}
		if(count>0){ cm+=toString((15+count),36);}

		outbstr(cm);
	}

	//---------------------------------------------------------------------------
	// enc.encodeArrowNumber16()  ���t��ques��0�`8192?�܂ł̏ꍇ�A��蕔���G���R�[�h����
	//---------------------------------------------------------------------------
	protected void encodeArrowNumber16(){
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

		outbstr(cm);
	}

	//---------------------------------------------------------------------------
	// enc.encodeBorder() ���̋��E�����G���R�[�h����
	//---------------------------------------------------------------------------
	protected void encodeBorder(){
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

		outbstr(cm);
	}

	protected int[] borders;

	/**
	 * �̈�f�[�^���狫�E������Ȃ��f�[�^���쐬����B
	 */
	protected void makeBorderData() {
		int bdinside = (cols-1)*rows+cols*(rows-1);
		borders = new int[bdinside];
		for (int i = 0; i < bdinside; i++) {
			borders[i] = getBorder(i2b(i));
		}
	}

	/**
	 * �����̋��E���W�ɋ��E�������邩
	 * �e�T�u�N���X�Ŏ������邱��
	 * @param b
	 * @return �����Ȃ���� 0 ����� 1
	 */
	protected int getBorder(SideAddress b) {
		return 0;
	}

	//---------------------------------------------------------------------------
	// enc.encodeCircle41_42() ���ہE���ۂ��G���R�[�h����
	//---------------------------------------------------------------------------
	protected void encodeCircle41_42(){
		String cm="";
		int num=0;
		int pass=0;
		for(int i=0;i<rows*cols;i++){
			if     (QuC(i)==(41)){ pass+=(  Math.pow(3,2-num));}
			else if(QuC(i)==(42)){ pass+=(2*Math.pow(3,2-num));}
			num++; if(num==3){ cm += toString(pass, 27); num=0; pass=0;}
		}
		if(num>0){ cm += toString(pass, 27);}

		outbstr(cm);
	}

	public Address i2a(int i) {
		return Address.address(i/cols, i%cols);
	}

	public SideAddress i2b(int i) {
		if (i < 0) {
			return SideAddress.NOWHERE;
		} else if (i < (cols-1)*rows) {
			return SideAddress.sideAddress(Direction.VERT, i/(cols-1), i%(cols-1));
		} else if (i < (cols-1)*rows+cols*(rows-1)) {
			i = i - (cols-1)*rows;
			return SideAddress.sideAddress(Direction.HORIZ, i/(cols), i%(cols));
		}
		return SideAddress.NOWHERE;
	}

    public int a2i(Address a) {
    	return a.r() * cols + a.c();
    }

	protected int QuC(int i) {
		return 0;
	}

	protected int QnC(int i) {
		return 0;
	}

	protected int DiC(int i) {
		return 0;
	}

	protected int QuB(int i) {
		return borders[i];
	}

	public static final String toString(int i, int radix) {
		return Integer.toString(i, radix);
	}

}
