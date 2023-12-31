package pencilbox.common.core;

/**
 * �A���h�D�^���h�D�ŗp����Ֆʑ���̒P��
 */
public class AbstractStep {

	protected EditType type = EditType.NONE;

	public enum EditType {
		NONE,
		NUMBER,
		FIXED,
		STATE,
		LINE,
		;
	}
	/**
	 * �����ނ̋L��
	 * @return
	 */
	public EditType getType() {
		return type;
	}
	/**
	 * �V������������݂̑���ƍ��킹�Ĉ�̑���ɍ������邩�ǂ����B
	 * �������������ꍇ��������̂ɂ��Ă̓T�u�N���X�ŃI�[�o�[���C�h����B
	 * @param edit �V��������
	 * @return �������������Ȃ� true, �����łȂ���� false
	 */
	public boolean attachEdit(AbstractStep edit) {
		return false;
	}

}
