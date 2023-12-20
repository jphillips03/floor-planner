package floor.planner.util.raytracer.material;

import floor.planner.util.math.Ray;
import floor.planner.util.objects.Color;
import floor.planner.util.raytracer.pdf.Pdf;

public class ScatterRecord {
    public Color attenuation;
    public Ray scatterRay;
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

    public ScatterRecord(Color att, Pdf pdf, Ray scatterRay) {
        this(att, pdf);
        this.scatterRay = scatterRay;
    } 

    public ScatterRecord(Color att, Pdf pdf, boolean skip, Ray r) {
        this(att, pdf);
        this.skipPdf = skip;
        this.skipPdfRay = r;
    }
}
