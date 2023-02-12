package comp3350.inba.persistence;

import java.util.List;

import comp3350.inba.objects.SC;

public interface SCPersistence {
    List<SC> getSC(final String studentID);

    List<SC> getCS(final String courseID);
}
