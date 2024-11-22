package org.example.services.impl;

import org.example.dao.AddressDao;
import org.example.dao.JobDao;
import org.example.dao.LegalPersonDao;
import org.example.entity.JobEntity;
import org.example.entity.LegalPersonEntity;
import org.example.services.JobsService;
import org.example.utils.ValidatorUtil;

import java.util.List;
import java.util.stream.Collectors;

public class JobsServiceImpl implements JobsService {
    private final JobDao jobDao;
    private final LegalPersonDao legalPersonDao;
    private final AddressDao addressDao;

    public JobsServiceImpl(JobDao jobDao, LegalPersonDao legalPersonDao, AddressDao addressDao) {
        this.jobDao = jobDao;
        this.legalPersonDao = legalPersonDao;
        this.addressDao = addressDao;
    }

    @Override
    public List<JobEntity> listAll() {
        return jobDao.getAll().stream()
                .map(job -> {
                    // Assuming job is a JobEntity and has a person property
                    LegalPersonEntity legalPerson = legalPersonDao.getById(job.getPerson().getId());
                    job.setPerson(legalPerson);
                    return job;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<JobEntity> listByPerson(Integer idPerson) {
        return jobDao.getByIdPerson(idPerson);
    }

    @Override
    public JobEntity oneById(Integer id) {
        return jobDao.getById(id);
    }

    @Override
    public void updateById(JobEntity job) {
        jobDao.updateById(job);
        addressDao.updateById(job.getLocal());
    }

    @Override
    public JobEntity addJob(JobEntity job) {
        ValidatorUtil.validate(job);
        job.getLocal().setId(addressDao.create(job.getLocal()).getId());
        jobDao.create(job);
        return job;
    }

    @Override
    public void deleteById(JobEntity job) {
        jobDao.deleteById(job.getId());
        addressDao.deleteById(job.getLocal().getId());
    }
}