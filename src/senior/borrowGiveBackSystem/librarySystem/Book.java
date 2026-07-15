package senior.borrowGiveBackSystem.librarySystem;

/* 
 * 每本書的欄位狀態
 */
public class Book {
    private String number;
    private String title;
    private String author;
    private boolean available;
    private String borrowUser;

    public Book(String number, String title, String author, boolean available, String borrowUser) {
        this.number = number;
        this.title = title;
        this.author = author;
        this.available = available;
        this.borrowUser = borrowUser;
    }

    public String getType() {
        return "B";
    }

    public String getNumber() {
        return this.number;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public String getBorrowUser() {
        return this.borrowUser;
    }

    public String getExtraInfo() {
        return "";
    }

    public String toFileSting() {
        return getType() + "," + getNumber() + "," + getTitle() + "," + getAuthor() + "," + isAvailable() + ","
                + getExtraInfo() + "," + getBorrowUser();
    }

    public String getInfo() {
        return "編號: " + getNumber() + "\n"
                + "書名：" + getTitle() + "\n"
                + "作者：" + getAuthor() + "\n"
                + "可借：" + (isAvailable() ? "可" : "已被" + borrowUser + "借走");
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setBorrowUser(String borrowUser) {
        this.borrowUser = borrowUser;
    }
}
