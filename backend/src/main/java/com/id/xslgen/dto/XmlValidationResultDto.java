package com.id.xslgen.dto;

public record XmlValidationResultDto(
        boolean valid,
        XmlValidationErrorDto error
) {
    public static XmlValidationResultDto ok() { return new XmlValidationResultDto(true, null); }
    public static XmlValidationResultDto fail(XmlValidationErrorDto error) { return new XmlValidationResultDto(false, error); }
}
