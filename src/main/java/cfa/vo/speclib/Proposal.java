/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface Proposal {
    public Quantity<String> getIdentifier();
    public void setIdentifier( String value );
    public void setIdentifier( Quantity<String> q );
    public boolean hasIdentifier();
}
