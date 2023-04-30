package kz.iitu.diploma_resource_server.util.sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SqlUpdate extends SqlParams<SqlUpdate> {

    @Override
    protected SqlUpdate me() {
        return this;
    }

    public int applyTo(Connection connection) {
        var sql = this.sql.toString();

        try (var ps = connection.prepareStatement(sql)) {

            {
                int pos = 1;

                for (final var param : this.params) {
                    ps.setObject(pos++, param);
                }
            }

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException("hg3io32fds");
        }
    }

    public int applyTo(DataSource dataSource) {
        try (var connection = dataSource.getConnection()) {
            return applyTo(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "SqlUpdate{" +
                "sql=" + sql +
                ", params=" + params +
                '}';
    }

}
