package com.assemblyrobot.ui.views;

import com.assemblyrobot.shared.constants.Version;
import com.assemblyrobot.ui.Main;
import com.assemblyrobot.ui.utils.LinkUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;

public class About implements Initializable, View {
  @Setter private Main main;
  @Setter private Stage stage;

  @FXML private Text version;
  @FXML private Hyperlink githubLink;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    version.setText("Version %s".formatted(Version.semVer));

    githubLink.setOnAction(
        event ->
            LinkUtils.openURLInBrowser("https://github.com/linuswillner/assembly-robot-simulator"));
  }

  @FXML
  public void handleCloseButtonClick() {
    stage.close();
  }

  @Override
  public void afterInitialize() {}
}
