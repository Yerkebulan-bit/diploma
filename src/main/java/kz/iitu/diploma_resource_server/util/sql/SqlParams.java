package kz.iitu.diploma_resource_server.util.sql;

import java.util.ArrayList;
import java.util.List;

public abstract class SqlParams<T> {

    protected final StringBuilder sql = new StringBuilder();
    protected final List<Object> params = new ArrayList<>();

    protected abstract T me();

    public T sql(String sqlStr) {
        sql.append(sqlStr);
        return me();
    }

    public T param(Object param) {
        params.add(param);
        return me();
    }

    public T params(List<Object> valueList) {
        params.addAll(valueList);
        return me();
    }

}
