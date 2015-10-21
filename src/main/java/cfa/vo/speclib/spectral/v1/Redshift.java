/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v1;

import cfa.vo.speclib.Quantity;

/**
 *
 * @author mdittmar
 */
public interface Redshift {
    public Quantity<Double> getValue();
    public Quantity<Double> getResolution();
    public Quantity<Integer> getQuality();
    public Accuracy getAccuracy();
//    public Quantity<Double> getStatError();  #differ utype list vs schema
//    public Quantity<Double> getConfidence(); #differ utype list vs schema
    
    public void setValue( Double value );
    public void setResolution( Double value );
    public void setQuality( Integer value );
//    public void setStatError( Double value );
//    public void setConfidence( Double value );
    
    public void setValue( Quantity<Double> q );
    public void setResolution( Quantity<Double> q );
    public void setQuality( Quantity<Integer> q );
//    public void setStatError( Quantity<Double> q );
//    public void setConfidence( Quantity<Double> q );
    
    public boolean hasValue();
    public boolean hasResolution();
    public boolean hasQuality();
    public boolean hasAccuracy();

//    public boolean hasStatError();
//    public boolean hasConfidence();
}
