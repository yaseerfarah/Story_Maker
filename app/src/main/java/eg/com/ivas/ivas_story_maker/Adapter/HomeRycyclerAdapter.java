package eg.com.ivas.ivas_story_maker.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import eg.com.ivas.ivas_story_maker.Interface.CategorySelectListener;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.POJO.TemplateInfo;
import eg.com.ivas.ivas_story_maker.R;
import eg.com.ivas.ivas_story_maker.Util.EndlessRecyclerViewListener;
import eg.com.ivas.ivas_story_maker.Util.GridSpacingItemDecoration;
import eg.com.ivas.ivas_story_maker.Util.SpeadyLinearLayoutManager;
import eg.com.ivas.ivas_story_maker.Util.TemplateDiffUtil;


public class HomeRycyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CategorySelectListener {

    private Context context;
    private RecyclerView mRecyclerView;
    private List<CategoryInfo> categoryInfoList;
    private List<TemplateInfo> templateInfoList =new ArrayList<>();
    public static   final int CATEGORY_R=1;
    public static   final int TEMPLATE_R=2;
    public  static  final int PROGRESS=3;

    private boolean isLoading=false;

    private int[] image={
            R.mipmap.pic01,
            R.mipmap.pic02,
    };


    private NavController navController;
    private CardCategoryAdapter cardCategoryAdapter;



    public HomeRycyclerAdapter(Context context, List<CategoryInfo> categoryInfoList, NavController navController) {
        this.context = context;
        this.navController = navController;
        this.categoryInfoList=categoryInfoList;
        this.templateInfoList.addAll(categoryInfoList.get(0).getTemplateInfoList());
        cardCategoryAdapter=new CardCategoryAdapter(context,categoryInfoList,this::onSelect);

    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView=recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==CATEGORY_R) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recyclerview_layout, parent, false);
            return new HomeRycyclerAdapter.Categories_holder(view);
        }else if (viewType==TEMPLATE_R) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
            return new HomeRycyclerAdapter.Template_holder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new HomeRycyclerAdapter.Progress_holder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        if (holder instanceof Categories_holder) {

            SpeadyLinearLayoutManager linearLayoutManager=new SpeadyLinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            ((Categories_holder) holder).categories_recycler.setAdapter(cardCategoryAdapter);
            ((Categories_holder) holder).categories_recycler.setHasFixedSize(true);
            ((Categories_holder) holder).categories_recycler.setLayoutManager(linearLayoutManager);
            ((Categories_holder) holder).categories_recycler.setRecycledViewPool(mRecyclerView.getRecycledViewPool());

            ((Categories_holder) holder).end.setOnClickListener(v -> {
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < (cardCategoryAdapter.getItemCount() - 1 )&& ((Categories_holder) holder).categories_recycler!=null) {

                    linearLayoutManager.smoothScrollToPosition(((Categories_holder) holder).categories_recycler, new RecyclerView.State(), linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
            });

            ((Categories_holder) holder).start.setOnClickListener(v -> {
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0&& ((Categories_holder) holder).categories_recycler!=null) {

                    linearLayoutManager.smoothScrollToPosition(((Categories_holder) holder).categories_recycler, new RecyclerView.State(), linearLayoutManager.findFirstCompletelyVisibleItemPosition() - 1);
                }
            });


        }else if (holder instanceof Template_holder)  {


            ((Template_holder) holder).progressBar.setVisibility(View.VISIBLE);
            Glide.with(context).load(templateInfoList.get(holder.getAdapterPosition()-1).getPreviewImage())
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
                            ((Template_holder) holder).progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(((Template_holder) holder).imageView);
           // ((Template_holder) holder).imageView.setBackground(context.getDrawable(image[new Random().nextInt(1)]));





        }else if (holder instanceof Progress_holder){

        }

    }

    @Override
    public void onSelect(CategoryInfo categoryInfo) {
        updateTemplateList(categoryInfo.getTemplateInfoList());
    }


    public void addLoading(){
        isLoading=true;
        templateInfoList.add(new TemplateInfo());
        notifyItemInserted(templateInfoList.size()-1);
    }

    public void removeLoading(){
        isLoading=false;
        templateInfoList.remove(templateInfoList.size()-1);
        notifyItemRemoved(templateInfoList.size()-1);
    }



    public void updateTemplateList(List<TemplateInfo> templateInfos){
        TemplateDiffUtil templateDiffUtil=new TemplateDiffUtil(context,this.templateInfoList,templateInfos);
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(templateDiffUtil);

        this.templateInfoList.clear();
        this.templateInfoList.addAll(templateInfos);
        diffResult.dispatchUpdatesTo(this);

    }



    @Override
    public int getItemCount() {
        return templateInfoList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return CATEGORY_R;
        }
        else if(isLoading&&position==templateInfoList.size()) {
            return PROGRESS;
        }else {
            return TEMPLATE_R;
        }
    }





    //////////////////////////////////////////////////////////
    private class Categories_holder extends RecyclerView.ViewHolder{

        RecyclerView categories_recycler;
        ImageButton start,end;
        public Categories_holder(View itemView) {
            super(itemView);
            categories_recycler =itemView.findViewById(R.id.horizontalRecycler);
            start=itemView.findViewById(R.id.start);
            end=itemView.findViewById(R.id.end);
        }
    }


    //////////////////////////////////////////////////////////
    private class Template_holder extends RecyclerView.ViewHolder{

        CardView container;
        ImageView imageView;
        ProgressBar progressBar;
        public Template_holder(View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.cat_card);
            imageView=itemView.findViewById(R.id.image_view);
            progressBar=itemView.findViewById(R.id.progress);
        }
    }

    //////////////////////////////////////////////////////////
    private class Progress_holder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;
        public Progress_holder(View itemView) {
            super(itemView);
            progressBar=itemView.findViewById(R.id.progress);

        }
    }




}
