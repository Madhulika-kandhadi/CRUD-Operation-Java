package com.talentreef.interviewquestions.takehome.controllers;

import com.talentreef.interviewquestions.takehome.exceptions.WidgetAlreadyExistsException;
import com.talentreef.interviewquestions.takehome.exceptions.WidgetNotFoundException;
import com.talentreef.interviewquestions.takehome.models.WidgetDTO;
import com.talentreef.interviewquestions.takehome.services.WidgetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/widgets")
@Validated
public class WidgetController {

  private static final Logger logger = LoggerFactory.getLogger(WidgetController.class);
  private final WidgetService widgetService;

  public WidgetController(WidgetService widgetService) {
    this.widgetService = widgetService;
  }

  @GetMapping
  public ResponseEntity<List<WidgetDTO>> getAllWidgets() {
    logger.info("Fetching all widgets");
    List<WidgetDTO> widgets = widgetService.getAllWidgets();
    logger.info("Retrieved {} widgets", widgets.size());
    return ResponseEntity.ok(widgets);
  }

  @GetMapping("/{name}")
  public ResponseEntity<WidgetDTO> getWidgetByName(
          @PathVariable @Size(min = 3, max = 100) String name) {
    logger.info("Fetching widget with name: {}", name);
    WidgetDTO widget = widgetService.getWidgetByName(name);
    logger.info("Successfully fetched widget: {}", name);
    return ResponseEntity.ok(widget);
  }

  @PostMapping
  public ResponseEntity<?> createWidget(@Valid @RequestBody WidgetDTO widgetDTO) {
    logger.info("Creating widget with name: {}", widgetDTO.getName());
    try {
      WidgetDTO createdWidget = widgetService.createWidget(widgetDTO);
      logger.info("Successfully created widget: {}", createdWidget.getName());
      return ResponseEntity.status(HttpStatus.CREATED).body(createdWidget);
    } catch (WidgetAlreadyExistsException ex) {
      logger.warn("Failed to create widget: {}", ex.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
  }

  @PutMapping("/{name}")
  public ResponseEntity<WidgetDTO> updateWidget(
          @PathVariable @Size(min = 3, max = 100) String name,
          @RequestBody @Valid WidgetDTO widgetDTO) {
    logger.info("Updating widget with name: {}", name);
    WidgetDTO updatedWidget = widgetService.updateWidget(name, widgetDTO);
    logger.info("Successfully updated widget: {}", name);
    return ResponseEntity.ok(updatedWidget);
  }

  @DeleteMapping("/{name}")
  public ResponseEntity<?> deleteWidget(
          @PathVariable @Size(min = 3, max = 100) String name) {
    logger.info("Deleting widget with name: {}", name);
    try {
      widgetService.deleteWidget(name);
      logger.info("Successfully deleted widget: {}", name);
      return ResponseEntity.noContent().build();
    }
    catch (Exception ex) {
      logger.warn("Failed to delete widget: {}", ex.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
  }
}