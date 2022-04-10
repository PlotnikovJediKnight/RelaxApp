package by.bsuir.relaxapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ProfileImageGridAdapter extends BaseAdapter {

    Context context;
    Bitmap[] userProfileImages;

    LayoutInflater inflater;

    public ProfileImageGridAdapter(Context context, Bitmap[] images) {
        this.context = context;
        this.userProfileImages = images;
    }

    @Override
    public int getCount() {
        return (ProfileFragment.ADD_IMAGE_INDEX == -1) ? ProfileFragment.MAX_ATTACH_PHOTOS : ProfileFragment.ADD_IMAGE_INDEX + 1;
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
        imageView.setImageBitmap(userProfileImages[i]);

        return view;
    }
}
