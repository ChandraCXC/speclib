/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public abstract interface ObservingElement {

    public Quantity<String> getName();
    public void setName( String value );
    public void setName( Quantity<String> value );
    public boolean hasName();

}
