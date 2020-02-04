package com.example.secretphotovideolocker;

import android.content.Context;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryItemAdapter extends RecyclerView.Adapter<GalleryItemAdapter.GalleryItemViewHolder> {

    public interface ItemSelectedChangeListener{
        void onItemSelectedChange(int number);
    }

    private ItemSelectedChangeListener listener;

    private List<Picture> pictures;
    private Context context;

    private List<Picture> picturesSelected;

    int count = 0;

    public GalleryItemAdapter(Context context, List<Picture> pictures,ItemSelectedChangeListener listener) {
        this.context = context;
        this.pictures = pictures;
        this.listener=listener;
        this.picturesSelected = new ArrayList<>();
    }

    @NonNull
    @Override
    public GalleryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_gallery_picture, parent, false);
        return new GalleryItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryItemViewHolder holder, int position) {
        holder.bind(pictures.get(position), position);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }


    public class GalleryItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPictureItem;
        TextView textViewSelectCount;
        ConstraintLayout constraintLayoutItemGallery;

        public GalleryItemViewHolder(View itemView) {
            super(itemView);
            imageViewPictureItem = itemView.findViewById(R.id.imageViewPictureItem);
            textViewSelectCount = itemView.findViewById(R.id.item_gallery_slect_count);
            constraintLayoutItemGallery = itemView.findViewById(R.id.item_gallery);
        }

        public void bind(final Picture picture, final int position) {
            RequestOptions options = new RequestOptions().override(0).placeholder(R.drawable.ic_launcher_background);
//                    .centerCrop()
//                    .placeholder(R.drawable.ic_camera)
//                    .error(R.drawable.ic_send)
//                    .priority(Priority.HIGH);
            Glide.with(context)
                    .load(picture.getPath())
                    .apply(options)
                    .into(imageViewPictureItem);

            if (picture.getSelectCount() > 0) {
                textViewSelectCount.setText(picture.getSelectCount() + "");
                textViewSelectCount.setBackground(context.getResources().getDrawable(R.drawable.background_count_selected));
            } else {
                textViewSelectCount.setText("");
                textViewSelectCount.setBackground(context.getResources().getDrawable(R.drawable.background_count_not_selected));
            }

            constraintLayoutItemGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int removeSelectCount;
                    picture.setPosition(position);
                    if (picture.getSelectCount() > 0) {
                        count--;
                        textViewSelectCount.setText("");
                        textViewSelectCount.setBackground(context.getResources().getDrawable(R.drawable.background_count_not_selected));
                        //remove object from hashmap
                        //picturesSelectedMap.remove(picture);

                        picturesSelected.remove(picture);
                        //cập nhật lại vị trí
                        for (Picture pictureUpdate : pictures) {
                            if (pictureUpdate.getSelectCount() > picture.getSelectCount()) {
                                pictureUpdate.setSelectCount(pictureUpdate.getSelectCount() - 1);
                                notifyItemChanged(pictureUpdate.getPosition());
                            }
                        }
                        removeSelectCount=picture.getSelectCount();
                        picture.setSelectCount(0);

                    } else {
                        count++;
                        picture.setSelectCount(count);
                        //add object to hashmap
                        picturesSelected.add(picture);
                        //picturesSelected.add(picture);
                        textViewSelectCount.setText(picture.getSelectCount() + "");
                        textViewSelectCount.setBackground(context.getResources().getDrawable(R.drawable.background_count_selected));
                    }

                    listener.onItemSelectedChange(picturesSelected.size());
                }
            });
        }


    }

    //get all picture selected
    public ArrayList<Picture> getAllPictureSelected(){


        Collections.sort(picturesSelected, new Comparator<Picture>() {
            @Override
            public int compare(Picture o1, Picture o2) {
                return o1.getSelectCount()>=o2.getSelectCount()?1:-1;
            }
        });



        return (ArrayList<Picture>) picturesSelected;
    }
}
