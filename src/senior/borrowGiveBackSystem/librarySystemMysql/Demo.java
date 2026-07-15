package senior.borrowGiveBackSystem.librarySystemMysql;

import java.util.List;
import java.util.Scanner;

import senior.borrowGiveBackSystem.librarySystemMysql.models.Book;

public class Demo {
    public static void main(String[] args) {
        /*
            librarySystem
                ├── main (主要功能制定)
                    ├── IBorrow.java
                ├── db (資料庫)
                    ├── books.txt
                ├── librarySystem (借還書系統功能實作)
                    ├── Demo.java (畫面)
                    ├── Book.java (把書物件化)
                    ├── BookRepository.java (針對資料庫的操作行為)
                    └── LibraryService.java (商業邏輯的內容)
        */
        
        boolean working = true; // true 可以一直在系統內運行, false 代表離開系統

        LibraryService service = null;
        try {
            service = new LibraryService();
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
