package senior.borrowGiveBackSystem.librarySystem;

public class NovelBook extends Book {

    private String category;

    public NovelBook(String number, String title, String author, boolean available, String category, String borrowUser) {
        super(number, title, author, available, borrowUser);
        this.category = category;
    }

    @Override
    public String getType() {
        return "N";
    }

    @Override
    public String getExtraInfo() {
        return category;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + "\n"
              + "類型：小說\n"
              + "分類：" + getExtraInfo();
    }
}
