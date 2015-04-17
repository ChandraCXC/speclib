/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.SpectralDataset;
import cfa.vo.speclib.doc.SpectralDocument;
import cfa.vo.speclib.doc.SpectralProxy;
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
import java.util.Iterator;
import java.util.Set;
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
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.GroupElement;
import uk.ac.starlink.votable.ParamElement;
import uk.ac.starlink.votable.TableElement;
import uk.ac.starlink.votable.VODocument;
import uk.ac.starlink.votable.VOElement;
//import uk.ac.starlink.votable.VOStarTable;
import uk.ac.starlink.votable.VOTableVersion;
import uk.ac.starlink.votable.VOTableWriter;

/**
 * This class implements the IFileIO interface, providing methods to 
 * serialize/deserialize SpectralDataset instances in VOTable format.
 *  
 * @author mdittmar
 */
public class VOTableIO implements IFileIO {

    /* VOElement DOM node TagNames */
    private final static String DOM_TAG_DOCUMENT = "VOTABLE";
    private final static String DOM_TAG_RESOURCE = "RESOURCE";
    private final static String DOM_TAG_TABLE    = "TABLE";
    private final static String DOM_TAG_GROUP    = "GROUP";
    private final static String DOM_TAG_PARAM    = "PARAM";
    /* VOElement attribute TagNames */
    private final static String ATT_TAG_ARRSIZE  = "arraysize";
    private final static String ATT_TAG_DESC     = "description";
    private final static String ATT_TAG_DTYPE    = "datatype";
    private final static String ATT_TAG_ID       = "id";
    private final static String ATT_TAG_NAME     = "name";
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
        //Open Writers
        BufferedWriter buf = new BufferedWriter( new OutputStreamWriter( os ));
        VOTableWriter writer = new VOTableWriter( DataFormat.TABLEDATA, true, VOTableVersion.V13);
        
        // Convert SpectralDataset to W3C DOM Document using STIL package.
        Document vot = convertToVOT( ds );

        // Create DOM Transformer, flush Document to stream.
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");
            transformer.setOutputProperty( OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource( vot );
            StreamResult strm = new StreamResult(buf);
            transformer.transform(source, strm);
            
        //TODO - change exception action?
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(VOTableIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(VOTableIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //VOStarTable tab;
        //NodeList nodes = vot.getElementsByTagName(DOM_TAG_TABLE);
        //for ( int ii = 0; ii < nodes.getLength(); ii++ )
        //{
        //  tab = new VOStarTable( (TableElement)nodes.item(ii) );            
        //  writer.writeInlineStarTable(tab, buf);
        //}
        // Flush doc to Writer
        //buf.write( ds.toString() );
        buf.flush();
    }

    /**
     * Convert SpectralDocument to STIL VODocument object.
     *
     * STIL defines convenient Element classes for the various VOTable
     * Elements (Group, Param, Table, etc..) so we use these to build the
     * DOM and return its Document interface.
     * 
     * @param spdoc
     *    {@link cfa.vo.speclib.SpectralDocument }
     * @return 
     *    {@link org.w3c.dom.Document }
     */
      private Document convertToVOT( SpectralDataset ds )
      {
        VODocument vodoc = new VODocument(); // VODocument implements w3c Document interface..
        VOElement  root;
        VOElement  res;

        // Extract underlying SpectralDocument from input dataset
        SpectralDocument spdoc = null; //= ds.toDocument();
        if ( Proxy.isProxyClass( ds.getClass() ))
        {
           SpectralProxy h = (SpectralProxy)Proxy.getInvocationHandler( ds );
           spdoc = h.getDoc();
        }
        else
        {
           // Some sort of Error
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
     * Convert Quantity to ParamElement and add to provided parent element
     * 
     * @param parent
     * @param q
     * @return 
     */
    private void addParamElement( VOElement parent, Quantity q )
    {
        // get owner document for creating new elements.
        Document document = parent.getOwnerDocument();
        
        // create new ParamElement, set attributes.
        ParamElement param = (ParamElement) document.createElement(DOM_TAG_PARAM);        

        // get index of this element in the model.
        // TODO: implement

        //  ID
        //  TODO: Random ID generator? only set for ParamRef usage?
        if ( q.isSetID() )
          param.setAttribute( ATT_TAG_ID, q.getID() );

        //  Description
        if ( q.isSetDescription() )
          param.setAttribute( ATT_TAG_DESC, q.getDescription() );
        
        //  Name
        if ( q.isSetName() )
          param.setAttribute( ATT_TAG_NAME, q.getName() );

        //  UType - fill from model spec if not provided.
        if ( q.isSetUtype() )
          param.setAttribute( ATT_TAG_UTYPE, q.getUtype() );
        //else
        //  param.setAttribute( ATT_TAG_UTYPE, this.model.getUtype(ndx) );          
 
        //  UCD - fill from model spec if not provided
        if ( q.isSetUCD() )
          param.setAttribute( ATT_TAG_UCD, q.getUCD() );
        //else
        //  param.setAttribute( ATT_TAG_UTYPE, this.model.getUCD(ndx) );          
 
        //  Unit
        if ( q.isSetUnit() )
          param.setAttribute( ATT_TAG_UNIT, q.getUnit() );

        if ( q.isSetValue())
        {
          //  DataType
          String dtype = q.getValue().getClass().getSimpleName();
          if ( dtype.equalsIgnoreCase( "double" ))
          {
            param.setAttribute( ATT_TAG_DTYPE, "double" );
          }
          else if ( dtype.equalsIgnoreCase( "string" ))
          {
            param.setAttribute( ATT_TAG_DTYPE, "char" );

            //  ArraySize
            param.setAttribute( ATT_TAG_ARRSIZE, "*" );
          }
          else
          {
              throw new UnsupportedOperationException("Datatype not yet supported. ("+dtype+")");
          }
          
          //  Value (as String)
          param.setAttribute( ATT_TAG_VALUE, q.getValue().toString() );
        }
        else
        {
            // TODO - proper representation of NULL value (VOTable spec sections 5.5 and 6
            throw new UnsupportedOperationException("Null value representation not yet supported.");
        }
            
        
        // Add new parameter to parent.
        parent.appendChild(param);

    }

    private void addGroupElement( VOElement parent, SpectralDocument node )
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
        this.addNodeContent( group, node );       
    }
 
    private void addTableElement( VOElement parent, SpectralDocument node )
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
        this.addNodeContent( table, node );
    }
    
    private void addNodeContent( VOElement parent, SpectralDocument node )
    {
        // Get node key list.
        Set keys = node.getKeys();
        
        // Add all attributes/params for this node to Document
        Iterator it = keys.iterator();
        while (it.hasNext())
        {
            String mp = (String)it.next();
            Object obj = node.get( mp );
            if ( obj.getClass().equals( Quantity.class ))
            {
              this.addParamElement( parent, (Quantity)obj );
            }
        }
        
        // Add all sub-groups for this node to Document
        it = keys.iterator();
        while (it.hasNext())
        {
            String mp = (String)it.next();
            Object obj = node.get( mp );
            if ( Proxy.isProxyClass( obj.getClass() ))
            {
               SpectralProxy h = (SpectralProxy)Proxy.getInvocationHandler( obj );
               SpectralDocument newnode = h.getDoc();
               this.addGroupElement( parent, newnode );
            }
        }
    }

//Example from STIL code.
//<?xml version='1.0'?>
//<VOTABLE version="1.3"
// xmlns="http://www.ivoa.net/xml/VOTable/v1.3">
//<!--
// !  VOTable written by STIL version 3.0-10 (uk.ac.starlink.votable.VOTableWriter)
// !  at 2015-04-13T21:09:18
// !-->
//<RESOURCE>
//<TABLE name="Spectrum">
//<PARAM arraysize="4" datatype="char" name="prefix" utype="spec:Dataset.DataModel.Prefix" value="spec"/>
//<PARAM arraysize="12" datatype="char" name="model" utype="spec:Dataset.DataModel.Name" value="Spectrum-2.0"/>
//<PARAM datatype="double" name="snr" utype="spec:Derived.snr" value="1.33"/>
//<DATA>
//<TABLEDATA>
//</TABLEDATA>
//</DATA>
//</TABLE>
//</RESOURCE>
//</VOTABLE>

}
