package pencilbox.common.core;

import pencilbox.resource.Messages;



/**
 * 各盤面クラスの親クラス
 */
public class BoardBase {

	private UndoManager undoManager;
	private Size size;

	protected Address[] cellAddrs; // 盤面全体のマス座標リスト

	/**
	 * 盤面サイズをあたえて，Board 生成後の初期化処理を行う
	 * @param size 盤面の行列サイズ
	 */
	public void setSize(Size size){
		Address.createAddressInstances(size);
		SideAddress.createSideAddressInstances(size);
		this.size = size;
		prepareAddresse();
		setup();
	}

	public void setSize(int r, int c){
		this.setSize(new Size(r, c));
	}
	/**
	 * Board 生成後の初期化処理
	 * 配列生成などの初期化処理を行う
	 * 各サブクラスでオーバーライドする
	 */
	protected void setup(){
	}	

	protected void prepareAddresse() {
		this.cellAddrs = new Address[rows()*cols()];
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				cellAddrs[r*cols()+c] = Address.address(r, c);
			}
		}
	}

	/**
	 * UndoManager を設定する
	 * @param u UndoManager
	 */
	public void setUndoManager(UndoManager u) {
		undoManager = u;
	}

	public UndoManager getUndoManager() {
		return undoManager;
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

	public boolean isRecordUndo() {
		if (getUndoManager() != null)
			return getUndoManager().isRecordUndo();
		return false;
	}

	public void setRecordUndo(boolean b) {
		if (getUndoManager() != null)
			getUndoManager().setRecordUndo(b);
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
	 * 引数で座標を与えられたマスが盤上にあるかどうか
	 * @param position 調べる座標
	 * @return 盤上にあれば true
	 */
	public boolean isOnAll(Address... positions) {
		for (Address p : positions)
			if (! isOn(p))
				return false;
		return true;
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
	 * 引数で座標を与えられた辺が盤上にあるかどうか
	 * @param position
	 * @return 盤上にあれば true
	 */
	public boolean isSideOn(Address pos, int dir) {
		switch (dir) {
		case Direction.UP :
			return isSideOn(Direction.HORIZ, pos.r()-1, pos.c());
		case Direction.LT :
			return isSideOn(Direction.VERT, pos.r(), pos.c()-1);
		case Direction.DN :
			return isSideOn(Direction.HORIZ, pos.r(), pos.c());
		case Direction.RT :
			return isSideOn(Direction.VERT, pos.r(), pos.c());
		default :
			return false;
		}
	}
	/**
	 * 引数で与えられた領域全体が盤内にあるか
	 * @param area 領域
	 * @return 領域全体が盤内にあれば true, そうでなければ false を返す。
	 */
	public boolean isAreaOn(Area area) {
		for (Address pos : area) {
			if (!isOn(pos))
				return false;
		}
		return true;
	}

	/**
	 * 盤面全体を含む領域を作成して返す。
	 * @return 盤面全体を含む領域。
	 */
	public Area getWholeBoardArea() {
		Area area = new Area();
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				area.add(Address.address(r, c));
			}
		}
		return area;
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
	protected void fireUndoableEditUpdate(AbstractStep e) {
//		if (isRecordUndo())
		undoManager.addEdit(e);
	}
	/**
	 * アンドゥ
	 * 具体的な操作は各サブクラスで実装する
	 * @param step
	 */
	public void undo(AbstractStep step) {
	}
	/**
	 * リドゥ
	 * 具体的な操作は各サブクラスで実装する
	 * @param step
	 */
	public void redo(AbstractStep step) {
	}
	/**
	 * 正解チェックで完成の場合のコメント文字列
	 */
	public static final String COMPLETE_MESSAGE = Messages.getString("BoardBase.MessageComplete"); //$NON-NLS-1$
	
	/**
	 * 正解をチェックし，結果を文字列で返す
	 * @return 結果を表す文字列
	 */
	public String checkAnswerString() {
		return Messages.getString("BoardBase.MessageUnavailable"); //$NON-NLS-1$
	}
	/**
	 * 正解チェックし，結果を数値で返す
	 * @return 正解なら 0
	 */
	public int checkAnswerCode() {
		return 0;
	}

}
