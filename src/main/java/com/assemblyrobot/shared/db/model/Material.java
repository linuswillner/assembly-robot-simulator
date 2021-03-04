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
@Table(name = "materials")
@NoArgsConstructor
public class Material {
  @Id
  @Getter
  @Setter
  @Column(nullable = false)
  private String id;

  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(nullable = false, name = "run_id")
  private Run run;

  @Getter
  @Setter
  @Column(nullable = false, name = "material_queue_start_time")
  private double queueStartTime;

  @Getter
  @Setter
  @Column(nullable = false, name = "material_queue_end_time")
  private double queueEndTime;

  @Getter
  @Setter
  @Column(nullable = false, name = "material_queue_duration")
  private double queueDuration;

  @Getter
  @Setter
  @Column(nullable = false, name = "material_processing_start_time")
  private double processingStartTime;

  @Getter
  @Setter
  @Column(nullable = false, name = "material_processing_end_time")
  private double processingEndTime;

  @Getter
  @Setter
  @Column(nullable = false, name = "material_processing_duration")
  private double processingDuration;

  @Getter
  @Setter
  @Column(nullable = false, name = "material_passthrough_time")
  private double passthroughTime;

  public Material(
      String id,
      double queueStartTime,
      double queueEndTime,
      double queueDuration,
      double processingStartTime,
      double processingEndTime,
      double processingDuration,
      double passthroughTime) {
    this.id = id;
    this.queueStartTime = queueStartTime;
    this.queueEndTime = queueEndTime;
    this.queueDuration = queueDuration;
    this.processingStartTime = processingStartTime;
    this.processingEndTime = processingEndTime;
    this.processingDuration = processingDuration;
    this.passthroughTime = passthroughTime;
  }
}
