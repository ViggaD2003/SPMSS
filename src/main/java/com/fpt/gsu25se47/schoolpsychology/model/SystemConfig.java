package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_config")
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "is_editable")
    private Boolean isEditable;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
