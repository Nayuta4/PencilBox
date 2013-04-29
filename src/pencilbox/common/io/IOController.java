package pencilbox.common.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.PencilBoxException;
import pencilbox.common.core.Problem;
import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;
import pencilbox.heyawake.HeyawakeReader;
import pencilbox.heyawake.HeyawakeWriter;

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
		HEYAWAKE,
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
	 * 引数の文字列のURLまたはファイル名から問題を読み込む
	 * @param string URLまたはファイル名
	 * @return 読み込んだ問題
	 * @throws PencilBoxException
	 */
	public Problem openFile(String string) throws PencilBoxException {
		File file = new File(string);
		if (file.isFile()) {
			return openFile(file);
		}
		InputStream in = null;
		try {
			in = new URL(string).openStream();
			if (in != null) {
				return new Problem(createTxtReader().readProblem(new InputStreamReader(in)));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

		return null;
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
		DataFormat format = checkFileExt(file);
		try {
			if (format == DataFormat.TXT) {
				in = new FileReader(file);
				TxtReaderBase txtReader = createTxtReader();
				board = txtReader.readProblem(in);
				problem = new Problem(board);
			} else if (format == DataFormat.XML) {
				XmlReaderBase xmlReader = createXmlReader();
				problem = xmlReader.readProblem(file);
			} else if (format == DataFormat.PCL) {
				PclReaderBase pclReader = createPclReader();
				problem = pclReader.readProblem(file);
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
			} catch (IOException ec) {
				// close()による例外を受ける
			}
		}
		problem.setFile(file);
		return problem;
	}

	/**
	 * 各種フォーマットの文字列データから問題を作成する
	 * @param problemData 問題文字列データ
	 * @return 読み込んだ問題
	 * @throws PencilBoxException
	 */
	public Problem importProblemData(String string, DataFormat format) throws PencilBoxException {
		Problem problem = null;
		BoardBase board = null;
		Reader in = null;
		try {
			if (format == DataFormat.KANPEN) {
				int index = string.indexOf("?problem=");
				if (index > 0) {
					string = string.substring(index+9);
				} else if (index == -1) {
					throw new PencilBoxException("input data does not contain \"?problem=\".");
				}
				in = new StringReader(string.replace('/','\n').replace('_',' '));
				board = createTxtReader().readProblem(in);
			} else if (format == DataFormat.PZPRV3) {
				int index = string.indexOf('?');
				if (index > 0) {
					string = string.substring(index);
				} else if (index == -1) {
					throw new PencilBoxException("input data does not contain \"?\".");
				}
				board = createPzprReader().readProblem(string.trim());
			} else if (format == DataFormat.HEYAWAKE) {
				int index = string.indexOf("?problem=");
				if (index > 0) {
					string = string.substring(index+9);
				} else if (index == -1) {
					throw new PencilBoxException("input data does not contain \"?problem=\".");
				}
				board = new HeyawakeReader().readProblem(string.trim());
			}
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
		problem = new Problem(board);
		problem.setFile(null);
		return problem;
	}

	/**
	 * 問題をファイルに保存する
	 * @param problem 保存する問題
	 * @param file 保存先のファイル
	 * @throws PencilBoxException
	 */
	public void saveFile(Problem problem, File file) throws PencilBoxException{
		PrintWriter out = null;
		DataFormat format = checkFileExt(file);
		try {
			if (format == DataFormat.TXT) {
				TxtWriterBase txtWriter = createTxtWriter();
				out = new PrintWriter(new FileWriter(file));
				txtWriter.writeProblem(out, problem.getBoard());
			} else if (format == DataFormat.XML) {
				XmlWriterBase xmlWriter = createXmlWriter();
				xmlWriter.writeProblem(file, problem);
			} else if (format == DataFormat.PCL) {
				PclWriterBase pclWriter = createPclWriter();
				pclWriter.writeProblem(file, problem);
			}
		} catch (IOException e) {
			throw new PencilBoxException(e);
		} catch (TransformerException e) {
			throw new PencilBoxException(e);
		} finally {
			if (out != null) {
//				try {
					out.close();
//				} catch (IOException e) {
//				}
			}
		}
		problem.setFile(file); // セーブできたら，問題にそのファイル名を関連付ける。
	}

	/**
	 * 各種フォーマットの問題データを出力する。
	 * @param board 盤面
	 * @return 問題データ文字列
	 * @throws PencilBoxClassException
	 */
	public String exportProblemData(BoardBase board, DataFormat format) throws PencilBoxClassException {
		String string = "";
		try {
			if (format == DataFormat.KANPEN) {
				TxtWriterBase txtWriter = createTxtWriter();
				StringWriter sw = new StringWriter();
				PrintWriter out = new PrintWriter(sw);
				txtWriter.writeQuestion(out, board);
				out.close();
				String dataS = sw.toString().replace(System.getProperty("line.separator"), "/").replace(' ', '_');
				string = kanpenUrl() + pencilType.getPencilName() + ".html?problem=" + dataS;
			} else if (format == DataFormat.PZPRV3) {
				string = pzprv3Url() + createPzprWriter().writeQuestion(board);
			} else if (format == DataFormat.HEYAWAKE) {
				string = new HeyawakeWriter().writeQuestion(board);
			}
		} finally {
		}
		return string;
	}

	public String pzprv3Url() {
		return "http://pzv.jp/p.html?";
	}

	public String kanpenUrl() {
		return "http://www.kanpen.net/";
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
		pclWriter.setPuzzleType(this.pencilType);
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

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	public PzprReaderBase createPzprReader() throws PencilBoxClassException {
		PzprReaderBase pzprReader = (PzprReaderBase) ClassUtil.createInstance(
				pencilType, "PzprReader");
		return pzprReader;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	public PzprWriterBase createPzprWriter() throws  PencilBoxClassException {
		PzprWriterBase pzprWriter = (PzprWriterBase) ClassUtil.createInstance(
				pencilType, "PzprWriter");
		return pzprWriter;
	}
}
