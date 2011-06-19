package pencilbox.resource;

import java.util.ListResourceBundle;

public class ResourceBundle extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {

    { "PencilBoxLauncher.quit"                      , "Quit"                                                       } ,
    { "PencilType.bijutsukan"                       , "Akari"                                                      } ,
    { "PencilType.fillomino"                        , "Fillomino"                                                  } ,
    { "PencilType.goishi"                           , "Goishi Hiroi"                                               } ,
    { "PencilType.hakyukoka"                        , "Ripple Effect"                                              } ,
    { "PencilType.hashi"                            , "Hashiwokakero"                                              } ,
    { "PencilType.heyawake"                         , "Heyawake"                                                   } ,
    { "PencilType.hitori"                           , "Hitori"                                                     } ,
    { "PencilType.kakuro"                           , "Kakuro"                                                     } ,
    { "PencilType.kurodoko"                         , "Kurodoko"                                                   } ,
    { "PencilType.lits"                             , "LITS"                                                       } ,
    { "PencilType.masyu"                            , "Masyu"                                                      } ,
    { "PencilType.norinori"                         , "Norinori"                                                   } ,
    { "PencilType.numberlink"                       , "Number Link"                                                } ,
    { "PencilType.nurikabe"                         , "Nurikabe"                                                   } ,
    { "PencilType.shikaku"                          , "Shikaku"                                                    } ,
    { "PencilType.shakashaka"                       , "Shakashaka"                                                 } ,
    { "PencilType.slalom"                           , "Suraromu"                                                   } ,
    { "PencilType.slitherlink"                      , "Slitherlink"                                                } ,
    { "PencilType.sudoku"                           , "Sudoku"                                                     } ,
    { "PencilType.tentaisho"                        , "Tentai Show"                                                } ,
    { "PencilType.yajilin"                          , "Yajilin"                                                    } ,

    { "Problem.Untitled"                            , "Untitled"                                                   } ,

    { "MenuBase.fileMenu"                           , "File"                                                       } ,
    { "MenuBase.newBoardItem"                       , "New..."                                                     } ,
    { "MenuBase.openItem"                           , "Open..."                                                    } ,
    { "MenuBase.closeAndOpenItem"                   , "Close and Open..."                                          } ,
    { "MenuBase.saveItem"                           , "Save As..."                                                 } ,
    { "MenuBase.duplicateItem"                      , "Duplicate"                                                  } ,
    { "MenuBase.rotationItem"                       , "Rotation"                                                   } ,
    { "MenuBase.changeBoardSizeItem"                , "Change Board Size"                                          } ,
    { "MenuBase.exportMenu"                         , "Export Problem Data"                                        } ,
    { "MenuBase.saveImageItem"                      , "Save Image..."                                              } ,
    { "MenuBase.copyImageItem"                      , "Copy Image"                                                 } ,
    { "MenuBase.printPreviewItem"                   , "Print Preview..."                                           } ,
    { "MenuBase.printItem"                          , "Print..."                                                   } ,
    { "MenuBase.loadPreferencesItem"                , "Load Preferences..."                                        } ,
    { "MenuBase.storePreferencesItem"               , "Store Preferences..."                                       } ,
    { "MenuBase.propertyItem"                       , "Properties..."                                              } ,
    { "MenuBase.closeItem"                          , "Close"                                                      } ,
    { "MenuBase.quitItem"                           , "Quit"                                                       } ,
    { "MenuBase.editMenu"                           , "Edit"                                                       } ,
    { "MenuBase.answerModeItem"                     , "Answer Mode"                                                } ,
    { "MenuBase.problemEditModeItem"                , "Edit Mode"                                                  } ,
    { "MenuBase.regionEditModeItem"                 , "Region Edit Mode"                                           } ,
    { "MenuBase.clearItem"                          , "Clear Answer"                                               } ,
    { "MenuBase.trimAnswerItem"                     , "Trim Answer"                                                } ,
    { "MenuBase.exchangeNumbersItem"                , "Exchange Nubmers"                                           } ,
    { "MenuBase.symmetricPlacementItem"             , "Symmetric Placement"                                        } ,
    { "MenuBase.undoItem"                           , "Undo"                                                       } ,
    { "MenuBase.redoItem"                           , "Redo"                                                       } ,
    { "MenuBase.playbackItem"                       , "Playback"                                                   } ,
    { "MenuBase.historyItem"                        , "History..."                                                 } ,
    { "MenuBase.checkAnswerItem"                    , "Check Answer"                                               } ,
    { "MenuBase.immediateAnswerCheckItem"           , "Immediate Answer Check"                                     } ,
    { "MenuBase.viewMenu"                           , "View"                                                       } ,
    { "MenuBase.colorMenu"                          , "Set Colors"                                                 } ,
    { "MenuBase.backgroundColorItem"                , "Background"                                                 } ,
    { "MenuBase.gridColorItem"                      , "Grid"                                                       } ,
    { "MenuBase.renewColorItem"                     , "Renew Color"                                                } ,
    { "MenuBase.cellSizeItem"                       , "Cell Size..."                                               } ,
    { "MenuBase.indexItem"                          , "Show Index"                                                 } ,
    { "MenuBase.gridStyleItem"                      , "Show Grid"                                                  } ,
    { "MenuBase.cursorItem"                         , "Cursor"                                                     } ,
    { "MenuBase.markStyleMenu"                      , "No Paint Mark"                                              } ,
    { "MenuBase.linkWidthItem"                      , "Link Width"                                                 } ,
    { "MenuBase.helpMenu"                           , "Help"                                                       } ,
    { "MenuBase.aboutDialog"                        , "About PencilBox"                                            } ,

    { "MenuBase.rotationItem1"                      , "Rotate 90 Degrees to Left (1)"                              } ,
    { "MenuBase.rotationItem2"                      , "Rotate 180 Degrees (2)"                                     } ,
    { "MenuBase.rotationItem3"                      , "Rotate 90 Degrees to Right (3)"                             } ,
    { "MenuBase.rotationItem4"                      , "Exchange Horizontal and Vertical (4)"                       } ,
    { "MenuBase.rotationItem5"                      , "Reverse Left and Right (5)"                                 } ,
    { "MenuBase.rotationItem6"                      , "Rotate 180 Degrees and Exchange Horizontal and Vertical (6)" } ,
    { "MenuBase.rotationItem7"                      , "Reverse Up and Down (7)"                                    } ,
    { "MenuBase.exportItemKanpen"                   , "Kanpen"                                                     } ,
    { "MenuBase.exportItemPzprv3"                   , "PUZ-PRE v3"                                                 } ,
    { "MenuBase.exportItemHeyawake"                 , "Heyawake Applet"                                            } ,
    { "MenuBase.markStyle0"                         , "Not Displayed"                                              } ,
    { "MenuBase.markStyle1"                         , "Circle"                                                     } ,
    { "MenuBase.markStyle2"                         , "Filled Circle"                                              } ,
    { "MenuBase.markStyle3"                         , "Square"                                                     } ,
    { "MenuBase.markStyle4"                         , "Cross"                                                      } ,
    { "MenuBase.markStyle5"                         , "Paint"                                                      } ,

    { "MenuCommand.newBoardDialog"                  , "New Board"                                                  } ,
    { "MenuCommand.rotateBoardDialog"               , "Rotation"                                                   } ,
    { "MenuCommand.changeBoardSizeDialog"           , "Change Board Size"                                          } ,
    { "MenuCommand.properyDialog"                   , "Properties"                                                 } ,
    { "MenuCommand.dataExportDialog"                , "Export Problem Data"                                        } ,
    { "MenuCommand.checkAnswerDialog"               , "Check Answer"                                               } ,
    { "MenuCommand.historyDialog"                   , "History"                                                    } ,
    { "MenuCommand.indexLettersDialog"              , "Show Coordinates"                                           } ,
    { "MenuCommand.cellSizeDialog"                  , "Cell Size"                                                  } ,
    { "MenuCommand.linkWidthDialog"                 , "Link Width"                                                 } ,
    { "MenuCommand.aboutDialog"                     , "About PencilBox"                                            } ,
    { "MenuCommand.Message_Error"                   , "Error"                                                      } ,
    { "MenuCommand.Message_InappropriateSize"       , "It is inappropriate size."                                  } ,
    { "MenuCommand.Message_TooLarge"                , "It is too large."                                           } ,

    { "PencilBoxDialog.buttonOK"                    , "OK"                                                         } ,
    { "PencilBoxDialog.buttonCancel"                , "Cancel"                                                     } ,
    { "NewBoardDialog.labelCols"                    , "Cols:"                                                      } ,
    { "NewBoardDialog.labelRows"                    , "Rows:"                                                      } ,
    { "NewBoardDialog.checkBoxSquare"               , "Square"                                                     } ,
    { "DataExportDialog.buttonOpen"                 , "Open in New Frame"                                          } ,
    { "DataExportDialog.buttonCopy"                 , "Copy to Clipboard"                                          } ,
    { "DataExportDialog.buttonCancel"               , "Close"                                                      } ,
    { "PropertyDialog.labelAuthor"                  , "Author"                                                     } ,
    { "PropertyDialog.labelDifficulty"              , "Difficulty"                                                 } ,
    { "PropertyDialog.labelSource"                  , "Source"                                                     } ,
    { "PanelEventHandlerBase.checkAnswerDialog"     , "Check Answer"                                               } ,
    { "IndexLettersDialog.checkBox"                 , "Show Index"                                                 } ,
    { "IndexLettersDialog.labelCols"                , "Cols:"                                                      } ,
    { "IndexLettersDialog.labelRows"                , "Rows:"                                                      } ,
    { "ExchangeNumbersDialog.title"                 , "Exchange Numbers"                                           } ,
    { "ExchangeNumbersDialog.label1"                , "A Number�F"                                                  } ,
    { "ExchangeNumbersDialog.label2"                , "Another Number:"                                            } ,
    { "ExchangeNumbersDialog.buttonUpdate"          , "Exchange"                                                   } ,

    { "Menu.countAreaSizeItem"                      , "Count Area Size"                                            } ,
    { "Menu.dotHintItem"                            , "Dot Hint"                                                   } ,
    { "Menu.hideSoleNumberItem"                     , "Hide Sole Figure"                                           } ,
    { "Menu.hideStarItem"                           , "Hide Star"                                                  } ,
    { "Menu.highlightSelectionItem"                 , "Highlight Selection"                                        } ,
    { "Menu.indicateCompletionItem"                 , "Indicate Completion"                                        } ,
    { "Menu.indicateErrorItem"                      , "Indicate Errors"                                            } ,
    { "Menu.noBulbStyleMenu"                        , "No Bulb Mark"                                               } ,
    { "Menu.paintIlluminatedCellItem"               , "Paint Illuminated Cell"                                     } ,
    { "Menu.SelectLetterDialog"                     , "Select Letters"                                             } ,
    { "Menu.selectLetterItem"                       , "Select Letters..."                                          } ,
    { "Menu.separateAreaColorItem"                  , "Separate Area Color"                                        } ,
    { "Menu.separateLinkColorItem"                  , "Separate Link Color"                                        } ,
    { "Menu.separateWallColorItem"                  , "Separate Link Color"                                        } ,
    { "Menu.separateTetrominoColorItem"             , "Separate Tetromino Color"                                   } ,
    { "Menu.showAreaBorderItem"                     , "Show Area Border"                                           } ,
    { "Menu.showBeamItem"                           , "Show Beam"                                                  } ,

    { "Menu.numberColorItem"                        , "Figure"                                                     } ,
    { "Menu.fixedNumberColorItem"                   , "Fixed Figure"                                               } ,
    { "Menu.inputColorItem"                         , "Answer Figure"                                              } ,
    { "Menu.wallColorItem"                          , "Black Cell"                                                 } ,
    { "Menu.paintColorItem"                         , "Paint"                                                      } ,
    { "Menu.noPaintColorItem"                       , "No Paint"                                                   } ,
    { "Menu.circleColorItem"                        , "Circle"                                                     } ,
    { "Menu.areaBorderColorItem"                    , "Area Border"                                                } ,
    { "Menu.areaPaintColorItem"                     , "Area Paint"                                                 } ,
    { "Menu.lineColorItem"                          , "Line"                                                       } ,
    { "Menu.crossColorItem"                         , "Cross"                                                      } ,
    { "Menu.borderColorItem"                        , "Border"                                                     } ,
    { "Menu.gateColorItem"                          , "Gate"                                                       } ,
    { "Menu.whiteAreaColorItem"                     , "White Star Area"                                            } ,
    { "Menu.blackAreaColorItem"                     , "Black Star Area"                                            } ,
    { "Menu.bulbColorItem"                          , "Bulb"                                                       } ,
    { "Menu.noBulbColorItem"                        , "No Bulb"                                                    } ,
    { "Menu.illuminatedCellColorItem"               , "Illuminated Cell"                                           } ,
    { "Menu.stoneOutlineColorItem"                  , "Stone Outline"                                              } ,
    { "Menu.stoneColorItem"                         , "Stone"                                                      } ,
    { "Menu.triangleColorItem"                      , "Triangle"                                                   } ,

    { "bijutsukan.AnswerCheckMessage1"              , "Two or more bulbs placed in a same line.\n"                 } ,
    { "bijutsukan.AnswerCheckMessage2"              , "There are cells not illuminated.\n"                         } ,
    { "bijutsukan.AnswerCheckMessage3"              , "There is a black cell with wrong number of bulbs.\n"        } ,
    { "fillomino.AnswerCheckMessage1"               , "There is a blank cell.\n"                                   } ,
    { "fillomino.AnswerCheckMessage2"               , "There is an area larger than the number.\n"                 } ,
    { "fillomino.AnswerCheckMessage3"               , "There is an area smaller than the number.\n"                } ,
    { "goishi.AnswerCheckMessage1"                  , "There are remained stones.\n"                               } ,
    { "goishi.AnswerCheckMessage2"                  , "There is no stones.\n"                                      } ,
    { "hakyukoka.AnswerCheckMessage1"               , "It is not complete.\n"                                      } ,
    { "hakyukoka.AnswerCheckMessage2"               , "There is an area that contains two same figures.\n"         } ,
    { "hakyukoka.AnswerCheckMessage3"               , "There is an area that contains too large figure.\n"         } ,
    { "hakyukoka.AnswerCheckMessage4"               , "There are figures with insufficient distance.\n"            } ,
    { "hashi.AnswerCheckMessage1"                   , "There is a line that is branched or crossed.\n"             } ,
    { "hashi.AnswerCheckMessage2"                   , "Not all figures area connected\n"                           } ,
    { "hashi.AnswerCheckMessage3"                   , "There is a figure with wrong number of lines.\n"            } ,
    { "heyawake.AnswerCheckMessage1"                , "There are consecutive black cells. \n"                      } ,
    { "heyawake.AnswerCheckMessage2"                , "The board is divided by painted cells.\n"                   } ,
    { "heyawake.AnswerCheckMessage3"                , "There is a room with wrong number of painted cells.\n"      } ,
    { "heyawake.AnswerCheckMessage4"                , "There are painted cells that continue to three rooms.\n"    } ,
    { "hitori.AnswerCheckMessage1"                  , "There are consecutive black cells. \n"                      } ,
    { "hitori.AnswerCheckMessage2"                  , "The board is divided by painted cells.\n"                   } ,
    { "hitori.AnswerCheckMessage3"                  , "Two unpainted figures exist in a same line.\n"              } ,
    { "kakuro.AnswerCheckMessage1"                  , "It is not complete.\n"                                      } ,
    { "kakuro.AnswerCheckMessage2"                  , "Two unpainted figures exist in a same line.\n"              } ,
    { "kakuro.AnswerCheckMessage3"                  , "There is a wrong sum.\n"                                    } ,
    { "kurodoko.AnswerCheckMessage1"                , "There are consecutive black cells. \n"                      } ,
    { "kurodoko.AnswerCheckMessage2"                , "The board is divided by painted cells.\n"                   } ,
    { "kurodoko.AnswerCheckMessage3"                , "There is a figure with too many unpainted cells.\n"         } ,
    { "kurodoko.AnswerCheckMessage4"                , "There is a figure with too few unpainted cells.\n"          } ,
    { "lits.AnswerCheckMessage1"                    , "There are no blocks.\n"                                     } ,
    { "lits.AnswerCheckMessage2"                    , "There is a block without a tetromino.\n"                    } ,
    { "lits.AnswerCheckMessage3"                    , "There are tetrominoes of same type that is adjacent.\n"     } ,
    { "lits.AnswerCheckMessage4"                    , "Black areas are divided.\n"                                 } ,
    { "lits.AnswerCheckMessage5"                    , "There are black cells that forms a 2x2 square.\n"           } ,
    { "masyu.AnswerCheckMessage1"                   , "There is a line that is branched or crossed.\n"             } ,
    { "masyu.AnswerCheckMessage2"                   , "There is a loop that is not closed.\n"                      } ,
    { "masyu.AnswerCheckMessage3"                   , "There is a white circle with wrong line.\n"                 } ,
    { "masyu.AnswerCheckMessage4"                   , "There is a black circle with wrong line.\n"                 } ,
    { "masyu.AnswerCheckMessage5"                   , "There are two or more lines. \n"                            } ,
    { "masyu.AnswerCheckMessage6"                   , "There is a white circle without a line.\n"                  } ,
    { "masyu.AnswerCheckMessage7"                   , "There is a black circle without a line.\n"                  } ,
    { "masyu.AnswerCheckMessage8"                   , "There is no loop.\n"                                        } ,
    { "norinori.AnswerCheckMessage1"                , "There are no blocks\n"                                      } ,
    { "norinori.AnswerCheckMessage2"                , "There is a block with wrong number of black cells.\n"       } ,
    { "norinori.AnswerCheckMessage3"                , "There is a connection of black cells that the size is not two.\n"   } ,
    { "numberlink.AnswerCheckMessage1"              , "There is a figure with two lines.\n"                        } ,
    { "numberlink.AnswerCheckMessage2"              , "There is a figure with no line.\n"                          } ,
    { "numberlink.AnswerCheckMessage3"              , "There is a line that is branched or crossed.\n"             } ,
    { "numberlink.AnswerCheckMessage4"              , "There is a line that cuts on the way. \n"                   } ,
    { "numberlink.AnswerCheckMessage5"              , "There is a line that connects different figures.\n"         } ,
    { "numberlink.AnswerCheckMessage6"              , "There is a line that is not connected to figures.\n"        } ,
    { "nurikabe.AnswerCheckMessage1"                , "There is a blank cell.\n"                                   } ,
    { "nurikabe.AnswerCheckMessage2"                , "There is an area with wrong number of cells.\n"             } ,
    { "nurikabe.AnswerCheckMessage4"                , "There is an area with two or more figures.\n"               } ,
    { "nurikabe.AnswerCheckMessage5"                , "There is an area with no figure.\n"                         } ,
    { "nurikabe.AnswerCheckMessage6"                , "Black areas are divided.\n"                                 } ,
    { "nurikabe.AnswerCheckMessage7"                , "There are black cells that forms a 2x2 square.\n"           } ,
    { "shakashaka.AnswerCheckMessage1"              , "There is a black cel with wrong number of triangles.\n"     } ,
    { "shakashaka.AnswerCheckMessage2"              , "There is an area which is not recutangular.\n"              } ,
    { "shikaku.AnswerCheckMessage1"                 , "There is an area with two or more figures.\n"               } ,
    { "shikaku.AnswerCheckMessage2"                 , "There is an area with no figure.\n"                         } ,
    { "shikaku.AnswerCheckMessage3"                 , "There is an area with too large size.\n"                    } ,
    { "shikaku.AnswerCheckMessage4"                 , "There is an area with too small size.\n"                    } ,
    { "shikaku.AnswerCheckMessage5"                 , "There is a cell that dose not contained by any areas.\n"    } ,
    { "shikaku.AnswerCheckMessage6"                 , "There is no figure on the board.\n"                         } ,
    { "slalom.AnswerCheckMessage1"                  , "There is a line that is branched or crossed.\n"             } ,
    { "slalom.AnswerCheckMessage2"                  , "There is a loop that is not closed.\n"                      } ,
    { "slalom.AnswerCheckMessage3"                  , "There are two or more lines. \n"                            } ,
    { "slalom.AnswerCheckMessage4"                  , "There is no loop.\n"                                        } ,
    { "slalom.AnswerCheckMessage5"                  , "There is a gate with wrong lines.\n"                        } ,
    { "slalom.AnswerCheckMessage6"                  , "There is a gate that the line does not cross.\n"            } ,
    { "slalom.AnswerCheckMessage7"                  , "There is a line that passes a gate two or more times.\n"    } ,
    { "slalom.AnswerCheckMessage8"                  , "A line passes gates by worng order.\n"                      } ,
    { "slalom.AnswerCheckMessage9"                  , "A line does not go through a circle.\n"                     } ,
    { "slitherlink.AnswerCheckMessage1"             , "There is a line that is branched or crossed.\n"             } ,
    { "slitherlink.AnswerCheckMessage2"             , "There is a loop that is not closed.\n"                      } ,
    { "slitherlink.AnswerCheckMessage3"             , "There is a figure with too many lines.\n"                   } ,
    { "slitherlink.AnswerCheckMessage5"             , "There are two or more lines. \n"                            } ,
    { "slitherlink.AnswerCheckMessage6"             , "There is a figure with too few lines.\n"                    } ,
    { "slitherlink.AnswerCheckMessage8"             , "There is no loop."                                          } ,
    { "sudoku.AnswerCheckMessage1"                  , "There is a mistake.\n"                                      } ,
    { "sudoku.AnswerCheckMessage2"                  , "It is not complete.\n"                                      } ,
    { "tentaisho.AnswerCheckMessage1"               , "There is a wrong area.\n"                                   } ,
    { "tentaisho.AnswerCheckMessage2"               , "It is not complete.\n"                                      } ,
    { "yajilin.AnswerCheckMessage1"                 , "There is a line that is branched or crossed.\n"             } ,
    { "yajilin.AnswerCheckMessage2"                 , "There is a loop that is not closed.\n"                      } ,
    { "yajilin.AnswerCheckMessage3"                 , "There are two or more lines. \n"                            } ,
    { "yajilin.AnswerCheckMessage4"                 , "There is a cell that is not painted and has no line.\n"     } ,
    { "yajilin.AnswerCheckMessage5"                 , "There is a figure with wrong number of painted cells.\n"    } ,
    { "yajilin.AnswerCheckMessage6"                 , "There are consecutive black cells. \n"                      } ,
    { "yajilin.AnswerCheckMessage7"                 , "There is a cell that is painted and has line.\n"            } ,
    { "BoardBase.MessageComplete"                   , "Correct answer."                                            } ,
    { "BoardBase.MessageUnavailable"                , "Answer check is not available."                             } ,

    };
}
