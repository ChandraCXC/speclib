/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Interface specification for reading/writing IVOA dataset objects.
 * 
 * @author mdittmar
 */
public interface IFileIO {
    
    /** Read IVOA Dataset from provided URL
     *  @param file 
     *     {@link java.net.URL}
     *  @return
     *     Object - Instance of IVOA dataset presented according to it's model.
     */
    Object read( URL file ) throws IOException;

    /** Read IVOA Dataset from provided Stream
     *  @param is 
     *     {@link java.io.InputStream}
     *  @return
     *     Object - Instance of IVOA dataset presented according to it's model.
     */
    Object read( InputStream is ) throws IOException;

    /** Write IVOA Dataset to specified URL
     *  @param file 
     *     {@link java.net.URL}
     *  @param doc
     *     Object - Instance of IVOA dataset presented according to it's model.
     */
    void write( URL file, Object doc ) throws IOException;

    /** Write IVOA Dataset to provided Stream 
     *  @param os 
     *     {@link java.io.InputStream}
     *  @param doc
     *     Object - Instance of IVOA dataset presented according to it's model.
     */
    void write( OutputStream os, Object doc ) throws IOException;
    
}
