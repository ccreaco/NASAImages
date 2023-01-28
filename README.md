# NASA Images - Android Application


# Introduction
The application allows users to download images from the NASA Images of the Day website (https://www.nasa.gov/multimedia/imagegallery/iotd.html).   
Users are able to select any date from a fragment, which will then retrieve the image from the website, and display it on the application.   
Once the image is downloaded, it will display the title of the image, the date select, the URL and the HD URL to the image on NASA website.   
Users have the option to save the image to their application folder for later view.    
Users can review their saved pictures on their device, which saves the description, title, image and date. The image can also be deleted from the application folder.    

# Tools and Technology
The programming language selected for this project is Java.   

# Software
Android Studio IDE was used to build this project.   

# Components Utilized
The application utilizes the following components:     
- Android fragments and drawers for opening different pages within the application. 
- Connection to MySQL database to insert the data from the images such as the date, title, URL, etc.  
- Dialogs and toast messages to alert the users of their selection within the application, such as confirmation a picture was saved.  
- SharedPreferences to save the users information and use it on other pages withint the application. 
- AsyncTask to open an Http URL Connection to the NASA website to read the image information into a JSON object to be utilized with the application.  
- Android UI elements such as Buttons, ListViews, ImageViews, Adapters, Toolbars, Menus, Snackbars, Progress Bars, etc. 
- Application supports both English and French. 
