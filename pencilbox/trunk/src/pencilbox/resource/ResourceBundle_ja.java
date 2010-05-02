package pencilbox.resource;

import java.util.ListResourceBundle;

public class ResourceBundle_ja extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {

    { "PencilBoxLauncher.quit"                      , "終了"                                                       } ,
    { "PencilType.bijutsukan"                       , "美術館"                                                     } ,
    { "PencilType.fillomino"                        , "フィルオミノ"                                               } ,
    { "PencilType.goishi"                           , "碁石ひろい"                                                 } ,
    { "PencilType.hakyukoka"                        , "波及効果"                                                   } ,
    { "PencilType.hashi"                            , "橋をかけろ"                                                 } ,
    { "PencilType.heyawake"                         , "へやわけ"                                                   } ,
    { "PencilType.hitori"                           , "ひとりにしてくれ"                                           } ,
    { "PencilType.kakuro"                           , "カックロ"                                                   } ,
    { "PencilType.kurodoko"                         , "黒どこ"                                                     } ,
    { "PencilType.lits"                             , "ＬＩＴＳ"                                                   } ,
    { "PencilType.masyu"                            , "ましゅ"                                                     } ,
    { "PencilType.numberlink"                       , "ナンバーリンク"                                             } ,
    { "PencilType.nurikabe"                         , "ぬりかべ"                                                   } ,
    { "PencilType.shikaku"                          , "四角に切れ"                                                 } ,
    { "PencilType.slalom"                           , "スラローム"                                                 } ,
    { "PencilType.slitherlink"                      , "スリザーリンク"                                             } ,
    { "PencilType.sudoku"                           , "数独"                                                       } ,
    { "PencilType.tentaisho"                        , "天体ショー"                                                 } ,
    { "PencilType.yajilin"                          , "ヤジリン"                                                   } ,

    { "Problem.Untitled"                            , "無題"                                                       } ,

    { "MenuBase.fileMenu"                           , "ファイル(F)"                                                } ,
    { "MenuBase.newBoardItem"                       , "新規作成(N)..."                                             } ,
    { "MenuBase.openItem"                           , "開く(O)..."                                                 } ,
    { "MenuBase.closeAndOpenItem"                   , "閉じて開く(L)..."                                           } ,
    { "MenuBase.saveItem"                           , "保存(S)..."                                                 } ,
    { "MenuBase.duplicateItem"                      , "複製(D)"                                                    } ,
    { "MenuBase.rotationItem"                       , "回転・反転(R)"                                              } ,
    { "MenuBase.changeBoardSizeItem"                , "盤面サイズ変更(Z)"                                          } ,
    { "MenuBase.exportMenu"                         , "問題データ出力(X)"                                          } ,
    { "MenuBase.saveImageItem"                      , "画像保存(G)..."                                             } ,
    { "MenuBase.copyImageItem"                      , "画像コピー(M)"                                              } ,
    { "MenuBase.printPreviewItem"                   , "印刷プレビュー(V)..."                                       } ,
    { "MenuBase.printItem"                          , "印刷(P)..."                                                 } ,
    { "MenuBase.loadPreferencesItem"                , "設定読込(F)..."                                             } ,
    { "MenuBase.storePreferencesItem"               , "設定保存(T)..."                                             } ,
    { "MenuBase.propertyItem"                       , "プロパティ(I)..."                                           } ,
    { "MenuBase.closeItem"                          , "閉じる(C)"                                                  } ,
    { "MenuBase.quitItem"                           , "終了(Q)"                                                    } ,
    { "MenuBase.editMenu"                           , "編集(E)"                                                    } ,
    { "MenuBase.answerModeItem"                     , "解答モード(A)"                                              } ,
    { "MenuBase.problemEditModeItem"                , "問題入力モード(E)"                                          } ,
    { "MenuBase.regionEditModeItem"                 , "領域編集モード(G)"                                          } ,
    { "MenuBase.clearItem"                          , "解答消去(C)"                                                } ,
    { "MenuBase.trimAnswerItem"                     , "補助記号消去(T)"                                            } ,
    { "MenuBase.symmetricPlacementItem"             , "対称配置(S)"                                                } ,
    { "MenuBase.undoItem"                           , "元に戻す(U)"                                                } ,
    { "MenuBase.redoItem"                           , "やり直し(R)"                                                } ,
    { "MenuBase.playbackItem"                       , "履歴再生(P)"                                                } ,
    { "MenuBase.historyItem"                        , "履歴(H)..."                                                 } ,
    { "MenuBase.checkAnswerItem"                    , "正解判定(K)"                                                } ,
    { "MenuBase.immediateAnswerCheckItem"           , "即時正解判定(M)"                                            } ,
    { "MenuBase.viewMenu"                           , "表示(V)"                                                    } ,
    { "MenuBase.colorMenu"                          , "色の設定(L)"                                                } ,
    { "MenuBase.backgroundColorItem"                , "背景"                                                       } ,
    { "MenuBase.gridColorItem"                      , "罫線"                                                       } ,
    { "MenuBase.renewColorItem"                     , "色の更新(U)"                                                } ,
    { "MenuBase.cellSizeItem"                       , "表示サイズ(S)..."                                           } ,
    { "MenuBase.indexItem"                          , "行列番号表示(I)"                                            } ,
    { "MenuBase.gridStyleItem"                      , "罫線表示(G)"                                                } ,
    { "MenuBase.cursorItem"                         , "カーソル(C)"                                                } ,
    { "MenuBase.markStyleMenu"                      , "塗らない印(N)"                                              } ,
    { "MenuBase.linkWidthItem"                      , "線の太さ(W)"                                                } ,
    { "MenuBase.helpMenu"                           , "ヘルプ(H)"                                                  } ,
    { "MenuBase.aboutDialog"                        , "PencilBox について(A)"                                      } ,

    { "MenuBase.rotationItem1"                      , "左90°回転(1)"                                              } ,
    { "MenuBase.rotationItem2"                      , "180°回転(2)"                                               } ,
    { "MenuBase.rotationItem3"                      , "右90°回転(3)"                                              } ,
    { "MenuBase.rotationItem4"                      , "縦横交換(4)"                                                } ,
    { "MenuBase.rotationItem5"                      , "左右反転(5)"                                                } ,
    { "MenuBase.rotationItem6"                      , "180°回転+縦横交換(6)"                                      } ,
    { "MenuBase.rotationItem7"                      , "上下反転(7)"                                                } ,
    { "MenuBase.exportItemKanpen"                   , "カンペン(K)"                                                } ,
    { "MenuBase.exportItemPzprv3"                   , "ぱずぷれv3(Z)"                                              } ,
    { "MenuBase.exportItemHeyawake"                 , "へやわけアプレット(H)"                                      } ,
    { "MenuBase.markStyle0"                         , "非表示"                                                     } ,
    { "MenuBase.markStyle1"                         , "○"                                                         } ,
    { "MenuBase.markStyle2"                         , "●"                                                         } ,
    { "MenuBase.markStyle3"                         , "■"                                                         } ,
    { "MenuBase.markStyle4"                         , "×"                                                         } ,
    { "MenuBase.markStyle5"                         , "塗りつぶし"                                                 } ,

    { "MenuCommand.newBoardDialog"                  , "新規作成"                                                   } ,
    { "MenuCommand.rotateBoardDialog"               , "回転・反転"                                                 } ,
    { "MenuCommand.changeBoardSizeDialog"           , "盤面サイズ変更"                                             } ,
    { "MenuCommand.properyDialog"                   , "プロパティ"                                                 } ,
    { "MenuCommand.dataExportDialog"                , "問題データ出力"                                             } ,
    { "MenuCommand.checkAnswerDialog"               , "正解判定"                                                   } ,
    { "MenuCommand.historyDialog"                   , "履歴"                                                       } ,
    { "MenuCommand.cellSizeDialog"                  , "表示サイズ"                                                 } ,
    { "MenuCommand.linkWidthDialog"                 , "線の太さ"                                                   } ,
    { "MenuCommand.aboutDialog"                     , "PencilBox について"                                         } ,
    { "MenuCommand.Message_Error"                   , "Error"                                                      } ,
    { "MenuCommand.Message_InappropriateSize"       , "不正なサイズです"                                           } ,
    { "MenuCommand.Message_TooLarge"                , "大きすぎです"                                               } ,

    { "PencilBoxDialog.buttonOK"                    , "了解"                                                       } ,
    { "PencilBoxDialog.buttonCancel"                , "取消"                                                       } ,
    { "NewBoardDialog.labelCols"                    , "ヨコ："                                                     } ,
    { "NewBoardDialog.labelRows"                    , "タテ："                                                     } ,
    { "NewBoardDialog.checkBoxSquare"               , "正方形"                                                     } ,
    { "DataExportDialog.buttonOpen"                 , "問題を別フレームに開く"                                     } ,
    { "DataExportDialog.buttonCopy"                 , "クリップボードにコピーする"                                 } ,
    { "DataExportDialog.buttonCancel"               , "閉じる"                                                     } ,
    { "PropertyDialog.labelAuthor"                  , "作者"                                                       } ,
    { "PropertyDialog.labelDifficulty"              , "難易度"                                                     } ,
    { "PropertyDialog.labelSource"                  , "出典"                                                       } ,
    { "PanelEventHandlerBase.checkAnswerDialog"     , "正解判定"                                                   } ,

    { "Menu.countAreaSizeItem"                      , "シマのマス数を数える(C)"                                    } ,
    { "Menu.dotHintItem"                            , "可能な数字をドットで示す(D)"                                } ,
    { "Menu.hideSoleNumberItem"                     , "初めからひとりの数字を隠す(H)"                              } ,
    { "Menu.hideStarItem"                           , "星の非表示(H)"                                              } ,
    { "Menu.highlightSelectionItem"                 , "選択数字ハイライト(H)"                                      } ,
    { "Menu.indicateCompletionItem"                 , "正誤を色で示す(E)"                                          } ,
    { "Menu.indicateErrorItem"                      , "誤りを赤で示す(E)"                                          } ,
    { "Menu.noBulbStyleMenu"                        , "照明なしの印(N)"                                            } ,
    { "Menu.paintIlluminatedCellItem"               , "照らされたマスを塗る(P)"                                    } ,
    { "Menu.SelectLetterDialog"                     , "文字種類の設定"                                             } ,
    { "Menu.selectLetterItem"                       , "文字種類の設定(T)..."                                       } ,
    { "Menu.separateAreaColorItem"                  , "領域の色分け(R)"                                            } ,
    { "Menu.separateLinkColorItem"                  , "線の色分け(R)"                                              } ,
    { "Menu.separateWallColorItem"                  , "壁の色分け(R)"                                              } ,
    { "Menu.separateTetrominoColorItem"             , "テトロミノの色分け(D)"                                      } ,
    { "Menu.showAreaBorderItem"                     , "境界線表示(B)"                                              } ,
    { "Menu.showBeamItem"                           , "光線表示(B)"                                                } ,

    { "Menu.numberColorItem"                        , "数字"                                                       } ,
    { "Menu.fixedNumberColorItem"                   , "問題数字"                                                   } ,
    { "Menu.inputColorItem"                         , "解答数字"                                                   } ,
    { "Menu.wallColorItem"                          , "黒マス"                                                     } ,
    { "Menu.paintColorItem"                         , "黒マス"                                                     } ,
    { "Menu.noPaintColorItem"                       , "塗らない印"                                                 } ,
    { "Menu.circleColorItem"                        , "丸印"                                                       } ,
    { "Menu.areaBorderColorItem"                    , "領域境界"                                                   } ,
    { "Menu.areaPaintColorItem"                     , "領域内部"                                                   } ,
    { "Menu.lineColorItem"                          , "線"                                                         } ,
    { "Menu.crossColorItem"                         , "×印"                                                       } ,
    { "Menu.borderColorItem"                        , "境界線"                                                     } ,
    { "Menu.gateColorItem"                          , "門"                                                         } ,
    { "Menu.whiteAreaColorItem"                     , "白星領域"                                                   } ,
    { "Menu.blackAreaColorItem"                     , "黒星領域"                                                   } ,
    { "Menu.bulbColorItem"                          , "照明"                                                       } ,
    { "Menu.noBulbColorItem"                        , "照明なし"                                                   } ,
    { "Menu.illuminatedCellColorItem"               , "照らされたマス"                                             } ,
    { "Menu.stoneOutlineColorItem"                  , "石の縁"                                                     } ,
    { "Menu.stoneColorItem"                         , "石"                                                         } ,

    { "bijutsukan.AnswerCheckMessage1"              , "複数の照明のある列がある\n"                                 } ,
    { "bijutsukan.AnswerCheckMessage2"              , "照らされていないマスがある\n"                               } ,
    { "bijutsukan.AnswerCheckMessage3"              , "照明個数の合っていない数字がある\n"                         } ,
    { "fillomino.AnswerCheckMessage1"               , "空白マスがある\n"                                           } ,
    { "fillomino.AnswerCheckMessage2"               , "数字より面積の大きい領域がある\n"                           } ,
    { "fillomino.AnswerCheckMessage3"               , "数字より面積の小さい領域がある\n"                           } ,
    { "goishi.AnswerCheckMessage1"                  , "石が残っている\n"                                           } ,
    { "goishi.AnswerCheckMessage2"                  , "石が１つもない\n"                                           } ,
    { "hakyukoka.AnswerCheckMessage1"               , "未完成\n"                                                   } ,
    { "hakyukoka.AnswerCheckMessage2"               , "同じ数字を複数含む領域がある\n"                             } ,
    { "hakyukoka.AnswerCheckMessage3"               , "領域面積より大きい数字を含む領域がある\n"                   } ,
    { "hakyukoka.AnswerCheckMessage4"               , "間隔不足の数字がある\n"                                     } ,
    { "hashi.AnswerCheckMessage1"                   , "橋が交差している\n"                                         } ,
    { "hashi.AnswerCheckMessage2"                   , "全体がひとつながりになっていない\n"                         } ,
    { "hashi.AnswerCheckMessage3"                   , "橋の数が数字と一致していない\n"                             } ,
    { "heyawake.AnswerCheckMessage1"                , "連続する黒マスがある\n"                                     } ,
    { "heyawake.AnswerCheckMessage2"                , "黒マスにより盤面が分断されている\n"                         } ,
    { "heyawake.AnswerCheckMessage3"                , "数字と黒マス数の一致していない部屋がある\n"                 } ,
    { "heyawake.AnswerCheckMessage4"                , "白マスが３部屋以上続いている箇所がある\n"                   } ,
    { "hitori.AnswerCheckMessage1"                  , "連続する黒マスがある\n"                                     } ,
    { "hitori.AnswerCheckMessage2"                  , "黒マスにより盤面が分断されている\n"                         } ,
    { "hitori.AnswerCheckMessage3"                  , "ひとりでない数字がある\n"                                   } ,
    { "kakuro.AnswerCheckMessage1"                  , "未完成\n"                                                   } ,
    { "kakuro.AnswerCheckMessage2"                  , "縦横に重複する数字がある\n"                                 } ,
    { "kakuro.AnswerCheckMessage3"                  , "正しくない計がある\n"                                       } ,
    { "kurodoko.AnswerCheckMessage1"                , "連続する黒マスがある\n"                                     } ,
    { "kurodoko.AnswerCheckMessage2"                , "黒マスにより盤面が分断されている\n"                         } ,
    { "kurodoko.AnswerCheckMessage3"                , "白マス数の足りない数字がある\n"                             } ,
    { "kurodoko.AnswerCheckMessage4"                , "白マス数の多すぎる数字がある\n"                             } ,
    { "lits.AnswerCheckMessage1"                    , "ブロックが１つもない\n"                                     } ,
    { "lits.AnswerCheckMessage2"                    , "テトロミノを含まないブロックがある\n"                       } ,
    { "lits.AnswerCheckMessage3"                    , "同じ形のテトロミノが隣接している\n"                         } ,
    { "lits.AnswerCheckMessage4"                    , "黒マスがひとつながりになっていない\n"                       } ,
    { "lits.AnswerCheckMessage5"                    , "黒マスが２ｘ２マスのカタマリになっている\n"                 } ,
    { "masyu.AnswerCheckMessage1"                   , "線が分岐または交差している\n"                               } ,
    { "masyu.AnswerCheckMessage2"                   , "閉じていない線がある\n"                                     } ,
    { "masyu.AnswerCheckMessage3"                   , "○の通り方の間違いがある\n"                                 } ,
    { "masyu.AnswerCheckMessage4"                   , "●の通り方の間違いがある\n"                                 } ,
    { "masyu.AnswerCheckMessage5"                   , "複数の線がある\n"                                           } ,
    { "masyu.AnswerCheckMessage6"                   , "線が通っていない○がある\n"                                 } ,
    { "masyu.AnswerCheckMessage7"                   , "線が通っていない●がある\n"                                 } ,
    { "masyu.AnswerCheckMessage8"                   , "線がない\n"                                                 } ,
    { "numberlink.AnswerCheckMessage1"              , "複数の線が出ている数字がある\n"                             } ,
    { "numberlink.AnswerCheckMessage2"              , "線の出ていない数字がある\n"                                 } ,
    { "numberlink.AnswerCheckMessage3"              , "線が分岐または交差している\n"                               } ,
    { "numberlink.AnswerCheckMessage4"              , "途中で切れている線がある\n"                                 } ,
    { "numberlink.AnswerCheckMessage5"              , "異なる数字につながる線がある\n"                             } ,
    { "numberlink.AnswerCheckMessage6"              , "数字につながっていない線がある\n"                           } ,
    { "nurikabe.AnswerCheckMessage1"                , "空白マスがある\n"                                           } ,
    { "nurikabe.AnswerCheckMessage2"                , "数字と面積が一致していないシマがある\n"                     } ,
    { "nurikabe.AnswerCheckMessage4"                , "数字を複数含むシマがある\n"                                 } ,
    { "nurikabe.AnswerCheckMessage5"                , "数字を含まないシマがある\n"                                 } ,
    { "nurikabe.AnswerCheckMessage6"                , "黒マスが複数に分断されている\n"                             } ,
    { "nurikabe.AnswerCheckMessage7"                , "黒マスが２ｘ２マスのかたまりになっている\n"                 } ,
    { "shikaku.AnswerCheckMessage1"                 , "複数の数字を含む四角がある\n"                               } ,
    { "shikaku.AnswerCheckMessage2"                 , "数字を含まない四角がある\n"                                 } ,
    { "shikaku.AnswerCheckMessage3"                 , "面積が数字を超える四角がある\n"                             } ,
    { "shikaku.AnswerCheckMessage4"                 , "面積が数字に満たない四角がある\n"                           } ,
    { "shikaku.AnswerCheckMessage5"                 , "四角に含まれないマスがある\n"                               } ,
    { "shikaku.AnswerCheckMessage6"                 , "盤上に数字がひとつもない\n"                                 } ,
    { "slalom.AnswerCheckMessage1"                  , "線が分岐または交差している\n"                               } ,
    { "slalom.AnswerCheckMessage2"                  , "閉じていない線がある\n"                                     } ,
    { "slalom.AnswerCheckMessage3"                  , "複数の線がある\n"                                           } ,
    { "slalom.AnswerCheckMessage4"                  , "線がない\n"                                                 } ,
    { "slalom.AnswerCheckMessage5"                  , "門の通り方の間違いがある\n"                                 } ,
    { "slalom.AnswerCheckMessage6"                  , "通っていない門がある\n"                                     } ,
    { "slalom.AnswerCheckMessage7"                  , "複数回通った門がある\n"                                     } ,
    { "slalom.AnswerCheckMessage8"                  , "門を通る順番が誤っている\n"                                 } ,
    { "slalom.AnswerCheckMessage9"                  , "○を通っていない\n"                                         } ,
    { "slitherlink.AnswerCheckMessage1"             , "線が分岐または交差している\n"                               } ,
    { "slitherlink.AnswerCheckMessage2"             , "閉じていない線がある\n"                                     } ,
    { "slitherlink.AnswerCheckMessage3"             , "線の数が数字より多いところがある\n"                         } ,
    { "slitherlink.AnswerCheckMessage5"             , "複数の線がある\n"                                           } ,
    { "slitherlink.AnswerCheckMessage6"             , "線の数が数字より少ないところがある\n"                       } ,
    { "slitherlink.AnswerCheckMessage8"             , "線がない\n"                                                 } ,
    { "sudoku.AnswerCheckMessage1"                  , "間違いがある\n"                                             } ,
    { "sudoku.AnswerCheckMessage2"                  , "未完成\n"                                                   } ,
    { "tentaisho.AnswerCheckMessage1"               , "正しくない領域がある\n"                                     } ,
    { "tentaisho.AnswerCheckMessage2"               , "未完成\n"                                                   } ,
    { "yajilin.AnswerCheckMessage1"                 , "線が分岐または交差している\n"                               } ,
    { "yajilin.AnswerCheckMessage2"                 , "閉じていない線がある\n"                                     } ,
    { "yajilin.AnswerCheckMessage3"                 , "複数の線がある\n"                                           } ,
    { "yajilin.AnswerCheckMessage4"                 , "線の通っていないマスがある\n"                               } ,
    { "yajilin.AnswerCheckMessage5"                 , "黒マスの数が数字と一致していない矢印がある\n"               } ,
    { "yajilin.AnswerCheckMessage6"                 , "連続する黒マスがある\n"                                     } ,
    { "yajilin.AnswerCheckMessage7"                 , "線と黒マスが重なっているマスがある\n"                       } ,
    { "BoardBase.MessageComplete"                   , "正解です"                                                   } ,
    { "BoardBase.MessageUnavailable"                , "判定機能がありません"                                       } ,

    };
}
