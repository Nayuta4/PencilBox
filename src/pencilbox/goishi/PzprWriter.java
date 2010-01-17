package pencilbox.goishi;

import pencilbox.common.io.PzprWriterBase;


/**
 * 参考：pzprv3 goishi.js
 */
public class PzprWriter extends PzprWriterBase {
	
	private Board bd;
	
	private boolean fitsize = false; // 盤面サイズをデータに合わせるか
	private int bdpadding = 1; // 盤面サイズをデータに合わせるときの余白数

	protected String getPzprName() {
		return "goishi";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		encodeGoishi();
	}

	protected void encodeGoishi(){
		BoardSize d;
		if (fitsize == true)
			d = getSizeOfBoard_goishi();
		else
			d = new BoardSize(0, 0, cols-1, rows-1);
		String cm="";
		int count=0, pass=0;
		for(int cy=d.y1;cy<=d.y2;cy++){
			for(int cx=d.x1;cx<=d.x2;cx++){
				if(!bd.isOn(cy, cx) || bd.getState(cy, cx) == Board.BLANK){ pass+=(1<<(4-count));}
				count++; if(count==5){ cm += toString(pass,32); count=0; pass=0;}
			}
		}
		if(count>0){ cm += toString(pass,32);}
		outbstr(cm);
		outSize(d.y2-d.y1+1,d.x2-d.x1+1);
	}

	/**
	 * 盤面データにあわせて縮小した盤面サイズを取得する。
	 * @return
	 */
	protected BoardSize getSizeOfBoard_goishi(){
		int x1=9999, x2=-1, y1=9999, y2=-1, count=0;
		for(int c=0;c<rows*cols;c++){
			if(QuC(c)!=7){ continue;}
			if(x1>c%cols){ x1=c%cols;}
			if(x2<c%cols){ x2=c%cols;}
			if(y1>c/cols){ y1=c/cols;}
			if(y2<c/cols){ y2=c/cols;}
			count++;
		}
		if(count==0){return new BoardSize(0, 0, 1, 1);}
		int e = (bdpadding>0) ? bdpadding : 0; 
		return new BoardSize(x1-e, y1-e, x2+e, y2+e);
	}

	protected int QuC(int i) {
		if(i<0||cols*rows<=i){ return 0;}
		int n = bd.getState(i2a(i));
		if (n == Board.STONE)
			return 7;
		else
			return 0;
	}

}

class BoardSize {
	int x1;
	int y1;
	int x2;
	int y2;

	BoardSize(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
}