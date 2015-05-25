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
public interface CharacterizationAxis {
    
    //TODO: Name,UCD,Unit properties..  as Quantity?
    public Quantity<String> getName();
    public Quantity<String> getUCD();
    public Quantity<String> getUnit();
    public Quantity<String> getCalibrationStatus();
    public Accuracy getAccuracy();
    public CoordSys getCoordSys();
    public Coverage getCoverage();
    public Resolution getResolution();
    public SamplingPrecision getSamplingPrecision();
    public List<QualityCode> getQualityDefs();
    
    public void setName( String name );
    public void setName( Quantity<String> value );
    public void setUCD( String ucd );
    public void setUCD( Quantity<String> value );
    public void setUnit( String unit );
    public void setUnit( Quantity<String> value );
    public void setCalibrationStatus( String value );
    public void setCalibrationStatus( Quantity<String> value );
    public void setAccuracy( Accuracy value );
    public void setCoordSys( CoordSys value );
    public void setCoverage( Coverage value );
    public void setResolution( Resolution value );
    public void setSamplingPrecision( SamplingPrecision value );
    public void setQualityDefs( List<QualityCode> value );
    public void setQualityDefs( int index, QualityCode value );

    public boolean isSet();
    public boolean isSetAccuracy();
    public boolean isSetCoordSys();
    public boolean isSetCoverage();
    public boolean isSetResolution();
    public boolean isSetQualityDefs();
    
}
