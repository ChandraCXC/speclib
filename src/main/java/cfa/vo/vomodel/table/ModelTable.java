/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel.table;

import cfa.vo.vomodel.Model;
import cfa.vo.vomodel.Utype;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  This Class provides access to VO Data Model definitions stored as a simple table.  
 * 
 * @author mdittmar
 */
public class ModelTable implements Model {

    protected String title;  // model title
    protected String name;   // model name
    protected String prefix; // model prefix 
    protected URL    refURL; // reference URL

    protected ArrayList<Utype> map;          // Utype list for model records.
    protected ArrayList<Entry> data;         // set of model records.
    
    /**
     * Constructor to generate empty ModelTable.
     */
    ModelTable(ModelTableBuilder builder) throws IOException {
        read(builder.getUrl(), builder);
    }
    
    /**
     * Read indicated VO Data Model Table file and load the content.
     * 
     * @param filepath 
     * @throws FileNotFoundException if URL cannot be resolved
     * @throws IOException on error reading file
     */
    protected final void read( URL filepath, ModelTableBuilder builder ) throws IOException
    {
        String builderTitle = builder.getTitle();
        String builderName = builder.getName();
        String builderPrefix = builder.getPrefix();
        URL builderRefURL = builder.getRefURL();

        if (builderTitle != null)
            this.title = builder.getTitle();

        if (builderName != null)
            this.name = builder.getName();

        if (builderPrefix != null)
            this.prefix = builder.getPrefix();

        if (builderRefURL != null)
            this.refURL = builder.getRefURL();

        InputStream is;
        BufferedReader br;
        String line;    // line from table
        String[] parts; // line data
        Entry record;   // local object to hold record
        Utype key;      // key for map.
        
        int count = 0;

        if ( filepath == null )
            throw new FileNotFoundException("Null URL input. ");
        else
            is = filepath.openStream();
 
        if ( is == null )
            throw new FileNotFoundException("File not found: "+filepath);

        // create storage for index map and model records
        this.map  = new ArrayList<Utype>();
        this.data = new ArrayList<Entry>();
        
        // read table rows
        br = new BufferedReader( new InputStreamReader(is) );
        while ( (line = br.readLine()) != null )
        {
           // skip comment lines
           if ( line.startsWith("#") )
               continue;

           // process rows
           if ( line.startsWith("Title=") )
           {
               if (this.title != null) continue;
               this.title = line.substring(line.indexOf("=")+1);
           }
           else if ( line.startsWith("Name=") )
           {
               if (this.name != null) continue;
               this.name = line.substring(line.indexOf("=")+1);
           }
           else if ( line.startsWith("Prefix=") )
           {
               if (this.prefix != null) continue;
               this.prefix = line.substring(line.indexOf("=")+1);
           }
           else if ( line.startsWith("URL=") )
           {
               if (this.refURL != null) continue;
               String tmp = line.substring(line.indexOf("=")+1).trim();
               this.refURL = new URL( tmp );
           }
           else
           {
               record = new Entry();
               parts = line.split("&");
               if ( parts.length != 8 )
                   throw new IOException("Invalid record found.");

               record.modelpath = parts[0].trim();
               record.type   = parts[1].trim();
               record.tag    = parts[2].trim();
               record.ucd    = parts[3].trim();
               record.defval = parts[4].trim();
               record.unit   = parts[5].trim();
               record.descr  = parts[6].trim();
               record.mult   = parts[7].trim();
               
               key = new Utype( record.modelpath, record.tag, this.prefix );
               //this.map.put(key, count);
               this.map.add(key);
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
    public void write( URL filename ) throws IOException
    {
        OutputStream os;
        Entry item;

        if ( filename == null )
            throw new FileNotFoundException("Null URL input. ");

        // Open output stream
        os = new FileOutputStream( filename.getFile() );
        BufferedWriter buf = new BufferedWriter( new OutputStreamWriter( os ));

        String fmt = "%1s %-125s & %-24s & %-70s & %-45s & %-25s & %-20s & %-40s & %-5s";
        
        // Write header lines
        buf.write("# -----------------------------------");
        buf.newLine();
        buf.write("# VO Data Model Summary");
        buf.newLine();
        buf.write("# -----------------------------------");
        buf.newLine();
        buf.write("Title=" + this.title);
        buf.newLine();
        buf.write("Name=" + this.name);
        buf.newLine();
        buf.write("Prefix=" + this.prefix);
        buf.newLine();
        buf.write("URL=" + this.refURL);
        buf.newLine();
        buf.write("#");
        buf.newLine();
        buf.write("#");
        buf.newLine();
        buf.write( String.format( fmt, "#", "Model Path", "Type", "Tag", 
                "UCD", "Default", "Unit", "Description", "REQ" ) );
        buf.newLine();
        buf.write("#");
        for ( int ii=0; ii<350;ii++)
            buf.write("-");
        buf.newLine();

        Iterator it = this.data.iterator();
        while (it.hasNext())
        {
            item = (Entry)it.next();
            buf.write( String.format( fmt, " ", item.modelpath, item.type, 
                        item.tag, item.ucd, item.defval, item.unit, 
                        item.descr, item.mult ) );
            buf.newLine();
        }
        buf.flush();
        buf.close();
    }
    
    public String getTitle() {
        if ( this.title == null )
            throw new IllegalStateException("No Model loaded.");
        
        return( this.title );
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

    public Integer getRecordIndex(Utype utype) 
    {
        String key;
        Integer result;

        if ( this.map == null )
            throw new IllegalStateException("No Model loaded.");

        if ( this.map.contains( utype ) )
            result = this.map.indexOf(utype);
        else
            throw new IllegalArgumentException("Utype invalid or not recognized. "+utype.toString());

        return(result);
    }

    public Integer getRecordIndexByPath(String modelpath) 
    {
        String key;
        Integer result = -1; // init to 'not found'

        if ( this.map == null )
            throw new IllegalStateException("No Model loaded.");

        // null or empty string never matches.
        if ( modelpath == null || modelpath.trim().isEmpty() )
            return(result);
        
        for (Utype item : this.map )
        {
            if ( item.matchModelPath(modelpath))
            {
                result = this.map.indexOf(item);
                break;
            }
        }
        return(result);
    }

    public Integer getRecordIndexByTag(String label) 
    {
        String key;
        Integer result = -1; // init to 'not found'

        if ( this.map == null )
            throw new IllegalStateException("No Model loaded.");

        // null or empty string never matches.
        if ( label == null || label.trim().isEmpty() )
            return(result);

        for (Utype item : this.map )
        {
            if ( item.matchTag(label))
            {
                result = this.map.indexOf(item);
                break;
            }
        }

        return(result);
    }
    
    public Utype getUtype(Integer utypenum) {
        Utype result;

        if ( this.map == null )
            throw new IllegalStateException("No Model loaded.");

        result = this.map.get(utypenum);
        
        return( result );
    }

    public String getUnit(Integer utypenum) {
        return( this.getField( "UNIT", utypenum ) );
    }

    public String getUnit(Utype utype) {
        return( this.getField( "UNIT", utype ) );
    }

    public String getUCD(Integer utypenum) {
        return( this.getField( "UCD", utypenum ));
    }

    public String getUCD(Utype utype) {
        return( this.getField( "UCD", utype ));
    }

    public String getType(Integer utypenum) {
        return( this.getField( "TYPE", utypenum ));
    }

    public String getType(Utype utype) {
        return( this.getField( "TYPE", utype ));
    }

    public String getDefault(Integer utypenum) {
        return( this.getField( "DEFAULT", utypenum ));
    }

    public String getDefault(Utype utype) {
        return( this.getField( "DEFAULT", utype ));
    }

    public String getDescription(Integer utypenum) {
        return( this.getField( "DESC", utypenum ));
    }

    public String getDescription(Utype utype) {
        return( this.getField( "DESC", utype ));
    }

    public Boolean isMandatory(Integer utypenum) {
        return(this.getField("REQ", utypenum ).equalsIgnoreCase("MAN") );
    }

    public Boolean isMandatory(Utype utype) {
        return(this.getField("REQ", utype ).equalsIgnoreCase("MAN") );
    }

    public Boolean isValidUtype(Utype utype) {
        Boolean result;
        Integer n;
        try{
            n = this.getRecordIndex(utype);
            result = true;
        }
        catch (IllegalArgumentException ex){
            result = false;
        }
        return (result);
    }

    public List<Utype> getUtypes() {
        String tmpstr;
        List<Utype> result;
        Iterator iter;

        if ( this.data == null )
            throw new IllegalStateException("No Model loaded.");

        result = (List<Utype>)this.map.clone();

        return( result );
    }

    private String getField( String field, Utype utype )
    {
        String result;
        Integer index;
        if ( this.map == null )
            throw new IllegalStateException("No Model loaded.");
                    
        index = getRecordIndex( utype );
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
        else if ( field.equals("TAG"))
            result = data.get(record).tag;
        else if ( field.equals("UNIT"))
            result = data.get(record).unit;
        else if ( field.equals("UCD"))
            result = data.get(record).ucd;
        else if ( field.equals("DEFAULT"))
            result = data.get(record).defval;
        else if ( field.equals("REQ"))
            result = data.get(record).mult;
        else if ( field.equals("DESC"))
            result = data.get(record).descr;
        else
            result = null;
            
        return(result);
    }

}
