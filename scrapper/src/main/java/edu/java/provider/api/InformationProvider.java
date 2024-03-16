package edu.java.provider.api;

import edu.java.provider.InformationProviders;
import java.net.URI;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;

public interface InformationProvider {

    boolean isSupported(URI url);

    String getType();

    LinkInformation fetchInformation(URI url);

    LinkInformation filter(LinkInformation info, OffsetDateTime after, String optionalContext);

    default LinkInformation filter(LinkInformation info, OffsetDateTime after) {
        return filter(info, after, null);
    }

    @Autowired
    default void registerSelf(InformationProviders providers) {
        providers.registerProvider(getType(), this);
    }
}
