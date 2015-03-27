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
public interface Characterization {

    public List<CharacterizationAxis> getCharacterizationAxes();
    public CharacterizationAxis getCharacterizationAxes( int index );
    
    public void setCharacterizationAxes( List<CharacterizationAxis> value );
    public void setCharacterizationAxes( int index, CharacterizationAxis value );

    public boolean isSetCharacterizationAxes();
}
