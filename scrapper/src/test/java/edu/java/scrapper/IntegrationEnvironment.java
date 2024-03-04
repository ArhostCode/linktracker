package edu.java.scrapper;

import java.nio.file.Path;
import liquibase.Liquibase;
import liquibase.UpdateSummaryEnum;
import liquibase.UpdateSummaryOutputEnum;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.ChangeExecListenerCommandStep;
import liquibase.command.core.helpers.DatabaseChangelogCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.command.core.helpers.ShowSummaryArgument;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.SneakyThrows;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class IntegrationEnvironment {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    @SneakyThrows
    private static void runMigrations(JdbcDatabaseContainer<?> container) {
        Database database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(container.createConnection("")));
        Liquibase liquibase = new Liquibase(
            "master.xml",
            new DirectoryResourceAccessor(Path.of("migrations")),
            database
        );
        updateCommand(liquibase).execute();
    }

    @SneakyThrows
    private static CommandScope updateCommand(Liquibase liquibase) {
        CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
        updateCommand.addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, liquibase.getDatabase());
        updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_ARG, liquibase.getDatabaseChangeLog());
        updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, liquibase.getChangeLogFile());
        updateCommand.addArgumentValue(UpdateCommandStep.CONTEXTS_ARG, "");
        updateCommand.addArgumentValue(UpdateCommandStep.LABEL_FILTER_ARG, "");
        updateCommand.addArgumentValue(
            ChangeExecListenerCommandStep.CHANGE_EXEC_LISTENER_ARG,
            liquibase.getDefaultChangeExecListener()
        );
        updateCommand.addArgumentValue(ShowSummaryArgument.SHOW_SUMMARY_OUTPUT, UpdateSummaryOutputEnum.LOG);
        updateCommand.addArgumentValue(
            DatabaseChangelogCommandStep.CHANGELOG_PARAMETERS,
            liquibase.getChangeLogParameters()
        );
        updateCommand.addArgumentValue(ShowSummaryArgument.SHOW_SUMMARY, UpdateSummaryEnum.VERBOSE);
        return updateCommand;
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
