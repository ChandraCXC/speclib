/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v2;

import cfa.vo.speclib.Quantity;
import java.net.URL;

/**
 *
 * @author mdittmar
 */
public interface DataModel {
    public Quantity<String> getName();
    public Quantity<String> getPrefix();
    public Quantity<URL> getURL();

    public void setName( String value );
    public void setPrefix( String value );
    public void setURL( URL value );

    public void setName( Quantity<String> q );
    public void setPrefix( Quantity<String> q );
    public void setURL( Quantity<URL> q );

    public boolean hasName();
    public boolean hasPrefix();
    public boolean hasURL();

}
