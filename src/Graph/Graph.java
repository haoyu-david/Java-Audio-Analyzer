package Graph;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.*;

public class Graph extends ImageView {
    private WritableImage image;
    private PixelWriter pixelWriter;
    private int width = 1000;
    private int height = 1000;

    public Graph() {
        image = new WritableImage(width, height);
        setImage(image);

        pixelWriter = image.getPixelWriter();
        createPanel();
    }

    public void setPixel(int x, int y, int color) {
        pixelWriter.setArgb(x, y, color);
    }

    private void createPanel() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                setPixel(i, j, Color.WHITE.getRGB());
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
