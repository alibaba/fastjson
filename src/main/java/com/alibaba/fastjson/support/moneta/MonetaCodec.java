package com.alibaba.fastjson.support.moneta;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

public class MonetaCodec implements ObjectSerializer, ObjectDeserializer {
    public static final MonetaCodec instance = new MonetaCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Money money = (Money) object;
        if (money == null) {
            serializer.writeNull();
            return;
        }

        SerializeWriter out = serializer.out;
        out.writeFieldValue('{', "numberStripped", money.getNumberStripped());
        out.writeFieldValue(',', "currency", money.getCurrency().getCurrencyCode());
        out.write('}');
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONObject object = parser.parseObject();
        Object currency = object.get("currency");

        String currencyCode = null;
        if (currency instanceof JSONObject) {
            currencyCode = ((JSONObject) currency).getString("currencyCode");
        } else if (currency instanceof String) {
            currencyCode = (String) currency;
        }

        Object numberStripped = object.get("numberStripped");

        if (numberStripped instanceof BigDecimal) {
            return (T) Money.of((BigDecimal) numberStripped, Monetary.getCurrency(currencyCode));
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
