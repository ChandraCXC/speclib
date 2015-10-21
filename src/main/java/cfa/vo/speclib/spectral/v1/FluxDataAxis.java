/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v1;

import cfa.vo.speclib.Quantity;


/**
 * This is a specialization of the DataAxis interface for the Flux domain. *
 * 
 * @author mdittmar
 */
public interface FluxDataAxis extends DataAxis {
    
    public Quantity<Integer> getQuality();
    
    public void setQuality( Integer value );
    public void setQuality( Quantity<Integer> value );

    public boolean hasQuality();
    
}
