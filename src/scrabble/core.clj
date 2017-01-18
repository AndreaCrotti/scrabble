(ns scrabble.core
  (:require [clojure.string :as str]
            [clojure.data.json :as json]
            [clojure.set :refer [intersection]]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.math.combinatorics :as combo])
  (:gen-class))

(def dict-file "/usr/share/dict/american-english")

(def mult-word {:dw 2
                :tw 3})

(def mult-char {:dl 2
                :tl 3})

(def points
  {1 [\a \e \i \o \r \s \t]
   2 [\d \l \n \u]
   3 [\g \h \y]
   4 [\b \c \f \m \p]
   5 [\k \w \v]
   8 [\x]
   10 [\j \q \z]})

;; There should be an easier way to do this transformation
(def keyed-points
  (let [subsets
        (for [p points]
          (let [[weight letters] p]
            (for [l letters]
              {l weight})))]
    (into {} (flatten subsets))))

(defn word-value [word]
  (apply + (map (fn [v] (get keyed-points v)) word)))

(defn char-value [ch]
  "Return the value of the given char"
  (let [val-type (:val ch)
        multiplier (get mult-char val-type 1)]
    (* (get keyed-points (:letter ch) 0) multiplier)))

(defn word-to-charpos [word]
  (into {} (map-indexed (fn [idx v] {idx v}) word)))

(defn word-value-tiles [tiles word]
  (let [charpos (word-to-charpos word)
        to-fill-in (filter #(nil? (:letter %)) tiles)
        pre-filled (filter #(not (nil? (:letter %))) tiles)
        filled-in (for [tf to-fill-in]
                    (assoc tf :letter (get charpos (:pos tf))))
        partial-sum
        (+
         (apply + (map #(get keyed-points (:letter %)) pre-filled))
         (apply + (map char-value filled-in)))
        all-multipliers (map #(get mult-word % 1) (map :val filled-in))
        word-multiplier (if (empty? all-multipliers)
                          1
                          (apply max all-multipliers))]
    (* word-multiplier partial-sum)))

(defonce all-words
  (->> dict-file
       slurp
      (str/split-lines)
      (map str/lower-case)))

(defn possibilities [letters]
  "Return all possible valid words from the given letters"
  (let [perms (combo/permutations letters)
        perms-words (map str/join perms)
        valid-words (intersection (set perms-words) (set all-words))]
    (into []
          (for [vl valid-words]
            {:value (word-value vl) :word vl}))))


(def cli-options
  ;; An option with a required argument
  [["-w" "--word" "Word to analyze"
    :validate [#(pos? (count %))]]
   ["-h" "--help"]])


(defn -main
  [& args]
  (let [options (parse-opts args cli-options)
        word (nth (:arguments options) 0)
        variants (sort-by :value (possibilities word))]
    (println (json/write-str variants))))
