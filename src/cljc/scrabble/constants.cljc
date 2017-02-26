(ns scrabble.constants)

(def ^:const MAX-TILES 10)
(def ^:const MAX-LETTERS 7)
(def ^:const AVAILABLE-LANGUAGES ^:const #{:english :italian})

(def ^:const DICT-FILES
  {:english "resources/american-english"})


(def ^:const POINTS
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
