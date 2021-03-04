package com.assemblyrobot.shared.db.dao;

import com.assemblyrobot.shared.db.DB;
import com.assemblyrobot.shared.db.model.Run;
import java.util.List;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class RunDAO implements DAO {
  private final SessionFactory sessionFactory = DB.getSessionFactory();
  private static final Logger logger = LogManager.getLogger();

  @Override
  public Run getRun(long id) {
    Transaction transaction = null;

    try (val session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      val run = session.get(Run.class, id);
      transaction.commit();
      return run;
    } catch (Exception e) {
      logger.error("Error while getting run {}:", id, e);

      if (transaction != null) {
        transaction.rollback();
      }

      return null;
    }
  }

  @Override
  public Run[] getAllRuns() {
    try (val session = sessionFactory.openSession()) {
      @SuppressWarnings("unchecked")
      List<Run> result = session.createQuery("from runs").getResultList();
      return result.toArray(new Run[0]);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public boolean logRun(Run run) {
    Transaction transaction = null;

    try (val session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      session.saveOrUpdate(run);
      transaction.commit();
      return true;
    } catch (Exception e) {
      logger.error("Error while logging run {}:", run.getId(), e);

      if (transaction != null) {
        transaction.rollback();
      }

      return false;
    }
  }

  @Override
  public boolean deleteRun(long id) {
    // Check that what we're deleting exists
    if (getRun(id) == null) {
      logger.warn("Attempted to delete non-existent run {}.", id);
      return false;
    }

    Transaction transaction = null;

    try (val session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      session.delete(id);
      transaction.commit();
      return true;
    } catch (Exception e) {
      logger.error("Error while deleting run {}:", id, e);

      if (transaction != null) {
        transaction.rollback();
      }

      return false;
    }
  }
}
