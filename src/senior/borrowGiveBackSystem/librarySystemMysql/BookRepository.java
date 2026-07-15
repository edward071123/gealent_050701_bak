package senior.borrowGiveBackSystem.librarySystemMysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import senior.borrowGiveBackSystem.librarySystemMysql.models.Book;
import senior.borrowGiveBackSystem.librarySystemMysql.models.Category;
import senior.borrowGiveBackSystem.librarySystemMysql.models.Item;

public class BookRepository {

    // 從 MySQL 查詢所有書籍資料
    //
    // 使用三段式 SQL：
    // 1. 先查 books 資料表
    // 2. 再用 category_id 查 categories 資料表
    // 3. 最後用 item_id 和 category_id 查 items 資料表
    public List<Book> loadBooks() throws Exception {
        List<Book> books = new ArrayList<>();

        String bookSql = "SELECT id, title, author, available, borrow_user, category_id, item_id FROM books ORDER BY id";
        String categorySql = "SELECT code, name FROM categories WHERE id = ?";
        String itemSql = "SELECT name FROM items WHERE id = ? AND category_id = ?";

        try (
                // 建立 MySQL 連線，連線資訊放在同一個 package 的 DBConnection.java
                Connection connection = new DBConnection().getConnection();

                PreparedStatement bookPs = connection.prepareStatement(bookSql);
                PreparedStatement categoryPs = connection.prepareStatement(categorySql);
                PreparedStatement itemPs = connection.prepareStatement(itemSql);

                // executeQuery() 會執行 SELECT，查詢結果會放在 ResultSet 裡
                ResultSet bookRs = bookPs.executeQuery();
            ) {

            // bookRs.next() 會一筆一筆往下讀 books 的查詢結果
            while (bookRs.next()) {
                int categoryId = bookRs.getInt("category_id");
                int itemId = bookRs.getInt("item_id");
                Category category = findCategory(categoryPs, categoryId);
                Item item = findItem(itemPs, itemId, categoryId);

                books.add(toBook(bookRs, category, item));
            }
        } catch (Exception e) {
            throw new Exception("查詢 books 資料表失敗: " + e.getMessage());
        }

        return books;
    }

    // 將指定書籍的借閱狀態寫回 MySQL
    //
    // 借書或還書時，LibraryService 會先修改記憶體中的 Book 物件：
    // - available：true 代表可借，false 代表已借出
    // - borrow_user：借閱人姓名，沒有人借時使用 "NULL"
    //
    // 這個方法會把修改後的狀態更新回 books 資料表。
    public void saveBook(Book book) throws Exception {
        // 只更新借還書會改變的欄位，不更新 title、author、category_id、item_id
        String sql = "UPDATE books SET available = ?, borrow_user = ? WHERE id = ?";

        try (
                Connection connection = new DBConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
            ) {
            // 第 1 個 ? 對應 available 欄位
            ps.setBoolean(1, book.isAvailable());

            // 第 2 個 ? 對應 borrow_user 欄位
            ps.setString(2, book.getBorrowUser());

            // 第 3 個 ? 對應 WHERE id = ?，指定要更新哪一本書
            ps.setInt(3, Integer.parseInt(book.getNumber()));

            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("更新 books 資料表失敗: " + e.getMessage());
        }
    }

    private Category findCategory(PreparedStatement ps, int categoryId) throws Exception {
        ps.setInt(1, categoryId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new Category(categoryId, rs.getString("code"), rs.getString("name"));
            }
        }

        return new Category(categoryId, "", "");
    }

    private Item findItem(PreparedStatement ps, int itemId, int categoryId) throws Exception {
        ps.setInt(1, itemId);
        ps.setInt(2, categoryId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new Item(itemId, categoryId, rs.getString("name"));
            }
        }

        return new Item(itemId, categoryId, "");
    }

    // 把資料庫查到的一筆書籍資料轉成 Java 物件
    private Book toBook(ResultSet rs, Category category, Item item) throws Exception {
        // books.id 是 int，原本 Book 類別的 number 是 String，所以這裡轉成字串
        String number = String.valueOf(rs.getInt("id"));
        String title = rs.getString("title");
        String author = rs.getString("author");
        boolean available = rs.getBoolean("available");
        String borrowUser = rs.getString("borrow_user");

        // 如果資料庫 borrow_user 是 null 或空字串，統一轉成原本系統使用的 "NULL"
        if (borrowUser == null || borrowUser.isEmpty()) {
            borrowUser = "NULL";
        }

        return new Book(number, title, author, available, borrowUser,
                category.getCode(), category.getName(), item.getName());
    }

}
