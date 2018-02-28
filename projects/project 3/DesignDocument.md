# SRAp3
This application uses the Google Vision API to detect labels in an image from either phone gallery, or image taken in app. Users are prompted to give yes/no answer if labels found were in the picture in descending order of Google's certainty. I made this application to understand how to use such an API and to show my friends and family a power piece of software I was able to bend to my will. It is also quite fun to use an app which I have been interested in since an episode of the television show "Silicon Valley" poked fun at the concept of an app that could detect what an image was, but their app only detected if something was a hotdog, or if it wasn't a hotdog. Mine returns Googles' best guesses, which in my testing have been quite accurate.

## System Design 
This app targets SDK 19 and above. It will require a camera, access to the internet, and external write permission to store the high-res image that the users take to detect labels. Runs on Android 7+.

## Usage
Launching the app will take you to a blank screen with three buttons. They are: Open Camera, Open Gallery, Detect.
Once detect gets a response from google it launches an activity "Second Menu" to ask the user a yes/no question about the labels detected. Clicking the phone's back arrow from main menu quits the app. Clicking the back arrow from the second menu returns the user to the main menu.

###Main Menu

####open camera
Open Camera launches the phone's camera. Once you have taken a satisfactory picture and clicked the check mark you will return to the main menu with your image taken displayed on the screen. You may then properly "detect".

####Open Gallery
Open Gallery launches your selected image file selector. Once a single image is selected you return to the main menu and the selected image is displayed on the screen. You may then properly "detect".

####Detect
Before an image has been selected, this button will raise a toast prompting the user to select an image. Once an image has been selected, detect will display a circular progress bar to indicate that the app is processing the image and awaiting a response from Google Cloud. When the response comes in a new activity is launched to ask the user a series of questions about the labels detected by Google's Vision API.

###Second Menu

In this activity, the labels detected and their certainty of accuracy are displayed on the screen one at a time in descending order. There are two button below the display text: "yes", "no".

####yes

If a user clicks "yes" then the app will return to the Main Menu where their image they just detected will be, including a smug toast saying that the app thought that the image contained that label, then prompting the user to continue selecting images and detecting labels.

####no

If a user clicks "no" then the app will change the text to the label with the next highest certainty. If the app is out of labels for that image the activity quits and toasts the user saying it ran out of labels, then prompts the user to continue using the app.
