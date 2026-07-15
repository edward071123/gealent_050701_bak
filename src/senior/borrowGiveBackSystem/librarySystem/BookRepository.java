package senior.borrowGiveBackSystem.librarySystem;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/* 
 * 只負責讀寫檔案
 */
public class BookRepository {
    // 檔案路徑
    private String dataFilePath;

    public BookRepository(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    // 讀檔: String -> Book
    public List<Book> loadBooks() throws Exception {
        List<Book> books = new ArrayList<>();

        try {
            // 讀book.txt的檔案每一行 存到lines這個集合內
            // Paths.get(this.dataFilePath) 讀取檔案的路徑
            List<String> lines = Files.readAllLines(Paths.get(this.dataFilePath));

            for (String oneLine : lines) {
                // "P,1,Java入門,張三,false,Java,aa"
                // 字串切割
                // 用符號分割且存成array
                String[] data = oneLine.split(",", -1);

                String type = data[0];
                String number = data[1];
                String title = data[2];
                String author = data[3];
                boolean available = Boolean.parseBoolean(data[4]);
                String extra = data[5];
                String borrowUser = data[6];

                // 組合成書是for 資料架構
                if (type.equals("P")) {
                    // 程式類的書
                    ProgrammingBook p = new ProgrammingBook(number, title, author, available, borrowUser, extra);
                    books.add(p);
                } else if (type.equals("N")) {
                    // 小說類的書
                    NovelBook n = new NovelBook(number, title, author, available, borrowUser, extra);
                    books.add(n);
                }

            }

        } catch (Exception e) {
            // 去找出
            String filePath = Paths.get(this.dataFilePath).toAbsolutePath().toString();
            throw new Exception("讀取 books.txt 失敗: " + e.getMessage()
                    + ", 目前程式讀取檔案的路徑為: " + filePath);
        }

        return books;
    }

    // 寫入: Book -> String
    public void saveBooks(List<Book> books) throws Exception {
        List<String> lines = new ArrayList<>();

        for (Book oneBook : books) {
            String oneLine = oneBook.toFileSting();
            lines.add(oneLine);
        }

        try {
            Files.write(Paths.get(this.dataFilePath), lines);
        } catch (Exception e) {
            throw new Exception("儲存 books.txt 失敗, 原因為: " + e.getMessage());
        }
    }
}
