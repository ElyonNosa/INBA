package comp3350.inba.persistence;

import java.util.List;

import comp3350.inba.objects.Course;

public interface CoursePersistence {
    List<Course> getCourseSequential();

    List<Course> getCourseRandom(Course currentCourse);

    Course insertCourse(Course currentCourse);

    Course updateCourse(Course currentCourse);

    void deleteCourse(Course currentCourse);
}
