/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface Location {
    
    public Quantity<Double[]> getValue();
    public void setValue( Double[] value );
    public void setValue( Quantity<Double[]> value );
    public boolean isSetValue();
    
}
