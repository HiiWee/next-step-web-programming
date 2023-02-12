package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.SelectJdbcTemplate;
import java.sql.PreparedStatement;
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
        };
        insertJdbcTemplate.insert();
    }

    public User findByUserId(String userId) {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
            @Override
            protected String createQuery() {
                return "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
            }

            @Override
            protected void setValue(final PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, userId);
            }
        };
        return selectJdbcTemplate.queryForObject();
    }

    public List<User> findAll() {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
            @Override
            protected String createQuery() {
                return "SELECT userId, password, name, email FROM USERS";
            }

            @Override
            protected void setValue(final PreparedStatement preparedStatement) throws SQLException {

            }
        };
        return selectJdbcTemplate.query();
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
        };
        updateJdbcTemplate.update();
    }
}
