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
public interface Contact {
    
    public Quantity<String> getName();
    public Quantity<String> getEmail();
    
    public void setName( String value );
    public void setEmail( String value );
    
    public void setName( Quantity<String> value );
    public void setEmail( Quantity<String> value );
    
    public boolean hasName();
    public boolean hasEmail();

}
