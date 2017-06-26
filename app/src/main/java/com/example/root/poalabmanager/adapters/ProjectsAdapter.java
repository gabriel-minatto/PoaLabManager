package com.example.root.poalabmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.poalabmanager.MainActivity;
import com.example.root.poalabmanager.MenuActivity;
import com.example.root.poalabmanager.R;
import com.example.root.poalabmanager.controllers.ProjectsController;
import com.example.root.poalabmanager.interfaces.ClickRecycler;
import com.example.root.poalabmanager.models.Projects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by minatto on 18/06/17.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder>{
    public static ClickRecycler clickRecycler;
    Context contexto;
    private List<Projects> projectsList;

    public ProjectsAdapter(Context contexto,List<Projects> projectsList){
        this.contexto = contexto;
        this.projectsList = projectsList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.projects_list_recycler,
                viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder,final int position){
        final Projects project = projectsList.get(position);
        viewHolder.viewName.setText(project.getName());
    }
    public void removeItem(int position){
        projectsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, projectsList.size(
        ));
    }
    @Override
    public int getItemCount() {
        return projectsList.size();
    }
    protected class MyViewHolder extends RecyclerView.ViewHolder{
        protected TextView viewName;
        public MyViewHolder(final View itemView){
            super(itemView);
            viewName = (TextView) itemView.findViewById(R.id.project_name);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    clickRecycler.onCustomClick(projectsList.get(getLayoutPosition()));
                }
            });
        }
    }
}
