/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

import java.net.URI;

/**
 *
 * @author mdittmar
 */
public interface MagnitudeSystem {
    
    public Quantity<String> getType();
    public Quantity<URI> getReferenceSpectrum();

    public void setType( String value );
    public void setReferenceSpectrum( URI value );
    
    public void setType( Quantity<String> value );
    public void setReferenceSpectrum( Quantity<URI> value );

    public boolean hasType();
    public boolean hasReferenceSpectrum();
    
}
