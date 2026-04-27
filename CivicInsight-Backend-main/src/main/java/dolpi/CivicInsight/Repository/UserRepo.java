package dolpi.CivicInsight.Repository;

import dolpi.CivicInsight.Entity.UserEnity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<UserEnity,String> {
    UserEnity findByUsername(String username);

   // boolean findByUsername(String username);

    boolean existsByEmail(String email);
}
