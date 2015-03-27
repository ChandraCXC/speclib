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
public interface Spectral {
    public Dataset getDataset();
    public Curation getCuration();
    public Characterization getCharacterization();
    public CoordSys getCoordSys();
    public List<Point> getData();
    public Point getData(int index );
    public DataID getDataID();
    public Derived getDerived();
    public Target getTarget();
    
    public void setDataset( Dataset value );
    public void setCuration( Curation value );
    public void setCharacterization( Characterization value );
    public void setCoordSys( CoordSys value );
    public void setData( List<Point> value );
    public void setData( int index, Point value );
    public void setDataID( DataID value );
    public void setDerived( Derived value );
    public void setTarget( Target value );
    
    public boolean isSetDataset();
    public boolean isSetCuration();
    public boolean isSetCharacterization();
    public boolean isSetCoordSys();
    public boolean isSetData();
    public boolean isSetDataID();
    public boolean isSetDerived();
    public boolean isSetTarget();
        
}
