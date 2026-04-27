package dolpi.CivicInsight.Repository;

import dolpi.CivicInsight.Controller.Complaint;
import dolpi.CivicInsight.Entity.Admin;
import dolpi.CivicInsight.Entity.Complaints;
import dolpi.CivicInsight.Entity.OfficerEnty;
import dolpi.CivicInsight.Entity.UserEnity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepo extends MongoRepository<Complaints,String> {

    @Query("{ 'officer_id' : ?0 }")
    List<Complaints> findByOfficerId(String officerId);

    List<Complaints> findByUserId(String userId);

    List<Complaints>
    findByStatusAndUrgencyAndCreatedAtBeforeAndNotifiedFalse(
            String status,
            String urgency,
            LocalDateTime time
    );

    List<Complaints> findByAdminIdAndStatusAndNotified(String adminId, String status, boolean notified);

    List<Complaints> findByCity(String city);
}
