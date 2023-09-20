package main

import (
	"fmt"
	"math"
	"strconv"
	"strings"
)

func GenDisplaceFn(a float64, v0 float64, s0 float64) func(float64) float64 {
	return func(time float64) float64 {
		return 0.5*math.Pow(time, 2)*a + v0*time + s0
	}
}

func main() {
	var acceleration float64
	var velocity float64
	var displacement float64

	fmt.Print("Acceleration: ")
	fmt.Scan(&acceleration)

	fmt.Print("Velocity: ")
	fmt.Scan(&velocity)

	fmt.Print("Displacement: ")
	fmt.Scan(&displacement)

	fn := GenDisplaceFn(acceleration, velocity, displacement)

	for {
		var input string
		fmt.Print("Time (Or X for exit): ")
		fmt.Scan(&input)

		if strings.Contains(input, "X") {
			break
		}

		time, e := strconv.ParseFloat(input, 64)
		if e != nil {
			continue
		}
		displ := fn(time)
		fmt.Printf("S = %f\n", displ)
	}
}
