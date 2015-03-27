/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface Derived {

    public Redshift getRedshift();
    public Quantity<Double> getSNR();
    public Quantity<Double> getVarAmpl();

    public void setRedshift( Redshift value );
    public void setSNR( Double value );
    public void setVarAmpl( Double value);
    
    public void setSNR( Quantity<Double> q );
    public void setVarAmpl( Quantity<Double> q );
    
    public boolean isSetSNR();
    public boolean isSetVarAmpl();
    public boolean isSetRedshift();

}
