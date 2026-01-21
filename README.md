# Karaden Javaライブラリ
Karaden Javaライブラリは、Javaで書かれたアプリケーションからKaraden APIへ簡単にアクセスするための手段を提供します。<br />
それにはAPIレスポンスから動的に初期化するAPIリソースの一連のクラス定義が含まれているため、Karaden APIの幅広いバージョンと互換性があります。
## インストール方法
パッケージを変更しないならば、このソースコードは必要ありません。
### Gradle
あなたのプロジェクトのビルドファイルに下記の依存を追加します。
```groovy
implementation 'jp.karaden:karaden-prg-java:1.2.1'
```
### Maven
あなたのプロジェクトのPOMファイルに下記の依存を追加します。
```xml
<dependency>
  <groupId>jp.karaden</groupId>
  <artifactId>karaden-prg-java</artifactId>
  <version>1.2.1</version>
</dependency>
```
## 動作環境
Java 8～
## 使い方
このライブラリを使用するには、Karadenでテナントを作成し、プロジェクト毎に発行できるトークンを発行する必要があります。<br />
作成したテナントID（テナントIDはテナント選択画面で表示されています）は、`Config.tenantId`に、発行したトークンは`Config.apiKey`にそれぞれ設定します。
```java
import jp.karaden.Config;
import jp.karaden.exception.KaradenException;
import jp.karaden.model.Message;
import jp.karaden.param.message.MessageCreateParams;

public class KaradenExample {
    public static void main(String[] args) {
        Config.apiKey = "<トークン>";
        Config.tenantId = "<テナントID>";

        MessageCreateParams params = MessageCreateParams.newBuilder()
            .withServiceId(1)
            .withTo("09012345678")
            .withBody("本文")
            .build();

        try {
            Message message = Message.create(params);
            System.out.println(message);
        } catch (KaradenException e) {
            e.printStackTrace();
        }
    }
}
```
### リクエスト毎の設定
同一のプロセスで複数のキーを使用する必要がある場合、リクエスト毎にキーやテナントIDを設定することができます。
```java
MessageDetailParams params = MessageDetailParams.newBuilder()
    .withId("<メッセージID>")
    .build();
RequestOptions requestOptions = RequestOptions.newBuilder()
    .withApiKey("<トークン>")
    .withTenantId("<テナントID>")
    .build();
Message message = Message.detail(params, requestOptions);
```
### タイムアウトについて
通信をするファイルサイズや実行環境の通信速度によってはHTTP通信時にタイムアウトが発生する可能性があります。<br />
何度も同じような現象が起こる際は、ファイルサイズの調整もしくは`RequestOptions`からタイムアウトの時間を増やして、再度実行してください。<br />
```java
RequestOptions requestOptions = RequestOptions.newBuilder()
    .withApiKey("<トークン>")
    .withTenantId("<テナントID>")
    .withConnectionTimeout(<ミリ秒>)
    .withReadTimeout(<ミリ秒>)
    .build();
BulkMessage bulkMessage = BulkMessageService.create("<ファイルパス>", requestOptions);
```
