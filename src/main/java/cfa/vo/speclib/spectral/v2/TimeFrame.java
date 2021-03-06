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
public interface TimeFrame extends Frame {
    
    public Quantity<String> getReferencePosition();
    public Quantity<Double> getZero();
    
    public void setReferencePosition( String value );
    public void setZero( Double value );
    
    public void setZero( Quantity<Double> value );
    public void setReferencePosition( Quantity<String> value );
    
    public boolean hasReferencePosition();
    public boolean hasZero();
}
