package floor.planner.util.jogl.raytracer;

import floor.planner.util.jogl.objects.Color;

public class PPMImageFormatter {

    public static String write(Color[][] image, RayTraceTask task) {
        StringBuilder builder = new StringBuilder();
        // P3 means colors are in ASCII
        builder.append("P3\n");
        // add width and height of image
        builder.append(String.format("%d %d\n", image[0].length, image.length));
        // 255 for max color
        builder.append("255\n");

        // render RGB triplets
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                // write each value as RGB triplets
                builder.append(image[i][j].toRGBTripletString());
                task.updateProgress(task.workDone++);
            }
        }

        return builder.toString();
    }

    public static String write(Color[][] image, RayTraceTask task, int samplesPerPixel) {
        StringBuilder builder = new StringBuilder();
        // P3 means colors are in ASCII
        builder.append("P3\n");
        // add width and height of image
        builder.append(String.format("%d %d\n", image[0].length, image.length));
        // 255 for max color
        builder.append("255\n");

        // render RGB triplets
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                // write each value as RGB triplets
                builder.append(image[i][j].toRGBTripletString(samplesPerPixel));
                task.updateProgress(task.workDone++);
            }
        }

        return builder.toString();
    }
}
