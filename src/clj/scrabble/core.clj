;; TODO: replace the hardcoded :english with a language argument

(ns scrabble.core
  (:gen-class)
  (:require [clojure
             [set :refer [intersection]]
             [string :as str]]
            [clojure.data.json :as json]
            [clojure.math.combinatorics :as combo]
            [clojure.tools.cli :refer [parse-opts]]
            [scrabble.constants :as const]))


(def mult-word {:dw 2 :tw 3})
(def mult-char {:ol 1 :dl 2 :tl 3})

(defn word-value [word]
  (apply + (map (fn [v] (get (:english const/KEYED-POINTS) v)) word)))

(defn char-value
  "Return the value of the given char"
  [ch]
  (let [val-type (:val ch)
        multiplier (get mult-char val-type 1)]
    (* (get (:english const/KEYED-POINTS) (:letter ch) 0) multiplier)))

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
         (apply + (map #(get (:english const/KEYED-POINTS) (:letter %)) pre-filled))
         (apply + (map char-value filled-in)))
        all-multipliers (map #(get mult-word % 1) (map :val filled-in))
        word-multiplier (if (empty? all-multipliers)
                          1
                          (apply max all-multipliers))]
    (* word-multiplier partial-sum)))

(def mult-char-to-keyword
  {\1 :ol
   \2 :dl
   \3 :tl
   \6 :dw
   \9 :tw})

;; another representation could be a simple comma separated list
;; of strings, with the symbols inside them
(defn str-to-tile
  "Convert a string representation of tiles to the list of maps"
  [tile-str]
  (vec
   (map-indexed
    (fn [idx v] (let [sym
                     (get mult-char-to-keyword v nil)
                     letter (when (nil? sym) v)]
                 {:pos idx :letter letter :val sym}))
    tile-str)))

(defn perms-with-length
  "Permutations by length"
  [letters size]
  (map str/join (set (map #(take size %) (combo/permutations letters)))))

(defonce all-words
  (->> (:english const/DICT-FILES)
       slurp
       (str/split-lines)
       (map str/lower-case)
       (into #{})
       (into [])))

(defn anagrams
  "Return all possible valid words from the given letters"
  ([letters]
   (anagrams letters (count letters)))

  ([letters max-size]
   (anagrams letters max-size 1))

  ([letters max-size min-size]
   (apply clojure.set/union
          (for [size (range max-size min-size -1)]
            (let [perms-words (perms-with-length letters size)]
              (intersection (set perms-words) (set all-words)))))))

(defn best-words
  [tiles word]
  "Return all the possible evaluations of the anagrams given the tiles"
  (let [patt (re-pattern (str "^" (clojure.string/replace tiles #"\d" "[a-z]") "$"))
        tiles-obj (str-to-tile tiles)
        ans (anagrams word)
        filtered-ans (filter some? (map #(re-matches patt %) ans))
        valued (map (partial word-value-tiles tiles-obj) filtered-ans)
        res (zipmap filtered-ans valued)]

    (reverse (sort-by second res))))

(defn permute-with-tiles [tiles letters]
  (let [empty-cells
        (filter #(nil? (:letter %)) tiles)
        filled-cells
        (filter #(not (nil? (:letter %))) tiles)
        max-length (count tiles)
        max-to-use (count empty-cells)]
    (for [an (anagrams letters max-to-use)]
      (let [letters-dict (map (fn [v] {:letter v}) an)]
        (concat filled-cells  (map merge empty-cells letters-dict))))))

(defn matches
  ([tiles letters]
   (matches tiles letters true))

  ([tiles letters only-existing]
   (let [perms (permute-with-tiles tiles letters)
         all-matches
         (into {}
               (for [word perms]
                 (let [w (str/join "" (map :letter word))]
                   {w (word-value-tiles tiles w)})))]
     (if only-existing
       (filter #(contains? all-words %) all-matches)
       all-matches))))

(def cli-options
  ;; An option with a required argument
  [["-w" "--word" "Word to analyze"
    :default "friend"
    :validate [#(pos? (count %)) "Must not be an empty word"]]
   ["-t", "--tiles", "tiles configuration"]
   ["-h" "--help"]])

(defn -main
  [& args]
  (let [options (parse-opts args cli-options)
        word (nth (:arguments options) 0)
        variants (sort-by :value (anagrams word))]
    (println (json/write-str variants))))
