/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface Support {
    
    public Quantity<Double> getExtent();
    public Quantity<String> getArea();
    
    public void setExtent( Double value );
    public void setArea( String value );
    
    public void setExtent( Quantity<Double> value );
    public void setArea( Quantity<String> value );
    
}
