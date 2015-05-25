/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.doc.MPArrayList;
import cfa.vo.speclib.doc.ModelDocument;
import cfa.vo.vomodel.Model;
import cfa.vo.vomodel.ModelFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.NodeList;
import uk.ac.starlink.votable.FieldElement;
import uk.ac.starlink.votable.FieldRefElement;
import uk.ac.starlink.votable.GroupElement;
import uk.ac.starlink.votable.ParamElement;
import uk.ac.starlink.votable.TableElement;
import uk.ac.starlink.votable.VOElement;
import uk.ac.starlink.votable.VOStarTable;

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
        String modelName = this.identifyModel( doc );
        try {
            this.model = new ModelFactory().newInstance( modelName );
        } catch (IOException ex) {
            Logger.getLogger(VOTableIO.class.getName()).log(Level.SEVERE, null, ex);
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
            q = this.convertParam( (ParamElement)child );
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
        int ngroups = 0;  // number of subgroups found
        int nparams = 0;  // number of parameters found

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
          else if ( eltype.equalsIgnoreCase(DOM_TAG_PARAM) )
          {
            q = this.convertParam( (ParamElement)child );
            nparams++;
            mp = q.getModelpath();
            if ( mp == null )
            {
              mp = "Param"+nparams;
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
            //System.out.println("Skipping FIELDref in GROUP ");
          }
          else
            System.out.println("MCD TEMP: Unexpected Node in GROUP = "+eltype);
        }
        
        // Can have empty node if only contained FIELDrefs.. don't need those.
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
    
    private Quantity convertParam( ParamElement param )
    {
        // Split param into equivalent of Field + value
        String strval = param.getAttribute( ATT_TAG_VALUE );
        Quantity q = convertParam( (FieldElement)param, strval );
        return q;
    }
    
    private Quantity convertParam( FieldElement param, String sval )
    {
        String tmpstr = "";
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
            throw new UnsupportedOperationException("Need to get datatype from parameter.");
        }
        //String sval = param.getAttribute( ATT_TAG_VALUE );
        if ( sval == null || sval.equals(""))
            // TODO - proper handling of NULL value
            throw new UnsupportedOperationException("Null value representation not yet supported.");

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
            if ( arlen < 0 )
                q.setValue( Double.valueOf(sval) );
            else
            {
                // Split values, load Double array.
                String[] sarr = sval.split("\\s+");
                Double[] darr = new Double[arlen];
                for ( int ii=0; ii< arlen; ii++)
                    darr[ii] = Double.valueOf(sarr[ii]);
                q.setValue(darr);
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
           System.out.println("MCD TEMP: Bad element "+q.getModelpath());
           throw new UnsupportedOperationException("Datatype not yet supported. ("+dtype+")");
        }
        
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
             if ( (col.data.length > ii)&& (col.data[ii] != null) )
             {
               // Define Quantity from FIELD and String data value.
               q = convertParam( col.info, col.data[ii] );
               rec.put(key, q);
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
    
    private String identifyModel( VOElement top )
    {
        // First try: Param with expected UType.
        NodeList params = top.getElementsByVOTagName(DOM_TAG_PARAM);
        ParamElement param;
        String result = "";

        String expected = "Dataset.DataModel.Name";
        String actual; // Actual model path value
        for ( int ii=0; ii<params.getLength(); ii++)
        {
           param = (ParamElement)params.item(ii);
           actual = param.getAttribute(ATT_TAG_UTYPE);
           if ( actual != null && actual.contains(expected))
           {
             result = param.getAttribute(ATT_TAG_VALUE);
             break;
           }
        }
        return result;
    }

    private String derivePathFromSubnode( ModelDocument node )
    {
        String result = null;
        
        if ( (node != null) && (node.getKeys().size() > 0 ))
        {
           result = (String)(node.getKeys().toArray())[0];
           int last = result.lastIndexOf("_");
           if ( last > 0 )
             result = result.substring(0, last);
        }
        return result;
    }
}
