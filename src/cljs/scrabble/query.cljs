(ns scrabble.query
  (:require [ajax.core :refer [GET]]))


(defn find-matches [language tiles letters]
  [["hello 10"]
   ["other" 20]])

#_(let [result (atom)]
  (def my-handler [response]
    (reset! result (str response))))

(def tmp-result (atom))

(defn my-handler [response]
  (reset! tmp-result (str response)))

(defn get-anagrams [word]
  (do
    (GET "http://localhost:3000/anagrams" {:params {:word word
                                                    :handler my-handler}})
    @tmp-result))

