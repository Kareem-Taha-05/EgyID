import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ID_generator
{
    
    private String name;
    private String gender; 
    private String religion; 
    private String governorate; 
    private String birthdate;
    private String occupation;    
    private String issueDate;
    private String expirationDate;    
    private String uniqueID;
    private String picturePath;

    //enum instructor to assign each of the governorates to its code
    private enum Governorate {
    	CAIRO(01),ALEXANDRIA(02),PORTSAID(03),SUEZ(04),DAMIETTA(11),DAKAHLIA(12),SHARKIA(13),QALUBIA(14),
    	KAFRELSHEIKH(15),GHARBIA(16),MONUFIA(17),BEHEIRA(18),ISMAILIA(19),GIZA(21),BENISUEF(22),FAYOUM(23),
    	MINYA(24),ASSIUT(25),SOHAG(26),QENA(27),ASWAN(28),LUXOR(29),REDSEA(31),NEWVALLEY(32),MATROUH(33),
    	NORTHSINAI(34),SOUTHSINAI(35),FOREIGNNATIONALS(88);
    	
    	final int governorateCode; //final because governorateCode can not be changed    	
    	Governorate(int governorateCode){
    		this.governorateCode = governorateCode;};
    	public int getGovernorateCode() {
    	    return governorateCode;
    	        }
    }    
    
    //constructor 
    public ID_generator(String name, String gender, String religion, String governorate, String birthdate, String occupation, String picturePath) {
        this.name = validateName(name.trim());
        this.gender = validateGender(gender.trim());
        this.religion = validateReligion(religion.trim());
        this.governorate = validateGovernorate(governorate.trim());
        this.birthdate = validateBirthdate(birthdate.trim());
        this.occupation = title(occupation.trim());
        
        //getting the current time and adding 7 years to get the expiration time
        LocalDate currentdate= LocalDate.now();
		LocalDate expirationDate = currentdate.plusYears(7); 
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //set the proper date format
		this.issueDate = currentdate.format(format);
    	this.expirationDate= expirationDate.format(format);
    	
        this.uniqueID = generateUniqueID();
        this.picturePath = picturePath;
    }
    
    // used to check if the name has a number in it or not
    private boolean containsNumber(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
    
    // generates a unique ID based on the real Egyptian ID standards and making sure it does not repeat 
    private String generateUniqueID() {
    	
        // Split the birthdate (DD/MM/YYYY)
        String[] parts = birthdate.split("/");

        // Extract year, month, and day
        int year = Integer.parseInt(parts[2]);
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        // Get the century (20th or 21st century) [first number in the id]
        int century = year / 100;   // Integer division to get the century part
        century = (century == 19) ? 2 : 3;  // If century is 19, set to 2; if century is 20, set to 3

        // Last two digits of the year [second and third number in the id]
        String last2ofYear = Integer.toString(year).substring(2);

        // Get the governorate code from the enum [8th and 9th number in the id]
        Governorate gov = Governorate.valueOf(governorate.toUpperCase());
        int governorateCode = gov.getGovernorateCode();

        // Generate a random 4-digit serial number [last four digits in the id before the validation number]
        Random random = new Random();
        int randomID = random.nextInt(9999 - 1000) + 1000; // Ensures a number between 1000 and 9999

        // Format the day and month to always have two digits (e.g., "01", "09")  [ 4th to 7th number in the id]
        String formattedDay = String.format("%02d", day);
        String formattedMonth = String.format("%02d", month);

        // making the unique ID without the validation number
        String generatedID = String.format("%d%s%s%s%02d%d", century, last2ofYear, formattedMonth, formattedDay, governorateCode, randomID);

        //last is the validation number calculated from the rest of the ID
        String code = generatedID;
        int sum = 0;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            int n = Character.getNumericValue(c);
            n *= 2;
            if (n < 10)
                sum += n;
            else
                sum += (n / 10) + (n % 10);
        }
        // Add the check digit if the sum is not divisible by 10
        if (sum % 10 != 0) {
            int checkDigit = 10 - (sum % 10);
            generatedID = String.format("%d%s%s%s%02d%d%d", century, last2ofYear, formattedMonth, formattedDay, governorateCode, randomID, checkDigit);
        } else {
            // If the sum is already divisible by 10, we don't need to add a check digit
            generatedID = String.format("%d%s%s%s%02d%d0", century, last2ofYear, formattedMonth, formattedDay, governorateCode, randomID);
        }

        // Check if the generated ID already exists, and remaking if necessary
        while (IDs_File.checkForID(generatedID)) {
            randomID = random.nextInt(9000) + 1000;  // Generate a new random number if the id is found

            // Recreate the ID with the new random number
            generatedID = String.format("%d%s%s%s%02d%d", century, last2ofYear, formattedMonth, formattedDay, governorateCode, randomID);

            // Recalculate the check digit for the new generated ID
            sum = 0;
            for (int i = 0; i < generatedID.length(); i++) {
                char c = generatedID.charAt(i);
                int n = Character.getNumericValue(c);
                n *= 2;
                if (n < 10)
                    sum += n;
                else
                    sum += (n / 10) + (n % 10);
            }
            // Add the check digit if the sum is not divisible by 10
            if (sum % 10 != 0) {
                int checkDigit = 10 - (sum % 10);
                generatedID = String.format("%d%s%s%s%02d%d%d", century, last2ofYear, formattedMonth, formattedDay, governorateCode, randomID, checkDigit);
            } else {
                generatedID = String.format("%d%s%s%s%02d%d0", century, last2ofYear, formattedMonth, formattedDay, governorateCode, randomID);
            }
        }

        return generatedID;
    }




    //making sure the name has no numbers using the containsNumber method that we created above
    private String validateName(String name) {
    	boolean hasNumber = containsNumber(name);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        else if (hasNumber) {
            throw new IllegalArgumentException("Name cannot contain a number.");
        }
        return name;
    }

    //making sure the gender is either male or female
    private String validateGender(String gender) {
        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
            throw new IllegalArgumentException("Invalid gender. Must be 'Male' or 'Female'.");
        }
        return gender;
    }

    //making sure the religion is either muslim or christian
    private String validateReligion(String religion) {
        if (!religion.equalsIgnoreCase("Muslim") && !religion.equalsIgnoreCase("Christian")) {
            throw new IllegalArgumentException("Invalid religion. Must be 'Muslim' or 'Christian'.");
        }
        return religion;
    }
    
    //making sure the governorate is not empty
    private String validateGovernorate(String governorate) {
        if (governorate == null || governorate.isEmpty()) {
            throw new IllegalArgumentException("Governorate cannot be empty.");
        }
        return governorate;
    }
    
    //making sure the birthdate is real and valid 
    private String validateBirthdate(String birthdate) {
        if (birthdate == null || !birthdate.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new IllegalArgumentException("Birthdate must be in the format DD/MM/YYYY.");
        }
        String[] parts = birthdate.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 01 and 12.");
        }
        
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 01 and 31.");
        }
        if (year < 1900 || year > 2100 ) {
        	throw new IllegalArgumentException("year must be between 1900 and 2100.");
        }
        
        return birthdate;
    }
    
    //SETTERS
    public void setName(String name) {
        this.name = validateName(name);
    }
    public void setGender(String gender) {
        this.gender = validateGender(gender);
    }
    public void setReligion(String religion) {
        this.religion = validateReligion(religion);
    }
    public void setOccupation(String occupation) {
        this.occupation = title(occupation);
    }
    public void setGovernorate(String governorate) {
        this.governorate = validateGovernorate(governorate);
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = validateBirthdate(birthdate);
    }
    
    //GETTERS
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getReligion() { return religion; }
    public String getGovernorate() { return governorate; }
    public String getBirthdate() { return birthdate; }
    public String getOccupation() { return occupation; }
    public String getIssueDate() { return issueDate; }
    public String getUniqueID() { return uniqueID; }  
    public String getPicturePath() { return picturePath; }

    //used to make the first letter capital and the rest small to look better to the user
    public String title(String str)
    {
    	return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
        
    @Override
    public String toString() {
        return "name='" + title(name) + '\'' + "\n" +
                "gender='" + title(gender) + '\'' + "\n" +
                "religion='" + title(religion) + '\'' + "\n" +
                "governorate='" + title(governorate) + '\'' + "\n" +
                "occupation='" + title(occupation) + '\'' + "\n" +
                "birthdate='" + birthdate + '\'' + "\n" +
                "issueDate='" + issueDate + '\'' + "\n" +
                "expirationDate='" + expirationDate + '\'' + "\n" +
                "uniqueID='" + uniqueID + '\'' ;
    }
}
