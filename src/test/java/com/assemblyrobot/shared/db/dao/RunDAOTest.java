package com.assemblyrobot.shared.db.dao;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.ApplicationConfig;
import com.assemblyrobot.shared.db.model.Engine;
import com.assemblyrobot.shared.db.model.Material;
import com.assemblyrobot.shared.db.model.Run;
import com.assemblyrobot.shared.db.model.StageController;
import com.assemblyrobot.shared.db.model.Station;
import java.util.Arrays;
import lombok.val;
import org.hamcrest.MatcherAssert;
import org.junit.Assume;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RunDAOTest {
  private final ApplicationConfig config = Config.getConfig();
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
        new Run(
            config.getArrivalIntervalParams(),
            config.getAssemblyTimeParams(),
            config.getErrorCheckTimeParams(),
            config.getErrorOccurrenceParams(),
            config.getErrorFixTimes(),
            config.getStationParams());
    val engine = new Engine(30);
    val stageController = new StageController(10, 10);
    Station[] stations = {new Station("AssemblyStation-1", 10, 10, 30, 30)};
    Material[] materials = {new Material("Material-1 [AssemblyStation-1]", 1, 1, 0, 1, 2, 1, 1)};

    dao.logRun(run, engine, stageController, stations, materials);
  }

  @Test
  @DisplayName("getRun(): Returns a correctly stored Run object")
  void getRun() {
    // This is a really shallow check, but there aren't many better ways to check whether these two
    // are equal
    MatcherAssert.assertThat(dao.getRun(1), instanceOf(Run.class));
  }

  @Test
  @DisplayName("getRun(): Returns an array of multiple Run objects")
  void getAllRuns() {
    // Already getting one run from afterEach
    createTestRun();

    Arrays.stream(dao.getAllRuns())
        .forEach(run -> MatcherAssert.assertThat(run, instanceOf(Run.class)));
  }

  @Test
  @DisplayName("deleteRun(): Correctly deletes a Run")
  void deleteRun() {
    createTestRun();
    dao.deleteRun(1);

    assertNull(dao.getRun(1));
  }
}
