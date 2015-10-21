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
public interface RedshiftFrame extends Frame {
    
    public Quantity<String> getDopplerDefinition();
    public Quantity<String> getReferencePosition();
    
    public void setDopplerDefinition( String value );
    public void setReferencePosition( String value );
    
    public void setDopplerDefinition( Quantity<String> value );
    public void setReferencePosition( Quantity<String> value );
    
    public boolean hasDopplerDefinition();
    public boolean hasReferencePosition();
    
}
