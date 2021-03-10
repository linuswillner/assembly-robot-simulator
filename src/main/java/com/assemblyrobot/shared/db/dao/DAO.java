package com.assemblyrobot.shared.db.dao;

import com.assemblyrobot.shared.db.model.EngineDTO;
import com.assemblyrobot.shared.db.model.MaterialDTO;
import com.assemblyrobot.shared.db.model.RunDTO;
import com.assemblyrobot.shared.db.model.StageControllerDTO;
import com.assemblyrobot.shared.db.model.StationDTO;

public interface DAO {
  RunDTO getRun(long id);

  RunDTO[] getAllRuns();

  boolean logRun(
      RunDTO run,
      EngineDTO engine,
      StageControllerDTO stageController,
      StationDTO[] stations,
      MaterialDTO[] materials);

  boolean deleteRun(long id);
}
