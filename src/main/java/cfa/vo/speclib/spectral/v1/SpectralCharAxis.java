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
public interface SpectralCharAxis  extends CharacterizationAxis {
    
  public Quantity<Double> getResPower();
  public void setResPower( Double value );
  public void setResPower( Quantity<Double> value );

}
