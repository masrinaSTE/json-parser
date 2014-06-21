package json.parsing.blog;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by masrina on 6/19/14.
 */
public class ServiceHandler {
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler(){}

    public String makeServiceCall(String url, int method){
        return this.makeServiceCall(url, method, null);
    }

    private String makeServiceCall(String url, int method, List<NameValuePair> params) {
        try{
            // http client
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Check http Request method
            if(method == POST){
                HttpPost httpPost = new HttpPost(url);
                // add post params
                if(params != null){
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = defaultHttpClient.execute(httpPost);
            } else if(method == GET){
                if(params != null){
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = defaultHttpClient.execute(httpGet);
            }

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("RESPONSE", response);
        return response;
    }

}
