------------------------------------------------------------------------
  PencilBox version 1.4.0
                                                      2009-08-11 Nayuta
------------------------------------------------------------------------

■概要
PencilBoxは，(株)ニコリ発行の雑誌「パズル通信ニコリ」に掲載されている
各種ペンシルパズルを，鉛筆をマウスに持ち替えてパソコン上で遊んだり作ったり
するためのプログラムです。

主な特長として，以下の機能があります。
  ・TXT形式またはXML形式による問題ファイルの読み書き
  ・マウスまたはキーボードを用いた解答入力
  ・マウスとキーボードを用いた問題入力
  ・解答操作の UNDO, REDO
  ・正解判定
  ・パズルの種類に応じた各種ヒント表示

以下の18種類のペンシルパズルに対応しています。
   1. カックロ
   2. 黒マスはどこだ
   3. 四角に切れ
   4. 数独
   5. スラローム
   6. スリザーリンク
   7. 天体ショー
   8. ナンバーリンク
   9. ぬりかべ
  10. 波及効果
  11. 橋をかけろ
  12. 美術館
  13. ひとりにしてくれ
  14. フィルオミノ
  15. へやわけ
  16. ましゅ
  17. ヤジリン
  18. ＬＩＴＳ

■動作環境
PencilBox の実行には Java Runtime Environment のversion1.5以上が必要です。
Java Runtime Environment は http://java.com/ より入手できます。

作者は Windows XP 上で使用しています。他の環境でも同様に動作することを
期待していますが，特に動作確認は行っていません。

■使用条件
PencilBox はフリーソフトです。GNU GPL に従い変更，再配布を行うことができます。
詳細は LICENSE.txt を参照下さい。

PencilBox の使用により発生したいかなる損害に対しても，作者は責任を負いません。
余計なヒントのせいでパズルがつまらなくなったなどと言われても関知しません。

■実行方法
pencilbox.jar が全種類で共通に使用するクラスファイルをまとめたもので，
kakuro.jar など各パズルの名前のついたjarファイルは，個々の種類でのみ使用する
クラスファイルをまとめたものです。

例えばカックロのプログラムを実行するには，java -jar kakuro.jar とします。
このとき，pencilbox.jar が同じディレクトリにある必要があります。
環境によっては，jarファイルのアイコンをダブルクリックすることにより
プログラムを実行できます。

また，java -jar pencilbox.jar とすると，パズルの種類の書かれたボタンが並んだ
パネルが現れるので，実行する種類のボタンを選択してください。
終了と書かれたボタンを選択すると，プログラムを終了します。

操作方法の詳細については pencilbox.html を参照下さい。

■連絡先
PencilBoxに関する情報やダウンロード先については以下のURLを参照ください。
  URL: http://pencilbox.sourceforge.jp/

ユーザー用メーリングリストを運用開始しました。
不具合情報や改善提案などございましたら，ご連絡いただければ幸いです。
利用される方は以下から登録して下さい。
  http://lists.sourceforge.jp/mailman/listinfo/pencilbox-users

または作者に直接e-mailで連絡を下っても構いません。
  e-mail: nayuta@kanpen.net

■謝辞
Heyawake Applet Developer 様作成の HeyawakeBox version 1.2.1
 (http://www.geocities.jp/heyawake/box/)のソースコードを一部取り入れています。
感謝いたします。

■内容物
配布アーカイブには以下のファイルが含まれます。（順不同）
 readme.txt      このファイル
 LICENSE.txt      GNU GPL 条文 
 history.txt      改訂履歴
 pencilbox.html   マニュアル
 pencilbox.ini    設定ファイル（初期状態の内容は空です）
 style.css        pencilbox.html用スタイルシート
 pencilbox.jar    全体で共通に使用するクラスファイル
 bijutsukan.jar   美術館用クラスファイル
 fillomino.jar    フィルオミノ用クラスファイル
 hakyukoka.jar    波及効果用クラスファイル
 hashi.jar        橋をかけろ用クラスファイル
 heyawake.jar     へやわけ用クラスファイル
 hitori.jar       ひとりにしてくれ用クラスファイル
 kakuro.jar       カックロ用クラスファイル
 kurodoko.jar     黒マスはどこだ用クラスファイル
 masyu.jar        ましゅ用クラスファイル
 numberlink.jar   ナンバーリンク用クラスファイル
 nurikabe.jar     ぬりかべ用クラスファイル
 shikaku.jar      四角に切れ用クラスファイル
 slalomjar        スラローム用クラスファイル
 slitherlink.jar  スリザーリンク用クラスファイル
 sudoku.jar       数独用クラスファイル
 tentaisho.jar    天体ショー用クラスファイル
 yajilin.jar      ヤジリン用クラスファイル
 lits.jar         ＬＩＴＳ用クラスファイル
 problem/         サンプル問題を含むディレクトリ


/*
 PencilBox : a pencil puzzle editor
 Copyright (C) 2006-2009 Nayuta
 
 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 oundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
