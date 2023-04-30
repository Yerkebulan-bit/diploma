package kz.iitu.diploma_resource_server.util.sql;

import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ResultSetObjectFactory<C> {

    private final Class<C> theClass;

    private interface CopyElement {
        String resultSetFieldName();

        FieldSetter fieldSetter();
    }

    private final List<CopyElement> copyList = new ArrayList<>();

    public C create(ResultSet resultSet) {
        try {
            var instance = theClass.getConstructor().newInstance();

            for (final CopyElement copyElement : copyList) {
                var objectValue = resultSet.getObject(copyElement.resultSetFieldName());
                copyElement.fieldSetter().setField(instance, objectValue);
            }

            return instance;

        } catch (SQLException | InvocationTargetException | InstantiationException
                | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSetObjectFactory(Class<C> theClass, ResultSet resultSet, String sql) {
        this.theClass = theClass;
        createCopyList(resultSet, sql);
    }

    private void createCopyList(ResultSet resultSet, String sql) {
        var classSetter = FieldSetterMapCollector.get(theClass);

        try {

            ResultSetMetaData metaData = resultSet.getMetaData();

            var columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                var columnName = metaData.getColumnName(i);
                var fieldSetter = classSetter.get(columnName);
                if (fieldSetter == null) {
                    throw new RuntimeException(
                            "ytC9PVI85x :: No field `" + columnName + "` in " + theClass + " using sql : " + sql);
                }
                copyList.add(new CopyElement() {
                    @Override
                    public String resultSetFieldName() {
                        return columnName;
                    }

                    @Override
                    public FieldSetter fieldSetter() {
                        return fieldSetter;
                    }
                });
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static <C> ResultSetObjectFactory<C> create(Class<C> theClass, ResultSet resultSet, String sql) {
        return new ResultSetObjectFactory<>(theClass, resultSet, sql);
    }

    @SuppressWarnings("rawtypes")
    private static final ConcurrentHashMap<String, ResultSetObjectFactory> factoryMap = new ConcurrentHashMap<>();

    public static <C> ResultSetObjectFactory<C> get(Class<C> theClass, ResultSet rs, String sql) {
        try {
            var md = MessageDigest.getInstance("SHA-1");
            var sum = md.digest(sql.getBytes(UTF_8));
            var hash = Base64.getEncoder().encodeToString(sum);
            //noinspection unchecked
            return factoryMap.computeIfAbsent(hash, s -> create(theClass, rs, sql));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

