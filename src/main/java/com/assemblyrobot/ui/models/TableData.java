package com.assemblyrobot.ui.models;

public class TableData {

  private int queueLength;
  private String status;
  private String name;

  public TableData(String name, String status, int q) {
    this.name = name;
    this.queueLength = q;
    this.status = status;
  }

  public int getQueueLength() {
    return queueLength;
  }

  public void setQueueLength(int queueLength) {
    this.queueLength = queueLength;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStatus(long s) {
    if (s > 0) {
      this.status = "Busy";
    } else {
      this.status = "Idle";
    }
  }


}
