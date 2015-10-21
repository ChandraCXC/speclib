/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v2;

import cfa.vo.speclib.Quantity;

/**
 *
 * @author mdittmar
 */
public interface Target {
    
    public Quantity<String> getName();
    public Quantity<String> getDescription();
    public Quantity<Double[]> getPos();
    public Quantity<String> getTargetClass();
    public Quantity<String> getSpectralClass();
    public Quantity<Double> getRedshift();
    public Quantity<Double> getVarAmpl();

    public void setName( String value );
    public void setDescription( String value );
    public void setPos( Double[] value );
    public void setTargetClass( String value );
    public void setSpectralClass( String value );
    public void setRedshift( Double value );
    public void setVarAmpl( Double value );

    public void setName( Quantity<String> value );
    public void setDescription( Quantity<String> value );
    public void setPos( Quantity<Double[]> value );
    public void setTargetClass( Quantity<String> value );
    public void setSpectralClass( Quantity<String> value );
    public void setRedshift( Quantity<Double> value );
    public void setVarAmpl( Quantity<Double> value );

}
