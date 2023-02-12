package next.dao;

import core.jdbc.JdbcTemplate;
import java.util.List;
import next.model.User;

public class UserDao {

    public void insert(User user) {
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate() {
            @Override
            protected String createQuery() {
                return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            }
        };

        insertJdbcTemplate.insert(preparedStatement -> {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
        });
    }

    public void update(final User user) {
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate() {
            @Override
            protected String createQuery() {
                return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
            }
        };
        updateJdbcTemplate.update(preparedStatement -> {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserId());

        });
    }

    public User findByUserId(String userId) {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate() {
            @Override
            protected String createQuery() {
                return "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
            }
        };
        return selectJdbcTemplate.queryForObject(
                preparedStatement -> preparedStatement.setString(1, userId),
                resultSet -> new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"))
        );
    }

    public List<User> findAll() {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate() {
            @Override
            protected String createQuery() {
                return "SELECT userId, password, name, email FROM USERS";
            }
        };
        return selectJdbcTemplate.query(resultSet ->
                new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"))
        );
    }
}
