# APPLICATION NAME
This application is called photoapp. You launch it and choose to open the camera or to open the gallery. You select an image from either camera or gallery and then you may edit the image, the RGB values can be edited here, as well as brightness and adding a reflection of the image is as easy as clicking the reflection button. Click reset to revert to original. Click save to save a copy of the image.

## System Design 
This will run on all Android devices running API 17 or higher.

## Usage
The usage of this application is easy and restrictive. There are two activities, ActivityMain, and EditPhoto. The first activity has three buttons,"Open Camera", "Edit Image" and "Open gallery". EditPhoto has four sliders four red, green, blue and brightness. EditPhoto also contains a reflection button, reset

Open camera: This will open your phones camera. Take an image as you normally would, and accept a photo when you are satisfied. That image will then appear in lower resolution on the main menu.

Open gallery: Thiss will ask you what method you would like to look for a photo, your mileage may vary depending on device. Select your choice of photo gallery and choose a single image. By selecting the image you will be brought back to the main menu and your image will be displayed, at a fixed cap on resolution.

Edit Image: This will only launch the edit menu if you have selected an image to edit. Once clicked, it launches the edit photo activity.

Edit Photo Activity:

In the EditPhoto activity each value for RGB has a slider labeled accordingly and as you move the slider, a value will appear next to the label. Once you let go of the slider the image will be redrawn with the new filter according to your settings.

Brightness Works the same as the RGB sliders, except each time you release the slider, it will make the image brighter if above the mid point and darker if it is below the mid point.

Reflection will take the image, without changing the filters and add a reflection to the image. Changing the filters will get rid of the reflection.

Save should save the image to the device. Currently, there is an error that is not allowing the permissions set in the manifest to take effect. This may not work.
