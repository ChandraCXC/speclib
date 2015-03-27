/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

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

    public boolean isSetBinLow();
    public boolean isSetBinHigh();
    public boolean isSetBinSize();
    public boolean isSetStatError();
    public boolean isSetStatErrLow();
    public boolean isSetStatErrHigh();
    public boolean isSetSysError();
    public boolean isSetQualityStatus();
    
}
