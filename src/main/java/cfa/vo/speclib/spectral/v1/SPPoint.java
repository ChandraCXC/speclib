/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v1;

/**
 *
 * @author mdittmar
 */
public interface SPPoint {
    
    public TimeDataAxis getTimeAxis();
    public SpectralDataAxis getSpectralAxis();
    public FluxDataAxis getFluxAxis();
    public FluxDataAxis getBackgroundModel();

    public void setTimeAxis( TimeDataAxis value );
    public void setSpectralAxis( SpectralDataAxis value );
    public void setFluxAxis( FluxDataAxis value );
    public void setBackgroundModel( FluxDataAxis value );

    public boolean hasTimeAxis();
    public boolean hasSpectralAxis();
    public boolean hasFluxAxis();
    public boolean hasBackgroundModel();    
}
