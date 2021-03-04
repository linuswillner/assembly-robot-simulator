package com.assemblyrobot.shared.db;

import lombok.Getter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public abstract class DB {
  @Getter private static SessionFactory sessionFactory;
  private static final Logger logger = LogManager.getLogger();

  public DB() {
    try (val factory =
        new Configuration()
            .configure("/resources/config/hibernate.cfg.xml")
            .buildSessionFactory()) {
      sessionFactory = factory;
    } catch (Exception e) {
      logger.fatal("Could not build session factory:", e);
      System.exit(1);
    }
  }
}
