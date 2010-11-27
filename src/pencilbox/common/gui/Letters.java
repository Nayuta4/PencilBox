package pencilbox.common.gui;

/**
 * ”š‚Ì‘ã‘Ö•¶š‚ÌW‡
 */
public class Letters {

	public static String getLetterSeries(int option) {
		String letters;

		switch (option) {
		case 0 :
			letters = "";
			break;
		case 1 :
			letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			break;
		case 2 :
			letters = "ƒŸƒ ƒ¡ƒ¢ƒ£ƒ¤ƒ¥ƒ¦ƒ§ƒ¨ƒ©ƒªƒ«ƒ¬ƒ­ƒ®ƒ¯ƒ°ƒ±ƒ²ƒ³ƒ´ƒµƒ¶";
			break;
		case 3 :
			letters = "„@„A„B„C„D„E„F„G„H„I„J„K„L„M„N„O„P„Q„R„S„T„U„V„W„X„Y„Z„[„\„]„^„_„`";
			break;
		case 4 :
			letters = "ƒAƒCƒEƒGƒIƒJƒLƒNƒPƒRƒTƒVƒXƒZƒ\ƒ^ƒ`ƒcƒeƒgƒiƒjƒkƒlƒmƒnƒqƒtƒwƒzƒ}ƒ~ƒ€ƒƒ‚ƒ„ƒ†ƒˆƒ‰ƒŠƒ‹ƒŒƒƒ";
			break;
		case 5 :
			letters = "‚¢‚ë‚Í‚É‚Ù‚Ö‚Æ‚¿‚è‚Ê‚é‚ğ‚í‚©‚æ‚½‚ê‚»‚Â‚Ë‚È‚ç‚Ş‚¤‚î‚Ì‚¨‚­‚â‚Ü‚¯‚Ó‚±‚¦‚Ä‚ ‚³‚«‚ä‚ß‚İ‚µ‚ï‚Ğ‚à‚¹‚·";
			break;
		case 6 :
			letters = "™š›œŸ ¡¢£¤¥§˜—–•”“’‘";
			break;
		case 7 :
			letters = "éçéæéåéäéãéâéáéàéßéŞéİéÜéÛéÙéØé×éÖéÕéÔéÓéÒéÑéĞéÏéÎéÍéÌéËéÊéÉéÈéÇéÆéÅéÄéÃéÂéÁéÀéÀé¿é¾é½é¼é»éºé¹é¸é·é¶éµ";
			break;
		default :
			letters = "";
			break;
			//		letter = "ƒ¿ƒÀƒÁƒÂƒÃƒÄƒÅƒÆƒÇƒÈƒÉƒÊƒËƒÌƒÍƒÎƒÏƒĞƒÑƒÒƒÓƒÔƒÕƒÖ";
			//		letter = "ˆê“ñOlŒÜ˜Zµ”ª‹ã\•Sç–œ‰­’›";
		}
		return letters;
	}
}
