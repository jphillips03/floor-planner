gradle := ./gradlew

build:
	${gradle} clean
	${gradle} build

refresh-dependencies: 
	${gradle} --refresh-dependencies

run: build
	${gradle} run
