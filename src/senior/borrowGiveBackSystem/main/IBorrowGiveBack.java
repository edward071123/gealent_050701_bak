package senior.borrowGiveBackSystem.main;

public interface IBorrowGiveBack {
    // 借
    public String borrow(int number, String borrowUser) throws Exception;

    // 還
    public String giveBack(int number) throws Exception;

}
