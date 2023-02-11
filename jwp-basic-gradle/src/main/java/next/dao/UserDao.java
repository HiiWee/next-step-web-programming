package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.jdbc.SelectJdbcTemplate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import next.exception.DataAccessException;
import next.model.User;

public class UserDao {
    public void insert(User user) {
        JdbcTemplate insertTemplate = new JdbcTemplate() {
            @Override
            protected void setValue(final PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };
        insertTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)");
    }

    public void update(final User user) {
        JdbcTemplate updateTemplate = new JdbcTemplate() {
            @Override
            protected void setValue(final PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }
        };
        updateTemplate.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?");
    }

    public User findByUserId(String userId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }

            return user;
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    public List<User> findAll() {
        SelectJdbcTemplate selectTemplate = new SelectJdbcTemplate() {
            @Override
            protected void setValue(final PreparedStatement preparedStatement) throws SQLException {
            }

            @Override
            protected Object mapRow(final ResultSet resultSet) throws SQLException {
                return new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
            }

        };
        return selectTemplate.query("SELECT userId, password, name, email from USERS");
    }
}
