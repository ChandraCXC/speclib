/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.doc.MPArrayList;
import cfa.vo.speclib.doc.ModelDocument;
import cfa.vo.vomodel.DefaultModelBuilder;
import cfa.vo.vomodel.Model;
import org.w3c.dom.NodeList;
import uk.ac.starlink.votable.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
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
    private final static String ATT_TAG_ID       = "id";
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
    
    // Public Methods - Any 'flavor' or Mapper should have methods of this form.
    public VOElement convert( ModelDocument doc )
    {
        VOElement result = null;
        return result;
    }
    
    public ModelDocument convert( VOElement doc )
    {
        ModelDocument result;
        
        // Currently supported models all reside in a single RESOURCE.
        NodeList resources = doc.getElementsByVOTagName(DOM_TAG_RESOURCE);
        if ( resources.getLength() > 1 )
            throw new UnsupportedOperationException("Document with >1 RESOURCE, not supported");
        
        result = convertResource( (VOElement)resources.item(0) );
               
        return result;
    }

    public ModelDocument convert( VOElement doc, Model model )
    {
        ModelDocument result;

        // Assign provided model to internal storage.
        this.model = model;
        
        // Process document.
        result = convert( doc );

        return result;
    }

    //Private Methods - depends on format structure..
    private VOElement convertResource( ModelDocument doc )
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
    private ModelDocument convertResource( VOElement doc )
    {
        ModelDocument result = new ModelDocument();
        ModelDocument table; // Table document in Resource.
        String mp;
        int ntables = 0;
        
        VOElement[] children = doc.getChildren();
        for ( VOElement child: children )
        {
          String eltype = child.getTagName();
          if ( eltype.equalsIgnoreCase(DOM_TAG_TABLE) )
          {
            ntables++;
            table = this.convertTable( (TableElement)child );
            mp = derivePathFromSubnode(table);
            if ( mp == null )
              mp = "TABLE"+ntables;
            else if ( mp.indexOf("_")> -1)  // Unstructured content.
              mp = mp.substring(0, mp.indexOf("_"));

            result.put(mp, table);
          }
          else
            System.out.println("MCD TEMP: Unexpected Node in RESOURCE = "+eltype);
        }
        return result;
    }
    private TableElement convertTable( ModelDocument doc )
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
    private ModelDocument convertTable( TableElement doc )
    {
        ModelDocument table = new ModelDocument();
        ModelDocument node; // Group within Table.
        String mp;
        Quantity q;
        int ngroups = 0;
        
        // Find and load data model specification matching this dataset.
        if ( this.model == null ){
          try {
            this.model = this.identifyModel( doc );
          } catch (IOException ex) {
            Logger.getLogger(VOTableIO.class.getName()).log(Level.SEVERE, null, ex);
          }
        }

        // Process FIELD elements to local Column objects
        this.loadColumnData( doc );

        // Process the child elements of the table.. 
        VOElement[] children = doc.getChildren();
        for ( VOElement child: children )
        {
          String eltype = child.getTagName();
          if ( eltype.equalsIgnoreCase(DOM_TAG_GROUP) )
          {
            node = this.convertGroup( (GroupElement)child );
            if ( node != null )
            {
              ngroups++;
              mp = derivePathFromSubnode(node);
              if ( mp == null )
                mp = "Group"+ngroups;
              table.put(mp, node);
            }
          }
          else if ( eltype.equalsIgnoreCase(DOM_TAG_PARAM) )
          {
            try {
              q = this.convertParam( (ParamElement)child );
            } catch (IOException ex) {
              Logger.getLogger(VOTMapper.class.getName()).log(Level.WARNING, null, ex);
              continue;
            }
            if ( q.getModelpath() == null )
            {
              q.setModelpath("SpectralDataset_Custom");
              continue;  //TODO: proper handling of unmodeled parameters.
            }

            table.put( q.getModelpath(), q );
          }
          else if ( eltype.equalsIgnoreCase(DOM_TAG_FIELD) )
          {
            //System.out.println(" Skip FIELD Node in TABLE ");
          }
          else if ( eltype.equalsIgnoreCase(DOM_TAG_DATA) )
          {
            // Data node, follows all Groups, Params, Fields.. 
            // Generate ModelDocument of Column data content and add to table.
            MPArrayList<ModelDocument> data = convertColumnData();
            if ( data != null )
            {
              mp = data.getModelpath();
              if ( mp == null )
                mp = "Data";
              table.put( mp, data );
            }
          }
          else
            System.out.println("MCD TEMP: Unexpected Node in TABLE = "+eltype);
        }
        return table;
    }
    private GroupElement convertGroup( ModelDocument node )
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
    private ModelDocument convertGroup( GroupElement group )
    {
        ModelDocument node = new ModelDocument();
        Quantity q;       // Quantity element
        String mp;        // model path for element
        String gp = null; // model path for this group
        int ngroups = 0;  // number of subgroups found
        int nparams = 0;  // number of parameters found
        this.level++;     // Increment depth marker

        VOElement[] children = group.getChildren();
        for ( VOElement child: children )
        {
          String eltype = child.getTagName();
          if ( eltype.equalsIgnoreCase(DOM_TAG_GROUP) )
          {
            ModelDocument subnode = this.convertGroup( (GroupElement)child );
            if ( subnode != null )
            {
              ngroups++;
              mp = derivePathFromSubnode(subnode);
              if ( mp == null )
                mp = "Group"+ngroups;
              if ( mp.matches(".+\\[\\]\\.*[a-zA-Z]*$") )
              { // Array Element.. 
                String arraymp = mp.substring(0, mp.lastIndexOf('[') );
                if ( node.contains(arraymp))
                  ((MPArrayList)node.get(arraymp)).add(subnode);
                else
                {
                  MPArrayList garr = new MPArrayList();
                  garr.setModelpath(arraymp);
                  garr.add(subnode);
                  node.put( arraymp, garr );
                }
              }
              else
              { // Regular Group.. add to node.
                node.put( mp, subnode );
              }
            }
          }
          else if (( eltype.equalsIgnoreCase(DOM_TAG_PARAM) ) || 
                   ( eltype.equalsIgnoreCase(DOM_TAG_PARAMREF) ) )
          {
            try{
              if ( eltype.equalsIgnoreCase(DOM_TAG_PARAM) )
                q = this.convertParam( (ParamElement)child );
              else
                q = this.convertParamRef( (ParamRefElement)child );
            }catch (IOException ex){
                Logger.getLogger(VOTMapper.class.getName()).log(Level.WARNING, null, ex);
                continue;
            }
            nparams++;
            mp = q.getModelpath();
            if ( mp == null )
            {
              mp = "CustomParam"+nparams;
              q.setModelpath(mp);
              continue;
            }
            if ( gp == null )
              gp = getSubPath( mp , this.level );
            if (! mp.contains(gp))
            { // model path inconsistent with the group.. mark as custom
                System.out.println("MCD TEMP: Override bad group content -"+mp);
              mp = "CustomParam"+nparams;
              q.setModelpath(mp);
              continue;
            }
            if ( mp.matches(".+\\[\\]\\.*[a-zA-Z]*$") )
            { // Array Element.. 
              String arraymp = mp.substring(0, mp.lastIndexOf('[') );
              if ( node.contains(arraymp))
                  ((MPArrayList)node.get(arraymp)).add(q);
              else
              {
                  MPArrayList qarr = new MPArrayList();
                  qarr.setModelpath(arraymp);
                  qarr.add(q);
                  node.put( arraymp, qarr );
              }
            }
            else
            { // Regular Parameter.. add to node.
              node.put( mp, q );
            }
          }
          else if ( eltype.equalsIgnoreCase(DOM_TAG_FIELDREF) )
          {
            //System.out.println("Skipping FIELDref in GROUP, already handled separately. ");
          }
          else
            System.out.println("MCD TEMP: Unexpected Node in GROUP = "+eltype);
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
    
    private Quantity convertParam( ParamElement param ) throws IOException
    {
        // Split param into equivalent of Field + value
        String strval = param.getAttribute( ATT_TAG_VALUE );
        Quantity q = convertParam( (FieldElement)param, strval );
        return q;
    }
    
    private Quantity convertParam( FieldElement param, String sval ) throws IOException
    {
        String tmpstr;
        Quantity q = new Quantity();
            
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
          System.out.println("MCD TEMP: Unmodeled Element - "+utype);
        
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
    
        if ( param.hasAttribute(ATT_TAG_NAME) )
        {
            tmpstr = param.getAttribute(ATT_TAG_NAME);
            q.setName(tmpstr);
        }
        
        if ( param.hasAttribute(ATT_TAG_UCD) )
        {
            tmpstr = param.getAttribute(ATT_TAG_UCD);
            q.setUCD(tmpstr);
        }
        
        if ( param.hasAttribute(ATT_TAG_UNIT) )
        {
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
           if ( q.getModelpath() == null )
             throw new IOException("UnModeled element with unsupported data type. dtype="+dtype);
           else
             throw new IOException("Modeled element with unexpected data type. mp="+q.getModelpath()+"  dtype="+dtype);
        }
        
        return q;        
    }
    
    private Quantity convertParamRef( ParamRefElement paramref ) throws IOException
    {        
        // Get copy of associated PARAM element
        ParamElement param = (ParamElement)paramref.getParam().cloneNode(true);

        // Override attributes defined on the reference
        if ( paramref.hasAttribute(ATT_TAG_UCD) )
            param.setAttribute(ATT_TAG_UCD, paramref.getAttribute(ATT_TAG_UCD) );
        
        if ( paramref.hasAttribute(ATT_TAG_UTYPE) )
           param.setAttribute(ATT_TAG_UTYPE, paramref.getAttribute(ATT_TAG_UTYPE) );

        // Define Quantity for this element
        Quantity q = convertParam( param );
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
        for ( int ii=0; ii<fields.getLength(); ii++)
        {
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
        for ( int ii=0; ii<fields.getLength(); ii++)
        {
          FieldRefElement field = (FieldRefElement)fields.item(ii);
          ref = field.getAttribute(ATT_TAG_REF);
          key = "";
          col = null;

          // Find matching Column
          Object[] keys = this.tabledata.keySet().toArray();
          for ( int jj=0; jj < keys.length; jj++ )
          {
             key = (String)keys[jj];
             col = (Column)this.tabledata.get(key);
             id = col.info.getAttribute(ATT_TAG_ID);
             if ( ref.equalsIgnoreCase(id) )
                break; // Found corresponding column.
          }
          
          if ( col == null )
              throw new UnsupportedOperationException("FIELDref (ref="+ref+") does not reference existing column.");

          // Get model path for FIELDref
          ndx = model.getRecordIndexByTag(field.getAttribute(ATT_TAG_UTYPE));
          if ( ndx >= 0 )
          {
             mp = this.model.getUtype(ndx).getModelPath();
             
             // Check Model Path against Column key
             if ( key.startsWith("_Column") )
             {
                 // Matches by default.. replace entry with mp key
                 this.tabledata.remove( key );
                 this.tabledata.put( mp, col );
             }
             else if ( ! key.equals(mp) )
             {
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
          for ( int icol=0; icol < ncols; icol++ )
          {
            col = this.tabledata.get((String)keys[icol]);
            col.data = new String[nrows];
            
            for ( int irow=0; irow < nrows; irow++ )
            {
              strval = startab.getCell(irow, icol).toString();
              col.data[irow] = strval;
            }
          }
        } catch (IOException ex) {
            Logger.getLogger(VOTMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private MPArrayList<ModelDocument> convertColumnData()
    {
       Column col;
       Quantity q;
       String key;
       String mp;

       // Determine number of rows.. should be same across Columns
       int nrows = -1;
       for ( Column c : this.tabledata.values() )
           if ( (c.data != null) && (c.data.length > nrows) )
               nrows = c.data.length;

       // Create List for Data node.
       MPArrayList<ModelDocument> data = new MPArrayList( nrows );
       Object[] keys = this.tabledata.keySet().toArray();
       mp = ((String)keys[0]).substring(0, ((String)keys[0]).indexOf("_Data[]")+5); // TODO: Kinda Hacky
       data.setModelpath( mp );  // Model path for List element
       
       for ( int ii=0; ii < nrows; ii++ ){
          // Create ModelDocument object to hold this record/point.
          ModelDocument rec = new ModelDocument();
          data.add(rec);
          for ( int jj=0; jj < keys.length; jj++ )
          {  // Add Quantity for each Column with data.
             key = (String)keys[jj];
             col = (Column)this.tabledata.get(key);
             if ( (col.data.length > ii)&& (col.data[ii] != null) ){
               try {
                 // Define Quantity from FIELD and String data value.
                 q = convertParam( col.info, col.data[ii] );
                 rec.put(key, q);
               } catch (IOException ex) {
                 Logger.getLogger(VOTMapper.class.getName()).log(Level.WARNING, null, ex);
               }
             }
          }
          
          // Handle Array elements
          String arrpath;
          String grppath;
          MPArrayList<ModelDocument> list;
          ModelDocument subnode;
          for ( int jj=0; jj < keys.length; jj++ ){
            key = (String)keys[jj];
            int ndx = key.indexOf("[]", mp.length()+2);
            if ( ndx == -1 )
                continue;

            // Get or Create List
            arrpath = key.substring(0, ndx );
            if ( rec.contains(arrpath))
            {
              list = (MPArrayList)rec.get( arrpath );
            }
            else
            {
              list = new MPArrayList();
              list.setModelpath(arrpath);
              rec.put( arrpath, list );
            }
            // Create new subnode for this grouping
            // move element from this node to subnode.
            subnode = new ModelDocument();
            subnode.put(key, rec.get(key));
            rec.remove(key);

            // extract model path for the group (what type of element is it?
            ndx = key.indexOf("_", arrpath.length()); // nodes past this elem?
            if ( ndx == -1 )
              grppath = key;
            else
              grppath = key.substring(0, ndx);
            
            // scan forward for other entries in this group (assuming in sequence)
            int kk;
            for ( kk = jj+1; kk < keys.length; kk++ ){
               key = (String)keys[kk];
               if ( key.startsWith( arrpath ) )
               { // element is member of same array list.
                 if ( !key.startsWith( grppath ) || subnode.contains(key) )
                 {  // but not same group.. cut subnode and start a new one
                    list.add(subnode);
                    subnode = new ModelDocument();
                    ndx = key.indexOf("_", arrpath.length()); // nodes past this elem?
                    if ( ndx == -1 )
                      grppath = key;
                    else
                      grppath = key.substring(0, ndx);
                 }
                 // and member of same group.. move to subnode
                 subnode.put( key, rec.get(key) );
                 rec.remove( key );
               }
               else
                 break;
            }
            // Add completed subnode to list
            list.add(subnode);
            jj = kk-1;
          }
       }

       return data;
    }
    
    private Model identifyModel( VOElement top ) throws IOException
    {
        // First try: Param with expected UType.
        NodeList params = top.getElementsByVOTagName(DOM_TAG_PARAM);
        ParamElement param;
        String modelname = "";
        Model result;

        String expected = "Dataset.DataModel.Name";
        String actual; // Actual model path value
        for ( int ii=0; ii<params.getLength(); ii++)
        {
           param = (ParamElement)params.item(ii);
           actual = param.getAttribute(ATT_TAG_UTYPE);
           if ( actual != null && actual.contains(expected))
           {
             modelname = param.getAttribute(ATT_TAG_VALUE);
             break;
           }
        }
        result = new DefaultModelBuilder(modelname).build();
        return result;
    }

    private String derivePathFromSubnode( ModelDocument node )
    {
        String result = null;
        
        if ( (node != null) && (node.getKeys().size() > 0 ))
        {
           String first = (String)(node.getKeys().toArray())[0];
           result = getSubPath(first, this.level+1);
        }
        return result;
    }
    private String getSubPath( String mp, int depth )
    {
       int last = 0;
       int ii = 0;
       while ( (ii < depth) && ( last > -1 ))
       {
          last = mp.indexOf("_",last+1);
          ii++;
       }
       if ( last > 0 )
         mp = mp.substring(0, last);

       return mp;
    }
}
