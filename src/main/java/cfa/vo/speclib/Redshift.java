/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface Redshift {
    public Quantity<Double> getValue();
    public Quantity<Double> getStatError();
    public Quantity<Double> getConfidence();
    
    public void setValue( Double value );
    public void setStatError( Double value );
    public void setConfidence( Double value );
    
    public void setValue( Quantity<Double> q );
    public void setStatError( Quantity<Double> q );
    public void setConfidence( Quantity<Double> q );
    
    public boolean hasValue();
    public boolean hasStatError();
    public boolean hasConfidence();
}
