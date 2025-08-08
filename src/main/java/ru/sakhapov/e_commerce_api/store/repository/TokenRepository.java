package ru.sakhapov.e_commerce_api.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sakhapov.e_commerce_api.store.security.jwt.Token;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
      select t from Token t join t.user u
      where u.id = :id and t.expired = false and t.revoked = false
    """)

    List<Token> findAllValidTokenByUser(Long id);

    Token findByToken(String token);

}
