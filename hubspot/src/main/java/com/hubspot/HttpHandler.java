package com.hubspot;

import java.io.InputStream;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;

public class HttpHandler {
    private HttpURLConnection httpEndpoint;
    private HttpsURLConnection secureEndpoint;

    public HttpURLConnection getHttpEndpoint() {
        return httpEndpoint;
    }
    
    public HttpsURLConnection getSecureEndpoint() {
        return secureEndpoint;
    }
    

    public HttpHandler(){}

    public HttpHandler(String endpoint) throws Exception {
        try {
            if(StringUtils.isNotEmpty(endpoint)) {
                this.httpEndpoint = UtilHelper.getHttp(endpoint);
                this.secureEndpoint = UtilHelper.getHttpS(endpoint);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     *
     * @return
     */
    public String doGet() throws Exception {
        int status=0;
        String response="";
        try {
            if (null != this.secureEndpoint) {
                this.secureEndpoint.setRequestMethod("GET");
                this.secureEndpoint.setInstanceFollowRedirects(true);
                status = this.secureEndpoint.getResponseCode();
                if (status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_MOVED_TEMP) {
                    String loc = this.secureEndpoint.getHeaderField("Location");
                    this.secureEndpoint = UtilHelper.getHttpS(loc);
                    status = this.secureEndpoint.getResponseCode();
                }
                if(status == HttpURLConnection.HTTP_OK){
                    response = UtilHelper.readHttpResponse(this.secureEndpoint.getInputStream());
                }
            } else if (null != this.httpEndpoint) {
                this.httpEndpoint.setRequestMethod("GET");
                this.httpEndpoint.setInstanceFollowRedirects(true);
                status = this.httpEndpoint.getResponseCode();
                if (status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_MOVED_TEMP) {
                    String loc = this.httpEndpoint.getHeaderField("Location");
                    this.httpEndpoint = UtilHelper.getHttp(loc);
                    status = this.httpEndpoint.getResponseCode();
                }
                if(status == HttpURLConnection.HTTP_OK){
                    response = UtilHelper.readHttpResponse(this.httpEndpoint.getInputStream());
                }
            } else {
                //no src http
            }

            if(status == HttpURLConnection.HTTP_OK){
                response = UtilHelper.readHttpResponse(this.httpEndpoint.getInputStream());
            }
        } catch (Exception e){
            //e.printStackTrace();
            throw new Exception(e);
        }

        return response;
    }

    /**
     *
     * @param params
     * @return
     */
    public String doPost(Map<String, String> params){
        String response="";
        int status;
        try{
            if(null != this.secureEndpoint){
                this.secureEndpoint.setRequestMethod("POST");
                UtilHelper.setHttpHeaders_Payload(params, null, this.secureEndpoint);
                status = this.secureEndpoint.getResponseCode();
                InputStream is = this.secureEndpoint.getInputStream();
                if(null!= is){
                    response = UtilHelper.readHttpResponse(is);
                }
            } else if(null != this.httpEndpoint){
                this.httpEndpoint.setRequestMethod("POST");
                UtilHelper.setHttpHeaders_Payload(params, this.httpEndpoint, null);
                status = this.httpEndpoint.getResponseCode();
                InputStream is = this.httpEndpoint.getInputStream();
                if(null!= is){
                    response = UtilHelper.readHttpResponse(is);
                }
            } else{

            }
        } catch (Exception e){

        }
        return response;
    }
}
