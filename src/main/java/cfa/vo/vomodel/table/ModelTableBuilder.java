package cfa.vo.vomodel.table;

import cfa.vo.vomodel.AbstractURLModelBuilder;
import cfa.vo.vomodel.Entry;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olaurino on 6/3/15.
 */
public class ModelTableBuilder extends AbstractURLModelBuilder {
    private Map<Entry, Entry> overrides = new HashMap();

    public ModelTableBuilder(URL url) {
        super(url);
    }

    @Override
    public ModelTable build() throws IOException {
        return new ModelTable(this);
    }

    public ModelTableBuilder overrideEntries(Map<Entry, Entry> overrides) {
        this.overrides = overrides;
        return this;
    }

    public Map<Entry, Entry> getOverrides() {
        return this.overrides;
    }
}
