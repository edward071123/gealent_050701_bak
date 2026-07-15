package senior.borrowGiveBackSystem.librarySystem;

import java.util.ArrayList;
import java.util.List;

import senior.borrowGiveBackSystem.main.IBorrowGiveBack;

/*
    models:         db的table     => Book.java
    repostories :   操作資料庫     => BookRepository.java
    services:       共同邏輯(共用的功能)       => LibraryService.java
    只負責借書/還書規則
*/



public class LibraryService implements IBorrowGiveBack {
    private BookRepository repository;
    private List<Book> books;

    public LibraryService(String dataFilePath) throws Exception {
        this.repository = new BookRepository(dataFilePath);
        // 讀取全部的書
        books = repository.loadBooks();
    }

    // 查看所有的書
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    // 借
    @Override
    public String borrow(int number, String borrowUser) throws Exception {
        // 1. 檢查傳入的資料
        if (borrowUser.isEmpty()) {
            throw new Exception("借閱人姓名不能為空");
        }

        if (borrowUser.equals("NULL")) {
            throw new Exception("借閱人姓名不能為NULL");
        }
        // 2. 根據number 須取得"目標書"的物件 => 檢查是否已被借走
        Book targetBook = checkBook(number);  // SELECT * FROM books WHERE id = number;

        // 3. 有被借走的話要回傳: 已被xxx借走
        if (!targetBook.isAvailable()) {
            return "編號: " + number + ",書名: " + targetBook.getTitle() + " 已被" + targetBook.getBorrowUser() + "借走";
        }

        // UPDATE books SET column1 = value1, column2 = value2, ...WHERE id = number;
        // 4. 沒被借走的話更改資料狀態(available:false, borrowUser)且回傳: 借閱 xx編號 xx書 成功
        targetBook.setAvailable(false);
        targetBook.setBorrowUser(borrowUser);
        repository.saveBooks(books);

        return borrowUser + " 借閱 編號: " + number + ",書名: " + targetBook.getTitle() + " 成功";
    }

    // 還
    @Override
    public String giveBack(int number) throws Exception {
        // 1. 根據number 須取得"目標書"的物件
        Book targetBook = checkBook(number);
        // 2. 檢查是否已被借走, 沒有被借走要回傳: 此書並無借出
        if (targetBook.isAvailable()) {
            return "編號: " + number + ",書名: " + targetBook.getTitle() + "並無借出";
        }

        // 3. 有被借出的話 則要更改借出的狀態available:true, 並且把borrowUser 改成NULL
        targetBook.setAvailable(true);
        targetBook.setBorrowUser("NULL");
        repository.saveBooks(books);

        return "編號: " + number + ",書名: " + targetBook.getTitle() + " 歸還成功";
    }

    private Book checkBook(int number) throws Exception {
        Book targetBook = null;

        for (Book oneBook : this.books) {
            // 把所有書每本取出比對number 得到該目標書籍 就break
            int oneBookNumber = Integer.parseInt(oneBook.getNumber());
            if (oneBookNumber == number) {
                targetBook = oneBook;
                break;
            }
        }

        return targetBook;
    }
}
