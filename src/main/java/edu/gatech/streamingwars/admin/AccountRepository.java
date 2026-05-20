package edu.gatech.streamingwars.account;

import edu.gatech.streamingwars.admin.DemographicGroup;
import edu.gatech.streamingwars.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountUser, Long> {

    AccountUser findAccountUserByUser(AppUser appUser);

    long countAccountUsersByDemographicGroup(DemographicGroup demographicGroup);
}
