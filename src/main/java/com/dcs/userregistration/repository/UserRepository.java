/**
 * 
 */
package com.dcs.userregistration.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dcs.userregistration.model.User;

/**
 * @author Debashis
 *
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

	/**
	 * @param emailId
	 * @return
	 */
	User findByEmailId(String emailId);
}
