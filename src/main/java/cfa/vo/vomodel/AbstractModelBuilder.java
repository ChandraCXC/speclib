package cfa.vo.vomodel;

import java.io.IOException;
import java.net.URL;

/**
 * Created by olaurino on 6/4/15.
 */
public abstract class AbstractModelBuilder implements ModelBuilder {
    private URL refURL;
    private String prefix;
    private String name;
    private String title;

    public ModelMetadata getModelMetadata() {
        return new ModelMetadata() {
            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public String getModelName() {
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
        };
    }

    @Override
    public AbstractModelBuilder withModelMetadata(ModelMetadata modelMetadata) {
        if (modelMetadata != null) {
            this.name = modelMetadata.getModelName();
            this.prefix = modelMetadata.getPrefix();
            this.refURL = modelMetadata.getReferenceURL();
            this.title = modelMetadata.getTitle();
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
