/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface Bounds {
    
    public Quantity<Double> getExtent();
    public Interval getLimits();
    
    public void setExtent( Double value );
    
    public void setExtent( Quantity<Double> value );
    public void setLimits( Interval value );
    
    public boolean isSetExtent();
    public boolean isSetLimits();
    
}
