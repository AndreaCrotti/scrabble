(ns scrabble.logic
  (:require [clojure.core.logic :as l]
            [clojure.math.combinatorics :as combo]))

(def words-list ["one" "two" "neo"])

(l/run 2 [q]
  (l/== q 2))

;; permuteo seems to hang forever even with a small list when using run*


;; use fresh to create new names

;; we can not multiply x since for example it's a Lvar

(l/run 2 [q]
  (l/fresh [x y]
    (l/== [x 2] [1 y])
    (l/== q [x y])))

(defn my-appendo [l1 l2 o]
  (l/conde
   ((l/== l1 ()) (l/== l2 o))
   ((l/fresh [a d r]
      (l/conso a d l1)
      (l/conso a r o)
      (my-appendo d l2 r)))))

(l/run 2 [q]
  (my-appendo [1] q [1 2 3 5]))


;; 1. generate all anagrams of a word
;; 2. generate all anagrams given a word with some Holes and various possibilities
;;  Given for example H _ l l _
;;  and a choice of letters like [a, e, b, y]
;;  Find all the possible matching words, so we have to satisfy:
;;  - final generated word is in the dictionary (membero on a list of words)
;;  - fixed letters are in the right position (ntho?)
;;  - non fixed letters are in one of the holes
;;  - non fixed letters are used *only* once and are part ofa given list of letters

(l/run 2 [q]
  (l/membero q words-list))
