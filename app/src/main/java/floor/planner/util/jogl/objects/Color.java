package floor.planner.util.jogl.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.util.math.Interval;
import floor.planner.util.math.Vector;

public class Color {
    private static Logger logger = LoggerFactory.getLogger(Color.class);

    private float red;
    private float green;
    private float blue;
    private Vector color;

    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.color = new Vector(new double[]{ red, green, blue });
    }

    public Color(float[] color) {
        this.red = color[0];
        this.green = color[1];
        this.blue = color[2];
        this.color = new Vector(new double[]{ this.red, this.green, this.blue });
    }

    public Color(Vector color) {
        this.color = color;
        this.red = (float) color.getX();
        this.green = (float) color.getY();
        this.blue = (float) color.getZ();
    }

    public float getRed() {
        return this.red;
    }
    public void setRed(float val) {
        this.red = val;
        this.color.setValues(new double[] { this.red, this.green, this.blue });
    }

    public float getGreen() {
        return this.green;
    }
    public void setGreen(float val) {
        this.green = val;
        this.color.setValues(new double[] { this.red, this.green, this.blue });
    }

    public float getBlue() {
        return this.blue;
    }
    public void setBlue(float val) {
        this.blue = val;
        this.color.setValues(new double[] { this.red, this.green, this.blue });
    }

    public Vector getColor() {
        return this.color;
    }
    public void setColor(Vector v) {
        this.red = (float) v.getX();
        this.green = (float) v.getY();
        this.blue = (float) v.getZ();
        this.color.setValues(v.getValues());
    }

    public String toRGBTripletString() {
        // scale rgb values from 0.0 to 1.0 to be from 0 to 255
        int r = (int) (this.red * 255.999);
        int g = (int) (this.green * 255.999);
        int b = (int) (this.blue * 255.999);

        return String.format("%d %d %d \n", r, g, b);
    }

    public String toRGBTripletString(int samplesPerPixel) {
        double red = this.red;
        double green = this.green;
        double blue = this.blue;

        // Divide the color by the number of samples
        double scale = 1.0 / (double) samplesPerPixel;
        red *= scale;
        green *= scale;
        blue *= scale;

        // Write the translated [0,255] value of each color component
        Interval intensity = new Interval(0.000, 0.999);
        int r = (int) (256 * intensity.clamp(red));
        int g = (int) (256 * intensity.clamp(green));
        int b = (int) (256 * intensity.clamp(blue));

        return String.format("%d %d %d \n", r, g, b);
    }
}
