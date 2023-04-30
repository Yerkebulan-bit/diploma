package kz.iitu.diploma_resource_server.util.sql;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SqlSelectTo<C> extends SqlParams<SqlSelectTo<C>> {

    private final Class<C> theClass;

    private SqlSelectTo(Class<C> theClass) {
        this.theClass = theClass;
    }

    public static <C> SqlSelectTo<C> theClass(Class<C> theClass) {
        return new SqlSelectTo<>(theClass);
    }

    @Override
    protected SqlSelectTo<C> me() {
        return this;
    }

    public List<C> applyTo(DataSource dataSource) {
        try (var connection = dataSource.getConnection()) {
            return applyTo(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void apply(DataSource dataSource, Consumer<C> destination) {
        try (var connection = dataSource.getConnection()) {
            apply(connection, destination);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void apply(Connection connection, Consumer<C> destination) {
        var sql = this.sql.toString();
        try (var ps = connection.prepareStatement(sql)) {

            {
                int pos = 1;
                for (final Object param : params) {
                    ps.setObject(pos++, param);
                }
            }

            try (var rs = ps.executeQuery()) {
                ResultSetObjectFactory<C> creator = null;

                while (rs.next()) {
                    if (creator == null) {
                        creator = ResultSetObjectFactory.get(theClass, rs, sql);
                    }
                    destination.accept(creator.create(rs));
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<C> applyTo(Connection connection) {
        List<C> ret = new ArrayList<>();
        apply(connection, ret::add);
        return ret;
    }

}