package com.education.project.media.holder.mediaholder.repository;

import com.education.project.media.holder.mediaholder.dto.response.MediaInfoResponse;
import com.education.project.media.holder.mediaholder.mapper.MediaMapper;
import com.education.project.media.holder.mediaholder.model.DataPage;
import com.education.project.media.holder.mediaholder.model.Media;
import com.education.project.media.holder.mediaholder.model.MediaSearchCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Repository
public class MediaCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    @Autowired
    private MediaMapper mediaMapper;

    public MediaCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();

    }

    public Page<MediaInfoResponse> findAllWithFilters(
            DataPage page, MediaSearchCriteria searchCriteria){

        CriteriaQuery<Media> criteriaQuery =
                criteriaBuilder.createQuery(Media.class);
        Root<Media> root = criteriaQuery.from(Media.class);
        Predicate predicate = getPredicate(searchCriteria, root);

        criteriaQuery.where(predicate);
        setOrder(page, criteriaQuery, root);

        TypedQuery<Media> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page.getPageNumber() * page.getPageSize());
        typedQuery.setMaxResults(page.getPageSize());

        Pageable pageable = getPageable(page);

        long mediasCount = getCount();

        return new PageImpl<>(
                mediaMapper.toDtoInfo(typedQuery.getResultList()),
                pageable,
                mediasCount);
    }



    private Pageable getPageable(DataPage page){
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        return PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
    }

    private void setOrder(
            DataPage page, CriteriaQuery<Media> criteria, Root<Media> root){

        if (page.getSortDirection().equals(Sort.Direction.ASC)){
            criteria.orderBy(criteriaBuilder.asc(root.get(page.getSortBy())));
        }
    }


    private long getCount(){
        CriteriaQuery<Long> countQuery =
                criteriaBuilder.createQuery(Long.class);
        Root<Media> countRoot = countQuery.from(Media.class);
        countQuery.select(criteriaBuilder.count(countRoot));
        return entityManager.createQuery(countQuery).getResultList().get(0);
    }


    private Predicate getPredicate(MediaSearchCriteria searchCriteria,
                                   Root<Media> root){
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(searchCriteria.getName())){
            predicates.add(
                    criteriaBuilder.like(root.get("name"),
                            "%" + searchCriteria.getName() + "%")
            );
        }
        if (Objects.nonNull(searchCriteria.getDescription())){
            predicates.add(
                    criteriaBuilder.like(root.get("description"),
                            "%" + searchCriteria.getDescription() + "%")
            );
        }
        if (searchCriteria.getType() > 0){
            predicates.add(
                    criteriaBuilder.equal(root.get("type"),
                            searchCriteria.getType())
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
