package com.assemblyrobot.ui.controllers;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.Configuration;
import com.assemblyrobot.shared.db.dao.RunDAO;
import com.assemblyrobot.shared.db.model.EngineDTO;
import com.assemblyrobot.shared.db.model.MaterialDTO;
import com.assemblyrobot.shared.db.model.RunDTO;
import com.assemblyrobot.shared.db.model.StageControllerDTO;
import com.assemblyrobot.shared.db.model.StationDTO;
import com.assemblyrobot.simulator.core.metrics.CentralMetricsCollector;
import com.assemblyrobot.simulator.system.SimulatorEngine;
import com.assemblyrobot.simulator.system.components.StageController;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.metricscollectors.EngineMetricsCollector;
import com.assemblyrobot.simulator.system.metricscollectors.MaterialMetricsCollector;
import com.assemblyrobot.ui.views.Overview;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/** Controller class for the {@link com.assemblyrobot.ui.views.Overview}. */
@RequiredArgsConstructor
public class OverviewController {
  @Getter private StationViewerController stationViewerController;
  private SimulatorEngine engine = new SimulatorEngine(this);
  private final Configuration config = Config.getConfig();
  private final RunDAO dao = RunDAO.getInstance();
  private final CentralMetricsCollector metricsCollector = CentralMetricsCollector.getInstance();
  private final Overview overview;
  private static final Logger logger = LogManager.getLogger();

  public void setStationViewerController(
      StationViewerController stationViewerController) {
    this.stationViewerController = stationViewerController;
    engine.setStationViewerController(stationViewerController);
  }

  /**
   * Starts a new simulation run.
   */
  public void startEngine() {
    Configurator.setRootLevel(Level.TRACE);
    engine.start();
  }

  /**
   * Ends the current simulation run.
   */
  public void stopEngine() {
    engine.endRun();
  }

  /**
   * Resets the {@link com.assemblyrobot.simulator.core.Engine} for a new simulator run.
   */
  public void resetEngine() {
    engine.interrupt(); // Interrupt any operations (=> sleeps) in progress
    val newEngine = new SimulatorEngine(this);
    newEngine.setStationViewerController(stationViewerController);
    engine = newEngine;
  }

  /**
   * Pauses execution and enters step-by-step mode.
   *
   * @param isPause Whether to set the engine to step-by-step mode.
   */
  public void setPause(boolean isPause) {
    engine.setPause(isPause);
  }

  /**
   * Allows the engine to run a new CPU cycle in step-by-step mode.
   *
   * @param canProceed Whether to allow proceeding or not.
   */
  public void setCanProceed(boolean canProceed) {
    engine.setCanProceed(canProceed);
  }

  /**
   * Moves the engine forward by one CPU cycle in step-by-step mode.
   */
  public void takeStep() {
    engine.setCanProceed(true);
    logger.trace("Taking a step forward.");
  }

  /**
   * Sets the engine speed multiplier.
   *
   * @param value The speed multiplier to adjust sleep time by.
   */
  public void setSpeed(double value) {
    engine.setSpeedMultiplier(value);
  }

  /**
   * Logs the current simulator run to the database.
   */
  public void logRun() {
    val run =
        new RunDTO(
            config.getArrivalIntervalParams(),
            config.getAssemblyTimeParams(),
            config.getErrorCheckTimeParams(),
            config.getErrorOccurrenceParams(),
            config.getErrorFixTimes(),
            config.getStationParams());
    val engine = new AtomicReference<EngineDTO>();
    val stageController = new AtomicReference<StageControllerDTO>();
    val stationsList = new ArrayList<StationDTO>();
    val materialsList = new ArrayList<MaterialDTO>();

    metricsCollector
        .getCollectors()
        .values()
        .forEach(
            collector -> {
              switch (collector.getType()) {
                case ENGINE -> engine
                    .set(new EngineDTO(collector.getMetric(EngineMetricsCollector.Metrics.TOTAL_SIMULATION_TIME.getMetricName(), 0)));

                case STAGE -> {}

                case STAGE_CONTROLLER -> stageController.set(
                    new StageControllerDTO(collector.getMetric(StageController.Metrics.TOTAL_MATERIAL_AMOUNT.getMetricName(), 0),
                        collector.getMetric(StageController.Metrics.TOTAL_ASSEMBLED_AMOUNT.getMetricName(), 0)));

                case STATION -> stationsList.add(new StationDTO(collector.getHostName(),
                        collector.getMetric(Station.Metrics.STATION_MATERIAL_AMOUNT.getMetricName(), 0),
                        collector.getMetric(Station.Metrics.STATION_PROCESSED_AMOUNT.getMetricName(), 0),
                        collector.getMetric(Station.Metrics.STATION_BUSY_TIME.getMetricName(), 0),
                        collector.getMetric(Station.Metrics.STATION_TOTAL_PASSTHROUGH_TIME.getMetricName(), 0)));

                case MATERIAL -> materialsList.add(new MaterialDTO(collector.getHostName(),
                        collector.getMetric(MaterialMetricsCollector.Metrics.QUEUE_START_TIME.getMetricName(), 0),
                        collector.getMetric(MaterialMetricsCollector.Metrics.QUEUE_END_TIME.getMetricName(), 0),
                        collector.getMetric(MaterialMetricsCollector.Metrics.QUEUE_DURATION.getMetricName(), 0),
                        collector.getMetric(MaterialMetricsCollector.Metrics.PROCESSING_START_TIME.getMetricName(), 0),
                        collector.getMetric(MaterialMetricsCollector.Metrics.PROCESSING_END_TIME.getMetricName(), 0),
                        collector.getMetric(MaterialMetricsCollector.Metrics.PROCESSING_DURATION.getMetricName(), 0),
                        collector.getMetric(MaterialMetricsCollector.Metrics.PASSTHROUGH_TIME.getMetricName(), 0)
                    ));
              }
            });

    val stations = stationsList.toArray(new StationDTO[0]);
    val materials = materialsList.toArray(new MaterialDTO[0]);

    dao.logRun(run, engine.get(), stageController.get(), stations, materials);
  }

  /**
   * Resets all metrics collectors.
   */
  public void resetMetricsCollectors(){
    metricsCollector.dump();
  }

  /**
   * Visualises the arrival of a {@link com.assemblyrobot.simulator.system.components.Material}
   * into the system.
   */
  public void onArrival(){
    overview.materialToAssembly();
  }

  /**
   * Visualises the transfer of a {@link com.assemblyrobot.simulator.system.components.Material} in
   * the system.
   *
   * @param destination {@link String}
   */
  public void onTransfer(String destination) {
    switch (destination) {
      case "error_check" -> overview.materialToErrorCheck();
      case "bolting" -> overview.materialToBolting();
      case "fitting" -> overview.materialToFitting();
      case "welding" -> overview.materialToWelding();
      case "riveting" ->overview.materialToRiveting();
      case "positioning" -> overview.materialToPosition();
      case "from_bolting" -> overview.materialFromBolting();
      case "from_fitting" -> overview.materialFromFitting();
      case "from_welding" -> overview.materialFromWelding();
      case "from_riveting" -> overview.materialFromRiveting();
      case "from_positioning" -> overview.materialFromPosition();
      case "departure" -> overview.materialToDeparture();
    }
  }

}
