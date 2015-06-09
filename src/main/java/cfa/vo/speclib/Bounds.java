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
    public Quantity<Double> getStart();
    public Quantity<Double> getStop();
    //    public Interval getLimits();
    
    public void setExtent( Double value );
    public void setStart( Double value );
    public void setStop( Double value );
    
    public void setExtent( Quantity<Double> value );
    public void setStart( Quantity<Double> value );
    public void setStop( Quantity<Double> value );
//    public void setLimits( Interval value );
    
    public boolean hasExtent();
    public boolean hasStart();
    public boolean hasStop();
//    public boolean hasLimits();
    
}
