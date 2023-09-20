package main

import (
	"fmt"
	"sync"
)

type Chopstick struct {
	mutex sync.Mutex
}

type Philosopher struct {
	id       int
	leftCS   *Chopstick
	rightCS  *Chopstick
	timesEat int
}

type Host struct {
	philos      []Philosopher
	eatingLimit int
	eatingNow   int
	mutex       sync.Mutex
}

func (h *Host) allowsToEat() bool {
	return h.eatingNow < h.eatingLimit
}

func (h *Host) start() {
	h.mutex.Lock()
	h.eatingNow++
	h.mutex.Unlock()
}

func (h *Host) end() {
	h.mutex.Lock()
	h.eatingNow--
	h.mutex.Unlock()
}

func (c *Chopstick) getChopstick() {
	c.mutex.Lock()
}
func (c *Chopstick) releaseChopstick() {
	c.mutex.Unlock()
}

func (p *Philosopher) eat() {
	p.leftCS.getChopstick()
	p.rightCS.getChopstick()

	fmt.Println("starting to eat", p.id)
	p.timesEat++

	p.leftCS.releaseChopstick()
	p.rightCS.releaseChopstick()

	fmt.Println("finishing eating", p.id)
}

func (p *Philosopher) canEat(limit int) bool {
	return p.timesEat < limit
}

func philoEat(philosopher *Philosopher, host *Host, times int, wg *sync.WaitGroup) {
	for i := 0; i < times; {
		if host.allowsToEat() {
			host.start()
			philosopher.eat()
			host.end()
			i++
		}
	}
	wg.Done()
}

func hostAndStart(host *Host, mainWg *sync.WaitGroup) {
	philosophers := host.philos
	numPhilos := len(philosophers)

	wg := sync.WaitGroup{}
	wg.Add(numPhilos)

	for i := 0; i < numPhilos; i++ {
		go philoEat(&philosophers[i], host, 3, &wg)
	}
	wg.Wait()
	mainWg.Done()
}

func main() {
	const NumPhilos = 5

	var philosophers [NumPhilos]Philosopher
	var chopsticks [NumPhilos]Chopstick

	for i := 0; i < NumPhilos; i++ {
		chopsticks[i] = Chopstick{sync.Mutex{}}
	}
	for i := 0; i < NumPhilos; i++ {
		philosophers[i] = Philosopher{i + 1, &chopsticks[i], &chopsticks[(i+1)%NumPhilos], 0}
	}

	host := Host{philosophers[0:], 2, 0, sync.Mutex{}}

	mainWg := sync.WaitGroup{}
	mainWg.Add(1)

	go hostAndStart(&host, &mainWg)

	mainWg.Wait()

}
