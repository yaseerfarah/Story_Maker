package eg.com.ivas.ivas_story_maker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import ja.burhanrashid52.photoeditor.ItemSelectListener;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.POJO.TemplateInfo;
import eg.com.ivas.ivas_story_maker.R;


/**
 * Created by DELL on 1/17/2019.
 */

public class CardTemplateAdapter extends RecyclerView.Adapter<CardTemplateAdapter.Category_holder> implements ItemSelectListener<CategoryInfo> {


    private Context context;
    private List<TemplateInfo> templateInfoList =new ArrayList<>();
    private NavController navController;



    public CardTemplateAdapter(Context context,NavController navController) {
        this.context = context;
        this.navController=navController;

    }

    @NonNull
    @Override
    public Category_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);


        return new Category_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Category_holder holder, final int position) {

        holder.progressBar.setVisibility(View.VISIBLE);
       /* Glide.with(context).load(templateInfoList.get(holder.getAdapterPosition()).getPreviewImage())
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
                .into(holder.imageView);*/






    }




    @Override
    public int getItemCount() {
        return templateInfoList.size();
    }

    @Override
    public void onSelect(CategoryInfo categoryInfo) {
        templateInfoList.clear();
        templateInfoList.addAll(categoryInfo.getTemplateInfoList());
       // Toast.makeText(context,String.valueOf(templateInfoList.size()),Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }


    //////////////////////////////////////////////////////////
    public class Category_holder extends RecyclerView.ViewHolder{
        CardView container;
       ImageView imageView;
       ProgressBar progressBar;
        public Category_holder(View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.cat_card);
            imageView=itemView.findViewById(R.id.image_view);
            progressBar=itemView.findViewById(R.id.progress);


        }
    }


}


