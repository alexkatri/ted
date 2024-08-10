package org.example.ted.repositories;


import org.example.ted.models.Article;
import org.example.ted.models.User;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<Article> findByUser(User user_email);
}
