package floor.planner.util.objects.obj3d;

import com.jogamp.opengl.GL2;

import java.util.List;

import floor.planner.util.math.Color;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Vector;
import floor.planner.util.objects.DrawableElement;
import floor.planner.util.raytracer.Aabb;
import floor.planner.util.raytracer.Intersectable;

public abstract class DrawableElement3D extends Intersectable implements DrawableElement {
    public Aabb boundingBox;
    public float[] materialColor = { 0.5f, 0.5f, 0.5f, 1.0f };
    public float[] specularColor = { 1f, 1f, 1f, 1f };
    public float[] shininess = { 5.0f }; // this is low apparently...
    public Color color;

    public void drawPolygon(GL2 gl, List<float[]> points) {
        gl.glColor3f(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
        gl.glBegin(GL2.GL_POLYGON);
        double[] normal = Vector.normal(
            MathUtil.floatToDoubleArray(points.get(0)),
            MathUtil.floatToDoubleArray(points.get(1)),
            MathUtil.floatToDoubleArray(points.get(2)),
            MathUtil.floatToDoubleArray(points.get(3))
        );
        gl.glNormal3f((float) normal[0], (float) normal[1], (float) normal[2]);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, materialColor, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specularColor, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess, 0);
        for (float[] point : points) {
            gl.glVertex3f(point[0], point[1], point[2]);
        }
        gl.glEnd();
    }

    @Override
    public Aabb boundingBox() {
        return this.boundingBox;
    }

    public abstract Point3D getMidPoint();
}
