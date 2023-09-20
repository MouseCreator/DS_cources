package main

import (
	"fmt"
	"strconv"
)

func t() {
	var p int
	var n int
	for n == 0 {
		fmt.Printf("Enter a float value: ")
		num, err := fmt.Scan(&p)
		n = num
		if err != nil {
			err.Error()
		}
	}
	fmt.Printf(strconv.Itoa(p))

}
