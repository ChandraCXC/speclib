/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface Resolution {
    public Quantity<Double> getRefVal();
    
    public void setRefVal( Double value );
    
    public void setRefVal( Quantity<Double> value );
    
    public void hasRefVal();
}
