/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface SpectralFrame extends CoordFrame {
    
    public Quantity<Double> getRedshift();
    
    public void setRedshift( Double value );
    
    public void setRedshift( Quantity<Double> value );
    
    public boolean isSetRedshift();
    
}
