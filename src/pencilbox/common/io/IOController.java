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
	
	private static final int EXT_PCL = 3;
	private static final int EXT_XML = 1;

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
		try {
			switch (checkFileExt(file)) {
			case EXT_PCL:
				is = new FileInputStream(file);
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = builder.parse(is);
				PclReaderBase pclReader = createPclReader();
				problem = pclReader.makeProblem(doc);
				break;
			case EXT_XML:
				is = new FileInputStream(file);
				XmlReaderBase xmlReader = createXmlReader();
				problem = xmlReader.readProblem(is);
				break;
			default:
				in = new FileReader(file);
				TxtReaderBase txtReader = createTxtReader();
				board = txtReader.readProblem(in);
				problem = new Problem(board);
				break;
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
	 * �����t�@�C���ɕۑ�����
	 * @param problem �ۑ�������
	 * @param file �ۑ���̃t�@�C��
	 * @throws PencilBoxException
	 */
	public void saveFile(Problem problem, File file)
			throws PencilBoxException{
		problem.setFile(file);
		PrintWriter out = null;
		try {
			switch (checkFileExt(file)) {
			case EXT_PCL:
				PclWriterBase pclWriter = createPclWriter();
				Document doc = pclWriter.buildDocument(problem);
				Transformer t = TransformerFactory.newInstance()
						.newTransformer();
				t.setOutputProperty("indent", "yes");
				t.transform(new DOMSource(doc), new StreamResult(
						new FileOutputStream(file)));
				break;
			case EXT_XML:
				XmlWriterBase xmlWriter = createXmlWriter();
				out = xmlWriter.open(file);
				xmlWriter.writeProblem(problem);
				xmlWriter.close();
				break;
			default:
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

	private int checkFileExt(File file) {
		String filename = file.getName();
		int len = filename.length();
		if (len >= 4) {
			if (filename.substring(len - 4).equals(".pcl"))
				return EXT_PCL;
			if (filename.substring(len - 4).equals(".xml"))
				return EXT_XML;
		}
		return 0;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	private XmlReaderBase createXmlReader() throws PencilBoxClassException {
		XmlReaderBase xmlReader = (XmlReaderBase) ClassUtil.createInstance(
				pencilType, "XmlReader");
		xmlReader.setPuzzleType(this.pencilType.getPencilName()); // �ǂ̃p�Y�����ʒm���Ȃ���΂Ȃ�Ȃ�
		return xmlReader;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	private XmlWriterBase createXmlWriter() throws PencilBoxClassException {
		XmlWriterBase xmlWriter = (XmlWriterBase) ClassUtil.createInstance(
				pencilType, "XmlWriter");
		xmlWriter.setPuzzleType(this.pencilType.getPencilName()); // �ǂ̃p�Y�����ʒm���Ȃ���΂Ȃ�Ȃ�
		return xmlWriter;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	private PclReaderBase createPclReader() throws PencilBoxClassException {
		PclReaderBase pclReader = (PclReaderBase) ClassUtil.createInstance(
				pencilType, "PclReader");
		return pclReader;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	private PclWriterBase createPclWriter() throws PencilBoxClassException {
		PclWriterBase pclWriter = (PclWriterBase) ClassUtil.createInstance(
				pencilType, "PclWriter");
		return pclWriter;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	private TxtReaderBase createTxtReader() throws  PencilBoxClassException {
		TxtReaderBase txtReader = (TxtReaderBase) ClassUtil.createInstance(
				pencilType, "TxtReader");
		return txtReader;
	}

	/**
	 * @return
	 * @throws PencilBoxClassException
	 */
	private TxtWriterBase createTxtWriter() throws  PencilBoxClassException {
		TxtWriterBase txtWriter = (TxtWriterBase) ClassUtil.createInstance(
				pencilType, "TxtWriter");
		return txtWriter;
	}
	
}
