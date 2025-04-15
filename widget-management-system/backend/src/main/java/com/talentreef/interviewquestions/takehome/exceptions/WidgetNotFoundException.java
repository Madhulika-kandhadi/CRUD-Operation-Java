package com.talentreef.interviewquestions.takehome.exceptions;

public class WidgetNotFoundException extends RuntimeException
{
    public WidgetNotFoundException(String message) {
        super(message);
    }
}
