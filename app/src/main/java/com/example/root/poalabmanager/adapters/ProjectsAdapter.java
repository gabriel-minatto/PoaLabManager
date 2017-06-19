package com.example.root.poalabmanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.poalabmanager.MainActivity;
import com.example.root.poalabmanager.R;
import com.example.root.poalabmanager.controllers.ProjectsController;
import com.example.root.poalabmanager.interfaces.ClickRecycler;
import com.example.root.poalabmanager.models.Projects;

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
        this.clickRecycler = clickRecycler;
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
        viewHolder.viewNomeCidade.setText(project.getName());
        viewHolder.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ProjectsAdapter.this.contexto instanceof MainActivity){
                    try {
                        ((MainActivity)ProjectsAdapter.this.contexto).projectController.deleteById(project.getId());
                        //https://stackoverflow.com/questions/12142255/call-activity-method-from-adapter
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    removeItem(position);
                }

            }
        });
    }
    private void removeItem(int position){
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
        protected TextView viewNomeCidade;
        protected ImageButton delete;
        public MyViewHolder(final View itemView){
            super(itemView);
            viewNomeCidade = (TextView) itemView.findViewById(R.id.project_name);
            delete = (ImageButton) itemView.findViewById(R.id.delete_project_button);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    clickRecycler.onCustomClick(projectsList.get(getLayoutPosition()));
                }
            });
        }
    }
}
