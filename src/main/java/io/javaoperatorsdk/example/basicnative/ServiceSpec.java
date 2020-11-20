package io.javaoperatorsdk.example.basicnative;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ServiceSpec {

    private String name;
    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
