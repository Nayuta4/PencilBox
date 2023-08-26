# PencilBox

## Overview

PencilBox is a pencil puzzle editor.

It supports the following 22 types of pencil puzzles of “Puzzle Communication Nikoli".

-  Kakuro (カックロ)
-  Kurodoko (黒どこ)
-  Goishi Hiroi (碁石ひろい)
-  Satogaeri (さとがえり)
-  Shakashaka (シャカシャカ)
-  Shikaku (四角に切れ)
-  Sudoku (数独)
-  Surarom (スラローム)
-  Slitherlink (スリザーリンク)
-  Tentai Show (天体ショー)
-  Numberlink (ナンバーリンク)
-  Nurikabe (ぬりかべ)
-  Norinori (のりのり)
-  Ripple Effect (波及効果)
-  Hashiwokakero (橋をかけろ)
-  Akari (美術館)
-  Hitori (ひとりにしてくれ)
-  Fillomino (フィルオミノ)
-  Heyawake (へやわけ)
-  Masyu (ましゅ)
-  Yajilin (ヤジリン)
-  LITS (LITS)

## Prerequisites

You need Java 5 or later to run PencilBox.

## Installation

Download `pencilbox-*.*.*.zip` from [Releases](https://github.com/Nayuta4/pencilbox/releases/latest) and extract it.

## Usage

The file `pencilbox.jar` contains common class files used by all puzzle types. Jar files of each puzzle type, such as `kakuro.jar`, contain class files specific to those types.

For example, to run the Kakuro program, use the command: `java -jar kakuro.jar`. Ensure that `pencilbox.jar` is in the same directory. Depending on your environment, you can also execute the program by double-clicking the jar file's icon.

Using `java -jar pencilbox.jar` will display a panel with buttons representing different puzzle types. Select the button corresponding to the puzzle you want to play. 

For detailed instructions on how to use PencilBox, pleas refer to [`pencilbox.html`](https://nayuta4.github.io/PencilBox/pencilbox.html).

## License

[GNU General Public License](doc/LICENSE.txt) 

## Old versions

Old versions of PnecilBox were released on OSDN.
-   [PencilBox - OSDN](https://osdn.net/projects/pencilbox/)
