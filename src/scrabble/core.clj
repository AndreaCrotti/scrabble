(ns scrabble.core
  (:require [clojure.string :as str]
            [clojure.set :refer [intersection]]
            [clojure.math.combinatorics :as combo])
  (:gen-class))

(def dict-file "/usr/share/dict/american-english")

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
