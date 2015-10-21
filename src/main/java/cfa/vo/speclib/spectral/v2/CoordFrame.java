/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v2;

import cfa.vo.speclib.Quantity;

/**
 *
 * @author mdittmar
 */
public interface CoordFrame extends Frame {
    
    public Quantity<String> getReferencePosition();
    
    public void setReferencePosition( String value );
    
    public void setReferencePosition( Quantity<String> value );
    
    public boolean hasReferencePosition();
}
