package floor.planner.util.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Color extends Vector {
    private static Logger logger = LoggerFactory.getLogger(Color.class);

    private float red;
    private float green;
    private float blue;

    public Color(double red, double green, double blue) {
        super(red, green, blue);
        this.red = (float) red;
        this.green = (float) green;
        this.blue = (float) blue;
    }

    public Color(float red, float green, float blue) {
        super(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(float[] color) {
        super(new double[]{ color[0], color[1], color[2] });
        this.red = color[0];
        this.green = color[1];
        this.blue = color[2];
    }

    public Color(Vector color) {
        super(color);
        this.red = (float) color.getX();
        this.green = (float) color.getY();
        this.blue = (float) color.getZ();
    }

    public float getRed() {
        return this.red;
    }
    public void setRed(float val) {
        this.red = val;
        this.setValues(new double[] { this.red, this.green, this.blue });
    }

    public float getGreen() {
        return this.green;
    }
    public void setGreen(float val) {
        this.green = val;
        this.setValues(new double[] { this.red, this.green, this.blue });
    }

    public float getBlue() {
        return this.blue;
    }
    public void setBlue(float val) {
        this.blue = val;
        this.setValues(new double[] { this.red, this.green, this.blue });
    }

    public void setColor(Vector v) {
        this.red = (float) v.getX();
        this.green = (float) v.getY();
        this.blue = (float) v.getZ();
        this.setValues(v.getValues());
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

        // checks for NaN or infinite components, added in section 12.6 from
        // book 3 (so far nothing; maybe something in upstream code not right...)
        if (Double.isNaN(red) || Double.isInfinite(red)) {
            red = 0;
            logger.warn("Infinite or NaN red");
        }
        if (Double.isNaN(blue) || Double.isInfinite(blue)) {
            blue = 0;
            logger.warn("Infinite or NaN blue");
        }
        if (Double.isNaN(green) || Double.isInfinite(green)) {
            green = 0;
            logger.warn("Infinite or NaN green");
        }

        // Divide the color by the number of samples
        double scale = 1.0 / (double) samplesPerPixel;
        red *= scale;
        green *= scale;
        blue *= scale;

        red = this.linearToGamma(red);
        green = this.linearToGamma(green);
        blue = this.linearToGamma(blue);

        // Write the translated [0,255] value of each color component
        Interval intensity = new Interval(0.000, 0.999);
        int r = (int) (256 * intensity.clamp(red));
        int g = (int) (256 * intensity.clamp(green));
        int b = (int) (256 * intensity.clamp(blue));

        return String.format("%d %d %d \n", r, g, b);
    }

    private double linearToGamma(double linearComponent) {
        return Math.sqrt(linearComponent);
    }
}
