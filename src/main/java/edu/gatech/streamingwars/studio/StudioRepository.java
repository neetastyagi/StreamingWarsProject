package edu.gatech.streamingwars.studio;

import edu.gatech.streamingwars.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Long> {

    Studio findByAppUser(AppUser appuser);
}
