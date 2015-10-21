/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v1;

import cfa.vo.speclib.spectral.v2.*;
import cfa.vo.speclib.Quantity;
import java.net.URI;
import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface Curation {

    public Contact getContact();
    public void setContact( Contact value );
    public boolean hasContact();

    public Quantity<String> getPublisher();
    public Quantity<URI> getPublisherID();
    public Quantity<URI> getPublisherDID();
    public Quantity<String> getDate();
    public Quantity<String> getVersion();
    public Quantity<String> getRights();
    public Quantity<String> getReferences( int index );
    public List<Quantity> getReferences();
    
    public void setPublisher( String value );
    public void setPublisherID( URI value );
    public void setPublisherDID( URI value );
    public void setDate( String value );
    public void setVersion( String value );
    public void setRights( String value );
    public void setReferences( int index, String value );
    
    public void setPublisher( Quantity value );
    public void setPublisherID( Quantity value );
    public void setPublisherDID( Quantity value );
    public void setDate( Quantity value );
    public void setVersion( Quantity value );
    public void setRights( Quantity value );
    public void setReference( int index, Quantity value );
    public void setReferences( List<Quantity> values );
    
    public boolean hasPublisher();
    public boolean hasPublisherID();
    public boolean hasPublisherDID();
    public boolean hasDate();
    public boolean hasVersion();
    public boolean hasRights();
    public boolean hasReferences();
        
}
