package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.model.SystemConfig;

import java.util.List;

public interface SystemConfigService {

    String getValueByKey(String key);

    <T> T getValueAs(String key, Class<T> type);

    List<SystemConfig> getAllConfigs();

    List<SystemConfig> getConfigsByGroup(String group);

    SystemConfig updateConfigValue(String key, String newValue);
}
