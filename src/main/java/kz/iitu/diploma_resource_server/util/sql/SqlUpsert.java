package kz.iitu.diploma_resource_server.util.sql;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static kz.iitu.diploma_resource_server.util.sql.SqlUpsert.Type.ARRAY;
import static kz.iitu.diploma_resource_server.util.sql.SqlUpsert.Type.OTHER;

public class SqlUpsert {

    private final String tableName;

    private SqlUpsert(String tableName) {
        this.tableName = tableName;
    }

    public static SqlUpsert into(String tableName) {
        return new SqlUpsert(tableName);
    }

    private static class NameValue {
        public final String  name;
        public final boolean key;
        public final Object  value;
        public final Type    type;
        public final int     paramSizeForArray;

        private NameValue(String name, boolean key, Object value) {
            this.name              = name;
            this.key               = key;
            this.value             = value;
            this.type              = OTHER;
            this.paramSizeForArray = 1;
        }

        private NameValue(String name, boolean key, Object value, Type type, int paramSizeForArray) {
            this.name              = name;
            this.key               = key;
            this.value             = value;
            this.type              = type;
            this.paramSizeForArray = paramSizeForArray;
        }

        @Override
        public String toString() {
            return "NameValue{" +
                    "name='" + name + '\'' +
                    ", key=" + key +
                    ", value=" + value +
                    ", type=" + type +
                    ", paramSizeForArray=" + paramSizeForArray +
                    '}';
        }
    }

    public enum Type {
        ARRAY, OTHER
    }

    private final List<NameValue> dataList = new ArrayList<>();

    public SqlUpsert key(String name, Object value) {
        dataList.add(new NameValue(name, true, value));
        return this;
    }

    public SqlUpsert field(String name, Object value) {
        dataList.add(new NameValue(name, false, value));
        return this;
    }

    public SqlUpsert field(String name, Type type, Object value, int paramSizeForArray) {
        dataList.add(new NameValue(name, false, value, type, paramSizeForArray));
        return this;
    }


    public Optional<SqlUpdate> toUpdate() {

        if (dataList.isEmpty()) {
            return Optional.empty();
        }
        if (dataList.stream().noneMatch(x -> x.key)) {
            return Optional.empty();
        }
        if (dataList.stream().allMatch(x -> x.key)) {
            return Optional.empty();
        }

        SqlUpdate u = new SqlUpdate();

        u.sql("INSERT INTO " + tableName + " as a");

        u.sql(dataList.stream().map(x -> x.name).collect(joining(", ", " (", ")")));

        u.sql(" VALUES ");

        u.sql(dataList.stream().map(x -> {
            if (x.type == ARRAY) {
                StringJoiner sj = new StringJoiner(",");
                for (int i = 0; i < x.paramSizeForArray; i++) {
                    sj.add("?");
                }
                return "ARRAY[" + (x.paramSizeForArray == 0 ? "''" : sj.toString()) + "]";
            }

            return "?";
        }).collect(joining(", ", "(", ")")));

        dataList.stream().map(x -> x.value).forEach(x -> {
            if (x instanceof List) {
                u.params(new ArrayList<>((Collection<?>) x));
                return;
            }

            u.param(x);
        });

        u.sql(" ON CONFLICT ");

        u.sql(dataList.stream().filter(x -> x.key).map(x -> x.name).collect(joining(", ", "(", ")")));

        u.sql(" DO UPDATE SET ");

        u.sql(dataList.stream().filter(x -> !x.key).map(x -> {
            if (x.type == ARRAY) {
                StringJoiner sj = new StringJoiner(",");
                for (int i = 0; i < x.paramSizeForArray; i++) {
                    sj.add("?");
                }
                return x.name + " = ARRAY[" + (x.paramSizeForArray == 0 ? "''" : sj.toString()) + "]";
            }

            return x.name + " = ?";
        }).collect(joining(", ")));

        dataList.stream().filter(x -> !x.key).map(x -> x.value).forEach(x -> {
            if (x instanceof List) {
                u.params(new ArrayList<>((Collection<?>) x));
                return;
            }

            u.param(x);
        });

        u.sql(" WHERE ");

        u.sql(dataList.stream().filter(x -> x.key).map(x -> "a." + x.name + " = ?").collect(joining(" AND ")));
        dataList.stream().filter(x -> x.key).map(x -> x.value).forEach(u::param);

        return Optional.of(u);
    }

    @Override
    public String toString() {
        return "SqlUpsert{" +
                "tableName='" + tableName + '\'' +
                ", dataList=" + dataList +
                '}';
    }

}
