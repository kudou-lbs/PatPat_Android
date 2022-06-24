package com.lbs.patpat.adapter;

import android.content.Context;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lbs.patpat.R;
import com.lbs.patpat.model.UserModel;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    private Context context;
    private List<UserModel> userModelList; //信息列表
    private OnItemClickListener mOnItemClickListener; //点击响应

    public UserListAdapter(Context context, List<UserModel> userModelList){
        this.context=context;
        this.userModelList = userModelList;
        //this.mOnItemClickListener=mOnItemClickListener;
    }

    public void setUserModelList(List<UserModel> userModelList) {
        this.userModelList = userModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_search_user,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //设置图标，这里还需要再network里写相关方法
        UserModel currentUser=userModelList.get(position);
        Glide.with(context)
                .load("https://upload.wikimedia.org/wikipedia/zh/9/94/Genshin_Impact.jpg")
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.avatar);
        holder.nickname.setText(currentUser.getNickname());
        holder.idAndFans.setText("ID："+currentUser.getUid()
                +"\t粉丝："+currentUser.getFans_num());
        //还有关注的展示和点击事件没写

        //整个项的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(currentUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(userModelList !=null){
            return userModelList.size();
        }
        return 0;
    }

    public interface OnItemClickListener{
        public void onItemClick(UserModel userModel);
    }

    //自定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView nickname;
        TextView idAndFans;
        TextView followed;
        public ViewHolder(View view) {
            super(view);
            avatar=view.findViewById(R.id.item_user_avatar);
            nickname=view.findViewById(R.id.item_user_nickname);
            idAndFans=view.findViewById(R.id.item_user_id_and_fans);
            followed=view.findViewById(R.id.header_follow_and_fans);
        }

    }
}
