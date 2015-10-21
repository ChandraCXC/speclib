/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v1;

import cfa.vo.speclib.Quantity;

/**
 *
 * @author mdittmar
 */
public interface TimeFrame extends CoordFrame {
    
    public Quantity<Double> getZero();
    
    public void setZero( Double value );
    
    public void setZero( Quantity<Double> value );
    
    public boolean hasZero();
}
