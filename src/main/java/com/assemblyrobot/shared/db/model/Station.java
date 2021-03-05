package com.assemblyrobot.shared.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stations")
@NoArgsConstructor
public class Station {
  @Id
  @Getter
  @Setter
  @Column(nullable = false, length = 64)
  private String id;

  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(nullable = false, name = "run_id")
  private Run run;

  @Getter
  @Setter
  @Column(nullable = false, name = "station_entered_material_amount")
  private double enteredMaterialAmount;

  @Getter
  @Setter
  @Column(nullable = false, name = "station_exited_material_amount")
  private double exitedMaterialAmount;

  @Getter
  @Setter
  @Column(nullable = false, name = "station_busy_time")
  private double busyTime;

  @Getter
  @Setter
  @Column(nullable = false, name = "station_total_passthrough_time")
  private double totalPassthroughTime;

  public Station(
      String id,
      double enteredMaterialAmount,
      double exitedMaterialAmount,
      double busyTime,
      double totalPassthroughTime) {
    this.id = id;
    this.enteredMaterialAmount = enteredMaterialAmount;
    this.exitedMaterialAmount = exitedMaterialAmount;
    this.busyTime = busyTime;
    this.totalPassthroughTime = totalPassthroughTime;
  }
}
