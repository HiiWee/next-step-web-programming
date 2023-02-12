package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import next.exception.DataAccessException;

public class JdbcTemplate {
    public void update(final String query, final PreparedStatementSetter preparedStatementSetter) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatementSetter.setValue(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException(exception);
        }
    }

    public <T> List<T> query(
            final String query,
            final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatementSetter.setValue(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> results = new ArrayList<>();
            T result;
            while (resultSet.next()) {
                result = rowMapper.mapRow(resultSet);
                results.add(result);
            }
            return results;
        } catch (SQLException exception) {
            throw new DataAccessException(exception);
        }
    }

    public <T> List<T> query(final String query, final RowMapper<T> rowMapper, final Object... values) {
        return query(query, createPreparedStatementSetter(values), rowMapper);
    }

    public <T> T queryForObject(
            final String query,
            final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatementSetter.setValue(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            T result = null;
            if (resultSet.next()) {
                result = rowMapper.mapRow(resultSet);
            }
            resultSet.close();
            return result;
        } catch (SQLException exception) {
            throw new DataAccessException(exception);
        }
    }

    public <T> T queryForObject(
            final String query,
            final RowMapper<T> rowMapper,
            final Object... values) {
        return queryForObject(query, createPreparedStatementSetter(values), rowMapper);
    }

    private PreparedStatementSetter createPreparedStatementSetter(final Object[] values) {
        return preparedStatement -> {
            for (int index = 0; index < values.length; index++) {
                preparedStatement.setObject(index + 1, values[index]);
            }
        };
    }
}
