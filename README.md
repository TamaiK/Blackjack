## ブラックジャック
ディーラー（コンピュータ）とブラックジャックで勝負

## 概要
開始時、プレイヤーとディーラー双方に2枚のカードを配る

※スート、枚数上限（一山なら同一の数字は4枚）は考慮しないものとする

「A」は「1または11」、「J,Q,K」は「10」として扱う

カードの合計が「21」に近い方が勝ち

プレイヤーは追加でカードを引く事が可能。

ただし、「21」を超えた場合、バスト（確定負け）となる

ディーラー（コンピュータ）は「17」を超えるまでカードを引く

## 開始
開始とともに、プレイヤー、ディーラーに2枚ずつカードを配る

``` console
あなたに「8」が配られました。
ディーラーに「A」が配られました。
あなたに「2」が配られました。
ディーラーに「5」が配られました。

```

## 入力
現在の互いの手札の合計値が表示され、カードを引くか選ぶ

``` console
ディーラーの合計は 16(A,5) です。
あなたの合計は 10(2,8) です。
もう１枚カードを引きますか？(Y/N)：
```

### 入力チェック
YかN以外を入力した場合は再入力を求める

例：数字
``` console
ディーラーの合計は 16(A,5) です。
あなたの合計は 10(2,8) です。
もう１枚カードを引きますか？(Y/N)：2
注意：Y/Nで入力してください。

もう１枚カードを引きますか？(Y/N)：
```

## もう1枚引く場合
カードを1枚引き、更に引くか選ぶ

``` console
もう１枚カードを引きますか？(Y/N)：Y

あなたに「9」が配られました。
ディーラーの合計は 16(A,5) です。
あなたの合計は 19(2,8,9) です。
もう１枚カードを引きますか？(Y/N)：
```

### 「21」になった場合
その旨を表示

これ以上引く必要はないので手番は終了します

``` console
もう１枚カードを引きますか？(Y/N)：Y

あなたに「2」が配られました。
ディーラーの合計は 16(A,5) です。
あなたの合計は 21(2,8,9,2) です。
21になりました！
```


### バストした場合
その旨を表示

強制的に手番は終了します

``` console
もう１枚カードを引きますか？(Y/N)：Y

あなたに「Q」が配られました。
ディーラーの合計は 16(A,5) です。
あなたの合計は 29(2,8,9,Q) です。
あなたはバストしました……
```

## 引くのを止めた場合
ディーラーに手番が移り、「17」を超えるまでカードを引く

``` console
もう１枚カードを引きますか？(Y/N)：N

ディーラーに「J」が配られました。
ディーラーの合計は 16(A,5,J) です。

ディーラーに「3」が配られました。
ディーラーの合計は 19(A,5,J,3) です。
```

### ディーラーのバスト
ディーラーもバストする場合があります

``` console
もう１枚カードを引きますか？(Y/N)：N

ディーラーに「J」が配られました。
ディーラーの合計は 16(A,5,J) です。

ディーラーに「K」が配られました。
ディーラーの合計は 26(A,5,J,K) です。
ディーラーはバストしました……
```

## 最終結果
互いの結果を比較して勝敗を出す

例：勝ち
``` console
最終結果
あなたの合計は 19(2,8,9) です。
ディーラーの合計は 26(A,5,J,K) です。

あなたの勝ちです！
```

例：負け
``` console
最終結果
あなたの合計は 29(2,8,9,Q) です。
ディーラーの合計は 19(A,5,J,3) です。

あなたの負けです……
```

例：引き分け
``` console
最終結果
あなたの合計は 19(2,8,9) です。
ディーラーの合計は 19(A,5,J,3) です。

引き分けです。
```

## 実行例

``` console
あなたに「8」が配られました。
ディーラーに「A」が配られました。
あなたに「2」が配られました。
ディーラーに「5」が配られました。

ディーラーの合計は 16(A,5) です。
あなたの合計は 10(2,8) です。
もう１枚カードを引きますか？(Y/N)：Y

あなたに「9」が配られました。
ディーラーの合計は 16(A,5) です。
あなたの合計は 19(2,8,9) です。
もう１枚カードを引きますか？(Y/N)：N

ディーラーに「J」が配られました。
ディーラーの合計は 16(A,5,J) です。

ディーラーに「3」が配られました。
ディーラーの合計は 19(A,5,J,3) です。

あなたの合計は 19(2,8,9) です。
ディーラーの合計は 19(A,5,J,3) です。

引き分けです。
```

## チャレンジ対応

## ブラックジャック判定
初手2枚で21達成時（A＋10～Kのどれか）の場合、

「ブラックジャック」として、普通の21より強い扱いにする

``` console
あなたに「10」が配られました。
ディーラーに「A」が配られました。
あなたに「A」が配られました。
ディーラーに「5」が配られました。

ディーラーの合計は 16(A,5) です。
あなたの合計は 21(10,A) です。
ブラックジャック！
```

## コインを賭ける
手持ちのコインを設定する

```
・最初の手持ちは100コイン
・1プレイ10コイン
・勝てば倍にして返ってくる（+20コイン）
・負ければ没収（+0コイン）
・引き分けはそのまま返却（+10コイン）
・「ブラックジャック」で勝利時は3倍になる（+30コイン）
・手持ちが0コインになると終了
```

### 表示

### 開始時
コインについての説明を追加

``` console
最初の手持ちは100コインです。
1プレイ10コインです。
コインが無くなったら終了です。

```

### ブラックジャック開始時
10コイン消費する旨を表示

``` console
ベット
-10コイン
```

### 最終結果表示時
返金表示と現在の手持ちを表示

例：勝ち
``` console
最終結果
あなたの合計は 19(2,8,9) です。
ディーラーの合計は 26(A,5,J,K) です。

あなたの勝ちです！
+20コイン

現在の手持ちは 110コイン です。
```

例：負け
``` console
最終結果
あなたの合計は 29(2,8,9,Q) です。
ディーラーの合計は 19(A,5,J,3) です。

あなたの負けです……
+0コイン

現在の手持ちは 90コイン です。
```

### ブラックジャック終了時に0コイン
その旨を表示しゲームを終了する

``` console
あなたの負けです……
+0コイン

現在の手持ちは 0コイン です。
手持ちのコインが無くなりました……
```
