package main

import (
	"fmt"
	"slices"
	"strconv"
)

func t4() {
	var sli = make([]int, 3)
	for {
		var input string
		fmt.Printf("Write an integer: ")
		fmt.Scan(&input)
		if input == "X" {
			break
		}

		var x, _ = strconv.Atoi(input)

		sli = append(sli, x)

		slices.Sort(sli)

		fmt.Println(sli)
	}
}
