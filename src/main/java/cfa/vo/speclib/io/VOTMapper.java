/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.doc.MPArrayList;
import cfa.vo.speclib.doc.MPQuantity;
import cfa.vo.speclib.doc.MPNode;
import cfa.vo.speclib.doc.ModeledElement;
import cfa.vo.vomodel.DefaultModelBuilder;
import cfa.vo.vomodel.Model;
import org.w3c.dom.NodeList;
import uk.ac.starlink.votable.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mdittmar
 */
public class VOTMapper {

    /* VOElement DOM node TagNames */
    private final static String DOM_TAG_DOCUMENT  = "VOTABLE";
    private final static String DOM_TAG_RESOURCE  = "RESOURCE";
    private final static String DOM_TAG_TABLE     = "TABLE";
    private final static String DOM_TAG_GROUP     = "GROUP";
    private final static String DOM_TAG_FIELD     = "FIELD";    
    private final static String DOM_TAG_FIELDREF  = "FIELDref";
    private final static String DOM_TAG_PARAM     = "PARAM";
    private final static String DOM_TAG_PARAMREF  = "PARAMref";
    private final static String DOM_TAG_DATA      = "DATA";
    private final static String DOM_TAG_TABLEDATA = "TABLEDATA";
    private final static String DOM_TAG_ROW       = "TR";
    private final static String DOM_TAG_COL       = "TD";
    
    /* VOElement attribute TagNames */
    private final static String ATT_TAG_ARRSIZE  = "arraysize";
    private final static String ATT_TAG_DESC     = "description";
    private final static String ATT_TAG_DTYPE    = "datatype";
    private final static String ATT_TAG_ID       = "ID";
    private final static String ATT_TAG_NAME     = "name";
    private final static String ATT_TAG_REF      = "ref";
    private final static String ATT_TAG_TYPE     = "type";
    private final static String ATT_TAG_UCD      = "ucd";
    private final static String ATT_TAG_UNIT     = "unit";
    private final static String ATT_TAG_UTYPE    = "utype";
    private final static String ATT_TAG_VALUE    = "value";
    private final static String ATT_TAG_VERSION  = "version";
    private final static String ATT_TAG_XMLNS    = "xmlns";
 
    private Model model;   // DataModel specification interface
    private LinkedHashMap< String, Column > tabledata;
    private int level = 1;

    //Constructors
    public VOTMapper()
    {
       model = null;
       tabledata = new LinkedHashMap<String, Column>();
    }
    
    // Public Methods - Any 'flavor' of Mapper should have methods of this form.
    public VOElement convert( MPNode doc )
    {
        VOElement result = null;
        return result;
    }
    
    public MPNode convert( VOElement doc )
    {
        MPNode result;
        
        // Currently supported models all reside in a single RESOURCE.
        NodeList resources = doc.getElementsByVOTagName(DOM_TAG_RESOURCE);
        if ( resources.getLength() > 1 )
            throw new UnsupportedOperationException("Document with >1 RESOURCE, not supported");
        
        result = convertResource( (VOElement)resources.item(0) );
               
        return result;
    }

    public MPNode convert( VOElement doc, Model model )
    {
        MPNode result;

        // Assign provided model to internal storage.
        this.model = model;
        
        // Process document.
        result = convert( doc );

        return result;
    }

    //Private Methods - depends on format structure..
    private VOElement convertResource( MPNode doc )
    {
       VOElement vot = null;
       return vot;   
    }

    /** 
     *  VOTable RESOURCE node may contain:
     *  Attributes
     *    + id
     *    + name
     *    + utype
     *  Elements
     *    + DESCRIPTION
     *    followed by a mixture of
     *    + INFO
     *    + GROUP
     *    + PARAM
     *    + LINK
     *  none of which currently map to modeled elements.
     *  NOTE: A RESOURCE Group could be used to hold the Model declaration
     *    + TABLE (one or more)
     * 
     * @param doc
     * @return 
     */
    private MPNode convertResource( VOElement doc )
    {
        MPNode result;
        MPNode table;  // Table document in Resource.
        MPNode[] tables;
        String mp;
        int ntables = 0;
        
        VOElement[] children = doc.getChildren();
        tables = new MPNode[ children.length ];
        for ( VOElement child: children )
        {
          String eltype = child.getTagName();
          if ( eltype.equalsIgnoreCase(DOM_TAG_TABLE) )
          {
            table = this.convertTable( (TableElement)child );
            tables[ntables] = table;
            ntables++;
            mp = table.getModelpath();
            if ( mp.equals(ModeledElement.MP_UNDEFINED) )
              mp = "TABLE"+ntables;
            else if ( mp.indexOf("_") > -1)  // Unstructured content.
              mp = mp.substring(0, mp.indexOf("_"));

            table.setModelpath(mp);
          }
          else
            System.out.println("MCD TEMP: Unsupported Element type in RESOURCE = "+eltype);
        }
        
        // If only one Table.. Resource == Table
        if ( ntables == 1 ){
            result = tables[0];
        }
        else {
          result = new MPNode();  // Node for combined object
          result.setModelpath("Resource");  //TODO: would need to identify the resource.
          for ( MPNode item: tables )
            result.appendChild(item);
        }

        return result;
    }
    private TableElement convertTable( MPNode doc )
    {
       TableElement tab = null;
       return tab;   
    }
    /**
     * VOTable TABLE Element may contain:
     * Attributes        Elements
     *  + id              + DESCRIPTION
     *  + name            + PARAM
     *  + utype           + FIELD
     *  + ucd             + GROUP
     *                    + LINK
     *                    + DATA
     * 
     * @param doc
     * @return 
     */
    private MPNode convertTable( TableElement doc )
    {
        MPNode table = new MPNode();
        MPNode node; // Group within Table.
        String nodepath = "SpectralDataset"; //TODO: Fixme, determine dynamically.
        String mp;
        MPQuantity q;
        int ngroups = 0;
        int nparams = 0;

        List<ModeledElement> paramStack= new ArrayList<>();
        
        // Find and load data model specification matching this dataset.
        // TODO: without a model spec. this code cannot do very much with the file,
        //       should the Exception go to the client? (maybe local Exception type).
        if ( this.model == null ){
          try {
            this.model = this.identifyModel( doc );
          } catch (IOException ex) {
            Logger.getLogger(VOTableIO.class.getName()).log(Level.SEVERE, null, ex);
          }
        }

        // Assign model path for the table.
        table.setModelpath(nodepath);
        
        // Process FIELD elements to local Column objects
        this.loadColumnData( doc );

        // Gather PARAM elements for processing as a unit.
        VOElement[] children = doc.getChildren();
        for ( VOElement child: children ) {
          String eltype = child.getTagName();
          if ( eltype.equalsIgnoreCase(DOM_TAG_PARAM) ) {
            try {
              q = this.convertParam( (ParamElement)child );
            } catch (Throwable ex) {
              Logger.getLogger(VOTMapper.class.getName()).log(Level.WARNING, null, ex);
              continue;
            }
            nparams++;
            mp = q.getModelpath();
            if ( mp.equals(ModeledElement.MP_UNDEFINED) || (! mp.contains(nodepath))) {
              // Unmodeled element or model path inconsistent with this node.
              mp = nodepath+"_Custom[].CustomParam"+nparams;
              q.setModelpath(mp);
            }
            
            // Add to parameter stack
            paramStack.add( q );
          }
        }
        // Process Parameter stack
        table.build( paramStack );

        // Process remaining element types
        for ( VOElement child: children ) {
          String eltype = child.getTagName();
          
          if ( eltype.equalsIgnoreCase(DOM_TAG_GROUP) ) {
            node = this.convertGroup( (GroupElement)child );
            if ( node != null ) {
              ngroups++;
              mp = node.getModelpath();
              if ( mp.equals(ModeledElement.MP_UNDEFINED) ){
                mp = "Group"+ngroups;
                node.setModelpath( mp );
              }
              table.build( node );
            }
          }
          else if (( eltype.equalsIgnoreCase(DOM_TAG_PARAM) ) ||
                   ( eltype.equalsIgnoreCase(DOM_TAG_FIELD) ) ) {
            //PARAM elements handled above.
            //FIELD element already processed;
            continue;
          }
          else if ( eltype.equalsIgnoreCase(DOM_TAG_DATA) ) {
            // Data node in VOTable, follows all Groups, Params, Fields.. 
            // Generate MPNode of Column data content and add to table.
            MPArrayList<MPNode> data = convertColumnData();
            if ( data != null ) {
              mp = data.getModelpath();
              if ( mp.equals(ModeledElement.MP_UNDEFINED) )
                mp = "Data";
              data.setModelpath( mp );
              table.appendChild( data );
            }
          }
          else
            System.out.println("MCD TEMP: Unsupported Element type in TABLE = "+eltype);
        }
        return table;
    }
    private GroupElement convertGroup( MPNode node )
    {
       GroupElement group = null;
       return group;   
    }
    /**
     * VOTable GROUP element may contain:
     * Attributes:       Elements:
     *  + id              + DESCRIPTION
     *  + name            + FIELDref
     *  + ucd             + PARAM
     *  + utype           + PARAMref
     *  + ref.            + GROUP
     * 
     * @param group
     * @return 
     */
    private MPNode convertGroup( GroupElement group )
    {
        MPNode node = new MPNode();  // this node
        String nodepath;                           // model path for this node
        
        MPQuantity q;     // Quantity element
        String mp;        // model path for element
        int ngroups = 0;  // number of subgroups found
        int nparams = 0;  // number of parameters found
        this.level++;     // Increment depth marker

        // Stack of parameters associated with this group.. must process together.
        List<ModeledElement> paramStack= new ArrayList<>();
        
        // Get model path to group.
        nodepath = identifyNode( group );
        node.setModelpath(nodepath);

        // Gather PARAM and PARAMref elements for processing as a unit.
        VOElement[] children = group.getChildren();
        for ( VOElement child: children ) {
          String eltype = child.getTagName();
          if (( eltype.equalsIgnoreCase(DOM_TAG_PARAM) ) || 
              ( eltype.equalsIgnoreCase(DOM_TAG_PARAMREF) ) )
          {
            try{
              if ( eltype.equalsIgnoreCase(DOM_TAG_PARAM) )
                q = this.convertParam( (ParamElement)child );
              else
                q = this.convertParamRef( (ParamRefElement)child );
            }catch (Throwable ex){
                Logger.getLogger(VOTMapper.class.getName()).log(Level.WARNING, null, ex);
                continue;
            }
            nparams++;
            mp = q.getModelpath();
            if ( mp.equals(ModeledElement.MP_UNDEFINED) || (! mp.contains(nodepath))) {
              // Unmodeled element or model path inconsistent with this node.
              mp = nodepath+"_Custom[].CustomParam"+nparams;
              q.setModelpath(mp);
            }
            
            // Add to parameter stack
            paramStack.add( q );
          }
        }
        // Process Parameter stack
        node.build( paramStack );
        
        // Process the child elements of the Node.. then other types
        for ( VOElement child: children ) {
          String eltype = child.getTagName();
          if ( eltype.equalsIgnoreCase(DOM_TAG_GROUP) ) {
            MPNode subnode = this.convertGroup( (GroupElement)child );
            if ( subnode != null ) {
              ngroups++;
              mp = subnode.getModelpath();
              if ( mp.equals(ModeledElement.MP_UNDEFINED) )
                mp = nodepath+"_Group"+ngroups;
              if ( mp.matches(".+\\[\\]\\.*[a-zA-Z]*$") ) {
                // Array Element.. 
                String arraymp = mp.substring(0, mp.lastIndexOf('[') );
                MPArrayList garr = (MPArrayList)node.getChildByMP(arraymp);
                if ( garr != null )
                  garr.add(subnode);
                else
                {
                  garr = new MPArrayList();
                  garr.setModelpath(arraymp);
                  garr.add(subnode);
                  node.appendChild( garr );
                }
              }
              else
              { // Regular Group.. add to node.
                subnode.setModelpath(mp);
                node.appendChild( subnode );
              }
            }
          }
          else if (( eltype.equalsIgnoreCase(DOM_TAG_PARAM) ) || 
                   ( eltype.equalsIgnoreCase(DOM_TAG_PARAMREF) ) ||
                   ( eltype.equalsIgnoreCase(DOM_TAG_FIELDREF) )
                  )
          {
              //PARAM, PARAMRef elements handled above.
              //FIELDRef elements already processed.
              continue;
          }
          else
            System.out.println("MCD TEMP: Unsupported Element type in GROUP = "+eltype);
        }
        this.level--;     // Decrement depth marker
        
        // Can have empty node if only contained PARAMrefs or FIELDrefs.. don't need those.
        if ( ngroups + nparams == 0 )
          return null;
        else
          return node;
    }
    
    private ParamElement convertParam( Quantity q )
    {
       ParamElement param = null;
       return param;   
    }
    
    private MPQuantity convertParam( ParamElement param ) throws IOException
    {
        // Split param into equivalent of Field + value
        String strval = param.getAttribute( ATT_TAG_VALUE );
        MPQuantity q = convertParam( (FieldElement)param, strval );
        return q;
    }
    
    private MPQuantity convertParam( FieldElement param, String sval ) throws IOException
    {
        String tmpstr;
        MPQuantity q = new MPQuantity();
            
        // UType - identifies element within model.
        //   - get index of this element in the model spec.
        String utype = param.getAttribute(ATT_TAG_UTYPE);
        Integer ndx = this.model.getRecordIndexByTag( utype );
        if ( ndx > 0 )
        {
          q.setModelpath( this.model.getUtype(ndx).getModelPath() );
          q.setUtype(utype);
        }
        else
        { // preserve user utype, but do not set model path.
          q.setUtype(utype);
        }

        if ( param.hasAttribute(ATT_TAG_ID) )
        {
          tmpstr = param.getAttribute(ATT_TAG_ID);
          q.setID(tmpstr);
        }

        tmpstr = null;
        VOElement desc = (VOElement)param.getChildByName(ATT_TAG_DESC.toUpperCase());
        if ( desc != null )
          tmpstr = desc.getTextContent();
        else if ( param.hasAttribute(ATT_TAG_DESC) )
          tmpstr = param.getAttribute(ATT_TAG_DESC);
        if ( tmpstr != null )
          q.setDescription(tmpstr);
    
        if ( param.hasAttribute(ATT_TAG_NAME) ) {
            tmpstr = param.getAttribute(ATT_TAG_NAME);
            q.setName(tmpstr);
        }
        
        if ( param.hasAttribute(ATT_TAG_UCD) ) {
            tmpstr = param.getAttribute(ATT_TAG_UCD);
            q.setUCD(tmpstr);
        }
        
        if ( param.hasAttribute(ATT_TAG_UNIT) ) {
            tmpstr = param.getAttribute(ATT_TAG_UNIT);
            q.setUnit(tmpstr);
        }
        
        // DataType, ArrSize, Value
        String dtype;
        if ( ndx > 0 )
            dtype = this.model.getType(ndx);
        else
        {
            //TODO- Unmodeled element, Convert VOTable dtype to Java data type
            //dtype = convertDType( param.getAttribute( ATT_TAG_DTYPE ) );
            return q;
            //throw new UnsupportedOperationException("Need to get datatype from parameter.");
        }

        // TODO - proper handling of NULL value by dtype.. 
        // TODO - handle VOTable NULL value assignment.
        int arlen = -1;
        if ( param.hasAttribute(ATT_TAG_ARRSIZE ) && !dtype.equals("char"))
        {
           String arrsiz = param.getAttribute(ATT_TAG_ARRSIZE);
           if ( arrsiz.equals("*"))
               arlen = sval.split("\\s").length;
           else
               arlen = Integer.valueOf(arrsiz).intValue();
        }

        if ( dtype.equalsIgnoreCase("Double") )
        {   // Double, Double[]
            if ( arlen < 0 ){
              if ( sval == null || sval.isEmpty())
                q.setValue( Double.NaN );
              else
                q.setValue( Double.valueOf(sval) );
            }
            else {
                Double[] darr = new Double[arlen];
                if ( sval == null || sval.isEmpty()){
                  for ( int ii=0; ii< arlen; ii++)
                    darr[ii] = Double.NaN;
                }
                else {
                  // Split values, load Double array.
                  String[] sarr = sval.split("\\s+");
                  for ( int ii=0; ii< arlen; ii++)
                    darr[ii] = Double.valueOf(sarr[ii]);
                }
                q.setValue(darr);
            }
        }
        else if ( dtype.equalsIgnoreCase( "Long" ) )
        {   // Long
            if ( arlen < 0 )
                q.setValue( Long.valueOf(sval) );
            else {
                // Split values, load Double array.
                String[] sarr = sval.split("\\s+");
                Long[] iarr = new Long[arlen];
                for ( int ii=0; ii< arlen; ii++)
                    iarr[ii] = Long.valueOf(sarr[ii]);
                q.setValue(iarr);
            }
        }
        else if ( dtype.equalsIgnoreCase( "Integer" ) )
        {   // Integer
            if ( arlen < 0 )
                q.setValue( Integer.valueOf(sval) );
            else
            {
                // Split values, load Double array.
                String[] sarr = sval.split("\\s+");
                Integer[] iarr = new Integer[arlen];
                for ( int ii=0; ii< arlen; ii++)
                    iarr[ii] = Integer.valueOf(sarr[ii]);
                q.setValue(iarr);
            }
        }
        else if ( dtype.equalsIgnoreCase( "String" ) ) 
        {
            q.setValue( sval );
        }
        else if ( dtype.equalsIgnoreCase( "URL" ) ) 
        {
           URL url = null;
           try {
             url = new URL(sval);
           } catch (MalformedURLException ex) {
               //TODO - Throw exception? Log as validation error? or what?
                Logger.getLogger(VOTMapper.class.getName()).log(Level.SEVERE, null, ex);
           }
            q.setValue( url );
        }
        else if ( dtype.equalsIgnoreCase( "URI" ) ) 
        {
           URI uri = null;
           try {
             uri = new URI(sval);
           } catch (URISyntaxException ex) {
             //TODO - Throw exception? Log as validation error? or what?
             Logger.getLogger(VOTMapper.class.getName()).log(Level.SEVERE, null, ex);
           }
           q.setValue( uri );
        }
        else if ( dtype.equalsIgnoreCase( "Boolean" ) )
        {
            q.setValue( Boolean.valueOf(sval) );
        }
        else
        {
           // Instances where element does not match a supported datatype.
           // Example; ObsConfig.Instrument utype placed on ObsConfig.Instrument.Name param.
           // NOTE: when dtype of unmodeled elements are pulled from Param, this could be
           //       either a modeled or unmodeled element.
           // TODO - ModeledElementException || UnModeledElementException?
           if ( q.getModelpath().equals(ModeledElement.MP_UNDEFINED) )
             throw new IOException("UnModeled element with unsupported data type. dtype="+dtype);
           else
             throw new IOException("Modeled element with unexpected data type. mp="+q.getModelpath()+"  dtype="+dtype);
        }
        
        return q;        
    }
    
    private MPQuantity convertParamRef( ParamRefElement paramref ) throws IOException
    {        
        // Get copy of associated PARAM element
        ParamElement param = (ParamElement)paramref.getParam().cloneNode(true);

        // Override attributes defined on the reference
        if ( paramref.hasAttribute(ATT_TAG_UCD) )
            param.setAttribute(ATT_TAG_UCD, paramref.getAttribute(ATT_TAG_UCD) );
        
        if ( paramref.hasAttribute(ATT_TAG_UTYPE) )
           param.setAttribute(ATT_TAG_UTYPE, paramref.getAttribute(ATT_TAG_UTYPE) );

        // Define Quantity for this element
        MPQuantity q = convertParam( param );
        return q;
    }
    
    private void loadColumnData( TableElement table )
    {
        Column col;   // Column corresponding to FIELD element
        String mp;    // model path for the column
        int ndx;      // index of element in Model spec.

        // Pull list of FIELD elements from table, these each correspond
        // to a table column in the DATA section.
        NodeList fields = table.getElementsByVOTagName(DOM_TAG_FIELD);
        for ( int ii=0; ii<fields.getLength(); ii++) {
          col = new Column();
          col.info = (FieldElement)fields.item(ii);

          // Initialize model path for the column, 
          // If utype set on FIELD, try that; otherwise, use default.
          ndx = model.getRecordIndexByTag(col.info.getAttribute(ATT_TAG_UTYPE));

          if ( ndx >= 0 )
              mp = this.model.getUtype(ndx).getModelPath();
          else
              mp = "_Column"+ii; // default

          this.tabledata.put(mp, col);
        }
        
        //Populate Data Arrays from DATA Element of Table.
        this.processColumnData( table );
        
        // Pull list of FIELDref elements from table, if present, these each 
        // refer to an existing Column, but may represent a different model
        // element.
        String ref;
        String id;
        String key;
        fields = table.getElementsByVOTagName(DOM_TAG_FIELDREF);
        for ( int ii=0; ii<fields.getLength(); ii++) {
          FieldRefElement field = (FieldRefElement)fields.item(ii);
          ref = field.getAttribute(ATT_TAG_REF);
          key = "";
          col = null;

          // Find matching Column
          Object[] keys = this.tabledata.keySet().toArray();
          for ( int jj=0; jj < keys.length; jj++ ) {
             key = (String)keys[jj];
             col = (Column)this.tabledata.get(key);
             id = col.info.getAttribute(ATT_TAG_ID);
             if ( ref.equalsIgnoreCase(id) )
                break; // Found corresponding column.
             col = null;
          }
          
          if ( col == null )
              throw new UnsupportedOperationException("FIELDref (ref="+ref+") does not reference existing column.");

          // Get model path for FIELDref
          ndx = model.getRecordIndexByTag(field.getAttribute(ATT_TAG_UTYPE));
          if ( ndx >= 0 ) {
             mp = this.model.getUtype(ndx).getModelPath();
             
             // Check Model Path against Column key
             if ( key.startsWith("_Column") ) {
                 // Matches by default.. replace entry with mp key
                 this.tabledata.remove( key );
                 this.tabledata.put( mp, col );
             }
             else if ( ! key.equals(mp) ) {
                 // Reference defines different model element
                 Column newcol = new Column();
                 newcol.info = (FieldElement)col.info.cloneNode(false);
                 newcol.data = col.data;  // same data.
                 
                 // Add new model element to list, swap col for update.
                 this.tabledata.put(mp, newcol);
                 col = newcol;
             }
          }
          // else Fieldref with no utype.. assume match
          
          // Update utype and ucd attributes from FIELDref
          if ( field.hasAttribute(ATT_TAG_UTYPE))
            col.info.setAttribute(ATT_TAG_UTYPE, field.getAttribute(ATT_TAG_UTYPE));
          if ( field.hasAttribute(ATT_TAG_UCD))
            col.info.setAttribute(ATT_TAG_UCD, field.getAttribute(ATT_TAG_UCD));
          if ( field.hasAttribute(ATT_TAG_NAME))
            col.info.setAttribute(ATT_TAG_NAME, field.getAttribute(ATT_TAG_NAME));

        } // end FIELDref loop        
    }
    
    private void processColumnData( TableElement table )
    {
        Column col;     // local Column object
        String strval;  // holds data value as string

        // Get keys from column map.. in order of entry
        Object[] keys = this.tabledata.keySet().toArray();

        // Convert TableElement to VOStarTable, which is the best way to 
        // access the DATA content which can be in many forms.
        try {
          VOStarTable startab = new VOStarTable( table );

          int nrows = (int)startab.getRowCount();
          int ncols = (int)startab.getColumnCount();
          if ( ncols != keys.length )
            throw new UnsupportedOperationException("Star Table ncols ("+ncols+") != Fields ncols ("+keys.length+")");

          // Transfer Table data values to Column objects.
          for ( int icol=0; icol < ncols; icol++ ) {
            col = this.tabledata.get((String)keys[icol]);
            col.data = new String[nrows];
            
            for ( int irow=0; irow < nrows; irow++ ) {
              strval = startab.getCell(irow, icol).toString();
              col.data[irow] = strval;
            }
          }
        } catch (IOException ex) {
            Logger.getLogger(VOTMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private MPArrayList<MPNode> convertColumnData()
    {
       Column col;
       MPQuantity q;
       String key;
       String mp;

       // Determine number of rows.. should be same across Columns
       int nrows = -1;
       for ( Column c : this.tabledata.values() )
           if ( (c.data != null) && (c.data.length > nrows) )
               nrows = c.data.length;

       // Get model path for Data node - use first tabledata entry
       String[] keys = this.tabledata.keySet().toArray( new String[0] );
       mp = getSubPath( keys[0], "Data[]" );
       
       // Create List for Data node.
       MPArrayList<MPNode> data = new MPArrayList( nrows );
       data.setModelpath( mp.substring(0, mp.indexOf("_Data[]")+5) );  // Model path for List element
       
       // list for data values which form the Data node content (eg SPPoint)
       List<ModeledElement> paramStack= new ArrayList<>();
       for ( int ii=0; ii < nrows; ii++ ){
          // Create MPNode object to hold this record/point.
          MPNode rec = new MPNode( mp );
          data.add(rec);
          
          // Clear stack of point values for this node.
          paramStack.clear();
          
          // Add Quantity for each Column with data.
          for ( int jj=0; jj < keys.length; jj++ ) {
             key = keys[jj];
             col = (Column)this.tabledata.get(key);
             if ( (col.data.length > ii)&& (col.data[ii] != null) ){
               try {
                 // Define Quantity from FIELD and String data value.
                 q = convertParam( col.info, col.data[ii] );
                 q.setModelpath( key );
                 // Add to stack
                 paramStack.add(q);
               } catch (IOException ex) {
                 Logger.getLogger(VOTMapper.class.getName()).log(Level.WARNING, null, ex);
               }
             }
          }
          
          // Process Parameter stack, folding data values into node hierarchy.
          rec.build( paramStack );          
       }
       return data;
    }
    
    private Model identifyModel( VOElement top ) throws IOException
    {
        // First try: Param with expected UType/Tag.
        NodeList params = top.getElementsByVOTagName(DOM_TAG_PARAM);
        ParamElement param;
        String modelname = "";
        Model result;

        String expected  = "Dataset.DataModel.Name"; // Spectral-2.0
        String expected2 = "Spectrum.DataModel";     // Spectrum-1.*
        String actual; // Actual model path value
        for ( int ii=0; ii<params.getLength(); ii++) {
           param = (ParamElement)params.item(ii);
           actual = param.getAttribute(ATT_TAG_UTYPE);
           if ( actual != null && actual.contains(expected)) {
             modelname = param.getAttribute(ATT_TAG_VALUE);
             break;
           }
           else if ( actual != null && actual.contains(expected2)) {
             modelname = param.getAttribute(ATT_TAG_VALUE);
             break;
           }
        }
        result = new DefaultModelBuilder(modelname).build();
        return result;
    }
    
    // Identify Node..
    //   extracted from the first modeled parameter.
    private String identifyNode( GroupElement node )
    {
        String nodepath = ModeledElement.MP_UNDEFINED;
        MPQuantity q;
        String mp = ModeledElement.MP_UNDEFINED;

        // TODO: - node may have utype, could check this against model spec.
        VOElement[] children = node.getChildren();
        for ( VOElement child: children ) {
          String eltype = child.getTagName();
          if ( eltype.equalsIgnoreCase(DOM_TAG_GROUP) ){
              mp = identifyNode( (GroupElement)child );
          }
          else if (( eltype.equalsIgnoreCase(DOM_TAG_PARAM) ) || 
                   ( eltype.equalsIgnoreCase(DOM_TAG_PARAMREF) ) )
          {
            try{
              if ( eltype.equalsIgnoreCase(DOM_TAG_PARAM) )
                q = this.convertParam( (ParamElement)child );
              else
                q = this.convertParamRef( (ParamRefElement)child );
            }catch (Throwable ex){
                continue;
            }
            mp = q.getModelpath();
          }
          if ( !mp.equals(ModeledElement.MP_UNDEFINED) ) {
            // First modeled param, get node path to the current level.
            nodepath = getSubPath( mp, this.level );
            break;
          }
        }

        return nodepath;
    }

    // MP.getSubPath( int level )
    private String getSubPath( String mp, int depth )
    {
       String result = mp;
       int last = 0;
       int ii = 0;
       while ( (ii < depth) && ( last > -1 )) {
          last = mp.indexOf("_",last+1);
          ii++;
       }
       if ( last > 0 )
         result = mp.substring(0, last);

       return result;
    }
    
    // MP.getSubPath( String nodename )
    private String getSubPath( String mp, String nodename )
    {
       String result = mp;
       
       // Get number of levels in the input path.
       int n = mp.length() - mp.replace("_","").length();
       
       // find level with matching node name (starts with)
       for ( int ii=1; ii <= n; ii++ ) {
           result = this.getSubPath(mp, ii);
           if ( result.indexOf("_"+nodename) > -1 ){
               break;  // This is the matching node..
           }
       }
       return result;
    }
}
