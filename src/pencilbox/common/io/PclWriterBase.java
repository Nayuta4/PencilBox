package pencilbox.common.io;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Problem;
import pencilbox.common.core.Property;


/**
 * pcl形式ファイル書き出しの基礎クラス
 */
public abstract class PclWriterBase {

	private Document doc;
	private DocumentBuilder builder;
	private BoardBase board;
	
	/**
	 * 問題データを.pcl形式でファイルに書き出す
	 * @param file ファイル 
	 * @param problem 問題
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 * @throws FileNotFoundException 
	 * @throws FileNotFoundException 
	 */
	public void writeProblem(File file, Problem problem) throws TransformerException {
		Document doc = buildDocument(problem);
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty("indent", "yes");
		t.transform(new DOMSource(doc), new StreamResult(file));
	}
	/**
	 * 問題をDOMドキュメントに変換する
	 * @param problem 入力する問題
	 * @return 出力したしたDocument
	 */
	public Document buildDocument(Problem problem) {
		this.board = problem.getBoard();
		Property property = problem.getProperty();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			// よく分からないのでとりあえず適当に処理する
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
	 * Board を取得する
 	 * @return　board
	 */
	protected BoardBase getBoard() {
		return board;
	}
}
