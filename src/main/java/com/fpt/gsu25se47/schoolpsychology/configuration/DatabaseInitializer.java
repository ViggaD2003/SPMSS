package com.fpt.gsu25se47.schoolpsychology.configuration;

import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            Resource resource = resourceLoader.getResource("classpath:data.sql");
            System.out.println(resource.getFilename());
            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, resource);
            }
        }
    }
} 