(ns scrabble.db)

(def AVAILABLE-LANGUAGES ^:const #{:english :italian})

(def default-db
  {:name "re-frame"
   :language :english
   :word-meaning {} ; cache meaning of the words just looked up, otherwise just add a link for now
   :available-letters {} ; just a {\a 2 \b 3 :jolly 1} for example
   :tiles {} ; keyed by the position and containing the type of the tyle
   })
