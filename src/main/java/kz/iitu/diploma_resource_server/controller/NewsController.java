package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.News;
import kz.iitu.diploma_resource_server.register.NewsRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsRegister newsRegister;

    @Autowired
    public NewsController(NewsRegister newsRegister) {
        this.newsRegister = newsRegister;
    }

    @GetMapping("/load-news")
    public List<News> loadNews() {
        return newsRegister.loadNews();
    }

    @PostMapping("/save-news")
    public String saveNews(@RequestBody News news) {
        return newsRegister.saveNew(news);
    }

}
