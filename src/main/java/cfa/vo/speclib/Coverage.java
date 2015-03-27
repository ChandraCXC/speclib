/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface Coverage {
    public Location getLocation();
    public Bounds   getBounds();
    public Support  getSupport();
    
    public void setLocation( Location value );
    public void setBounds( Bounds value );
    public void setSupport( Support value );
    
    public boolean isSetLocation();
    public boolean isSetBounds();
    public boolean isSetSupport();
    
}
