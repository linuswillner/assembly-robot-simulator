package com.assemblyrobot.shared.db.model;

import static com.assemblyrobot.shared.utils.JsonUtils.gson;

import com.assemblyrobot.shared.config.model.ErrorFixTimeConfig;
import com.assemblyrobot.shared.config.model.ErrorOccurrenceConfig;
import com.assemblyrobot.shared.config.model.NormalDistributionConfig;
import com.assemblyrobot.shared.config.model.StationConfig;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "runs")
@NoArgsConstructor
public class Run implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @Id
  @Getter
  @Setter
  @Column(nullable = false)
  @GeneratedValue
  private long id;

  @Column(nullable = false, name = "arrival_interval_params")
  private String arrivalIntervalParams;

  @Column(nullable = false, name = "assembly_time_params")
  private String assemblyTimeParams;

  @Column(nullable = false, name = "error_check_time_params")
  private String errorCheckTimeParams;

  @Column(nullable = false, name = "error_occurrence_params")
  private String errorOccurrenceParams;

  @Column(nullable = false, name = "error_fix_times")
  private String errorFixTimes;

  @Column(nullable = false, name = "station_params")
  private String stationParams;

  @Getter
  @Setter
  @OneToOne(mappedBy = "run", orphanRemoval = true, cascade = CascadeType.ALL)
  private Engine engine;

  @Getter
  @Setter
  @OneToOne(mappedBy = "run", orphanRemoval = true, cascade = CascadeType.ALL)
  private StageController stageController;

  @Getter
  @Column(nullable = false)
  @OneToMany(mappedBy = "run", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private final Set<Station> stations = new HashSet<>();

  @Getter
  @Column(nullable = false)
  @OneToMany(mappedBy = "run", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private final Set<Material> materials = new HashSet<>();

  // Having to use this monolith constructor as opposed to @RequiredArgsConstructor because we don't
  // want a run ID to be defined before creation time
  public Run(
      NormalDistributionConfig arrivalIntervalParams,
      NormalDistributionConfig assemblyTimeParams,
      NormalDistributionConfig errorCheckTimeParams,
      ErrorOccurrenceConfig errorOccurrenceParams,
      ErrorFixTimeConfig errorFixTimes,
      StationConfig stationParams) {
    setArrivalIntervalParams(arrivalIntervalParams);
    setAssemblyTimeParams(assemblyTimeParams);
    setErrorCheckTimeParams(errorCheckTimeParams);
    setErrorOccurrenceParams(errorOccurrenceParams);
    setErrorFixTimes(errorFixTimes);
    setStationParams(stationParams);
  }

  public NormalDistributionConfig getArrivalIntervalParams() {
    return gson.fromJson(arrivalIntervalParams, NormalDistributionConfig.class);
  }

  public void setArrivalIntervalParams(NormalDistributionConfig arrivalIntervalParams) {
    this.arrivalIntervalParams = gson.toJson(arrivalIntervalParams);
  }

  public NormalDistributionConfig getAssemblyTimeParams() {
    return gson.fromJson(assemblyTimeParams, NormalDistributionConfig.class);
  }

  public void setAssemblyTimeParams(NormalDistributionConfig assemblyTimeParams) {
    this.assemblyTimeParams = gson.toJson(assemblyTimeParams);
  }

  public NormalDistributionConfig getErrorCheckTimeParams() {
    return gson.fromJson(errorCheckTimeParams, NormalDistributionConfig.class);
  }

  public void setErrorCheckTimeParams(NormalDistributionConfig errorCheckTimeParams) {
    this.errorCheckTimeParams = gson.toJson(errorCheckTimeParams);
  }

  public ErrorOccurrenceConfig getErrorOccurrenceParams() {
    return gson.fromJson(errorOccurrenceParams, ErrorOccurrenceConfig.class);
  }

  public void setErrorOccurrenceParams(ErrorOccurrenceConfig errorOccurrenceParams) {
    this.errorOccurrenceParams = gson.toJson(errorOccurrenceParams);
  }

  public ErrorFixTimeConfig getErrorFixTimes() {
    return gson.fromJson(errorFixTimes, ErrorFixTimeConfig.class);
  }

  public void setErrorFixTimes(ErrorFixTimeConfig errorFixTimes) {
    this.errorFixTimes = gson.toJson(errorFixTimes);
  }

  public StationConfig getStationParams() {
    return gson.fromJson(stationParams, StationConfig.class);
  }

  public void setStationParams(StationConfig stationParams) {
    this.stationParams = gson.toJson(stationParams);
  }

  public void setStations(Station[] stations) {
    this.stations.addAll(Arrays.asList(stations));
  }

  public void setMaterials(Material[] materials) {
    this.materials.addAll(Arrays.asList(materials));
  }
}
