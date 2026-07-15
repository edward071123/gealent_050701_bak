package senior.borrowGiveBackSystemPlus.main;


// 泛型的應用 因應傳進來的不同型態
public interface IBorrowGiveBack<T> {
    // 借
    public String borrow(int number, T borrowUser) throws Exception;

    // 還
    public String giveBack(int number) throws Exception;
}
