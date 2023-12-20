package floor.planner.util.raytracer.texture;

import floor.planner.util.math.Point3D;
import floor.planner.util.objects.Color;

public class SolidColor implements Texture {
    private Color colorValue;

    public SolidColor(Color c) {
        this.colorValue = c;
    }

    public SolidColor(double red, double green, double blue) {
        this(new Color(red, green, blue));
    }

    public Color value(double u, double v, Point3D p) {
        return this.colorValue;
    }
}
