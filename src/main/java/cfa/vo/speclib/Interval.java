/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

/**
 *
 * @author mdittmar
 */
public class Interval extends Quantity {

    public Interval(){
        super();
        this.setValue( new Double[2] );
    }
    
    public Double getStart()
    {
        Double[] result;

        if (!this.hasValue())
            this.setValue( new Double[2] );

        result = (Double[])this.getValue();

        return result[0];
    }
    
    public Double getStop()
    {
        Double[] result;

        if (!this.hasValue())
            this.setValue( new Double[2] );

        result = (Double[])this.getValue();

        return result[1];
    }
    
    public void setStart( Double start )
    {
        Double[] value;

        if (!this.hasValue())
            this.setValue( new Double[2] );

        value = (Double[])this.getValue();
        value[0] = start;
    }

    public void setStop( Double stop )
    {
        Double[] value;

        if (!this.hasValue())
            this.setValue( new Double[2] );

        value = (Double[])this.getValue();
        value[1] = stop;
    }

}
