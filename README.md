# コミュニティーサイト

![Javaバージョン](https://img.shields.io/badge/JavaSE-ver.17-grean)
![SpringBoot Version](https://img.shields.io/badge/SpringBoot-ver.2.6.7-grean)
![ビルドツール](https://img.shields.io/badge/build-maven-grean)
![DB操作フレームワーク](https://img.shields.io/badge/DB-MyBatis-grean)


## ファイルの概要
com.example.community →　定数ファイルとMain  
com.example.community.auth →　ログイン関連  
com.example.community.controller →　コントロールによるアクセスわけ  
com.example.community.dto →　要素の作成時を保持するためのクラス  
com.example.community.entity →　要素を取得するためのクラス  
com.example.community.repository →　DBアクセス, SQL文はresources.同階層にXMLで記載  
com.example.community.service →　ビジネスロジック  
com.example.community.controller →　コントローラーによる処理分け  
com.example.community.dto →　要素の作成時に値を保持するためのクラス  
com.example.community.entity →　要素を取得するために使うクラス  
com.example.community.repository →　DBアクセス層, SQL文はresources.同階層にXMLで記載  
com.example.community.service →　ビジネスロジック層  

## 機能

**実装済み** 
* 新規登録機能
* メール確認
* ログイン機能
* 部屋作成
* 部屋編集
* 部屋削除
* 部屋CRUDのバリデーション
* トーク機能
* 部屋の自由参加と承認制での処理分け
* 検索機能
* マイページ
* Good,Bad機能
* ページャー

**未実装** 
* ログイン機能とアクセス制御
* 画像、動画投稿
* フレンド機能
* 部屋招待機能
* デザイン
* レスポンシブ対応
* JSでバリデーション
* トークの自動更新と非同期投稿
