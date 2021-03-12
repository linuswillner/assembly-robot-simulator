package com.assemblyrobot.shared.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class MaterialDTO {
  @Id
  @Getter
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long uid;

  @Getter
  @Column(nullable = false, length = 64)
  private String id;

  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(nullable = false, name = "run_id")
  private RunDTO run;

  @Getter
  @Column(nullable = false, name = "material_queue_start_time")
  private double queueStartTime;

  @Getter
  @Column(nullable = false, name = "material_queue_end_time")
  private double queueEndTime;

  @Getter
  @Column(nullable = false, name = "material_queue_duration")
  private double queueDuration;

  @Getter
  @Column(nullable = false, name = "material_processing_start_time")
  private double processingStartTime;

  @Getter
  @Column(nullable = false, name = "material_processing_end_time")
  private double processingEndTime;

  @Getter
  @Column(nullable = false, name = "material_processing_duration")
  private double processingDuration;

  @Getter
  @Column(nullable = false, name = "material_passthrough_time")
  private double passthroughTime;

  public MaterialDTO(
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
