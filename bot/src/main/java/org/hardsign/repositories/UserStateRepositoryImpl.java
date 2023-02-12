package org.hardsign.repositories;

import org.hardsign.models.users.UserStateEntity;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class UserStateRepositoryImpl implements UserStateRepository {

    private final SessionFactory sessionFactory;

    public UserStateRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<UserStateEntity> findByUserId(long userId) {
        try (var session = sessionFactory.openSession()) {
            var cb = session.getCriteriaBuilder();
            var cq = cb.createQuery(UserStateEntity.class);
            var root = cq.from(UserStateEntity.class);

            var query = cq.select(root).where(cb.equal(root.get("userId"), userId));
            return session.createQuery(query).getResultList().stream().findFirst();
        }
    }

    @Override
    public UserStateEntity save(UserStateEntity entity) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();
        }
        return entity;
    }
}
