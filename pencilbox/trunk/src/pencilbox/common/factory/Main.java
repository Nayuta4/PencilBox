package pencilbox.common.factory;

import javax.swing.UIManager;


/**
 *
 */
public abstract class Main {
	
	/**
	 * 
	 */
	public Main() {
		run();
	}

	/**
	 * アプリケーションを実行する
	 */
	public void run() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			PencilType type = PencilType.getPencilType(getClass().getPackage().getName().substring(Constants.ROOT_PACKAGE_NAME.length()+1));
			new PencilFactory(type).createNewFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
