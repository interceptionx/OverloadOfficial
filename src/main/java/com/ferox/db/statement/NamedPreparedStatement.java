package com.ferox.db.statement;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;

public final class NamedPreparedStatement implements PreparedStatement {
    private final Query query;
    private final PreparedStatement statement;

    private NamedPreparedStatement(Query query, PreparedStatement statement) {
        this.query = query;
        this.statement = statement;
    }

    public static NamedPreparedStatement create(Connection connection, String sql) throws SQLException {
        Query query = Query.fromSql(sql);
        return new NamedPreparedStatement(query, connection.prepareStatement(query.getSql()));
    }

    public static NamedPreparedStatement create(Connection connection, String sql, int autoGeneratedKeys) throws SQLException {
        Query query = Query.fromSql(sql);
        return new NamedPreparedStatement(query, connection.prepareStatement(query.getSql(), autoGeneratedKeys));
    }

    public static NamedPreparedStatement create(Connection connection, String sql, int[] columnIndexes) throws SQLException {
        Query query = Query.fromSql(sql);
        return new NamedPreparedStatement(query, connection.prepareStatement(query.getSql(), columnIndexes));
    }

    public static NamedPreparedStatement create(Connection connection, String sql, String[] columnNames) throws SQLException {
        Query query = Query.fromSql(sql);
        return new NamedPreparedStatement(query, connection.prepareStatement(query.getSql(), columnNames));
    }

    public static NamedPreparedStatement create(Connection connection, String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        Query query = Query.fromSql(sql);
        return new NamedPreparedStatement(query, connection.prepareStatement(query.getSql(), resultSetType, resultSetConcurrency));
    }

    public static NamedPreparedStatement create(Connection connection, String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        Query query = Query.fromSql(sql);
        return new NamedPreparedStatement(query, connection.prepareStatement(query.getSql(), resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    public void setNull(String parameter, int sqlType) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setNull(i, sqlType);
        }
        query.updateValue(parameter, "NULL");
    }

    public void setBoolean(String parameter, boolean x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setBoolean(i, x);
        }
        query.updateValue(parameter, Boolean.toString(x));
    }

    public void setByte(String parameter, byte x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setByte(i, x);
        }
        query.updateValue(parameter, Byte.toString(x));
    }

    public void setShort(String parameter, short x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setShort(i, x);
        }
        query.updateValue(parameter, Short.toString(x));
    }

    public void setInt(String parameter, int x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setInt(i, x);
        }
        query.updateValue(parameter, Integer.toString(x));
    }

    public void setLong(String parameter, long x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setLong(i, x);
        }
        query.updateValue(parameter, Long.toString(x));
    }

    public void setFloat(String parameter, float x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setFloat(i, x);
        }
        query.updateValue(parameter, Float.toString(x));
    }

    public void setDouble(String parameter, double x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setDouble(i, x);
        }
        query.updateValue(parameter, Double.toString(x));
    }

    public void setBigDecimal(String parameter, @NotNull BigDecimal x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setBigDecimal(i, x);
        }
        query.updateValue(parameter, x.toString());
    }

    public void setString(String parameter, @NotNull String x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setString(i, x);
        }
        query.updateValue(parameter, x);
    }

    public void setBytes(String parameter, @NotNull byte[] x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setBytes(i, x);
        }
        query.updateValue(parameter, Arrays.toString(x));
    }

    public void setDate(String parameter, @NotNull Date x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setDate(i, x);
        }
        query.updateValue(parameter, x.toString());
    }

    public void setTime(String parameter, @NotNull Time x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setTime(i, x);
        }
        query.updateValue(parameter, x.toString());
    }

    public void setTimestamp(String parameter, @NotNull Timestamp x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setTimestamp(i, x);
        }
        query.updateValue(parameter, x.toString());
    }

    public void setAsciiStream(String parameter, @NotNull InputStream x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setAsciiStream(i, x);
        }
        query.updateValue(parameter, x.toString());
    }

    @Deprecated
    public void setUnicodeStream(String parameter, @NotNull InputStream x, int length) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setUnicodeStream(i, x, length);
        }
        query.updateValue(parameter, x.toString());
    }

    public void setBinaryStream(String parameter, @NotNull InputStream x, int length) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setBinaryStream(i, x, length);
        }
        query.updateValue(parameter, x.toString());
    }

    public void setObject(String parameter, Object x, int targetSqlType, int scale) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setObject(i, x, targetSqlType, scale);
        }
        query.updateValue(parameter, x.toString());
    }

    public void setObject(String parameter, @NotNull Object x, int targetSqlType) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setObject(i, x, targetSqlType);
        }
        query.updateValue(parameter, x.toString());
    }

    public void setObject(String parameter, @NotNull Object x) throws SQLException {
        for (Integer i : query.indices(parameter)) {
            statement.setObject(i, x);
        }
        query.updateValue(parameter, x.toString());
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return statement.unwrap(iface);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return statement.isWrapperFor(iface);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return statement.executeUpdate(sql);
    }

    @Override
    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        statement.setNull(parameterIndex, sqlType);
    }

    @Override
    public void close() throws SQLException {
        statement.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return statement.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        statement.setMaxFieldSize(max);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        statement.setBoolean(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        statement.setByte(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        statement.setShort(parameterIndex, x);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return statement.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        statement.setMaxRows(max);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        statement.setInt(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        statement.setLong(parameterIndex, x);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        statement.setEscapeProcessing(enable);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        statement.setFloat(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        statement.setDouble(parameterIndex, x);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return statement.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        statement.setQueryTimeout(seconds);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        statement.setBigDecimal(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        statement.setString(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        statement.setBytes(parameterIndex, x);
    }

    @Override
    public void cancel() throws SQLException {
        statement.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return statement.getWarnings();
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        statement.setDate(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        statement.setTime(parameterIndex, x);
    }

    @Override
    public void clearWarnings() throws SQLException {
        statement.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        statement.setCursorName(name);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        statement.setTimestamp(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        statement.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return statement.execute(sql);
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        statement.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return statement.getResultSet();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        statement.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return statement.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return statement.getMoreResults();
    }

    @Override
    public void clearParameters() throws SQLException {
        statement.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return statement.getFetchDirection();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        statement.setFetchDirection(direction);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        statement.setObject(parameterIndex, x);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return statement.getFetchSize();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        statement.setFetchSize(rows);
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return statement.getResultSetConcurrency();
    }

    @Override
    public boolean execute() throws SQLException {
        return statement.execute();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return statement.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        statement.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        statement.clearBatch();
    }

    @Override
    public void addBatch() throws SQLException {
        statement.addBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return statement.executeBatch();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        statement.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        statement.setRef(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        statement.setBlob(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        statement.setClob(parameterIndex, x);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return statement.getConnection();
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        statement.setArray(parameterIndex, x);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return statement.getMetaData();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return statement.getMoreResults(current);
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        statement.setDate(parameterIndex, x, cal);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return statement.getGeneratedKeys();
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        statement.setTime(parameterIndex, x, cal);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        statement.setTimestamp(parameterIndex, x, cal);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        statement.setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return statement.executeUpdate(sql, columnIndexes);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        statement.setURL(parameterIndex, x);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return statement.executeUpdate(sql, columnNames);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return statement.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        statement.setRowId(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        statement.setNString(parameterIndex, value);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.execute(sql, autoGeneratedKeys);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        statement.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        statement.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        statement.setClob(parameterIndex, reader, length);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return statement.execute(sql, columnIndexes);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        statement.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        statement.setNClob(parameterIndex, reader, length);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return statement.execute(sql, columnNames);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        statement.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return statement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return statement.isClosed();
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return statement.isPoolable();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        statement.setPoolable(poolable);
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        statement.closeOnCompletion();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        statement.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return statement.isCloseOnCompletion();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        statement.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        statement.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        statement.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        statement.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        statement.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        statement.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        statement.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        statement.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        statement.setNClob(parameterIndex, reader);
    }

    @Override
    public String toString() {
        return query.toString();
    }
}
