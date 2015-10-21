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
public interface SamplingPrecision {
    
    public Quantity<Double> getSampleExtent();
    public SamplingPrecisionRefVal getSamplingPrecisionRefVal();
    
    public void setSampleExtent( Double value );
    
    public void setSampleExtent( Quantity<Double> value );
    public void setSamplingPrecisionRefVal( SamplingPrecisionRefVal value );
    
    public boolean hasSampleExtent();
    public boolean hasSamplingPrecisionRefVal();
    
}
