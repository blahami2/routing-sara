/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.certicon.routing.data.basic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An implementation of {@link DataDestination} using a {@link String}. Obtain result via the {@link #getResult() getResult} method.
 *
 * @author Michael Blaha  {@literal <michael.blaha@certicon.cz>}
 */
public class StringDataDestination implements DataDestination {

    private final StringBuilder sb = new StringBuilder();

    @Override
    public DataDestination open() throws IOException {
        sb.delete( 0, sb.length() );
        return this;
    }

    @Override
    public DataDestination write( String str ) throws IOException {
        sb.append( str );
        return this;
    }

    @Override
    public DataDestination close() throws IOException {
        return this;
    }
    
    /**
     * Returns the current content of this {@link DataDestination}
     * @return {@link String} representation of the content
     */
    public String getResult(){
        return sb.toString();
    }

    @Override
    public DataDestination flush() throws IOException {
        sb.delete( 0, sb.length() );
        return this;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new StringOutputStream();
    }
    
    private class StringOutputStream extends OutputStream {
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        @Override
        public void write( int b ) throws IOException {
            os.write( b );
        }

        @Override
        public void close() throws IOException {
            super.close();
            sb.append( new String(os.toByteArray(), "UTF-8"));
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            sb.append( new String(os.toByteArray(), "UTF-8"));
        }
        
        
    }
}
