/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

/**
 *
 * @author mdittmar
 */
public enum FileFormat {
    VOT,
    FITS;

    /**
     * Provides the filename extension associated with the file format.
     * 
     * @return 
     *    String containing file extension.
     */
    public String exten()
    {
        String retval = "Unknown";
        
        if ( this == FITS )
            retval = "fits";
        else if ( this == VOT )
            retval = "vot";
        
        return retval;
    }

    /**
     *  Generates user-friendly name of format type.
     * 
     * @return 
     *    String representation of format type
     */
    @Override
    public String toString(){
        String retval = "Unknown";
        
        if ( this == FITS )
            retval = "FITS";
        else if ( this == VOT )
            retval = "VOTable";
        
        return retval;
    }


}
