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
public interface SpectralFrame extends CoordFrame {
    
    public Quantity<Double> getRedshift();
    
    public void setRedshift( Double value );
    
    public void setRedshift( Quantity<Double> value );
    
    public boolean hasRedshift();
    
}
