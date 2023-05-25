package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.News;

import java.util.List;

public interface NewsRegister {

    List<News> loadNews();

    String saveNew(News news);

}
