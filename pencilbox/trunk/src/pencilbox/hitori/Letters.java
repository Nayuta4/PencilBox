package pencilbox.hitori;

/**
 * ÌãÖ¶ÌW
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
			letter = " ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶".toCharArray();
			break;
		case 3 :
			letter = "@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`".toCharArray();
			break;
		case 4 :
			letter = "ACEGIJLNPRTVXZ\^`cegijklmnqtwz}~".toCharArray();
			break;
		case 5 :
			letter = "¢ëÍÉÙÖÆ¿èÊéðí©æ½ê»ÂËÈçÞ¤îÌ¨­âÜ¯Ó±¦Ä ³«äßÝµïÐà¹·".toCharArray();
			break;
		case 6 :
			letter = " ¡¢£¤¥§".toCharArray();
			break;
		case 7 :
			letter = "éçéæéåéäéãéâéáéàéßéÞéÝéÜéÛéÙéØé×éÖéÕéÔéÓéÒéÑéÐéÏéÎéÍéÌéËéÊéÉéÈéÇéÆéÅéÄéÃéÂéÁéÀéÀé¿é¾é½é¼é»éºé¹é¸é·é¶éµ".toCharArray();
			break;
		default :
			letter = new char[] {};
			break;
			//		letter = "¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ".toCharArray();
			//		letter = "êñOlÜZµªã\Sç­".toCharArray();
		}
		return letter;
	}
}
