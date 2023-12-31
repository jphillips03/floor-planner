package floor.planner.util.objects.obj3d;

import com.jogamp.opengl.GL2;

import java.util.List;

import floor.planner.constants.MaterialType;
import floor.planner.util.math.Color;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Vector;
import floor.planner.util.objects.DrawableElement;
import floor.planner.util.raytracer.Aabb;
import floor.planner.util.raytracer.intersectable.Intersectable;
import floor.planner.util.raytracer.material.Dielectric;
import floor.planner.util.raytracer.material.Isotropic;
import floor.planner.util.raytracer.material.Lambertian;
import floor.planner.util.raytracer.material.Material;
import floor.planner.util.raytracer.material.Metal;
import floor.planner.util.raytracer.texture.SolidColor;

public abstract class DrawableElement3D extends Intersectable implements DrawableElement {
    private float[] specularColor = { 1f, 1f, 1f, 1f };

    protected Color color;
    protected Material mat;

    public Aabb boundingBox;
    public float[] materialColor = { 0.5f, 0.5f, 0.5f, 1.0f };
    public float[] shininess = { 5.0f }; // this is low apparently...

    public void setColor(Color c) {
        this.color = c;
        if (this.mat instanceof Dielectric) {
            this.mat = new Dielectric(1.5); // glass
        } else if (this.mat instanceof Isotropic) {
            this.mat = new Isotropic(this.color);
        } else if (this.mat instanceof Lambertian) {
            this.mat = new Lambertian(new SolidColor(c));
        } else {
            this.mat = new Metal(this.color, 0);
        }
    }

    public float[] getSpecular() {
        return this.specularColor;
    }
    public void setSpecular(float[] c) {
        this.specularColor = c;
    }

    public MaterialType getMaterialType() {
        if (this.mat instanceof Dielectric) {
            return MaterialType.DIELECTRIC;
        } else if (this.mat instanceof Isotropic) {
            return MaterialType.ISOTROPIC;
        } else if (this.mat instanceof Lambertian) {
            return MaterialType.LAMBERTIAN;
        } else {
            return MaterialType.METAL;
        }
    }
    public void setMaterialType(MaterialType type) {
        if (type.equals(MaterialType.DIELECTRIC)) {
            this.mat = new Dielectric(1.5); // glass
        } else if (type.equals(MaterialType.ISOTROPIC)) {
            this.mat = new Isotropic(this.color);
        } else if (type.equals(MaterialType.LAMBERTIAN)) {
            this.mat = new Lambertian(new SolidColor(this.color));
        } else {
            this.mat = new Metal(this.color, 0);
        }
    }

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
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, this.color.getFloatValues(), 0);
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
