package com.talentreef.interviewquestions.takehome.services;

import com.talentreef.interviewquestions.takehome.models.WidgetDTO;

import java.util.List;

public interface WidgetService {

  List<WidgetDTO> getAllWidgets();

  WidgetDTO getWidgetByName(String name);

  WidgetDTO createWidget(WidgetDTO dto);

  WidgetDTO updateWidget(String name, WidgetDTO dto);

  void deleteWidget(String name);
}
