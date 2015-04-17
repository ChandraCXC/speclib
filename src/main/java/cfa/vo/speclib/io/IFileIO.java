/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.SpectralDataset;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Interface specification for reading/writing Spectral class objects.
 * 
 * @author mdittmar
 */
public interface IFileIO {
    
    /** Read Spectral Dataset from provided URL
     *  @param file 
     *     {@link java.net.URL}
     *  @return
     *    {@link cfa.vo.speclib.SpectralDataset }
     */
    SpectralDataset read( URL file );

    /** Read Spectral Dataset from provided Stream
     *  @param is 
     *     {@link java.io.InputStream}
     *  @return
     *    {@link cfa.vo.speclib.SpectralDataset }
     */
    SpectralDataset read( InputStream is );

    /** Write Spectral Dataset to specified URL
     *  @param file 
     *     {@link java.net.URL}
     *  @param doc
     *    {@link cfa.vo.speclib.SpectralDataset }
     */
    void write( URL file, SpectralDataset doc ) throws IOException;

    /** Write Spectral Dataset to provided Stream 
     *  @param os 
     *     {@link java.io.InputStream}
     *  @param doc
     *    {@link cfa.vo.speclib.SpectralDataset }
     */
    void write( OutputStream os, SpectralDataset doc ) throws IOException;
    
}
