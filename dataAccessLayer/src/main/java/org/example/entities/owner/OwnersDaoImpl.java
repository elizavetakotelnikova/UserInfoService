package org.example.entities.owner;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OwnersDaoImpl implements OwnersDao {
    private static SessionFactory factory;
    public OwnersDaoImpl() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    public Owner save(Owner owner) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(owner);
            tx.commit();
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage());
        }
        return owner;
    }

    @Override
    public Owner update(Owner owner) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(owner);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return owner;
    }

    @Override
    public Owner findById(long id) {
        try (Session session = factory.openSession()) {
            var owner = (Owner) session.get(Owner.class, id);
            if (owner == null) throw new QueryException("No such owner");
            return owner;
        } catch (QueryException e) {
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Owner> findByCriteria(FindCriteria criteria) {
        if (criteria.getBirthday() != null) return getByBirthday(criteria.getBirthday());
        return new ArrayList<>();
    }
    public List<Owner> getByBirthday(LocalDate date) {
        try (Session session = factory.openSession()) {
            Query query = session.createQuery("SELECT owner from Owner owner where owner.birthday = :date");
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

    @Override
    public void delete(long id) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Owner owner = (Owner)session.get(Owner.class, id);
            session.remove(owner);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

