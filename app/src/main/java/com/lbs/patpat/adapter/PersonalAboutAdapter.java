package com.lbs.patpat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lbs.patpat.R;
import com.lbs.patpat.model.PersonalModel;

import java.util.List;

public class PersonalAboutAdapter extends RecyclerView.Adapter<PersonalAboutAdapter.ViewHolder>{

    private Context context;
    private List<PersonalModel> personalModelList; //信息列表
    private PersonalAboutAdapter.OnItemClickListener mOnItemClickListener; //点击响应

    public PersonalAboutAdapter(Context context, List<PersonalModel> personalModelList){
        this.context=context;
        this.personalModelList = personalModelList;
        //this.mOnItemClickListener=mOnItemClickListener;
    }

    public void setPersonalInfoItemList(List<PersonalModel> personalModelList) {
        this.personalModelList = personalModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonalAboutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_personal_about,parent,false);
        return new PersonalAboutAdapter.ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull PersonalAboutAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //设置图标，这里还需要再network里写相关方法
        PersonalModel currentUser= personalModelList.get(position);
        holder.title.setText(currentUser.getTitle());
        holder.detail.setText(currentUser.getDetail());

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
        if(personalModelList !=null){
            return personalModelList.size();
        }
        return 0;
    }

    public interface OnItemClickListener{
        public void onItemClick(PersonalModel personalModel);
    }

    //自定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView detail;
        public ViewHolder(View view) {
            super(view);
            title=view.findViewById(R.id.item_personal_about_title);
            detail=view.findViewById(R.id.item_personal_about_detail);
        }
    }
}
