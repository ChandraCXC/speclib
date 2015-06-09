/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface SpaceFrame extends Frame {

    public Quantity<Double> getEquinox();
    public Quantity<String> getReferencePosition();
    
    public void setEquinox( Double value );
    public void setReferencePosition( String value );
    
    public void setEquinox( Quantity<Double> value );
    public void setReferencePosition( Quantity<String> value );
    
    public boolean hasEquinox();
    public boolean hasReferencePosition();
    
}
