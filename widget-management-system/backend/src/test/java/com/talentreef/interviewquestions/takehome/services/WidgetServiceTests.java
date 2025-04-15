package com.talentreef.interviewquestions.takehome.services;

import com.talentreef.interviewquestions.takehome.exceptions.WidgetAlreadyExistsException;
import com.talentreef.interviewquestions.takehome.exceptions.WidgetNotFoundException;
import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.models.WidgetDTO;
import com.talentreef.interviewquestions.takehome.repositories.WidgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WidgetServiceTests {

  @Mock
  private WidgetRepository widgetRepository;

  @InjectMocks
  private WidgetServiceImpl widgetService;

  private WidgetDTO sampleWidgetDTO;

  private Widget sampleWidget;

  @BeforeEach
  void setUp() {
    sampleWidgetDTO = WidgetDTO.builder()
            .id(1L)
            .name("Sample Widget")
            .description("A sample widget for testing.")
            .price(new BigDecimal("99.99"))
            .build();

    sampleWidget = Widget.builder()
            .id(1L)
            .name("Sample Widget")
            .description("A sample widget for testing.")
            .price(new BigDecimal("99.99"))
            .build();
  }

  // getAllWidgets
  @Test
  public void getAllWidgets_returnsList() {
    // Arrange
    Widget anotherWidget = Widget.builder()
            .id(2L)
            .name("Another Widget")
            .description("Another test widget.")
            .price(new BigDecimal("149.99"))
            .build();
    when(widgetRepository.findAll()).thenReturn(List.of(sampleWidget, anotherWidget));

    // Act
    List<WidgetDTO> result = widgetService.getAllWidgets();

    // Assert
    assertEquals(2, result.size());
    assertEquals("Sample Widget", result.get(0).getName());
    assertEquals("Another Widget", result.get(1).getName());
    verify(widgetRepository, times(1)).findAll();
  }

  @Test
  public void getAllWidgets_emptyList() {
    // Arrange
    when(widgetRepository.findAll()).thenReturn(List.of());

    // Act
    List<WidgetDTO> result = widgetService.getAllWidgets();

    // Assert
    assertTrue(result.isEmpty());
    verify(widgetRepository, times(1)).findAll();
  }

  // getWidgetByName
  @Test
  public void getWidgetByName_success() {
    // Arrange
    when(widgetRepository.findByName("Sample Widget")).thenReturn(Optional.of(sampleWidget));

    // Act
    Optional<WidgetDTO> result = Optional.ofNullable(widgetService.getWidgetByName("Sample Widget"));

    // Assert
    assertTrue(result.isPresent());
    assertEquals("Sample Widget", result.get().getName());
    assertEquals(new BigDecimal("99.99"), result.get().getPrice());
    verify(widgetRepository, times(1)).findByName("Sample Widget");
  }

  @Test
  public  void getWidgetByName_notFound() {
    // Arrange
    when(widgetRepository.findByName("NonExistent")).thenReturn(Optional.empty());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            widgetService.getWidgetByName("NonExistent"));

    assertEquals("Widget not found", exception.getMessage());
    verify(widgetRepository, times(1)).findByName("NonExistent");
  }

//  @Test
//  public void getWidgetByName_emptyName() {
//    // Act & Assert
//    assertThrows(IllegalArgumentException.class, () ->
//            widgetService.getWidgetByName(""));
//    verify(widgetRepository, never()).findByName(anyString());
//  }


  // createWidget
  @Test
  public void createWidget_success() {
    // Arrange
    when(widgetRepository.existsByName("Sample Widget")).thenReturn(false);
    when(widgetRepository.save(any(Widget.class))).thenReturn(sampleWidget);

    // Act
    WidgetDTO result = widgetService.createWidget(sampleWidgetDTO);

    // Assert
    assertNotNull(result);
    assertEquals("Sample Widget", result.getName());
    assertEquals(new BigDecimal("99.99"), result.getPrice());
    verify(widgetRepository, times(1)).existsByName("Sample Widget");
    verify(widgetRepository, times(1)).save(any(Widget.class));
  }

  @Test
  public  void createWidget_duplicateName() {
    // Arrange
    when(widgetRepository.existsByName("Sample Widget")).thenReturn(true);

    // Act & Assert
    WidgetAlreadyExistsException exception = assertThrows(WidgetAlreadyExistsException.class, () ->
            widgetService.createWidget(sampleWidgetDTO));
    assertEquals("Widget with name 'Sample Widget' already exists.", exception.getMessage());
    verify(widgetRepository, times(1)).existsByName("Sample Widget");
    verify(widgetRepository, never()).save(any(Widget.class));
  }

  @Test
  public void createWidget_boundaryNameLength() {
    // Arrange
    String longName = "a".repeat(100); // Max length
    WidgetDTO boundaryWidget = WidgetDTO.builder()
            .name(longName)
            .description("Valid description.")
            .price(new BigDecimal("100.00"))
            .build();
    when(widgetRepository.existsByName(longName)).thenReturn(false);
    when(widgetRepository.save(any(Widget.class))).thenReturn(toEntity(boundaryWidget));

    // Act
    WidgetDTO result = widgetService.createWidget(boundaryWidget);

    // Assert
    assertEquals(longName, result.getName());
    verify(widgetRepository, times(1)).existsByName(longName);
    verify(widgetRepository, times(1)).save(any(Widget.class));
  }

  // updateWidget
  @Test
  public void updateWidget_success_bothFields() {
    // Arrange
    when(widgetRepository.findByName("Sample Widget")).thenReturn(Optional.of(sampleWidget));
    when(widgetRepository.save(any(Widget.class))).thenReturn(sampleWidget);

    // Act
    WidgetDTO result = widgetService.updateWidget("Sample Widget", sampleWidgetDTO);

    // Assert
    assertEquals("A sample widget for testing.", result.getDescription());
    assertEquals(new BigDecimal("99.99"), result.getPrice());
    verify(widgetRepository, times(1)).findByName("Sample Widget");
    verify(widgetRepository, times(1)).save(any(Widget.class));
  }

  @Test
  public void updateWidget_success_partialUpdate() {
    // Arrange
    when(widgetRepository.findByName("Sample Widget")).thenReturn(Optional.of(sampleWidget));
    when(widgetRepository.save(any(Widget.class))).thenReturn(sampleWidget);

    // Act
    WidgetDTO result = widgetService.updateWidget("Sample Widget", sampleWidgetDTO);

    // Assert
    assertEquals("A sample widget for testing.", result.getDescription());
    assertEquals(new BigDecimal("99.99"), result.getPrice()); // Unchanged
    verify(widgetRepository, times(1)).findByName("Sample Widget");
    verify(widgetRepository, times(1)).save(any(Widget.class));
  }

  @Test
  public void updateWidget_notFound() {
    // Arrange
    when(widgetRepository.findByName("NonExistent")).thenReturn(Optional.empty());

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            widgetService.updateWidget("NonExistent", sampleWidgetDTO));
    assertEquals("Widget not found", exception.getMessage());
    verify(widgetRepository, times(1)).findByName("NonExistent");
    verify(widgetRepository, never()).save(any(Widget.class));
  }

  @Test
  public void updateWidget_boundaryPrice() {
    // Arrange
    when(widgetRepository.findByName("Sample Widget")).thenReturn(Optional.of(sampleWidget));
    when(widgetRepository.save(any(Widget.class))).thenReturn(sampleWidget);

    // Act
    WidgetDTO result = widgetService.updateWidget("Sample Widget", sampleWidgetDTO);

    // Assert
    assertEquals(new BigDecimal("99.99"), result.getPrice());
    verify(widgetRepository, times(1)).findByName("Sample Widget");
    verify(widgetRepository, times(1)).save(any(Widget.class));
  }

  // deleteWidget
  @Test
  void deleteWidget_existingName_deletesSuccessfully() {
    // Arrange
    String name = "SampleWidget";
    when(widgetRepository.existsByName(name)).thenReturn(true);

    // Act
    widgetService.deleteWidget(name);

    // Assert
    verify(widgetRepository, times(1)).deleteByName(name);
  }

  @Test
  public void deleteWidget_emptyName() {
    // Act & Assert
    assertThrows(WidgetNotFoundException.class, () ->
            widgetService.deleteWidget(""));
    verify(widgetRepository, never()).deleteByName(anyString());
  }

  WidgetDTO toDTO(Widget widget) {
    return WidgetDTO.builder()
            .id(widget.getId())
            .name(widget.getName())
            .description(widget.getDescription())
            .price(widget.getPrice())
            .build();
  }

  private Widget toEntity(WidgetDTO dto) {
    return Widget.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .price(dto.getPrice())
            .build();
  }
}