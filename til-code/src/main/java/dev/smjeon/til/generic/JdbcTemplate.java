package dev.smjeon.til.generic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
    private DataSource dataSource;

    public <T> List<T> query(String query, RowMapper<T> rowMapper, Object... objects) {
        List<T> results = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, query, objects);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                T t = rowMapper.mapRow(rs);
                results.add(t);
            }
        } catch (Exception e) {
            logger.error("Error occurred while executing Query", e);
            throw new JdbcTemplateException(e);
        }
        return results;
    }

    private PreparedStatement createPreparedStatement(Connection con, String sql, Object... objects) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql);
        for (int i = 0; i < objects.length; i++) {
            pstmt.setObject(i + 1, objects[i]);
        }
        return pstmt;
    }
}
