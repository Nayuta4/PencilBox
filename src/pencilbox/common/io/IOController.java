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
 * ���f�[�^�̃t�@�C�����o�͏������s���N���X�B
 * GUI�Ƃ͊֌W�Ȃ��g�p�ł���悤�ɂ���B
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
	 * IOController���擾����
	 * @param pencilType �p�Y���̎��
	 * @return IOController IOController�C���X�^���X
	 */
	public static IOController getInstance(PencilType pencilType) {
		return new IOController(pencilType);
	}
	private IOController(PencilType pencilType) {
		this.pencilType = pencilType;
	}

	/**
	 * �V�K�̖����t�@�C������ǂݍ���
	 * @param file ���t�@�C��
	 * @return �ǂݍ��񂾖��
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
			// txtReader �͂܂Ƃ��ȃG���[���������Ă��Ȃ��̂ŁC���f�[�^�����������Ƃ��낢��RunTimeException����������
			// ������RunTimeException�������Ŏ󂯂�PencilboxException�ɕϊ�����
			throw new PencilBoxException(e);
		} finally {
			try {
				if (in != null)
					in.close();
				if (is != null)
					is.close();
			} catch (IOException ec) {
				// close()�ɂ���O���󂯂�
			}
		}
		problem.setFile(file);
		return problem;
	}

	/**
	 * ��蕶����f�[�^��������쐬����
	 * @param problemData ��蕶����f�[�^
	 * @return �ǂݍ��񂾖��
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
			// txtReader �͂܂Ƃ��ȃG���[���������Ă��Ȃ��̂ŁC���f�[�^�����������Ƃ��낢��RunTimeException����������
			// ������RunTimeException�������Ŏ󂯂�PencilboxException�ɕϊ�����
			throw new PencilBoxException(e);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ec) {
				// close()�ɂ���O���󂯂�
			}
		}
		problem.setFile(null);
		return problem;
	}

	/**
	 * �����t�@�C���ɕۑ�����
	 * @param problem �ۑ�������
	 * @param file �ۑ���̃t�@�C��
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
	 * ���f�[�^��������쐬����
	 * @param board �Ֆ�
	 * @return ���f�[�^������
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
		xmlReader.setPuzzleType(this.pencilType.getPencilName()); // �ǂ̃p�Y�����ʒm���Ȃ���΂Ȃ�Ȃ�
		return xmlReader;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	public XmlWriterBase createXmlWriter() throws PencilBoxClassException {
		XmlWriterBase xmlWriter = (XmlWriterBase) ClassUtil.createInstance(
				pencilType, ClassUtil.XMLWRITER_CLASS);
		xmlWriter.setPuzzleType(this.pencilType.getPencilName()); // �ǂ̃p�Y�����ʒm���Ȃ���΂Ȃ�Ȃ�
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
