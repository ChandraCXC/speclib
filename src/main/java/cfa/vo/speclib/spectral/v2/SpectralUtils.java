package cfa.vo.speclib.spectral.v2;

import cfa.vo.speclib.doc.ModelObjectFactory;

/**
 * Created by olaurino on 6/8/15.
 */
public class SpectralUtils {
    public static SpectralCharAxis getSpectralCharAxis(SpectralDataset ds) {
        SpectralCharAxis ret = (SpectralCharAxis) new ModelObjectFactory().newInstance(SpectralCharAxis.class);
        for (CharacterizationAxis axis : ds.getCharacterization().getCharacterizationAxes()) {
            if (axis instanceof SpectralCharAxis) {
                return (SpectralCharAxis) axis;
            }
        }

        return ret;
    }
}
