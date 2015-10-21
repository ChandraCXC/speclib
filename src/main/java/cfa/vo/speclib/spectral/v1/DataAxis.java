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
public interface DataAxis {
    
    public Quantity<String> getSystemID();
    public String getName();
    public String getUCD();
    public String getUnit();
    public Quantity<Double> getResolution();
    public Quantity<Double> getValue();
    public Accuracy getAccuracy();

    public void setSystemID( String value );
    public void setSystemID( Quantity<String> value );
    public void setName( String value );
    public void setUCD( String ucd );
    public void setUnit( String unit );
    public void setResolution( Double value );
    public void setResolution( Quantity<Double> value );
    public void setValue( Double value );
    public void setValue( Quantity<Double> value );
    public void setAccuracy( Accuracy value );
    
    public boolean hasValue();
    public boolean hasAccuracty();
    public boolean hasResolution();
}
