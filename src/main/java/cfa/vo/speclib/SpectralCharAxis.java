/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface SpectralCharAxis  extends CharacterizationAxis {
    
  public SpectralResolution getResolution();
  public void setResolution( SpectralResolution value );

}
