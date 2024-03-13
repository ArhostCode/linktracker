package edu.java.provider.api;

import edu.java.provider.InformationProviders;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;

public interface InformationProvider {

    boolean isSupported(URI url);

    String getType();

    LinkInformation fetchInformation(URI url);

    @Autowired
    default void registerSelf(InformationProviders providers) {
        providers.registerProvider(getType(), this);
    }
}
