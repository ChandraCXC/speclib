/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v1;

import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface Characterization {

    public List<CharacterizationAxis> getCharacterizationAxes();
    public CharacterizationAxis getCharacterizationAxes( int index );
    public FluxCharAxis getFluxAxis();
    public SpatialCharAxis getSpatialAxis();
    public SpectralCharAxis getSpectralAxis();
    public TimeCharAxis getTimeAxis();
    
    public void setCharacterizationAxes( List<CharacterizationAxis> value );
    public void setCharacterizationAxes( int index, CharacterizationAxis value );
    public void setFluxAxis( FluxCharAxis value );
    public void setSpatialAxis( SpatialCharAxis value );
    public void setSpectralAxis( SpectralCharAxis value );
    public void setTimeAxis( TimeCharAxis value );

    public boolean hasCharacterizationAxes();
    public boolean hasFluxAxis();
    public boolean hasSpatialAxis();
    public boolean hasSpectralAxis();
    public boolean hasTimeAxis();
}
