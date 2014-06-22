package json.parsing.blog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
/**
 * Created by masrina on 6/20/14.
 * implementation of Html.ImageGetter.
 * fetch the image from AsyncTask and update the image
 * http://stackoverflow.com/questions/7424512/android-html-imagegetter-as-asynctask/7442725#7442725
 */
public class URLImageParser implements Html.ImageGetter {
    Context context;
    TextView container;

    // will execute AsyncTask and refresh the container
    public URLImageParser(TextView view, Context c){
        this.context = c;
        this.container = view;
    }

    public Drawable getDrawable(String source){

        URLDrawable urlDrawable = new URLDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask= new ImageGetterAsyncTask(urlDrawable);
        asyncTask.execute(source);

        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable>{
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable urldraw){
            this.urlDrawable = urldraw;
        }

        @Override
        protected Drawable doInBackground(String... params){
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result){
            // set the correct bound according to the result from HTTP Call
            urlDrawable.setBounds(0,0,0 + result.getIntrinsicWidth(), 0 + result.getIntrinsicHeight());

            // change the reference of the current drawable to the result from HTTP Call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
            URLImageParser.this.container.invalidate();

            // For ICS
            URLImageParser.this.container.setHeight((URLImageParser.this.container.getHeight()
            + result.getIntrinsicHeight()));

            // pre ICS
            URLImageParser.this.container.setEllipsize(null);

        }
        // Get the drawable from URL
        public Drawable fetchDrawable(String urlString){
            try{
//                InputStream inputStream = fetch(urlString);
                InputStream inputStream = fetch(urlString);

                BufferedInputStream bufferedInputStream =
                        new BufferedInputStream(inputStream);
                Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream);
                bufferedInputStream.close();

//                Drawable d = new BitmapDrawable(bm);
//                d.setId("1");
//                TextView textView;
//                textView.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 1, 0);
//
                // convert Bitmap to Drawable
                Drawable drawable = new BitmapDrawable( context.getResources(), bm);

//                Drawable drawable = Drawable.createFromStream(inputStream, "src");
                drawable.setBounds(2, 2, 0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight());
                return drawable;
            }catch (Exception e){
                return null;
            }
        }

        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }
}
