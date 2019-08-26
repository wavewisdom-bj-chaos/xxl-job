 package com.xxl.job.admin.entity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author B250M-J
 * @date 2019/08/26
 */
@Component
public class AcmValue {

    @Value("${xxl.job.i18n}")
     private  String group;
     
     @Value("${server.port}")
     private  String endpoint;
     
  //   @Value("${datasource.url}")
     private  String namespace;
     
    // @Value("${datasource.url}")
     private  String accessKey;
     
     //@Value("${datasource.url}")
     private  String secretKey;
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public String getAccessKey() {
        return accessKey;
    }
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
     
    public String  toString() {
        return "["+this.group+"]"+"["+this.endpoint+"]"+"["+this.namespace+"]"+"["+this.accessKey+"]"+"["+this.secretKey+"]";
        
    }
     
}
