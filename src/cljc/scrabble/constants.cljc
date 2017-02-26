(ns scrabble.constants)

(def ^:const MAX-TILES
  "Maxmium number of tiles to consider"
  10)

(def ^:const MAX-LETTERS
  "Maximum numbers of letters to play"
  7)

(def ^:const AVAILABLE-LANGUAGES
  "All the available languages"
  #{:english :italian})

(def ^:const DICT-FILES
  "Dictionary"
  {:english "resources/american-english"})

(def ^:const POINTS
  "Points per letter and per language"
  {
   :italian
   {}

   :english
   {1 [\a \e \i \o \r \s \t]
    2 [\d \l \n \u]
    3 [\g \h \y]
    4 [\b \c \f \m \p]
    5 [\k \w \v]
    8 [\x]
    10 [\j \q \z]}})

(defn- keyed-points [language]
  "Simple structural transformation to make it easier to play with points"
  (let [subsets
        (for [p (language POINTS)]
          (let [[weight letters] p]
            (for [l letters]
              {l weight})))]
    (into {} (flatten subsets))))

(def ^:const KEYED-POINTS
  (zipmap AVAILABLE-LANGUAGES (map keyed-points AVAILABLE-LANGUAGES)))
