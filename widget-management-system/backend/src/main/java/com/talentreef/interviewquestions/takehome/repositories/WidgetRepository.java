package com.talentreef.interviewquestions.takehome.repositories;

import com.talentreef.interviewquestions.takehome.models.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WidgetRepository extends JpaRepository<Widget, Long> {
  Optional<Widget> findByName(String name);
  void deleteByName(String name);
  boolean existsByName(String name);
}