package com.zeedoo.api.users.database;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * Handles {@link UUID} type
 *
 */
public class UUIDTypeHandler implements TypeHandler {

	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter,
			JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.toString());
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		return UUID.fromString(rs.getString(columnName));
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		return UUID.fromString(rs.getString(columnIndex));
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return UUID.fromString(cs.getString(columnIndex));
	}
}
