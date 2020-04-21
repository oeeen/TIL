package dev.smjeon.til.generic;

import java.sql.ResultSet;

public interface RowMapper<T> {
    T mapRow(ResultSet rs) throws Exception;
}
