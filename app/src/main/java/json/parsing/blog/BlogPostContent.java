package json.parsing.blog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Created by masrina on 6/19/14.
 */
public class BlogPostContent extends Activity {
    // JSON nodes keys
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // get intent data
        Intent i = getIntent();

        // Get JSON values from previous intent
        String postitle = i.getStringExtra(TAG_TITLE);
        String post_content = i.getStringExtra(TAG_CONTENT);

        // Display value on the screen
        TextView blog_post_title = (TextView) findViewById(R.id.blogpost_title);
        TextView blog_post_content = (TextView) findViewById(R.id.blogpost_content);

        blog_post_title.setText(postitle);

        URLImageParser p = new URLImageParser(blog_post_content, this);
        Spanned htmlSpan = Html.fromHtml(post_content, p, null);
        blog_post_content.setMovementMethod(LinkMovementMethod.getInstance());
//        blog_post_content.setCompoundDrawablesWithIntrinsicBounds(0, 0 , 0, 0);
        blog_post_content.setText(htmlSpan);
    }
}
