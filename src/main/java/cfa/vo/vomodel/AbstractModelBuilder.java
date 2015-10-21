package cfa.vo.vomodel;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Abstract Class for IVOA Model Definitions Builders.
 * 
 * This suite of Classes build and populate the Model object
 * from a variety of storage mechanisms.
 * 
 * We define a set of methods which allow the user to override
 * the primary Model information.
 * 
 * Created by olaurino on 6/4/15.
 */
public abstract class AbstractModelBuilder implements ModelBuilder {
    private URL refURL;
    private String prefix;
    private String name;
    private String title;
    private List<Entry> entries;

    public ModelData getModelData() {
        return new ModelData() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getPrefix() {
                return prefix;
            }

            @Override
            public URL getReferenceURL() {
                return refURL;
            }

            @Override
            public List<Entry> getEntries() {
                return entries;
            }
        };
    }

    @Override
    public AbstractModelBuilder withModelData(ModelData modelData) {
        if (modelData != null) {
            this.name = modelData.getName();
            this.prefix = modelData.getPrefix();
            this.refURL = modelData.getReferenceURL();
            this.title = modelData.getTitle();
            this.entries = modelData.getEntries();
        }
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public AbstractModelBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AbstractModelBuilder withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public AbstractModelBuilder withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public URL getRefURL() {
        return refURL;
    }

    @Override
    public AbstractModelBuilder withRefURL(URL refURL) {
        this.refURL = refURL;
        return this;
    }

    public abstract Model build() throws IOException;
}
