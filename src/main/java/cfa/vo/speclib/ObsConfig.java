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
public interface ObsConfig {
    
    public List<ObservingElement> getObservingElements();

    public void setObservingElements( List<ObservingElement> value );
    public void setObservingElements( int index, ObservingElement value );
    
    public boolean isSetObservingElements();
}
