package senior.databaseInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentRepository {

    // 查詢全部
    public void findAll() throws Exception {
        String sql = "SELECT * FROM students";
        try (
            // Java 7 開始新增的 try-with-resources，也是 JDBC 最推薦的寫法
            // 需要關閉連線的寫在try(這裡面)
            Connection connection = new DBConnection().getConnection();
            // 避免被SQL Injections(注入式語法) 攻擊
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();      
        ) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id")
                            + " "
                            + rs.getString("name")
                            + " "
                            + rs.getInt("age")
                );
            }

        } catch (Exception e) {
            throw new Exception("查詢全部出現問題:" + e.getMessage());
        }

    }

    // 查詢單一by Id
    public void findOne(int id) throws Exception {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (
            // Java 7 開始新增的 try-with-resources，也是 JDBC 最推薦的寫法
            // 需要關閉連線的寫在try(這裡面)
            Connection connection = new DBConnection().getConnection();
            // 避免被SQL Injections(注入式語法) 攻擊
            PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    System.out.println(
                            rs.getInt("id")
                                + " "
                                + rs.getString("name")
                                + " "
                                + rs.getInt("age")
                    );
                }
            }
        } catch (Exception e) {
            throw new Exception("查詢單一出現問題:" + e.getMessage());
        }

    }

    // 新增
    public void insert(Student student) throws Exception {
        String sql = "INSERT INTO students(name, age) VALUES(?, ?)";

        try (
            // Java 7 開始新增的 try-with-resources，也是 JDBC 最推薦的寫法
            // 需要關閉連線的寫在try(這裡面)
            Connection connection = new DBConnection().getConnection();
            // 避免被SQL Injections(注入式語法) 攻擊
            PreparedStatement ps = connection.prepareStatement(sql);   
        ) {
            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            int row = ps.executeUpdate();
            System.out.println("新增成功：" + row);

        } catch (Exception e) {
            throw new Exception("新增出現問題:" + e.getMessage());
        }
    }

    // 修改
    public void update(Student student) throws Exception {
        String sql = "UPDATE students SET name=?, age=? WHERE id=?";

        try (
            // Java 7 開始新增的 try-with-resources，也是 JDBC 最推薦的寫法
            // 需要關閉連線的寫在try(這裡面)
            Connection connection = new DBConnection().getConnection();
            // 避免被SQL Injections(注入式語法) 攻擊
            PreparedStatement ps = connection.prepareStatement(sql);   
        ) {
            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setInt(3, student.getId());

            int row = ps.executeUpdate();
            System.out.println("修改成功：" + row);

        } catch (Exception e) {
            throw new Exception("修改出現問題:" + e.getMessage());
        }
    }

    // 刪除
    public void delete(int id) throws Exception {
         String sql = "DELETE FROM students WHERE id=?";

        try (
            // Java 7 開始新增的 try-with-resources，也是 JDBC 最推薦的寫法
            // 需要關閉連線的寫在try(這裡面)
            Connection connection = new DBConnection().getConnection();
            // 避免被SQL Injections(注入式語法) 攻擊
            PreparedStatement ps = connection.prepareStatement(sql);   
        ) {
            ps.setInt(1, id);
            
            int row = ps.executeUpdate();
            System.out.println("刪除成功：" + row);

        } catch (Exception e) {
            throw new Exception("刪除出現問題:" + e.getMessage());
        }
    }
}
