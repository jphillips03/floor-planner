# FloorPlanner

The FloorPlanner is a basic CAD program for creating and managing floor plans in 2D and 3D. Additionally users can generate a ray traced image of the current view in 3D. It is based on a set of programming assignments I had in one of my graduate school classes a long time ago in a city not to far away...

## Table of Contents

* [Prerequisites](#prerequisites)
* [Features](#features)
* [Screenshots](#screenshots)
* [App Structure](#app-structure)
* [Author](#author)
* [Acknowledgements](#acknowledgements)

## Prerequisites

The application is written using Java 17 and JOGL. The user interface is developed using JavaFX, and the 2D and 3D views of the floor plan are rendered using JOGL. The application is built using Gradle. Below is the list of technologies and the version used when developing the app.

* Java version 17.0.9-oracle
* Gradle version 8.0.2
* JavaFX version 17.0.9
* JOGL version 2.5.0

### Optional Software

I utilize [SDKMAN!](https://sdkman.io) in my development environment for managing different versions of SDKs. I have included a `.sdkmanrc` file in the repo, so if you have SDKMAN! installed and have Java version 17.0.9-oracle installed, then you can run `sdk env` at the root of the application to initialize the correct Java version for the app (assuming this is not the default version of Java you are using in your environment).

## Ray Tracer

The ray trace code is based on the [_Ray Tracing in One Weekend_](https://raytracing.github.io/books/RayTracingInOneWeekend.html) series. I worked through each of the books and implemented most of the functionality from there. I did skip some of the features contained in the book, like motion and volumes (i.e. smoke and fog) since these are not needed in the floor plan. Some of the worlds used in the series are available to generate from the "Ray Tracer" menu item. Selecting anything other than "3D Rendering" will generate the corresponding ray traced image from the series.

> NOTE: This application was developed and tested on RHEL 9, and as such I've only included native JOGL files needed for linux. I do not expect this application to work as is on Windows or Mac since I have not included the native JOGL files needed for those operating systems.

## Features

The goal of the project is to allow users to create and manage basic floor plans in 2D, view and move around the floor plan in 3D, and ultimately generate a ray traced image of the current view in 3D. A floor plan has a width and height (represented as a matrix for each floor), can be 1 or more floors, and each floor can have a set of basic elements assigned to the indexes in the matrix associated with the floor.

### Create/Load and Save Floor Plan

Users can create new or load existing floor plans using the corresponding options in the `File` menu. They can also save their changes at any time from the same menu.

### 2D View

The initial view of a floor plan (after one is created or loaded) will display a grid in 2D of the first floor (or the only floor if the plan is just 1 floor). When in 2D users can click any cell in the grid to change the element type in that cell as well as the material type using the corresponding drop down fields in the top right corner of the screen. The selected cell in the grid will "highlight" blue (instead of the default white).

If there is more than 1 floor, users can switch between floors using the `Floor` drop down menu in the `Floor` panel on the right side of the app.

> NOTE: Changing the material type will only effect the ray traced image.

### 3D View

To switch between 2D and 3D simply click the corresponding radio button in the `Floor` panel on the right side of the app. Once in 3D users can move around in the floor plan using the arrow buttons on their keyboard. They can also change the angle (up/down) of movement using the `pg up` and `pg dn` buttons. There is a `Re-Center` button which will bring the user back to the "bottom" of the floor plan.

### Color and Lighting

Users can change the color for each element in the floor plan using the color sliders in the right pane (below the element and material type drop downs). Color changes will be reflected in 2D, 3D, and the ray traced image.

> NOTE: I do not currently save colors for the elements since that was not part of the original requirements for the project, and I am trying to stick with most of the original requirements (for now...).

Changing the lighting works similar to changing colors, however it only effects the 3D view.

### Ray Traced Images

A ray traced image can be rendered from the current view in 3D by selecting `3D Rendering` in the `Ray Trace` menu. Users can also generate images from the Ray Tracing In One Week series by selecting any of the options in the `RTIOW Series Scenes` menu (found below the `3D Rendering` menu item).

A progress indicator will display when the ray tracer is running to generate an image, and will automatically close when complete. This is a nice addition I added from the original project I wrote in grad school as it does give me an idea of how long the ray tracing process might take. I did not have that in my original project, and would just have to guess how long the ray tracing process would take (which was not very fun). Some ray traced images can take a while to render depending on settings used and the hardware where the application is running.

> NOTE: Ray traced images are saved with a ppm extension, based on code from RTIOW series.

## Screenshots

Below is a screenshot of the JavaFX UI showing a loaded floor plan in 2D. The UI uses a standard BorderPane layout with a toolbar in the north pane, the JOGL viewport in the center, material and light controls in the right pane, and the `Re-Center` button for re-centering in the 3D view in the bottom pane.

![2D view](images/2d-view.png)
*2D View*

And the same floor plan in 3D.

![3D view](images/3d-view.png)
*3D View*

## Ray Traced Images

The following sections include sample ray traced images generated by the FloorPlanner application.

### Floor Plan

Below is a sample ray traced image of the floor plan showin in the screen shots above. The sphere is metal, and you can see a reflection of the scene in front of it (albeit very small; should probably create a better floor plan to show this...).

![Ray Trace view](images/ray-trace-view.png)
*Ray Trace View*

### Ray Tracing in One Weekend Series

Below are images rendered from the Ray Tracing in One Weekend Series.

![lots of spheres](images/lots-of-spheres.png)
*[_Ray Tracing in One Weekend_](https://raytracing.github.io/books/RayTracingInOneWeekend.html) Final Image*

![cornell box lambertian](images/cornell-box-lambertian.png)
*[_Ray Tracing: The Next Week_](https://raytracing.github.io/books/RayTracingTheNextWeek.html) Standard Cornell Box*

![cornell box metal](images/cornell-box-metal.png)
*[_Ray Tracing: The Rest of Your Life_](https://raytracing.github.io/books/RayTracingTheRestOfYourLife.html) Cornell Box with reflections*

## App Structure

The main source code for the application is found in `src/main`. This directory is split into `java` and `resources` directories. All of the code is located under `java`, while `resources` contains CSS styles and FXML views associated with JavaFX controllers defined in the code. 

### Java Code

The heart of the app starts at `src/main/java/floor/planner`. This directory is split into multiple directories in my attempt to organize the code base. Coming from a lot of web frameworks I structured my app very similar to those that I've worked with. If you have worked with Groovy/Grails, Ruby on Rails, or maybe even some Java Spring projects you should be somewhat familiar with the layout. The directory structure is as follows.

```bash
src/main/
├── java/floor/
│   └── planner/
│       ├── config
│       ├── constants
│       ├── controllers
│       ├── listeners
│       ├── models
│       ├── services
│       └── util/
│           ├── jogl
│           ├── math
│           ├── objects
│           └── raytracer
└── resources
```

The `config` directory contains all code needed to configure and intialize the app, included JavaFX and JOGL. The `constants` directory contains `enum` values used in the application. `controllers` contains all of the JavaFX controllers associated with the views for the app. `listeners` contains custom JavaFX change listeners for the fields defined in the right pane of the application for controlling things like element type and material, colors, and lighting. The `models` directory contains the main models used by the application (i.e. camera, floor, floor plan, and light). The `services` directory contains classes for handling the business logic needed for the application. Things like initializing a floor plan, or initializing scenes used by the RTIOW series when generating ray traced images.

The `util` directory is further broken down to separate out classes associated with JOGL, ray tracing, different types of objects used in the floor plan (2D and 3D), as well as a general math directory which contains custom implementations of things like Vector, Matrix, Point3D, etc. I'm not going to go through each of the directories here as they are futher split themselves, but I think most of the directories and structure here is fairly self explanatory if you are familiar with any of the technologies used.

## Author

* **[Jonathan Phillips]** - (https://github.com/jphillips03)

## Acknowledgements

This project has been a long time coming (in the sense that I've been meaning to work on it for a while now). The original project this was based on was probably one of the hardest programming challenges I tackled at the time. I never got the original program working 100%, and that has plagued me over the years, which is why I've wanted to take up the challenge again and make a better version. Not to try to get credit for it, but for my own satisfaction. While this project is still not perfect (there are definitely still some bugs), it is far better than the original one I submitted. I am much happier with the GUI layout, the functionality, structure, and overall code.

Also, I'm pretty sure I would never have completed the ray tracing portion of the project had I not stumbled upon the [_Ray Tracing in One Weekend Series_](https://raytracing.github.io/). That was an invaluable resource with great explanations and full code examples. I did have to translate the code from C++ to Java, which was somewhat painful as it's been over 20 years since I've looked at any C++ code. But since the original project I wrote was in Java, I felt the need to re-write it in Java again. Big thanks to the authors of the series though, for making a great set of books, and for making them free and available to all!
