package com.assemblyrobot.shared.db.dao;

import com.assemblyrobot.shared.db.model.Run;

public interface DAO {
  Run getRun(long id);
  Run[] getAllRuns();
  boolean logRun(Run run);
  boolean deleteRun(long id);
}
