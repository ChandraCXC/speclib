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
public interface ZeroPoint {
    
    public Quantity<Double> getFlux();
    public Quantity<Double> getReferenceMagnitude();
    public Quantity<Integer> getType();

    public void setFlux( Double value );
    public void setReferenceMagnitude( Double value );
    public void setType( Integer value );

    public void setFlux( Quantity<Double> value );
    public void setReferenceMagnitude( Quantity<Double> value );
    public void setType( Quantity<Integer> value );

    public boolean hasFlux();
    public boolean hasReferenceMagnitude();
    public boolean hasType();

}
