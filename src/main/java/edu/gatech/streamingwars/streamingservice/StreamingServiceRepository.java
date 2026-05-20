package edu.gatech.streamingwars.streamingservice;

import edu.gatech.streamingwars.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamingServiceRepository extends JpaRepository<StreamingService, Long> {

    StreamingService findStreamingServiceByAppUser(AppUser appuser);
}
