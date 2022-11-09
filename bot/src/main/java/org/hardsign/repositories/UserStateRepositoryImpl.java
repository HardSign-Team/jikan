package org.hardsign.repositories;

import org.hardsign.models.users.UserStateEntity;
import org.hibernate.HibernateException;
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
            var em = session
                    .getEntityManagerFactory()
                    .createEntityManager();
            var criteriaBuilder = em.getCriteriaBuilder();
            var query = criteriaBuilder.createQuery(UserStateEntity.class);
            var root = query.from(UserStateEntity.class);

            var userIdCriteria = query
                    .where(criteriaBuilder.equal(root.get("userId"), userId));
            return session.createQuery(userIdCriteria).getResultList().stream().findFirst();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
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
