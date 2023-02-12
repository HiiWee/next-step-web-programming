package next.dao;

import core.jdbc.JdbcTemplate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import next.model.User;

public class UserDao {

    public void insert(User user) {
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate() {
            @Override
            protected String createQuery() {
                return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            }

            @Override
            protected void setValue(final PreparedStatement preparedStatement)
                    throws SQLException {
                preparedStatement.setString(1, user.getUserId());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getName());
                preparedStatement.setString(4, user.getEmail());
            }

            @Override
            protected User mapRow(final ResultSet rs) throws SQLException {
                return null;
            }
        };
        insertJdbcTemplate.insert();
    }

    public void update(final User user) {
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate() {
            @Override
            protected String createQuery() {
                return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
            }

            @Override
            protected void setValue(final PreparedStatement preparedStatement)
                    throws SQLException {
                preparedStatement.setString(1, user.getPassword());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getUserId());
            }

            @Override
            protected User mapRow(final ResultSet rs) throws SQLException {
                return null;
            }
        };
        updateJdbcTemplate.update();
    }

    public User findByUserId(String userId) {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate() {
            @Override
            protected String createQuery() {
                return "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
            }

            @Override
            protected void setValue(final PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, userId);
            }

            @Override
            protected User mapRow(final ResultSet rs) throws SQLException {
                return new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        };
        return (User) selectJdbcTemplate.queryForObject();
    }

    public List<User> findAll() {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate() {
            @Override
            protected String createQuery() {
                return "SELECT userId, password, name, email FROM USERS";
            }

            @Override
            protected void setValue(final PreparedStatement preparedStatement) throws SQLException {

            }

            @Override
            protected User mapRow(final ResultSet rs) throws SQLException {
                return new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        };
        return (List<User>) selectJdbcTemplate.query();
    }
}
