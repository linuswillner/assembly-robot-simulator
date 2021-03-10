package com.assemblyrobot.ui.models;

import lombok.Getter;

public class TableData {

  @Getter
  private int queueLength;
  @Getter
  private String status;
  @Getter
  private String name;


  /**
   * this constructor is used to convert the {@link com.assemblyrobot.simulator.system.components.StationQueue}s
   * station objects to TableData objects that can be viewed in a table
   *
   * @param name   the name of the {@link com.assemblyrobot.simulator.system.components.Station}
   * @param status if the {@link com.assemblyrobot.simulator.system.components.Station} is handling
   *               a material, this is passed to the constructor as a boolean and converted to
   *               string for the table
   * @param q      number of materials in the stations queue
   */
  public TableData(String name, boolean status, int q) {
    this.name = name;
    this.queueLength = q;
    if (status) {
      this.status = "Busy";
    } else {
      this.status = "Idle";
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStatus(Boolean s) {
    if (s) {
      this.status = "Busy";
    } else {
      this.status = "Idle";
    }

  }


}
