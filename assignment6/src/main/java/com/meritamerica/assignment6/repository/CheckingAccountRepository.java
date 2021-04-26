package com.meritamerica.assignment6.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.meritamerica.assignment6.models.CheckingAccount;

public interface CheckingAccountRepository extends JpaRepository<CheckingAccount,Integer>{

}
