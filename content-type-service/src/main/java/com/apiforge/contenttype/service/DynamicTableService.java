package com.apiforge.contenttype.service;

import com.apiforge.contenttype.model.Field;
import com.apiforge.contenttype.model.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicTableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTableForContentType(String tableName, List<Field> fields) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        sql.append("id BIGSERIAL PRIMARY KEY, ");
        sql.append("created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, ");
        sql.append("updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");

        for (Field field : fields) {
            sql.append(", ");
            sql.append(field.getFieldName()).append(" ");
            sql.append(mapFieldTypeToSql(field.getType()));

            if (field.getRequired() != null && field.getRequired()) {
                sql.append(" NOT NULL");
            }

            if (field.getUnique() != null && field.getUnique()) {
                sql.append(" UNIQUE");
            }
        }

        sql.append(")");

        jdbcTemplate.execute(sql.toString());
    }

    public void dropTableForContentType(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName + " CASCADE";
        jdbcTemplate.execute(sql);
    }

    public void addColumnToTable(String tableName, Field field) {
        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(tableName);
        sql.append(" ADD COLUMN IF NOT EXISTS ");
        sql.append(field.getFieldName()).append(" ");
        sql.append(mapFieldTypeToSql(field.getType()));

        if (field.getRequired() != null && field.getRequired()) {
            sql.append(" NOT NULL");
        }

        if (field.getUnique() != null && field.getUnique()) {
            sql.append(" UNIQUE");
        }

        jdbcTemplate.execute(sql.toString());
    }

    public void removeColumnFromTable(String tableName, String columnName) {
        String sql = "ALTER TABLE " + tableName + " DROP COLUMN IF EXISTS " + columnName;
        jdbcTemplate.execute(sql);
    }

    private String mapFieldTypeToSql(FieldType fieldType) {
        return switch (fieldType) {
            case SHORT_TEXT -> "VARCHAR(255)";
            case LONG_TEXT -> "TEXT";
            case RICH_TEXT -> "TEXT";
            case NUMBER -> "NUMERIC";
            case BOOLEAN -> "BOOLEAN";
            case DATETIME -> "TIMESTAMP";
            case MEDIA -> "BIGINT";
            case RELATION -> "BIGINT";
        };
    }
}
