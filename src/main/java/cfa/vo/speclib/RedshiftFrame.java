/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface RedshiftFrame extends CoordFrame {
    
    public Quantity<String> getDopplerDefinition();

    public void setDopplerDefinition( String value );

    public void setDopplerDefinition( Quantity<String> value );

    public boolean isSetDopplerDefinition();
    
}
