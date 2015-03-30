/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

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
    
    public boolean isSetCodeNum();
    public boolean isSetDefinition();
}
