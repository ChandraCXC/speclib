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
    
    public Quantity<Integer> getValue();
    public Quantity<String> getDescription();
    
    public void setValue( Integer value );
    public void setDescription( String value );
    
    public void setValue( Quantity<Integer> value );
    public void setDescription( Quantity<String> value );
    
    public boolean isSetValue();
    public boolean isSetDescription();
}
