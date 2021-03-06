/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v1;

import cfa.vo.speclib.Quantity;
import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface DataID {
    
    public Quantity<String> getBandpass();
    public Quantity<String> getCollections( int index );
    public List<Quantity> getCollections();
    public Quantity<String> getContributors( int index );
    public List<Quantity> getContributors();
    public Quantity<String> getCreationType();
    public Quantity<String> getCreator();
    public Quantity<URI> getCreatorDID();
    public Quantity<String> getDataSource();
    public Quantity<URI> getDatasetID();
    public Quantity<String> getDate();
    public Quantity<String> getInstrument();
    public Quantity<URL> getLogo();
    public Quantity<String> getTitle();  
    public Quantity<String> getVersion();
    
    public void setBandpass( String value );
    public void setCollections( int index, String value );
    public void setContributors( int index, String value );
    public void setCreationType( String value );
    public void setCreator( String value );
    public void setCreatorDID( URI value );
    public void setDataSource( String value );
    public void setDatasetID( URI value );
    public void setDate( String value );
    public void setInstrument( String value );
    public void setLogo( URL value );
    public void setTitle( String value );
    public void setVersion( String value );
    
    public void setBandpass( Quantity<String> value );
    public void setCollections( int index, Quantity<String> value );
    public void setCollections( List<Quantity> values );
    public void setContributors( int index, Quantity<String> value );
    public void setContributors( List<Quantity> values );
    public void setCreationType( Quantity<String> value );
    public void setCreator( Quantity<String> value );
    public void setCreatorDID( Quantity<URI> value );
    public void setDataSource( Quantity<String> value );
    public void setDatasetID( Quantity<URI> value );
    public void setDate( Quantity<String> value );
    public void setInstrument( Quantity<String> value );
    public void setLogo( Quantity<URL> value );
    public void setTitle( Quantity<String> value );
    public void setVersion( Quantity<String> value );
    
    public boolean hasBandpass();
    public boolean hasCollections();
    public boolean hasContributors();
    public boolean hasCreationType();
    public boolean hasCreator();
    public boolean hasCreatorDID();
    public boolean hasDataSource();
    public boolean hasDatasetID();
    public boolean hasDate();
    public boolean hasInstrument();
    public boolean hasLogo();
    public boolean hasTitle();
    public boolean hasVersion();
}
