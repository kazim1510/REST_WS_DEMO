package com.webapp.app.ws.io.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;

import com.webapp.app.ws.io.dao.DAO;
import com.webapp.app.ws.io.entity.UserEntity;
import com.webapp.app.ws.shared.dto.UserDTO;
import com.webapp.app.ws.utils.HibernateUtils;

public class MYSQLDAO implements DAO {
    Session session;

    @Override
    public void openConnection() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        session = sessionFactory.openSession();
    }

    @Override
    public void closeConnection() {
        if (session != null) {
            session.close();
        }
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        UserDTO userDTO = null;

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<UserEntity> criteriaQuery = cb.createQuery(UserEntity.class);

        Root<UserEntity> profileRoot = criteriaQuery.from(UserEntity.class);
        criteriaQuery.select(profileRoot);
        criteriaQuery.where(cb.equal(profileRoot.get("email"), userName));

        Query<UserEntity> query = session.createQuery(criteriaQuery);

        List<UserEntity> resultList = query.getResultList();

        if (resultList != null && !resultList.isEmpty()) {
            UserEntity userEntity = resultList.get(0);
            userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDTO);
        }

        return userDTO;

    }

    @Override
    public UserDTO getUser(String userId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<UserEntity> criteriaQuery = cb.createQuery(UserEntity.class);

        Root<UserEntity> profileRoot = criteriaQuery.from(UserEntity.class);
        criteriaQuery.select(profileRoot);
        criteriaQuery.where(cb.equal(profileRoot.get("userId"), userId));

        UserEntity userEntity = session.createQuery(criteriaQuery).getSingleResult();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userEntity, userDTO);

        return userDTO;
    }

    @Override
    public UserDTO saveUser(UserDTO user) {
        UserDTO returnValue = null;
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        session.beginTransaction();
        session.save(userEntity);
        session.getTransaction().commit();

        returnValue = new UserDTO();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();

        BeanUtils.copyProperties(userDTO, userEntity);
        session.beginTransaction();
        session.update(userEntity);
        session.getTransaction().commit();

    }


}
