package dolpi.CivicInsight.Repository;

import dolpi.CivicInsight.Entity.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends MongoRepository<Admin,String> {
    Admin findByCity(String city);

    Admin findByUsername(String username);

    boolean existsByEmail(String email);
}
