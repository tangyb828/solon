package org.noear.nami.common;

public class Constants {
    public static final String ct_hessian = "application/hessian";
    public static final String ct_protobuf = "application/protobuf";
    public static final String ct_json = "application/json";
    public static final String ct_form_urlencoded = "application/x-www-form-urlencoded";

    //
    //没有type，无法反序列化异常
    //
    public static final String at_type_json = "@type_json";
    public static final String at_protobuf = "@protobuf";
    public static final String at_hession = "@hession";

    public static final String m_get = "GET";
    public static final String m_post = "POST";

    public static final String h_serialization = "X-Serialization";
    public static final String h_content_type = "Content-Type";
    public static final String h_accept= "Accept";
}
