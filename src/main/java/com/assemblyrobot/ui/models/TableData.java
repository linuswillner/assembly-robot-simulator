package com.assemblyrobot.ui.models;

import lombok.Getter;

public class TableData {

  @Getter private int queueLength;
  @Getter private String status;
  @Getter private String name;

  public TableData(String name, boolean status, int q) {
    this.name = name;
    this.queueLength = q;
    if (status) {
      this.status = "Busy";
    } else {
      this.status = "Idle";
    }
  }

  public void setQueueLength(int queueLength) {
    this.queueLength = queueLength;
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
