/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 * Provides information specifying what kind of dataset, what data models 
 * are used and other information to aid in data discovery and access.
 * 
 * @author mdittmar
 */
public interface Dataset {

    public Quantity<String> getDataProductType();
    public Quantity<String> getDataProductSubtype();
    public Quantity<Integer> getCalibLevel();
    public DataModel getDataModel();
    public Curation getCuration();
    public Characterization getCharacterization();
    public CoordSys getCoordSys();
    public DataID getDataID();
    public Derived getDerived();
    public ObsConfig getObsConfig();
    public Proposal getProposal();
    public Target getTarget();
    
    public void setDataProductType( Quantity<String> q );
    public void setDataProductType( String value );
    public void setDataProductSubtype( Quantity<String> q );
    public void setDataProductSubtype( String value );
    public void setCalibLevel( Quantity<Integer> q );
    public void setDataModel(DataModel value);
    public void setCuration( Curation value );
    public void setCharacterization( Characterization value );
    public void setCoordSys( CoordSys value );
    public void setDataID( DataID value );
    public void setDerived( Derived value );
    public void setObsConfig( ObsConfig value );
    public void setProposal( Proposal value );
    public void setTarget( Target value );
    
    public boolean isSetDataProductType();
    public boolean isSetDataProductSubtype();
    public boolean isSetCalibLevel();
    public boolean isSetDataModel();
    public boolean isSetCuration();
    public boolean isSetCharacterization();
    public boolean isSetCoordSys();
    public boolean isSetDataID();
    public boolean isSetDerived();
    public boolean isSetObsConfig();
    public boolean isSetProposal();
    public boolean isSetTarget();
}
