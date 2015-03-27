/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel;

import cfa.vo.vomodel.table.ModelTable;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 *
 * @author mdittmar
 */
public class ModelFactory {
    
    // Keep store of which models are supported, and by which method.
    private HashMap<String, String> supported;
    
    public ModelFactory()
    {
        supported.put("SPECTRUM-2.0","Table");
    }
    
    /**
     * Constructor to generate an Object pre-loaded with the specified 
     * VO Data Model definition.
     * 
     * @param modelName  VO Data Model name as specified in model document.
     *                   expected format [name]-[version].[subversion]
     * @throws IllegalArgumentException if model name is not recognized.
     * @throws IOException on problem loading model definition.
     * @return Model interface to Object.
     */
    public Model newInstance( String modelName ) throws IOException
    {
        Model model;
        String key = modelName.toUpperCase();
        URL file;
        
        if (! this.supported.containsKey( key ) )
            throw new IllegalArgumentException("Invalid or unrecognized Model name.");

        if ( this.supported.get(key).equals("Table"))
        {
            model = load_table( key );
        }
        else
            model = null;

        return model;
    }
    
    private Model load_table( String modelName ) throws IOException
    {
        ModelTable model;
        String resourceFile;
        URL resourceURL;

        boolean fail=false;
        
        if ( modelName.equals("SPECTRUM-1.03") )
        {
            resourceFile = "/spectrum_1p03.txt";
        }
        else if ( modelName.equals( "SPECTRUM-2.0" ) )
        {
            resourceFile = "/spectrum_2p0.txt";
        }
        else if ( modelName.equals("PHOTOMETRYPOINT-1.0") )
        {
            resourceFile = "/photometrypoint_1p0.txt";
        }
        else
            throw new IllegalArgumentException("Invalid or unrecognized Model name.");

        try {
            resourceURL = ModelFactory.class.getResource(resourceFile);
            model = new ModelTable();
            model.read( resourceURL );
        }
        catch( IOException e )
        {
            throw e;
        }

        return( model );
    }
    
}
