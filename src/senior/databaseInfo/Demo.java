package senior.databaseInfo;

public class Demo {
    public static void main(String[] args) {
        /*
            執行流程
            Main
                │
                ▼
            StudentRepository
                │
                ▼
            DBConnection
                │
                ▼
            MySQL
        */
        try {
            StudentRepository repository = new StudentRepository();
            

            repository.findAll();

            // 課堂練習: 請大家設計一個findOne(int id) : 查詢單一同學
            // repository.findOne(2);

            // repository.insert(new Student("Ed", 23));

            // repository.update(new Student(7, "Tom2026" , 30));

            // repository.delete(8);

            // 課後練習: 請用本範例去修改 borrowGiveBackSystem/librarySystem的db 改為使用mysql 的 新增/修改/刪除/查詢
            // 提示: 只需搬移DBConnection.java 跟 模仿StudentRepository.java去修改 BookRepository.java
            // 記得新增table

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } 
        
    }
}
