/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.SpectralDataset;
import cfa.vo.speclib.doc.MPArrayList;
import cfa.vo.speclib.doc.ModelDocument;
import cfa.vo.speclib.doc.ModelProxy;
import cfa.vo.vomodel.Model;
import cfa.vo.vomodel.ModelFactory;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import uk.ac.starlink.votable.FieldElement;
import uk.ac.starlink.votable.FieldRefElement;
import uk.ac.starlink.votable.GroupElement;
import uk.ac.starlink.votable.ParamElement;
import uk.ac.starlink.votable.TableElement;
import uk.ac.starlink.votable.VODocument;
import uk.ac.starlink.votable.VOElement;

/**
 * This class implements the IFileIO interface, providing methods to 
 * serialize/deserialize SpectralDataset instances in VOTable format.
 *  
 * @author mdittmar
 */
public class VOTableIO implements IFileIO {

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
    /* Constants */   
    private final static String CONST_VOTABLE_VERSION  = "1.3";
    private final static String CONST_VOTABLE_SCHEMA  = "http://www.ivoa.net/xml/VOTable/v1.3";
    
    // Model Specification for dataset being read or written.
    private Model model = null;
    private LinkedHashMap< String, Column > tabledata = null;
    
    /** Read Spectral Dataset from specified URL
     *  @param file 
     *     {@link java.net.URL}
     * 
     *  @return doc
     *    {@link cfa.vo.speclib.SpectralDataset }
     */
    //TODO:  Implement
    public SpectralDataset read(URL file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** Read Spectral Dataset from provided input stream
     *  @param is 
     *     {@link java.io.InputStream}
     * 
     *  @return doc
     *    {@link cfa.vo.speclib.SpectralDataset }
     */
    //TODO:  Implement
    public SpectralDataset read(InputStream is) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** Write Spectral Dataset to specified URL
     *  @param file 
     *     {@link java.net.URL}
     *  @param ds
     *    {@link cfa.vo.speclib.SpectralDataset }
     */
    public void write(URL file, SpectralDataset ds) throws IOException 
    {
        OutputStream os;
        
        if ( file.getProtocol().equalsIgnoreCase("file"))
        {
            // Special handling for file protocol.
            os = new FileOutputStream( file.getFile() );
        }
        else
        {
          // Get output stream to URL
          URLConnection con = file.openConnection();
          con.setDoOutput(true);
          os = con.getOutputStream();
        }

        // write doc to output stream.
        write( os, ds );
        
        // close the stream.
        os.close();
    }

    /** Write Spectral Dataset to specified stream
     *  @param os 
     *     {@link java.io.OutputStream}
     *  @param ds
     *    {@link cfa.vo.speclib.SpectralDataset }
     */
    public void write(OutputStream os, SpectralDataset ds) throws IOException
    {   
        // Convert SpectralDataset to W3C DOM Document using STIL package.
        Document vot = convertToVOT( ds );
        DOMSource source = new DOMSource( vot );

        // Setup output stream
        BufferedWriter buf = new BufferedWriter( new OutputStreamWriter( os ));
        StreamResult strm = new StreamResult(buf);

        // Create DOM Transformer, flush Document to stream.
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");
            transformer.setOutputProperty( OutputKeys.INDENT, "yes");
            transformer.transform(source, strm);
            
        //TODO - change exception action?
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(VOTableIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(VOTableIO.class.getName()).log(Level.SEVERE, null, ex);
        }

        buf.flush();
    }
    
    /**
     * Convert SpectralDocument to STIL VODocument object.
     *
     * STIL defines convenient Element classes for the various VOTable
     * Elements (Group, Param, Table, etc..) so we use these to build the
     * DOM and return its Document interface.
     * 
     * @param ds
     *    {@link cfa.vo.speclib.SpectralDataset }
     * @return 
     *    {@link org.w3c.dom.Document }
     */
      private Document convertToVOT( SpectralDataset ds ) throws IOException
      {
        VODocument vodoc = new VODocument(); // VODocument implements w3c Document interface..
        VOElement  root;
        VOElement  res;

        // Extract underlying SpectralDocument from input dataset
        ModelDocument spdoc = null;
        if ( Proxy.isProxyClass( ds.getClass() ))
        {
           ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( ds );
           spdoc = h.getDoc();
        }
        else
        {
           // TODO - Some sort of Error
           System.out.println("Input object is not a Proxy! ");
        }
        
        // Get model specification for this dataset.. to fill in any 
        // important missing information. (Utypes, UCDs, etc.)
        ModelFactory mf = new ModelFactory();
        String tmpstr = ds.getDataModel().getName().getValue();
        try {
            this.model = mf.newInstance( tmpstr );
        } catch (IOException ex) {
            // TODO - what error handling do we want here?
            Logger.getLogger(VOTableIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* Add VOTable declaration content */
        root = (VOElement) vodoc.createElement(DOM_TAG_DOCUMENT);
        root.setAttribute(ATT_TAG_XMLNS, CONST_VOTABLE_SCHEMA);
        root.setAttribute(ATT_TAG_VERSION, CONST_VOTABLE_VERSION );
        vodoc.appendChild(root);

        /* add RESOURCE element to doc.         */
        res = (VOElement) vodoc.createElement(DOM_TAG_RESOURCE);
        root.appendChild(res);
        
        /* add TABLE element to doc.         */
        addTableElement( res, spdoc );
        
        return vodoc;
    }
    
    /**
     * Creates FIELD and FIELDref element based on provided Quantity.
     * The FIELDref is added to the parent element.
     * The FIELD element is returned to the caller for handling.
     * (in general, the FIELDs are written in a separate section of the table)
     * 
     * The FIELD id and FIELDRef reference attributes will be linked, if 
     * the provided Quantity does not already have an ID specified, one will
     * be generated.
     * 
     * @param parent
     *    Parent element to receive the FIELDref.
     * @param q
     *    Quantity which will define the FIELD.
     * @return 
     *    FieldElement based on Quantity.
     */
    private FieldElement addFieldRefElement( VOElement parent, Quantity q )
    {
        // get index of this element in the model.
        Integer ndx = model.getRecordIndexByPath( q.getModelpath() );
        if ( ndx == -1 )
          System.out.println("MCD TEMP: Unmodeled Field - "+q.getModelpath());
     
        // Add FIELD and FIELDref elements if not already done.

        // get owner document for creating new elements.
        Document document = parent.getOwnerDocument();

        // Create FIELD and FIELDref Elements.
        FieldElement field = (FieldElement) document.createElement(DOM_TAG_FIELD);
        FieldRefElement ref = (FieldRefElement)document.createElement(DOM_TAG_FIELDREF);

        // Set Element attributes.
        // Field:    ID, UCD, UTYPE, NAME, DESCRIPTION, UNIT, DATATYPE, ARRAYSIZE, 
        // FieldRef: ID, UCD, UTYPE, NAME, DESCRIPTION 

        //  ID
        if ( q.isSetID() )
        {
          field.setAttribute( ATT_TAG_ID, q.getID() );
          ref.setAttribute( ATT_TAG_REF, q.getID() );
        }
        else
        { // generate random id string.
          String uuid = UUID.randomUUID().toString();
          field.setAttribute( ATT_TAG_ID, uuid );
          ref.setAttribute( ATT_TAG_REF, uuid);
        }
        
        //  Description
        if ( q.isSetDescription() )
          ref.setAttribute( ATT_TAG_DESC, q.getDescription() );
        
        //  Name
        if ( q.isSetName() )
          ref.setAttribute( ATT_TAG_NAME, q.getName() );

        //  UType - fill from model spec if not provided.
        if ( q.isSetUtype() )
          ref.setAttribute( ATT_TAG_UTYPE, q.getUtype() );
        else if ( ndx >= 0 )
          ref.setAttribute( ATT_TAG_UTYPE, this.model.getUtype(ndx).getTag() );
        
        //  UCD - fill from model spec if not provided
        if ( q.isSetUCD() )
          ref.setAttribute( ATT_TAG_UCD, q.getUCD() );
        else if ( ndx >= 0 && (! this.model.getUCD(ndx).isEmpty()) )
          ref.setAttribute( ATT_TAG_UCD, this.model.getUCD(ndx) );

        //  Unit
        if ( q.isSetUnit() )
          field.setAttribute( ATT_TAG_UNIT, q.getUnit() );

        if ( q.isSetValue())
        {
          //  DataType
          String dtype = q.getValue().getClass().getSimpleName();
          if ( dtype.equalsIgnoreCase( "double" ))
          {
            field.setAttribute( ATT_TAG_DTYPE, "double" );
          }
          else if ( dtype.equalsIgnoreCase( "string" ))
          {
            field.setAttribute( ATT_TAG_DTYPE, "char" );

            //  ArraySize
            field.setAttribute( ATT_TAG_ARRSIZE, "*" );
          }
          else if ( dtype.equalsIgnoreCase( "integer" ))
          {
            field.setAttribute( ATT_TAG_DTYPE, "int" );
          }
          else if ( dtype.equalsIgnoreCase( "boolean" ))
          {
            field.setAttribute( ATT_TAG_DTYPE, "boolean" );
          }
          else
          {
            throw new UnsupportedOperationException("Datatype not yet supported. ("+dtype+")");
          }
        }
        
        // Add new FieldRef to parent.
        parent.appendChild(ref);
        
        // Return FIELD element
        return( field );
    }

    /**
     * Create a Group Element and add it to the parent element.
     * 
     * @param parent
     *    Parent element to receive the Group.
     * @param node
     *    Document node containing a collection of related objects
     * 
     * @throws IOException 
     */
    private void addGroupElement( VOElement parent, ModelDocument node ) throws IOException
    {
        // get owner document for creating new elements.
        Document document = parent.getOwnerDocument();
        
        // Create Group Element for node.
        GroupElement group = (GroupElement) document.createElement(DOM_TAG_GROUP);
 
        // In VOTable this element may have 
        //  + ID
        //  + NAME
        //  + DESCRIPTION
        // which we do not currently support.
        // These would be attributes of the SpectralDocument class.
        
        // Add this group to the parent node.
        parent.appendChild( group );
        
        // Add group content.
        this.addNodeContent( group, node, -1 );
    }
 
    /**
     * Convert Quantity to ParamElement and add to provided parent element
     * 
     * @param parent
     *    Parent element to receive the Param.
     * @param q
     *    Quantity from which to populate the Param.
     * @return 
     */
    private void addParamElement( VOElement parent, Quantity q )
    {
        // get owner document for creating new elements.
        Document document = parent.getOwnerDocument();
        
        // create new ParamElement, set attributes.
        ParamElement param = (ParamElement) document.createElement(DOM_TAG_PARAM);        

        // get index of this element in the model.
        Integer ndx = model.getRecordIndexByPath( q.getModelpath() );
        if ( ndx == -1 )
            System.out.println("MCD TEMP: Unmodeled Element - "+q.getModelpath());

        // Temporary string for use below
        String tmpstr;
        
        //  ID
        //  TODO: Random ID generator? only set for ParamRef usage?
        if ( q.isSetID() )
          param.setAttribute( ATT_TAG_ID, q.getID() );

        //  Description - serialized as subelement of param.
        tmpstr = null;
        if ( q.isSetDescription() )
            tmpstr = q.getDescription();
        else if ( ndx >= 0 && (! this.model.getDescription(ndx).isEmpty()) )
            tmpstr = this.model.getDescription(ndx);
        if ( tmpstr != null )
        {
          //param.setAttribute( ATT_TAG_DESC, q.getDescription() );
          VOElement desc = (VOElement)document.createElement(ATT_TAG_DESC.toUpperCase());
          desc.setTextContent( tmpstr );
          param.appendChild( desc );
        }
        
        //  Name
        if ( q.isSetName() )
          param.setAttribute( ATT_TAG_NAME, q.getName() );

        //  UType - fill from model spec if not provided.
        if ( q.isSetUtype() )
          param.setAttribute( ATT_TAG_UTYPE, q.getUtype() );
        else if ( ndx >= 0 )
          param.setAttribute( ATT_TAG_UTYPE, this.model.getUtype(ndx).getTag() );
        
        //  UCD - fill from model spec if not provided
        if ( q.isSetUCD() )
          param.setAttribute( ATT_TAG_UCD, q.getUCD() );
        else if ( ndx >= 0 && (! this.model.getUCD(ndx).isEmpty()) )
          param.setAttribute( ATT_TAG_UCD, this.model.getUCD(ndx) );

        //  Unit
        if ( q.isSetUnit() )
          param.setAttribute( ATT_TAG_UNIT, q.getUnit() );

        if ( q.isSetValue())
        {
          tmpstr = null;
          
          //  DataType
          String dtype = q.getValue().getClass().getSimpleName();
          if ( dtype.equalsIgnoreCase( "double" ))
          {
            param.setAttribute( ATT_TAG_DTYPE, "double" );
            tmpstr = q.getValue().toString();
          }
          else if ( dtype.equalsIgnoreCase( "string" ) || 
                    dtype.equalsIgnoreCase( "url" ) ||
                    dtype.equalsIgnoreCase( "uri" )
                  )
          {
            param.setAttribute( ATT_TAG_DTYPE, "char" );
            param.setAttribute( ATT_TAG_ARRSIZE, "*" );
            tmpstr = q.getValue().toString();
          }
          else if ( dtype.equalsIgnoreCase( "integer" ))
          {
            param.setAttribute( ATT_TAG_DTYPE, "int" );
            tmpstr = q.getValue().toString();
          }
          else if ( dtype.equalsIgnoreCase( "double[]" ))
          {
            param.setAttribute( ATT_TAG_DTYPE, "double" );
            Double[] darr = (Double[])q.getValue();
            param.setAttribute( ATT_TAG_ARRSIZE, Integer.toString( darr.length ) );
            tmpstr = java.util.Arrays.toString( darr ).replaceAll("\\[(.*)\\]","$1").replaceAll(",","");
          }
          else
          {
              System.out.println("MCD TEMP: Bad element "+q.getModelpath());
              throw new UnsupportedOperationException("Datatype not yet supported. ("+dtype+")");
          }
          
          //  Value (as String)
          param.setAttribute( ATT_TAG_VALUE, tmpstr );
        }
        else
        {
            // TODO - proper representation of NULL value (VOTable spec sections 5.5 and 6
            throw new UnsupportedOperationException("Null value representation not yet supported.");
        }
        
        // Add new parameter to parent.
        parent.appendChild(param);
    }
    
    /**
     * 
     * @param parent
     *    Parent element to receive the Group.
     * @param node
     *    Document node containing a top level Objects to be serialized as a table.
     * 
     * @throws IOException 
     */
    private void addTableElement( VOElement parent, ModelDocument node ) throws IOException
    {
        // get owner document for creating new elements.
        Document document = parent.getOwnerDocument();

        // Create Table Element
        TableElement table = (TableElement) document.createElement(DOM_TAG_TABLE);
        if ( node.contains("SpectralDataset_DataProductType"))
        {
          Quantity type = (Quantity)node.get("SpectralDataset_DataProductType");
          table.setAttribute( ATT_TAG_NAME, (String)type.getValue());
        }
        
        // Add table to parent node (resource).
        parent.appendChild(table);
        
        // Add table content
        this.addNodeContent( table, node, -1 );
        
        // Add table data to document.
        this.addTableDataElement( table );
    }

    /**
     * Add Content of Document node to parent element.
     * Converts each node object to DOM element, and adds to parent.
     * 
     * @param parent
     *   Parent element to receive node content.
     * @param node
     *   Node to transfer.
     * @param row
     *   Row number.. if adding FIELD records, -1 otherwise.
     * 
     * @throws IOException 
     */
    private void addNodeContent( VOElement parent, ModelDocument node, int row ) throws IOException
    {
        // Iterate through node objects, add each to parent document.
        //   Lists which are not FIELDs get iterated, so that addObjectContent
        //   can assume that any List it receives produces FIELDs.
        for (String mp : node.getKeys()) 
        {
            Object obj = node.get( mp );
            // TODO - remove hack.. 
            if ( obj.getClass().equals( MPArrayList.class ) && !mp.endsWith("Dataset_Data"))
            {
                for (Object item : (MPArrayList)obj )
                  this.addObjectContent( parent, item, row );
            }
            else
              this.addObjectContent( parent, obj, row );
        }
    }

    /**
     * Generate DOM element for object and add to parent document.
     * 
     * @param parent
     *   Parent element to receive node content.
     * @param obj 
     *   Object to be converted.
     *   - Quantity => ParamElement or ColumnValue
     *   - Proxy    => GroupElement
     *   - List     => FieldElement
     * @param row 
     *   Row number.. if adding FIELD records, -1 otherwise.
     * 
     */
    private void addObjectContent( VOElement parent, Object obj, int row ) throws IOException
    {
        if ( obj.getClass().equals( Quantity.class ))
        {
          // Quantity Object => ParamElement or Column Value
          Quantity q = (Quantity)obj;
          if ( q.getModelpath() == null )
            System.out.println("MCD TEMP: Quantity with NULL modelpath."+q.toString());
          String mp = q.getModelpath();
          if ( row >= 0 )
              this.addColumnValue( q, row ); // Adding column data
          else
            this.addParamElement( parent, (Quantity)obj );  // Adding Param
        }
        else if ( obj.getClass().equals( MPArrayList.class ) )
        {
            // Lists are mapped to FIELDS.  The list may contain Quantities
            //  defining a single field, or a complex object defining many
            //  fields (ie: SPPoint).
            this.addColumns( parent, (MPArrayList)obj );
        }
        else if ( Proxy.isProxyClass( obj.getClass() ))
        {
          // Proxy Object => GroupElement
          // If row >= 0, we are flushing FIELD objects, and should not create
          // the Group on each iteration.
          ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( obj );
          ModelDocument newnode = h.getDoc();
          if ( row < 0 )
              this.addGroupElement( parent, newnode );
          else
              this.addNodeContent( parent, newnode, row );
              
        }
        else
        {
            System.out.println("MCD TEMP: Missed Object.");
            throw new UnsupportedOperationException("Unsupported Type encountered - "+obj.getClass().getSimpleName());
            // TODO Error
        }
    }
    
    /**
     * Convert List object to FIELD/FIELDref elements, add FIELDref to 
     * the parent element.  Creates Column object to store FIELD element
     * and associated data, which will populate the DATA section.
     * 
     * Input list may contain either:
     *   - Quantity, to define a single column
     *   - Object, to define multiple columns
     * 
     * @param parent
     *   Parent element to receive node content.
     * @param fieldobject
     *   List of objects from which to define FIELDS.
     * 
     * @throws IOException 
     */
     private void addColumns( VOElement parent, MPArrayList fieldobject ) throws IOException
    {
        // List length determines #rows in fields.
        int nrows = fieldobject.size();

        // Allocate tabledata hash
        if ( tabledata == null )
            tabledata = new LinkedHashMap<String, Column>();
        
        // Add Group and FieldRef tree from first Data instance to doc.
        // Create and add column definition to tabledata.
        Object obj = fieldobject.get(0);
        this.addColumns( parent, obj, nrows);
        
        // Add Column Content
        for (int ii=0; ii < nrows; ii++ )
        {
            this.addObjectContent(parent, fieldobject.get(ii), ii );
        }

    }
    
    /**
     * Convert object to FIELD/FIELDref elements, add FIELDref to 
     * the parent element.  Creates Column object to store FIELD element
     * and associated data, which will populate the DATA section.
     * 
     * Input object may contain either:
     *   - Quantity, to define a single column
     *   - Proxy, to define multiple columns
     * 
     * @param parent
     *   Parent element to receive node content.
     * @param obj
     *   Object from which to define FIELDS.
     * @param nrows
     *   Number of rows the FIELD contains.
     * 
     * @throws IOException 
     */
    private void addColumns( VOElement parent, Object obj, int nrows ) throws IOException
    {
        if ( obj.getClass().equals( Quantity.class ))
        {
            Quantity q = (Quantity)obj;
            String mp = q.getModelpath();

            // Add FieldRef to document, FIELD to tabledata
            FieldElement field = addFieldRefElement(parent, q );
            Column col = new Column();
            col.info = field;
            col.data = new String[nrows];
            tabledata.put( mp, col );
        }
        else if ( Proxy.isProxyClass( obj.getClass() ))
        {
            ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( obj );
            ModelDocument newnode = h.getDoc();

            // Add Group to parent (do not use addGroup()
            Document document = parent.getOwnerDocument();
            GroupElement group = (GroupElement) document.createElement(DOM_TAG_GROUP);
            parent.appendChild( group );

            // Add Column for object (recursive)
            for (String mp : newnode.getKeys())
                addColumns( group, newnode.get(mp), nrows );
        }
        else if ( obj.getClass().equals( MPArrayList.class ) )
        {
            // List of objects, each of which is a Column.
            for (Object item : (MPArrayList)obj )
                this.addColumns( parent, item, nrows );
        }
        else
        {
           throw new UnsupportedOperationException("Can not add columns from "+obj.getClass().getSimpleName());
        }
    }
    /**
     * Assign value to row of column 
     *   - Obtains Column from tabledata by modelpath
     *   - assigns value to data attribute for the indicated row.
     * 
     * @param q
     * @param row
     * @throws IOException 
     */
    private void addColumnValue( Quantity q, int row ) throws IOException
    {
        String mp = q.getModelpath();
        Column col = this.tabledata.get(mp);
        if ( col != null )
        {
            col.data[ row ] = q.getValue().toString();
        }
        else
            throw new IOException("Attempt to assign values to undefined column: "+mp);

    }    
    
    /**
     * Add FIELD elements to parent element.
     * Add DATA and TABLEDATA elements to the parent element.
     * Populate TABLEDATA with inline serialization of table data.
     * 
     * @param parent
     * @throws IOException 
     */
    private void addTableDataElement( VOElement parent ) throws IOException
    {
        // get owner document for creating new elements.
        Document document = parent.getOwnerDocument();
        int nrows = -1;
        
        // Add FIELD element for each Column
        for ( Column col : this.tabledata.values() )
        {
            if ( nrows == -1 )
                nrows = col.data.length;
            else if ( col.data.length != nrows )
                throw new IOException("Columns do not have uniform length.");

            parent.appendChild( col.info );
        }

        // Create TABLEDATA Element
        VOElement data = (VOElement) document.createElement(DOM_TAG_DATA);
        parent.appendChild( data );
        VOElement table = (VOElement) document.createElement(DOM_TAG_TABLEDATA);
        parent.appendChild( table );
        
        // Build and add Element for each row of data.
        VOElement tr;
        VOElement td;
        for (int ii = 0; ii < nrows; ii++ )
        {
          tr = (VOElement) document.createElement(DOM_TAG_ROW);
          table.appendChild( tr );

          // Build String of the row entry.
          for ( Column col : this.tabledata.values() )
          {
              td = (VOElement)document.createElement(DOM_TAG_COL);
              td.setTextContent(col.data[ii]);
              tr.appendChild( td );
          }
        }
    }
    
}
