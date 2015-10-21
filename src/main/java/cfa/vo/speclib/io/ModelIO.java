/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.doc.MPNode;
import cfa.vo.speclib.doc.ModelObjectFactory;
import cfa.vo.speclib.doc.ModelProxy;
import cfa.vo.speclib.doc.ModeledDocument;
import cfa.vo.vomodel.DefaultModelBuilder;
import cfa.vo.vomodel.Model;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.net.URL;

/**
 * Abstract parent for set of IO classes to serialize objects implemented
 * using {@link cfa.vo.speclib.doc.ModelProxy}.  Each subclass will support
 * (de)serialization of a particular format (VOTable, FITS, etc).
 * 
 * This parent class provides methods to verify and enforce the 
 * requirements of the IVOA Dataset Object being passed to/from the user.
 * 
 * @author mdittmar
 */
public abstract class ModelIO implements IFileIO {

    @Override
    public Object read(URL file) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object read(InputStream is) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(URL file, Object doc) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(OutputStream os, Object doc) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    
    /**
     * Method to generate the appropriate Interface corresponding to the 
     * provided ModeledDocument.
     * 
     * @param doc
     * @return
     */
    protected Object generateInterface( ModeledDocument doc ){
        Object result;
        MPNode top = (MPNode)doc; //TODO: doc.getTopNode()???

        String modelname = doc.identifyModel();
        result = new ModelObjectFactory(modelname).newInstanceByModelPath( top.getModelpath(), top );
        
        return result;
    }

    /**
     * Method validates that the input object is an interface implemented by 
     * the ModelProxy class.  Extracts and returns the underlying ModeledDocument
     * storage.
     * 
     * @param ds
     *     Object - Instance of IVOA dataset presented according to it's model.
     *              This implementation requires interfaces implemented using 
     *              {@link cfa.vo.speclib.doc.ModelProxy}
     * 
     * @return
     *   {@link cfa.vo.speclib.doc.ModeledDocument}  Underlying ModeledDocument storage.
     * 
     * @throws IllegalArgumentException
     *   If input dataset is not a proxy interface implemented by ModelProxy.
     * 
     */
    protected ModeledDocument extractModeledDocument( Object ds ){
        ModeledDocument result = null;

        // Extract underlying ModeledDocument from input dataset
        if ( Proxy.isProxyClass( ds.getClass() )) {
           ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( ds );
           result = h.getDoc();
        }
        else {
            throw new IllegalArgumentException("ds is not a Proxy type, got ("+ds.getClass().getSimpleName()+") instead.");
        }
        
        return result;
    }

    /**
     * Get Model Definitions corresponding to the IVOA Model which the document represents.
     * 
     * @param doc
     * @return
     * @throws IOException
     */
    protected Model getModelDefs( ModeledDocument doc ) throws IOException
    {
        String modelname;
        Model result;
        
        modelname = doc.identifyModel();
        
        //TODO: This check should probably be moved in the DefaultModelBuilder implementation
        if ( modelname == null || modelname.isEmpty() )
          modelname = DefaultModelBuilder.DEFAULT_MODEL_NAME;
        
        result = new DefaultModelBuilder(modelname).build();
        return result;
    }

}
