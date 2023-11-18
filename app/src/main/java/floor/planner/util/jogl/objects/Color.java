package floor.planner.util.jogl.objects;

public class Color {
    private float red;
    private float green;
    private float blue;

    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float getRed() {
        return this.red;
    }
    public void setRed(float val) {
        this.red = val;
    }

    public float getGreen() {
        return this.green;
    }
    public void setGreen(float val) {
        this.green = val;
    }

    public float getBlue() {
        return this.blue;
    }
    public void setBlue(float val) {
        this.blue = val;
    }

    public String toRGBTripletString() {
        // scale rgb values from 0.0 to 1.0 to be from 0 to 255
        int r = (int) (this.red * 255.999);
        int g = (int) (this.green * 255.999);
        int b = (int) (this.blue * 255.999);

        return String.format("%d %d %d \n", r, g, b);
    }
}
