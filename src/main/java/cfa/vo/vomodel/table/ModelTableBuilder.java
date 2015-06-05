package cfa.vo.vomodel.table;

import cfa.vo.vomodel.AbstractURLModelBuilder;

import java.io.IOException;
import java.net.URL;

/**
 * Created by olaurino on 6/3/15.
 */
public class ModelTableBuilder extends AbstractURLModelBuilder {

    public ModelTableBuilder(URL url) {
        super(url);
    }

    @Override
    public ModelTable build() throws IOException {
        return new ModelTable(this);
    }
}
