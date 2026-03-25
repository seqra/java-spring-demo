package org.seqra.demo.model;

public class DefaultFormatter implements IFormatter {
    @Override
    public String format(String value) {
        return value;
    }
}
