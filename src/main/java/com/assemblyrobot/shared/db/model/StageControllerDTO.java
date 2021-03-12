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
@Table(name = "stage_controllers")
@NoArgsConstructor
public class StageControllerDTO {
  @Id
  @Getter
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(nullable = false, name = "run_id")
  private RunDTO run;

  @Getter
  @Column(nullable = false, name = "total_entered_material_amount")
  private double totalEnteredMaterialAmount;

  @Getter
  @Column(nullable = false, name = "total_exited_material_amount")
  private double totalExitedMaterialAmount;

  public StageControllerDTO(double totalEnteredMaterialAmount, double totalExitedMaterialAmount) {
    this.totalEnteredMaterialAmount = totalEnteredMaterialAmount;
    this.totalExitedMaterialAmount = totalExitedMaterialAmount;
  }
}
