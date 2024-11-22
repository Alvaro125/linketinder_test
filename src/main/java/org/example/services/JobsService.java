package org.example.services;

import org.example.entity.JobEntity;

import java.util.List;

public interface JobsService {
    public abstract List<JobEntity> listAll();

    public abstract List<JobEntity> listByPerson(Integer idPerson);

    public abstract JobEntity oneById(Integer id);

    public abstract void updateById(JobEntity job);

    public abstract JobEntity addJob(JobEntity job);

    public abstract void deleteById(JobEntity job);
}
