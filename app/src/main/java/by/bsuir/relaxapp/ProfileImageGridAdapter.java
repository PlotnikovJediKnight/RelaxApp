package by.bsuir.relaxapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ProfileImageGridAdapter extends BaseAdapter {

    Context context;
    int[] userProfileImages;

    LayoutInflater inflater;

    public ProfileImageGridAdapter(Context context, int[] images) {
        this.context = context;
        this.userProfileImages = images;
    }

    @Override
    public int getCount() {
        return userProfileImages.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null){
            view = inflater.inflate(R.layout.photo_gallery_item, null);
        }

        ImageView imageView = view.findViewById(R.id.item_image);
        imageView.setImageResource(userProfileImages[i]);

        return view;
    }
}
