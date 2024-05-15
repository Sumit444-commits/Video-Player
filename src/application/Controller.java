package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class Controller implements Initializable{
    @FXML
    private AnchorPane pane;
    @FXML
    private Label lblPlayer;
    @FXML
    private MediaView videoScreen;
    @FXML
    private Button playBtn,pauseBtn,resetBtn,nextBtn,previousBtn,searchBtn;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar progress;
    @FXML
    private TextField searchBox;

    private Media media;
    private MediaPlayer mediaPlayer;
    private File directory;
    private File[] files;
    private ArrayList<File> videos;
    private int videoNumber;
    private int[] speed = {25,50,75,100,125,150,200};
    private Timer timer;
    private TimerTask task;
    private boolean running;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        videos = new ArrayList<File>();
        directory = new File("video");
        files = directory.listFiles();
        for (File file : files) {
            videos.add(file);
        }
        media = new Media(videos.get(videoNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        videoScreen.setMediaPlayer(mediaPlayer);
        lblPlayer.setText(videos.get(videoNumber).getName());


       for(int i=0;i<speed.length;i++){
        speedBox.getItems().add(Integer.toString(speed[i])+"%");
       }

       speedBox.setOnAction(this::changeSpeed);

       volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

        @Override
        public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
          mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
        }
        
       });
       progress.setStyle("-fx-accent : #00FF00;");
      
    }

    public void searchVideo(ActionEvent event){
        String searchValue = searchBox.getText();
       
     
        for(File file: videos){
            if (file.getName().equalsIgnoreCase(searchValue+".mp4")) {
                mediaPlayer.stop();
            if(running){
                cancelTimer();
            }
             media = new Media(file.toURI().toString());
             mediaPlayer = new MediaPlayer(media);
             videoScreen.setMediaPlayer(mediaPlayer);
             lblPlayer.setText(file.getName());
                break;
            }
        }
      
    }
    
    public void playMedia(){
        beginTimer();
        changeSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
        mediaPlayer.play();
    }
    public void pauseMedia(){
        cancelTimer();
        mediaPlayer.pause();
    }
    public void resetMedia(){
        progress.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0.0));
    }
    public void previousVideo(){
        if (videoNumber>0){
            videoNumber--;
            mediaPlayer.stop();
            if(running){
                cancelTimer();
            }
            media = new Media(videos.get(videoNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            videoScreen.setMediaPlayer(mediaPlayer);
            lblPlayer.setText(videos.get(videoNumber).getName());
            playMedia();
        } else{
            videoNumber=videos.size()-1;
            mediaPlayer.stop();
            if(running){
                cancelTimer();
            }
            media = new Media(videos.get(videoNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            videoScreen.setMediaPlayer(mediaPlayer);
            lblPlayer.setText(videos.get(videoNumber).getName());
            playMedia();
        }
    }
    public void nextVideo(){
        if (videoNumber<videos.size()-1) {
            videoNumber++;
            mediaPlayer.stop();
            if(running){
                cancelTimer();
            }
        media = new Media(videos.get(videoNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        videoScreen.setMediaPlayer(mediaPlayer);
        lblPlayer.setText(videos.get(videoNumber).getName());
        playMedia();
        }else{
            videoNumber=0;
            mediaPlayer.stop();
            if(running){
                cancelTimer();
            }
        media = new Media(videos.get(videoNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        videoScreen.setMediaPlayer(mediaPlayer);
        lblPlayer.setText(videos.get(videoNumber).getName());
        playMedia();
        }
    }

    public void changeSpeed(ActionEvent event){
        if (speedBox.getValue()==null) {
            mediaPlayer.setRate(1);
        }else{
        mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0,speedBox.getValue().length()-1))*0.01);
        }
    }

    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                progress.setProgress(current/end);

                if (current/end ==1) {
                    cancelTimer();
                }
            }
            
        };

        timer.scheduleAtFixedRate(task, 0,1000);
    }

    public void cancelTimer(){
        running=false;
        timer.cancel();
    }
}
