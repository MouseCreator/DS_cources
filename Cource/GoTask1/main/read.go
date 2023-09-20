package main

import (
	"fmt"
	"io"
	"os"
)

type Name struct {
	fname string
	lname string
}

func main() {
	var filename string

	_, err2 := fmt.Scan(&filename)
	if err2 != nil {
		return
	}

	open, err := os.Open(filename)
	if err != nil {
		return
	}

	names := make([]Name, 0)

	bname := make([]byte, 20)
	separator := make([]byte, 1)
	lineSeparator := make([]byte, 2)
	for {
		read, err := open.Read(bname)
		if err != nil {
			return
		}
		if read < 20 {
			break
		}
		s := string(bname)

		read, err = open.Read(separator)
		if err != nil {
			return
		}
		if read < 1 {
			break
		}

		read, err = open.Read(bname)
		if err != nil {
			return
		}
		if read < 20 {
			break
		}
		d := string(bname)

		name := Name{s, d}
		names = append(names, name)

		read, err = open.Read(lineSeparator)

		if err != nil {
			if err == io.EOF {
				break
			}
		}

		if read < 1 {
			break
		}
	}

	for _, n := range names {
		fmt.Println(n)
	}
}
