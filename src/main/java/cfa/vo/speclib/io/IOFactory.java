/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

/**
 * Class to invoke the appropriate I/O provider based on desired format.
 * The file format is specified using the IOFormat enumeration provided 
 * with this package.
 * 
 * @author mdittmar
 */
public class IOFactory {
    
    /**
     * Method to select and instantiate the appropriate class for 
     * reading and writing Spectral data in the provided format.
     */
    IFileIO newInstance(FileFormat format)
    {
        IFileIO result = null;

        if ( format == FileFormat.VOT )
        {
            result = new VOTableIO();
        }
        else if ( format == FileFormat.FITS )
        {
            throw new UnsupportedOperationException("Not supported yet.");                    
        }
        return result;
    }
}
