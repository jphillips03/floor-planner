gradle := ./gradlew

build:
	${gradle} build

refresh-dependencies: 
	${gradle} --refresh-dependencies

run:
	${gradle} run
