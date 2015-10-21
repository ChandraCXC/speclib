/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v2;

import cfa.vo.speclib.Quantity;
import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface SpectralDataset extends Dataset{
    
    public Integer  getLength();
    public Quantity<String> getSpectralSI();
    public Quantity<String> getTimeSI();
    public Quantity<String> getFluxSI();
    public List<SPPoint> getData();
    public SPPoint getData(int index );

    public void setSpectralSI( String value );
    public void setTimeSI( String value );
    public void setFluxSI( String value );
    
    public void setSpectralSI( Quantity<String> q );
    public void setTimeSI( Quantity<String> q );
    public void setFluxSI( Quantity<String> q );
    public void setData( List<SPPoint> value );
    public void setData( int index, SPPoint value );

    public boolean hasSpectralSI();
    public boolean hasTimeSI();
    public boolean hasFluxSI();
    public boolean hasData();
}
