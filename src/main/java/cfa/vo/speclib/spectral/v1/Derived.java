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
public interface Derived {

    public Redshift getRedshift();
    public Quantity<Double> getSNR();
    public Quantity<Double> getVarAmpl();

    public void setRedshift( Redshift value );
    public void setSNR( Double value );
    public void setVarAmpl( Double value);
    
    public void setSNR( Quantity<Double> q );
    public void setVarAmpl( Quantity<Double> q );
    
    public boolean hasSNR();
    public boolean hasVarAmpl();
    public boolean hasRedshift();

}
