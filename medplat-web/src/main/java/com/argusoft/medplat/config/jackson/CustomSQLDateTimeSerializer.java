package com.argusoft.medplat.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class CustomSQLDateTimeSerializer  extends StdSerializer<Date> {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

    protected CustomSQLDateTimeSerializer(Class<Date> t) {
        super(t);
    }

    public CustomSQLDateTimeSerializer() {
        this(null);
    }

    @Override
    public Class<Date> handledType() {
        return Date.class;
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider sp)
            throws IOException {
        gen.writeString(simpleDateFormat.format(value));
    }
}
