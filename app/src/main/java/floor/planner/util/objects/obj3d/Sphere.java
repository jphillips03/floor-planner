package floor.planner.util.objects.obj3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL2;
import floor.planner.util.math.Color;
import floor.planner.util.math.Interval;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Random;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;
import floor.planner.util.raytracer.Aabb;
import floor.planner.util.raytracer.IntersectRecord;
import floor.planner.util.raytracer.Onb;
import floor.planner.util.raytracer.material.Material;
import floor.planner.util.raytracer.material.Metal;

public class Sphere extends DrawableElement3D {
    private static final Logger logger = LoggerFactory.getLogger(Sphere.class);
    //private static double radius = 0.5f; // 0.0625f;

    // private double x;
    // private double y;
    // private double z;
    private double radius;
    private Vector center;
    private Material mat;

    public Sphere(Vector pos, double radius, Material mat) {
        this.radius = radius;
        this.center = pos;
        this.mat = mat;

        Vector rvec = new Vector(radius, radius, radius);
        this.boundingBox = new Aabb(
            new Point3D(pos.subtract(rvec)),
            new Point3D(pos.add(rvec))
        );
    }

    public Sphere(double x, double y, double z, double radius, Material mat) {
        // this.x = x; // + 0.5f; // move to middle of tile
        // this.y = y; // + 0.5f; // move to middle of tile
        // this.z = z;
        this.radius = radius;
        this.center = new Vector(new double[]{ x, y, z });
        this.mat = mat;

        Vector rvec = new Vector(radius, radius, radius);
        this.boundingBox = new Aabb(
            new Point3D(this.center.subtract(rvec)),
            new Point3D(this.center.add(rvec))
        );
    }

    public Sphere(double x, double y, double z, double radius) {
        this.radius = radius;
        // move x, y coordinates to middle of tile (i.e. + 0.5)
        this.center = new Vector(new double[]{ x + 0.5, y + 0.5, z });
        this.materialColor = new float[]{ 0.0f, 0.7f, 0.0f, 1f };
        this.color = new Color(new Vector(this.materialColor));
        this.mat = new Lambertian(new Color(this.materialColor));

        Vector rvec = new Vector(radius, radius, radius);
        this.boundingBox = new Aabb(
            new Point3D(this.center.subtract(rvec)),
            new Point3D(this.center.add(rvec))
        );
    }

    public Vector getCenter() {
        return this.center;
    }

    @Override
    public void draw(GL2 gl) {
        // below code based on following https://stackoverflow.com/a/7687413
        int lats = 50;
        int longs = 50;
        gl.glColor3f(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
        for (int i = 0; i <= lats; i++) {
            double lat0 = Math.PI * (-0.5 + (double) (i - 1) / lats);
            double z0 = Math.sin(lat0);
            double zr0 = Math.cos(lat0);

            double lat1 = Math.PI * (-0.5 + (double) i / lats);
            double z1 = Math.sin(lat1);
            double zr1 = Math.cos(lat1);

            gl.glBegin(GL2.GL_QUAD_STRIP);
            for (int j = 0; j <= longs; j++) {
                double lng = 2 * Math.PI * (double) (j - 1) / longs;
                double x = Math.cos(lng);
                double y = Math.sin(lng);

                gl.glNormal3d(x * zr0, y * zr0, z0);
                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, materialColor, 0);
                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specularColor, 0);
                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess, 0);
                gl.glVertex3d((radius * x * zr0) + this.center.getX(), (radius * y * zr0) + this.center.getY(), (radius * z0) + this.center.getZ());

                gl.glNormal3d(x * zr1, y * zr1, z1);
                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, materialColor, 0);
                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specularColor, 0);
                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess, 0);
                gl.glVertex3d((radius * x * zr1) + this.center.getX(), (radius * y * zr1) + this.center.getY(), (radius * z1) + this.center.getZ());
            }
            gl.glEnd();
        }
    }

    @Override
    public boolean intersect(
        Ray r,
        Interval rayT,
        IntersectRecord rec
    ) {
        Vector oc = Vector.subtract(r.getOrigin(), this.center);
        double a = r.getDirection().lengthSqrd();
        double halfB = Vector.dot(oc, r.getDirection());
        double c = oc.lengthSqrd() - radius * radius;
        double discriminant = halfB * halfB - a * c;

        if (discriminant < 0) {
            return false;
        }

        // find nearest root that lies in acceptable range
        double sqrtD = (double) Math.sqrt((double) discriminant);
        double root = (-halfB - sqrtD) / a;
        if (!rayT.surrounds(root)) {
            root = (-halfB + sqrtD) / a;
            if (!rayT.surrounds(root)) {
                return false;
            }
        }

        rec.setT(root);
        rec.setP(r.at(rec.getT()));
        Vector outwardNormal = Vector.subtract(rec.getP(), this.center).divide(radius);
        rec.setFaceNormal(r, outwardNormal);
        this.getSphereUv(outwardNormal, rec);
        rec.setMaterial(this.mat);
        return true;
    }

    private void getSphereUv(Vector normal, IntersectRecord rec) {
        double theta = Math.acos(-normal.getY());
        double phi = Math.atan2(-normal.getZ(), normal.getX()) + MathUtil.PI;

        rec.setU(phi / (2 * MathUtil.PI));
        rec.setV(theta / MathUtil.PI);
    }

    @Override
    public double pdfValue(Point3D origin, Vector v) {
        /*
         * hit_record rec;
        if (!this->hit(ray(o, v), interval(0.001, infinity), rec))
            return 0;

        auto cos_theta_max = sqrt(1 - radius*radius/(center1 - o).length_squared());
        auto solid_angle = 2*pi*(1-cos_theta_max);

        return  1 / solid_angle;
         */
        IntersectRecord rec = new IntersectRecord();
        if (!this.intersect(new Ray(origin, v), new Interval(0.001, Double.POSITIVE_INFINITY), rec)) {
            return 0;
        }

        double cosThetaMax = Math.sqrt(
            1 - this.radius * this.radius / 
            (Vector.subtract(
                this.center,
                origin
            ).lengthSqrd())
        );
        double solidAngle = 2 * MathUtil.PI * (1 - cosThetaMax);
        return 1 / solidAngle;
    }

    public Vector random(Point3D p) {
        return this.random(p);
    }

    public Vector random(Vector v) {
        Vector direction = this.center.subtract(v);
        double distSqrd = direction.lengthSqrd();
        Onb uvw = new Onb();
        uvw.buildFromW(direction);
        return uvw.local(this.randomToSphere(radius, distSqrd));
    }

    private Vector randomToSphere(double radius, double distSqrd) {
        double r1 = Random.randomDouble();
        double r2 = Random.randomDouble();
        double z = 1 + r2 * (Math.sqrt(1 - radius * radius / distSqrd) - 1);

        double phi = 2 * MathUtil.PI * r1;
        double x = Math.cos(phi) * Math.sqrt(1 - z * z);
        double y = Math.sin(phi) * Math.sqrt(1 - z * z);
        return new Vector(x, y, z);
    }

    public Point3D getMidPoint() {
        return new Point3D(this.center);
    }
}
