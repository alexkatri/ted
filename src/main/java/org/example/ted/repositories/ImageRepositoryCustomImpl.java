package org.example.ted.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.ted.models.Image;
import org.example.ted.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class ImageRepositoryCustomImpl implements ImageRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Image findByUser(User user_email) {
        Image image = null;
        Query query = entityManager.createQuery("SELECT i FROM Image i WHERE i.user = ?1");
        query.setParameter(1, user_email);
        List<Image> images = query.getResultList();
        if (images != null && images.size() > 0)
            image = images.get(0);
        return image;
    }
}
