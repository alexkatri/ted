//package org.example.ted.repositories;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import org.example.ted.models.Request;
//import org.example.ted.models.User;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//@Transactional(readOnly = true)
//public class RequestRepositoryCustomImpl implements RequestRepositoryCustom {
//
//    @PersistenceContext
//    EntityManager entityManager;
//
//    @Override
//    public List<Request> findByReceiver(User receiver) {
//        Query query = entityManager.createQuery("SELECT r FROM Request r WHERE r.receiver = ?1");
//        query.setParameter(1, receiver);
//        List<Request> requests = query.getResultList();
//        return requests;
//    }
//}
