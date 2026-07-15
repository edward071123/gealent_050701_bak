package senior.borrowGiveBackSystem.main;

/* 
 * 借還系統
 */
public interface IBorrow {
    public String borrow(int number, String borrowUser) throws Exception;
    public String giveBack(int number) throws Exception;
}
