(ns scrabble.constants)

(def DEFAULT-LANGUAGE :english)

(def JOLLY-CHAR
  "Symbol to use for the jolly letter"
  \*)

(def MAX-TILES
  "Maxmium number of tiles to consider"
  10)

(def MAX-LETTERS
  "Maximum numbers of letters to play"
  7)

(def AVAILABLE-LANGUAGES
  "All the available languages"
  #{:english :italian})

;; this is really just a defaultdict, so a dictionary
;; which computes the result
(def DICT-FILES
  "Dictionary"
  {:english "resources/english.txt"
   :italian "resources/italian.txt"})

(def POINTS
  "Points per letter and per language"
  {
   :italian
   {}

   :english
   {0 [JOLLY-CHAR]
    1 [\a \e \i \o \r \s \t]
    2 [\d \l \n \u]
    3 [\g \h \y]
    4 [\b \c \f \m \p]
    5 [\k \w \v]
    8 [\x]
    10 [\j \q \z]}})

(def LANGUAGES
  "Available languages"
  (keys POINTS))

(defn points-to-alphabet [language]
  ;; an easier way to do this maybe?
  (apply concat (vals (language POINTS))))

(def ALPHABET
  "Valid alphabet for each language, derived from the points definition"
  (zipmap LANGUAGES

          (map points-to-alphabet LANGUAGES)))

(defn- keyed-points [language]
  "Simple structural transformation to make it easier to play with points"
  (let [subsets
        ;; can probably be simplified slightly
        (for [p (language POINTS)]
          (let [[weight letters] p]
            (for [l letters]
              {l weight})))]

    (into {} (flatten subsets))))

(def KEYED-POINTS
  (zipmap AVAILABLE-LANGUAGES (map keyed-points AVAILABLE-LANGUAGES)))
