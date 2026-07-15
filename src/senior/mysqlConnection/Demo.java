package senior.mysqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Demo {
    /*
        新增資料庫
        CREATE DATABASE school;
    
        使用資料庫
        USE school;
    
        新增資料表
        CREATE TABLE students(
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(50),
            age INT
        );

        單筆新增
        INSERT INTO students (name, age) VALUES ('Tom', 18);

        新增多筆
        INSERT INTO students (name, age)
        VALUES
        ('Tom1', 18),
        ('Amy', 20),
        ('John', 21),
        ('Mary', 19);

        查詢
        SELECT * FROM students;
    */

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/school";
        String user = "root";
        String password = "!QAZ2wsx";

        try {
            // 1. 載入 MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 建立連線
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("MySQL 連線成功");

            // 3. 建立 SQL 執行物件
            Statement stmt = conn.createStatement();

            // 4. 執行 SQL
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            // 5. 讀取結果
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " / " +
                    rs.getString("name") + " / " +
                    rs.getInt("age")
                );
            }

            // 6. 關閉資源
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
