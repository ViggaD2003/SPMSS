package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_config")
@Getter
@Setter
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_group")
    private String configGroup; // e.g., SURVEY, APPOINTMENT, SUPPORT_PROGRAM

    @Column(name = "config_key", unique = true, nullable = false)
    private String configKey;

    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    private String configValue;

    @Column(name = "value_type", nullable = false)
    private String valueType; // INTEGER, BOOLEAN, STRING, etc.

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_editable")
    private Boolean isEditable;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

