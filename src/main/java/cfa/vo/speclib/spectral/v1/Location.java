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
public interface Location {
    
    public Quantity<Double[]> getValue();
    public Quantity<Double> getResolution();
    public Accuracy getAccuracy();

    public void setValue( Double[] value );
    public void setResolution( Double value );
    
    public void setValue( Quantity<Double[]> value );
    public void setResolution( Quantity<Double> value );
    public void setAccuracy( Accuracy value );
    
    public boolean hasValue();
    public boolean hasResolution();
    public boolean hasAccuracy();
    
}
