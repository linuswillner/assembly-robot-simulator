package com.assemblyrobot.shared.db.dao;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.Configuration;
import com.assemblyrobot.shared.db.model.EngineDTO;
import com.assemblyrobot.shared.db.model.MaterialDTO;
import com.assemblyrobot.shared.db.model.RunDTO;
import com.assemblyrobot.shared.db.model.StageControllerDTO;
import com.assemblyrobot.shared.db.model.StationDTO;
import java.util.Arrays;
import lombok.val;
import org.hamcrest.MatcherAssert;
import org.junit.Assume;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Disabled
class RunDAOTest {
  private final Configuration config = Config.getConfig();
  private final RunDAO dao = RunDAO.getInstance();

  @BeforeEach
  void beforeEach() {
    Assume.assumeThat(System.getenv("CI"), is(nullValue()));
    createTestRun();
  }

  @AfterEach
  void afterEach() {
    Arrays.stream(dao.getAllRuns()).forEach(run -> dao.deleteRun(run.getId()));
  }

  private void createTestRun() {
    val run =
        new RunDTO(
            config.getArrivalIntervalParams(),
            config.getAssemblyTimeParams(),
            config.getErrorCheckTimeParams(),
            config.getErrorOccurrenceParams(),
            config.getErrorFixTimes(),
            config.getStationParams());
    val engine = new EngineDTO(30);
    val stageController = new StageControllerDTO(10, 10);
    StationDTO[] stations = {new StationDTO("AssemblyStation-1", 10, 10, 30, 30)};
    MaterialDTO[] materials = {new MaterialDTO("MaterialDTO-1 [AssemblyStation-1]", 1, 1, 0, 1, 2, 1, 1)};

    dao.logRun(run, engine, stageController, stations, materials);
  }

  @Test
  @DisplayName("getRun(): Returns a correctly stored RunDTO object")
  void getRun() {
    // This is a really shallow check, but there aren't many better ways to check whether these two
    // are equal
    MatcherAssert.assertThat(dao.getRun(dao.getAllRuns()[0].getId()), instanceOf(RunDTO.class));
  }

  @Test
  @DisplayName("getRun(): Returns an array of multiple RunDTO objects")
  void getAllRuns() {
    // Already getting one run from afterEach
    createTestRun();

    Arrays.stream(dao.getAllRuns())
        .forEach(run -> MatcherAssert.assertThat(run, instanceOf(RunDTO.class)));
  }

  @Test
  @DisplayName("deleteRun(): Correctly deletes a RunDTO")
  void deleteRun() {
    createTestRun();
    dao.deleteRun(1);

    assertNull(dao.getRun(1));
  }
}
