package org.example.ted.repositories;


import org.example.ted.models.Comment;
import org.example.ted.models.Like;
import org.example.ted.models.User;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findByUser(User created_by);
}
