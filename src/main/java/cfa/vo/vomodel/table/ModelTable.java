/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel.table;
import cfa.vo.vomodel.Model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *  This Class provides access to VO Data Model definitions stored as a simple table.  
 * 
 * @author mdittmar
 */
public class ModelTable implements Model {

    protected String name;   // model name
    protected String prefix; // model prefix 
    protected URL    refURL; // reference URL

    protected HashMap<String, Integer> map;  // map Utype to record number.
                                             //   key = lowercase utype with no prefix
    protected ArrayList<Entry> data;         // set of model records.
    
    protected Boolean includePrefix = true;  // control flag for prefix handling.
            
    /**
     * Constructor to generate empty ModelTable.
     */
    public ModelTable()
    {
        this.name = null;
        this.prefix = null;
        this.refURL = null;
        this.map = null;
        this.data = null;
    }
    
    /**
     * Read indicated VO Data Model Table file and load the content.
     * 
     * @param filepath 
     * @throws FileNotFoundException if URL cannot be resolved
     * @throws IOException on error reading file
     */
    public void read( URL filepath ) throws IOException
    {
        InputStream is;
        BufferedReader br;
        String line;    // line from table
        String[] parts; // line data
        Entry record;   // local object to hold record
        String key;     // key for map.
        
        int count = 0;

        if ( filepath == null )
            throw new FileNotFoundException("Null URL input. ");
        else
            is = filepath.openStream();
 
        if ( is == null )
            throw new FileNotFoundException("File not found: "+filepath);

        // create storage for index map and model records
        this.map = new HashMap<String,Integer>();
        this.data = new ArrayList<Entry>();
        
        // read table rows
        br = new BufferedReader( new InputStreamReader(is) );
        while ( (line = br.readLine()) != null )
        {
           // skip comment lines
           if ( line.startsWith("#") )
               continue;

           // process rows
           if ( line.startsWith("Name=") )
           {
               this.name = line.substring(line.indexOf("=")+1);
           }
           else if ( line.startsWith("Prefix=") )
           {
               this.prefix = line.substring(line.indexOf("=")+1);
           }
           else if ( line.startsWith("URL=") )
           {
               String tmp = line.substring(line.indexOf("=")+1).trim();
               this.refURL = new URL( tmp );
           }
           else
           {
               record = new Entry();
               parts = line.split("&");
               if ( parts.length != 7 )
                   throw new IOException("Invalid record found.");

               record.type   = parts[0].trim();
               record.utype  = parts[1].trim();
               record.ucd    = parts[2].trim();
               record.defval = parts[3].trim();
               record.unit   = parts[4].trim();
               record.descr  = parts[5].trim();
               record.req    = parts[6].trim();
               
               key = this.utype2key( record.utype );
               this.map.put(key, count);
               this.data.add(record);
               count++;
           }
        }
        br.close();
    }
    /**
     * Write VO Data Model Table content to file.
     * 
     * @param filename 
     */
    public void write( String filename )
    {
        throw new UnsupportedOperationException("Not supported yet.");        
    }
    
    public void setIncludeModelPrefix( boolean flag ) 
    {
        this.includePrefix = flag;
    }

    public String getModelName() {
        if ( this.name == null )
            throw new IllegalStateException("No Model loaded.");
        
        return( this.name );
    }

    public String getPrefix() {
        if ( this.prefix == null )
            throw new IllegalStateException("No Model loaded.");
        
        return( this.prefix );
    }

    public URL getReferenceURL() {
        if ( this.refURL == null )
            throw new IllegalStateException("No Model loaded.");
        
        return( this.refURL );
    }

    public Integer getUtypeNum(String utype) 
    {
        String key;
        Integer result;

        if ( this.map == null )
            throw new IllegalStateException("No Model loaded.");

        // convert utype string to key format
        key = this.utype2key( utype );

        if ( this.map.containsKey(key))
            result = this.map.get(key);
        else
            throw new IllegalArgumentException("Utype invalid or not recognized.");

        return(result);
    }

    public String getUtype(Integer utypenum) {
        String result;
        
        if (this.includePrefix)
            result = this.prefix + ":" + this.getField( "UTYPE", utypenum );
        else
            result = this.getField( "UTYPE", utypenum );
 
        return( result );
    }

    public String getUnit(Integer utypenum) {
        return( this.getField( "UNIT", utypenum ) );
    }

    public String getUnit(String utype) {
        return( this.getField( "UNIT", utype ) );
    }

    public String getUCD(Integer utypenum) {
        return( this.getField( "UCD", utypenum ));
    }

    public String getUCD(String utype) {
        return( this.getField( "UCD", utype ));
    }

    public String getType(Integer utypenum) {
        return( this.getField( "TYPE", utypenum ));
    }

    public String getType(String utype) {
        return( this.getField( "TYPE", utype ));
    }

    public String getDefault(Integer utypenum) {
        return( this.getField( "DEFAULT", utypenum ));
    }

    public String getDefault(String utype) {
        return( this.getField( "DEFAULT", utype ));
    }

    public Boolean isMandatory(Integer utypenum) {
        return(this.getField("REQ", utypenum ).equalsIgnoreCase("MAN") );
    }

    public Boolean isMandatory(String utype) {
        return(this.getField("REQ", utype ).equalsIgnoreCase("MAN") );
    }

    public Boolean isValidUtype(String utype) {
        Boolean result;
        Integer n;
        try{
            n = this.getUtypeNum(utype);
            result = true;
        }
        catch (IllegalArgumentException ex){
            result = false;
        }
        return (result);
    }

    public List<String> getUtypes() {
        String tmpstr;
        List<String> result;
        Iterator iter;
        Entry item;

        if ( this.data == null )
            throw new IllegalStateException("No Model loaded.");

        result = new ArrayList<String>(data.size());
        iter = this.data.iterator();
        while (iter.hasNext())
        {
            item = (Entry)iter.next();
            if ( this.includePrefix )
                tmpstr = this.prefix + ":" + item.utype ;
            else
                tmpstr = item.utype;

            result.add( tmpstr );
        }
        
        return( result );
    }

    private String utype2key( String utype )
    {
        String result;
        String tmpstr = null;

        if ( utype == null )
            throw new IllegalArgumentException("Invalid (null) Utype entered.");

        if ( this.prefix != null )
            tmpstr = this.prefix.toLowerCase()+":";

        // Strip the model prefix.
        if ( tmpstr != null )
            result = utype.toLowerCase().replaceFirst( tmpstr ,"" );
        else
            result = utype.toLowerCase();

        return(result);
    }
    
    private String getField( String field, String utype )
    {
        String result;
        Integer index;
        if ( this.map == null )
            throw new IllegalStateException("No Model loaded.");
                    
        index = getUtypeNum( utype );
        result = this.getField( field, index );
        return(result);
    }
    private String getField( String field, Integer record )
    {
        String result;
        if ( this.data == null )
            throw new IllegalStateException("No Model loaded.");

        if ( field.equals("TYPE"))
            result = data.get(record).type;
        else if ( field.equals("UTYPE"))
            result = data.get(record).utype;
        else if ( field.equals("UNIT"))
            result = data.get(record).unit;
        else if ( field.equals("UCD"))
            result = data.get(record).ucd;
        else if ( field.equals("DEFAULT"))
            result = data.get(record).defval;
        else if ( field.equals("REQ"))
            result = data.get(record).req;
        else if ( field.equals("DESC"))
            result = data.get(record).utype;
        else
            result = null;
            
        return(result);
    }

}
