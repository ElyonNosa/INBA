package comp3350.inba.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.inba.tests.objects.CourseTest;
import comp3350.inba.tests.objects.SCTest;
import comp3350.inba.tests.objects.StudentTest;
import comp3350.inba.tests.business.CalculateGPATest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StudentTest.class,
        CourseTest.class,
        SCTest.class,
        CalculateGPATest.class
})
public class AllTests
{

}
