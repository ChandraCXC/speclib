/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface Correction {
    public Quantity<String> getName();
    public boolean isApplied();

    public void setName( String value );
    public void setName( Quantity<String> value );
    public void setApplied( Boolean value );    
    public void setApplied( Quantity<Boolean> value );
    
    public boolean hasName();
    public boolean hasApplied();
}
