//package com.limbae.pfy.repository;
//
//import com.limbae.pfy.domain.UserVO;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//import java.util.List;
//import java.util.Optional;
//
//public class JpaUserRepository implements UserRepository {
//
//    EntityManager em;
//
//    public JpaUserRepository(EntityManager em){
//        this.em = em;
//    }
//    // ----------
//
//    @Override
//    public UserVO save(UserVO vo) {
//        EntityTransaction ts = em.getTransaction();
//
//        ts.begin();
//        em.persist(vo);
//        ts.commit();
//
//        return vo;
//    }
//
//    @Override
//    public Optional<UserVO> findByUid(int uid) {
//        UserVO vo = em.find(UserVO.class, uid);
//        return Optional.ofNullable(vo);
//    }
//
//    @Override
//    public List<UserVO> findByEmail(String username) {
//        List<UserVO> list =
//                em.createQuery("select u from user u where u.username =" + username, UserVO.class).getResultList();
//        return list;
//
//    }
//
//    @Override
//    public Optional<UserVO> findOneWithAuthoritiesByUsername(String username) {
//        List<UserVO> list = em.createQuery("select u from user u where u.username =" + username, UserVO.class).getResultList();
//        return list.stream().findAny();
//    }
//}
