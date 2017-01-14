(ns scrabble.core
  (:require [clojure.string :as str]
            [clojure.set :refer [intersection]]
            [clojure.math.combinatorics :as combo])
  (:gen-class))

(def dict-file "/usr/share/dict/american-english")

(def points
  {1 [\a \e \i \o \r \s \t]
   2 [\d \l \n \u]
   3 [\g \h \y]
   4 [\c \f \m \p \q]
   5 [\k \w]
   8 [\x]
   10 [\j \q \z]})

(def keyed-points
  (let [subsets
        (for [p points]
          (let [[weight letters] p]
            (for [l letters]
              {l weight})))]
    (into {} (flatten subsets))))

(defn word-value [word]
  (apply + (map (fn [v] (get keyed-points v)) word)))

(def all-words
  (->> dict-file
       slurp
      (str/split-lines)
      (map str/lower-case)))

(defn possibilities [letters]
  "Return all possible valid words from the given letters"
  (let [perms (combo/permutations letters)
        perms-words (map str/join perms)]
    (intersection (set perms-words) (set all-words))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
