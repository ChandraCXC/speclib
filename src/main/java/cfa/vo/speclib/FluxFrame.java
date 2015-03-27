/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

import java.net.URI;

/**
 *
 * @author mdittmar
 */
public interface FluxFrame extends CoordFrame {
    
    //TODO: Define PhotCal class... 
    
    public Quantity<URI> getRefID();
//    public PhotCal getPhotCal();
    
    public void setRefID( URI value );
//    public void setPhotCal( PhotCal value );
    
    public void setRefID( Quantity<URI> value );
    
    public boolean isSetRefID();
//    public boolean isSetPhotCal();
}
