package io.javaoperatorsdk.example.basicnative;

import io.fabric8.kubernetes.api.builder.Function;
import io.fabric8.kubernetes.client.CustomResourceDoneable;

public class CustomServiceDonable extends CustomResourceDoneable<CustomService> {
    public CustomServiceDonable(CustomService resource, Function function) {
        super(resource, function);
    }
}
