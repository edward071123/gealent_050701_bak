package senior.borrowGiveBackSystem.librarySystem;

import java.util.List;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        // 1.
        // 把提款機的部分 搬過來 改寫成
        // ===== 校園圖書館系統 =====
        // 1. 查看所有書籍
        // 2. 借書
        // 3. 還書
        // 0. 離開

        // 2.
        // 建立db/book.txt 內容如下面四行
        // 大分類,編號,書名,作者,是否已被借,小分類,被誰借走
        // P,1,Java入門,張三,false,Java,aa
        // P,2,Python程式設計,王五,true,Python,NULL
        // N,3,哈利波特,J.K.Rowling,true,奇幻,NULL
        // N,4,達文西密碼,丹布朗,true,歷史,NULL

        // 3. 為了建立一個書的物件(object), 所以必須建立以下類別
        // 建立Book.java 有以下屬性, 且有getter, setter
        // String type(種類) => 預設B, getType() 方法 => 回傳B
        // int number (編號)
        // String title (書名)
        // String author (作者)
        // boolean available (是否可借)
        // String borrowUser (誰借走)

        // 建立建構子傳入以上屬性的資料進來

        // 4.
        // 建立ProgrammingBook.java(程式類別的書) 繼承Book.java 且有以下屬性
        // String language (程式語言)

        // 覆寫 getType() 方法 => 回傳P
        // 建立建構子傳入以上屬性的資料進來

        // 5.
        // 建立NovelBook.java(小說類別的書) 繼承Book.java 且有以下屬性
        // String category (類別)

        // 覆寫 getType() 方法 => 回傳N
        // 建立建構子傳入以上屬性的資料進來

        // 6. 把  (想像一下 Demo.java的 main(程式進入點是銀行大廳), 要引導到LibraryService.java這棟大樓的, 三個小房間為窗口)
        // (1). 查看所有書籍 getAllBooks()
        // (2). 借書 borrow()
        // (3). 還書 giveBack()
        // 獨立為一個類別(LibraryService.java)的三個方法去讓main 物件化後呼叫

        // 7. 
        // 借的流程
        // (1). 檢查傳入的borrowUser資料是不是為空
        // (2). 取得目標書籍
        // (3). 檢查是否被借走, 如果已被借走 => 回傳已被借走, 目前無法被借
        // (4). 沒被借 => 更改資料庫狀態(寫入資料庫)

        // 8. 
        // 還的流程
        // (1). 取得目標書籍
        // (2). 檢查是否被借走, 如果沒被借走 => 回傳沒被借走, 所以無需改狀態
        // (3). 已被借 => 更改資料庫狀態(寫入資料庫)

        // 9. 
        // 寫出以下針對資料庫的操作(BookRepository.java)
        // 查詢所有書 => 讀取
        // 借書,還書 => 寫入

        /*
            librarySystem
                ├── main (主要功能制定)
                    ├── IBorrow.java
                ├── db (資料庫)
                    ├── books.txt
                ├── librarySystem (借還書系統功能實作)
                    ├── Demo.java (畫面)
                    ├── Book.java (把書物件化)
                    ├── ProgrammingBook.java (把書物件化)
                    ├── NovelBook.java (把書物件化)
                    ├── BookRepository.java (針對資料庫的操作行為)
                    └── LibraryService.java (商業邏輯的內容)
        */

        // 1. Demo.java => new LibraryService() 
        
        // 2. LibraryService.java => public LibraryService(String dataFilePath) 
  
        // 3. BookRepository.java => loadBooks(); => 讀取全部的書
        
        boolean working = true; // true 可以一直在系統內運行, false 代表離開系統

        String dataFilePath = "src/senior/borrowGiveBackSystem/db/book.txt";
        LibraryService service = null;
        try {
            service = new LibraryService(dataFilePath);
        } catch (Exception e) {
            working = false;
            System.out.println(e.getMessage());
        }

        Scanner sc = new Scanner(System.in);

        while (working == true) {
            System.out.println("===== 校園圖書館系統 =====");
            System.out.println("1. 查看所有書籍");
            System.out.println("2. 借書");
            System.out.println("3. 還書");
            System.out.println("0. 離開");
            System.out.print("請輸入操作數字：");

            String choice = sc.nextLine();

            try {
                if (choice.equals("1")) {
                    // 查看所有書籍
                    printBookInfo(service.getAllBooks());

                } else if (choice.equals("2")) {
                    // 借書
                    System.out.print("請輸入借閱人姓名：");
                    String name = sc.nextLine();

                    System.out.print("請輸入書籍編號(數字)：");
                    int number = Integer.parseInt(sc.nextLine());

                    System.out.println(service.borrow(number, name));

                } else if (choice.equals("3")) {
                    // 還書
                    System.out.print("請輸入書籍編號(數字)：");
                    int number = Integer.parseInt(sc.nextLine());
                    System.out.println(service.giveBack(number));

                } else if (choice.equals("0")) {
                    System.out.println("===== 離開 =====");
                    working = false;

                } else {
                    System.out.println("===== 輸入錯誤 =====");

                }
            } catch (Exception e) {
                System.out.println("");
                System.out.println("=====操作錯誤====");
                System.out.println(e.getMessage());
            }

        }

        sc.close();

        // String dataFilePath = "src/senior/borrowGiveBackSystem/db/book.txt";
        // try {
        // BookRepository b1 = new BookRepository(dataFilePath);
        // List<Book> books = b1.loadBooks();
        // System.out.println(books.get(1).getTitle());

        // } catch (Exception e) {
        // System.out.println(e.getMessage());
        // }

    }

    public static void printBookInfo(List<Book> books) {
        System.out.println("===== 書籍列表起始 =====");

        for (int i = 0; i < books.size(); i++) {
            System.out.println();
            System.out.println(books.get(i).getInfo());
        }

        System.out.println("===== 書籍列表結束 =====");
    }
}
