package com.assemblyrobot.shared.db.dao;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.Config.UserSetting;
import com.assemblyrobot.shared.db.model.Engine;
import com.assemblyrobot.shared.db.model.Material;
import com.assemblyrobot.shared.db.model.Run;
import com.assemblyrobot.shared.db.model.StageController;
import com.assemblyrobot.shared.db.model.Station;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/** DAO for accessing the simulator run history database. */
public class RunDAO implements DAO {
  private SessionFactory sessionFactory;
  @Getter private static final RunDAO instance = new RunDAO();
  private static final Logger logger = LogManager.getLogger();

  private RunDAO() {
    try {
      val config = new Configuration().configure("/config/hibernate.cfg.xml");
      val isFirstRun = !Config.hasUserSetting(UserSetting.FIRST_RUN);

      // Only create tables on first run
      // Also using System.out.println() here because the logger isn't yet defined at the
      // constructor point
      if (isFirstRun) {
        System.out.println("Tables do not exist, creating.");
        config.setProperty("hibernate.hbm2ddl.auto", "create");
        Config.putUserSetting(UserSetting.FIRST_RUN, false);
      } else {
        System.out.println("Tables already exist, not creating again.");
        config.setProperty("hibernate.hbm2ddl.auto", "validate");
      }
      sessionFactory = config.buildSessionFactory();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Gets a simulator run by ID.
   *
   * @param id {@link Run#getId()}
   * @return {@link Run}
   */
  @Override
  public Run getRun(long id) {
    Transaction transaction = null;

    try (Session session = sessionFactory.openSession()) {
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

  /**
   * Returns an array of all runs in the database.
   *
   * @return {@link Run}[]
   */
  @Override
  public Run[] getAllRuns() {
    try (val session = sessionFactory.openSession()) {
      @SuppressWarnings({"unchecked", "deprecation"})
      List<Run> result = session.createCriteria(Run.class).list();
      return result.toArray(new Run[0]);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Logs a simulator run.
   *
   * @param run {@link Run}
   * @param engine {@link Engine}
   * @param stageController {@link StageController}
   * @param stations {@link Station}[]
   * @param materials {@link Material}[]
   * @return {@link Boolean} indicating whether the logging of the run was successful.
   */
  @Override
  public boolean logRun(
      Run run,
      Engine engine,
      StageController stageController,
      Station[] stations,
      Material[] materials) {
    Transaction transaction = null;

    try (val session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();

      engine.setRun(run);
      stageController.setRun(run);
      Arrays.stream(stations).forEach(station -> station.setRun(run));
      Arrays.stream(materials).forEach(material -> material.setRun(run));

      run.setEngine(engine);
      run.setStageController(stageController);
      run.setStations(stations);
      run.setMaterials(materials);

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

  /**
   * Deletes a run from the database based on its ID.
   *
   * @param id {@link Run#getId()}
   * @return {@link Boolean} indicating whether deletion was successful.
   */
  @Override
  public boolean deleteRun(long id) {
    val toDelete = getRun(id);

    // Check that what we're deleting exists
    if (toDelete == null) {
      logger.warn("Attempted to delete non-existent run {}.", id);
      return false;
    }

    Transaction transaction = null;

    try (val session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      session.delete(toDelete);
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

  @SuppressWarnings("deprecation")
  @Override
  public void finalize() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }
}
