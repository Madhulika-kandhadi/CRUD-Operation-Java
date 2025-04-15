package com.talentreef.interviewquestions.takehome.services;

import com.talentreef.interviewquestions.takehome.exceptions.WidgetAlreadyExistsException;
import com.talentreef.interviewquestions.takehome.exceptions.WidgetNotFoundException;
import com.talentreef.interviewquestions.takehome.models.Widget;
import com.talentreef.interviewquestions.takehome.models.WidgetDTO;
import com.talentreef.interviewquestions.takehome.repositories.WidgetRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WidgetServiceImpl implements WidgetService {

    private static final Logger logger = LoggerFactory.getLogger(WidgetServiceImpl.class);
    private final WidgetRepository widgetRepository;

    public WidgetServiceImpl(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public List<WidgetDTO> getAllWidgets() {
        logger.info("Fetching all widgets");
        List<WidgetDTO> widgets = widgetRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved {} widgets", widgets.size());
        return widgets;
    }

    @Override
    public WidgetDTO getWidgetByName(String name) {
        logger.info("Fetching widget with name: {}", name);
        Widget widget = widgetRepository.findByName(name)
                .orElseThrow(() -> {
                    logger.error("Widget not found with name: {}", name);
                    return new IllegalArgumentException("Widget not found");
                });
        logger.info("Successfully fetched widget: {}", name);
        return toDTO(widget);
    }

    @Transactional
    @Override
    public WidgetDTO createWidget(WidgetDTO dto) {
        logger.info("Creating widget with name: {}", dto.getName());
        if (widgetRepository.existsByName(dto.getName())) {
            logger.warn("Widget already exists with name: {}", dto.getName());
            throw new WidgetAlreadyExistsException("Widget with name '" + dto.getName() + "' already exists.");
        }

        Widget saved = widgetRepository.save(toEntity(dto));
        logger.info("Successfully created widget: {}", saved.getName());
        return toDTO(saved);
    }

    @Transactional
    @Override
    public WidgetDTO updateWidget(String name, WidgetDTO dto) {
        logger.info("Updating widget with name: {}", name);
        Widget existing = widgetRepository.findByName(name)
                .orElseThrow(() -> {
                    logger.error("Widget not found with name: {}", name);
                    return new IllegalArgumentException("Widget not found");
                });

        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());

        Widget updated = widgetRepository.save(existing);
        logger.info("Successfully updated widget: {}", name);
        return toDTO(updated);
    }

    @Transactional
    @Override
    public void deleteWidget(String name) {
        logger.info("Deleting widget with name: {}", name);
        if (!widgetRepository.existsByName(name)) {
            logger.error("Widget not found with name: {}", name);
            throw new WidgetNotFoundException("Widget with name '" + name + "' not found");
        }
        widgetRepository.deleteByName(name);
        logger.info("Successfully deleted widget: {}", name);
    }

    // --- Helpers ---
    WidgetDTO toDTO(Widget widget) {
        logger.debug("Converting widget entity to DTO: {}", widget.getName());
        return WidgetDTO.builder()
                .id(widget.getId())
                .name(widget.getName())
                .description(widget.getDescription())
                .price(widget.getPrice())
                .build();
    }

    private Widget toEntity(WidgetDTO dto) {
        logger.debug("Converting widget DTO to entity: {}", dto.getName());
        return Widget.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }
}