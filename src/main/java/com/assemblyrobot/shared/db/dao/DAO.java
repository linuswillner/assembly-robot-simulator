package com.assemblyrobot.shared.db.dao;

import com.assemblyrobot.shared.db.model.Engine;
import com.assemblyrobot.shared.db.model.Material;
import com.assemblyrobot.shared.db.model.Run;
import com.assemblyrobot.shared.db.model.StageController;
import com.assemblyrobot.shared.db.model.Station;

public interface DAO {
  Run getRun(long id);

  Run[] getAllRuns();

  boolean logRun(
      Run run,
      Engine engine,
      StageController stageController,
      Station[] stations,
      Material[] materials);

  boolean deleteRun(long id);
}
