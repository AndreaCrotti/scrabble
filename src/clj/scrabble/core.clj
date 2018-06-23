;; TODO: replace the hardcoded :english with a language argument

(ns scrabble.core
  (:require [clojure
             [set :refer [intersection]]
             [string :as str]]
            [clojure.math.combinatorics :as combo]
            [scrabble.constants :as const]))

(def mult-word {:dw 2 :tw 3})
(def mult-char {:ol 1 :dl 2 :tl 3})

(defn clean-words [words]
  (->> words
       (map str/lower-case)
       (map #(re-find #"\w+" %)) ;; remove all the genitive forms
       (filter (complement nil?))
       (into #{})
       (sort)
       (into [])))

(defn load-words
  "Load all the words"
  [filename]
  (->> filename
       slurp
       str/split-lines
       clean-words))

(def ALL-WORDS
  ;; TODO: should this be a constant instead?
  {:english (load-words (:english const/DICT-FILES))
   :italian (load-words (:italian const/DICT-FILES))})

(defn char-value
  "Return the value of the given char"
  [ch]
  (let [val-type (:val ch)
        multiplier (get mult-char val-type 1)]
    ;; should not ve english only
    (* (get (:english const/KEYED-POINTS) (:letter ch) 0) multiplier)))

(defn word-to-charpos
  "From a word return a mapping between a char and its position"
  [word]
  (into {} (map-indexed (fn [idx v] {idx v}) word)))

(defn word-value
  "Compute the value of the given word given the tile configuration"
  ([word]
   (apply + (map (fn [v] (get (:english const/KEYED-POINTS) v)) word)))

  ([tiles word]
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
     (* word-multiplier partial-sum))))

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

(defn anagrams
  "Return all possible valid words from the given letters"
  [letters & {:keys [min-size language max-size]
              :or {min-size 1 language const/DEFAULT-LANGUAGE}}]

  (apply clojure.set/union
         (for [size (range (or max-size (count letters)) min-size -1)]
           (let [perms-words (perms-with-length letters size)]
             (intersection (set perms-words) (set (language ALL-WORDS)))))))

(defn best-words
  [tiles word & {:keys [language] :or {language const/DEFAULT-LANGUAGE}}]
  "Return all the possible evaluations of the anagrams given the tiles"
  (let [patt (re-pattern (str "^" (clojure.string/replace tiles #"\d" "[a-z]") "$"))
        tiles-obj (str-to-tile tiles)
        ans (anagrams word)
        filtered-ans (filter some? (map #(re-matches patt %) ans))
        valued (map (partial word-value tiles-obj) filtered-ans)
        res (zipmap filtered-ans valued)]

    (reverse (sort-by second res))))
