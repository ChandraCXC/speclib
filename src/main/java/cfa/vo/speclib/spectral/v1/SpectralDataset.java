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
public interface SpectralDataset{
    
    public Quantity<String> getDataModel();
    public Quantity<String> getType();
    public Integer  getLength();
    public Quantity<String> getSpectralSI();
    public Quantity<String> getTimeSI();
    public Quantity<String> getFluxSI();
    public Characterization getCharacterization();
    public CoordSys getCoordSys();
    public Curation getCuration();
    public DataID getDataID();
    public Derived getDerived();
    public Target getTarget();

    public List<SPPoint> getData();
//    public SPPoint getData(int index );

    public void setDatamodel( String value );
    public void setType( String value );
    public void setSpectralSI( String value );
    public void setTimeSI( String value );
    public void setFluxSI( String value );
    
    public void setDatamodel( Quantity<String> q );
    public void setType( Quantity<String> q );
    public void setSpectralSI( Quantity<String> q );
    public void setTimeSI( Quantity<String> q );
    public void setFluxSI( Quantity<String> q );
    public void setCharacterization( Characterization value );
    public void setCoordSys( CoordSys value );
    public void setCuration( Curation value );
    public void setDataID( DataID value );
    public void setDerived( Derived value );
    public void setTarget( Target value );
//    public void setData( List<SPPoint> value );
//    public void setData( int index, SPPoint value );

    public boolean hasDatamodel();
    public boolean hasType();
    public boolean hasSpectralSI();
    public boolean hasTimeSI();
    public boolean hasFluxSI();
    public boolean hasCharacterization();
    public boolean hasCoordSys();
    public boolean hasCuration();
    public boolean hasDataID();
    public boolean hasDerived();
    public boolean hasTarget();
//    public boolean hasData();
}
