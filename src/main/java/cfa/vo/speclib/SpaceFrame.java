/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface SpaceFrame extends CoordFrame {

    public Quantity<Double> getEquinox();
    
    public void setEquinox( Double value );
    
    public void setEquinox( Quantity<Double> value );
    
    public boolean isSetEquinox();
    
}
