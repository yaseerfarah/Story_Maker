package eg.com.ivas.ivas_story_maker.Util;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import eg.com.ivas.ivas_story_maker.POJO.TemplateInfo;


public class TemplateDiffUtil extends DiffUtil.Callback {



    private List<TemplateInfo> oldList;
    private List<TemplateInfo> newList;

    Context context;

    public TemplateDiffUtil(Context context, List<TemplateInfo> oldList, List<TemplateInfo> newList) {
        this.oldList = oldList;
        this.newList = newList;
        this.context=context;
    }



    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId()==newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getPreviewImage().matches(newList.get(newItemPosition).getPreviewImage());
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

            return super.getChangePayload(oldItemPosition, newItemPosition);

    }
}
