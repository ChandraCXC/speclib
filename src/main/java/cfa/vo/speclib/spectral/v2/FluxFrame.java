/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v2;

import cfa.vo.speclib.Quantity;
import java.net.URI;

/**
 *
 * @author mdittmar
 */
public interface FluxFrame extends Frame {
        
    public Quantity<URI> getRefID();
    public Quantity<String> getReferencePosition();
    public PhotCal getPhotCal();
    
    public void setRefID( URI value );
    public void setReferencePosition( String value );
    public void setPhotCal( PhotCal value );
    
    public void setRefID( Quantity<URI> value );
    public void setReferencePosition( Quantity<String> value );
    
    public boolean hasRefID();
    public boolean hasReferencePosition();
    public boolean hasPhotCal();
}
