package org.example.uthteammatching.models;

public enum ProjectType {
    HOC_THUAT("học thuật"),
    BEN_NGOAI("bên ngoài");

    private final String displayName;

    ProjectType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}