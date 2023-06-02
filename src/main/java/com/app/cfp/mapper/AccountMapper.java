package com.app.cfp.mapper;

import com.app.cfp.dto.AccountDTO;
import com.app.cfp.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper extends DataMapper<Account, AccountDTO> {

    Account toDomain(AccountDTO accountDTO);

    AccountDTO toDto(Account account);
}
