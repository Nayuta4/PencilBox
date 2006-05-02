package pencilbox.common.io;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Problem;
import pencilbox.common.core.Property;
import pencilbox.common.core.Size;


/**
 * 
 */
public abstract class XmlReaderBase extends DefaultHandler implements
		XmlConstants {
	private String puzzleType;
	protected String node;
	private String rowNode;
	private String boardNode;
	protected Attributes attributes;
	private BoardBase board;
	private Property property;
	private Problem problem;
	private Size size;
	private int rowNo, colNo;
	private DataFormatException exception;
	// abstractにすると定義しないといけなくなる
	protected BoardBase makeBoard() {
//		return (PuzzleBoard) ClassUtil.createInstance(type, "Board");
		return null;
	}
	
	public void setPuzzleType(String s) {
		this.puzzleType = s;
	}

	/**
	 * @param in
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws DataFormatException
	 */
	protected Problem readProblem(InputStream in)
		throws ParserConfigurationException, SAXException, IOException, DataFormatException {
		exception = null;
		SAXParser sax = SAXParserFactory.newInstance().newSAXParser();
		problem = new Problem();
		property = problem.getProperty();
		sax.parse(in, this);
		if (exception != null)
			throw exception;
		return problem;
	}
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		try {
			node = qName.intern();
			this.attributes = attributes;
			if (node == "puzzle")
				onPuzzle();
			else if (node == "size")
				onSize();
			else if (node == "board")
				onBoard();
			else if (node == "brow")
				onBrow();
			else if (node == "answer")
				onAnswer();
			else if (node == "arow")
				onArow();
			else if (node == N0)
				onN(0);
			else if (node == N1)
				onN(1);
			else if (node == N2)
				onN(2);
			else if (node == N3)
				onN(3);
			else if (node == N4)
				onN(4);
			else if (node == N5)
				onN(5);
			else if (node == N6)
				onN(6);
			else if (node == N7)
				onN(7);
			else if (node == N8)
				onN(8);
			else if (node == N9)
				onN(9);
			else if (node == N)
				onN();
			else if (node == CHAR)
				onChar();
			else if (node == HORIZ)
				onType();
			else if (node == HORIZ_VERT)
				onType();
			else if (node == SPACE)
				onType();
			else if (node == UNKNOWN)
				onType();
			else if (node == VERT)
				onType();
			else if (node == WALL)
				onType();
			else if (node == "number")
				onNumber();
			else if (node == PROPERTY);
			else if (node == AUTHOR)
				property.setAuthor(attributes.getValue(VALUE));
			else if (node == SOURCE)
				property.setSource(attributes.getValue(VALUE));
			else if (node == DIFFICULTY)
				property.setDifficulty(attributes.getValue(VALUE));
			else
				start();
		} catch (DataFormatException e) {
			if (this.exception == null)
				this.exception = e;
		}
	}
	protected void start() {
	}

	public void endElement(String uri, String localName, String qName) {
		endElementX(uri, localName, qName);
	}
	private void endElementX(String uri, String localName, String qName) {
		node = qName.intern();
		if (node == "brow")
			onBrowEnd();
		else if (node == "arow")
			onArowEnd();
		else if (node == "board")
			onBoardEnd();
		else if (node == "answer")
			onAnswerEnd();
	}

	protected void onPuzzle() throws DataFormatException {
		if (!puzzleType.equals(attributes.getValue("type")))
			throw new DataFormatException("wrong puzzle type");
	}
	protected void onSize() throws DataFormatException {
		String rowAttr = attributes.getValue("row");
		String colAttr = attributes.getValue("col");
		if (rowAttr == null || colAttr == null)
			throw new DataFormatException("size error");
		int row = Integer.parseInt(rowAttr);
		int col = Integer.parseInt(colAttr);
		size = new Size(row, col);
		board = makeBoard();
		board.setSize(size);
		problem.setSize(size);
		problem.setBoard(board);
	}
	protected void onBoard() {
		boardNode = node;
		rowNo = 0;
	}
	protected void onBoardEnd() {
	}
	protected void onAnswerEnd() {
	}
	protected void onAnswer() {
		// 1次元のときはcolNoしか使わないのでcolNoもリセットしておく
		rowNo = 0;
		colNo = 0;
	}
	protected void onBrow() {
		rowNode = node;
	}
	protected void onBrowEnd() {
		rowNo++;
		colNo = 0;
	}
	protected void onArow() {
		rowNode = node;
	}
	protected void onArowEnd() {
		rowNo++;
		colNo = 0;
	}
	protected void onN() {
		int v = 0;
		String vAttr = attributes.getValue("v");
		if (vAttr != null)
			v = Integer.parseInt(vAttr);
		onN(v);
	}
	protected void onN(int v) {
		int n = 1;
		String nAttr = attributes.getValue("n");
		if (nAttr != null)
			n = Integer.parseInt(nAttr);
		for (int i = 0; i < n; i++) {
			if (rowNode == "brow")
				setBN(rowNo, colNo, v);
			else
				setAN(rowNo, colNo, v);
			colNo++;
		}
	}
	protected void onNumber() {
		int r = Integer.parseInt(attributes.getValue("r")) - 1;
		int c = Integer.parseInt(attributes.getValue("c")) - 1;
		int n = Integer.parseInt(attributes.getValue("n"));
		if (boardNode == "board")
			setBN(r, c, n);
		else if (boardNode == "answer")
			setAN(r, c, n);
	}
	protected void onChar() {
		char c = '?';
		String vAttr = attributes.getValue("v");
		if (vAttr != null) {
			if (vAttr.length() != 0)
				c = vAttr.charAt(0);
		}
		int n = 1;
		String nAttr = attributes.getValue("n");
		if (nAttr != null)
			n = Integer.parseInt(nAttr);
		for (int i = 0; i < n; i++) {
			if (rowNode == "brow")
				setBC(rowNo, colNo, c);
			else
				setAC(rowNo, colNo, c);
			colNo++;
		}
	}
	protected void setBN(int r, int c, int v) {
	}
	protected void setAN(int r, int c, int v) {
	}
	protected void setBC(int r, int c, char v) {
	}
	protected void setAC(int r, int c, char v) {
	}
	protected void setBSpace(int r, int c) {
	}
	protected void setASpace(int r, int c) {
	}
	protected void onType() {
		int n = 1;
		String nAttr = attributes.getValue("n");
		if (nAttr != null)
			n = Integer.parseInt(nAttr);
		for (int i = 0; i < n; i++) {
			if (rowNode == "brow")
				setBType(rowNo, colNo, node);
			else
				setAType(rowNo, colNo, node);
			colNo++;
		}
	}
	protected void setBType(int r, int c, String type) {
	}
	protected void setAType(int r, int c, String type) {
	}
	/*
	 * 数値属性値を得る、無いかエラーならnoneになる
	 */
//	protected int getAttribute(Attributes attributes, String name, int none)
//		throws PuzzleException {
//		String nAttr = attributes.getValue(name);
//		if (nAttr == null)
//			return none;
//		try {
//			int n = Integer.parseInt(nAttr);
//			return n;
//		} catch (NumberFormatException e) {
//			throw new PuzzleException("NumberFormatException:" + nAttr);
//		}
//	}
}
