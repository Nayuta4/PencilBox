package pencilbox.hitori;

/**
 * ÌãÖ¶ÌW
 */
class Letters {

	static String getLetterSeries(int option) {
		String letters;

		switch (option) {
		case 0 :
			letters = "";
			break;
		case 1 :
			letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			break;
		case 2 :
			letters = " ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶";
			break;
		case 3 :
			letters = "@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`";
			break;
		case 4 :
			letters = "ACEGIJLNPRTVXZ\^`cegijklmnqtwz}~";
			break;
		case 5 :
			letters = "¢ëÍÉÙÖÆ¿èÊéðí©æ½ê»ÂËÈçÞ¤îÌ¨­âÜ¯Ó±¦Ä ³«äßÝµïÐà¹·";
			break;
		case 6 :
			letters = " ¡¢£¤¥§";
			break;
		case 7 :
			letters = "éçéæéåéäéãéâéáéàéßéÞéÝéÜéÛéÙéØé×éÖéÕéÔéÓéÒéÑéÐéÏéÎéÍéÌéËéÊéÉéÈéÇéÆéÅéÄéÃéÂéÁéÀéÀé¿é¾é½é¼é»éºé¹é¸é·é¶éµ";
			break;
		default :
			letters = "";
			break;
			//		letter = "¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ";
			//		letter = "êñOlÜZµªã\Sç­";
		}
		return letters;
	}
}
