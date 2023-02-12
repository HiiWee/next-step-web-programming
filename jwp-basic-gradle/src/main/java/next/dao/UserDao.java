package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.InsertJdbcTemplate;
import core.jdbc.UpdateJdbcTemplate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import next.exception.DataAccessException;
import next.model.User;

public class UserDao {

    public void insert(User user) {
        InsertJdbcTemplate insertJdbcTemplate = new InsertJdbcTemplate() {
            @Override
            protected String createQueryForInsert() {
                return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            }

            @Override
            protected void setValueForInsert(final User user, final PreparedStatement preparedStatement)
                    throws SQLException {
                preparedStatement.setString(1, user.getUserId());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getName());
                preparedStatement.setString(4, user.getEmail());
            }
        };
        insertJdbcTemplate.insert(user);
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
        Connection con = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS";
            Statement st = con.createStatement();

            rs = st.executeQuery(sql);

            List<User> users = new ArrayList<>();
            User user;
            while (rs.next()) {
                user = new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                users.add(user);
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
            return users;
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    public void update(final User user) {
        UpdateJdbcTemplate updateJdbcTemplate = new UpdateJdbcTemplate() {
            @Override
            protected String createQueryForUpdate() {
                return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
            }

            @Override
            protected void setValueForUpdate(final User user, final PreparedStatement preparedStatement)
                    throws SQLException {
                preparedStatement.setString(1, user.getPassword());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getUserId());
            }
        };
        updateJdbcTemplate.update(user);
    }
}
