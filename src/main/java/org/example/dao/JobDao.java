package org.example.dao;

import org.example.entity.JobEntity;

import java.util.List;

public interface JobDao {
    public abstract List<JobEntity> getAll();

    public abstract List<JobEntity> getByIdPerson(Integer idPerson);

    public abstract JobEntity getById(Integer id);

    public abstract void updateById(JobEntity job);

    public abstract JobEntity create(JobEntity job);

    public abstract void deleteById(Integer id);
}
