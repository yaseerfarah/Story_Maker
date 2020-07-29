package eg.com.ivas.ivas_story_maker.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import androidx.recyclerview.widget.RecyclerView;
import eg.com.ivas.ivas_story_maker.Interface.MvpPresenter;
import ja.burhanrashid52.photoeditor.ItemSelectListener;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.POJO.TemplateInfo;
import eg.com.ivas.ivas_story_maker.R;
import eg.com.ivas.ivas_story_maker.Util.SpeadyLinearLayoutManager;
import eg.com.ivas.ivas_story_maker.Util.TemplateDiffUtil;

import static eg.com.ivas.ivas_story_maker.View.ImageEditor.TEMPLATE_LINK;


public class HomeRycyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemSelectListener<CategoryInfo> {

    private Context context;
    private MvpPresenter mvpPresenter;
    private RecyclerView mRecyclerView;
    private CategoryInfo currentCategory;
    private List<CategoryInfo> categoryInfoList;
    private List<TemplateInfo> templateInfoList =new ArrayList<>();
    public static   final int CATEGORY_R=1;
    public static   final int TEMPLATE_R=2;
    public  static  final int PROGRESS=3;

    private boolean isLoading=false;

    private String[] image={

            "https://drive.google.com/uc?id=1QMllNz_ipO51tmQhtoEY5o_WZJx3KD5o",
            "https://drive.google.com/uc?id=1U_wDWvPLmSXVlVZW2bbpDve_qvEaBGM_"

    };


    private NavController navController;
    private CardCategoryAdapter cardCategoryAdapter;
    private Parcelable recyclerState;


    public HomeRycyclerAdapter(Context context, List<CategoryInfo> categoryInfoList,MvpPresenter mvpPresenter, NavController navController) {
        this.context = context;
        this.mvpPresenter=mvpPresenter;
        this.navController = navController;
        this.categoryInfoList=categoryInfoList;
       // this.templateInfoList.addAll(categoryInfoList.get(0).getTemplateInfoList());
        cardCategoryAdapter=new CardCategoryAdapter(context,categoryInfoList,this);
        if (!categoryInfoList.isEmpty()){
            currentCategory=categoryInfoList.get(0);
            templateInfoList.add(new TemplateInfo(0,"Category","Category"));
            templateInfoList.addAll(categoryInfoList.get(0).getTemplateInfoList());
        }


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

       // holder.setIsRecyclable(false);

        if (holder instanceof Categories_holder) {
            SpeadyLinearLayoutManager linearLayoutManager=new SpeadyLinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            linearLayoutManager.onRestoreInstanceState(recyclerState);
            ((Categories_holder) holder).categories_recycler.setHasFixedSize(true);
            ((Categories_holder) holder).categories_recycler.setLayoutManager(linearLayoutManager);
            ((Categories_holder) holder).categories_recycler.setAdapter(cardCategoryAdapter);
           // ((Categories_holder) holder).categories_recycler.setRecycledViewPool(mRecyclerView.getRecycledViewPool());



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


            ((Categories_holder) holder).categories_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    recyclerState=((Categories_holder) holder).categories_recycler.getLayoutManager().onSaveInstanceState();
                }
            });





        }else if (holder instanceof Template_holder)  {


            ((Template_holder) holder).progressBar.setVisibility(View.VISIBLE);
            Glide.with(context).load(templateInfoList.get(holder.getAdapterPosition()).getPreviewImage())
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


            ((Template_holder) holder).container.setOnClickListener(v -> {
                Bundle bundle=new Bundle();
               // bundle.putString(TEMPLATE_LINK,"https://drive.google.com/uc?id=1cSHU-9e2pCT4QcrUw4WMayYTElz3uw8k");
                bundle.putString(TEMPLATE_LINK,templateInfoList.get(holder.getAdapterPosition()).getTemplateImage());
                navController.navigate(R.id.action_home_to_imageEditor,bundle);
            });



        }else if (holder instanceof Progress_holder){
            ((Progress_holder) holder).progressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSelect(CategoryInfo categoryInfo) {
        if (isLoading){
            mvpPresenter.disposeNextPageCall();
        }
        currentCategory=categoryInfo;
        updateTemplateList(categoryInfo.getTemplateInfoList());

    }


    public CategoryInfo getCurrentCategory(){
        return currentCategory;
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

    public boolean isLoading(){
        return isLoading;
    }


    public void updateTemplateList(List<TemplateInfo> templateInfos){
        //Toast.makeText(context, String.valueOf(templateInfos.size()), Toast.LENGTH_SHORT).show();
        if (isLoading){
            removeLoading();
        }
        List<TemplateInfo> newList=new ArrayList<>();
        newList.add(new TemplateInfo(0,"Category","Category"));
        newList.addAll(templateInfos);

        TemplateDiffUtil templateDiffUtil=new TemplateDiffUtil(context,this.templateInfoList,newList);
        DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(templateDiffUtil);

        this.templateInfoList.clear();
        this.templateInfoList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);

    }

    public void updateNextPage(CategoryInfo categoryInfo){

        if (isLoading){
            removeLoading();
            updateTemplateList(categoryInfo.getTemplateInfoList());
        }

        for(CategoryInfo cat:categoryInfoList){
            if (cat.getId()==categoryInfo.getId()){
                cat.setTemplateInfoList(categoryInfo.getTemplateInfoList());
            }
        }

    }



    @Override
    public int getItemCount() {
        return templateInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return CATEGORY_R;
        }
        else if(isLoading&&position==templateInfoList.size()-1) {
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
            container=itemView.findViewById(R.id.card_view);
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
