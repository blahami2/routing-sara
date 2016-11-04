/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.data.basic.database;

import cz.certicon.routing.data.basic.Reader;
import cz.certicon.routing.data.basic.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * An abstract implementation of the {@link Reader}/{@link Writer} interfaces
 * for the server database access. Encapsulates database access (connection creating),
 * controls the state before reading/writing and opens the connection if
 * necessary.
 *
 * @author Michael Blaha {@literal <michael.blaha@certicon.cz>}
 * @param <In> output of the reader (type to be read)
 * @param <InData> additional data for the reader (if it requires any)
 * @param <Out> type to be written
 * @param <OutData> additional data
 */
public abstract class AbstractServerDatabase<In, Out, InData, OutData> extends AbstractDatabase<In, Out, InData, OutData> {

    public AbstractServerDatabase( Properties connectionProperties ) {
        super( connectionProperties );
    }

    @Override
    protected Connection createConnection( Properties properties ) throws ClassNotFoundException, SQLException {
        Class.forName( getDriverClass( properties ) );
        return DriverManager.getConnection( getDatabaseUrl( properties ), getUsername( properties ), getPassword( properties ) );
    }

    /**
     * Returns 'driver' value of the given properties. Overwrite if necessary.
     *
     * @param properties containing driver class property
     * @return driver class
     */
    protected String getDriverClass( Properties properties ) {
        return properties.getProperty( "driver" );
    }

    /**
     * Returns 'url' value of the given properties. Overwrite if necessary.
     *
     * @param properties containing url property
     * @return database url
     */
    protected String getDatabaseUrl( Properties properties ) {
        return properties.getProperty( "url" );
    }

    /**
     * Returns 'user' value of the given properties. Overwrite if necessary.
     *
     * @param properties containing user property
     * @return database username
     */
    protected String getUsername( Properties properties ) {
        return properties.getProperty( "user" );
    }

    /**
     * Returns 'password' value of the given properties. Overwrite if necessary.
     *
     * @param properties containing password property
     * @return database password
     */
    protected String getPassword( Properties properties ) {
        return properties.getProperty( "password" );
    }

}
