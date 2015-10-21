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
public abstract interface Frame {
    
    public Quantity<String> getID();
    public Quantity<String> getName();
    public Quantity<String> getUCD();
    
    public void setID( String value );
    public void setName( String value );
    public void setUCD( String value );
    
    public void setID( Quantity<String> value );
    public void setName( Quantity<String> value );
    public void setUCD( Quantity<String> value );
    
    public boolean hasID();
    public boolean hasName();
    public boolean hasUCD();

}
