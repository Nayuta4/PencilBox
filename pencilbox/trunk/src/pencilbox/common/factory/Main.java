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
	 * �A�v���P�[�V���������s����
	 * �����̕����񂪃t�@�C�����̏ꍇ�͂��̃t�@�C�����J���B�����\�B
	 * ��L�ɓ��Ă͂܂�Ȃ��ꍇ��C��������z��܂��͋󕶎���̏ꍇ�́C��̔Ֆʂ̃t���[�����J���B
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
