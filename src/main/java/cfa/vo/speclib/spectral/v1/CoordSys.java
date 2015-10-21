/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v1;

import cfa.vo.speclib.Quantity;
import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface CoordSys {
    
    public Quantity<String> getID();
    public CoordFrame getCoordFrame( int index );
    public List<CoordFrame> getCoordFrames();
    
    public void setID( String value );
    public void setCoordFrame( int index, CoordFrame value );
    public void setCoordFrames( List<CoordFrame> values );

    public void setID( Quantity<String> value );
    
    public void hasID();
    public void hasCoordFrames();
    
}
