package com.fpt.gsu25se47.schoolpsychology.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pagination")
public class PaginationProperties {
    private int defaultPageSize;
    private int defaultPageNumber;
}
