package json.parsing.blog;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {
    private ProgressDialog progressDialog;
    private static String url = "http://chinwyejin.com/wp_api/v1/posts?per_page=20&post_type=post";
    private final static String TAG_POST = "posts";
    private final static String TAG_ID = "id";
    private final static String TAG_TYPE = "type";
    private final static String TAG_TITLE = "title";
    private final static String TAG_CONTENT = "content";

    // posts JSON array
    JSONArray posts = null;

    // HashMap for ListView
    ArrayList<HashMap<String, String>> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        postList = new ArrayList<HashMap<String, String>>();
        ListView listView = getListView();

        // ListView on ItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting value from selected item
                String post_title = ((TextView) view.findViewById(R.id.post_title)).getText().toString();
                String blog_content =  ((TextView) view.findViewById(R.id.blog_content)).getText().toString();

                // Start activity_content
                Intent i = new Intent(getApplicationContext(), BlogPostContent.class);
                i.putExtra(TAG_TITLE, post_title);
                i.putExtra(TAG_CONTENT, blog_content);
                startActivity(i);
            }
        });

        new GetPostTitle().execute();
    }

    private class GetPostTitle extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // Display progress dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params){
            // Creating service handler class instance
            ServiceHandler serviceHandler = new ServiceHandler();

            // Making a request to url and get response
            String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
            Log.d("JSON response: ", jsonStr);
            if(jsonStr != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    // Get JSONArray node
                    posts = jsonObject.getJSONArray(TAG_POST);
                    // looping through all posts
                    for(int i=0; i< posts.length(); i++){
                        JSONObject object = posts.getJSONObject(i);
                        String type = object.getString(TAG_TYPE);
                        String id = object.getString(TAG_ID);
                        String title = object.getString(TAG_TITLE);
                        String content = object.getString(TAG_CONTENT);
                        String replaceContent = content.replaceAll("\n", "<br>");
                        // temporary HashMap for single post
                        HashMap<String, String> post = new HashMap<String, String>();
                        Log.i("type", type);
                        Log.i("content", content);
                        // add each value to HashMap key => value
                        post.put(TAG_ID, id);
                        post.put(TAG_TITLE, title);
                        post.put(TAG_CONTENT, replaceContent);

                        // add post to postList
                        if(type.contains("post")) {
                            postList.add(post);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Log.e("ServiceHandler", "couldn't get any data from url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            // update parsed JSON into ListView
            ListAdapter listAdapter = new SimpleAdapter(MainActivity.this, postList,
                    R.layout.list_item, new String[]{
                    TAG_TITLE, TAG_CONTENT}, new int[]{R.id.post_title, R.id.blog_content});
            setListAdapter(listAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
