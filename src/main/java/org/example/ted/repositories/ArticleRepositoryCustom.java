package org.example.ted.repositories;


import org.example.ted.models.Article;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<Article> userFeed(int id);
}
