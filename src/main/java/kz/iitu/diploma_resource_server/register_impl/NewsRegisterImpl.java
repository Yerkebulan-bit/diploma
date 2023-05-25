package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.model.News;
import kz.iitu.diploma_resource_server.register.NewsRegister;
import kz.iitu.diploma_resource_server.sql.NewsTable;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class NewsRegisterImpl implements NewsRegister {

    private final DataSource dataSource;

    @Autowired
    public NewsRegisterImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<News> loadNews() {
        return SqlSelectTo.theClass(News.class)
                .sql(NewsTable.SELECT_ALL_NEWS)
                .applyTo(dataSource);
    }

    @Override
    public String saveNew(News news) {
        Objects.requireNonNull(news);

        var id = UUID.randomUUID().toString();

        SqlUpsert.into(NewsTable.TABLE_NAME)
                .key(NewsTable.ID, id)
                .field(NewsTable.TITLE, news.title)
                .field(NewsTable.CONTENT, news.content)
                .field(NewsTable.CATEGORY, news.category)
                .toUpdate().ifPresent(u -> u.applyTo(dataSource));

        return id;
    }
}
