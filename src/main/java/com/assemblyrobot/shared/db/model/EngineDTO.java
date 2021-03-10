package com.assemblyrobot.shared.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "engines")
@NoArgsConstructor
public class EngineDTO {
  @Id
  @Getter
  @Column(nullable = false)
  @GeneratedValue
  private long id;

  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(nullable = false, name = "run_id")
  private RunDTO run;

  @Getter
  @Column(nullable = false, name = "total_simulation_time")
  private double totalSimulationTime;

  public EngineDTO(double totalSimulationTime) {
    this.totalSimulationTime = totalSimulationTime;
  }
}
