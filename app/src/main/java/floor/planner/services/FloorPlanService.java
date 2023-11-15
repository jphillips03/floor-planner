package floor.planner.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.constants.AspectRatio;
import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;
import floor.planner.util.FileUtil;
import floor.planner.util.jogl.objects.obj2d.ClippingPlane;

public class FloorPlanService {
    private static final Logger logger = LoggerFactory.getLogger(FloorPlanService.class);

    private FloorService floorService = new FloorService();

    public FloorPlan create(String text) {
        FloorPlan plan = new FloorPlan();
        this.parseDimensions(plan, text);
        this.initClippingPlane(plan);
        this.parseFloors(plan, text);
        return plan;
    }

    public FloorPlan create(int numFloors, int height, int width) {
        FloorPlan plan = new FloorPlan(numFloors, height, width);
        this.initClippingPlane(plan);
        for (int i = 0; i < numFloors; i++) {
            Floor floor = this.floorService.createEmptyFloor(height, width, i);
            plan.getFloors().add(floor);
        }
        return plan;
    }

    private void initClippingPlane(FloorPlan plan) {
        logger.info("Aspect Ratio: " + AspectRatio.X.value + ":" + AspectRatio.Y.value);
        ClippingPlane plane = new ClippingPlane(
            (float) plan.getWidth() / 2 - AspectRatio.X.value,
            (float) plan.getWidth() / 2 + AspectRatio.X.value,
            (float) plan.getHeight() / 2 - AspectRatio.Y.value,
            (float) plan.getHeight() / 2 + AspectRatio.Y.value
        );
        plan.setClippingPlane(plane);
    }

    private void parseDimensions(FloorPlan plan, String text) {
        String[] rows = text.split("\n");
        String[] dimensionsText = rows[0].split(" ");
        plan.setDimensions(
            Integer.parseInt(dimensionsText[1]),
            Integer.parseInt(dimensionsText[0]),
            Integer.parseInt(dimensionsText[2])
        );
    }

    private void parseFloors(FloorPlan plan, String text) {
        for (int i = 0; i < plan.getFloorNumbers(); i++) {
            // first floor starts at second row (i + 1), rest of the floors
            // start at i + floor height
            int floorStartsAt = i == 0 ? i + 1 : i + plan.getHeight();
            Floor floor = floorService.create(
                text,
                i,
                floorStartsAt,
                plan.getHeight(),
                plan.getWidth()
            );
            plan.getFloors().add(floor);
        }
    }

    public void save(File file, FloorPlan plan) {
        FileUtil.save(file, plan.toString());
    }
}
