/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface CoordSys {
    
    public Quantity<String> getID();
    public Frame getCoordFrame( int index );
    public List<CoordFrame> getCoordFrames();
    public SpaceFrame getSpaceFrame();
    public SpectralFrame getSpectralFrame();
    public TimeFrame getTimeFrame();
    public RedshiftFrame getRedshiftFrame();
    public FluxFrame getFluxFrame( int index );
    public List<FluxFrame> getFluxFrames();
    
    public void setID( String value );
    public void setCoordFrame( int index, CoordFrame value );
    public void setCoordFrames( List<CoordFrame> values );
    public void setSpaceFrame( SpaceFrame value );
    public void setSpectralFrame( SpectralFrame value );
    public void setTimeFrame( TimeFrame value );
    public void setRedshiftFrame( RedshiftFrame value );
    public void setFluxFrame( int index, FluxFrame value );
    public void setFluxFrames( List<FluxFrame> values );

    public void setID( Quantity<String> value );
    
    public void isSetID();
    public void isSetCoordFrames();
    public void isSetSpaceFrame();
    public void isSetSpectralFrame();
    public void isSeTimeFrame();
    public void isSetRedshiftFrame();
    public void isSetFluxFrames();
    
}
