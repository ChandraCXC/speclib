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
    public DataModel getDataModel();
    public Quantity<String> getType();
    public Integer  getLength();
    public Quantity<String> getSpectralSI();
    public Quantity<String> getTimeSI();
    public Quantity<String> getFluxSI();

    public void setDataModel(DataModel value);
    public void setType( String value );
    public void setSpectralSI( String value );
    public void setTimeSI( String value );
    public void setFluxSI( String value );

    public void setType( Quantity<String> q );
    public void setSpectralSI( Quantity<String> q );
    public void setTimeSI( Quantity<String> q );
    public void setFluxSI( Quantity<String> q );

    public boolean isSetDataModel();
    public boolean isSetType();
    public boolean isSetSpectralSI();
    public boolean isSetTimeSI();
    public boolean isSetFluxSI();
}
