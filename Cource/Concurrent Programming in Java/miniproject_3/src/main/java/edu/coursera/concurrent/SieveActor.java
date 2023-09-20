package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 *
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determine the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     *
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        final SieveActorActor sieveActor = new SieveActorActor(3);
        finish(() -> {
            for (int i = 3; i < limit; i += 2) {
                sieveActor.send(i);
            }
            sieveActor.send(-1);
        });

        int remainingAsPrimes = limit >>> 1;
        remainingAsPrimes += 1;
        SieveActorActor currentActor = sieveActor;
        while (currentActor != null) {
            remainingAsPrimes -= currentActor.countRejected;
            currentActor = currentActor.nextActor;
        }

        return remainingAsPrimes;
    }

    public static final class SieveCounter {
        private final int primesLimit;
        private final boolean[] removedNumbers;
        public SieveCounter(int primesNum) {
            this.primesLimit = primesNum;
            removedNumbers = new boolean[primesNum];
            if (primesLimit > 2) {
                removeObviousNotPrimes();
            }
        }

        private void removeObviousNotPrimes() {
            removedNumbers[0] = true;
            removedNumbers[1] = true;
        }

        public void removePrimeAt(int index) {
            if (removedNumbers[index]) {
                return;
            }
            removedNumbers[index] = true;
        }
        public int getPrimesCount() {
            return 0;
        }
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {

        private SieveActorActor nextActor = null;
        private final int myNumber;
        private int countRejected = 0;
        public SieveActorActor(int myNumber) {
            this.myNumber = myNumber;
        }
        /**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            Integer integer = (Integer) msg;
            if (integer < 1) {
                if (nextActor != null) {
                    nextActor.send(msg);
                }
                return;
            }
            if (isDivisor(integer)) {
                countRejected++;
                return;
            }
            if (nextActor == null) {
                nextActor = new SieveActorActor(integer);
            } else {
                nextActor.send(msg);
            }

        }

        private boolean isDivisor(Integer integer) {
            return integer % myNumber == 0;
        }

    }
}
