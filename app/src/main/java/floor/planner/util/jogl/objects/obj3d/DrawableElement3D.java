package floor.planner.util.jogl.objects.obj3d;

import com.jogamp.opengl.GL2;

import java.util.List;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.objects.DrawableElement;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public abstract class DrawableElement3D extends DrawableElement {
    public float[] materialColor = { 0.5f, 0.5f, 0.5f, 1.0f };
    public float[] specularColor = { 1f, 1f, 1f, 1f };
    public float[] shininess = { 5.0f }; // this is low apparently...
    public Color color;

    public void drawPolygon(GL2 gl, List<float[]> points) {
        gl.glColor3f(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
        gl.glBegin(GL2.GL_POLYGON);
        float[] normal = Vector.normal(
            points.get(0),
            points.get(1),
            points.get(2),
            points.get(3)
        );
        gl.glNormal3f(normal[0], normal[1], normal[2]);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, materialColor, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specularColor, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess, 0);
        for (float[] point : points) {
            gl.glVertex3f(point[0], point[1], point[2]);
        }
        gl.glEnd();
    }

    public abstract float intersect(Ray r);
}
