(ns scrabble.constants)

(def default-language :english)

(def jolly-char
  "Symbol to use for the jolly letter"
  \*)

(def max-tiles
  "Maxmium number of tiles to consider"
  10)

(def max-letters
  "Maximum numbers of letters to play"
  7)

(def available-languages
  "All the available languages"
  #{:english :italian})

;; this is really just a defaultdict, so a dictionary
;; which computes the result
(def dict-files
  "Dictionary"
  {:english "resources/english.txt"
   :italian "resources/italian.txt"})

(def points
  "Points per letter and per language"
  {
   :italian
   {}

   :english
   {0 [jolly-char]
    1 [\a \e \i \o \r \s \t]
    2 [\d \l \n \u]
    3 [\g \h \y]
    4 [\b \c \f \m \p]
    5 [\k \w \v]
    8 [\x]
    10 [\j \q \z]}})

(def languages
  "Available languages"
  (keys points))

(defn points-to-alphabet [language]
  ;; an easier way to do this maybe?
  (apply concat (vals (language points))))

(def alphabet
  "Valid alphabet for each language, derived from the points definition"
  (zipmap languages
          (map points-to-alphabet languages)))

(defn- gen-keyed-points [language]
  "Simple structural transformation to make it easier to play with points"
  (let [subsets
        ;; can probably be simplified slightly
        (for [p (language points)]
          (let [[weight letters] p]
            (for [l letters]
              {l weight})))]

    (into {} (flatten subsets))))

(def keyed-points
  (zipmap available-languages (map gen-keyed-points available-languages)))
