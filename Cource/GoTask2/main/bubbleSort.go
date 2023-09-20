package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func Swap(sli []int, pos int) {
	temp := sli[pos+1]
	sli[pos+1] = sli[pos]
	sli[pos] = temp
}

func BubbleSort(sli []int) {
	n := len(sli)
	wasSwapped := true

	for wasSwapped {
		wasSwapped = false
		for i := 0; i < n-1; i++ {
			if sli[i] > sli[i+1] {
				Swap(sli, i)
				wasSwapped = true
			}
		}
	}
}

func SplitStrToI(str string) []int {
	strSplitParts := strings.Split(str, " ")

	ints := make([]int, 0)
	for _, s := range strSplitParts {
		i, e := strconv.Atoi(strings.TrimSpace(s))
		if e != nil {
			continue
		}
		ints = append(ints, i)
	}

	return ints
}

func GetInputString() string {
	scanner := bufio.NewScanner(os.Stdin)

	fmt.Print("Enter sequence of integers, separated with spaces: ")
	scanner.Scan()
	input := scanner.Text()
	return input
}

func GetUserSlice() []int {
	inputString := GetInputString()
	return SplitStrToI(inputString)
}

func main() {
	sli := GetUserSlice()
	BubbleSort(sli)
	fmt.Println(sli)
}
