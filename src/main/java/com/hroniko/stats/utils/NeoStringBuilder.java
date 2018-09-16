package com.hroniko.stats.utils;

public class NeoStringBuilder {
    private StringBuilder stringBuilder;

    public NeoStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public NeoStringBuilder() {
        this.stringBuilder = new StringBuilder();
    }

    public NeoStringBuilder add(String word){
        stringBuilder.append(word);
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
