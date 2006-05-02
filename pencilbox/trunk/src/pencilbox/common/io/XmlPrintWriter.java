package pencilbox.common.io;
import java.io.*;

/**
 * 
 */
public class XmlPrintWriter extends PrintWriter {
	int nlCount = 10;
	public XmlPrintWriter(Writer arg0) {
		super(arg0);
	}
	public XmlPrintWriter(Writer arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public XmlPrintWriter(OutputStream arg0) {
		super(arg0);
	}

	public XmlPrintWriter(OutputStream arg0, boolean arg1) {
		super(arg0, arg1);
	}
	static final String blankLine = "                    ";
	static String indentText(int lvl, String txt) {
		if(lvl < 0) lvl = 0;	// 20031207
		return blankLine.substring(0, lvl*2) + txt;
	}
	public void print(int lvl, String txt) {
		print(indentText(lvl, txt));
	}
	public void printIndent(int lvl) {
		print(indentText(lvl, ""));
	}
	public void println(int lvl, String txt){
		println(indentText(lvl, txt));
	}	
	public void printlnAt(int c){
		if(c % nlCount == 0 && c != 0) println();
	}
}
