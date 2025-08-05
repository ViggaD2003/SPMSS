package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.model.SystemConfig;
import com.fpt.gsu25se47.schoolpsychology.repository.SystemConfigRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SystemConfigService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Override
    public String getValueByKey(String key) {
        return systemConfigRepository.findByConfigKey(key)
                .map(SystemConfig::getConfigValue)
                .orElseThrow(() -> new EntityNotFoundException("Config key not found: " + key));
    }

    @Override
    public <T> T getValueAs(String key, Class<T> type) {

        String value = getValueByKey(key);
        if (type == Boolean.class) {
            return type.cast(Boolean.parseBoolean(value));
        } else if (type == Integer.class) {
            return type.cast(Integer.parseInt(value));
        } else if (type == Double.class) {
            return type.cast(Double.parseDouble(value));
        } else if (type == String.class) {
            return type.cast(value);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type.getName());
        }
    }

    @Override
    public List<SystemConfig> getAllConfigs() {
        return systemConfigRepository.findAll();
    }

    @Override
    public List<SystemConfig> getConfigsByGroup(String group) {
        return systemConfigRepository.findAllByConfigGroup(group);
    }

    @Override
    public SystemConfig updateConfigValue(String key, String newValue) {

        SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElseThrow(() -> new EntityNotFoundException("Config key not found: " + key));

        if (Boolean.FALSE.equals(config.getIsEditable())) {
            throw new IllegalStateException("Config is not editable: " + key);
        }

        config.setConfigValue(newValue);
        config.setUpdatedAt(LocalDateTime.now());
        return systemConfigRepository.save(config);
    }
}
