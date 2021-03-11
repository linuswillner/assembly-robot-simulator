package com.assemblyrobot.ui.controllers;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.ApplicationConfig;
import com.assemblyrobot.shared.constants.ErrorType;
import com.assemblyrobot.shared.constants.StageID;
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
import lombok.SneakyThrows;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class OverviewController {

  @Getter private final SimulatorEngine engine = new SimulatorEngine(this);
  private final ApplicationConfig config = Config.getConfig();
  private final RunDAO dao = RunDAO.getInstance();
  private Overview overview;
  private final CentralMetricsCollector metricsCollector = CentralMetricsCollector.getInstance();
  private static final Logger logger = LogManager.getLogger();

  public OverviewController(Overview overview){
    this.overview = overview;
  }

  public void startEngine() {
    Configurator.setRootLevel(Level.TRACE);
    engine.start();
  }

  public void stopEngine() {
    engine.endRun();
  }

  public void setPause(boolean isPause) {
    engine.setPause(isPause);
  }

  public void setCanProceed(boolean canProceed) {
    engine.setCanProceed(canProceed);
  }

  public void takeStep() {
    engine.setCanProceed(true);
    logger.trace("Taking a step forward.");
  }

  public void setSpeed(double value) {
    engine.setSpeedMultiplier(value);
  }

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

  public void resetMetricsCollectors(){
    metricsCollector.dump();
  }

  //Animation controls
  @SneakyThrows
  public void onArrival(){
    overview.materialToAssembly();
  }

  @SneakyThrows
  public void onTransfer(String destination){
    switch (destination){
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
