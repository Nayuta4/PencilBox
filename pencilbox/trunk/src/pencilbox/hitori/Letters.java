package pencilbox.hitori;

/**
 * ”š‚Ì‘ã‘Ö•¶š‚ÌW‡
 */
class Letters {

	static char[] getLetterSeries(int option) {
		char[] letter;

		switch (option) {
		case 0 :
			letter = new char[] {};
			break;
		case 1 :
			letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			break;
		case 2 :
			letter = "ƒŸƒ ƒ¡ƒ¢ƒ£ƒ¤ƒ¥ƒ¦ƒ§ƒ¨ƒ©ƒªƒ«ƒ¬ƒ­ƒ®ƒ¯ƒ°ƒ±ƒ²ƒ³ƒ´ƒµƒ¶".toCharArray();
			break;
		case 3 :
			letter = "„@„A„B„C„D„E„F„G„H„I„J„K„L„M„N„O„P„Q„R„S„T„U„V„W„X„Y„Z„[„\„]„^„_„`".toCharArray();
			break;
		case 4 :
			letter = "ƒAƒCƒEƒGƒIƒJƒLƒNƒPƒRƒTƒVƒXƒZƒ\ƒ^ƒ`ƒcƒeƒgƒiƒjƒkƒlƒmƒnƒqƒtƒwƒzƒ}ƒ~ƒ€ƒƒ‚ƒ„ƒ†ƒˆƒ‰ƒŠƒ‹ƒŒƒƒ".toCharArray();
			break;
		case 5 :
			letter = "‚¢‚ë‚Í‚É‚Ù‚Ö‚Æ‚¿‚è‚Ê‚é‚ğ‚í‚©‚æ‚½‚ê‚»‚Â‚Ë‚È‚ç‚Ş‚¤‚î‚Ì‚¨‚­‚â‚Ü‚¯‚Ó‚±‚¦‚Ä‚ ‚³‚«‚ä‚ß‚İ‚µ‚ï‚Ğ‚à‚¹‚·".toCharArray();
			break;
		case 6 :
			letter = "™š›œŸ ¡¢£¤¥§˜—–•”“’‘".toCharArray();
			break;
		case 7 :
			letter = "éçéæéåéäéãéâéáéàéßéŞéİéÜéÛéÙéØé×éÖéÕéÔéÓéÒéÑéĞéÏéÎéÍéÌéËéÊéÉéÈéÇéÆéÅéÄéÃéÂéÁéÀéÀé¿é¾é½é¼é»éºé¹é¸é·é¶éµ".toCharArray();
			break;
		default :
			letter = new char[] {};
			break;
			//		letter = "ƒ¿ƒÀƒÁƒÂƒÃƒÄƒÅƒÆƒÇƒÈƒÉƒÊƒËƒÌƒÍƒÎƒÏƒĞƒÑƒÒƒÓƒÔƒÕƒÖ".toCharArray();
			//		letter = "ˆê“ñOlŒÜ˜Zµ”ª‹ã\•Sç–œ‰­’›".toCharArray();
		}
		return letter;
	}
}
