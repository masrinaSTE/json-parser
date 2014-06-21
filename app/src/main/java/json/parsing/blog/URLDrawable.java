package json.parsing.blog;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by masrina on 6/20/14.
 */
public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;

    @Override
    public void draw(Canvas canvas){
        // override the draw to facilitate refresh function later
        if(drawable != null){
            drawable.draw(canvas);
        }
    }
}
