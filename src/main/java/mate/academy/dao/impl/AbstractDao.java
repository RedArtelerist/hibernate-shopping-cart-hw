package mate.academy.dao.impl;

import java.util.function.Function;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AbstractDao {
    protected <T> T performWithinTx(Function<Session, T> sessionFunction) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            T result = sessionFunction.apply(session);
            transaction.commit();
            return result;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}