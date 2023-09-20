package main

import (
	"fmt"
	"strings"
)

type Animal interface {
	Eat()
	Move()
	Speak()
}

type Cow struct {
	name string
}
type Snake struct {
	name string
}
type Bird struct {
	name string
}

func (cow Cow) Eat() {
	fmt.Println("grass")
}
func (cow Bird) Eat() {
	fmt.Println("grass")
}
func (snake Snake) Eat() {
	fmt.Println("mice")
}

func (cow Cow) Move() {
	fmt.Println("walk")
}
func (cow Bird) Move() {
	fmt.Println("fly")
}
func (snake Snake) Move() {
	fmt.Println("slither")
}

func (cow Cow) Speak() {
	fmt.Println("moo")
}
func (cow Bird) Speak() {
	fmt.Println("peep")
}
func (snake Snake) Speak() {
	fmt.Println("hsss")
}

func unifyString(input string) string {
	input = strings.TrimSpace(input)
	input = strings.ToLower(input)
	return input
}

func createAnimal(animals *map[string]Animal) {
	var animalName string

	fmt.Scan(&animalName)

	var animalType string

	fmt.Scan(&animalType)

	animalType = unifyString(animalType)

	var animalToAdd Animal

	switch animalType {
	case "cow":
		animalToAdd = Cow{animalName}
	case "snake":
		animalToAdd = Snake{animalName}
	case "bird":
		animalToAdd = Bird{animalName}
	default:
		fmt.Printf("No support of %s animal type\n", animalType)
		return
	}
	(*animals)[animalName] = animalToAdd
	fmt.Println("Created it!")
}

func execQuery(animals map[string]Animal) {

	var animalName string

	fmt.Scan(&animalName)

	currentAnimal, p := animals[animalName]

	if !p {
		fmt.Println("You do not have an animal with this name!")
		return
	}

	var activity string

	fmt.Scan(&activity)

	activity = unifyString(activity)

	switch activity {
	case "eat":
		currentAnimal.Eat()
	case "move":
		currentAnimal.Move()
	case "speak":
		currentAnimal.Speak()
	default:
		fmt.Printf("This animal is not able to %s\n", activity)
	}
}

func main() {
	fmt.Println("Type command, animal and request or X to exit")
	animals := make(map[string]Animal)
	for {
		fmt.Print("> ")

		var command string

		fmt.Scan(&command)

		command = unifyString(command)

		if command == "x" {
			return
		}
		if command == "newanimal" {
			createAnimal(&animals)
		} else if command == "query" {
			execQuery(animals)
		}

	}
}
