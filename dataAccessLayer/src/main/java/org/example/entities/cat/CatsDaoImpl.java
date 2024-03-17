package org.example.entities.cat;

import org.example.Color;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public Cat save(Cat cat) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(cat);
            tx.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage());
        }
        return cat;
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
    public List<Cat> getAll() {
        return null;
    }

    @Override
    public Cat findById(long id) {
        try (Session session = factory.openSession()) {
            var cat = (Cat) session.get(Cat.class, id);
            if (cat == null) throw new QueryException("No such cat");
            return cat;
        } catch (QueryException e) {
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Cat> findByCriteria(FindCriteria criteria) {
        if (criteria.getBirthday() != null) return getByBirthday(criteria.getBirthday());
        if (criteria.getName() != null) return getByName(criteria.getName());
        if (criteria.getBreed() != null) return getByBreed(criteria.getBreed());
        if (criteria.getColor() != null) return getByColor(criteria.getColor());
        return new ArrayList<>();
    }

    public List<Cat> getByName(String name) {
        try (Session session = factory.openSession()) {
            Query query = session.createQuery("SELECT cat from Cat cat where cat.name = :name");
            query.setParameter("name", name);
            var cats = query.list();
            if (cats == null) throw new QueryException("No such cat");
            return cats;
        } catch (QueryException e) {
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Cat> getByBirthday(LocalDate date) {
        try (Session session = factory.openSession()) {
            Query query = session.createQuery("SELECT cat from Cat cat where cat.birthday = :date");
            query.setParameter("date", date);
            var cats = query.list();
            if (cats == null) throw new QueryException("No such date");
            return cats;
        } catch (QueryException e) {
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Cat> getByColor(Color color) {
        try (Session session = factory.openSession()) {
            Query query = session.createQuery("SELECT cat from Cat cat where cat.color = :color");
            query.setParameter("color", color);
            var cats = query.list();
            if (cats == null) throw new QueryException("No such cat");
            return cats;
        } catch (QueryException e) {
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Cat> getByBreed(String breed) {
        try (Session session = factory.openSession()) {
            Query query = session.createQuery("SELECT cat from Cat cat where cat.breed = :breed");
            query.setParameter("breed", breed);
            var cats = query.list();
            if (cats == null) throw new QueryException("No such cat");
            return cats;
        } catch (QueryException e) {
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(long id) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Cat cat = (Cat)session.get(Cat.class, id);
            session.remove(cat);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
