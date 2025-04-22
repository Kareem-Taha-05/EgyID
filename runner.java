import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import java.io.File;

public class runner {

    // Declare imagePath as a class field
    private String imagePath = "";
    // Class field to store the original ID (so that it stays constant when editing other data)
    private ID_generator originalID = null;

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new runner().createAndShowGUI());
    }
    
    // the actual UI Program
    private void createAndShowGUI() {

        JFrame frame = new JFrame("Egyptian ID Application"); //name of the program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closing the program when the window is closed.
        frame.setSize(1000, 600);                             //initial size of the program when it is opened
        frame.setLayout(new BorderLayout());                  //BorderLayout is an existing layout manager in Java Swing that  
                                                              //divides the UI into five regions: North, South, East, West, Center.

        //=========================================================================================================================================================//
                                                        //INPUT LAYOUT//      
        
        // creating the input layout which will be placed at the center of the whole UI layout
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10)); //the grid will have 7 rows,  2 columns, and a vertical and horizontal gap of 10 pixels
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //adds padding of 10 pixels in each direction
        
        //this creates the name label and field that will take the input of the name
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        inputPanel.add(nameLabel);     //adding the name label and name field to our layout to be able to extract
        inputPanel.add(nameField);     //that information later into variables to pass to the EgyptianID class

        //same as what we did with name but for the other fields
        JLabel genderLabel = new JLabel("Gender (Male/Female):");
        JTextField genderField = new JTextField();
        inputPanel.add(genderLabel);
        inputPanel.add(genderField);
        
        JLabel religionLabel = new JLabel("Religion (Muslim/Christian):");
        JTextField religionField = new JTextField();
        inputPanel.add(religionLabel);
        inputPanel.add(religionField);

        JLabel occupationLabel = new JLabel("Occupation (ex:Student, Engineer, etc.):");
        JTextField occupationField = new JTextField();
        inputPanel.add(occupationLabel);
        inputPanel.add(occupationField);

        JLabel birthdateLabel = new JLabel("Birthdate (DD/MM/YYYY):");
        JTextField birthdateField = new JTextField();
        inputPanel.add(birthdateLabel);
        inputPanel.add(birthdateField);
        
        //since the governorates are constant values, we will make it a combo box instead of a field of input
        JLabel governorateLabel = new JLabel("Governorate:");
        String[] governorates = {
        		"CAIRO", "ALEXANDRIA", "PORTSAID", "SUEZ", "DAMIETTA", 
        	    "DAKAHLIA", "SHARKIA", "QALUBIA", "KAFRELSHEIKH", 
        	    "GHARBIA", "MONUFIA", "BEHEIRA", "ISMAILIA", "GIZA", 
        	    "BENISUEF", "FAYOUM", "MINYA", "ASSIUT", "SOHAG", 
        	    "QENA", "ASWAN", "LUXOR", "REDSEA", "NEWVALLEY", 
        	    "MATROUH", "NORTHSINAI", "SOUTHSINAI", "FOREIGNNATIONALS"
        };
        JComboBox<String> governorateComboBox = new JComboBox<>(governorates); //creating the combo box with the values
        inputPanel.add(governorateLabel);
        inputPanel.add(governorateComboBox);  //adding the combo box to the layout        
        
        //now that the layout was completed we add it to the whole UI layout at the center so that we can add the output layout below it and the buttons above it
        frame.add(inputPanel, BorderLayout.CENTER);

        
        //==========================================================================================================================================================//
                                                           //OUTPUT LAYOUT//
        
        // Creating the output layout that will later be added at the bottom of the UI layout
        JPanel outputPanel = new JPanel(new BorderLayout());  // Panel with BorderLayout for the output area

        // Set a border around the output layout to act as a frame 
        Border panelBorder = BorderFactory.createLineBorder(Color.BLACK, 2);  //creating a black frame
        outputPanel.setBorder(panelBorder);                                   //applying it
        
        // Creating the output text area where the info will appear
        JTextArea outputArea = new JTextArea(15, 20);         //output text area with 15 rows and 20 columns as a size
        outputArea.setEditable(false);                        //make the output area read only (cannot be edited by the user)
        outputPanel.add(outputArea, BorderLayout.CENTER);     //adding it to the center (it will expand to the east until the outline of the program UI

        // Creating the area where the image will appear
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(300, 300));  //set the preferred size for the image (optional)
        outputPanel.add(imageLabel, BorderLayout.WEST);        //add the image label to the left side of the output panel
        
        //making a separator between the image and the text
        JSeparator verticalSeparator = new JSeparator(SwingConstants.VERTICAL); //creating the separator
        verticalSeparator.setPreferredSize(new Dimension(10, 1));               //set width for the separator
        verticalSeparator.setBackground(Color.BLACK);                           //make it black
        JPanel imageAndSeparatorPanel = new JPanel(new BorderLayout());         //create a container to hold the image and separator
        imageAndSeparatorPanel.add(imageLabel, BorderLayout.CENTER);            //add image to the left side
        imageAndSeparatorPanel.add(verticalSeparator, BorderLayout.EAST);       //add vertical separator to the right side of the image

        //add the container with image and separator to the outputPanel
        outputPanel.add(imageAndSeparatorPanel, BorderLayout.WEST);

        //add the text area to the output layout at the center (it will expand all the way to the right border)
        outputPanel.add(outputArea, BorderLayout.CENTER);
        
        //add the outputPanel to the bottom of the frame
        frame.add(outputPanel, BorderLayout.SOUTH);

        //============================================================================================================================================//
                                                   //BUTTONS AND FUNCTIONALITY//
        
        //creating the buttons layout that will be added to the top of the whole UI layout
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        //creating the generate ID button
        JButton generateButton = new JButton("Generate ID");
        buttonPanel.add(generateButton);

        //creating the upload image button
        JButton uploadButton = new JButton("Upload Image");
        buttonPanel.add(uploadButton);
        
        //creating the reset button
        JButton resetButton = new JButton("Reset");
        buttonPanel.add(resetButton);

        //adding all the buttons
        frame.add(buttonPanel, BorderLayout.NORTH);

        //THIS PART ACTIVATES WHEN THE "Upload Image" BUTTON IS PRESSED
        uploadButton.addActionListener(e -> {
        	//selecting the image
            JFileChooser fileChooser = new JFileChooser();              //creates an object that has the ability to make us select an image
            fileChooser.setDialogTitle("Select an Image");              //the text written at the top left of the image selector
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));         //filters the chosen files so that only files with these extensions can be opened
            int result = fileChooser.showOpenDialog(frame);             //saves whether the user clicked select or cancel into the int result
            if (result == JFileChooser.APPROVE_OPTION) {                // checks if the user clicked select
                File selectedFile = fileChooser.getSelectedFile();      //saves the image that the user selected
                imagePath = selectedFile.getAbsolutePath();             //saves the path into a string

                //display the selected image in the imageLabel
                ImageIcon icon = new ImageIcon(imagePath);              //creates an object that contains the selected image
                Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);  //resizes the image to fit perfectly within the image frame
                imageLabel.setIcon(new ImageIcon(image));               //sets the new resized image in it's place
            }
        });        
        
        //THIS PART ACTIVATES WHEN THE "Generate ID" BUTTON IS PRESSED
        generateButton.addActionListener(e -> {
            try {
                //collects all the data that were entered in their text areas (null if nothing was added)
                String name = nameField.getText().trim();
                String gender = genderField.getText().trim();
                String religion = religionField.getText().trim();
                String governorate = governorateComboBox.getSelectedItem().toString();
                String birthdate = birthdateField.getText().trim();
                String occupation = occupationField.getText().trim();

                //creates a new ID only if there is no saved ID (this makes it so that if we edit the current ID the unique ID number is not changed until we save)
                if (originalID == null) {
                    originalID = new ID_generator(name, gender, religion, governorate, birthdate, occupation, imagePath);
                }

                //display the generated ID in the output area
                outputArea.setText(originalID.toString());

                //create the option window where you can choose to edit or save or cancel, and saves the result in an int
                int choice = JOptionPane.showOptionDialog(
                        frame,
                        "Are you sure the ID is correct?\nDo you want to save it?",
                        "Confirm ID",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Save", "Edit", "Cancel"},
                        "Save"
                );

                //checks if the user chose edit or save or cancel
                if (choice == JOptionPane.YES_OPTION) {          //if the user picked save
                	
                	// Update the originalID object using setters with the edited data
                    originalID.setName(nameField.getText().trim());
                    originalID.setGender(genderField.getText().trim());
                    originalID.setReligion(religionField.getText().trim());
                    originalID.setGovernorate(governorateComboBox.getSelectedItem().toString());
                    originalID.setBirthdate(birthdateField.getText().trim());
                    originalID.setOccupation(occupationField.getText().trim());
                    // After confirming the edit, update the output area with the new ID details
                    outputArea.setText(originalID.toString());                	
                	
                    IDs_File.saveID(originalID);                  //save the original ID into the csv file
                    JOptionPane.showMessageDialog(frame, "ID Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);           //displays a message to let the user know that the ID was saved
                    originalID = null;                           //deletes the saved ID so that it can create a unique ID number if we generate a new ID using the same data
                    generateButton.setText("Generate new ID");   //resets the Generate ID name so that the user knows that the next ID will have a new unique ID
                } 
                else if (choice == JOptionPane.NO_OPTION) {      //if the user picked edit
                    JOptionPane.showMessageDialog(frame, "You can edit the details now.", "Edit ID", JOptionPane.INFORMATION_MESSAGE);     //display the message to let the user know that he can now edit the data while not creating a new unique ID
                    generateButton.setText("Confirm Edit");      //changing the generate ID button text to let the user know that he is in editing mode
                 }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        //THIS PART ACTIVATES WHEN THE "Reset" BUTTON IS PRESSED
        resetButton.addActionListener(e -> {
            // Clear all input fields and reset the image
            nameField.setText("");
            genderField.setText("");
            religionField.setText("");
            governorateComboBox.setSelectedIndex(0);
            occupationField.setText("");
            birthdateField.setText("");
            imageLabel.setIcon(null);
            imageLabel.setText("No Image Uploaded");
            outputArea.setText("");
            //deletes the current saved image path and ID  (deleting the ID so that a new unique ID is formed if we enter any new inputs)
            imagePath = "";
            originalID = null;
        });        
        //display the Program 
        frame.setVisible(true);
    }
}

