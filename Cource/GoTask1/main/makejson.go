package main

import (
	"encoding/json"
	"fmt"
)

func t11() {
	var userMap = make(map[string]string)

	var name string
	var address string
	fmt.Printf("Enter your name: ")
	fmt.Scan(&name)
	fmt.Printf("Enter your address: ")
	fmt.Scan(&address)

	userMap["name"] = name
	userMap["address"] = address

	js, _ := json.Marshal(userMap)

	fmt.Println(string(js))
}
