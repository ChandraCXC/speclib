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
public interface Point {
    public List<DataAxis> getDataAxes();
    public DataAxis getDataAxes( int index );
    
    public void setDataAxes( List<DataAxis> value );
    public void setDataAxes( int index, DataAxis value );

    public boolean isSetDataAxes();

    
}
