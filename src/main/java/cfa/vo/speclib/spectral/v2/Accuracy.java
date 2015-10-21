/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.spectral.v2;

import cfa.vo.speclib.Quantity;

/**
 *
 * @author mdittmar
 */
public interface Accuracy {
    
    public Quantity<Double> getBinLow();
    public Quantity<Double> getBinHigh();
    public Quantity<Double> getBinSize();
    public Quantity<Double> getStatError();
    public Quantity<Double> getStatErrLow();
    public Quantity<Double> getStatErrHigh();
    public Quantity<Double> getSysError();
    public Quantity<Integer> getQualityStatus();
    
    public void setBinLow( Double value );
    public void setBinHigh( Double value );
    public void setBinSize( Double value );
    public void setStatError( Double value );
    public void setStatErrLow( Double value );
    public void setStatErrHigh( Double value );
    public void setSysError( Double value );
    public void setQualityStatus( Integer value );
    
    public void setBinLow( Quantity<Double> value );
    public void setBinHigh( Quantity<Double> value );
    public void setBinSize( Quantity<Double> value );
    public void setStatError( Quantity<Double> value );
    public void setStatErrLow( Quantity<Double> value );
    public void setStatErrHigh( Quantity<Double> value );
    public void setSysError( Quantity<Double> value );
    public void setQualityStatus( Quantity<Integer> value );

    public boolean hasBinLow();
    public boolean hasBinHigh();
    public boolean hasBinSize();
    public boolean hasStatError();
    public boolean hasStatErrLow();
    public boolean hasStatErrHigh();
    public boolean hasSysError();
    public boolean hasQualityStatus();
    
}
