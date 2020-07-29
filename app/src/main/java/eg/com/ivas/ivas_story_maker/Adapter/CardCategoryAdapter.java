package eg.com.ivas.ivas_story_maker.Adapter;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ja.burhanrashid52.photoeditor.ItemSelectListener;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.R;


/**
 * Created by DELL on 1/17/2019.
 */

public class CardCategoryAdapter extends RecyclerView.Adapter<CardCategoryAdapter.Category_holder> {


    private Context context;
    private RecyclerView mRecyclerView;
    private List<CategoryInfo> categoryInfoList ;
    private CategoryInfo currantCategory;
    private ItemSelectListener categorySelectListener;
    private Category_holder lastCheck;
    private int button_index;


    private int[] icons={
            R.drawable.ic_chat,
            R.drawable.ic_stories,
            R.drawable.ic_content,
            R.drawable.ic_stories,
            R.drawable.ic_stories,

    };


    public CardCategoryAdapter(Context context, List<CategoryInfo> categoryInfoList, ItemSelectListener categorySelectListener) {
        this.context = context;
        this.categoryInfoList = categoryInfoList;
        this.categorySelectListener = categorySelectListener;
        this.button_index =0;


    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView=recyclerView;
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
        holder.title.setText(categoryInfoList.get(holder.getAdapterPosition()).getTitle());
        //holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context).load(categoryInfoList.get(holder.getAdapterPosition()).getIconImage())
                .apply(RequestOptions.timeoutOf(60*1000))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(getClass().getName(),e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(holder.icon);

       // holder.icon.setBackground(context.getDrawable(icons[holder.getAdapterPosition()]));

        if (button_index==holder.getAdapterPosition()){
            holder.transition.startTransition(0);
            lastCheck=holder;

        }

        holder.container.setOnClickListener(v -> {
            categorySelectListener.onSelect(categoryInfoList.get(holder.getAdapterPosition()));
            lastCheck.transition.reverseTransition(200);
            holder.transition.startTransition(200);
            lastCheck=holder;
            button_index=holder.getAdapterPosition();

        });






    }




    @Override
    public int getItemCount() {
        return categoryInfoList.size();
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


