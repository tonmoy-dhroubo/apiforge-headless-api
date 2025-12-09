package com.apiforge.content.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DynamicContentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> create(String tableName, Map<String, Object> data) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<Object> params = new java.util.ArrayList<>();

        data.forEach((key, value) -> {
            if (columns.length() > 0) {
                columns.append(", ");
                values.append(", ");
            }
            columns.append(key);
            values.append("?");
            params.add(value);
        });

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s) RETURNING *",
                tableName, columns, values);

        return jdbcTemplate.queryForMap(sql, params.toArray());
    }

    public List<Map<String, Object>> findAll(String tableName) {
        String sql = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> findById(String tableName, Long id) {
        String sql = String.format("SELECT * FROM %s WHERE id = ?", tableName);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public Map<String, Object> update(String tableName, Long id, Map<String, Object> data) {
        StringBuilder setClause = new StringBuilder();
        List<Object> params = new java.util.ArrayList<>();

        data.forEach((key, value) -> {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(key).append(" = ?");
            params.add(value);
        });

        params.add(id);

        String sql = String.format("UPDATE %s SET %s, updated_at = CURRENT_TIMESTAMP WHERE id = ? RETURNING *",
                tableName, setClause);

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, params.toArray());
        return results.isEmpty() ? null : results.get(0);
    }

    public void delete(String tableName, Long id) {
        String sql = String.format("DELETE FROM %s WHERE id = ?", tableName);
        jdbcTemplate.update(sql, id);
    }

    public List<Map<String, Object>> findWithFilters(String tableName, Map<String, Object> filters) {
        StringBuilder whereClause = new StringBuilder();
        List<Object> params = new java.util.ArrayList<>();

        filters.forEach((key, value) -> {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(key).append(" = ?");
            params.add(value);
        });

        String sql = String.format("SELECT * FROM %s WHERE %s", tableName, whereClause);
        return jdbcTemplate.queryForList(sql, params.toArray());
    }
}
