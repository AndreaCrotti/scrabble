(ns scrabble.logic
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.arithmetic :as la]
            [clojure.core.logic.fd :as fd]
            [clojure.math.combinatorics :as combo]))

(def words-list ["one" "two" "neo"])

;; 1. generate all anagrams of a word
;; 2. generate all anagrams given a word with some Holes and various possibilities
;;  Given for example H _ l l _
;;  and a choice of letters like [a, e, b, y]
;;  Find all the possible matching words, so we have to satisfy:
;;  - final generated word is in the dictionary (membero on a list of words)
;;  - fixed letters are in the right position (ntho?)
;;  - non fixed letters are in one of the holes
;;  - non fixed letters are used *only* once and are part ofa given list of letters

(defn init
  [vars hints]
  (println "Vars = " vars
           "hints = " hints)
  (if (seq vars)
    (let [hint (first hints)]
      (l/all
       (if (= \_ hint)
         (l/== (first vars) hint)
         l/succeed)

       (init (next vars) (next hints))))

    l/succeed))

(defn solver
  [hints letters]

  (let [vars (repeatedly (count hints) l/lvar)]
    (l/run 1 [q]
      ;; the order does matter in the amount of backtracking that gets done
      
      (l/== q vars)
      (l/membero q [(seq "hello")])
      #_(l/everyg #(l/membero % letters) vars)
      (init vars hints))))

(comment
  (solver "h_ll_" "hello"))
