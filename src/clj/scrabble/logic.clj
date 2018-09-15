(ns scrabble.logic
  (:require [clojure.core.logic :as l]
            [clojure.core.unify :as u]
            [clojure.core.logic.arithmetic :as la]
            [clojure.core.logic.fd :as fd]
            [clojure.math.combinatorics :as combo]))

(def words-list ["one" "two" "neo"])

(u/flatten-bindings)

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

(defn ntho
  [xs idx o]
  (l/conde
   ;;TODO: would be nice to add the extra conditions needed here eventually
   #_(l/and* (la/>= idx 0)
           (la/< idx (count xs)))

   (l/succeed (l/== o (nth xs idx)))))

(l/run 2 [q]
  (ntho [1 2 3] 1 q))

(def choices [\a \b \c])
(def word "h_ll_")

(l/run 2 [q]
  (ntho word 1 q))

(l/run 3 [q]
  (l/membero q choices))

(l/run 2 [q]
  (l/fresh [a b]
    (l/membero a choices)
    (l/membero b choices)
    ;;FIXME: doesn't work since ntho doesn't support LVar at the moment
    (ntho q 0 "h")
    (ntho q 1 a)
    (ntho q 4 b)))


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
      (l/membero q ["hello"])
      (l/everyg #(l/membero % letters) vars)
      (init vars hints))))

(comment
  (solver "h_ll_" "eo"))


;; would be nice to use unification directly but it doesn't work
(u/unify ['?first "Argento" "Baudo"]
         ["Dario" '?last '?very-last])

(u/subst '[(?a * ?x | 2) + (?b * ?x) + ?c]
         '{?c 3, ?b 4, ?x 5
           ?z (?a * 5 | 2)})

