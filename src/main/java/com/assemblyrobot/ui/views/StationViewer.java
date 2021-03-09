package com.assemblyrobot.ui.views;

import com.assemblyrobot.ui.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import lombok.Setter;

public class StationViewer implements Initializable, View {
  @Setter private Main main;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}
}
