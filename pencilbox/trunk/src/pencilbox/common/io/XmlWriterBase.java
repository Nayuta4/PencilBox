package pencilbox.common.io;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Problem;
import pencilbox.common.core.Property;
import pencilbox.common.core.Size;


/**
 * 
 */
public abstract class XmlWriterBase implements XmlConstants {

	private String puzzleType;
	private BoardBase board;
	private Property property;
	private int col;
	private int len;
	private String elm;
	protected XmlPrintWriter writer;
	private int indent;

	protected void writeProblem(Problem problem) {
		board = problem.getBoard();
		property = problem.getProperty();
		outHeader();
		outSize();
		outProperty();
		startBoard();
		setBoardData(board);
		endBoard();
		startAnswer();
		setAnswerData(board);
		endAnswer();
		outTail();
	}
	protected void setBoardData(BoardBase board) {
		throw new UnsupportedOperationException("setBoardData ñ¢çÏê¨");
	}
	protected void setAnswerData(BoardBase board) {
		throw new UnsupportedOperationException("setAnswerData ñ¢çÏê¨");
	}
	protected XmlPrintWriter open(File file) throws IOException {
		FileOutputStream os = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		writer = new XmlPrintWriter(osw);
		return writer;
	}
	protected void close() {
		if (writer == null)
			return;
		writer.close();
		writer = null;
	}
	protected void startTag(int level, String tag) {
		writer.print(level, "<" + tag);
	}
	protected void startTag(String tag) {
		startTag(indent++, tag);
	}
	protected void closeTag() {
		writer.println(">");
	}
	protected void emptyTag() {
		writer.println("/>");
		indent--;
		if (indent < 0)
			indent = 0;
	}
	protected void endTag(int level, String tag) {
		writer.println(level, "</" + tag + ">");
	}
	protected void endTag(String tag) {
		writer.println();
		indent--;
		if (indent < 0)
			indent = 0;
		endTag(indent, tag);
	}
	protected void attribute(String attr, String value) {
		writer.print(" " + attr + "=\"" + value + "\"");
	}
	protected void attribute(String attr, int value) {
		writer.print(" " + attr + "=\"" + value + "\"");
	}
	protected void indent() {
		writer.printIndent(indent);
	}
	protected void outHeader() {
		outHeader(puzzleType);
	}
	protected void outHeader(String type) {
		writer.println("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
		writer.println("<puzzle type=\"" + type + "\">");
	}
	protected void outTail() {
		writer.println("</puzzle>");
		writer.close();
	}
	protected void outSize() {
		Size size = board.getSize();
		outSize(size);
	}
	protected void outSize(int rows, int cols) {
		writer.println(1, "<size row=\"" + rows + "\" col=\"" + cols + "\"/>");
	}
	protected void outSize(Size size) {
		StringBuffer text = new StringBuffer("<size");
		if (size.getRows() != 0)
			text.append(" row=\"" + size.getRows() + "\"");
		if (size.getCols() != 0)
			text.append(" col=\"" + size.getCols() + "\"");
		text.append(" />");
		writer.println(1, text.toString());
	}
	protected void outProperty() {
		startTag(PROPERTY);
		closeTag();
		startTag(AUTHOR);
		attribute(VALUE, property.getAuthor());
		emptyTag();
		startTag(SOURCE);
		attribute(VALUE, property.getSource());
		emptyTag();
		startTag(DIFFICULTY);
		attribute(VALUE, property.getDifficulty());
		emptyTag();
		endTag(PROPERTY);
	}
	protected void startBoard() {
		writer.println(1, "<board>");
	}
	protected void endBoard() {
		writer.println(1, "</board>");
	}
	void rowBegin() {
		col = 0;
		len = 0;
		elm = "";
	}
	protected void startBrow(int r) {
		writer.println(2, "<brow row=\"" + (r + 1) + "\">");
		writer.printIndent(3);
		rowBegin();
	}
	protected void endBrow() {
		//		if (!elm.equals(SPACE)) {
		out(ROW_END_MARKER);
		//		}
		writer.println();
		writer.println(2, "</brow>");
	}
	protected void startAnswer() {
		writer.println(1, "<answer>");
	}
	protected void endAnswer() {
		writer.println(1, "</answer>");
	}
	protected void startArow(int r) {
		writer.println(2, "<arow row=\"" + (r + 1) + "\">");
		writer.printIndent(3);
		rowBegin();
	}
	protected void endArow() {
		//		if (!elm.equals(SPACE)) {
		out(ROW_END_MARKER);
		//		}
		writer.println();
		writer.println(2, "</arow>");
	}
	void flushElm() {
		if (len == 0)
			return;
		switch (len) {
			case 0 :
				break;
			case 2 :
				writer.print("<" + elm + "/>");
				writer.print("<" + elm + "/>");
				break;
			case 1 :
				writer.print("<" + elm + "/>");
				break;
			default :
				writer.print("<" + elm + " n=\"" + len + "\"/>");

		}
		col += len;
		len = 0;
	}
	protected void out(String newElm) {
		if (!elm.equals(newElm)) {
			flushElm();
			elm = newElm;
		}
		len++;
		col++;
	}
	private char[] char1Store = new char[1];
	protected void out(char c) {
		char1Store[0] = c;
		String s = new String(char1Store);
		writer.print("<" + CHAR + " v=\"" + s + "\"/>");
	}
	protected void outN(int v) {
		if (v >= 0 && v <= 9) {
			switch (v) {
				case 0 :
					out(N0);
					break;
				case 1 :
					out(N1);
					break;
				case 2 :
					out(N2);
					break;
				case 3 :
					out(N3);
					break;
				case 4 :
					out(N4);
					break;
				case 5 :
					out(N5);
					break;
				case 6 :
					out(N6);
					break;
				case 7 :
					out(N7);
					break;
				case 8 :
					out(N8);
					break;
				case 9 :
					out(N9);
					break;
			}
		} else {
			flushElm();
			writer.print("<n v=\"" + v + "\"/>");
			len = 0;
			col++;
			elm = N;
		}
	}
	protected void outNumber(int r, int c, int n) {
		startTag(2,"number");
		attribute("r", r+1);
		attribute("c", c+1);
		attribute("n", n);
		emptyTag();
//		writer.println(2,"<number r=\"" + (r+1) + "\" c=\"" + (c+1) + "\" n=\"" + n+ "\"/>");
	}
	/**
	 * @param puzzleType The puzzleType to set.
	 */
	public void setPuzzleType(String puzzleType) {
		this.puzzleType = puzzleType;
	}
}
