package edu.java.provider;

import edu.java.provider.api.InformationProvider;
import java.util.HashMap;
import java.util.Map;

public class InformationProviders {

    private final Map<String, InformationProvider> providers = new HashMap<>();

    public InformationProvider getProvider(String providerType) {
        return providers.get(providerType);
    }

    public void registerProvider(String informationProviderType, InformationProvider provider) {
        providers.put(informationProviderType, provider);
    }

}
