import Graph.Graph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Main extends Application {
    private FileInputStream inputStream;
    private byte [] data;
    private Graph graph = new Graph();

    public static void main(String args[]){
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("WAV", "*.wav")
        );

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) {
            System.exit(1);
        }
        wavDisplay(file, primaryStage);

        primaryStage.show();
    }

    private void wavDisplay(File file, Stage primaryStage) throws IOException {
        inputStream = new FileInputStream(file);
        data = new byte[(int)file.length()];
        inputStream.read(data);
        int bytePerSample = ((data[34] & 0xff) | (data[35] & 0xff)<<8) / 8;
        int sampleTotal = ((((data[40] & 0xff) | (data[41] & 0xff)<<8) | (data[42] & 0xff)<<16) | (data[43] & 0xff)<<24) / bytePerSample;
        int[] amplitude = new int[sampleTotal];
        int maxValue = 0;

        switch (bytePerSample) {
            case 1:
                for (int i = 0; i < sampleTotal; i++) {
                    amplitude[i] = data[44 + i];
                    if (amplitude[i] > 128) {
                        amplitude[i] = amplitude[i] - 256;
                    }
                    if (maxValue < amplitude[i]) {
                        maxValue = amplitude[i];
                    }
                }
                break;

            case 2:
                for (int i = 0; i < sampleTotal; i++) {
                    amplitude[i] = (data[44 + i * 2] & 0xff) | (data[45 + i * 2] & 0xff)<<8;
                    if (amplitude[i] > 32768) {
                        amplitude[i] = amplitude[i] - 65536;
                    }
                    if (maxValue < amplitude[i]) {
                        maxValue = amplitude[i];
                    }
                }
                break;
        }

        for (int i = 0; i < amplitude.length; i++) {
            amplitude[i] = amplitude[i] * 500 / maxValue;
        }

        primaryStage.setTitle(".wav file");
        HBox root = new HBox(15);
        Text totalSample = new Text();
        totalSample.setText("Total Sample: " + sampleTotal);
        Text maximumValue = new Text();
        maximumValue.setText("Maximum Value: " + maxValue);
        root.getChildren().addAll(graph, totalSample, maximumValue);

        int interval = amplitude.length / 1000;
        int j = 0;
        for (int i = 0; i < 1000; i++) {
            for (; j < i * interval; j++) {
                if (amplitude[j] < 500 && amplitude[j] > -500) {
                    drawPixel(i, amplitude[j]);
                }
            }
        }


        Scene scene  = new Scene(root,graph.getWidth() + 350,graph.getHeight());
        primaryStage.setScene(scene);
    }

    private void drawPixel(int i, int amplitude) {
        if (amplitude > 0) {
            for (int y = 0; y < amplitude; y++) {
                graph.setPixel(i, y + 500, Color.BLUE.getRGB());
            }
        } else if (amplitude < 0) {
            for (int y = 0; y > amplitude; y--) {
                graph.setPixel(i, y + 500, Color.BLUE.getRGB());
            }
        }
    }
}
