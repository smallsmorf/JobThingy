import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;
import java.io.*;

public class Job {
    // File path to the TXT file containing the job information
    private static final String FILE_PATH = "jobs.txt";

    public static void main(String[] args) {
        // Example usage
        // addJob("12345MMT_", "2023-02-05", "Melbourne, NSW, Australia",
        //         "Senior", "700000", "Software architecture, SQL, python", "Full-time");
       updateJob("12345MMT_", "2023-06-01", "Melbourne, Victoria, Australia",
              "Senior", "700000", "Software architecture, C#, ptyon", "Full-time");
    }

    public static boolean addJob(String jobId, String datePosted, String address, String level,
                                 String salary, String skills, String jobType) {
        // Check if the job ID already exists
        if (!isValidJobId(jobId)) {
            return false;
        }
        // Check if the date is valid
        if (!isValidDate(datePosted)) {
            return false;
        }
        // Check if the address is valid
        if (!isValidAddress(address)) {
            return false;
        }
        // Check if the salary is valid
        if (!isValidSalary(level, salary)) {
            return false;
        }
        // Check if the skills are valid
        if (!isValidSkills(skills)) {
            return false;
        }
        // Check if the job type is valid
        if (!isValidJobType(level, jobType)) {
            return false;
        }
        // Construct the job information string
        String jobInfo = jobId + ", " + datePosted + ", " + address + ", " + level + ", " + salary + ", " + skills + ", " + jobType;
        // All conditions met, update job information in the TXT file
        try {
            //open file in append mode
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            //write the job information to the file
            writer.write(jobInfo);
            //write a new line after each entry
            writer.newLine();
            //close the file
            writer.close();
            return true;
        } catch (IOException e) {
            //print the error message
            System.out.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateJob(String jobId, String datePosted, String address, String level,
                                    String salary, String skills, String jobType) {
        //same validations as addJob
        if (!isValidJobId(jobId)) {
            return false;
        }

        if (!isValidDate(datePosted)) {
            return false;
        }

        if (!isValidAddress(address)) {
            return false;
        }

        if (!isValidSalary(level, salary)) {
            return false;
        }

        if (!isValidSkills(skills)) {
            return false;
        }

        if (!isValidJobType(level, jobType)) {
            return false;
        }

        String updatedJobInfo = jobId + ", " + datePosted + ", " + address + ", " + level + ", " + salary + ", " + skills + ", " + jobType;
        String tempFile = "temp.txt";

        try {
            // Open the original file in read mode
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            // Open the temporary file in write mode
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            // Read each line from the original file and write it to the temporary file
            String line;
            // Flag to check if the job ID was found in the file
            boolean jobUpdated = false;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(jobId)) {
                    // Update the job information
                    writer.write(updatedJobInfo);
                    writer.newLine();
                    jobUpdated = true;
                } else {
                    // Copy the existing job information to the temporary file
                    writer.write(line);
                    writer.newLine();
                }
            }

            reader.close();
            writer.close();

            if (!jobUpdated) {
                // The job ID was not found in the file
                System.out.println("Job not found: " + jobId);
                // Delete the temporary file
                File tempFileObj = new File(tempFile);
                tempFileObj.delete();
                return false;
            }

            // Delete the original file
            File originalFile = new File(FILE_PATH);
            originalFile.delete();

            // Rename the temporary file to the original file name
            File tempFileObj = new File(tempFile);
            tempFileObj.renameTo(originalFile);

            return true;
        } catch (IOException e) {
            System.out.println("Error updating job: " + e.getMessage());
            return false;
        }
    }

    //validate job ID format
    private static boolean isValidJobId(String jobId) {
        return Pattern.matches("[1-5]{5}[A-Z]{3}_", jobId);
    }
    //validate date format(YYYY-MM-DD)
    private static boolean isValidDate(String datePosted) {
        return Pattern.matches("\\d{4}-\\d{2}-\\d{2}", datePosted);
    }
    //validate address format(City, State, Country)
    private static boolean isValidAddress(String address) {
        return Pattern.matches("[A-Za-z]+, [A-Za-z]+, [A-Za-z]+", address);
    }
    //validate salary based on job level
    private static boolean isValidSalary(String level, String salary) {
        int parsedSalary;
        try {
            parsedSalary = Integer.parseInt(salary);
        } catch (NumberFormatException e) {
            //invalid salary format
            return false;
        }

        if (level.equals("Senior") || level.equals("Executive")) {
            //make sure that senior & executive jobs have a salary of at least $100,000
            return parsedSalary >= 100000;
        } else if (level.equals("Junior")) {
            //make sure that junior jobs have a salary between $40,000 and $70,000
            return parsedSalary >= 40000 && parsedSalary <= 70000;
        }
        //invalid job level
        return false;
    }
    //validate skills format
    private static boolean isValidSkills(String skills) {
        String[] skillList = skills.split(", ");
        if (skillList.length < 1 || skillList.length > 3) {
            //the job should have between 1 and 3 skills
            return false;
        }

        for (String skill : skillList) {
            if (skill.split(" ").length > 2) {
                //each skill should have at most 2 words
                return false;
            }
        }
        //skill format is valid
        return true;
    }
    private static boolean isValidJobType(String level, String jobType) {
        if (level.equals("Senior") || level.equals("Executive")) {
            // Senior & executive jobs cannot be part-time
            return !jobType.equals("Part-time");
        } else if (level.equals("Junior")) {
            // Junior jobs can have any job type
            return true; 
        }

        return false;
    }
}
