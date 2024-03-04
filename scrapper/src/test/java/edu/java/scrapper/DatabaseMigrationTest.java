package edu.java.scrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseMigrationTest extends IntegrationEnvironment {

    @SneakyThrows @Test
    public void migrationChatShouldCorrectWork() {
        Connection connection = POSTGRES.createConnection("");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM tg_chat");
        Assertions.assertThat(statement.executeQuery().getMetaData().getColumnName(1)).isEqualTo("id");
    }

    @SneakyThrows @Test
    public void migrationLinkShouldCorrectWork() {
        Connection connection = POSTGRES.createConnection("");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM link");
        ResultSet resultSet = statement.executeQuery();
        Assertions.assertThat(resultSet.getMetaData().getColumnName(1)).isEqualTo("id");
        Assertions.assertThat(resultSet.getMetaData().getColumnName(2)).isEqualTo("url");
        Assertions.assertThat(resultSet.getMetaData().getColumnName(3)).isEqualTo("description");
        Assertions.assertThat(resultSet.getMetaData().getColumnName(4)).isEqualTo("updated_at");
    }

}
