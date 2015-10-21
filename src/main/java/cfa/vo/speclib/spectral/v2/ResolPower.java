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
public interface ResolPower {
    public Quantity<Double> getRefVal();

    public void setRefVal( Double value );

    public void setRefVal( Quantity<Double> value );

    public boolean hasRefVal();
    
}
