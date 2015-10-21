/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel;

import cfa.vo.vomodel.table.ModelTable;
import cfa.vo.vomodel.table.ModelTableBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Warehouse Class which stores the information to access and build 
 * a variety of 'supported' IVOA Data Model defintions.
 * 
 * IVOA Data Model to be loaded is determined by the Model name.
 * 
 * @author mdittmar
 */
public class DefaultModelBuilder extends AbstractModelBuilder {
    
    // Keep store of which models are supported, and by which method.
    private HashMap<String, String> supported;
    private String modelName;
    private Map<Entry, Entry> overrides = new HashMap();
    public static final String DEFAULT_MODEL_NAME = "SPECTRUM-2.0";
    
    /**
     * Constructor initializes the warehouse of supported VO Data Models.
     * 
     * Note: The DefaultModelBuilder has information to support the generation
     * of multiple IVOA Models, however each instance is configured to a 
     * specific model to facilitate the user overrides mechanism.
     * 
     * @param modelName
     *    {@link String}  Name of IVOA Model of interest.  The value/format
     *                    should match that provided by the model document.
     */
    public DefaultModelBuilder(String modelName)
    {
        supported = new HashMap();
        supported.put("SPECTRUM-1.0","Table:/spectrum_1p1.txt");
        supported.put("SPECTRUM-2.0","Table:/spectrum_2p0.txt");
      //supported.put("PHOTOMETRYPOINT-1.0","Table:/photometrypoint_1p0.txt");

        // Should this validate that modelName is in 'supported'..
        // rather than waiting for the build() command to throw the exception.
        this.modelName = modelName;
    }

    public DefaultModelBuilder overrideEntries(Map<Entry, Entry> overrides) {
        this.overrides = overrides;
        return this;
    }
    
    /**
     * Builds a Model Object pre-loaded with the specified IVOA Data Model definition.
     *
     * @throws IllegalArgumentException if model name is not recognized.
     * @throws IOException on problem loading model definition.
     * 
     * @return Model interface to Object.
     */
    @Override
    public Model build() throws IOException
    {
        Model model = null;
        String key = modelName.toUpperCase();
        
        if ( key == null || key.isEmpty() )
            throw new IllegalArgumentException("No Model identified.");
        
        if (! this.supported.containsKey( key ) )
            throw new IllegalArgumentException("Invalid or unrecognized Model name '"+this.modelName+"'.");

        if ( this.supported.get(key).startsWith("Table")){
            model = load_table( key );
        }

        return model;
    }

     /**
     * Load model definition from ASCII Table.
     * 
     * @param modelName
     * @return
     * @throws IOException 
     */
    private Model load_table( String modelName ) throws IOException
    {
        ModelTable model;
        String resourceFile;
        URL resourceURL;

        // Get Table file location from 'supported' info.
        String record = this.supported.get( modelName );
        resourceFile = record.replace("Table:", "");
        
        try {
            // Generate URL from resource file name, and load the model defs.
            resourceURL = DefaultModelBuilder.class.getResource(resourceFile);
            model = (ModelTable) new ModelTableBuilder(resourceURL)
                    .overrideEntries(overrides)
                    .withModelData(getModelData())
                    .build();
        }
        catch( IOException e ){
            throw e;
        }

        return( model );
    }
    
}
