package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Comment;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Comment> findByUser(User created_by) {
        Query query = entityManager.createQuery("SELECT c FROM Comment c WHERE c.createdBy = ?1");
        query.setParameter(1, created_by);
        List<Comment> comments = query.getResultList();
        return comments;
    }
}
