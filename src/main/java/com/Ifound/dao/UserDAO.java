package com.Ifound.dao;

import com.Ifound.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDAO extends CrudRepository<User, Long> {

    @Query(nativeQuery = true , value = "select u.* from document " +
            "left join users u on (d.owner_document_id = u.id )" +
            " where d.number_of_registry=:id ")
    User findByOwnerDocumentNumber(@Param("id")String id);

    User findByEmailOrNumberOfPrincipalDocument(String email , String numberOfPrincipalDocument);

Optional<User> findByUsername(String str);

    User findFirstByUsername(String s);

    boolean existsByUsername(String login);


    Optional<User> findByNumberOfPrincipalDocument(String document);
}
