package eg.com.ivas.ivas_story_maker.Adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import eg.com.ivas.ivas_story_maker.Interface.FilterBase;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.POJO.FilterInfo;
import eg.com.ivas.ivas_story_maker.POJO.SpiralFilter;
import eg.com.ivas.ivas_story_maker.R;
import ja.burhanrashid52.photoeditor.ItemSelectListener;
import ja.burhanrashid52.photoeditor.PhotoFilter;

import static eg.com.ivas.ivas_story_maker.Util.ImageUtils.getBitmapFromAsset;




public class CardFilterAdapter<T extends FilterBase> extends RecyclerView.Adapter<CardFilterAdapter<T>.Category_holder> {


    private Context context;
    private List<T> photoFilterList;
    private ItemSelectListener<T> categorySelectListener;
    private Category_holder lastCheck;
    private int button_index;




    public CardFilterAdapter(Context context, List<T> photoFilters, ItemSelectListener<T> categorySelectListener) {
        this.context = context;
        this.photoFilterList = photoFilters;
        this.categorySelectListener = categorySelectListener;
        this.button_index =0;
        categorySelectListener.onSelect(photoFilters.get(0));
        //categorySelectListener.onSelect(categoryInfoList.get(0));

    }



    @NonNull
    @Override
    public Category_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_cardview, parent, false);
        return new Category_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Category_holder holder, final int position) {

        holder.setIsRecyclable(false);
        String title=photoFilterList.get(holder.getAdapterPosition()).getFilterTitle();
        String imagePreview = photoFilterList.get(holder.getAdapterPosition()).getFilterPreviewImage();

        holder.title.setText(title);

        holder.icon.setImageBitmap(getBitmapFromAsset(context,imagePreview));

        if (button_index==holder.getAdapterPosition()){
            holder.transition.startTransition(0);
            lastCheck=holder;
        }

        holder.container.setOnClickListener(v -> {
            categorySelectListener.onSelect(photoFilterList.get(holder.getAdapterPosition()));
            lastCheck.transition.reverseTransition(200);
            holder.transition.startTransition(200);
            lastCheck=holder;
            button_index=holder.getAdapterPosition();

        });






    }






    @Override
    public int getItemCount() {
        return photoFilterList.size();
    }


    //////////////////////////////////////////////////////////
    public class Category_holder extends RecyclerView.ViewHolder{
        CardView container;
        TextView title;
        ImageView icon;
        ProgressBar progressBar;
        RelativeLayout background;
        TransitionDrawable transition;
        public Category_holder(View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.cat_card);
            title=itemView.findViewById(R.id.category_name);
            icon=itemView.findViewById(R.id.category_image);
            progressBar=itemView.findViewById(R.id.progress);
            background=itemView.findViewById(R.id.background);
            transition=(TransitionDrawable)background.getBackground();

        }
    }


}


