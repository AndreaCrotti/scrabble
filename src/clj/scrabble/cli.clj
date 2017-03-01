(ns scrabble.cli
  (:require [clojure.data.json :as json]
            [clojure.tools.cli :refer [parse-opts]]
            [scrabble.core :as core]))


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
        variants (sort-by :value (core/anagrams word))]
    (println (json/write-str variants))))
