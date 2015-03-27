/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface DataAxis {
    
    public Quantity<String> getSystemID();
    public String getName();
    public String getUCD();
    public String getUnit();
    public Quantity<Double> getValue();
    public Accuracy getAccuracy();
    public List<Correction> getCorrections();
    public Correction getCorrections( int index );
    public Resolution getResolution();

    public void setSystemID( String value );
    public void setSystemID( Quantity<String> value );
    public void setName( String value );
    public void setUCD( String ucd );
    public void setUnit( String unit );
    public void setValue( Double value );
    public void setValue( Quantity<Double> value );
    public void setAccuracy( Accuracy value );
    public void setCorrections( List<Correction> value );
    public void setCorrections( int index, Correction value );
    public void setResolution( Resolution value );
    
    public boolean isSetValue();
    public boolean isSetAccuracty();
    public boolean isSetCorrections();
    public boolean isSetResolution();
}
