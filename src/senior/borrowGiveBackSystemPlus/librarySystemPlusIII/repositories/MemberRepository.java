package senior.borrowGiveBackSystemPlus.librarySystemPlusIII.repositories;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import senior.borrowGiveBackSystemPlus.db.DBConnection;
import senior.borrowGiveBackSystemPlus.models.Member;

public class MemberRepository extends BaseRepository {

    public Member login(String account, String password) throws Exception {
        String sql = "SELECT id, account, display_name, password_hash, is_admin FROM members WHERE account = ? AND password_hash = ?";

        try (
                Connection connection = new DBConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
            ) {
            ps.setString(1, account);
            ps.setString(2, hashPassword(password));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toMember(rs);
                }
            }
        }

        throw new Exception("帳號或密碼錯誤");
    }

    public Member findByAccount(String account) throws Exception {
        String sql = "SELECT id, account, display_name, password_hash, is_admin FROM members WHERE account = ?";

        try (
                Connection connection = new DBConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
            ) {
            ps.setString(1, account);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toMember(rs);
                }
            }
        }

        throw new Exception("找不到帳號: " + account);
    }

    public List<Member> loadMembers() throws Exception {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT id, account, display_name, password_hash, is_admin FROM members ORDER BY id";

        try (
                Connection connection = new DBConnection().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
            ) {
            while (rs.next()) {
                members.add(toMember(rs));
            }
        }

        return members;
    }

    public void addMember(String account, String displayName, String password, boolean admin) throws Exception {
        String sql = "INSERT INTO members (account, display_name, password_hash, is_admin) VALUES (?, ?, ?, ?)";
        executeUpdate(sql, account, displayName, hashPassword(password), admin);
    }

    public void updateMemberInfo(int id, String account, String displayName, boolean admin) throws Exception {
        String sql = "UPDATE members SET account = ?, display_name = ?, is_admin = ? WHERE id = ?";
        executeUpdate(sql, account, displayName, admin, id);
    }

    public void updateMemberPassword(int id, String password) throws Exception {
        String sql = "UPDATE members SET password_hash = ? WHERE id = ?";
        executeUpdate(sql, hashPassword(password), id);
    }

    public void deleteMember(int id) throws Exception {
        String sql = "DELETE FROM members WHERE id = ?";
        executeUpdate(sql, id);
    }

    private Member toMember(ResultSet rs) throws Exception {
        return new Member(
                rs.getInt("id"),
                rs.getString("account"),
                rs.getString("display_name"),
                rs.getString("password_hash"),
                rs.getBoolean("is_admin"));
    }

    private String hashPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hashText = new StringBuilder();

        for (byte oneByte : hashBytes) {
            hashText.append(String.format("%02x", oneByte));
        }

        return hashText.toString();
    }
}
