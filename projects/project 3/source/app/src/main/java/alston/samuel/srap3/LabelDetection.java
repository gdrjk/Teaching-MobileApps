package alston.samuel.srap3;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Samuel on 2/26/2018.
 */

public class LabelDetection extends Activity{
    //the vision object used to pass info back and forth to Google Vision API
    private Vision vision;
    private Image encodedImage;
    private Map<String, Float> labelMap;
    private static final String LABEL_DETECTION = "LABEL_DETECTION";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCAlxdQBvhRZ5IcCYFEi7pAoVUpE_LBaDo";
    private Bitmap bitmap;

    public static Map<String, Float> annotateImage(byte[] imageBytes) throws IOException {
        // Construct the Vision API instance
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        VisionRequestInitializer initializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY);
        Vision vision = new Vision.Builder(httpTransport, jsonFactory, null)
                .setVisionRequestInitializer(initializer)
                .build();

        // Create the image request
        AnnotateImageRequest imageRequest = new AnnotateImageRequest();
        Image img = new Image();
        img.encodeContent(imageBytes);
        imageRequest.setImage(img);

        // Add the features we want
        Feature labelDetection = new Feature();
        labelDetection.setType(LABEL_DETECTION);
        labelDetection.setMaxResults(MAX_LABEL_RESULTS);
        imageRequest.setFeatures(Collections.singletonList(labelDetection));

        // Batch and execute the request
        BatchAnnotateImagesRequest requestBatch = new BatchAnnotateImagesRequest();
        requestBatch.setRequests(Collections.singletonList(imageRequest));
        BatchAnnotateImagesResponse response = vision.images()
                .annotate(requestBatch)
                // Due to a bug: requests to Vision API containing large images fail when GZipped.
                .setDisableGZipContent(true)
                .execute();

        return convertResponseToMap(response);
    }

    /*
    public Map<String, Float> makeRequest(byte[] imageBytes) throws IOException{
        //Send encodedImage to google to get labels and scores back in map

        bitmap = getIntent().getParcelableExtra("bitmap");
        //get vision ready to make request
        vision = createVisionBuilder().build();
        printMap(labelMap);

        //convert bitmap to Image
        encodedImage = encodePhoto();

        //Set desired feature to make request
        Feature desiredFeature = new Feature();
        desiredFeature.setType(labelDetection);

        //prepare request
        AnnotateImageRequest request = new AnnotateImageRequest();
        request.setImage(encodedImage);
        request.setFeatures(Arrays.asList(desiredFeature));

        //Google expects batch of images because it does several images at once.
        BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
        batchRequest.setRequests(Arrays.asList(request));

        //execute request

        try{
            BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();
            return convertResponseToMap(batchResponse);
        } catch(IOException e){
            Toast.makeText(getApplicationContext(), "That didn't work: "+e.toString(), Toast.LENGTH_LONG).show();
        }

        //handle bad response return
        return null;
    }
*/
    //Google method modified by SRA
    private static Map<String, Float> convertResponseToMap(BatchAnnotateImagesResponse response) {

        // Convert response into a readable collection of annotations
        Map<String, Float> annotations = new HashMap<>();
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                annotations.put(label.getDescription(), label.getScore());
            }
        }
        return annotations;
    }

    private Vision.Builder createVisionBuilder(){
        //create a vision builder for a new vision
        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyCAlxdQBvhRZ5IcCYFEi7pAoVUpE_LBaDo"));

        return visionBuilder;
    }

    private Image encodePhoto(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] photoData = stream.toByteArray();
        Image inputImage = new Image();
        return inputImage.encodeContent(photoData);
    }
}
