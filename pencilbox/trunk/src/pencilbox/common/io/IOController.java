package pencilbox.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.PencilBoxException;
import pencilbox.common.core.Problem;
import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;

/**
 * 問題データのファイル入出力処理を行うクラス。
 * GUIとは関係なく使用できるようにする。
 */
public class IOController {
	
	public enum DataFormat {
		TXT,
		XML,
		PCL,
		KANPEN,
		PZPRV3,
		;
	}

	private PencilType pencilType;

	/**
	 * IOControllerを取得する
	 * @param pencilType パズルの種類
	 * @return IOController IOControllerインスタンス
	 */
	public static IOController getInstance(PencilType pencilType) {
		return new IOController(pencilType);
	}
	private IOController(PencilType pencilType) {
		this.pencilType = pencilType;
	}

	/**
	 * 新規の問題をファイルから読み込む
	 * @param file 問題ファイル
	 * @return 読み込んだ問題
	 * @throws PencilBoxException
	 */
	public Problem openFile(File file) throws PencilBoxException {
		Problem problem = null;
		BoardBase board = null;
		Reader in = null;
		InputStream is = null;
		DataFormat format = checkFileExt(file); 
		try {
			if (format == DataFormat.PCL) {
				is = new FileInputStream(file);
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = builder.parse(is);
				PclReaderBase pclReader = createPclReader();
				problem = pclReader.makeProblem(doc);
			} else if (format == DataFormat.XML) {
				is = new FileInputStream(file);
				XmlReaderBase xmlReader = createXmlReader();
				problem = xmlReader.readProblem(is);
			} else {
				in = new FileReader(file);
				TxtReaderBase txtReader = createTxtReader();
				board = txtReader.readProblem(in);
				problem = new Problem(board);
			}
		} catch (ParserConfigurationException e) {
			throw new PencilBoxException(e);
		} catch (SAXException e) {
			throw new PencilBoxException(e);
		} catch (IOException e) {
			throw new PencilBoxException(e);
		} catch (RuntimeException e) {
			// txtReader はまともなエラー処理をしていないので，問題データがおかしいといろいろRunTimeExceptionが投げられる
			// それらのRunTimeExceptionをここで受けてPencilboxExceptionに変換する
			throw new PencilBoxException(e);
		} finally {
			try {
				if (in != null)
					in.close();
				if (is != null)
					is.close();
			} catch (IOException ec) {
				// close()による例外を受ける
			}
		}
		problem.setFile(file);
		return problem;
	}

	/**
	 * 問題文字列データから問題を作成する
	 * @param problemData 問題文字列データ
	 * @return 読み込んだ問題
	 * @throws PencilBoxException
	 */
	public Problem readProblemData(String problemData) throws PencilBoxException {
		Problem problem = null;
		BoardBase board = null;
		Reader in = null;
		try {
			in = new StringReader(problemData.replace('/','\n').replace('_',' '));
			TxtReaderBase txtReader = createTxtReader();
			board = txtReader.readProblem(in);
			problem = new Problem(board);
		} catch (IOException e) {
			throw new PencilBoxException(e);
		} catch (RuntimeException e) {
			// txtReader はまともなエラー処理をしていないので，問題データがおかしいといろいろRunTimeExceptionが投げられる
			// それらのRunTimeExceptionをここで受けてPencilboxExceptionに変換する
			throw new PencilBoxException(e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ec) {
				// close()による例外を受ける
			}
		}
		problem.setFile(null);
		return problem;
	}

	/**
	 * 問題をファイルに保存する
	 * @param problem 保存する問題
	 * @param file 保存先のファイル
	 * @throws PencilBoxException
	 */
	public void saveFile(Problem problem, File file)
			throws PencilBoxException{
		problem.setFile(file);
		PrintWriter out = null;
		DataFormat format = checkFileExt(file);
		try {
			if (format == DataFormat.PCL) {
				PclWriterBase pclWriter = createPclWriter();
				Document doc = pclWriter.buildDocument(problem);
				Transformer t = TransformerFactory.newInstance()
						.newTransformer();
				t.setOutputProperty("indent", "yes");
				t.transform(new DOMSource(doc), new StreamResult(
						new FileOutputStream(file)));
			} else if (format == DataFormat.XML) {
				XmlWriterBase xmlWriter = createXmlWriter();
				out = xmlWriter.open(file);
				xmlWriter.writeProblem(problem);
				xmlWriter.close();
			} else {
				TxtWriterBase txtWriter = createTxtWriter();
//				txtWriter.setBoard(problem.getBoard());
				out = new PrintWriter(new FileWriter(file));
				txtWriter.writeProblem(out, problem.getBoard());
				out.close();
			}
		} catch (IOException e) {
			throw new PencilBoxException(e);
		} catch (TransformerException e) {
			throw new PencilBoxException(e);
		} finally {
			if (out != null)
				out.close();
		}
	}
	
	/**
	 * 問題データ文字列を作成する
	 * @param board 盤面
	 * @return 問題データ文字列
	 * @throws PencilBoxClassException
	 */
	public String getProblemDataString(BoardBase board) throws PencilBoxClassException {
		TxtWriterBase txtWriter = createTxtWriter();
		StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);
		txtWriter.writeQuestion(out, board);
		String separator = System.getProperty("line.separator");
		String problemDataS = sw.toString().replace(separator, "/").replace(' ', '_');
		out.close();
		return problemDataS;
	}

	private DataFormat checkFileExt(File file) {
		String filename = file.getName();
		int len = filename.length();
		if (len >= 4) {
			if (filename.substring(len - 4).equals(".pcl"))
				return DataFormat.PCL;
			if (filename.substring(len - 4).equals(".xml"))
				return DataFormat.XML;
		}
		return DataFormat.TXT;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	public XmlReaderBase createXmlReader() throws PencilBoxClassException {
		XmlReaderBase xmlReader = (XmlReaderBase) ClassUtil.createInstance(
				pencilType, ClassUtil.XMLREADER_CLASS);
		xmlReader.setPuzzleType(this.pencilType.getPencilName()); // どのパズルか通知しなければならない
		return xmlReader;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	public XmlWriterBase createXmlWriter() throws PencilBoxClassException {
		XmlWriterBase xmlWriter = (XmlWriterBase) ClassUtil.createInstance(
				pencilType, ClassUtil.XMLWRITER_CLASS);
		xmlWriter.setPuzzleType(this.pencilType.getPencilName()); // どのパズルか通知しなければならない
		return xmlWriter;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	public PclReaderBase createPclReader() throws PencilBoxClassException {
		PclReaderBase pclReader = (PclReaderBase) ClassUtil.createInstance(
				pencilType, ClassUtil.PCLREADER_CLASS);
		return pclReader;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	public PclWriterBase createPclWriter() throws PencilBoxClassException {
		PclWriterBase pclWriter = (PclWriterBase) ClassUtil.createInstance(
				pencilType, ClassUtil.PCLWRITER_CLASS);
		return pclWriter;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	public TxtReaderBase createTxtReader() throws PencilBoxClassException {
		TxtReaderBase txtReader = (TxtReaderBase) ClassUtil.createInstance(
				pencilType, ClassUtil.TXTREADER_CLASS);
		return txtReader;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	public TxtWriterBase createTxtWriter() throws PencilBoxClassException {
		TxtWriterBase txtWriter = (TxtWriterBase) ClassUtil.createInstance(
				pencilType, ClassUtil.TXTWRITER_CLASS);
		return txtWriter;
	}

}
