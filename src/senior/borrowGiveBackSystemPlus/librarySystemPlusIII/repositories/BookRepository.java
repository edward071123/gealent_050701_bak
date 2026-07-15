package senior.borrowGiveBackSystemPlus.librarySystemPlusIII.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import senior.borrowGiveBackSystemPlus.db.DBConnection;
import senior.borrowGiveBackSystemPlus.models.Book;
import senior.borrowGiveBackSystemPlus.models.Category;
import senior.borrowGiveBackSystemPlus.models.Item;

public class BookRepository extends BaseRepository {

    // 從 MySQL 查詢所有書籍資料
    //
    // 使用三段式 SQL：
    // 1. 先查 books 資料表
    // 2. 再用 category_id 查 categories 資料表
    // 3. 最後用 item_id 和 category_id 查 items 資料表
    public List<Book> loadBooks() throws Exception {
        List<Book> books = new ArrayList<>();

        String bookSql = "SELECT id, title, author, available, borrow_member_id, category_id, item_id FROM books ORDER BY id";
        String categorySql = "SELECT name FROM categories WHERE id = ?";
        String itemSql = "SELECT name FROM items WHERE id = ? AND category_id = ?";
        String memberSql = "SELECT account FROM members WHERE id = ?";

        try (
                Connection connection = new DBConnection().getConnection();
                PreparedStatement bookPs = connection.prepareStatement(bookSql);
                PreparedStatement categoryPs = connection.prepareStatement(categorySql);
                PreparedStatement itemPs = connection.prepareStatement(itemSql);
                PreparedStatement memberPs = connection.prepareStatement(memberSql);
                ResultSet bookRs = bookPs.executeQuery();
            ) {
            while (bookRs.next()) {
                int categoryId = bookRs.getInt("category_id");
                int itemId = bookRs.getInt("item_id");
                int borrowMemberId = bookRs.getInt("borrow_member_id");
                Category category = findCategory(categoryPs, categoryId);
                Item item = findItem(itemPs, itemId, categoryId);
                String borrowUser = findBorrowUser(memberPs, borrowMemberId);

                books.add(toBook(bookRs, category, item, borrowUser));
            }
        } catch (Exception e) {
            throw new Exception("查詢 books 資料表失敗: " + e.getMessage());
        }

        return books;
    }

    public List<Category> loadCategories() throws Exception {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM categories ORDER BY id";

        try (
                Connection connection = new DBConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
            ) {
            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        }

        return categories;
    }

    public List<Item> loadItems() throws Exception {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT id, category_id, name FROM items ORDER BY category_id, id";

        try (
                Connection connection = new DBConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
            ) {
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getInt("category_id"), rs.getString("name")));
            }
        }

        return items;
    }

    public void saveBook(Book book) throws Exception {
        String sql = "UPDATE books SET available = ?, borrow_member_id = ? WHERE id = ?";

        try (
                Connection connection = new DBConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
            ) {
            ps.setBoolean(1, book.isAvailable());
            if (book.getBorrowMemberId() == 0) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, book.getBorrowMemberId());
            }
            ps.setInt(3, Integer.parseInt(book.getNumber()));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("更新 books 資料表失敗: " + e.getMessage());
        }
    }

    public void addCategory(String name) throws Exception {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        executeUpdate(sql, name);
    }

    public void updateCategory(int id, String name) throws Exception {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        executeUpdate(sql, name, id);
    }

    public void deleteCategory(int id) throws Exception {
        String sql = "DELETE FROM categories WHERE id = ?";
        executeUpdate(sql, id);
    }

    public void addItem(int categoryId, String name) throws Exception {
        String sql = "INSERT INTO items (category_id, name) VALUES (?, ?)";
        executeUpdate(sql, categoryId, name);
    }

    public void updateItem(int id, int categoryId, String name) throws Exception {
        String sql = "UPDATE items SET category_id = ?, name = ? WHERE id = ?";
        executeUpdate(sql, categoryId, name, id);
    }

    public void deleteItem(int id) throws Exception {
        String sql = "DELETE FROM items WHERE id = ?";
        executeUpdate(sql, id);
    }

    public void addBook(int id, String title, String author, int categoryId, int itemId) throws Exception {
        String sql = "INSERT INTO books (id, title, author, available, borrow_member_id, category_id, item_id) "
                + "VALUES (?, ?, ?, TRUE, NULL, ?, ?)";
        executeUpdate(sql, id, title, author, categoryId, itemId);
    }

    public void updateBook(int id, String title, String author, int categoryId, int itemId) throws Exception {
        String sql = "UPDATE books SET title = ?, author = ?, category_id = ?, item_id = ? WHERE id = ?";
        executeUpdate(sql, title, author, categoryId, itemId, id);
    }

    public void deleteBook(int id) throws Exception {
        String sql = "DELETE FROM books WHERE id = ?";
        executeUpdate(sql, id);
    }

    private Category findCategory(PreparedStatement ps, int categoryId) throws Exception {
        ps.setInt(1, categoryId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new Category(categoryId, rs.getString("name"));
            }
        }

        return new Category(categoryId, "");
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

    private String findBorrowUser(PreparedStatement ps, int borrowMemberId) throws Exception {
        if (borrowMemberId == 0) {
            return "NULL";
        }

        ps.setInt(1, borrowMemberId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("account");
            }
        }

        return "NULL";
    }

    // 把資料庫查到的一筆書籍資料轉成 Java 物件
    private Book toBook(ResultSet rs, Category category, Item item, String borrowUser) throws Exception {
        return new Book(
                String.valueOf(rs.getInt("id")),
                rs.getString("title"),
                rs.getString("author"),
                rs.getBoolean("available"),
                borrowUser,
                rs.getInt("borrow_member_id"),
                rs.getInt("category_id"),
                rs.getInt("item_id"),
                category.getName(),
                item.getName());
    }
}
