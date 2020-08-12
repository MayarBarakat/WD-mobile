package com.example.wdmobilee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class FolderRVAdapter extends RecyclerView.Adapter<FolderRVAdapter.FolderViewHolder> implements Serializable {
    private ArrayList<Folder>folders;
    private onRecyclerViewItemClickListener listener;

    public FolderRVAdapter(ArrayList<Folder> folders, onRecyclerViewItemClickListener listener) {
        this.folders = folders;
        this.listener = listener;
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout,null,false);
        FolderViewHolder folderViewHolder=new FolderViewHolder(v);
        return folderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder folder=folders.get(position);
        holder.iv.setImageResource(R.drawable.folder);
        holder.tv_name.setText(folder.getName());
        holder.tv_date.setText(folder.getDate());
        holder.iv.setTag(folder.getPath());
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView tv_name,tv_date;
        public FolderViewHolder(@NonNull final View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.folder_iv);
            tv_name=itemView.findViewById(R.id.folder_tv_name);
            tv_date=itemView.findViewById(R.id.folder_tv_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path=(String)iv.getTag();
                    listener.onItemClick(path);
                }
            });
        }
    }
}
