
import java.util.*;
import java.io.*;

public class edu {

    static Random generator = new Random();

    static ArrayList<String> departments = new ArrayList<String>(Arrays.asList("CSE","MTH","CHM","BIO","HUM","ECE","SWE","PSY"));

    static int[] roomCounts;

    static ArrayList<Integer> cseInstructors = new ArrayList<Integer>();
    static ArrayList<Integer> mthInstructors = new ArrayList<Integer>();
    static ArrayList<Integer> chmInstructors = new ArrayList<Integer>();
    static ArrayList<Integer> bioInstructors = new ArrayList<Integer>();
    static ArrayList<Integer> humInstructors = new ArrayList<Integer>();
    static ArrayList<Integer> eceInstructors = new ArrayList<Integer>();
    static ArrayList<Integer> sweInstructors = new ArrayList<Integer>();
    static ArrayList<Integer> psyInstructors = new ArrayList<Integer>();

    public static int randInt(int n) {
        return generator.nextInt(n) + 1;
    }

    public static String randDept() {
       return departments.get(generator.nextInt(8));
    }

    public static int deptNumber(String dept) {
        return departments.indexOf(dept);
    }
    
    public static int randNumDiff(int n, int d) {
       int v;
       while (true) {
          v = randInt(n);
          if (v != d)
             return v;
       }
    }

    public static String randName() {
        String S = "";
        int len = randInt(32);

        for (int i=1; i<=len; i++)
           S = S + (char)('A' + (generator.nextInt(26)));

        return S;
    }

    public static int randAdvisor(int deptNum) {
       switch (deptNum) {
           case 0 : return cseInstructors.get(generator.nextInt(cseInstructors.size()));
           case 1 : return mthInstructors.get(generator.nextInt(mthInstructors.size()));
           case 2 : return chmInstructors.get(generator.nextInt(chmInstructors.size()));
           case 3 : return bioInstructors.get(generator.nextInt(bioInstructors.size()));
           case 4 : return humInstructors.get(generator.nextInt(humInstructors.size()));
           case 5 : return eceInstructors.get(generator.nextInt(eceInstructors.size()));
           case 6 : return sweInstructors.get(generator.nextInt(sweInstructors.size()));
           case 7 : return psyInstructors.get(generator.nextInt(psyInstructors.size()));
           default : System.out.println("Error  in randAdvisor"); return -1;
       }
    }

    public static void generateDepartmentRecords() throws Exception {
        PrintWriter  deptWriter = new PrintWriter("department.csv");
        for (int i=0; i<=departments.size()-1; i++) {
	    deptWriter.println(departments.get(i) + "," + randInt(20) + "," + 
			       (499999+randInt(1500000)));
	}
        deptWriter.close();
    }

    public static void generateClassroomRecords(int buildingCount) throws Exception {
        PrintWriter  classroomWriter = new PrintWriter("classroom.csv");
     	int capacity;

        roomCounts = new int[buildingCount];
        for (int i=1; i<=buildingCount; i++) {
           roomCounts[i-1] = randInt(30);        // On average, 15 classrooms per building
           for (int j=1; j<=roomCounts[i-1]; j++) {
              capacity = randInt(186) + 14;  // Classroom size is between 15 and 200
              classroomWriter.println(i + "," + j + "," + capacity);
           }
        }
        classroomWriter.close();
    }

    public static void generateInstructorRecords(int instructorCount) throws Exception {
        PrintWriter  instructorWriter = new PrintWriter("instructor.csv");
        String instructorDept;
        for (int i=1; i<=instructorCount; i++) {
            instructorDept = randDept();
            instructorWriter.println(i + "," + randName() + ","
                   + instructorDept + "," + (generator.nextInt(120000)+30000));

            switch (deptNumber(instructorDept)) {
               case 0 : cseInstructors.add(i); break;
               case 1 : mthInstructors.add(i); break;
               case 2 : chmInstructors.add(i); break;
               case 3 : bioInstructors.add(i); break;
               case 4 : humInstructors.add(i); break;
               case 5 : eceInstructors.add(i); break;
               case 6 : sweInstructors.add(i); break;
               case 7 : psyInstructors.add(i); break;
               default : System.out.println("Error in generateInstructorRecords");
            }
        }
        instructorWriter.close();
    }

    public static void generateStudentAndAdvisorRecords(int studentCount) throws Exception {
        PrintWriter  studentWriter = new PrintWriter("student.csv");
        PrintWriter  advisorWriter = new PrintWriter("advisor.csv");
        for (int i=1; i<=studentCount; i++) {
            String studentDept = randDept();
            studentWriter.println(i + "," + randName() + ","
                   + studentDept + "," + generator.nextInt(131));
            
            if (randInt(10) <= 9) {
	        int studentDeptNum = deptNumber(studentDept);
                int advisor = randAdvisor(studentDeptNum);
                advisorWriter.println(i + "," + advisor);
            }
        }
        studentWriter.close();
        advisorWriter.close();
    }

    public static void generateCourseRecords(int courseCount) throws Exception {
        PrintWriter  courseWriter = new PrintWriter("course.csv");
        for (int i=1; i<=courseCount; i++) {
	         courseWriter.println(i + "," + randName() + ","
			                          + randDept() + "," +  randInt(4)); 
        }
        courseWriter.close();
    }

    public static void generatePrerequisiteRecords(int prereqCount, int courseCount) throws Exception {
        PrintWriter  prereqWriter = new PrintWriter("prerequisite.csv");
        for (int i=1; i<=prereqCount; i++) {
            int cid = randInt(courseCount);
            int pid = randNumDiff(courseCount,cid);
            prereqWriter.println(cid + "," + pid);
        }
        prereqWriter.close();
    }

    public static void generateCourseSectionRecords(int coursesPerSemester, int courseCount, int instructorCount, int studentCount, int buildingCount) throws Exception {
        PrintWriter  sectionWriter = new PrintWriter("section.csv");
        PrintWriter  teachesWriter = new PrintWriter("teach.csv");
        PrintWriter  takesWriter = new PrintWriter("take.csv");
	int pseudokey = 1;
        int courseProb = (int)((double)coursesPerSemester/courseCount*10);           // Probability an arbitrary course is taught
        for (int year=2000; year<=2015; year++) {
            for (int semester=0; semester<=1; semester++) {
                for (int courseID=1; courseID<=courseCount; courseID++) {
                    if (randInt(10) <= courseProb) {
                       int sectionCnt = randInt(5);             // On average 3 sections or a course being taught
                       for (int sectionID=1; sectionID<=sectionCnt; sectionID++) {
                           int building = randInt(buildingCount);
                           int classroom = randInt(roomCounts[building-1]);
                           int timeslot = randInt(17);

                           sectionWriter.println(courseID + "," + sectionID + "," + (semester==0 ? "FALL" : "SPRING") + "," + year + "," + building + "," + classroom + "," + timeslot);

                           // Create a teacher record for this section
                           // Need to add building and classroom!!!!!!!!!
                           int tic = randInt(instructorCount);
                           teachesWriter.println(tic + "," + courseID + "," + sectionID + "," + (semester==0 ? "FALL" : "SPRING") + "," + year);

                           // Create enrollment records for this section
                           int enrollments = randInt(186) + 14;  // Classroom size is between 15 and 200
                           for (int k=1; k<=enrollments; k++) {
                               int sid = randInt(studentCount);
                               char grade = (char)('A' + generator.nextInt(5));
                               takesWriter.println(pseudokey + "," + sid + "," + courseID + "," + sectionID + "," + (semester==0 ? "FALL" : "SPRING") + "," + year + "," + grade);
			       pseudokey++;
                           }
                       }
                    }
		}
	    }
	}
        sectionWriter.close();
        teachesWriter.close();
        takesWriter.close();
    }

    public static void generateTimeslotRecords () throws Exception {
        PrintWriter  timeslotWriter = new PrintWriter("timeslot.csv");
        timeslotWriter.println(1 + ",MWF," + "8," + "9");
        timeslotWriter.println(2 + ",MWF," + "9," + "10");
        timeslotWriter.println(3 + ",MWF," + "10," + "11");
        timeslotWriter.println(4 + ",MWF," + "11," + "12");
        timeslotWriter.println(5 + ",MWF," + "12," + "1");
        timeslotWriter.println(6 + ",MWF," + "1," + "2");
        timeslotWriter.println(7 + ",MWF," + "2," + "3");
        timeslotWriter.println(8 + ",MWF," + "3," + "4");
        timeslotWriter.println(9 + ",MWF," + "4," + "5");
        timeslotWriter.println(10 + ",MWF," + "5," + "6");
        timeslotWriter.println(11 + ",TR," + "8," + "9:15");
        timeslotWriter.println(12 + ",TR," + "9:30," + "10:45");
        timeslotWriter.println(13 + ",TR," + "11," + "12:15");
        timeslotWriter.println(14 + ",TR," + "12:30," + "1:45");
        timeslotWriter.println(15 + ",TR," + "2," + "3:15");
        timeslotWriter.println(16 + ",TR," + "3:30," + "4:45");
        timeslotWriter.println(17 + ",TR," + "5," + "6:15");
        timeslotWriter.close();
    }


    public static void main(String[] args) throws Exception {

        // Get the scale factor from the user
        Scanner kb = new Scanner(System.in);
        System.out.print("Enter SF:");
        int SF = kb.nextInt();
        int buildingCount = SF;
        int classroomCount = SF*15;
        int instructorCount = classroomCount*17;      // 1 instructor for every classroom, building, and timeslot
        int studentCount = instructorCount*20*12;      // 20/1 student/instructor ratio, but this is for over 16 years
        int courseCount = instructorCount*5;          // 5 courses per instructor
        int prereqCount = (int)(courseCount*0.9);     // # of prerequisites is 90% of the total # of courses
        int coursesPerSemester = (int)(instructorCount * 2.5);   // 2.5 courses per semester per instructor

        generateDepartmentRecords();
        generateClassroomRecords(buildingCount);
        generateInstructorRecords(instructorCount);
        generateStudentAndAdvisorRecords(studentCount);
        generateCourseRecords(courseCount);
        generatePrerequisiteRecords(prereqCount,courseCount);
        generateCourseSectionRecords(coursesPerSemester,courseCount,instructorCount,studentCount,buildingCount);
        generateTimeslotRecords();
    }
}
