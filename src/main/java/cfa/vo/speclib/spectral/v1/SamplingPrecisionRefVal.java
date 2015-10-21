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
public interface SamplingPrecisionRefVal {
    
    public Quantity<Double> getFillFactor();
    public void setFillFactor( Double value );
    public void setFillFactor( Quantity<Double> value );
    public boolean hasFillFactor();
}
