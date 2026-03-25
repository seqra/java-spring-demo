package org.seqra.demo.model;

import org.springframework.web.util.HtmlUtils;

public class EscapeFormatter implements IFormatter {
    @Override
    public String format(String value) {
        return HtmlUtils.htmlEscape(value);
    }
}
