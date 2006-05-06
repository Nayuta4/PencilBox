package pencilbox.common.io;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Problem;
import pencilbox.common.core.Property;


/**
 * pcl�`���t�@�C�������o���̊�b�N���X
 */
public abstract class PclWriterBase {

	private Document doc;
	private DocumentBuilder builder;
	private BoardBase board;
	
	/**
	 * ����DOM�h�L�������g�ɕϊ�����
	 * @param problem ���͂�����
	 * @return �o�͂�������Document
	 */
	public Document buildDocument(Problem problem) {
		this.board = problem.getBoard();
		Property property = problem.getProperty();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			// �悭������Ȃ��̂łƂ肠�����K���ɏ�������
		}
		doc = builder.newDocument();
		Element puzzleElement = doc.createElement("puzzle");
		doc.appendChild(puzzleElement);

		Element typeElement = doc.createElement("type");
		typeElement.setTextContent(getClass().getPackage().getName());
		puzzleElement.appendChild(typeElement);

		Element sizeElement = doc.createElement("size");
		sizeElement.setTextContent(makeSizeText());
		puzzleElement.appendChild(sizeElement);

		Element questionElement = doc.createElement("question");
		questionElement.setTextContent(makeQuestionText());
		puzzleElement.appendChild(questionElement);

		Element answerElement = doc.createElement("answer");
		answerElement.setTextContent(makeAnswerText());
		puzzleElement.appendChild(answerElement);
		
		String str = property.getAuthor();
		if (str.trim().length() > 0) {
			Element element = doc.createElement("author");
			element.setTextContent(str);
			puzzleElement.appendChild(element);
		}
		str = property.getDifficulty();
		if (str.trim().length() > 0) {
			Element element = doc.createElement("difficulty");
			element.setTextContent(str);
			puzzleElement.appendChild(element);
		}
		str = property.getSource();
		if (str.trim().length() > 0) {
			Element element = doc.createElement("source");
			element.setTextContent(str);
			puzzleElement.appendChild(element);
		}
		return doc;
	}
	protected String makeSizeText(){
		return board.rows() + " " + board.cols();
	}
	protected String makeQuestionText(){
		return "";
	}
	protected String makeAnswerText()	{
		return "";
	}
	/**
	 * Board ���擾����
 	 * @return�@board
	 */
	protected BoardBase getBoard() {
		return board;
	}
}