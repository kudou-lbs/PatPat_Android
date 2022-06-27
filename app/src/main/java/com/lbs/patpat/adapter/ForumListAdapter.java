package com.lbs.patpat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lbs.patpat.BR;
import com.lbs.patpat.R;
import com.lbs.patpat.global.CircleImageDrawable;
import com.lbs.patpat.model.ForumModel;
import com.lbs.patpat.viewmodel.ListViewModel;

import java.util.List;

public class ForumListAdapter extends RecyclerView.Adapter<ForumListAdapter.ViewHolder>{

    private Context context;
    private List<ForumModel> forumModelList; //信息列表
    private OnItemClickListener mOnItemClickListener; //点击响应

    public ForumListAdapter(Context context, List<ForumModel> forumModelList, OnItemClickListener mOnItemClickListener){
        this.context=context;
        this.forumModelList=forumModelList;
        this.mOnItemClickListener=mOnItemClickListener;
    }

    public void setForumModelList(List<ForumModel> forumModelList) {
        this.forumModelList = forumModelList;
        Log.d("lbs","notifychange");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_search_forum,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //设置图标，这里还需要再network里写相关方法
//        holder.icon.setImageDrawable(context.getDrawable(R.drawable.patpat));
        Glide.with(context)
                .load(forumModelList.get(position).getForumIcon())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(holder.icon);
        holder.name.setText(forumModelList.get(position).getForumName());
        holder.intro.setText(new StringBuilder()
                .append(forumModelList.get(position).getForumFollowNum())
                .append("关注 ")
                .append(forumModelList.get(position).getForumPostNum())
                .append("帖子")
                .toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(forumModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(forumModelList!=null){
            return forumModelList.size();
        }
        return 0;
    }

    public interface OnItemClickListener{
        public void onItemClick(ForumModel forumModel);
    }

    //自定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView name;
        TextView intro;
        public ViewHolder(View view) {
            super(view);
            icon=view.findViewById(R.id.item_forum_icon);
            name=view.findViewById(R.id.item_forum_name);
            intro=view.findViewById(R.id.item_forum_intro);
        }

    }
}
