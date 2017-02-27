(ns scrabble.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [scrabble.views-test]))

(doo-tests 'scrabble.views-test)
