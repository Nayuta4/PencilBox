package pencilbox.common.core;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;



/**
 * 各盤面クラスの親クラス
 */
public class BoardBase {

	private UndoManager undoManager;
	private Size size;
	
	/**
	 * 盤面サイズをあたえて，Board 生成後の初期化処理を行う
	 * @param size 盤面の行列サイズ
	 */
	public void setSize(Size size){
		this.size = size;
		setup();
	}
	/**
	 * Board 生成後の初期化処理
	 * 配列生成などの初期化処理を行う
	 * 各サブクラスでオーバーライドする
	 */
	protected void setup(){
	}	
	/**
	 * UndoManager を設定する
	 * @param u UndoManager
	 */
	public void setUndoManager(UndoManager u) {
		undoManager = u;
	}
	/**
	 * 盤面 Size を取得する
	 * @return 盤面のSize
	 */
	public Size getSize() {
		return size;
	}
	/**
	 * 盤面の行数を取得する
	 * @return 行数
	 */
	public final int rows() {
		return size.getRows();
	}
	/**
	 * 盤面の列数を取得する
	 * @return 列数
	 */
	public final int cols() {
		return size.getCols();
	}
	/**
	 * 引数で座標を与えられたマスが盤上にあるかどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 盤上にあれば true
	 */
	public boolean isOn(int r, int c) {
		return (r >= 0 && r < rows() && c >= 0 && c < cols());
	}
	/**
	 * 引数で座標を与えられたマスが盤上にあるかどうか
	 * 盤面サイズに adjustRow, adjustCol の値の補正を加える
	 * 0 <= r < rows()+adjustRow, 0 <= c < col()+adjustCol のときに true を返す
	 * @param r 行座標
	 * @param c 列座標
	 * @param adjustRow 行の補正値
	 * @param adjustCol 列の補正値
	 * @return 盤上にあれば true
	 */
	public boolean isOn(int r, int c, int adjustRow, int adjustCol) {
		return (r >= 0 && r < rows()+adjustRow && c >= 0 && c < cols()+adjustCol);
	}
	/**
	 * 引数で座標を与えられたマスが盤上にあるかどうか
	 * @param position 調べる座標
	 * @return 盤上にあれば true
	 */
	public boolean isOn(Address position) {
		return isOn(position.r(), position.c());
	}
	/**
	 * 引数で座標を与えられた辺が盤上にあるかどうか
	 * @param d 縦か横か
	 * @param r 行座標
	 * @param c 列座標
	 * @return 盤上にあれば true
	 */
	public boolean isSideOn(int d, int r, int c) {
		if (d==Direction.VERT)
			return (r >= 0 && r < rows() && c >= 0 && c < cols()-1);
		else if (d==Direction.HORIZ)
			return (r >= 0 && r < rows()-1 && c >= 0 && c < cols());
		else
			return false;
	}
	/**
	 * 引数で座標を与えられた辺が盤上にあるかどうか
	 * @param position
	 * @return 盤上にあれば true
	 */
	public boolean isSideOn(SideAddress position) {
		return isSideOn(position.d(), position.r(), position.c());
	}
	/**
	 * 解答を消去し，問題の初期盤面に戻す
	 * 各サブクラスで実装する
	 */
	public void clearBoard() {
	}
	/**
	 * 解答から盤面から白マスや×印などの補助的入力を消去する
	 * （今後，）各サブクラスで実装する
	 */
	public void trimAnswer() {
	}
	/**
	 * 盤面設定したあとに呼び，盤面状態を初期化する
	 * 各サブクラスで必要な処理を記述する
	 */
	public void initBoard() {
	}
	/**
	 * 引数で座標を与えられたマスが盤上の最外周マスであるかどうか
	 * @param r 行座標
	 * @param c 列座標
	 * @return 最外周マスなら true
	 */
	public boolean isOnPeriphery(int r, int c) {
		return (r == 0 || r == rows() - 1 || c == 0 || c == cols() - 1);
	}
	/**
	 * アンドゥイベントリスナーにイベントの発生を通知する
	 * アンドゥ対象の操作が発生したときに呼ぶ
	 * @param e the event アンドゥ対象の操作となるイベント
	 */
	protected void fireUndoableEditUpdate(UndoableEditEvent e) {
		undoManager.undoableEditHappened(e);
	}
	/**
	 * 正解チェックで完成の場合のコメント文字列
	 */
	public static final String COMPLETE_MESSAGE = "正解です";
	
	/**
	 * 正解をチェックし，結果を文字列で返す
	 * @return 結果を表す文字列
	 */
	public String checkAnswerString() {
		return "判定機能がありません";
	}
	/**
	 * 正解チェックし，結果を数値で返す
	 * @return 正解なら 0
	 */
	public int checkAnswerCode() {
		return 0;
	}
}
