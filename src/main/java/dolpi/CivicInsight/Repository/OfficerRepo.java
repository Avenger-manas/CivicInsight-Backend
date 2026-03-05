package dolpi.CivicInsight.Repository;

import dolpi.CivicInsight.Entity.OfficerEnty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficerRepo extends MongoRepository<OfficerEnty,String> {
    //boolean findByUsername(String username);

    OfficerEnty findByUsername(String username);

    List<OfficerEnty> findByCityAndDepartment(String city, String department);

    List<OfficerEnty> findByCity(String city);

    boolean existsByEmail(String email);
}
