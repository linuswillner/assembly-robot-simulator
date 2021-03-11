package com.assemblyrobot.ui.controllers;

import com.assemblyrobot.shared.db.dao.RunDAO;
import com.assemblyrobot.shared.db.model.RunDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.val;

public class DatabaseViewerController {
  private final RunDAO dao = RunDAO.getInstance();
  private Set<RunDTO> runs;

  /**
   * Returns a list of {@link RunDTO}s sorted in ascending order.
   *
   * @return {@link List}&lt;{@link RunDTO}&gt;
   */
  public List<RunDTO> getRuns() {
    val runArray = new ArrayList<>(runs);
    runArray.sort(Comparator.comparing(RunDTO::getId));
    return runArray;
  }

  /**
   * Updates the run list based on information in the database.
   */
  public void updateRuns() {
    runs = Arrays.stream(dao.getAllRuns()).collect(Collectors.toSet());
  }
}
