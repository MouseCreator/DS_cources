package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func t2() {

	scanner := bufio.NewScanner(os.Stdin)

	fmt.Print("Enter a line with spaces: ")

	scanner.Scan()

	var inputLine = scanner.Text()

	var s = strings.ToLower(inputLine)
	s = strings.TrimSpace(s)
	if strings.HasPrefix(s, "i") && strings.HasSuffix(s, "n") && strings.Contains(s, "a") {
		fmt.Printf("Found!")
	} else {
		fmt.Printf("Not Found!")
	}

}
