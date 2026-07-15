## 開始使用

歡迎來到 VS Code 的 Java 開發環境。以下內容可協助你在 Visual Studio Code 中開始撰寫 Java 程式。

## 資料夾結構

此工作區預設包含兩個資料夾，分別是：

- `src`：用來存放原始碼的資料夾
- `lib`：用來存放相依套件的資料夾

另外，編譯後產生的輸出檔案預設會放在 `bin` 資料夾中。

> 如果你想自訂資料夾結構，請開啟 `.vscode/settings.json`，並在其中更新相關設定。


## 編譯指令
```sh
javac -d bin \
    {檔案路徑}/*.java
```
## 範例編譯指令
```sh
javac -d bin \
    src/junior/abstractInfo/*.java
```

## 打包成jar指令
```sh
  jar --create \
    --file lib/{自訂檔案名稱}.jar \
    --main-class {package路徑}.Demo \
    -C {目標資料夾} .
```
## 範例打包成jar指令
```sh
  jar --create \
    --file lib/AnonymousInfo.jar \
    --main-class junior.anonymousInfo.Demo \
    -C bin .
```

## 可執行jar指令
```sh
java -jar lib/{目標檔案名稱}.jar
```
## 範例可執行jar指令
```sh
java -jar lib/AbstractDemo.jar
```