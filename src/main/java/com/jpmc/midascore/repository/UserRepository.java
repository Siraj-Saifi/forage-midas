//package com.jpmc.midascore.repository;
//
//import com.jpmc.midascore.entity.UserRecord;
//import org.springframework.data.repository.CrudRepository;
//
//public interface UserRepository extends CrudRepository<UserRecord, Long> {
//    UserRecord findById(long id);
//    UserRecord findByName(String name);
//}
package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserRecord, Long> {
    Optional<UserRecord> findById(long id);
    UserRecord findByName(String name);
}