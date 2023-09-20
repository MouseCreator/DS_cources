package main

import "fmt"

type Person struct {
	name string
	age  int16
}

func t3() {
	x := [3]int{1, 2, 3}
	for i, v := range x {
		fmt.Printf("At %d is %d\n", i, v)
	}

	arr := [...]string{"a", "b", "c", "d"}

	s1 := arr[1:3]

	arr[1] = "g"
	fmt.Println(s1)

	sli := make([]int, 10, 15)
	sli = append(sli, 5)
	fmt.Println(sli)

	p1 := new(Person)
	p1.name = "ne"
	p1.age = 2
	fmt.Println(*p1)

}
