package floor.planner.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;

public class FloorPlanService {
    private static final Logger logger = LoggerFactory.getLogger(FloorPlanService.class);

    private FloorService floorService = new FloorService();

    public FloorPlan create(String text) {
        FloorPlan plan = new FloorPlan();
        this.parseDimensions(plan, text);
        this.parseFloors(plan, text);
        return plan;
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
                floorStartsAt,
                plan.getHeight(),
                plan.getWidth()
            );
            plan.getFloors().add(floor);
        }
    }
}
