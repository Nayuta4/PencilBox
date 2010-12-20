package pencilbox.heyawake;

import pencilbox.common.core.Address;
import pencilbox.common.core.AreaBase;
import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * 参考：pzprv3 heyawake.js
 */
public class PzprReader extends PzprReaderBase {
	
	private Board bd;

	private AreaBase[] areaArray;
	private Square[] squareArray;
	int nSquare;
	
	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		this.decodeBorder();
		makeAreaIDsFromBorders();
		makeAreas();
		makeSquares();
		roomNumbers = new int[squareArray.length];
		for (int l = 0; l < squareArray.length; l++)
			roomNumbers[l] = -1;
		this.decodeRoomNumber16();
		for (int l = 0; l < squareArray.length; l++) {
			if (roomNumbers[l] >= 0)
				squareArray[l].setNumber(roomNumbers[l]);
		}
	}
	
	protected void sQnC(int id, int num) {
		bd.getSquare(i2a(id)).setNumber(num);
	}

	private void makeAreas() {
		areaArray = new AreaBase[nArea];
		for (int k=0; k<nArea; k++) {
			areaArray[k] = new AreaBase();
		}
		for (int i = 0; i < bd.rows()*bd.cols(); i++) {
			int k = areaIds[i]-1;
			areaArray[k].add(i2a(i));
		}
	}

	/**
	 * 領域データから四角形領域データ作成する。
	 */
	private void makeSquares() {
		squareArray = new Square[nArea];
		nSquare = 0;
		for(int k = 0; k < nArea; k++) {
			Square sq = makeCircumscribedSquare(areaArray[k]);
			if (sq.getSquareSize() == areaArray[k].size()) {
				squareArray[nSquare] = sq;
				nSquare++;
				bd.setSquare(sq, sq);
				bd.getSquareList().add(sq);
			} else {
				System.out.println("領域が四角形でない");
			}
		}
	}

	/**
	 * 領域に外接する四角形領域を作成する。
	 */
	public Square makeCircumscribedSquare(AreaBase area) {
		int r1 = bd.cols();
		int r2 = -1;
		int c1 = bd.rows();
		int c2 = -1;
		for(Address a : area){
			if(r1>a.r()){ r1=a.r();}
			if(r2<a.r()){ r2=a.r();}
			if(c1>a.c()){ c1=a.c();}
			if(c2<a.c()){ c2=a.c();}
		}
		return new Square(r1, c1, r2, c2);
	}

}
