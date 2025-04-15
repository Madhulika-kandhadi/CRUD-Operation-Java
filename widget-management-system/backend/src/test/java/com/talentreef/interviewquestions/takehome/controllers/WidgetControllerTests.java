package com.talentreef.interviewquestions.takehome.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentreef.interviewquestions.takehome.exceptions.WidgetAlreadyExistsException;
import com.talentreef.interviewquestions.takehome.exceptions.WidgetNotFoundException;
import com.talentreef.interviewquestions.takehome.models.WidgetDTO;
import com.talentreef.interviewquestions.takehome.services.WidgetService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WidgetControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private WidgetService widgetService;

  @Autowired
  private ObjectMapper objectMapper;

  private WidgetDTO sampleWidgetDTO;

  @BeforeEach
  void setUp() {
    sampleWidgetDTO = WidgetDTO.builder()
            .id(1L)
            .name("Sample Widget")
            .description("A sample widget for testing.")
            .price(new BigDecimal("99.99"))
            .build();
  }

  @Test
  public void createWidget_success() throws Exception {
    // Arrange
    WidgetDTO inputDTO = WidgetDTO.builder()
            .name("Sample Widget")
            .description("A sample widget for testing.")
            .price(new BigDecimal("99.99"))
            .build();

    when(widgetService.createWidget(any(WidgetDTO.class))).thenReturn(sampleWidgetDTO);

    // Act & Assert
    mockMvc.perform(post("/v1/widgets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Sample Widget")))
            .andExpect(jsonPath("$.description", is("A sample widget for testing.")))
            .andExpect(jsonPath("$.price", is(99.99)));

    verify(widgetService, times(1)).createWidget(any(WidgetDTO.class));
  }

  @Test
  public void createWidget_duplicateName() throws Exception {
    // Arrange
    WidgetDTO inputDTO = WidgetDTO.builder()
            .name("Sample Widget")
            .description("A sample widget for testing.")
            .price(new BigDecimal("99.99"))
            .build();

    when(widgetService.createWidget(any(WidgetDTO.class)))
            .thenThrow(new WidgetAlreadyExistsException("Widget with name 'Sample Widget' already exists."));

    // Act & Assert
    mockMvc.perform(post("/v1/widgets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isConflict())
            .andExpect(content().string("Widget with name 'Sample Widget' already exists."));

    verify(widgetService, times(1)).createWidget(any(WidgetDTO.class));
  }

  @Test
  public void createWidget_invalidInput() throws Exception {
    // Arrange
    WidgetDTO invalidDTO = WidgetDTO.builder()
            .name("ab") // Too short
            .description("abc") // Too short
            .price(new BigDecimal("0.99")) // Below minimum
            .build();

    // Act & Assert
    mockMvc.perform(post("/v1/widgets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  public void createWidget_boundaryValues() throws Exception {
    // Arrange
    String longName = "a".repeat(100);
    String longDescription = "a".repeat(1000);
    WidgetDTO boundaryDTO = WidgetDTO.builder()
            .name(longName)
            .description(longDescription)
            .price(new BigDecimal("20000.00"))
            .build();

    WidgetDTO createdDTO = WidgetDTO.builder()
            .id(1L)
            .name(longName)
            .description(longDescription)
            .price(new BigDecimal("20000.00"))
            .build();

    when(widgetService.createWidget(any(WidgetDTO.class))).thenReturn(createdDTO);

    // Act & Assert
    mockMvc.perform(post("/v1/widgets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(boundaryDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is(longName)))
            .andExpect(jsonPath("$.description", is(longDescription)))
            .andExpect(jsonPath("$.price", is(20000.00)));

    verify(widgetService, times(1)).createWidget(any(WidgetDTO.class));
  }

  @Test
  public void createWidget_specialCharactersInName() throws Exception {
    // Arrange
    String specialName = "Widget@#$%";
    WidgetDTO specialDTO = WidgetDTO.builder()
            .name(specialName)
            .description("Widget with special characters.")
            .price(new BigDecimal("50.00"))
            .build();

    WidgetDTO createdDTO = WidgetDTO.builder()
            .id(1L)
            .name(specialName)
            .description("Widget with special characters.")
            .price(new BigDecimal("50.00"))
            .build();

    when(widgetService.createWidget(any(WidgetDTO.class))).thenReturn(createdDTO);

    // Act & Assert
    mockMvc.perform(post("/v1/widgets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(specialDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is(specialName)))
            .andExpect(jsonPath("$.price", is(50.00)));

    verify(widgetService, times(1)).createWidget(any(WidgetDTO.class));
  }

  @Test
  public void getAllWidgets_success() throws Exception {
    // Arrange
    WidgetDTO anotherWidgetDTO = WidgetDTO.builder()
            .id(2L)
            .name("Another Widget")
            .description("Another test widget.")
            .price(new BigDecimal("149.99"))
            .build();

    when(widgetService.getAllWidgets()).thenReturn(List.of(sampleWidgetDTO, anotherWidgetDTO));

    // Act & Assert
    mockMvc.perform(get("/v1/widgets")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Sample Widget")))
            .andExpect(jsonPath("$[0].price", is(99.99)))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("Another Widget")))
            .andExpect(jsonPath("$[1].price", is(149.99)));

    verify(widgetService, times(1)).getAllWidgets();
  }

  @Test
  public void getWidgetByName_success() throws Exception {
    // Arrange
    when(widgetService.getWidgetByName("Sample Widget")).thenReturn(sampleWidgetDTO);

    // Act & Assert
    mockMvc.perform(get("/v1/widgets/Sample Widget")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Sample Widget")))
            .andExpect(jsonPath("$.description", is("A sample widget for testing.")))
            .andExpect(jsonPath("$.price", is(99.99)));

    verify(widgetService, times(1)).getWidgetByName("Sample Widget");
  }

  @Test
  public void getWidgetByName_notFound() throws Exception {
    // Arrange
    when(widgetService.getWidgetByName("NonExistent"))
            .thenThrow(new IllegalArgumentException("Widget not found"));

    // Act & Assert
    mockMvc.perform(get("/v1/widgets/NonExistent")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Widget not found"));

    verify(widgetService, times(1)).getWidgetByName("NonExistent");
  }

  @Test
  public void updateWidget_success() throws Exception {
    // Arrange
    WidgetDTO updateDTO = WidgetDTO.builder()
            .name("Sample Widget")
            .description("Updated description.")
            .price(new BigDecimal("149.99"))
            .build();

    WidgetDTO updatedDTO = WidgetDTO.builder()
            .id(1L)
            .name("Sample Widget")
            .description("Updated description.")
            .price(new BigDecimal("149.99"))
            .build();

    when(widgetService.updateWidget(eq("Sample Widget"), any(WidgetDTO.class)))
            .thenReturn(updatedDTO);

    // Act & Assert
    mockMvc.perform(put("/v1/widgets/Sample Widget")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Sample Widget")))
            .andExpect(jsonPath("$.description", is("Updated description.")))
            .andExpect(jsonPath("$.price", is(149.99)));

    verify(widgetService, times(1)).updateWidget(eq("Sample Widget"), any(WidgetDTO.class));
  }

  @Test
  public void updateWidget_notFound() throws Exception {
    // Arrange
    WidgetDTO updateDTO = WidgetDTO.builder()
            .name("Sample Widget")
            .description("Updated description.")
            .price(new BigDecimal("149.99"))
            .build();

    when(widgetService.updateWidget(eq("NonExistent"), any(WidgetDTO.class)))
            .thenThrow(new IllegalArgumentException("Widget not found"));

    // Act & Assert
    mockMvc.perform(put("/v1/widgets/NonExistent")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO)))
            .andExpect(status().isNotFound());
  }

  @Test
  public void updateWidget_invalidDTO() throws Exception {
    // Arrange
    WidgetDTO invalidDTO = WidgetDTO.builder()
            .name("") // Triggers "Name cannot be blank"
            .description("abc") // Too short
            .price(new BigDecimal("0.99")) // Below minimum
            .build();

    // Act & Assert
    mockMvc.perform(put("/v1/widgets/Sample Widget")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[*]", hasItems(
                    "description: Description must be between 5 and 1000 characters",
                    "price: Price must be at least 1.00"
            )));


  }

  @Test
  public void updateWidget_minPrice() throws Exception {
    // Arrange
    WidgetDTO updateDTO = WidgetDTO.builder()
            .name("Sample Widget")
            .description("Min price widget.")
            .price(new BigDecimal("1.00"))
            .build();

    WidgetDTO updatedDTO = WidgetDTO.builder()
            .id(1L)
            .name("Sample Widget")
            .description("Min price widget.")
            .price(new BigDecimal("1.00"))
            .build();

    when(widgetService.updateWidget(eq("Sample Widget"), any(WidgetDTO.class)))
            .thenReturn(updatedDTO);

    // Act & Assert
    mockMvc.perform(put("/v1/widgets/Sample Widget")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.price", is(1.00)));
  }

  @Test
  public void deleteWidget_success() throws Exception {
    // Arrange
    doNothing().when(widgetService).deleteWidget("Sample Widget");

    // Act & Assert
    mockMvc.perform(delete("/v1/widgets/Sample Widget")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

  }

  @Test
  public void deleteWidget_notFound() throws Exception {
    // Arrange
    doThrow(new WidgetNotFoundException("Widget not found"))
            .when(widgetService).deleteWidget("NonExistent");

    // Act & Assert
    mockMvc.perform(delete("/v1/widgets/NonExistent")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(content().string("Widget not found"));

  }

  @Test
  public void deleteWidget_specialCharactersInName() throws Exception {
    // Arrange
    String specialName = "Widget@#$%";
    doNothing().when(widgetService).deleteWidget(specialName);
    // Act & Assert
    mockMvc.perform(delete("/v1/widgets/" + specialName)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
  }
}