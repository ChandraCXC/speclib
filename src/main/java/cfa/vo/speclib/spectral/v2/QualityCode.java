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
public interface QualityCode {
    
    public Quantity<Integer> getCodeNum();
    public Quantity<String> getDefinition();
    
    public void setCodeNum( Integer value );
    public void setDefinition( String value );
    
    public void setCodeNum( Quantity<Integer> value );
    public void setDefinition( Quantity<String> value );
    
    public boolean hasCodeNum();
    public boolean hasDefinition();
}
