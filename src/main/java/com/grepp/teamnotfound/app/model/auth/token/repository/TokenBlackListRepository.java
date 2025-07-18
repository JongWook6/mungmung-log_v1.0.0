package com.grepp.teamnotfound.app.model.auth.token.repository;

import com.grepp.teamnotfound.app.model.auth.token.entity.TokenBlackList;
import org.springframework.data.repository.CrudRepository;

public interface TokenBlackListRepository extends CrudRepository<TokenBlackList, String> {
}
