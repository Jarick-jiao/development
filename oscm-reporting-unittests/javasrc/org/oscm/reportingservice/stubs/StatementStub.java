/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                             
 *                                                                              
 *  Creation Date: 19.02.2010                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.reportingservice.stubs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.oscm.internal.types.enumtypes.ServiceStatus;

/**
 * @author Mike J&auml;ger
 * 
 */
public class StatementStub implements Statement {

    private static final String QUERY_WITH_LOCALE = "select ge.ACTOR, ge.TYPE, ge.EVENTIDENTIFIER, ge.MULTIPLIER, p.PRODUCTID, ge.OCCURRENCETIME, pu.FIRSTNAME, pu.LASTNAME, sub.SUBSCRIPTIONID, lr.VALUE, lr.LOCALE "
            + "from LOCALIZEDRESOURCE lr, SUBSCRIPTION sub, ORGANIZATION org, PRODUCT p, "
            + "GATHEREDEVENT ge LEFT OUTER JOIN PLATFORMUSER pu ON ge.ACTOR=pu.USERID "
            + "where ge.SUBSCRIPTIONTKEY=sub.TKEY " + "and lr.locale='";

    private static final String QUERY_WITHOUT_LOCALE = "select ge.ACTOR, ge.TYPE, ge.EVENTIDENTIFIER, ge.MULTIPLIER, p.PRODUCTID, ge.OCCURRENCETIME, pu.FIRSTNAME, pu.LASTNAME, sub.SUBSCRIPTIONID "
            + "from SUBSCRIPTION sub, ORGANIZATION org, PRODUCT p, "
            + "GATHEREDEVENT ge LEFT OUTER JOIN PLATFORMUSER pu ON ge.ACTOR=pu.USERID "
            + "where ge.SUBSCRIPTIONTKEY=sub.TKEY "
            + "and (pu.ORGANIZATIONKEY=org.TKEY OR pu.ORGANIZATIONKEY IS NULL) "
            + "and sub.PRODUCT_TKEY=p.TKEY "
            + "and sub.ORGANIZATIONKEY=org.TKEY " + "and org.ORGANIZATIONID='";

    private ResultSet rs;

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    @Override
    public void addBatch(String sql) throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#cancel()
     */
    @Override
    public void cancel() throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#clearBatch()
     */
    @Override
    public void clearBatch() throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#close()
     */
    @Override
    public void close() throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#execute(java.lang.String)
     */
    @Override
    public boolean execute(String sql) throws SQLException {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    @Override
    public boolean execute(String sql, int autoGeneratedKeys)
            throws SQLException {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    @Override
    public boolean execute(String sql, String[] columnNames)
            throws SQLException {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeBatch()
     */
    @Override
    public int[] executeBatch() throws SQLException {

        return null;
    }

    private Object[] getExternalDataRow(String[] row, boolean active) {
        final String[] ret = new String[row.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = row[i].trim();
            if ("PRODUCTHISTORY.STATUS".equalsIgnoreCase(ret[i])) {
                ret[i] = (active ? ServiceStatus.ACTIVE
                        : ServiceStatus.INACTIVE).name();
            }
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        ResultSetStub stub = new ResultSetStub(sql);
        if (sql.startsWith("SELECT DISTINCT br.CREATIONTIME, br.PERIODSTARTTIME, br.PERIODENDTIME, br.RESULTXML, sup.name, sup.address, prdHist.productid")) {
            if (rs instanceof ResultSetStub) {
                ((ResultSetStub) rs).setSQLColumns(sql);
            }
            return rs;
        } else if (sql.indexOf("PRODUCTHISTORY.PRODUCTID") > 0) {
            sql = sql.substring(sql.indexOf(' '), sql.indexOf(" from ")).trim();
            final String[] values = sql.split(",");
            stub.addResultSet(getExternalDataRow(values, true));
            stub.addResultSet(getExternalDataRow(values, false));
            stub.addResultSet(getExternalDataRow(values, true));
            return stub;
        } else if (sql.indexOf("SubscriptionHistory") > 0) {
            stub.addResultSet("testSubId");
            return stub;
        } else if (sql.startsWith(QUERY_WITHOUT_LOCALE)) {
            stub.addResultSet(new Object[] { "actor", "type", "eventid",
                    new Long(1), "product id", new Long(1000), "firstname",
                    "lastname", "subid" });
            return stub;
        } else if (sql.startsWith(QUERY_WITH_LOCALE)
                && sql.contains("lr.locale='de'")) {
            stub.addResultSet(new Object[] { "actor", "type", "eventid",
                    new Long(1), "product id", new Long(1000), "firstname",
                    "lastname", "subid", "de desc", "de" });
            return stub;
        } else if (sql.startsWith(QUERY_WITH_LOCALE)
                && sql.contains("lr.locale='en'")) {
            stub.addResultSet(new Object[] { "actor", "type", "eventid",
                    new Long(1), "product id", new Long(1000), "firstname",
                    "lastname", "subid", "en desc", "en" });
            return stub;
        } else if (sql.startsWith(QUERY_WITH_LOCALE)
                && sql.contains("lr.locale='ja'")) {
            stub.addResultSet(new Object[] { "actor", "type", "eventid",
                    new Long(1), "product id", new Long(1000), "firstname",
                    "lastname", "subid", null, null });
            return stub;
        }
        if (rs instanceof ResultSetStub) {
            ((ResultSetStub) rs).setSQLColumns(sql);
        }
        return rs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    @Override
    public int executeUpdate(String sql) throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    @Override
    public int executeUpdate(String sql, int[] columnIndexes)
            throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#executeUpdate(java.lang.String,
     * java.lang.String[])
     */
    @Override
    public int executeUpdate(String sql, String[] columnNames)
            throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getFetchDirection()
     */
    @Override
    public int getFetchDirection() throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getFetchSize()
     */
    @Override
    public int getFetchSize() throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getGeneratedKeys()
     */
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getMaxFieldSize()
     */
    @Override
    public int getMaxFieldSize() throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getMaxRows()
     */
    @Override
    public int getMaxRows() throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getMoreResults()
     */
    @Override
    public boolean getMoreResults() throws SQLException {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getMoreResults(int)
     */
    @Override
    public boolean getMoreResults(int current) throws SQLException {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getQueryTimeout()
     */
    @Override
    public int getQueryTimeout() throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getResultSet()
     */
    @Override
    public ResultSet getResultSet() throws SQLException {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    @Override
    public int getResultSetConcurrency() throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getResultSetHoldability()
     */
    @Override
    public int getResultSetHoldability() throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getResultSetType()
     */
    @Override
    public int getResultSetType() throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getUpdateCount()
     */
    @Override
    public int getUpdateCount() throws SQLException {

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    @Override
    public void setCursorName(String name) throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setFetchDirection(int)
     */
    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setFetchSize(int)
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    @Override
    public void setMaxFieldSize(int max) throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setMaxRows(int)
     */
    @Override
    public void setMaxRows(int max) throws SQLException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    @Override
    public void setQueryTimeout(int seconds) throws SQLException {

    }

    public void setResultSet(ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {

        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {

        return false;
    }

    @Override
    public boolean isClosed() throws SQLException {

        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {

    }

    @Override
    public boolean isPoolable() throws SQLException {

        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {

        return false;
    }

}