/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v2;

/**
 *
 * @author mdittmar
 */
public interface SpectralResolution extends Resolution {

    public ResolPower getResolPower();
        
    public void setResolPower( ResolPower value );
    
    public boolean hasResolPower();
    
}
