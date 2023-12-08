package floor.planner.util.jogl.material;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.pdf.Pdf;
import floor.planner.util.math.Ray;

public class ScatterRecord {
    public Color attenuation;
    public Pdf pdf;
    public boolean skipPdf;
    public Ray skipPdfRay;

    public ScatterRecord(Color att) {
        this.attenuation = att;
    }

    public ScatterRecord(Color att, Pdf pdf) {
        this(att);
        this.pdf = pdf;
        this.skipPdf = false;
        // skipPdfRay will be null...
    }

    public ScatterRecord(Color att, Pdf pdf, boolean skip, Ray r) {
        this(att, pdf);
        this.skipPdf = skip;
        this.skipPdfRay = r;
    }
}
