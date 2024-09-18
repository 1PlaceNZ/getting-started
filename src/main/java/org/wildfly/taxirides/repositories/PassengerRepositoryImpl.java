package org.wildfly.taxirides.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.wildfly.taxirides.entities.Passenger;

import java.util.List;

@Transactional
public class PassengerRepositoryImpl implements PassengerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Passenger findById(Long id) {
        return entityManager.find(Passenger.class, id);
    }

    @Override
    public List<Passenger> findAll() {
        return entityManager.createQuery("SELECT p FROM Passenger p", Passenger.class).getResultList();
    }

    @Override
    public List<Passenger> findByIds(List<Long> ids) {
        return entityManager.createQuery("SELECT p FROM Passenger p WHERE p.id IN :ids", Passenger.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public Passenger save(Passenger passenger) {
        if (passenger.getId() == null) {
            entityManager.persist(passenger);
            return passenger;
        } else {
            return entityManager.merge(passenger);
        }
    }

    @Override
    public void delete(Passenger passenger) {
        if (entityManager.contains(passenger)) {
            entityManager.remove(passenger);
        } else {
            entityManager.remove(entityManager.merge(passenger));
        }
    }
}