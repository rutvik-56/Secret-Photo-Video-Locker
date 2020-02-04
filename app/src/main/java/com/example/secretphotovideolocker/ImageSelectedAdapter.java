package com.example.secretphotovideolocker;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ImageSelectedAdapter extends RecyclerView.Adapter<ImageSelectedAdapter.ImageItemViewHolder>{

    private Context context;
    private List<Picture> pictures;

    public ImageSelectedAdapter(Context context,List<Picture> pictures){
        this.context=context;
        this.pictures=pictures;
    }

    @NonNull
    @Override
    public ImageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_imge_selected,parent,false);
        return new ImageItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageItemViewHolder holder, int position) {
            holder.bind(pictures.get(position));
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public class ImageItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewSeleted;
        public ImageItemViewHolder(View itemView) {
            super(itemView);
            imageViewSeleted=itemView.findViewById(R.id.imageViewPictureSelectedItem);
        }

        public void bind(Picture picture) {
            RequestOptions options = new RequestOptions().override(0).placeholder(R.drawable.ic_launcher_background);
//                    .centerCrop()
//                    .placeholder(R.drawable.ic_camera)
//                    .error(R.drawable.ic_send)
//                    .priority(Priority.HIGH);
            Glide.with(context)
                    .load(picture.getPath())
                    .apply(options)
                    .into(imageViewSeleted);
        }
    }
}
