package com.assemblyrobot.ui.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lombok.val;

public abstract class AlertUtils {
  public static void showAlert(AlertType type, String title, String message) {
    val alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void showExceptionAlert(String title, String message, Exception e) {
    val alert = new Alert(AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);

    // Create expandable Exception.
    val stringWriter = new StringWriter();
    val printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    val exceptionText = stringWriter.toString();

    val label = new Label("The exception stack trace was:");

    val textArea = new TextArea(exceptionText);
    textArea.setEditable(false);
    textArea.setWrapText(true);

    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    val content = new GridPane();
    content.setMaxWidth(Double.MAX_VALUE);
    content.add(label, 0, 0);
    content.add(textArea, 0, 1);

    alert.getDialogPane().setExpandableContent(content);

    alert.showAndWait();
  }
}
