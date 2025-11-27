package com.mitica.fullclean;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoiaRepository extends JpaRepository<Joia, Joia.JoiaId> {
}
