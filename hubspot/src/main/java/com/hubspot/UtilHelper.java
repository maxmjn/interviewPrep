package com.hubspot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.lang3.StringUtils;

public class UtilHelper {
    
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String AUTHORIZATION = "Authorization";
    public static final String UTF_8 = StandardCharsets.UTF_8.name();
    public static final String PAYLOAD = "payload";

    /**
     *
     * @param s
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getHttp(String s) throws IOException {

        URL url = new URL(s);
        return (HttpURLConnection) url.openConnection();
    }


    /**
     *
     * @param s
     * @return
     * @throws IOException
     */
    public static HttpsURLConnection getHttpS(String s) throws IOException {
        URL url = new URL(s);
        return (HttpsURLConnection) url.openConnection();
    }

    /**
     *
     * @param is
     * @return
     */
    public static String readHttpResponse(InputStream is) throws Exception {
        StringBuffer sb = new StringBuffer();
        try(
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is)
                )
        ){
            String input;
            while (null!=(input=br.readLine())){
                sb.append(input);
            }
            //System.out.println("Response:" + sb);
        }catch (Exception e){
            //e.printStackTrace();
            throw new Exception(e);
        }
        return sb.toString();
    }

    /**
     * 
     * @param user
     * @param pwd
     * @return
     */
    public static String getBasicAuth(String user, String pwd){
        String basicAuth = null;
        if(StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(pwd)){
            basicAuth = Base64.getEncoder()
                .encodeToString((user + ":" + pwd)
                    .getBytes(StandardCharsets.UTF_8))
            ;
            basicAuth = "Basic " + basicAuth;
        }
        return basicAuth;
    }
    
    /**
     *
     * @param params
     * @param httpEndpoint
     * @param secureEndpoint
     */
    public static void setHttpHeaders_Payload(Map<String, String> params, HttpURLConnection httpEndpoint, HttpsURLConnection secureEndpoint)
        throws Exception {
        OutputStream os=null;
        
        //headers without DEFAULTS
        //"application/x-www-form-urlencoded"
        String contentType ="application/json; charset=UTF-8";
        String contentLanguage = "en-US";
        String accept = "application/json";
        
        //headers without DEFAULTS
        String contentLength = null; //"payload".getBytes().length
        String basicAuth = null;
        String payload = null;
        if(params!=null && params.size() > 0){
            //headers with DEFAULTS
            if (params.get(CONTENT_TYPE)!=null){
                contentType = params.get(CONTENT_TYPE);
            }
            if(params.get(ACCEPT)!=null){
                accept=params.get(ACCEPT);
            }
            if(params.get(CONTENT_LANGUAGE)!=null){
                contentLanguage = params.get(CONTENT_LANGUAGE);
            }

            //headers without DEFAULTS
            if(params.get(CONTENT_LENGTH)!=null){
                contentLength = params.get(CONTENT_LENGTH);
            }
            if(params.get(AUTHORIZATION)!=null){
                basicAuth=params.get(AUTHORIZATION);
            }
            if(params.get(PAYLOAD)!=null){
                payload=params.get(PAYLOAD);
            }
        }

        try {
            
            if(null!=secureEndpoint){
                secureEndpoint.setDoInput(true);
                secureEndpoint.setDoOutput(true);
                //headers with DEFAULTS
                secureEndpoint.setRequestProperty(CONTENT_TYPE, contentType);
                secureEndpoint.setRequestProperty(ACCEPT, accept);
                secureEndpoint.setRequestProperty(CONTENT_LANGUAGE, contentLanguage);
                
                //headers without DEFAULTS
                if(StringUtils.isNotEmpty(contentLength)) {
                    secureEndpoint.setRequestProperty(CONTENT_LENGTH, contentLength);
                }
                if(StringUtils.isNotEmpty(basicAuth)) {
                    secureEndpoint.setRequestProperty(AUTHORIZATION, basicAuth);
                }

                secureEndpoint.setUseCaches(false);

                os = secureEndpoint.getOutputStream();
            } else if(null!=httpEndpoint) {
                httpEndpoint.setDoInput(true);
                httpEndpoint.setDoOutput(true);
                //headers with DEFAULTS
                httpEndpoint.setRequestProperty(CONTENT_TYPE, contentType);
                httpEndpoint.setRequestProperty(ACCEPT, accept);
                httpEndpoint.setRequestProperty(CONTENT_LANGUAGE, contentLanguage);

                //headers without DEFAULTS
                if(StringUtils.isNotEmpty(contentLength)) {
                    httpEndpoint.setRequestProperty(CONTENT_LENGTH, contentLength);
                }
                if(StringUtils.isNotEmpty(basicAuth)) {
                    httpEndpoint.setRequestProperty(AUTHORIZATION, basicAuth);
                }

                httpEndpoint.setUseCaches(false);

                os = httpEndpoint.getOutputStream();
            }
            if(null!=os && StringUtils.isNotEmpty(payload)) {
                os.write(payload.getBytes(UTF_8));
                os.flush();
                os.close();
            }

        }catch (Exception e){
            //e.printStackTrace();
            throw new Exception(e);
        }
    }
}
