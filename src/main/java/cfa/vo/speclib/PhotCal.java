/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public interface PhotCal {

    public Quantity<String> getIdentifier();
    public ZeroPoint getZeroPoint();
    public MagnitudeSystem getMagnitudeSystem();

    public void setIdentifier( String value );

    public void setIdentifier( Quantity<String> value );
    public void setZeroPoint( ZeroPoint value );
    public void setMagnitudeSystem( MagnitudeSystem value );

    public boolean isSetIdentifier();
    public boolean isSetZeroPoint();
    public boolean isSetMagnitudeSystem();
    
}
