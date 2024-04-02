/*package org.example.entities.cat;

import org.example.valueObjects.Color;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Component
public class CatsDaoImpl implements CatsDao {
    private static SessionFactory factory;
    public CatsDaoImpl() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    public Cat update(Cat cat) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(cat);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return cat;
    }

    @Override
    public List<Cat> findAll() {
        try (Session session = factory.openSession()) {
            Query query = session.createQuery("SELECT cat from Cat cat");
            var cats = query.list();
            if (cats == null) throw new QueryException("No such cats");
            return cats;
        } catch (QueryException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Cat findById(long id) {
        try (Session session = factory.openSession()) {
            var cat = (Cat) session.get(Cat.class, id);
            if (cat == null) throw new QueryException("No such cat");
            return cat;
        } catch (QueryException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Cat> findByCriteria(FindCriteria criteria) {
        if (criteria.getBirthday() != null) return findByBirthday(criteria.getBirthday());
        if (criteria.getName() != null) return findByName(criteria.getName());
        if (criteria.getBreed() != null) return findByBreed(criteria.getBreed());
        if (criteria.getColor() != null) return findByColor(criteria.getColor());
        return new ArrayList<>();
    }
}*/
