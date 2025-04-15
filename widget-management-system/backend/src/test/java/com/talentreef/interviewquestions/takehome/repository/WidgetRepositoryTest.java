package com.talentreef.interviewquestions.takehome.repository;

import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.repositories.WidgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WidgetRepositoryTest {

    @Autowired
    private WidgetRepository widgetRepository;

    private Widget sampleWidget;

    @BeforeEach
    void setUp() {
        sampleWidget = Widget.builder()
                .name("Sample Widget")
                .description("A sample widget for testing.")
                .price(new BigDecimal("99.99"))
                .build();
    }

    @Test
   public void saveAndFindById_success() {
        // Act
        Widget savedWidget = widgetRepository.save(sampleWidget);
        Optional<Widget> foundWidget = widgetRepository.findById(savedWidget.getId());

        // Assert
        assertTrue(foundWidget.isPresent());
        assertEquals("Sample Widget", foundWidget.get().getName());
        assertEquals(new BigDecimal("99.99"), foundWidget.get().getPrice());
    }

    @Test
    public void findByName_success() {
        // Arrange
        widgetRepository.save(sampleWidget);

        // Act
        Optional<Widget> result = widgetRepository.findByName("Sample Widget");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Sample Widget", result.get().getName());
        assertEquals("A sample widget for testing.", result.get().getDescription());
    }

    @Test
    public void findByName_notFound() {
        // Act
        Optional<Widget> result = widgetRepository.findByName("NonExistent");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void existsByName_true() {
        // Arrange
        widgetRepository.save(sampleWidget);

        // Act
        boolean exists = widgetRepository.existsByName("Sample Widget");

        // Assert
        assertTrue(exists);
    }

    @Test
    public  void existsByName_false() {
        // Act
        boolean exists = widgetRepository.existsByName("NonExistent");

        // Assert
        assertFalse(exists);
    }

    @Test
    public  void deleteByName_success() {
        // Arrange
        Widget savedWidget = widgetRepository.save(sampleWidget);

        // Act
        widgetRepository.deleteByName("Sample Widget");
        Optional<Widget> result = widgetRepository.findByName("Sample Widget");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteByName_nonExistent() {
        // Act
        widgetRepository.deleteByName("NonExistent");

        // Assert (no exception expected, operation is idempotent)
        assertFalse(widgetRepository.existsByName("NonExistent"));
    }

    @Test
    public void findAll_multipleWidgets() {
        // Arrange
        Widget anotherWidget = Widget.builder()
                .name("Another Widget")
                .description("Another test widget.")
                .price(new BigDecimal("149.99"))
                .build();
        widgetRepository.save(sampleWidget);
        widgetRepository.save(anotherWidget);

        // Act
        List<Widget> result = widgetRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(w -> w.getName().equals("Sample Widget")));
        assertTrue(result.stream().anyMatch(w -> w.getName().equals("Another Widget")));
    }

    @Test
    public void uniqueNameConstraint_violation() {
        // Arrange
        widgetRepository.save(sampleWidget);
        Widget duplicateWidget = Widget.builder()
                .name("Sample Widget")
                .description("Different description.")
                .price(new BigDecimal("50.00"))
                .build();

        // Act & Assert
        assertThrows(Exception.class, () -> widgetRepository.saveAndFlush(duplicateWidget));
    }

    @Test
    public void boundaryNameLength_save() {
        // Arrange
        String longName = "a".repeat(100);
        Widget boundaryWidget = Widget.builder()
                .name(longName)
                .description("Valid description.")
                .price(new BigDecimal("100.00"))
                .build();

        // Act
        Widget savedWidget = widgetRepository.save(boundaryWidget);
        Optional<Widget> result = widgetRepository.findByName(longName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(longName, result.get().getName());
    }

    @Test
    public void boundaryPrice_save() {
        // Arrange
        Widget minPriceWidget = Widget.builder()
                .name("Min Price Widget")
                .description("Widget with minimum price.")
                .price(new BigDecimal("1.00"))
                .build();
        Widget maxPriceWidget = Widget.builder()
                .name("Max Price Widget")
                .description("Widget with maximum price.")
                .price(new BigDecimal("20000.00"))
                .build();

        // Act
        widgetRepository.save(minPriceWidget);
        widgetRepository.save(maxPriceWidget);

        // Assert
        Optional<Widget> minResult = widgetRepository.findByName("Min Price Widget");
        Optional<Widget> maxResult = widgetRepository.findByName("Max Price Widget");
        assertTrue(minResult.isPresent());
        assertEquals(new BigDecimal("1.00"), minResult.get().getPrice());
        assertTrue(maxResult.isPresent());
        assertEquals(new BigDecimal("20000.00"), maxResult.get().getPrice());
    }


}