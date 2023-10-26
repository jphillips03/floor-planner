package floor.planner.services;

import floor.planner.models.Floor;
import floor.planner.models.FloorPlan;

public class FloorPlanService {

    private FloorService floorService = new FloorService();

    public FloorPlan create(String text) {
        FloorPlan plan = new FloorPlan();
        String[] rows = text.split("\n");

        // parse and set dimensions from first row of text
        String[] dimensionsText = rows[0].split(" ");
        plan.setDimensions(
            Integer.parseInt(dimensionsText[1]),
            Integer.parseInt(dimensionsText[0]),
            Integer.parseInt(dimensionsText[2])
        );

        // parse each floor
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
        return plan;
    }
}
