/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v1;

import cfa.vo.speclib.Quantity;
import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface CharacterizationAxis {
    
    public Quantity<String> getName();
    public Quantity<String> getUCD();
    public Quantity<String> getUnit();
    public Quantity<Double> getResolution();
    public Quantity<String> getCalibration();
    public Accuracy getAccuracy();
    public Coverage getCoverage();
    public SamplingPrecision getSamplingPrecision();
    
    public void setName( String name );
    public void setUCD( String ucd );
    public void setUnit( String unit );
    public void setResolution( Double value );
    public void setCalibration( String value );

    public void setName( Quantity<String> value );
    public void setUCD( Quantity<String> value );
    public void setUnit( Quantity<String> value );
    public void setResolution( Quantity<Double> value );
    public void setCalibration( Quantity<String> value );
    public void setAccuracy( Accuracy value );
    public void setCoverage( Coverage value );
    public void setSamplingPrecision( SamplingPrecision value );

    public boolean hasAccuracy();
    public boolean hasCoverage();
    public boolean hasResolution();
    public boolean hasSamplingPrecision();
    
}
