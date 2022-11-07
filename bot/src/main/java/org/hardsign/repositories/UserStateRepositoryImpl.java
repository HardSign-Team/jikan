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
            return session.byId(UserStateEntity.class).loadOptional(userId);
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
