package pencilbox.common.factory;

import javax.swing.UIManager;

import pencilbox.common.core.PencilBoxException;

/**
 *
 */
public abstract class Main {
	
	/**
	 * 
	 */
	public Main() {
	}

	/**
	 * アプリケーションを実行する
	 * 引数の文字列がファイル名の場合はそのファイルを開く。複数可能。
	 * 上記に当てはまらない場合や，引数が空配列または空文字列の場合は，空の盤面のフレームを開く。
	 * @param args
	 */
	public void run(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		PencilType type = PencilType.getPencilType(getClass().getPackage().getName().substring(Constants.ROOT_PACKAGE_NAME.length()+1));
		PencilFactory factory = new PencilFactory(type);
		try {
			if (args.length == 0 || args[0].length() == 0) {
				factory.createNewFrame();
//			} else if (args[0].startsWith("?")) {
//				factory.createNewFrame(args[0]);
			} else {
				int success = 0;
				for (int i = 0; i < args.length; i++) {
					if (factory.createNewFrame(args[i])) {
						success++;
					}
				}
				if (success == 0)
					factory.createNewFrame();
			}
		} catch (PencilBoxException e) {
			e.printStackTrace();
		}
	}
}
