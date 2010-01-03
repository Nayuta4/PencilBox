package pencilbox.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Problem;
import pencilbox.common.core.Property;
import pencilbox.common.core.Size;
import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;


/**
 * pcl�`���t�@�C���ǂݍ��݂̊�b�N���X
 */
public abstract class PclReaderBase {

	private PencilType pencilType;
	private BoardBase board;
	private Property property;
	private Size size;
	private Problem problem;
	
	/**
	 * �t�@�C������PCL�`���̖��f�[�^��ǂ�Ŗ����쐬����B
	 * @param file ���f�[�^�t�@�C��
	 * @return �쐬�������
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws PencilBoxClassException
	 */
	public Problem readProblem(File file) throws ParserConfigurationException, SAXException, IOException, PencilBoxClassException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);
		makeProblem(doc);
		return problem;
	}

	/**
	 * DOM�h�L�������g����ɕϊ�����
	 * @param doc ����Document
	 * @return �o�͂������
	 * @throws PencilBoxClassException
	 */
	public Problem makeProblem(Document doc) throws PencilBoxClassException {
		problem = new Problem();
		property = problem.getProperty();
		Element root = doc.getDocumentElement();
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (! (child instanceof Element))
				continue;
			Element childElement = (Element)child;
			String tagName = childElement.getTagName();
			Text textNode = (Text)childElement.getFirstChild();
			String text = textNode.getData().trim();
			if (tagName.equals("type")) {
				pencilType = PencilType.getPencilType(text);
				board = (BoardBase) ClassUtil.createInstance(pencilType, ClassUtil.BOARD_CLASS);
			} else if (tagName.equals("size")) {
				readSize(text);
			} else if (tagName.equals("question")) {
				readQuestion(text);
			} else if (tagName.equals("answer")) {
				readAnswer(text);
			} else if (tagName.equals("author")) {
				property.setAuthor(text);
			} else if (tagName.equals("difficulty")) {
				property.setDifficulty(text);
			} else if (tagName.equals("source")) {
				property.setSource(text);
			}
		}
		problem.setBoard(board);
		problem.setProperty(property);
		return problem;
	}
	
	protected void readSize (String text) {
		StringTokenizer t = new StringTokenizer(text);
		int col = Integer.parseInt(t.nextToken());
		int row = Integer.parseInt(t.nextToken());
		size = new Size(row, col);
		board.setSize(size);
	}
	protected void readQuestion (String text) {
		System.out.println("readQuestion() method is not defined.");
	}
	protected void readAnswer (String text) {
		System.out.println("readAnswer() method is not defined.");
	}
	/**
	 * Board ���擾����
 	 * @return�@board
	 */
	protected BoardBase getBoard() {
		return board;
	}

}
