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
public interface SpectralFrame extends Frame {
    
    public Quantity<Double> getRedshift();
    public Quantity<String> getReferencePosition();
    
    public void setRedshift( Double value );
    public void setReferencePosition( String value );
    
    public void setRedshift( Quantity<Double> value );
    public void setReferencePosition( Quantity<String> value );
    
    public boolean hasRedshift();
    public boolean hasReferencePosition();
    
}
