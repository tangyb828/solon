package org.noear.nami.coder.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.noear.nami.Enctype;
import org.noear.nami.Encoder;

import java.nio.charset.StandardCharsets;


public class FastjsonEncoder implements Encoder {
    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    public static final FastjsonEncoder instance = new FastjsonEncoder();


    @Override
    public Enctype enctype() {
        return Enctype.application_json;
    }

    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONString(obj,
                SerializerFeature.BrowserCompatible,
                SerializerFeature.DisableCircularReferenceDetect)
                .getBytes(StandardCharsets.UTF_8);
    }
}