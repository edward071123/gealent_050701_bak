package senior.borrowGiveBackSystemPlus.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String url = "jdbc:mysql://localhost:3306/library_plus";
    private final String user = "root";
    private final String password = "!QAZ2wsx"; // 請換成自己host上的mysql server 的root 密碼

    // 建立連線
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
