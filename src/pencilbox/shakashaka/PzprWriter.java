package pencilbox.shakashaka;

import pencilbox.common.io.PzprWriterBase;


/**
 * éQçlÅFpzprv3 shakashaka.js
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "shakashaka";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		this.encode4Cell();
	}

	protected int QnC(int i) {
		return bd.getNumber(i2a(i));
	}

}
