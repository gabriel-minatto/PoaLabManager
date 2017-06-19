package com.example.root.poalabmanager.controllers;

import android.content.Context;

import com.example.root.poalabmanager.dao.ProjectsDao;
import com.example.root.poalabmanager.models.Projects;

import java.util.List;

/**
 * Created by minatto on 18/06/17.
 */

public class ProjectsController{

    private static ProjectsDao projectDao;
    private static ProjectsController instance;

    public static ProjectsController getInstance(Context context) throws Exception {
        if (instance == null) {
            instance = new ProjectsController();
            projectDao = new ProjectsDao(context);
        }
        return instance;
    }

    public boolean insert(Projects project) throws Exception {
        if (project == null || project.getName().isEmpty() || project.getUser() == 0) {
            return false;
        }
        projectDao.insert(project);
        return true;
    }

    public void update(Projects project) throws Exception {
        projectDao.update(project);
    }

    public List<Projects> findAll() throws Exception {
        return projectDao.findAll();
    }

    public List<Projects> findByUser(int user) throws Exception {
        return projectDao.findByUser(user);
    }

    public void deleteById(int id) throws Exception{
        projectDao.deleteById(id);
    }

    public void deleteAll() throws Exception {
        projectDao.deleteAll();
    }

}
