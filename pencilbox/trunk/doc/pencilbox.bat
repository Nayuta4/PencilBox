rem PencilBox���s�o�b�`�t�@�C���̗�

rem PencilBox�����s����

javaw -cp pencilbox.jar pencilbox.common.factory.PencilBoxLauncher

exit

rem �X���U�[�����N�Ȃǂ̋���T�C�Y�̔ՖʂȂǂ�StackOverflowError����������ꍇ�́C
rem javaw -Xss1m �ȂǂƎw�肵�Ă݂Ă��������B

rem �p�Y���̎�ނ��Ƃɖ��t�@�C�����w�肵�Ď��s����

javaw -cp bijutsukan.jar  pencilbox.bijutsukan.Main  sample/bijutsukan/BJ_sample02.txt 
javaw -cp fillomino.jar   pencilbox.fillomino.Main   sample/fillomino/FO_sample01.txt 
javaw -cp goishi.jar      pencilbox.goishi.Main      sample/goishi/GO_sample01.txt 
javaw -cp hakyukoka.jar   pencilbox.hakyukoka.Main   sample/hakyukoka/HK_sample01.txt 
javaw -cp hashi.jar       pencilbox.hashi.Main       sample/hashi/HS_sample01.txt 
javaw -cp heyawake.jar    pencilbox.heyawake.Main    sample/heyawake/HW_sample01.txt 
javaw -cp hitori.jar      pencilbox.hitori.Main      sample/hitori/HT_sample01.txt 
javaw -cp kakuro.jar      pencilbox.kakuro.Main      sample/kakuro/KK_sample02.txt 
javaw -cp kurodoko.jar    pencilbox.kurodoko.Main    sample/kurodoko/KD_sample01.txt 
javaw -cp lits.jar        pencilbox.lits.Main        sample/lits/LI_sample01.txt 
javaw -cp masyu.jar       pencilbox.masyu.Main       sample/masyu/MS_sample01.txt 
javaw -cp numberlink.jar  pencilbox.numberlink.Main  sample/numberlink/NL_sample02.txt 
javaw -cp nurikabe.jar    pencilbox.nurikabe.Main    sample/nurikabe/NK_sample01.txt 
javaw -cp shikaku.jar     pencilbox.shikaku.Main     sample/shikaku/SK_sample01.txt 
javaw -cp slalom.jar      pencilbox.slalom.Main      sample/slalom/SM-sample01.txt 
javaw -cp slitherlink.jar pencilbox.slitherlink.Main sample/ slitherlink/SL_sample01.txt 
javaw -cp sudoku.jar      pencilbox.sudoku.Main      sample/sudoku/SD_sample01.txt 
javaw -cp tentaisho.jar   pencilbox.tentaisho.Main   sample/tentaisho/TS_sample01.txt 
javaw -cp yajilin.jar     pencilbox.yajilin.Main     sample/yajilin/YLsample01.txt 

rem png�摜�ꊇ�ϊ��B�t�@�C���̖����� .png ��t�������摜�t�@�C�����o�͂����B

javaw -cp pencilbox.jar  pencilbox.tool.PngExport bijutsukan  sample/bijutsukan/BJ_sample02.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport fillomino   sample/fillomino/FO_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport goishi      sample/goishi/GO_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport hakyukoka   sample/hakyukoka/HK_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport hashi       sample/hashi/HS_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport heyawake    sample/heyawake/HW_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport hitori      sample/hitori/HT_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport kakuro      sample/kakuro/KK_sample02.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport kurodoko    sample/kurodoko/KD_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport lits        sample/lits/LI_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport masyu       sample/masyu/MS_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport numberlink  sample/numberlink/NL_sample02.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport nurikabe    sample/nurikabe/NK_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport shikaku     sample/shikaku/SK_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport slalom      sample/slalom/SM-sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport slitherlink sample/slitherlink/SL_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport sudoku      sample/sudoku/SD_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport tentaisho   sample/tentaisho/TS_sample01.txt 
javaw -cp pencilbox.jar  pencilbox.tool.PngExport yajilin     sample/yajilin/YLsample01.txt 
