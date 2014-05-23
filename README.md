Play Framework 2 Meetup発表資料
=====================================

Play Framework 2 Meetup
http://connpass.com/event/6020/

にて発表した際に使用したライブコーディングのためのプログラムです。

発表スライドは、
http://www.slideshare.net/yoshiterutakeshita3/dslplay
にアップロードされています。

## これは何？

Twitterクローンを作るためのひな形です。
DSL+コードジェネレーションによる開発のすばらしさを伝えることを目的としています。

------

## 初回起動

### 1.設定ファイル

server/conf/application.conf.sampleを
server/conf/application.confにコピーして、DBの設定を適切に書き換えてください。

### 2.DBの作成

ローカルのMySQLに、"dslgen"という名前でdatabaseを作成しておいてください。

### 3.sbt

sbtを準備してください

### 4.起動

コンソールから

    sbt run

を実行してください。


------

## コードジェネレーション

DSLが、
code-gen/dsl
に入っているので、まずこのファイルに新しい定義を追加してください。
定義が追加できたら、

    sbt generateCode

を実行します。
実行が完了すると、各種ファイルが生成されます。
あとは、XXXService.scalaファイルを実装すれば、実装完了です。
