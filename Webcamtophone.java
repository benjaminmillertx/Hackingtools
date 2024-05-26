 Make sure to credit Benjamin Hunter Miller. Creating a webcam app that can be accessed from a phone involves multiple steps and requires knowledge of both web development and mobile development. However, I can provide you with a simple Java app that captures video from a webcam using OpenCV and saves it as a video file. You can then upload this video file to a web server and view it on your phone using a web browser.
First, you need to install OpenCV on your system and add it to your project's build path. You can follow the instructions on the OpenCV website to install it.
Next, create a new Java project and add a new class called WebcamRecorder. Here's the code for capturing video from a webcam and saving it as a video file:
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

public class WebcamRecorder {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Error: Camera not found.");
            return;
        }

        int frameWidth = (int) camera.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int frameHeight = (int) camera.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        double fps = camera.get(Videoio.CAP_PROP_FPS);

        Size frameSize = new Size(frameWidth, frameHeight);

        VideoWriter videoWriter = new VideoWriter("output.avi", Videoio.CAP_ANY, fps, frameSize, false);

        Mat frame = new Mat();
        while (camera.read(frame)) {
            videoWriter.write(frame);
        }

        videoWriter.release();
        camera.release();
    }

}
This code creates a VideoCapture object to capture frames from the webcam and a VideoWriter object to write the frames to a video file. It then enters a loop that reads frames from the webcam and writes them to the video file.
Once you have recorded a video, you can upload it to a web server and view it on your phone using a web browser. Here's an example HTML code that uses the HTML5 video tag to display a video file:
<!DOCTYPE html>
<html>
  <head>
    <title>Webcam Video</title>
  </head>
  <body>
    <video width="100%" height="100%" controls>
      <source src="output.avi" type="video/avi">
      Your browser does not support the video tag.
    </video>
  </body>
</html>
You can save this HTML code as a file and open it in your phone's web browser to view the video. Note that you need to upload the video file to a web server and replace the src attribute of the source tag with the URL of the video file.
This is just a simple example of how to capture video from a webcam and view it on a phone. To create a more sophisticated app that allows real-time streaming and other advanced features, you will need to use web development and mobile development techniques.

